package uk.co.ribot.androidboilerplate.util;

import javax.inject.Singleton;

@Singleton
public class AuthTokenHolder {

    private String authToken;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getToken() {
        return authToken;
    }
}
