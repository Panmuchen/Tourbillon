package com.example.Tourbillon;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
    ImageButton button_setting;
    public static Date curDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        add = (ImageButton) findViewById(R.id.ImageButton_Add);
        LinearLayout linearLayout_Time = findViewById(R.id.LinearLayout_Time);

        // 查询空闲教室
        button_setting = findViewById(R.id.ImageButton_Settings);
        button_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 显示setting的菜单栏
                showSettingsMenu(view);
            }
        });

        // 导入课程按钮
        ImageButton button_import = findViewById(R.id.ImageButton_Import);
        button_import.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        //填写日期
        TextView textView_date = findViewById(R.id.TextView_date);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        curDate = new Date(System.currentTimeMillis());
        textView_date.setText(formatter.format(curDate));

        //测试：加入测试数据
        //addTestData();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });

        linearLayout_Time.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: layout_time");
                final EditText et = new EditText(MainActivity.this);
                new AlertDialog.Builder(MainActivity.this).setTitle("输入当前周")
                        .setIcon(android.R.drawable.sym_def_app_icon)
                        .setView(et)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //按下确定键后的事件
                                Toast.makeText(getApplicationContext(), et.getText().toString(),Toast.LENGTH_LONG).show();
                            }
                        }).setNegativeButton("取消",null).show();
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        //可以获得窗口大小，防止需要计算大小的函数
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
                    Intent intent = new Intent(MainActivity.this, ClassActivity.class);
                    intent.putExtra("action", ClassActivity.ACTION_INSERT);
                    startActivity(intent);
                }
                else if(id==R.id.action_addschedule) {
                    Intent intent = new Intent(MainActivity.this, ScheduleActivity.class);
                    intent.putExtra("action", ScheduleActivity.ACTION_INSERT);
                    startActivity(intent);
                }
                return false;
            }
        });
    }

    @SuppressLint("RestrictedApi")
    private void showSettingsMenu(View view){
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
        popupMenu.getMenuInflater().inflate(R.menu.settings_menu,popupMenu.getMenu());
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id==R.id.settings_queryClassroom){
                    Intent intent = new Intent(MainActivity.this, ClassroomQueryActivity.class);
                    startActivity(intent);
                }
                else if(id==R.id.settings_queryLibrary) {
                    Intent intent = new Intent(MainActivity.this, LibraryQueryActivity.class);
                    startActivity(intent);

                }
                else if(id == R.id.settings_others) {
                    // to do
                }
                return false;
            }
        });
    }
}