package com.example.mrezaei.pegahwarehouse;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Driver;
import java.util.ArrayList;
import java.util.List;

public class DriverDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "PegahSqlite.db";
    private static final String TABLE_DRIVERS = "drivers";
    private static final String KEY_ID = "id";
    private static final String KEY_DRIVER_CODE = "driverCode";
    private static final String KEY_FIRST_NAME = "firstName";
    private static final String KEY_LAST_NAME = "lastName";
    private static final String KEY_PHOTO = "photo";

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE="CREATE TABLE "+ TABLE_DRIVERS + "("
                +KEY_ID + " TEXT,"+ KEY_DRIVER_CODE + " TEXT," + KEY_FIRST_NAME + " TEXT,"
                + KEY_LAST_NAME +" TEXT," + KEY_PHOTO + "TEXT "+")";
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRIVERS);

        onCreate(db);
    }
    public DriverDatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    void addDriver(DriverPerson driverPerson){
        //First delete user if already exists
        deleteDriver(driverPerson);
        //next insert new user
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, driverPerson.getId());
        values.put(KEY_DRIVER_CODE, driverPerson.getDriverCode());
        values.put(KEY_FIRST_NAME, driverPerson.getFirstName());
        values.put(KEY_LAST_NAME, driverPerson.getLastName());
        values.put(KEY_PHOTO, driverPerson.getPhoto());
        db.insert(TABLE_DRIVERS,null,values);
        db.close();
    }

    DriverPerson getDriver(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_DRIVERS,
                new String[]{KEY_ID, KEY_DRIVER_CODE, KEY_FIRST_NAME, KEY_LAST_NAME,KEY_PHOTO},
                KEY_ID + "=?",
                new String[]{"'"+id+"'"},null,null,null);
        if (cursor!=null){
            cursor.moveToFirst();
        }
        DriverPerson driverPerson = new DriverPerson(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4));
        return driverPerson;
    }
    public List<DriverPerson> getAllDrivers(){
        List<DriverPerson> driverList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_DRIVERS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        if (cursor.moveToFirst()){
            do {
                DriverPerson driverPerson = new DriverPerson();
                driverPerson.setId(cursor.getString(0));
                driverPerson.setDriverCode(cursor.getString(1));
                driverPerson.setFirstName(cursor.getString(2));
                driverPerson.setLastName(cursor.getString(3));
                driverPerson.setPhoto(cursor.getString(4));

                driverList.add(driverPerson);
            } while (cursor.moveToNext());
        }
        return driverList;
    }

    public int updateDriver(Truck truck){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, truck.getId());
        values.put(KEY_FIRST_NAME, truck.getPlaque());
        values.put(KEY_LAST_NAME, truck.getTruckCode());
        values.put(KEY_DRIVER_CODE, truck.getTruckModel());

        return db.update(TABLE_DRIVERS,values,
                KEY_ID + " =?",new String[]{"'"+ truck.getId()+"'"});

    }

    public void deleteDriver(DriverPerson driverPerson){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DRIVERS,
                KEY_ID + "=?",new String[]{"'"+ driverPerson.getId()+"'"});
        db.close();
    }

    public int getDriversCount(){
        String countQuery=  "SELECT * FROM " + TABLE_DRIVERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery,null);
        cursor.close();

        return cursor.getCount();
    }
}
