package com.example.mrezaei.pegahwarehouse;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LastStatDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "LastStat.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + LastStatContract.FeedEntry.TABLE_NAME + " (" +
                    LastStatContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    LastStatContract.FeedEntry.COLUMN_NAME_DELIVER_ID + " INTEGER PRIMARY KEY," +
                    LastStatContract.FeedEntry.COLUMN_NAME_DELIVERY_COUNT + INTEGER_TYPE + COMMA_SEP +
                    LastStatContract.FeedEntry.COLUMN_NAME_SHRINK_COUNT + INTEGER_TYPE + COMMA_SEP +
                    LastStatContract.FeedEntry.COLUMN_NAME_PALLET_COUNT + INTEGER_TYPE +
                    // Any other options for the CREATE command
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + LastStatContract.FeedEntry.TABLE_NAME;

    public LastStatDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}