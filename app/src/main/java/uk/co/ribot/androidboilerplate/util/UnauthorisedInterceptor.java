package uk.co.ribot.androidboilerplate.util;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.Interceptor;
import okhttp3.Response;
import uk.co.ribot.androidboilerplate.BoilerplateApplication;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.event.BusEvent;

public class UnauthorisedInterceptor implements Interceptor {

    private static final String LOG = DataManager.class.getName();

    RxEventBus eventBus;

    public UnauthorisedInterceptor(Context context) {
        BoilerplateApplication.get(context).getComponent().inject(this);
        eventBus = BoilerplateApplication.get(context).eventBus();
    }


    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        Log.e(LOG, "222interceptor");
        if (response.code() != 200) {
            Log.e(LOG, String.format("Error : HTTP {}",response.code()));
            eventBus.post(new BusEvent.AuthenticationError()); // post event AuthenticationError
        }
        return response;
    }
}