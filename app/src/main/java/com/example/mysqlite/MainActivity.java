package com.example.mysqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MyDAO myDAO;    // 数据库访问对象
    private ListView listView;
    private List<Map<String, Object>> listData;
    private Map<String, Object> listItem;
    private SimpleAdapter listAdapter;

    // id(自增长字段), name, age
    private EditText et_name;
    private EditText et_age;

    private String selId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button bt_add= findViewById(R.id.bt_add);
        bt_add.setOnClickListener(this);
        Button bt_modify= findViewById(R.id.bt_modify);
        bt_modify.setOnClickListener(this);
        Button bt_del= findViewById(R.id.bt_del);
        bt_del.setOnClickListener(this);

        et_name = findViewById(R.id.et_name);
        et_age = findViewById(R.id.et_age);

        myDAO = new MyDAO(this);
        /*初始化数据*/
        if (myDAO.getRecordsNumber() == 0) {
            myDAO.insertInfo("lxt", 20);
            myDAO.insertInfo("xngz", 29);
        }
        displayRecords();

    }

    /**
     * 显示数据
     */
    public void displayRecords() {
        listView = findViewById(R.id.listView);
        listData = new ArrayList<Map<String, Object>>();
        Cursor cursor = myDAO.allQuery();
        /*循环遍历所有数据*/
        while (cursor.moveToNext()) {
            /**
             * 获取数据的两种方式：
             * 1.cursor.getInt(columnIndex) 通过列号获取数据
             * 2.cursor.getInt(columnName)  通过列名称获取数据
             */
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            int age = cursor.getInt(cursor.getColumnIndex("age"));
            listItem = new HashMap<>(); // 必须在循环内部创建
            listItem.put("_id", id);
            listItem.put("name", name);
            listItem.put("age", age);
            listData.add(listItem);
        }
        listAdapter = new SimpleAdapter(this,
                listData,
                R.layout.list_item,
                new String[]{"_id", "name", "age"},
                new int[]{R.id.tv_id, R.id.tvname, R.id.tvage});
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> rec = (Map<String, Object>) listAdapter.getItem(position);
                et_name.setText(rec.get("name").toString());
                et_age.setText(rec.get("age").toString());
                Log.i("ly", rec.get("_id").toString());
                selId = rec.get("_id").toString();
            }
        });
    }

    @Override
    public void onClick(View v) {

        if (selId != null) {
            String p1 = et_name.getText().toString().trim();
            int p2 = Integer.parseInt(et_age.getText().toString());
            switch (v.getId()) {
                case R.id.bt_add:
                    myDAO.insertInfo(p1, p2);
                    break;
                case R.id.bt_modify:
                    myDAO.updateInfo(p1, p2, selId);
                    break;
                case R.id.bt_del:
                    myDAO.deleteInfo(selId);
                    Toast.makeText(getApplicationContext(), "删除成功~", Toast.LENGTH_SHORT).show();
                    et_name.setText(null);
                    et_age.setText(null);
                    selId = null;
            }
        } else {// 未选择列表项
            // 点击添加按钮
            if (v.getId() == R.id.bt_add) {
                String p1 = et_name.getText().toString();
                String p2 = et_age.getText().toString();
                if (p1.equals("") || p2.equals("")) {
                    Toast.makeText(getApplicationContext(), "姓名和年龄不能为空！", Toast.LENGTH_SHORT).show();
                } else {
                    myDAO.insertInfo(p1, Integer.parseInt(p2));
                }
            } else {
                // 点击修改或删除按钮
                Toast.makeText(getApplicationContext(), "请选择记录！", Toast.LENGTH_SHORT).show();
            }
        }
        displayRecords();

    }
}
