package com.serosoft.academiassu.SqliteDB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.os.Environment;

import com.serosoft.academiassu.Helpers.Consts;
import com.serosoft.academiassu.Modules.Dashboard.Models.CurrencyDto;

import java.io.File;
import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "academia.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_CURRENCY = "currency";

    public static final String CURRENCY_ID = "currencyId";
    public static final String CURRENCY_NAME = "currencyName";
    public static final String CURRENCY_CODE = "currencyCode";

    public static String rootDirectory = Consts.ACADEMIA;
    public static String subRootDBDirectory = "Databases";

    public DbHelper(Context context)
    {
        super(context, /*makeDBFilePath()*/  DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static String makeDBFilePath()
    {
        File file = new File(Environment.getExternalStorageDirectory()
                .getPath(), rootDirectory + File.separator + subRootDBDirectory);
        if (!file.exists())
        {
            file.mkdirs();
        }

        String dbPath = (file.getPath() + File.separator + DATABASE_NAME);

        return dbPath;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("create table " + TABLE_CURRENCY + " ("
                + CURRENCY_ID + " TEXT,"
                + CURRENCY_NAME + " TEXT,"
                + CURRENCY_CODE + " TEXT"
                + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        onCreate(db);
    }

    //here is add customer and updated his details
    public void insertAllCurrency(ArrayList<CurrencyDto> currencyList)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        try {
            for (CurrencyDto currencyDto : currencyList)
            {
                if (!CheckIsDataAlreadyInDBorNot(TABLE_CURRENCY,CURRENCY_ID, String.valueOf(currencyDto.getId())))
                {
                    SQLiteStatement insert = db.compileStatement("INSERT OR REPLACE INTO " + TABLE_CURRENCY + "("
                            + CURRENCY_ID + "," + CURRENCY_NAME + "," + CURRENCY_CODE +")"
                            + "VALUES " + "(?,?,?)");


                    insert.bindString(1, String.valueOf(currencyDto.getId()));
                    insert.bindString(2, String.valueOf(currencyDto.getName()));
                    insert.bindString(3, String.valueOf(currencyDto.getCurrencyCode()));
                    insert.execute();
                }else
                {
                    //Update inserted user details
                    String query = "UPDATE " + TABLE_CURRENCY + " SET " +
                            CURRENCY_ID + " = "+"'"+currencyDto.getId()+"'" +" , " +
                            CURRENCY_NAME + " = "+"'"+currencyDto.getName()+"'"+" , " +
                            CURRENCY_CODE + " = "+"'"+currencyDto.getCurrencyCode()+"'" +
                            " WHERE " + CURRENCY_ID + " = '" + currencyDto.getId() + "'";

                    SQLiteStatement insert = db.compileStatement(query);
                    insert.execute();
                }
            }
            db.setTransactionSuccessful();
        }
        finally
        {
            db.endTransaction();
        }
    }

    // Getting all saved data
    public ArrayList<CurrencyDto> getAllData() {

        ArrayList<CurrencyDto> data = new ArrayList<CurrencyDto>();
        SQLiteDatabase db = this.getReadableDatabase();
        String select_query = "Select * from " + TABLE_CURRENCY;

        Cursor cursor = db.rawQuery(select_query, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    CurrencyDto currencyDto = new CurrencyDto(cursor.getString(0),
                            cursor.getString(1), cursor.getString(2));

                    data.add(currencyDto);

                } while (cursor.moveToNext());

            }
        } finally {

            cursor.close();
        }

        db.close();

        return data;
    }

    public boolean CheckIsDataAlreadyInDBorNot(String TableName, String dbfield, String fieldValue)
    {
        SQLiteDatabase sqldb = this.getReadableDatabase();
        String Query = "SELECT * FROM " + TableName + " WHERE " + dbfield + " = '" + fieldValue + "'";
        Cursor cursor = sqldb.rawQuery(Query, null);
        if (cursor.getCount() <= 0)
        {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    //db currency table
    public void deleteCurrencyDB()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {

            String Query = "DELETE  FROM " + TABLE_CURRENCY;
            SQLiteStatement insert = db.compileStatement(Query);
            insert.execute();
            db.setTransactionSuccessful();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

}
