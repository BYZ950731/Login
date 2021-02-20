package com.tonmx.inspection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;

public class ReviewNameDBHelper extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "reviewname.db";
    private final static int DATABASE_VERSION = 1;
    private final static String TABLE_NAME = "reviewname";
    SQLiteDatabase database;

    private final static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
            TMConstantData.FormName.DBCollums.ID + " INTEGER PRIMARY KEY autoincrement," +
            TMConstantData.FormName.DBCollums.USERID + " INTEGER," +
            TMConstantData.FormName.DBCollums.SHEETID + " INTEGER," +
            TMConstantData.FormName.DBCollums.SHEETNAME + " TEXT," +
            TMConstantData.FormName.DBCollums.FILENAME + " TEXT," +
            TMConstantData.FormName.DBCollums.UPDATETIME + " TEXT," +
            TMConstantData.FormName.DBCollums.CREATETIME + " TEXT," +
            TMConstantData.FormName.DBCollums.SHEETLOGID + " INTEGER," +
            TMConstantData.FormName.DBCollums.INSPECTOR + " TEXT," +
            TMConstantData.FormName.DBCollums.STATE + " INTEGER" +
            ")";



    public ReviewNameDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = getReadableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != DATABASE_VERSION) {
            db.execSQL("drop table if exists " + TABLE_NAME);
            onCreate(db);
        }
    }

    private ContentValues createInsertTodoContentValues(Todo item) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TMConstantData.FormName.DBCollums.USERID, PrefData.getUserId());
        contentValues.put(TMConstantData.FormName.DBCollums.SHEETID, item.getId());
        contentValues.put(TMConstantData.FormName.DBCollums.SHEETNAME, item.getSheetName());
        contentValues.put(TMConstantData.FormName.DBCollums.FILENAME, item.getFileName());
        contentValues.put(TMConstantData.FormName.DBCollums.UPDATETIME, item.getUpdateDatetime());
        contentValues.put(TMConstantData.FormName.DBCollums.CREATETIME, item.getCreateDatetime());
        contentValues.put(TMConstantData.FormName.DBCollums.SHEETLOGID, item.getSheetLogId());
        contentValues.put(TMConstantData.FormName.DBCollums.INSPECTOR, item.getInspector());
        contentValues.put(TMConstantData.FormName.DBCollums.STATE, item.getState());
        return contentValues;
    }

    public  long insertTodoForm(Todo item) {
        long count = database.insert(TABLE_NAME, null, createInsertTodoContentValues(item));
        return count;
    }

    public LinkedList<Todo> readReviewList(){
        LinkedList<Todo> list =new LinkedList<>();
        if (list != null || list.size() > 0) {
            list.clear();
        }

        String selectWhere = "userid = ? and state = ?";
        Cursor c = null;
        database.beginTransaction();
        try {

            c = database.query(TABLE_NAME, new String[]{TMConstantData.FormName.DBCollums.ID,
                            TMConstantData.FormName.DBCollums.SHEETID,
                            TMConstantData.FormName.DBCollums.SHEETNAME,
                            TMConstantData.FormName.DBCollums.FILENAME,
                            TMConstantData.FormName.DBCollums.UPDATETIME,
                            TMConstantData.FormName.DBCollums.CREATETIME,
                            TMConstantData.FormName.DBCollums.SHEETLOGID,
                            TMConstantData.FormName.DBCollums.INSPECTOR,
                            TMConstantData.FormName.DBCollums.STATE
                    },
                    selectWhere, new String[]{String.valueOf(PrefData.getUserId()),
                            String.valueOf(2)}, null, null, null);
            if (c == null) return null;

            int idIndex = c.getColumnIndex(TMConstantData.FormName.DBCollums.ID);
            int formidIndex=c.getColumnIndex(TMConstantData.FormName.DBCollums.SHEETID);
            int sheetnameIndex=c.getColumnIndex(TMConstantData.FormName.DBCollums.SHEETNAME);
            int filenameIndex=c.getColumnIndex(TMConstantData.FormName.DBCollums.FILENAME);
            int updatetimeIndex=c.getColumnIndex(TMConstantData.FormName.DBCollums.UPDATETIME);
            int creattimeIndex=c.getColumnIndex(TMConstantData.FormName.DBCollums.CREATETIME);
            int sheetlogidIndex=c.getColumnIndex(TMConstantData.FormName.DBCollums.SHEETLOGID);
            int inspectorIndex=c.getColumnIndex(TMConstantData.FormName.DBCollums.INSPECTOR);
            int stateIndex=c.getColumnIndex(TMConstantData.FormName.DBCollums.STATE);



            while(c.moveToNext()) {
                Todo item = new Todo();
                item.setId(c.getInt(formidIndex));
                item.setSheetName(c.getString(sheetnameIndex));
                item.setFileName(c.getString(filenameIndex));
                item.setUpdateDatetime(c.getString(updatetimeIndex));
                item.setCreateDatetime(c.getString(creattimeIndex));
                item.setSheetLogId(c.getInt(sheetlogidIndex));
                item.setState(c.getInt(stateIndex));
                item.setInspector(c.getString(inspectorIndex));
                list.add(item);
            }
            database.setTransactionSuccessful();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        } finally {
            database.endTransaction();
            if (c != null) {
                c.close();
            }
        }
        return list;

    }

    public int deleteReview() {
        SQLiteDatabase database = getWritableDatabase();
        int result = database.delete(TABLE_NAME, TMConstantData.FormName.DBCollums.STATE + "=?", new String[]{String.valueOf(2)});
        return result;
    }
}
