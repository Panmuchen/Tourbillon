package com.example.Tourbillon;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {
    final ClassManager classOp = new ClassManager(MainActivity.this);
    ImageButton add;
    Date curDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        add = (ImageButton) findViewById(R.id.ImageButton_Add);

        //填写日期
        TextView textView_date = findViewById(R.id.TextView_date);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        curDate = new Date(System.currentTimeMillis());
        textView_date.setText(formatter.format(curDate));

        //测试：加入测试数据
        addTestData();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.d(TAG, "onWindowFocusChanged: ");
        drawClassBoxes();
        TextView view_weekHeader = findViewById(R.id.textView_WeekHeader);
        ScheduleView scheduleView = findViewById(R.id.ScheduleView);
        view_weekHeader.setWidth(scheduleView.getWidth());
        Log.d(TAG, "onWindowFocusChanged: header width:"+view_weekHeader.getWidth());
        //设置初始滚动位置
        ScrollView scrollView = findViewById(R.id.scrollView_main);
        scrollView.scrollTo(0, 1440);
    }


    private void insertData(String id, String name, Integer time, Integer day) {
        classOp.insertData(id, name, time, 2, 1, 16, day, " ", " ");
    }

    private void addTestData() {
        // 加入测试课程
        insertData("Gaoshu", "高数", 3, 1);
        insertData("RuanGong", "软件工程", 1, 2);
        insertData("RuanGong2", "软件工程", 3, 2);
        classOp.insertData("ShuJuKu", "数据库", 5, 3, 1, 16, 3, "东上院101", "x老师");
        classOp.insertData("KC4", "课程4", 3, 2, 1, 8, 4, "东上院101", "x老师");
        classOp.insertData("KC5", "课程5", 10, 3, 1, 16, 5, "东上院101", "x老师");
        classOp.insertData("KC6", "课程6", 8, 2, 1, 16, 6, "东上院101", "x老师");
        //insertData(6, "课程7", 1, 2, 1, 16, 7, "东上院102", "x老师");
        printClassDatabase();
    }

    public void drawClassBoxes() {

        //从数据库拿到课程数据保存在链表
        List<Class_t> classes = classOp.query();
        ScheduleView scheduleView = findViewById(R.id.ScheduleView);
        scheduleView.setEvents(classes);
    }

    public void printClassDatabase() {
        List<Class_t> classes = classOp.query();
        for (Class_t c : classes) {
            Log.i(TAG, "printClassDatabase: " + c.toString());
        }
    }

    @SuppressLint("RestrictedApi")
    private void showPopupMenu(View view){
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id==R.id.action_addcourse){

                }
                else if(id==R.id.action_addschedule) {

                }
                return false;
            }
        });
    }
}