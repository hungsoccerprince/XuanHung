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

    public static final String DATABASE_NAME = "AlarmClock_ver2";

    public static final String TABLE_NOTE = "alarm_table";
    public static final String id = "_id";
    public static final String name = "name_alarm";
    public static final String ring = "ring_alarm";
    public static final String time = "time_alarm";


    public static final String CREATE_TABLE_ALARM =
            "CREATE TABLE IF NOT EXISTS alarm_table(_id INTEGER PRIMARY KEY, " +
                    "name_alarm VARCHAR(200)," +
                    "ring_alarm VARCHAR(200), " +
                    "time_alarm VARCHAR(50), " +
                    "vibrate VARCHAR(20)," +
                    "day_alarm VARCHAR(50)," +
                    "pCode_alarm INTERGER )" ;

    public static final String CREATE_TABLE_PCODE =
            "CREATE TABLE IF NOT EXISTS pcode_table(pcode INTERGER)";

    public static final int DATA_VERSION = 1;

    private SQLiteDatabase db;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE_ALARM);
            db.execSQL(CREATE_TABLE_PCODE);
            db.execSQL("Insert into pcode_table values(1)");

            Log.e("Creat Table","True");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Creat Table", "False");
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
            return cursor;
        }
        catch (Exception e){
            Log.e("get","NO");
            return  null;
        }
    }

    public Cursor getPcode(){
        open();
        SQLiteDatabase db = getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery("Select * from pcode_table", null);
            return cursor;
        }
        catch (Exception e){
            Log.e("get","NO");
            return  null;
        }
    }

    public void query_data(String sql){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql);
    }

    public void insert(ContentValues values) {
        open();
        try{
            db.insert(TABLE_NOTE, null, values);
            Log.e("insert", "TRUE");
        }
        catch (Exception e){
            Log.e("insert","FALSE");
        }
        close();

    }

    public boolean update( ContentValues values, String where,String table) {
        open();
        long index = db.update(table, values, where, null);
        close();
        return index > 0;
    }

    public boolean delete(String where) {
        open();
        long index = db.delete(TABLE_NOTE, where, null);
        close();
        return index > 0;
    }

}
