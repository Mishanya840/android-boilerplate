package uk.co.ribot.androidboilerplate.data.local;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Date;

import ru.macroplus.webplatform.dto.task.TaskDto;

public class Db {

    public Db() { }

    public abstract static class TaskProfileTable {
        public static final String TABLE_NAME = "task_dto";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "title";

        public static final String CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY, " +
                        COLUMN_TITLE + " TEXT NOT NULL " +
                " ); ";

        public static ContentValues toContentValues(TaskDto taskDto) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ID, taskDto.getId());
            values.put(COLUMN_TITLE, taskDto.getTitle());
            return values;
        }

        public static TaskDto parseCursor(Cursor cursor) {
            TaskDto taskDto = new TaskDto();
            taskDto.setId((cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))));
            taskDto.setTitle((cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))));

            return taskDto;
        }
    }
}
