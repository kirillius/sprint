package ru.kirillius.sprint.models;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import ru.kirillius.sprint.service.BasicFunctionDB;
import ru.kirillius.sprint.service.CommonHelper;

public class Event {
    public String name;
    public Date dateEvent;
    public boolean cyclical;
    public int rememberMinute, ID;
    private String tableName = "events";

    private String DB_CREATE_TABLE = "CREATE TABLE events ( _id INTEGER PRIMARY KEY, Name TEXT NULL, dateEvent DATETIME NULL, cyclical bit NULL, rememberMinute INTEGER NULL)";
    private String DB_DROP_TABLE = "DROP TABLE IF EXISTS events";

    public String getDB_CREATE_TABLE() {
        return DB_CREATE_TABLE;
    }

    public void setDB_CREATE_TABLE(String DB_CREATE_TABLE) {
        this.DB_CREATE_TABLE = DB_CREATE_TABLE;
    }

    public String getDB_DROP_TABLE() {
        return DB_DROP_TABLE;
    }

    public void setDB_DROP_TABLE(String DB_DROP_TABLE) {
        this.DB_DROP_TABLE = DB_DROP_TABLE;
    }

    public ArrayList<Event> getEventsFromDB(Context context) {
        BasicFunctionDB bfDB = new BasicFunctionDB(context);
        ArrayList<HashMap<String, String>> values = bfDB.get(tableName);

        ArrayList<Event> data = new ArrayList<>();
        for(HashMap<String, String> item: values) {
            Event currentItem = new Event();
            boolean addItem = false;

            if(item.get("_id")!=null) {
                currentItem.ID=Integer.parseInt(item.get("_id"));
                addItem=true;
            }

            if(item.get("name")!=null) {
                currentItem.name=item.get("name");
                addItem=true;
            }

            if(item.get("dateEvent")!=null) {
                currentItem.dateEvent= CommonHelper.getDateFromString(item.get("dateEvent"), "DD.MM.YYYY");
                addItem=true;
            }

            if(item.get("cyclical")!=null) {
                currentItem.cyclical=Boolean.parseBoolean(item.get("cyclical"));
                addItem=true;
            }

            if(item.get("rememberMinute")!=null) {
                currentItem.rememberMinute=Integer.parseInt(item.get("rememberMinute"));
                addItem=true;
            }

            if(addItem)
                data.add(currentItem);
        }

        return data;
    }

    public long getCountRecord(Context context) {
        return new BasicFunctionDB(context).getCount(tableName);
    }

    public String getTableName() {
        return tableName;
    }
}
