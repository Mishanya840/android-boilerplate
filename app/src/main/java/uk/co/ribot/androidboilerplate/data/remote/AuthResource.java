package uk.co.ribot.androidboilerplate.data.remote;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import uk.co.ribot.androidboilerplate.data.BaseRetrofitBuilder;
import uk.co.ribot.androidboilerplate.util.MyGsonTypeAdapterFactory;

/**
 * Created by Mishanya on 13.11.2017.
 */

public interface AuthResource {

    @GET("/device/login")
    Observable<Boolean> isAuth();

    /******** Helper class that sets up a new services *******/
    class Creator {

        public static AuthResource newAuthService(Application mApplication) {
            Retrofit retrofit = BaseRetrofitBuilder.getBaseRetrofitBuilder(mApplication);
            return retrofit.create(AuthResource.class);
        }
    }

}
