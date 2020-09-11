package com.example.movies.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.movies.models.SummaryMovie;

@Database(entities = {SummaryMovie.class},version = 1,exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    private static AppDataBase instance;
    private static final String DATABASE_NAME = "favorites";
    private static final Object lock = new Object();

    public static AppDataBase getInstance(Context context) {
        if(instance == null) {
            synchronized (lock) {
                instance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDataBase.class,AppDataBase.DATABASE_NAME)
                        .build();
            }
        }
        return instance;
    }

    public abstract SummaryMovieDao summaryMovieDao();
}
