package uk.co.ribot.androidboilerplate.data.remote;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import ru.macroplus.webplatform.dto.task.TaskDto;
import uk.co.ribot.androidboilerplate.data.BaseRetrofitBuilder;
import uk.co.ribot.androidboilerplate.util.MyGsonTypeAdapterFactory;

public interface TaskResource {

    @GET("/device/tasks")
    Observable<List<TaskDto>> getTasks();

    /******** Helper class that sets up a new services *******/
    class Creator {

        public static TaskResource newTaskService(Application mApplication) {
            Retrofit retrofit = BaseRetrofitBuilder.getBaseRetrofitBuilder(mApplication);
            return retrofit.create(TaskResource.class);
        }
    }
}
