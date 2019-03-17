package com.kkaty.contentproviderandruntimepermission.model;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class CustomerContentProvider extends ContentProvider {
    private static final int GENRE = 100;
    private static final int GENRE_ID = 101;
    private static final int MOVIE = 200;
    private static final int MOVIE_ID = 201;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDatabaseSQLiteHelper movieDatabaseSQLiteHelper;

    @Override
    public boolean onCreate() {
        movieDatabaseSQLiteHelper = new MovieDatabaseSQLiteHelper(getContext());
        return true;

    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = movieDatabaseSQLiteHelper.getWritableDatabase();
        Cursor retCursor;
        switch (sUriMatcher.match(uri)){
            case GENRE:
                retCursor = db.query(
                        ContentProviderContract.GenreEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case  GENRE_ID:
                long _id = ContentUris.parseId(uri);
                retCursor = db.query(
                        ContentProviderContract.GenreEntry.TABLE_NAME,
                        projection,
                        ContentProviderContract.GenreEntry._ID + " =?",
                        new String[]{String.valueOf(_id)},
                        null,
                        null,
                        sortOrder
                );
                break;
            case  MOVIE:
                retCursor = db.query(
                        ContentProviderContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case MOVIE_ID:
                _id = ContentUris.parseId(uri);
                retCursor = db.query(
                        ContentProviderContract.MovieEntry.TABLE_NAME,
                        projection,
                        ContentProviderContract.MovieEntry._ID + " = ?",
                        new String[]{String.valueOf(_id)},
                        null,
                        null,
                        sortOrder
                );
                break;
                default:
                    throw new  UnsupportedOperationException("Unknown uri: " + uri);

        }

        // Set the notification URI for the cursor to the one passed into the function. This
        // causes the cursor to register a content observer to watch for changes that happen to
        // this URI and any of it's descendants. By descendants, we mean any URI that begins
        // with this path.
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)){
            case GENRE:
                return  ContentProviderContract.GenreEntry.CONTENT_TYPE;
            case   GENRE_ID:
                return  ContentProviderContract.GenreEntry.CONTENT_ITEM_TYPE;
            case MOVIE:
                return ContentProviderContract.MovieEntry.CONTENT_TYPE;
            case MOVIE_ID:
                return ContentProviderContract.MovieEntry.CONTENT_ITEM_TYPE;
                default:
                    throw new  UnsupportedOperationException("Unknown uri: " + uri);
        }

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = movieDatabaseSQLiteHelper.getWritableDatabase();
        long _id;
        Uri returnUri;

        switch(sUriMatcher.match(uri)){
            case GENRE:
                _id = db.insert(ContentProviderContract.GenreEntry.TABLE_NAME, null, contentValues);
                if(_id > 0){
                    returnUri =  ContentProviderContract.GenreEntry.buildGenreUri(_id);
                } else{
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                }
                break;
            case MOVIE:
                _id = db.insert(ContentProviderContract.MovieEntry.TABLE_NAME, null, contentValues);
                if(_id > 0){
                    returnUri = ContentProviderContract.MovieEntry.buildMovieUri(_id);
                } else{
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Use this on the URI passed into the function to notify any observers that the uri has
        // changed.
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;

    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = movieDatabaseSQLiteHelper.getWritableDatabase();
        int rows; // Number of rows effected

        switch(sUriMatcher.match(uri)){
            case GENRE:
                rows = db.delete(ContentProviderContract.GenreEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MOVIE:
                rows = db.delete(ContentProviderContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Because null could delete all rows:
        if(selection == null || rows != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rows;

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = movieDatabaseSQLiteHelper.getWritableDatabase();
        int rows;

        switch(sUriMatcher.match(uri)){
            case GENRE:
                rows = db.update(ContentProviderContract.GenreEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            case MOVIE:
                rows = db.update(ContentProviderContract.MovieEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(rows != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rows;

    }

    public static UriMatcher buildUriMatcher(){
        String content = ContentProviderContract.CONTENT_AUTHORITY;

        // All paths to the UriMatcher have a corresponding code to return
        // when a match is found (the ints above).
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(content, ContentProviderContract.PATH_GENRE, GENRE);
        matcher.addURI(content, ContentProviderContract.PATH_GENRE + "/#", GENRE_ID);
        matcher.addURI(content, ContentProviderContract.PATH_MOVIE, MOVIE);
        matcher.addURI(content, ContentProviderContract.PATH_MOVIE + "/#", MOVIE_ID);

        return matcher;
    }
}
