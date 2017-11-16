package uk.co.ribot.androidboilerplate.ui.singin;

import android.os.Bundle;

import javax.inject.Inject;

import uk.co.ribot.androidboilerplate.ui.base.BaseActivity;

public class ActivitySignIn extends BaseActivity implements SignInMvpView {

    @Inject SignInPresenter mSignInPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSignInPresenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSignInPresenter.detachView();
    }

    @Override
    public void showSignInSuccessful() {

    }
}
