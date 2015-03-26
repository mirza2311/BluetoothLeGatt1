package com.example.android.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Mirza on 2015-03-25.
 */
public class MyDB{

    private MyDatabaseHelper dbHelper;

    private SQLiteDatabase database;

    public final static String EMP_TABLE="Sockets"; // name of table

    public final static String IP ="_id"; // id value for employee
    public final static String PORT ="name";  // name of employee
    /**
     *
     * @param context
     */
    public MyDB(Context context){
        dbHelper = new MyDatabaseHelper(context);
        database = dbHelper.getWritableDatabase();

    }


    public long createRecords(String id, String name){
        database.execSQL("Delete from Sockets");
        ContentValues values = new ContentValues();
        values.put(IP, id);
        values.put(PORT, name);
        return database.insert(EMP_TABLE, null, values);
    }

    public Cursor selectRecords() {
        String[] cols = new String[] {IP, PORT};
        Cursor mCursor = database.query(true, EMP_TABLE,cols,null
                , null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor; // iterate to get each value.
    }

   /** public void deleteTable(){
        database.execSQL("DROP TABLE IF EXISTS Sockets");
    }**/
}