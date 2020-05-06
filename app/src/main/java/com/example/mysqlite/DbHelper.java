package com.example.mysqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    /*表名*/
    public static final String TB_NAME = "friends";

    public DbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*当表不存在时，创建表*/
        db.execSQL("CREATE TABLE IF NOT EXISTS "
                + TB_NAME
                + " ( _id integer primary key autoincrement,"
                + " name varchar,"
                + " age integer" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*执行SQL命令*/
        db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
        onCreate(db);
    }
}
