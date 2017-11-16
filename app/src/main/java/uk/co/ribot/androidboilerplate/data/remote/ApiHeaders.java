package uk.co.ribot.androidboilerplate.data.remote;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import uk.co.ribot.androidboilerplate.injection.ApplicationContext;
import uk.co.ribot.androidboilerplate.util.AuthConstants;

@Singleton
public class ApiHeaders implements Interceptor {

  private Application application;

  @Inject
  public ApiHeaders(Application application) {
    this.application = application;
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    Request original = chain.request();

    // Request customization: add request headers
    Request.Builder requestBuilder = original.newBuilder()
            .addHeader("Cache-Control", "no-cache")
            .addHeader("Cache-Control", "no-store");

    AccountManager accountManager = AccountManager.get(application);
    Account[] accounts = accountManager.getAccountsByType(AuthConstants.ACCOUNT_TYPE);
    if (accounts.length != 0) {
      String token = accountManager.peekAuthToken(accounts[0], AuthConstants.AUTHTOKEN_TYPE);
      @SuppressLint("WifiManagerLeak") WifiManager manager = (WifiManager) application.getSystemService(Context.WIFI_SERVICE);
      WifiInfo info = manager.getConnectionInfo();
      String macAddress = info.getMacAddress();
      if (token != null) {
        requestBuilder.addHeader("auth-token", token);
        requestBuilder.addHeader("mac-address", macAddress);
        Log.i("Auth", String.format("Auth-token - {}, mac-address - {}", token, macAddress));
      }
    }

    Request request = requestBuilder.build();
    return chain.proceed(request);
  }
}