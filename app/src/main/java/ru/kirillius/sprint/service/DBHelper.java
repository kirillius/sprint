package ru.kirillius.sprint.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Lavrentev on 02.10.2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "ppDB.db";
    private static final int DB_VERSION = 1;
    private Context context;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*db.execSQL(new Specializations().getDB_CREATE_TABLE());
        db.execSQL(new Doctors().getDB_CREATE_TABLE());
        db.execSQL(new MOs().getDB_CREATE_TABLE());*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        /*Specializations spec = new Specializations();
        Doctors doctor = new Doctors();
        MOs mo = new MOs();

        db.execSQL(spec.getDB_DROP_TABLE());
        db.execSQL(doctor.getDB_DROP_TABLE());
        db.execSQL(mo.getDB_DROP_TABLE());*/

        /*db.execSQL(spec.getDB_CREATE_TABLE());
        db.execSQL(doctor.getDB_CREATE_TABLE());
        db.execSQL(mo.getDB_CREATE_TABLE());*/
    }
}
