package uk.co.ribot.androidboilerplate.ui.singin;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.injection.ConfigPersistent;
import uk.co.ribot.androidboilerplate.ui.base.BasePresenter;

@ConfigPersistent
public class SignInPresenter extends BasePresenter<SignInMvpView> {

    private final DataManager mDataManager;

    @Inject
    public SignInPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }


    public void singIn(String login, String password) {

        getMvpView().showSignInSuccessful();
    }

}
