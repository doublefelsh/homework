package com.example.mysqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * 调用数据库助手类DbHelper(用于打开数据库)
 * CRUD操作针对数据库test.db的表friends
 */
public class MyDAO {
    private SQLiteDatabase myDb;
    private DbHelper dbHelper;

    /*构造函数*/
    public MyDAO(Context context) {
        dbHelper = new DbHelper(context, "test.db", null, 1);
    }

    /**
     * 查询所有记录
     * @return
     */
    public Cursor allQuery() {
        myDb = dbHelper.getReadableDatabase();
        return myDb.rawQuery("select * from friends", null);
    }

    /**
     * 返回数据表记录数
     * @return
     */
    public int getRecordsNumber() {
        myDb = dbHelper.getReadableDatabase();
        Cursor cursor = myDb.rawQuery("select * from friends", null);
        return cursor.getCount();
    }

    /**
     * 插入数据
     * @param name
     * @param age
     */
    public void insertInfo(String name, int age) {
        myDb = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("age", age);
        long rowid = myDb.insert(DbHelper.TB_NAME, null, values);
        if (rowid == -1)
            Log.i("myDbDemo", "数据插入失败！");
        else
            Log.i("myDbDemo", "数据插入成功！" + rowid);
    }

    /**
     * 删除数据
     * @param selId
     */
    public void deleteInfo(String selId) {
        String where = "_id=" + selId;
        int i = myDb.delete(DbHelper.TB_NAME, where, null);
        if (i > 0)
            Log.i("myDbDemo", "数据删除成功！");
        else
            Log.i("myDbDemo", "数据未删除！");
    }

    /**
     * 修改数据
     * @param name
     * @param age
     * @param selId
     */
    public void updateInfo(String name, int age, String selId) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("age", age);
        String where = "_id=" + selId;
        int i = myDb.update(DbHelper.TB_NAME, values, where, null);

        /**
         * 更新数据库的原生方法为：
         * myDb.execSQL("update friends set name=?, age=? where _id=?", new Object[]{name, age, selId});
         */

        if (i > 0)
            Log.i("myDbDemo", "数据更新成功！");
        else
            Log.i("myDbDemo", "数据更新失败！");

    }
}
