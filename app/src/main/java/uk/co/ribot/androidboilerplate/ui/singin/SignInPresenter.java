package uk.co.ribot.androidboilerplate.ui.singin;

import uk.co.ribot.androidboilerplate.ui.base.BasePresenter;

public class SignInPresenter extends BasePresenter<SignInMvpView> {

    public void singIn(String login, String password) {

        getMvpView().showSignInSuccessful();
    }

}
