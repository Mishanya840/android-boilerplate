package uk.co.ribot.androidboilerplate.data;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;
import retrofit2.Response;
import ru.macroplus.webplatform.dto.task.TaskDto;
import timber.log.Timber;
import uk.co.ribot.androidboilerplate.BoilerplateApplication;
import uk.co.ribot.androidboilerplate.util.AndroidComponentUtil;
import uk.co.ribot.androidboilerplate.util.NetworkUtil;
import uk.co.ribot.androidboilerplate.util.RetryWithDelay;
import uk.co.ribot.androidboilerplate.util.RxUtil;

public class AuthService extends Service {

    @Inject DataManager mDataManager;
    private Disposable mDisposable;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, AuthService.class);
    }

    public static boolean isRunning(Context context) {
        return AndroidComponentUtil.isServiceRunning(context, AuthService.class);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        BoilerplateApplication.get(this).getComponent().inject(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        Timber.i("Starting auth...");

        if (!NetworkUtil.isNetworkConnected(this)) {
            Timber.i("Auth canceled, connection not available");
//            AndroidComponentUtil.toggleComponent(this, SyncService.SyncOnConnectionAvailable.class, true);
            stopSelf(startId);
            return START_NOT_STICKY;
        }

        RxUtil.dispose(mDisposable);
        mDataManager.isAuth()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(0, 2))
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(@NonNull Boolean isAuth) {
                        Timber.w( "Next. isAuth - %s", isAuth);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.w(e, "Error auth.");
                        if (e instanceof HttpException) {

                        }
//                        stopSelf(startId);
                    }

                    @Override
                    public void onComplete() {
                        Timber.i("Auth successfully!");
                        stopSelf(startId);
                    }
                });

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mDisposable != null) mDisposable.dispose();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
