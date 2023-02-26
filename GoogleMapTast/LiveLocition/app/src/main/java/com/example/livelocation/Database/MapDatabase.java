package com.example.livelocation.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.livelocation.Modal;

import java.util.ArrayList;

public class MapDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "AddressDatabase";
    private static final int DATABSE_VERSION = 1;

    public MapDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABSE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("create table Address(c_id integer primary key autoincrement,AreaName text,City text,State text,Country text,Landmark text,Pincode text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("drop table if exists Address");
        onCreate(sqLiteDatabase);
    }


    public void addaddress(Modal modal) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("AreaName", modal.getAreaName());
        values.put("City", modal.getCity());
        values.put("State", modal.getState());
        values.put("Country", modal.getCountry());
        values.put("Landmark", modal.getLandmark());
        values.put("Pincode", modal.getPincode());

        db.insert("Address", null, values);
    }

    public ArrayList<Modal> get_Address() {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Address", null);

        ArrayList<Modal> list = new ArrayList<>();

        while (cursor.moveToNext()) {

            Modal modal = new Modal();

            modal.setC_id(cursor.getInt(0));
            modal.setAreaName(cursor.getString(1));
            modal.setCity(cursor.getString(2));
            modal.setState(cursor.getString(3));
            modal.setCountry(cursor.getString(4));
            modal.setLandmark(cursor.getString(5));
            modal.setPincode(cursor.getString(6));

            list.add(modal);
        }
        return list;
    }

    public void deletedata(int id) {
        SQLiteDatabase database = this.getWritableDatabase();

        database.delete("Address", "c_id=?", new String[]{String.valueOf(id)});

    }
}
