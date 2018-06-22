package com.example.android.popularmoviesstagetwo.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import com.example.android.popularmoviesstagetwo.model.Movie;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class FavouriteMoviesDatabase extends RoomDatabase{

    private static final String LOG_TAG = FavouriteMoviesDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "favouritesList";
    private static FavouriteMoviesDatabase sInstance;

    public static FavouriteMoviesDatabase getsInstance(Context context){
        if (sInstance == null){
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                FavouriteMoviesDatabase.class, FavouriteMoviesDatabase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }
    public abstract MovieDao movieDao();
}
