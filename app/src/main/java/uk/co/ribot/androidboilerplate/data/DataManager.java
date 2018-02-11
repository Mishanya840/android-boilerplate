package uk.co.ribot.androidboilerplate.data;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import java.net.SocketTimeoutException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.katharsis.repository.response.HttpStatus;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import retrofit2.HttpException;
import ru.macroplus.webplatform.dto.task.TaskDto;
import timber.log.Timber;
import uk.co.ribot.androidboilerplate.BoilerplateApplication;
import uk.co.ribot.androidboilerplate.data.local.DatabaseHelper;
import uk.co.ribot.androidboilerplate.data.local.PreferencesHelper;
import uk.co.ribot.androidboilerplate.data.remote.AuthResource;
import uk.co.ribot.androidboilerplate.data.remote.TaskResource;
import uk.co.ribot.androidboilerplate.ui.singin.SignInActivity;

@Singleton
public class DataManager {

    private static final String LOG = DataManager.class.getName();

    private final TaskResource mTaskResource;
    private final AuthResource mAuthResource;
    private final DatabaseHelper mDatabaseHelper;
    private final PreferencesHelper mPreferencesHelper;

    @Inject
    public DataManager(TaskResource taskResource,
                       AuthResource authResource,
                       PreferencesHelper preferencesHelper,
                       DatabaseHelper databaseHelper) {
        mTaskResource = taskResource;
        mAuthResource = authResource;
        mPreferencesHelper = preferencesHelper;
        mDatabaseHelper = databaseHelper;
    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }

    public Observable<Boolean> isAuth() {
        return mAuthResource.isAuth();
    }

    public Observable<TaskDto> syncTasks() {
        return mTaskResource.getTasks()
                .concatMap(new Function<List<TaskDto>, ObservableSource<? extends TaskDto>>() {
                    @Override
                    public ObservableSource<? extends TaskDto> apply(@NonNull List<TaskDto> taskDtos)
                            throws Exception {
                        Log.i(LOG, String.format("Save to DB tasks: {}", taskDtos));
                        return mDatabaseHelper.setTasks(taskDtos);
                    }
                });
    }

    public Observable<List<TaskDto>> getTasks() {
        return mDatabaseHelper.getTasks().distinct();
    }

}
