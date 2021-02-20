package com.tonmx.inspection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.LinkedList;

class FormTemplateDBHelper extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "formtemplate.db";
    private final static int DATABASE_VERSION = 1;
    private final static String TABLE_NAME = "formtemplate";
    SQLiteDatabase database;
    private final static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
            TMConstantData.Template.DBCollums.ID + " INTEGER PRIMARY KEY autoincrement," +
            TMConstantData.Template.DBCollums.USERID + " INTEGER," +
            TMConstantData.Template.DBCollums.FORMID + " INTEGER," +
            TMConstantData.Template.DBCollums.SHEETID + " INTEGER," +
            TMConstantData.Template.DBCollums.NUMBER + " INTEGER," +
            TMConstantData.Template.DBCollums.PAGING + " TEXT," +
            TMConstantData.Template.DBCollums.DEVICESEAT + " TEXT," +
            TMConstantData.Template.DBCollums.DEVICENAME + " TEXT," +
            TMConstantData.Template.DBCollums.REVIEW + " TEXT," +
            TMConstantData.Template.DBCollums.TYPE + " INTEGER," +
            TMConstantData.Template.DBCollums.STATUS + " TEXT," +
            TMConstantData.Template.DBCollums.REMARKS + " TEXT," +
            TMConstantData.Template.DBCollums.CLICK + " INTEGER," +
            TMConstantData.Template.DBCollums.CREATETIME + " TEXT" +
            ")";
    public FormTemplateDBHelper(Context context){
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
    public  long insertForm(FormTempleteItem item) {
        long count = database.insert(TABLE_NAME, null, createInsertContentValues(item));
        return count;
    }

    public ContentValues createInsertContentValues(FormTempleteItem item) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TMConstantData.Template.DBCollums.USERID, item.getUserid());
        contentValues.put(TMConstantData.Template.DBCollums.FORMID, item.getId());
        contentValues.put(TMConstantData.Template.DBCollums.SHEETID, item.getSheetId());
        contentValues.put(TMConstantData.Template.DBCollums.NUMBER, item.getNumber());
        contentValues.put(TMConstantData.Template.DBCollums.PAGING, item.getPaging());
        contentValues.put(TMConstantData.Template.DBCollums.CREATETIME, item.getCreateTime());
        contentValues.put(TMConstantData.Template.DBCollums.DEVICESEAT, item.getDeviceSeat());
        contentValues.put(TMConstantData.Template.DBCollums.DEVICENAME, item.getDeviceName());
        contentValues.put(TMConstantData.Template.DBCollums.REVIEW, item.getReview());
        contentValues.put(TMConstantData.Template.DBCollums.REMARKS, item.getRemarks());
        contentValues.put(TMConstantData.Template.DBCollums.CLICK, item.getClick());
        contentValues.put(TMConstantData.Template.DBCollums.TYPE, item.getType());
        contentValues.put(TMConstantData.Template.DBCollums.STATUS, item.getStatus());
        return contentValues;
    }


    public LinkedList<FormTempleteItem> readPagingForShow(int sheetid,String paging){
        LinkedList<FormTempleteItem> list =new LinkedList<>();
        if (list != null || list.size() > 0) {
            list.clear();
        }

        String selectWhere = "sheetid = ? and paging = ? and userid = ?";
        Cursor c = null;
        database.beginTransaction();
        try {
            c = database.query(TABLE_NAME, new String[]{TMConstantData.Template.DBCollums.ID,
                            TMConstantData.Template.DBCollums.USERID,
                            TMConstantData.Template.DBCollums.FORMID,
                            TMConstantData.Template.DBCollums.SHEETID,
                            TMConstantData.Template.DBCollums.NUMBER,
                            TMConstantData.Template.DBCollums.PAGING,
                            TMConstantData.Template.DBCollums.DEVICESEAT,
                            TMConstantData.Template.DBCollums.DEVICENAME,
                            TMConstantData.Template.DBCollums.REVIEW,
                            TMConstantData.Template.DBCollums.TYPE,
                            TMConstantData.Template.DBCollums.CREATETIME,
                            TMConstantData.Template.DBCollums.REMARKS,
                            TMConstantData.Template.DBCollums.CLICK,
                            TMConstantData.Template.DBCollums.STATUS
                    },
                    selectWhere, new String[]{String.valueOf(sheetid),paging,String.valueOf(PrefData.getUserId())}, null, null, null);
            if (c == null) return null;
            int useridIndex = c.getColumnIndex(TMConstantData.Template.DBCollums.USERID);
            int formIndex = c.getColumnIndex(TMConstantData.Template.DBCollums.FORMID);
            int sheetidIndex=c.getColumnIndex(TMConstantData.Template.DBCollums.SHEETID);
            int numberIndex=c.getColumnIndex(TMConstantData.Template.DBCollums.NUMBER);
            int pagingIndex=c.getColumnIndex(TMConstantData.Template.DBCollums.PAGING);
            int deviceseatIndex=c.getColumnIndex(TMConstantData.Template.DBCollums.DEVICESEAT);
            int devicenameIndex=c.getColumnIndex(TMConstantData.Template.DBCollums.DEVICENAME);
            int reviewIndex=c.getColumnIndex(TMConstantData.Template.DBCollums.REVIEW);
            int typeIndex=c.getColumnIndex(TMConstantData.Template.DBCollums.TYPE);
            int creattimeIndex=c.getColumnIndex(TMConstantData.Template.DBCollums.CREATETIME);
            int remarksIndex=c.getColumnIndex(TMConstantData.Template.DBCollums.REMARKS);
            int clickIndex=c.getColumnIndex(TMConstantData.Template.DBCollums.CLICK);
            int statusIndex=c.getColumnIndex(TMConstantData.Template.DBCollums.STATUS);



            while(c.moveToNext()) {
                FormTempleteItem item = new FormTempleteItem();
                item.setUserid(c.getInt(useridIndex));
                item.setId(c.getInt(formIndex));
                item.setSheetId(c.getInt(sheetidIndex));
                item.setNumber(c.getInt(numberIndex));
                item.setPaging(c.getString(pagingIndex));
                item.setDeviceSeat(c.getString(deviceseatIndex));
                item.setDeviceName(c.getString(devicenameIndex));
                item.setReview(c.getString(reviewIndex));
                item.setType(c.getInt(typeIndex));
                item.setCreateTime(c.getString(creattimeIndex));
                item.setRemarks(c.getString(remarksIndex));
                item.setClick(c.getInt(clickIndex));
                item.setStatus(c.getString(statusIndex));
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

    public LinkedList<FormTempleteItem> readALL(int sheetid){
        LinkedList<FormTempleteItem> list =new LinkedList<>();
        if (list != null || list.size() > 0) {
            list.clear();
        }

        String selectWhere = "sheetid = ? and userid = ? and status != ?";
        Cursor c = null;
        database.beginTransaction();
        try {
            c = database.query(TABLE_NAME, new String[]{TMConstantData.Template.DBCollums.ID,
                            TMConstantData.Template.DBCollums.USERID,
                            TMConstantData.Template.DBCollums.FORMID,
                            TMConstantData.Template.DBCollums.SHEETID,
                            TMConstantData.Template.DBCollums.NUMBER,
                            TMConstantData.Template.DBCollums.PAGING,
                            TMConstantData.Template.DBCollums.DEVICESEAT,
                            TMConstantData.Template.DBCollums.DEVICENAME,
                            TMConstantData.Template.DBCollums.REVIEW,
                            TMConstantData.Template.DBCollums.TYPE,
                            TMConstantData.Template.DBCollums.CREATETIME,
                            TMConstantData.Template.DBCollums.REMARKS,
                            TMConstantData.Template.DBCollums.CLICK,
                            TMConstantData.Template.DBCollums.STATUS
                    },
                    selectWhere, new String[]{String.valueOf(sheetid),String.valueOf(PrefData.getUserId()),String.valueOf(2)}, null, null, null);
            if (c == null) return null;

            int useridIndex = c.getColumnIndex(TMConstantData.Template.DBCollums.USERID);
            int formIndex = c.getColumnIndex(TMConstantData.Template.DBCollums.FORMID);
            int sheetidIndex=c.getColumnIndex(TMConstantData.Template.DBCollums.SHEETID);
            int numberIndex=c.getColumnIndex(TMConstantData.Template.DBCollums.NUMBER);
            int pagingIndex=c.getColumnIndex(TMConstantData.Template.DBCollums.PAGING);
            int deviceseatIndex=c.getColumnIndex(TMConstantData.Template.DBCollums.DEVICESEAT);
            int devicenameIndex=c.getColumnIndex(TMConstantData.Template.DBCollums.DEVICENAME);
            int reviewIndex=c.getColumnIndex(TMConstantData.Template.DBCollums.REVIEW);
            int typeIndex=c.getColumnIndex(TMConstantData.Template.DBCollums.TYPE);
            int creattimeIndex=c.getColumnIndex(TMConstantData.Template.DBCollums.CREATETIME);
            int remarksIndex=c.getColumnIndex(TMConstantData.Template.DBCollums.REMARKS);
            int clickIndex=c.getColumnIndex(TMConstantData.Template.DBCollums.CLICK);
            int statusIndex=c.getColumnIndex(TMConstantData.Template.DBCollums.STATUS);



            while(c.moveToNext()) {
                FormTempleteItem item = new FormTempleteItem();
                item.setUserid(c.getInt(useridIndex));
                item.setId(c.getInt(formIndex));
                item.setSheetId(c.getInt(sheetidIndex));
                item.setNumber(c.getInt(numberIndex));
                item.setPaging(c.getString(pagingIndex));
                item.setDeviceSeat(c.getString(deviceseatIndex));
                item.setDeviceName(c.getString(devicenameIndex));
                item.setReview(c.getString(reviewIndex));
                item.setType(c.getInt(typeIndex));
                item.setCreateTime(c.getString(creattimeIndex));
                item.setRemarks(c.getString(remarksIndex));
                item.setClick(c.getInt(clickIndex));
                item.setStatus(c.getString(statusIndex));
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

    public int readincompleteNumber(int sheetid){

        String selectWhere = "sheetid = ? and click = ? and userid = ?";
        Cursor c = null;
        database.beginTransaction();
        try {
            c = database.query(TABLE_NAME, new String[]{TMConstantData.Template.DBCollums.ID,
                            TMConstantData.Template.DBCollums.USERID,
                            TMConstantData.Template.DBCollums.FORMID,
                            TMConstantData.Template.DBCollums.SHEETID,
                            TMConstantData.Template.DBCollums.NUMBER,
                            TMConstantData.Template.DBCollums.PAGING,
                            TMConstantData.Template.DBCollums.DEVICESEAT,
                            TMConstantData.Template.DBCollums.DEVICENAME,
                            TMConstantData.Template.DBCollums.REVIEW,
                            TMConstantData.Template.DBCollums.TYPE,
                            TMConstantData.Template.DBCollums.CREATETIME,
                            TMConstantData.Template.DBCollums.REMARKS,
                            TMConstantData.Template.DBCollums.CLICK,
                            TMConstantData.Template.DBCollums.STATUS
                    },
                    selectWhere, new String[]{String.valueOf(sheetid),String.valueOf(0),String.valueOf(PrefData.getUserId())}, null, null, null);
            if (c == null) return -1;

            int numberIndex=c.getColumnIndex(TMConstantData.Template.DBCollums.NUMBER);




            while(c.moveToNext()) {
                return c.getInt(numberIndex);
            }
            database.setTransactionSuccessful();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        } finally {
            database.endTransaction();
            if (c != null) {
                c.close();
            }
        }
        return -1;
    }
    public boolean updateStatus(FormTempleteItem item) {
        String select = TMConstantData.Template.DBCollums.SHEETID+" =?"+" and "+TMConstantData.Template.DBCollums.NUMBER+" =?"+" and "+TMConstantData.Template.DBCollums.USERID+" =?";
        boolean result = database.update(TABLE_NAME, updateClickContentValues(item), select, new String[]{String.valueOf(item.getSheetId()),String.valueOf(item.getNumber()),String.valueOf(PrefData.getUserId())}) > 0;
        return result;
    }


    public boolean updateRemarks(FormTempleteItem item) {
        String select = TMConstantData.Template.DBCollums.SHEETID+" =?"+" and "+TMConstantData.Template.DBCollums.NUMBER+" =?"+" and "+TMConstantData.Template.DBCollums.USERID+" =?";
        boolean result = database.update(TABLE_NAME, updateRemarkContentValues(item), select, new String[]{String.valueOf(item.getSheetId()),String.valueOf(item.getNumber()),String.valueOf(PrefData.getUserId())}) > 0;
        return result;
    }


    public boolean updateClick(FormTempleteItem item) {
        String select = TMConstantData.Template.DBCollums.SHEETID+" =?"+" and "+TMConstantData.Template.DBCollums.NUMBER+" =?"+" and "+TMConstantData.Template.DBCollums.USERID+" =?";
        boolean result = database.update(TABLE_NAME, updateClickContentValues(item), select, new String[]{String.valueOf(item.getSheetId()),String.valueOf(item.getNumber()),String.valueOf(PrefData.getUserId())}) > 0;
        return result;
    }

    private ContentValues updateClickContentValues(FormTempleteItem item) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TMConstantData.Template.DBCollums.CLICK, item.getClick());
        contentValues.put(TMConstantData.Template.DBCollums.STATUS, item.getStatus());
        return contentValues;
    }

    private ContentValues updateRemarkContentValues(FormTempleteItem item) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TMConstantData.Template.DBCollums.REMARKS, item.getRemarks());
        return contentValues;
    }

    public int deleteNormal(int sheetid) {
        SQLiteDatabase database = getWritableDatabase();
        String selectWhere = "sheetid = ? and userid = ?";
        int result = database.delete(TABLE_NAME, selectWhere, new String[]{String.valueOf(sheetid),String.valueOf(PrefData.getUserId())});
        return result;
    }
}
