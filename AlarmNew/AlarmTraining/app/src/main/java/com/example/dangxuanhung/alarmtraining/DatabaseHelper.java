package com.example.dangxuanhung.alarmtraining;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Dang Xuan Hung on 09/03/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "AlarmNew12";
    private static final String TAG = DatabaseHelper.class.getSimpleName();

    public static final String CREATE_TABLE_ALARM =
            "CREATE TABLE IF NOT EXISTS alarm_table(_id_alarm INTEGER PRIMARY KEY, " +
                    "name_alarm VARCHAR(200)," +
                    "ring_alarm VARCHAR(200), " +
                    "hour INTERGER," +
                    "minute INTERGER, "+
                    "arr_day VARCHAR(20),"+
                    "state VARCHAR(20),"+
                    "mode VARCHAR(20),"+
                    "vibrate VARCHAR(20))" ;
    public static final String CREATE_TABLE_DAY =
            "CREATE TABLE IF NOT EXISTS day_table(_id_day INTEGER PRIMARY KEY, " +
                    "id_alarm INTERGER NOT NULL," +
                    "name_alarm VARCHAR(200)," +
                    "day_alarm INTERGER, " +
                    "ring_alarm VARCHAR(200), " +
                    "hour_alarm INTERGER ," +
                    "minute_alarm INTERGER, " +
                    "state VARCHAR(20),"+
                    "mode VARCHAR(20),"+
                    "vibrate VARCHAR(20))" ;

    public static final int DATA_VERSION = 1;

    private SQLiteDatabase db;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE_ALARM);
            Log.d(TAG,"Create table alarm true");
            db.execSQL(CREATE_TABLE_DAY);
            Log.e(TAG,"Create table day true");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG," Create table False");
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void open() {
        try {
            db = getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (db != null && db.isOpen()) {
            try {
                db.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Cursor getData(String sql){
        open();
        SQLiteDatabase db = getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery(sql, null);
            Log.d(TAG,"get data true");
            return cursor;
        }
        catch (Exception e){
            Log.d(TAG,"get data false");
            return  null;
        }
    }


    public void query_data(String sql){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql);
    }

    public void insert(ContentValues values,String table) {
        open();
        try{
            db.insert(table, null, values);
            Log.d(TAG, "insert true");
        }
        catch (Exception e){
            Log.d(TAG, "insert false");
        }
        close();

    }

    public boolean update( ContentValues values, String where,String table) {
        open();
        long index = db.update(table, values, where, null);
        Log.d(TAG, "update complete!");
        close();
        return index > 0;
    }

    public boolean delete(String table,String where) {
        open();
        long index = db.delete(table, where, null);
        Log.d(TAG, "delete complete!");
        close();
        return index > 0;
    }

}
