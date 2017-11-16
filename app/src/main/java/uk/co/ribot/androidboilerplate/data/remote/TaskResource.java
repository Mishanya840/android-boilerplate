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
import uk.co.ribot.androidboilerplate.util.MyGsonTypeAdapterFactory;

public interface TaskResource {

//    String ENDPOINT = "http://10.170.253.197:8080/";
    String ENDPOINT = "http://192.168.1.46:8081/";

    @GET("/device/tasks")
    Observable<List<TaskDto>> getTasks();

    /******** Helper class that sets up a new services *******/
    class Creator {

        public static TaskResource newTaskService(Application mApplication) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapterFactory(MyGsonTypeAdapterFactory.create())
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .create();
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new ApiHeaders(mApplication));
            OkHttpClient client = httpClient.build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(TaskResource.ENDPOINT)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            return retrofit.create(TaskResource.class);
        }
    }
}
