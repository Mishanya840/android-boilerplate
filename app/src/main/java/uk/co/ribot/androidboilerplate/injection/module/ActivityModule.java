package uk.co.ribot.androidboilerplate.injection.module;

import android.app.Activity;
import android.content.Context;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;
import uk.co.ribot.androidboilerplate.injection.ActivityContext;
import uk.co.ribot.androidboilerplate.injection.component.ActivitySubcomponent;
import uk.co.ribot.androidboilerplate.ui.base.BaseActivity;

@Module(subcomponents = ActivitySubcomponent.class)
public class ActivityModule {

    private Activity mActivity;

    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

    @Provides
    Activity provideActivity() {
        return mActivity;
    }

    @Provides
    @ActivityContext
    Context providesContext() {
        return mActivity;
    }

}
