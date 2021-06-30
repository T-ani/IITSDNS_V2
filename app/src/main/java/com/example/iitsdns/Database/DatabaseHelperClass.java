package com.example.iitsdns.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.iitsdns.ModelClasses.PhoneNumberModelClass;
import com.example.iitsdns.ModelClasses.SPO2HeartModelClass;

import java.time.LocalDate;

public class DatabaseHelperClass extends SQLiteOpenHelper {

//    variable declaration here..........

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "WORKDATABASE";
    public static final String TABLE_NAME_1 = "SPO2_HEART";
    public static final String TABLE_NAME_2 = "TWO_PHONE_NUMBER";
    public static final String ID = "ID";
    public static final String SPO2 = "SPO2";
    public static final String HEART_RATE = "HEART_RATE";
    public static final String PHONE_NUMBER_1 = "PHONE_NUMBER_1";
    public static final String PHONE_NUMBER_2 = "PHONE_NUMBER_2";







//    variable declaration end here..........
//    constructor here..........

    public DatabaseHelperClass(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    //    constructor end here..........
    public static String CREATE_TABLE_1 = "CREATE TABLE " + TABLE_NAME_1 + " (" + ID + " INTEGER PRIMARY KEY, "+ SPO2 + " INTEGER, " + HEART_RATE + " INTEGER)";
    public static String CREATE_TABLE_2 = "CREATE TABLE " + TABLE_NAME_2 + " (" + ID + " INTEGER PRIMARY KEY, "+ PHONE_NUMBER_1 + " VARCHAR(100), " + PHONE_NUMBER_2 + " VARCHAR(100))";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_1);
//        db.execSQL("DROP TABLE CREATE_TABLE_2");
        db.execSQL(CREATE_TABLE_2);



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(CREATE_TABLE_2);

    }

//    now real work starts here..........
// two data class here..........

    public boolean addUpdateTaskForTwoValue(SPO2HeartModelClass spo2HeartModelClass){
        SQLiteDatabase sqLiteDatabaseRead;
        SQLiteDatabase sqLiteDatabaseWrite;
        sqLiteDatabaseRead = this.getReadableDatabase();
        sqLiteDatabaseWrite = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, 1);
        contentValues.put(SPO2, spo2HeartModelClass.getSPO2());
        contentValues.put(HEART_RATE, spo2HeartModelClass.getHeartBeat());
        String sqlIsDataExist = "Select * from " + TABLE_NAME_1 + " where " + ID + " = " + 1;
        Cursor cursor = sqLiteDatabaseRead.rawQuery(sqlIsDataExist, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            long insert = sqLiteDatabaseRead.insert(TABLE_NAME_1, null, contentValues);
            sqLiteDatabaseRead.close();
            if(insert == -1){
                return false;
            }else{
                return true;
            }
        }else{
            String sqlForUpdate = "UPDATE " + TABLE_NAME_1 + " SET " + SPO2 + " = " + spo2HeartModelClass.getSPO2()+ ", "+ HEART_RATE + " = " + spo2HeartModelClass.getHeartBeat() + " WHERE " + ID + " = " +1;
            sqLiteDatabaseWrite.execSQL(sqlForUpdate);
            Log.e("updating to data","problem in update operation for two data");
            sqLiteDatabaseWrite.close();
            return true;
        }
    }

    // read two value here.......
    public SPO2HeartModelClass readSPO2AndHeartRate(){
        SPO2HeartModelClass spo2HeartModelClass = null;
        SQLiteDatabase sqLiteDatabase;

        String sql ="SELECT * FROM " + TABLE_NAME_1;
        sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor;
        cursor = sqLiteDatabase.rawQuery(sql,null);

        if (cursor != null&&cursor.getCount()>0) {
            cursor.moveToFirst();
            int SPO2 = cursor.getInt(1);
            int HeartRate = cursor.getInt(2);
            spo2HeartModelClass = new SPO2HeartModelClass(SPO2, HeartRate);

        }
        else
            spo2HeartModelClass = new SPO2HeartModelClass(0, 0);


        return spo2HeartModelClass;

    }

//    phone number class here..........


    public boolean addUpdatePhoneNumber(PhoneNumberModelClass phoneNumberModelClass){
        SQLiteDatabase sqLiteDatabaseRead;
        SQLiteDatabase sqLiteDatabaseWrite;
        sqLiteDatabaseRead = this.getReadableDatabase();
        sqLiteDatabaseWrite = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, 1);
        contentValues.put(PHONE_NUMBER_1, phoneNumberModelClass.getPhoneNumber1());
        contentValues.put(PHONE_NUMBER_2, phoneNumberModelClass.getPhoneNumber2());


        String sqlIsDataExist = "Select * from " + TABLE_NAME_2 + " where " + ID + " = " + 1;
        Cursor cursor = sqLiteDatabaseRead.rawQuery(sqlIsDataExist, null);

        if(cursor.getCount() <= 0){
            cursor.close();
            long insert = sqLiteDatabaseRead.insert(TABLE_NAME_2, null, contentValues);

            sqLiteDatabaseRead.close();
            if(insert == -1){
                return false;
            }else{
                return true;
            }
        }else{
//            String sqlForUpdate = "UPDATE " + TABLE_NAME_2 + " SET " + PHONE_NUMBER_1 + " = " + phoneNumberModelClass.getPhoneNumber1() + ", "+ PHONE_NUMBER_2 + " = " + phoneNumberModelClass.getPhoneNumber2()+ " WHERE " + ID + " = " +1;
            String sqlForUpdate = "UPDATE " + TABLE_NAME_2 + " SET " + PHONE_NUMBER_1 + " = " + phoneNumberModelClass.getPhoneNumber1()+ ", "+ PHONE_NUMBER_2 + " = " + phoneNumberModelClass.getPhoneNumber2() + " WHERE " + ID + " = " +1;

            sqLiteDatabaseWrite.execSQL(sqlForUpdate);
            Log.e("updating phone number","problem in update operation for phone number");
            sqLiteDatabaseWrite.close();
            return true;
        }
    }


//    read phone number..........

    public PhoneNumberModelClass readPhoneNumberModelClass(){
        PhoneNumberModelClass phoneNumberModelClass = null;
        SQLiteDatabase sqLiteDatabase;

        String sql ="SELECT * FROM " + TABLE_NAME_2;
        sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor;
        if(sqLiteDatabase.rawQuery(sql,null)!=null)
        {
            cursor = sqLiteDatabase.rawQuery(sql,null);

        }
        else
            cursor=null;

        if(cursor!=null&&cursor.getCount()>0)
        {
            cursor.moveToFirst();
            String PhoneNumber1 = cursor.getString(1);
            String PhoneNumber2 = cursor.getString(2);
//          int HeartRate = cursor.getInt(2);
            phoneNumberModelClass = new PhoneNumberModelClass(PhoneNumber1, PhoneNumber2);

        }
       else
           phoneNumberModelClass = new PhoneNumberModelClass("", "");



        return phoneNumberModelClass;



    }
}