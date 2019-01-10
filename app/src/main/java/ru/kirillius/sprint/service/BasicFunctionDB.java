package ru.kirillius.sprint.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lavrentev on 26.10.2017.
 */

public class BasicFunctionDB {
    Context context;
    DBHelper dbHelper;

    public BasicFunctionDB(Context context) {
        this.context=context;
        this.dbHelper = new DBHelper(context);
    }

    public long insert(String tableName, HashMap<String, String> values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        for (Map.Entry<String,String> entry : values.entrySet()) {
            cv.put(entry.getKey(), entry.getValue());
        }

        if(values.size()>0) {
            long id = db.insert(tableName, null, cv);
            db.close();
            return id;
        }
        else {
            db.close();
            return 0;
        }
    }

    public boolean delete(String tableName, String field, String value)  {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        boolean result = db.delete(tableName, field + "=" + value, null) > 0;
        db.close();
        return result;
    }

    public long update(String tableName, HashMap<String, String> values, long _id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        for (Map.Entry<String,String> entry : values.entrySet()) {
            cv.put(entry.getKey(), entry.getValue());
        }

        if(values.size()>0) {
            String selection = "_id LIKE ?"; // where ID column = rowId (that is, selectionArgs)
            String[] selectionArgs = { String.valueOf(_id) };
            long id = db.update(tableName, cv, selection, selectionArgs);
            db.close();
            return id;
        }
        else {
            db.close();
            return _id;
        }
    }

    public ArrayList<HashMap<String, String>> get(String tableName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(tableName, null, null, null, null, null, "_id DESC");
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        while (cursor.moveToNext()) {
            HashMap<String, String> currentRow = new HashMap<>();
            for (String column : cursor.getColumnNames()) {
                currentRow.put(column, cursor.getString(cursor.getColumnIndex(column)));
            }

            if(currentRow.size()>0)
                data.add(currentRow);
        }

        db.close();
        return data;
    }

    public long getCount(String tableName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long countRecord  = DatabaseUtils.queryNumEntries(db, tableName);
        db.close();

        return countRecord;
    }

    public void deleteAll(String tableName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("delete from "+ tableName);
    }
}
