package com.quang.googleio.hanoi.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.quang.googleio.hanoi.model.Topic;

import java.util.ArrayList;

public class MySQLiteController extends SQLiteOpenHelper {
    private static final String DB_NAME = "data.sqlite";
    private static final String TABLE_TOPICS = "topics";

    public MySQLiteController(Context context) {
        super(context, DB_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_TOPICS + " (" +
                " id VARCHAR(255) PRIMARY KEY," +
                " level TEXT," +
                " duration TEXT," +
                " name TEXT," +
                " content TEXT," +
                " location TEXT," +
                " color TEXT," +
                " topictype TEXT," +
                " timestart TEXT," +
                " timeend TEXT," +
                " start TEXT," +
                " speaker TEXT," +
                " is_booked INTEGER" +
                ")");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOPICS);
        onCreate(db);
    }

    public ArrayList<Topic> getAllTopics() {
        ArrayList<Topic> listTopics = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TOPICS, null);
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String level = cursor.getString(1);
                String duration = cursor.getString(2);
                String name = cursor.getString(3);
                String content = cursor.getString(4);
                String location = cursor.getString(5);
                String color = cursor.getString(6);
                String topictype = cursor.getString(7);
                String timestart = cursor.getString(8);
                String timeend = cursor.getString(9);
                String start = cursor.getString(10);
                String speaker = cursor.getString(11);
                boolean isBooked = cursor.getInt(12) == 1;
                listTopics.add(new Topic(id, level, duration, name, content, location, color, topictype, timestart, timeend, start, speaker, isBooked));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return listTopics;
    }

    public ArrayList<Topic> getAllTopicsByRoom(String roomName) {
        ArrayList<Topic> listTopics = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_TOPICS, null, "location = ?", new String[]{roomName}, null, null, null);
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String level = cursor.getString(1);
                String duration = cursor.getString(2);
                String name = cursor.getString(3);
                String content = cursor.getString(4);
                String location = cursor.getString(5);
                String color = cursor.getString(6);
                String topictype = cursor.getString(7);
                String timestart = cursor.getString(8);
                String timeend = cursor.getString(9);
                String start = cursor.getString(10);
                String speaker = cursor.getString(11);
                boolean isBooked = cursor.getInt(12) == 1;
                listTopics.add(new Topic(id, level, duration, name, content, location, color, topictype, timestart, timeend, start, speaker, isBooked));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return listTopics;
    }

    public boolean insertTopic(Topic topic) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues content = new ContentValues();
            content.put("id", topic.getId());
            content.put("level", topic.getLevel());
            content.put("duration", topic.getDuration());
            content.put("name", topic.getName());
            content.put("content", topic.getContent());
            content.put("location", topic.getLocation());
            content.put("color", topic.getColor());
            content.put("topictype", topic.getTopictype());
            content.put("timestart", topic.getTimestart());
            content.put("timeend", topic.getTimeend());
            content.put("start", topic.getStart());
            content.put("speaker", topic.getSpeaker());
            content.put("is_booked", topic.isBooked() ? 1 : 0);
            if (db.insert(TABLE_TOPICS, null, content) == -1) {
                close();
                return false;
            }
            close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return false;
    }

    public boolean updateStatusOfTopic(String topicId, boolean isBooked) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues content = new ContentValues();
            content.put("is_booked", isBooked ? 1 : 0);
            if (db.update(TABLE_TOPICS, content, "id = ?", new String[]{topicId}) == -1) {
                close();
                return false;
            }
            close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return false;
    }

    public boolean updateTopic(Topic topic) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues content = new ContentValues();
            content.put("level", topic.getLevel());
            content.put("duration", topic.getDuration());
            content.put("name", topic.getName());
            content.put("content", topic.getContent());
            content.put("location", topic.getLocation());
            content.put("color", topic.getColor());
            content.put("topictype", topic.getTopictype());
            content.put("timestart", topic.getTimestart());
            content.put("timeend", topic.getTimeend());
            content.put("start", topic.getStart());
            content.put("speaker", topic.getSpeaker());
            content.put("is_booked", topic.isBooked());
            if (db.update(TABLE_TOPICS, content, "id = " + topic.getId(), null) == -1) {
                close();
                return false;
            }
            close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return false;
    }

    public boolean deleteTopic(String topicId) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            if (db.delete(TABLE_TOPICS, "id = " + topicId, null) == -1) {
                close();
                return false;
            }
            close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return false;
    }

    public boolean deleteAllTopic() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            if (db.delete(TABLE_TOPICS, null, null) == -1) {
                close();
                return false;
            }
            close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return false;
    }

    public boolean resetStatusAllTopic() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues content = new ContentValues();
            content.put("is_booked", 0);
            if (db.update(TABLE_TOPICS, content, "is_booked = 1", null) == -1) {
                close();
                return false;
            }
            close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return false;
    }
}
