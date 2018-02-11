package uk.co.ribot.androidboilerplate.ui.singin;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import net.glxn.qrgen.android.QRCode;
import net.glxn.qrgen.core.image.ImageType;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.ui.base.BaseActivity;
import uk.co.ribot.androidboilerplate.ui.main.MainActivity;
import uk.co.ribot.androidboilerplate.util.AuthAccountManager;
import uk.co.ribot.androidboilerplate.util.AuthConstants;

public class SignInActivity extends BaseActivity implements SignInMvpView {

    private static final String LOG = SignInActivity.class.getName();

    @Inject SignInPresenter mSignInPresenter;
    @Inject Application application;
    @Inject AuthAccountManager authAccountManager;

    @BindView(R.id.authKeyValue) TextView authKeyValue;
    @BindView(R.id.authQrCode) ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.i("SignInActivity");
        activityComponent().inject(this);
        setContentView(R.layout.singin_activity);
        ButterKnife.bind(this);

        mSignInPresenter.attachView(this);

        String token = authAccountManager.getToken();
        if (token != null && !token.isEmpty()) {
            Log.d(LOG,"successful");
            authKeyValue.setText(token);
            Bitmap qrCodeImage = QRCode.from(token).to(ImageType.JPG).bitmap();
            imageView.setImageBitmap(qrCodeImage);
        } else {
            Log.d(LOG,"token is Empty");
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSignInPresenter.detachView();
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
