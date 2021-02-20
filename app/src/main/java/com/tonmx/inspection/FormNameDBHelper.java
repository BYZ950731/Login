package com.tonmx.inspection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;

public class FormNameDBHelper extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "formname.db";
    private final static int DATABASE_VERSION = 1;
    private final static String TABLE_NAME = "formname";
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
            TMConstantData.FormName.DBCollums.REVIEWER + " TEXT," +
            TMConstantData.FormName.DBCollums.REVIEWERID + " INTEGER," +
            TMConstantData.FormName.DBCollums.STATE + " INTEGER" +
            ")";



    public FormNameDBHelper(Context context){
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


    private ContentValues createInsertHandContentValues(Hand item) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TMConstantData.FormName.DBCollums.USERID, PrefData.getUserId());
        contentValues.put(TMConstantData.FormName.DBCollums.SHEETID, item.getId());
        contentValues.put(TMConstantData.FormName.DBCollums.SHEETLOGID, item.getSheetLogId());
        contentValues.put(TMConstantData.FormName.DBCollums.SHEETNAME, item.getSheetName());
        contentValues.put(TMConstantData.FormName.DBCollums.FILENAME, item.getFileName());
        contentValues.put(TMConstantData.FormName.DBCollums.UPDATETIME, item.getUpdateDatetime());
        contentValues.put(TMConstantData.FormName.DBCollums.CREATETIME, item.getCreateDatetime());
        contentValues.put(TMConstantData.FormName.DBCollums.REVIEWER, item.getReviewer());
        contentValues.put(TMConstantData.FormName.DBCollums.REVIEWERID, item.getrId());
        contentValues.put(TMConstantData.FormName.DBCollums.STATE, item.getState());
        return contentValues;
    }

    public  long insertHandForm(Hand item) {
        long count = database.insert(TABLE_NAME, null, createInsertHandContentValues(item));
        return count;
    }

    public boolean updateState(int userid,Hand item) {
        boolean result=false;

        String select=TMConstantData.FormName.DBCollums.USERID+ "=? and " + TMConstantData.FormName.DBCollums.SHEETID + "=? ";
        try {
            result = database.update(TABLE_NAME, updateStatusContentValues(item),
                    select,  new String[]{String.valueOf(userid),String.valueOf(item.getId())}) > 0;
        } catch (SQLException ex) {
            return false;
        }
        return result;
    }

    private ContentValues updateStatusContentValues(Hand item) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TMConstantData.FormName.DBCollums.STATE, item.getState());
        return contentValues;
    }



    public boolean updateSheetLogId(int userid,Hand item) {
        boolean result=false;

        String select=TMConstantData.FormName.DBCollums.USERID+ "=? and " + TMConstantData.FormName.DBCollums.SHEETID + "=?";
        try {
            result = database.update(TABLE_NAME, updateSheetLogIdContentValues(item),
                    select,  new String[]{String.valueOf(userid),String.valueOf(item.getId())}) > 0;
        } catch (SQLException ex) {
            return false;
        }
        return result;
    }

    private ContentValues updateSheetLogIdContentValues(Hand item) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TMConstantData.FormName.DBCollums.STATE, item.getState());
        contentValues.put(TMConstantData.FormName.DBCollums.SHEETLOGID, item.getSheetLogId());
        contentValues.put(TMConstantData.FormName.DBCollums.REVIEWER, item.getReviewer());
        contentValues.put(TMConstantData.FormName.DBCollums.REVIEWERID, item.getrId());
        return contentValues;
    }


    public boolean DataExist(Hand item){
        Cursor c = null;
        boolean result = false;
        try {
            c = database.rawQuery("select * from formname where  userid =? and sheetid =?",new String[]{String.valueOf(PrefData.getUserId()),String.valueOf(item.getId())});
            result = null != c && c.moveToFirst() ;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(null != c && !c.isClosed()){
                c.close() ;
            }
        }

        return result;

    }

    public int querystate(int sheedid){
        Cursor c = null;
        int state = -1;
        try {
            c = database.rawQuery("select state from formname where  userid =? and sheetid =?",new String[]{String.valueOf(PrefData.getUserId()),String.valueOf(sheedid)});
            if (c == null) return -1;
            int stateIndex=c.getColumnIndex(TMConstantData.FormName.DBCollums.STATE);
            while(c.moveToNext()) {
                state = c.getInt(stateIndex);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(null != c && !c.isClosed()){
                c.close() ;
            }
        }

        return state;

    }

    public LinkedList<Hand> readOwnList(){
        LinkedList<Hand> list =new LinkedList<>();
        if (list != null && list.size() > 0) {
            list.clear();
        }

        String selectWhere = "userid = ? and state != ?";
        Cursor c = null;
        database.beginTransaction();
        try {

            c = database.query(TABLE_NAME, new String[]{TMConstantData.FormName.DBCollums.ID,
                            TMConstantData.FormName.DBCollums.SHEETID,
                            TMConstantData.FormName.DBCollums.SHEETNAME,
                            TMConstantData.FormName.DBCollums.SHEETLOGID,
                            TMConstantData.FormName.DBCollums.FILENAME,
                            TMConstantData.FormName.DBCollums.UPDATETIME,
                            TMConstantData.FormName.DBCollums.CREATETIME,
                            TMConstantData.FormName.DBCollums.REVIEWER,
                            TMConstantData.FormName.DBCollums.REVIEWERID,
                            TMConstantData.FormName.DBCollums.STATE

                    },
                    selectWhere, new String[]{String.valueOf(PrefData.getUserId()),
                            String.valueOf(2)}, null, null, null);
            if (c == null) return null;

            int idIndex = c.getColumnIndex(TMConstantData.FormName.DBCollums.ID);
            int sheetlogidIndex = c.getColumnIndex(TMConstantData.FormName.DBCollums.SHEETLOGID);
            int formidIndex=c.getColumnIndex(TMConstantData.FormName.DBCollums.SHEETID);
            int sheetnameIndex=c.getColumnIndex(TMConstantData.FormName.DBCollums.SHEETNAME);
            int filenameIndex=c.getColumnIndex(TMConstantData.FormName.DBCollums.FILENAME);
            int updatetimeIndex=c.getColumnIndex(TMConstantData.FormName.DBCollums.UPDATETIME);
            int creattimeIndex=c.getColumnIndex(TMConstantData.FormName.DBCollums.CREATETIME);
            int stateIndex=c.getColumnIndex(TMConstantData.FormName.DBCollums.STATE);
            int reviewerIndex=c.getColumnIndex(TMConstantData.FormName.DBCollums.REVIEWER);
            int revieweridIndex=c.getColumnIndex(TMConstantData.FormName.DBCollums.REVIEWERID);


            while(c.moveToNext()) {
                Hand item = new Hand();
                item.setId(c.getInt(formidIndex));
                item.setSheetLogId(c.getInt(sheetlogidIndex));
                item.setSheetName(c.getString(sheetnameIndex));
                item.setFileName(c.getString(filenameIndex));
                item.setUpdateDatetime(c.getString(updatetimeIndex));
                item.setCreateDatetime(c.getString(creattimeIndex));
                item.setrId(c.getInt(revieweridIndex));
                item.setState(c.getInt(stateIndex));
                item.setReviewer(c.getString(reviewerIndex));
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

    public LinkedList<Hand>  readDoingList(int sheetid){
        LinkedList<Hand> list =new LinkedList<>();
        if (list != null || list.size() > 0) {
            list.clear();
        }

        String selectWhere = "userid = ? and state = ? and sheetid = ?";
        Cursor c = null;
        database.beginTransaction();
        try {

            c = database.query(TABLE_NAME, new String[]{TMConstantData.FormName.DBCollums.ID,
                            TMConstantData.FormName.DBCollums.SHEETID,
                            TMConstantData.FormName.DBCollums.SHEETNAME,
                            TMConstantData.FormName.DBCollums.FILENAME,
                            TMConstantData.FormName.DBCollums.UPDATETIME,
                            TMConstantData.FormName.DBCollums.CREATETIME,
                            TMConstantData.FormName.DBCollums.STATE
                    },
                    selectWhere, new String[]{String.valueOf(PrefData.getUserId()),
                            String.valueOf(1),String.valueOf(sheetid)}, null, null, null);
            if (c == null) return null;

            int idIndex = c.getColumnIndex(TMConstantData.FormName.DBCollums.ID);
            int formidIndex=c.getColumnIndex(TMConstantData.FormName.DBCollums.SHEETID);
            int sheetnameIndex=c.getColumnIndex(TMConstantData.FormName.DBCollums.SHEETNAME);
            int filenameIndex=c.getColumnIndex(TMConstantData.FormName.DBCollums.FILENAME);
            int updatetimeIndex=c.getColumnIndex(TMConstantData.FormName.DBCollums.UPDATETIME);
            int creattimeIndex=c.getColumnIndex(TMConstantData.FormName.DBCollums.CREATETIME);
            int stateIndex=c.getColumnIndex(TMConstantData.FormName.DBCollums.STATE);



            while(c.moveToNext()) {
                Hand item = new Hand();
                item.setId(c.getInt(formidIndex));
                item.setSheetName(c.getString(sheetnameIndex));
                item.setFileName(c.getString(filenameIndex));
                item.setUpdateDatetime(c.getString(updatetimeIndex));
                item.setCreateDatetime(c.getString(creattimeIndex));
                item.setState(c.getInt(stateIndex));
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

//    public boolean readDoingForm() {
//        String selectWhere = "userid = ? and state = ?";
//        Cursor c = null;
//        database.beginTransaction();
//        try {
//            c = database.query(TABLE_NAME, new String[]{TMConstantData.FormName.DBCollums.ID,
//                            TMConstantData.FormName.DBCollums.FORMID,
//                            TMConstantData.FormName.DBCollums.SHEETNAME,
//                            TMConstantData.FormName.DBCollums.FILENAME,
//                            TMConstantData.FormName.DBCollums.UPDATETIME,
//                            TMConstantData.FormName.DBCollums.CREATETIME,
//                            TMConstantData.FormName.DBCollums.INSPECTOR,
//                            TMConstantData.FormName.DBCollums.STATE
//                    },
//                    selectWhere, new String[]{String.valueOf(PrefData.getUserId()),
//                            String.valueOf(1)}, null, null, null);
//            if (c == null) return false;
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//            return false;
//        } finally {
//            database.endTransaction();
//            if (c != null) {
//                c.close();
//            }
//        }
//        return true;
//    }


    public int deleteNormal() {
        SQLiteDatabase database = getWritableDatabase();
        String selectWhere = "userid = ? and state = ?";
        int result = database.delete(TABLE_NAME, selectWhere, new String[]{String.valueOf(PrefData.getUserId()),String.valueOf(0)});
        return result;
    }

    public int deletePass() {
        SQLiteDatabase database = getWritableDatabase();
        String selectWhere = "userid = ? and state = ?";
        int result = database.delete(TABLE_NAME, selectWhere, new String[]{String.valueOf(PrefData.getUserId()),String.valueOf(4)});
        return result;
    }

    public int deleteReject() {
        SQLiteDatabase database = getWritableDatabase();
        String selectWhere = "userid = ? and state = ?";
        int result = database.delete(TABLE_NAME, selectWhere, new String[]{String.valueOf(PrefData.getUserId()),String.valueOf(3)});
        return result;
    }





}
