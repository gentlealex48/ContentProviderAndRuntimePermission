package com.kkaty.contentproviderandruntimepermission.model;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class ContentProviderContract {
    public static final String CONTENT_AUTHORITY = "com.mobileapps.contentprovidersandruntimeperm";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIE = "movie";
    public static final String PATH_GENRE = "genre";

    public static final class MovieEntry implements BaseColumns {
        // Content URI represents the base location for the table
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        // These are special type prefixes that specify if a URI returns a list or a specific item
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI  + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_MOVIE;

        // Define the table schema
        public static final String TABLE_NAME = "movieTable";
        public static final String COLUMN_NAME = "movieName";
        public static final String COLUMN_RELEASE_DATE = "movieReleaseDate";
        public static final String COLUMN_GENRE = "movieGenre";

        // Define a function to build a URI to find a specific movie by it's identifier
        public static Uri buildMovieUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class GenreEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_GENRE).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_GENRE;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_GENRE;

        public static final String TABLE_NAME = "genreTable";
        public static final String COLUMN_NAME = "genreName";

        public static Uri buildGenreUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
