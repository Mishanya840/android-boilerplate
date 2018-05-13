package uk.co.ribot.androidboilerplate.injection.module;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.co.ribot.androidboilerplate.data.remote.ApiHeaders;
import uk.co.ribot.androidboilerplate.data.remote.AuthResource;
import uk.co.ribot.androidboilerplate.data.remote.TaskResource;
import uk.co.ribot.androidboilerplate.injection.ApplicationContext;
import uk.co.ribot.androidboilerplate.util.AuthTokenHolder;
import uk.co.ribot.androidboilerplate.util.Config;
import uk.co.ribot.androidboilerplate.util.MyGsonTypeAdapterFactory;
import uk.co.ribot.androidboilerplate.util.UnauthorisedInterceptor;

/**
 * Provide application-level dependencies.
 */
@Module
public class ApplicationModule {

    protected final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    TaskResource provideTasksResource(Retrofit retrofit) {
        return retrofit.create(TaskResource.class);
    }

    @Provides
    @Singleton
    AuthResource provideAuthResource(Retrofit retrofit) {
            return retrofit.create(AuthResource.class);
    }

    @Provides
    @Singleton
    ApiHeaders apiHeaders() {
        return new ApiHeaders(mApplication);
    }

    @Provides
    @Singleton
    UnauthorisedInterceptor unauthorisedInterceptor() {
        return new UnauthorisedInterceptor(mApplication);
    }

    @Provides
    @Singleton
    AuthTokenHolder authAccountManager(){
        return new AuthTokenHolder();
    }

    @Provides
    @Singleton
    Retrofit retrofit(ApiHeaders apiHeaders,
                      UnauthorisedInterceptor unauthorisedInterceptor) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(MyGsonTypeAdapterFactory.create())
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(apiHeaders);
        httpClient.addInterceptor(unauthorisedInterceptor);

        return new Retrofit.Builder()
                .baseUrl(Config.ENDPOINT)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }


}