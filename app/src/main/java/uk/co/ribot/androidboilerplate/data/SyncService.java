package uk.co.ribot.androidboilerplate.data;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.IBinder;

import java.net.SocketTimeoutException;

import javax.inject.Inject;

import io.katharsis.repository.response.HttpStatus;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;
import ru.macroplus.webplatform.dto.task.TaskDto;
import timber.log.Timber;
import uk.co.ribot.androidboilerplate.BoilerplateApplication;
import uk.co.ribot.androidboilerplate.ui.singin.SignInActivity;
import uk.co.ribot.androidboilerplate.util.AndroidComponentUtil;
import uk.co.ribot.androidboilerplate.util.NetworkUtil;
import uk.co.ribot.androidboilerplate.util.RetryWithDelay;
import uk.co.ribot.androidboilerplate.util.RxUtil;

public class SyncService extends Service {

    @Inject DataManager mDataManager;
    private Disposable mDisposable;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, SyncService.class);
    }

    public static boolean isRunning(Context context) {
        return AndroidComponentUtil.isServiceRunning(context, SyncService.class);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        BoilerplateApplication.get(this).getComponent().inject(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        Timber.i("Starting sync...");

        if (!NetworkUtil.isNetworkConnected(this)) {
            Timber.i("Sync canceled, connection not available");
            AndroidComponentUtil.toggleComponent(this, SyncOnConnectionAvailable.class, true);
            stopSelf(startId);
            return START_NOT_STICKY;
        }

        RxUtil.dispose(mDisposable);
        mDataManager.syncTasks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        if ( (throwable instanceof HttpException
                                && ((HttpException) throwable).code() == HttpStatus.FORBIDDEN_403)
                                || throwable instanceof SocketTimeoutException) {
                            Timber.e(throwable, "Not auth in DataManager::syncTasks()");
                            Intent singInIntent = SignInActivity.getStartIntent(getApplicationContext());
                            singInIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getApplicationContext().startActivity(singInIntent);
                        }
                        Timber.e(throwable, "There was an DataManager::syncTasks() %s", throwable.getClass().toString());
                    }
                })
                .subscribe(new Observer<TaskDto>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(@NonNull TaskDto ribot) {
                        Timber.w( "Next.");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.w(e, "Error syncing.");

                        stopSelf(startId);
                    }

                    @Override
                    public void onComplete() {
                        Timber.i("Synced successfully!");
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

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static class SyncOnConnectionAvailable extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)
                    && NetworkUtil.isNetworkConnected(context)) {
                Timber.i("Connection is now available, triggering sync...");
                AndroidComponentUtil.toggleComponent(context, this.getClass(), false);
                context.startService(getStartIntent(context));
            }
        }
    }

}
