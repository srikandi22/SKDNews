package com.kita.skdnews.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {DSNews.class, DSSettings.class}, version = 1, exportSchema = false)
public abstract class DBNews extends RoomDatabase {
    public abstract DAONews getDAONews();
    public abstract DAOSetting getDAOSetting();

    private static DBNews DB;

    public static DBNews getInstance(Context context) {
        if (null == DB) {
            DB = buildDatabaseInstance(context);
        }
        return DB;
    }

    private static DBNews buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(context,
                DBNews.class,
                "news_db")
                .allowMainThreadQueries().build();
    }

    public void cleanUp(){
        DB = null;
    }
}
