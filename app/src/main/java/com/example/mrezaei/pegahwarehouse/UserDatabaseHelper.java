package com.example.mrezaei.pegahwarehouse;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class UserDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "PegahSqlite.db";
    private static final String TABLE_USERS = "users";
    private static final String KEY_ID = "id";
    private static final String KEY_FIRST_NAME = "firstName";
    private static final String KEY_LAST_NAME = "lastName";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_PHOTO = "photo";
    

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE="CREATE TABLE "+ TABLE_USERS + "("
                +KEY_ID + " INTEGER PRIMARY KEY," + KEY_USER_NAME  + " TEXT,"
                + KEY_FIRST_NAME +" TEXT," + KEY_LAST_NAME + "TEXT," + KEY_PHOTO + " TEXT"+")";
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

        onCreate(db);
    }

    public UserDatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    void addUser(User user){
        //First delete user if already exists
        deleteUser(user);
        //next insert new user
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FIRST_NAME,user.getFirstName());
        values.put(KEY_LAST_NAME,user.getLastName());
        values.put(KEY_PHOTO,user.getPhoto());
        values.put(KEY_USER_NAME,user.getUserName());

//        db.insertWithOnConflict(TABLE_USERS,null,values,1);

        db.insert(TABLE_USERS,null,values);
        db.close();

    }
    User getUser(String id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS,
                new String[]{KEY_ID,KEY_USER_NAME, KEY_FIRST_NAME, KEY_LAST_NAME,KEY_PHOTO},
                KEY_ID + "=?",
                new String[]{"'"+id+"'"},null,null,null);
        if (cursor!=null){
            cursor.moveToFirst();
        }

        User user = new User(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4));
        return user;
    }
    public List<User> getAllUsers(){
        List<User> contactList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_USERS;
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

    public int updateUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID,user.getId());
        values.put(KEY_USER_NAME,user.getUserName());
        values.put(KEY_FIRST_NAME,user.getFirstName());
        values.put(KEY_LAST_NAME,user.getLastName());
        values.put(KEY_PHOTO,user.getPhoto());

        return db.update(TABLE_USERS,values,
                KEY_ID + " =?",new String[]{String.valueOf(user.getId())});

    }

    public void deleteUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS,
                KEY_ID + "=?",new String[]{String.valueOf(user.getId())});
        db.close();
    }

    public int getContactsCount(){
        String countQuery=  "SELECT * FROM " + TABLE_USERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery,null);
        cursor.close();

        return cursor.getCount();
    }
}
