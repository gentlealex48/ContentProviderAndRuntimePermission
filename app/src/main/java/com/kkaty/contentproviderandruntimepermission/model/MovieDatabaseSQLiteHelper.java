package com.kkaty.contentproviderandruntimepermission.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieDatabaseSQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "movieList.db";

    public MovieDatabaseSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        addGenreTable(sqLiteDatabase);
        addMovieTable(sqLiteDatabase);

    }

    private void addGenreTable(SQLiteDatabase db){
        db.execSQL(
                "CREATE TABLE " + ContentProviderContract.GenreEntry.TABLE_NAME + " (" +
                        ContentProviderContract.GenreEntry._ID + " INTEGER PRIMARY KEY, " +
                        ContentProviderContract.GenreEntry.COLUMN_NAME + " TEXT UNIQUE NOT NULL);"
        );
    }

    private void addMovieTable(SQLiteDatabase db){
        db.execSQL(
                "CREATE TABLE " + ContentProviderContract.MovieEntry.TABLE_NAME + " (" +
                        ContentProviderContract.MovieEntry._ID + " INTEGER PRIMARY KEY, " +
                        ContentProviderContract.MovieEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                        ContentProviderContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                        ContentProviderContract.MovieEntry.COLUMN_GENRE + " INTEGER NOT NULL, " +
                        "FOREIGN KEY (" + ContentProviderContract.MovieEntry.COLUMN_GENRE + ") " +
                        "REFERENCES " + ContentProviderContract.GenreEntry.TABLE_NAME
                        + " (" + ContentProviderContract.GenreEntry._ID + "));"
        );
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
