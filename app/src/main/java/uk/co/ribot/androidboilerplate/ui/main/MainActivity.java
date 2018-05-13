package uk.co.ribot.androidboilerplate.ui.main;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import ru.macroplus.webplatform.dto.task.TaskDto;
import uk.co.ribot.androidboilerplate.BoilerplateApplication;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.event.BusEvent;
import uk.co.ribot.androidboilerplate.ui.base.BaseActivity;
import uk.co.ribot.androidboilerplate.ui.singin.SignInActivity;
import uk.co.ribot.androidboilerplate.util.DialogFactory;

import static uk.co.ribot.androidboilerplate.util.LogCatTag.AUTH_PROCESS;

public class MainActivity extends BaseActivity implements MainMvpView {

    private static final String EXTRA_TRIGGER_SYNC_FLAG =
            "uk.co.ribot.androidboilerplate.ui.main.MainActivity.EXTRA_TRIGGER_SYNC_FLAG";

    private static final String LOG = MainActivity.class.getName();

    @Inject MainPresenter mMainPresenter;
    @BindView(R.id.webview) WebView mWebView;

    /**
     * Return an Intent to start this Activity.
     * triggerDataSyncOnCreate allows disabling the background sync service onCreate. Should
     * only be set to false during testing.
     */
    public static Intent getStartIntent(Context context, boolean triggerDataSyncOnCreate) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(EXTRA_TRIGGER_SYNC_FLAG, triggerDataSyncOnCreate);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mMainPresenter.attachView(this);

        ((BoilerplateApplication) getApplication()).eventBus().observable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {

                    @Override
                    public void accept(Object o) throws Exception {
                        Log.i(LOG, AUTH_PROCESS + "Changes");
                        if (o instanceof BusEvent.AuthenticationError) {
                            Log.i(LOG, AUTH_PROCESS + "BusEvent.AuthenticationError");
                            startActivity(SignInActivity.getStartIntent(getApplicationContext()));
                        }
                    }
                });


//        if (getIntent().getBooleanExtra(EXTRA_TRIGGER_SYNC_FLAG, true)) {
//            mMainPresenter.loadTasks();
//        } else {
            mWebView.loadUrl("file:///android_asset/" + "PromoPost.html");

            mWebView.getSettings().setAllowFileAccess(true);
            mWebView.getSettings().setJavaScriptEnabled(true);
//            wv.getSettings().setPluginsEnabled(true);
            // webview.loadUrl("file:///android_asset/new.html");
//        }
    }

    /**
     * Loads html page with the content.
     */
//    private void loadHtmlPage() {
//        String htmlString = getHtmlFromAsset();
//        if (htmlString != null) {
//            mButterflyWebView.loadDataWithBaseURL("file:///android_asset/images/", htmlString, "text/html", "UTF-8", null);
//        }
//        else {
//            Toast.makeText(this, R.string.no_such_page, Toast.LENGTH_LONG).show();
//        }
//    }

    /**
     * Gets html content from the assets folder.
     */
//    private String getHtmlFromAsset() {
//        InputStream is;
//        StringBuilder builder = new StringBuilder();
//        String htmlString = null;
//        try {
//            is = getAssets().open(getString(R.string.butterfly_html));
//            if (is != null) {
//                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    builder.append(line);
//                }
//
//                htmlString = builder.toString();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return htmlString;
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mMainPresenter.detachView();
    }

    @Override
    public void showTask(TaskDto taskDto) {
    }

    @Override
    public void showError() {
        DialogFactory.createGenericErrorDialog(this, getString(R.string.error_loading_ribots))
                .show();
    }


}
