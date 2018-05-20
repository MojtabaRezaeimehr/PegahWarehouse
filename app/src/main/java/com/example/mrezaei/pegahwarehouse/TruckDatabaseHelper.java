package com.example.mrezaei.pegahwarehouse;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class TruckDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "PegahSqlite.db";
    private static final String TABLE_TRUCKS = "trucks";
    private static final String KEY_ID = "id";
    private static final String KEY_PLAQUE = "plaque";
    private static final String KEY_TRUCK_CODE = "truckCode";
    private static final String KEY_TRUCK_MODEL = "truckModel";

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE="CREATE TABLE "+ TABLE_TRUCKS + "("
                +KEY_ID + " TEXT," + KEY_PLAQUE  + " TEXT,"
                + KEY_TRUCK_CODE +" TEXT," + KEY_TRUCK_MODEL + "TEXT "+")";
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRUCKS);

        onCreate(db);
    }
    public TruckDatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    void addTruck(Truck truck){
        //First delete user if already exists
        deleteTruck(truck);
        //next insert new user
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, truck.getId());
        values.put(KEY_PLAQUE, truck.getPlaque());
        values.put(KEY_TRUCK_CODE, truck.getTruckCode());
        values.put(KEY_TRUCK_MODEL, truck.getTruckModel());
        db.insert(TABLE_TRUCKS,null,values);
        db.close();
    }
    Truck getTruck(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TRUCKS,
                new String[]{KEY_ID,KEY_PLAQUE, KEY_TRUCK_CODE, KEY_TRUCK_MODEL},
                KEY_ID + "=?",
                new String[]{"'"+id+"'"},null,null,null);
        if (cursor!=null){
            cursor.moveToFirst();
        }
        Truck truck = new Truck(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3));
        return truck;
    }
    public List<User> getAllTrucks(){
        List<User> contactList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_TRUCKS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        if (cursor.moveToFirst()){
            do {
                User contact = new User();
                contact.setId(cursor.getString(0));
                contact.setUserName(cursor.getString(1));
                contact.setFirstName(cursor.getString(2));
                contact.setLastName(cursor.getString(3));
                contact.setPhoto(cursor.getString(4));

                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        return contactList;
    }

    public int updateTruck(Truck truck){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, truck.getId());
        values.put(KEY_PLAQUE, truck.getPlaque());
        values.put(KEY_TRUCK_CODE, truck.getTruckCode());
        values.put(KEY_TRUCK_MODEL, truck.getTruckModel());

        return db.update(TABLE_TRUCKS,values,
                KEY_ID + " =?",new String[]{"'"+ truck.getId()+"'"});

    }

    public void deleteTruck(Truck truck){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRUCKS,
                KEY_ID + "=?",new String[]{"'"+ truck.getId()+"'"});
        db.close();
    }

    public int getTrucksCount(){
        String countQuery=  "SELECT * FROM " + TABLE_TRUCKS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery,null);
        cursor.close();

        return cursor.getCount();
    }
}
