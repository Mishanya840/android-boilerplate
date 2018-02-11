package uk.co.ribot.androidboilerplate.data.local;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.VisibleForTesting;

import com.squareup.sqlbrite2.BriteDatabase;
import com.squareup.sqlbrite2.SqlBrite;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import ru.macroplus.webplatform.dto.task.TaskDto;

@Singleton
public class DatabaseHelper {

    private final BriteDatabase mDb;

    @Inject
    public DatabaseHelper(DbOpenHelper dbOpenHelper) {
        this(dbOpenHelper, Schedulers.io());
    }

    @VisibleForTesting
    public DatabaseHelper(DbOpenHelper dbOpenHelper, Scheduler scheduler) {
        SqlBrite.Builder briteBuilder = new SqlBrite.Builder();
        mDb = briteBuilder.build().wrapDatabaseHelper(dbOpenHelper, scheduler);
    }

    public BriteDatabase getBriteDb() {
        return mDb;
    }

    public Observable<TaskDto> setTasks(final Collection<TaskDto> newTasks) {
        return Observable.create(new ObservableOnSubscribe<TaskDto>() {
            @Override
            public void subscribe(ObservableEmitter<TaskDto> emitter) throws Exception {
                if (emitter.isDisposed()) return;
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try {
                    mDb.delete(Db.TaskProfileTable.TABLE_NAME, null);
                    for (TaskDto task : newTasks) {
                        System.out.println(task.toString());
                        long result = mDb.insert(Db.TaskProfileTable.TABLE_NAME,
                                Db.TaskProfileTable.toContentValues(task),
                                SQLiteDatabase.CONFLICT_REPLACE);
                        if (result >= 0) emitter.onNext(task);
                    }
                    transaction.markSuccessful();
                    emitter.onComplete();
                } finally {
                    transaction.end();
                }
            }
        });
    }

    public Observable<List<TaskDto>> getTasks() {
        return mDb.createQuery(Db.TaskProfileTable.TABLE_NAME,
                "SELECT * FROM " + Db.TaskProfileTable.TABLE_NAME)
                .mapToList(new Function<Cursor, TaskDto>() {
                    @Override
                    public TaskDto apply(@NonNull Cursor cursor) throws Exception {
                        return (Db.TaskProfileTable.parseCursor(cursor));
                    }
                });
    }

}
