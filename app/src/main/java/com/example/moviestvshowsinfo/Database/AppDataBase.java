package com.example.moviestvshowsinfo.Database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

/**
 * This abstract class will be used to create database and in other classes we will get
 * the database by calling {@getInstance} method.
 */
@Database(entities = {MovieTvShowEntity.class}, exportSchema = false, version = 1)
public abstract class AppDataBase extends RoomDatabase {

    private static final String DATABASE_NAME = "MoviesAndTvShows";
    private static final Object LOCK = new Object();
    private static AppDataBase appDataBase;

    public static AppDataBase getInstance(Context context) {
        if (appDataBase == null) {
            synchronized (LOCK) {
                appDataBase = Room.databaseBuilder(context.getApplicationContext(), AppDataBase.class, AppDataBase.DATABASE_NAME)
                        .build();
            }
        }

        return appDataBase;
    }

    /**
     * This abstract method will be used to refer to the {@MovieTvShowDao} interface.
     */

    public abstract MovieTvShowDao movieTvShowDao();
}
