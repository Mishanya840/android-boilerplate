package uk.co.ribot.androidboilerplate.data;

import android.util.Log;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import retrofit2.Response;
import ru.macroplus.webplatform.dto.task.TaskDto;
import uk.co.ribot.androidboilerplate.data.local.DatabaseHelper;
import uk.co.ribot.androidboilerplate.data.local.PreferencesHelper;
import uk.co.ribot.androidboilerplate.data.remote.AuthResource;
import uk.co.ribot.androidboilerplate.data.remote.TaskResource;

@Singleton
public class DataManager {

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
                    public ObservableSource<? extends TaskDto> apply(@NonNull List<TaskDto> ribots)
                            throws Exception {
                        return mDatabaseHelper.setTasks(ribots);
                    }
                });
    }

    public Observable<List<TaskDto>> getTasks() {
        return mDatabaseHelper.getTasks().distinct();
    }

}
