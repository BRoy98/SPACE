package com.hometsolutions.space.Utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by Bishwajyoti Roy on 11/1/2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_PATH = "/data/data/com.hometsolutions.space/databases/";
    private static final String DB_NAME = "database.db";
    private static final int DB_VERSION = 1;

    private static final String TB_ROOMS = "ROOMS";
    private static final String TB_LIGHTS = "LIGHT_DATA";
    private static final String TB_FANS = "FAN_DATA";
    private static final String TB_WINDOWS = "WINDOW_DATA";
    private static final String COL_ROOM_NAME = "ROOM_NAME";
    private static final String COL_LIGHT_NAME = "LIGHT_NAME";
    private static final String COL_LIGHT_ID = "LIGHT_ID";
    private static final String COL_FAN_NAME = "FAN_NAME";
    private static final String COL_FAN_ID = "FAN_ID";
    private static final String DATETIME = "DATETIME";
    private static final String COL_WINDOW_NAME = "WINDOW_NAME";
    private static final String COL_WINDOW_ID = "WINDOW_ID";

    private SQLiteDatabase myDB;
    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @Override
    public synchronized void close() {
        if (myDB != null) {
            myDB.close();
        }
        super.close();
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //                                                                          TABLE 'ROOMS' ENQUERYS START
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public List<String> getRoomsArrayList() {
        List<String> listRooms = new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c;

        try {
            c = db.rawQuery("SELECT * FROM " + TB_ROOMS, null);
            if (c == null) return null;

            String name;
            c.moveToFirst();
            do {
                name = c.getString(1);
                listRooms.add(name);
            } while (c.moveToNext());
            c.close();
        } catch (Exception e) {
            Log.e("SPACE", e.getMessage());
        }

        db.close();

        return listRooms;
    }

    /***
     * Get all ROOMs in a array from database
     * @return String[]
     * @throws Exception
     */
    public String[] getRoomsArray() {
        String[] listRooms = new String[999];
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c;
        String name;
        int i = 0;
        try {
            c = db.rawQuery("SELECT * FROM " + TB_ROOMS, null);
            if (c == null) return null;
            c.moveToFirst();
            do {
                name = c.getString(c.getColumnIndex(COL_ROOM_NAME));
                listRooms[i] = name.replaceAll("_", " ");
                i++;
            } while (c.moveToNext());
            c.close();
        } catch (Exception e) {
            Log.e("SPACE", e.getMessage());
        }

        db.close();
        return listRooms;
    }

    /***
     * Get ROOM count from database
     *
     * @throws Exception
     */
    public int getRoomCount() {
        String countQuery = "SELECT  * FROM " + TB_ROOMS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        db.close();
        return (cnt);
    }

    /***
     * Get the ROOM name by row number from database
     *
     * @throws Exception
     */
    public String roomNameByID(int id) {
        String[] roomArray = getRoomsArray();
        return roomArray[id];
    }

    public String roomIDByName(String roomName) {
        roomName = roomName.replaceAll(" ", "_");
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c;
        String roomID = null;
        int i = 0;
        try {
            c = db.rawQuery("SELECT * FROM " + TB_ROOMS + " WHERE "+ COL_ROOM_NAME + " = '"+roomName+"'", null);
            if (c == null) return null;
            c.moveToFirst();
            do {
                roomID = c.getString(c.getColumnIndex("ID"));
                i++;
            } while (c.moveToNext());
            c.close();
        } catch (Exception e) {
            Log.e("SPACE", e.getMessage());
        }
        return roomID;
    }

    /***
     * Add ROOM into database
     *
     * @throws Exception
     */
    public void addRoom(String rName){
        rName = rName.replaceAll(" ", "_");
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_ROOM_NAME, rName);
            this.getWritableDatabase().insertOrThrow(TB_ROOMS, "", contentValues);
        } catch (Exception e) {
            Log.e("SPACE - addRoom", e.getMessage());
        }
        db.close();
    }

    /***
     * Delete a ROOM from database
     *
     * @throws Exception
     */
    public void deleteRoom(String romName) {
        int roomID = Integer.valueOf(roomIDByName(romName));;
        romName = romName.replaceAll(" ", "_");
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TB_ROOMS, COL_ROOM_NAME + "='"+romName+"'", null);
        FAN_deleteAllByRoom(roomID - 1);
        db.close();
    }

    /***
     * Rename a ROOM
     *
     * @throws Exception
     */
    public void renameRoom(String oldName, String newName) {
        oldName = oldName.replaceAll(" ", "_");
        newName = newName.replaceAll(" ", "_");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TB_ROOMS + " SET " + COL_ROOM_NAME + "='"+newName+"' WHERE "+ COL_ROOM_NAME + "='"+oldName+"'");
        db.close();
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //                                                                          TABLE 'LIGHT_DATA' ENQUERYS START
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public void LIGHT_AddLight(String lightName, int roomID) {
        lightName = lightName.replaceAll(" ", "_");
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("ID", roomID);
            contentValues.put(COL_LIGHT_ID, generateID());
            contentValues.put(COL_LIGHT_NAME, lightName);
            contentValues.put(DATETIME, getDateTime());
            this.getWritableDatabase().insertOrThrow(TB_LIGHTS   , "", contentValues);
        } catch (Exception e) {
            Log.e("SPACE - addLight", e.getMessage());
        }
        db.close();
    }

    public int LIGHT_getCount(int roomID) {
        String countQuery = "SELECT  * FROM " + TB_LIGHTS + " WHERE ID = " + roomID;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        db.close();
        return (cnt);
    }

    public String LIGHT_getID(String lightName) {
        lightName = lightName.replaceAll(" ", "_");
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c;
        String ID = null;
        int i = 0;
        try {
            c = db.rawQuery("SELECT * FROM " + TB_LIGHTS + " WHERE " + COL_LIGHT_NAME +  "='"+lightName+"'", null);
            if (c == null) return null;
            c.moveToFirst();
            do {
                ID = c.getString(c.getColumnIndex(COL_LIGHT_ID));
                ID = ID.replaceAll("_", " ");
                i++;
            } while (c.moveToNext());
            c.close();
        } catch (Exception e) {
            Log.e("SPACE", e.getMessage());
        }
        db.close();
        return ID;
    }

    public String[] LIGHT_getNames(int roomID) {
        String[] lightNames = new String[999];
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c;
        String name;
        int i = 0;
        try {
            c = db.rawQuery("SELECT * FROM " + TB_LIGHTS + " WHERE ID = " + roomID, null);
            if (c == null) return null;
            c.moveToFirst();
            do {
                name = c.getString(c.getColumnIndex(COL_LIGHT_NAME));
                lightNames[i] = name.replaceAll("_", " ");
                i++;
            } while (c.moveToNext());
            c.close();
        } catch (Exception e) {
            Log.e("SPACE", e.getMessage());
        }

        db.close();
        return lightNames;
    }

    public String LIGHT_getTime(String lightName) {
        lightName = lightName.replaceAll(" ", "_");
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c;
        String ID = null;
        int i = 0;
        try {
            c = db.rawQuery("SELECT * FROM " + TB_LIGHTS + " WHERE " + COL_LIGHT_NAME + "='"+lightName+"'", null);
            if (c == null) return null;
            c.moveToFirst();
            do {
                ID = c.getString(c.getColumnIndex(DATETIME));
                ID = ID.replaceAll("_", " ");
                i++;
            } while (c.moveToNext());
            c.close();
        } catch (Exception e) {
            Log.e("SPACE", e.getMessage());
        }

        db.close();
        return ID;
    }

    public void LIGHT_rename(String oldName, String newName) {
        oldName = oldName.replaceAll(" ", "_");
        newName = newName.replaceAll(" ", "_");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TB_LIGHTS + " SET " + COL_LIGHT_NAME + "='"+newName+"' WHERE "+ COL_LIGHT_NAME + "='"+oldName+"'");
        db.close();
    }

    public void LIGHT_delete(String lightName) {
        lightName = lightName.replaceAll(" ", "_");
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TB_LIGHTS, COL_LIGHT_NAME + " = '"+lightName+"'", null);
        db.close();
    }

    public void LIGHT_deleteAllByRoom(int roomID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TB_LIGHTS, "ID = '"+roomID+"'", null);
        db.close();
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //                                                                          TABLE 'FAN_DATA' ENQUERYS START
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public void FAN_AddFan(String fanName, int roomID) {
        fanName = fanName.replaceAll(" ", "_");
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("ID", roomID);
            contentValues.put(COL_FAN_ID, generateID());
            contentValues.put(COL_FAN_NAME, fanName);
            contentValues.put(DATETIME, getDateTime());
            this.getWritableDatabase().insertOrThrow(TB_FANS, "", contentValues);
        } catch (Exception e) {
            Log.e("SPACE - FAN_addFan", e.getMessage());
        }
        db.close();
    }

    public int FAN_getCount(int roomID) {
        String countQuery = "SELECT  * FROM " + TB_FANS + " WHERE ID = " + roomID;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        db.close();
        return (cnt);
    }

    /***
     * Get the unique ID of the fan with given fan name
     */
    public String FAN_getID(String fanName) {
        fanName = fanName.replaceAll(" ", "_");
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c;
        String ID = null;
        int i = 0;
        try {
            c = db.rawQuery("SELECT * FROM " + TB_FANS + " WHERE " + COL_FAN_NAME +  "='"+fanName+"'", null);
            if (c == null) return null;
            c.moveToFirst();
            do {
                ID = c.getString(c.getColumnIndex(COL_FAN_ID));
                ID = ID.replaceAll("_", " ");
                i++;
            } while (c.moveToNext());
            c.close();
        } catch (Exception e) {
            Log.e("SPACE", e.getMessage());
        }
        db.close();
        return ID;
    }
    
    /***
     * Get names of all the fans in a room with given room ID
     */
    public String[] FAN_getNames(int roomID) {
        String[] fanNames = new String[999];
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c;
        String name;
        int i = 0;
        try {
            c = db.rawQuery("SELECT * FROM " + TB_FANS + " WHERE ID = " + roomID, null);
            if (c == null) return null;
            c.moveToFirst();
            do {
                name = c.getString(c.getColumnIndex(COL_FAN_NAME));
                fanNames[i] = name.replaceAll("_", " ");
                i++;
            } while (c.moveToNext());
            c.close();
        } catch (Exception e) {
            Log.e("SPACE", e.getMessage());
        }

        db.close();
        return fanNames;
    }

    /***
     * Get the 'time added' of the fan with given fan name
     */
    public String FAN_getTime(String fanName) {
        fanName = fanName.replaceAll(" ", "_");
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c;
        String ID = null;
        int i = 0;
        try {
            c = db.rawQuery("SELECT * FROM " + TB_FANS + " WHERE " + COL_FAN_NAME + "='"+fanName+"'", null);
            if (c == null) return null;
            c.moveToFirst();
            do {
                ID = c.getString(c.getColumnIndex(DATETIME));
                ID = ID.replaceAll("_", " ");
                i++;
            } while (c.moveToNext());
            c.close();
        } catch (Exception e) {
            Log.e("SPACE", e.getMessage());
        }

        db.close();
        return ID;
    }

    public void FAN_rename(String oldName, String newName) {
        oldName = oldName.replaceAll(" ", "_");
        newName = newName.replaceAll(" ", "_");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TB_FANS + " SET " + COL_FAN_NAME + "='"+newName+"' WHERE "+ COL_FAN_NAME + "='"+oldName+"'");
        db.close();
    }

    public void FAN_reGenerate(String fanName) {
        fanName = fanName.replaceAll(" ", "_");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TB_FANS + " SET " + COL_FAN_ID + "='"+generateID()+"' WHERE "+ COL_FAN_NAME + "='"+fanName+"'");
        db.close();
    }

    /***
     * Delete a fan with given fan name
     */
    public void FAN_delete(String fanName) {
        fanName = fanName.replaceAll(" ", "_");
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TB_FANS, COL_FAN_NAME + " = '"+fanName+"'", null);
        db.close();
    }

    public void FAN_deleteAllByRoom(int roomID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TB_FANS, "ID = '"+roomID+"'", null);
        db.close();
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //                                                                          DATABASE HANDLEING START
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    /***
     * Open database
     *
     * @throws SQLException
     */
    public void openDataBase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        myDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    /***
     * Copy database from source code assets to device
     *
     * @throws IOException
     */
    private void copyDataBase() throws IOException {
        try {
            InputStream myInput = context.getAssets().open(DB_NAME);
            String outputFileName = DB_PATH + DB_NAME;
            OutputStream myOutput = new FileOutputStream(outputFileName);

            byte[] buffer = new byte[1024];
            int length;

            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (Exception e) {
            Log.e("SPACE - copyDatabase", e.getMessage());
        }

    }

    /***
     * Check if the database doesn't exist on device, create new one
     *
     * @throws IOException
     */
    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();

        if (!dbExist) {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                Log.e("SPACE - create", e.getMessage());
            }
        }
    }


    /***
     * Check if the database is exist on device or not
     *
     * @return
     */
    private boolean checkDataBase() {
        SQLiteDatabase tempDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            tempDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READWRITE);
        } catch (SQLiteException e) {
            Log.e("SPACE - check", e.getMessage());
        }
        if (tempDB != null)
            tempDB.close();
        return tempDB != null;
    }

    @NonNull
    private String generateID() {
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder();//((100000 + rnd.nextInt(900000)) + "-");
        for (int i = 0; i < 4; i++)
            sb.append(chars[rnd.nextInt(chars.length)]);

        return sb.toString();
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String[] sortArray(String[] array, int arrayCount) {
        String sortedArray[] = new String[999];
        for(int i=0; i<arrayCount; i++)
        {
            for(int j=1; j<arrayCount; j++)
            {
                if(array[j-1].compareTo(sortedArray[j])>0)
                {
                    String temp = sortedArray[j-1];
                    sortedArray[j-1]=sortedArray[j];
                    sortedArray[j]=temp;
                }
            }
        }
        return sortedArray;
    }
}
