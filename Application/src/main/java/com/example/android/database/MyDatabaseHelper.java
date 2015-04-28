package com.example.android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Mirza on 2015-03-25.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Socket.db";

    private static final int DATABASE_VERSION = 1;
    public final static String IP ="_id"; // id value for employee
    public final static String PORT ="name";
    public final static String TABLE_CONTACTS = "Sockets";

    // Database creation sql statement


    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {

        database.execSQL("DROP TABLE IF EXISTS Sockets");
        String DATABASE_CREATE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + IP + " TEXT,"
                + PORT + " TEXT" + ")";
        database.execSQL(DATABASE_CREATE);
    }

    // Method is called during an upgrade of the database,
    @Override
    public void onUpgrade(SQLiteDatabase database,int oldVersion,int newVersion){
        Log.w(MyDatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS Sockets");
        onCreate(database);
    }


}
