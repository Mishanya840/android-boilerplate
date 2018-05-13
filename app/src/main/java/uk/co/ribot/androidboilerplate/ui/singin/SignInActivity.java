package uk.co.ribot.androidboilerplate.ui.singin;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import net.glxn.qrgen.android.QRCode;
import net.glxn.qrgen.core.image.ImageType;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Optional;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;
import timber.log.Timber;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.remote.AuthResource;
import uk.co.ribot.androidboilerplate.ui.base.BaseActivity;
import uk.co.ribot.androidboilerplate.ui.main.MainActivity;
import uk.co.ribot.androidboilerplate.util.AuthTokenHolder;

public class SignInActivity extends BaseActivity implements SignInMvpView {

    private static final String LOG = SignInActivity.class.getName();

    @Inject
    SignInPresenter mSignInPresenter;

    @Inject
    Application application;

    @Inject
    AuthTokenHolder authTokenHolder;

    @Inject
    AuthResource authResource;

    @BindView(R.id.authKeyValue) TextView authKeyValue;
    @BindView(R.id.authQrCode) ImageView imageView;

    List<Disposable> subscribes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.i("SignInActivity");
        activityComponent().inject(this);
        setContentView(R.layout.singin_activity);
        ButterKnife.bind(this);

        mSignInPresenter.attachView(this);

        @SuppressLint("WifiManagerLeak") WifiManager manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        String macAddress = info.getMacAddress();

        authKeyValue.setText(macAddress);
        Bitmap qrCodeImage = QRCode.from(macAddress).to(ImageType.JPG).withSize(500,500).bitmap();
        imageView.setImageBitmap(qrCodeImage);

        Disposable subscribe = authResource.getToken(macAddress)
                .subscribeOn(Schedulers.io())
                .retryWhen(throwableObservable -> throwableObservable.flatMap(throwable -> {
                    if (throwable instanceof HttpException) {
                        if (((HttpException) throwable).code() == 400) {
                            return io.reactivex.Observable.empty();
                        }
                    }
                    return io.reactivex.Observable.error(throwable);
                }))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(authToken -> {
                    authTokenHolder.setAuthToken(authToken);
                    startActivity(MainActivity.getStartIntent(getApplicationContext(), true));
                });

        subscribes.add(subscribe);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSignInPresenter.detachView();
        for (Disposable subscribe : subscribes) {
            subscribe.dispose();
        }
    }

    /**
     * Return an Intent to start this Activity.
     */
    public static Intent getStartIntent(Context context) {
        return new Intent(context, SignInActivity.class);
    }

    @Override
    public void showSignInSuccessful() {

    }
}
