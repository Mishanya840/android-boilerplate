package uk.co.ribot.androidboilerplate.injection.component;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;
import uk.co.ribot.androidboilerplate.data.AuthService;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.data.local.DatabaseHelper;
import uk.co.ribot.androidboilerplate.data.local.PreferencesHelper;
import uk.co.ribot.androidboilerplate.data.remote.TaskResource;
import uk.co.ribot.androidboilerplate.injection.ApplicationContext;
import uk.co.ribot.androidboilerplate.injection.module.ApplicationModule;
import uk.co.ribot.androidboilerplate.util.AuthTokenHolder;
import uk.co.ribot.androidboilerplate.util.RxEventBus;
import uk.co.ribot.androidboilerplate.util.UnauthorisedInterceptor;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    /*В каких классах хочешь инжектить*/
    void inject(AuthService authService);
//    void inject(SyncService syncService);
    void inject(UnauthorisedInterceptor syncService);

    /*какие классы из моделей modules ={} */
    @ApplicationContext Context context();
    Application application();
    TaskResource taskResource();
    PreferencesHelper preferencesHelper();
    DatabaseHelper databaseHelper();
    DataManager dataManager();
    RxEventBus eventBus();
    Retrofit retrofit();
    AuthTokenHolder authAccountManager();

}

