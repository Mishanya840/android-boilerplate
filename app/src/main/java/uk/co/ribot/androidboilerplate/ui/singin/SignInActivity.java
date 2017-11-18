package uk.co.ribot.androidboilerplate.ui.singin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.ui.base.BaseActivity;
import uk.co.ribot.androidboilerplate.ui.main.MainActivity;

public class SignInActivity extends BaseActivity implements SignInMvpView {

    @Inject SignInPresenter mSignInPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.i("SignInActivity");
        activityComponent().inject(this);
        setContentView(R.layout.singin_activity);
        ButterKnife.bind(this);

        mSignInPresenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSignInPresenter.detachView();
    }

    /**
     * Return an Intent to start this Activity.
     */
    public static Intent getStartIntent(Context context) {
        return new Intent(context, SignInActivity.class);
    }

    @Override
    public void showSignInSuccessful() {

    }
}
