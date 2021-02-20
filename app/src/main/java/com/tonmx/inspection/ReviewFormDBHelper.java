package com.tonmx.inspection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;

public class ReviewFormDBHelper extends SQLiteOpenHelper {


    private final static String DATABASE_NAME = "reviewform.db";
    private final static int DATABASE_VERSION = 1;
    private final static String TABLE_NAME = "reviewform";
    SQLiteDatabase database;

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

    public ReviewFormDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = getReadableDatabase();
    }

    private final static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
            TMConstantData.ReviewForm.DBCollums.ID + " INTEGER PRIMARY KEY autoincrement," +
            TMConstantData.ReviewForm.DBCollums.SHEETID + " INTEGER," +
            TMConstantData.ReviewForm.DBCollums.SHEETLOGID + " INTEGER," +
            TMConstantData.ReviewForm.DBCollums.FORMID + " INTEGER," +
            TMConstantData.ReviewForm.DBCollums.NUMBER + " INTEGER," +
            TMConstantData.ReviewForm.DBCollums.PAGING + " TEXT," +
            TMConstantData.ReviewForm.DBCollums.DEVICESEAT + " TEXT," +
            TMConstantData.ReviewForm.DBCollums.DEVICENAME + " TEXT," +
            TMConstantData.ReviewForm.DBCollums.STATUS + " TEXT," +
            TMConstantData.ReviewForm.DBCollums.REVIEW + " TEXT," +
            TMConstantData.ReviewForm.DBCollums.REMARKS + " TEXT," +
            TMConstantData.ReviewForm.DBCollums.TYPE + " INTEGER," +
            TMConstantData.ReviewForm.DBCollums.CREATETIME + " TEXT" +
            ")";


    public  long insertForm(ReviewTempleteItem item,int sheetId) {
        long count = database.insert(TABLE_NAME, null, createInsertContentValues(item,sheetId));
        return count;
    }

    public ContentValues createInsertContentValues(ReviewTempleteItem item,int sheetlogid) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TMConstantData.ReviewForm.DBCollums.FORMID, item.getId());
        contentValues.put(TMConstantData.ReviewForm.DBCollums.SHEETLOGID, sheetlogid);
        contentValues.put(TMConstantData.ReviewForm.DBCollums.NUMBER, item.getNumber());
        contentValues.put(TMConstantData.ReviewForm.DBCollums.PAGING, item.getPaging());
        contentValues.put(TMConstantData.ReviewForm.DBCollums.CREATETIME, item.getCreateDatetime());
        contentValues.put(TMConstantData.ReviewForm.DBCollums.DEVICESEAT, item.getDeviceSeat());
        contentValues.put(TMConstantData.ReviewForm.DBCollums.DEVICENAME, item.getDeviceName());
        contentValues.put(TMConstantData.ReviewForm.DBCollums.REVIEW, item.getReview());
        contentValues.put(TMConstantData.ReviewForm.DBCollums.REMARKS, item.getRemarks());
        contentValues.put(TMConstantData.ReviewForm.DBCollums.TYPE, item.getType());
        contentValues.put(TMConstantData.ReviewForm.DBCollums.STATUS, item.getStatus());
        return contentValues;
    }

    public int deleteReviewForm(int sheetid) {
        SQLiteDatabase database = getWritableDatabase();
        int result = database.delete(TABLE_NAME, TMConstantData.ReviewForm.DBCollums.SHEETLOGID + "=?", new String[]{String.valueOf(sheetid)});
        return result;
    }

    public LinkedList<ReviewTempleteItem> readReviewList(int sheetid){
        LinkedList<ReviewTempleteItem> list =new LinkedList<>();
        if (list != null || list.size() > 0) {
            list.clear();
        }

        String selectWhere = "sheetlogid = ?";
        Cursor c = null;
        database.beginTransaction();
        try {

            c = database.query(TABLE_NAME, new String[]{TMConstantData.FormName.DBCollums.ID,
                            TMConstantData.ReviewForm.DBCollums.FORMID,
                            TMConstantData.ReviewForm.DBCollums.SHEETID,
                            TMConstantData.ReviewForm.DBCollums.SHEETLOGID,
                            TMConstantData.ReviewForm.DBCollums.NUMBER,
                            TMConstantData.ReviewForm.DBCollums.PAGING,
                            TMConstantData.ReviewForm.DBCollums.CREATETIME,
                            TMConstantData.ReviewForm.DBCollums.DEVICESEAT,
                            TMConstantData.ReviewForm.DBCollums.DEVICENAME,
                            TMConstantData.ReviewForm.DBCollums.REVIEW,
                            TMConstantData.ReviewForm.DBCollums.REMARKS,
                            TMConstantData.ReviewForm.DBCollums.TYPE,
                            TMConstantData.ReviewForm.DBCollums.STATUS
                    },
                    selectWhere, new String[]{String.valueOf(sheetid)}, null, null, null);
            if (c == null) return null;

            int formidIndex = c.getColumnIndex(TMConstantData.ReviewForm.DBCollums.FORMID);
            int sheetidIndex=c.getColumnIndex(TMConstantData.ReviewForm.DBCollums.SHEETID);
            int sheetlogidIndex=c.getColumnIndex(TMConstantData.ReviewForm.DBCollums.SHEETLOGID);
            int numIndex=c.getColumnIndex(TMConstantData.ReviewForm.DBCollums.NUMBER);
            int pagingIndex=c.getColumnIndex(TMConstantData.ReviewForm.DBCollums.PAGING);
            int creattimeIndex=c.getColumnIndex(TMConstantData.ReviewForm.DBCollums.CREATETIME);
            int devseatIndex=c.getColumnIndex(TMConstantData.ReviewForm.DBCollums.DEVICESEAT);
            int devnameIndex=c.getColumnIndex(TMConstantData.ReviewForm.DBCollums.DEVICENAME);
            int reviewIndex=c.getColumnIndex(TMConstantData.ReviewForm.DBCollums.REVIEW);
            int remarksIndex=c.getColumnIndex(TMConstantData.ReviewForm.DBCollums.REMARKS);
            int typeIndex=c.getColumnIndex(TMConstantData.ReviewForm.DBCollums.TYPE);
            int statusIndex=c.getColumnIndex(TMConstantData.ReviewForm.DBCollums.STATUS);



            while(c.moveToNext()) {
                ReviewTempleteItem item = new ReviewTempleteItem();
                item.setId(c.getInt(formidIndex));
                item.setSheetid(c.getInt(sheetidIndex));
                item.setSheetLogId(c.getInt(sheetlogidIndex));
                item.setDeviceSeat(c.getString(devseatIndex));
                item.setDeviceName(c.getString(devnameIndex));
                item.setCreateDatetime(c.getLong(creattimeIndex));
                item.setReview(c.getString(reviewIndex));
                item.setStatus(c.getString(statusIndex));
                item.setType(c.getInt(typeIndex));
                item.setRemarks(c.getString(remarksIndex));
                item.setPaging(c.getString(pagingIndex));
                item.setNumber(c.getInt(numIndex));
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
}
