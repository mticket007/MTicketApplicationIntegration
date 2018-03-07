package com.ticket.m.mticketapplicationintegration;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by visha on 14-02-2018.
 */

public class QrDBHelper extends SQLiteOpenHelper {
    public QrDBHelper(Context context) {
        super(context, "Qr.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table QrCode (Id INTEGER PRIMARY KEY,Qrcode blob not null)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("drop table if exists Qrcode");
        onCreate(sqLiteDatabase);
    }
}
