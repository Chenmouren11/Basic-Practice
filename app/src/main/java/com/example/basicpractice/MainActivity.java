package com.example.basicpractice;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.IBinder;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private bindServiceA service = null;
    private boolean isBind = false;
    private IntentFilter intentFilter;
    private MyBroadcastReceiver myReceiver;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            bindServiceA.MyBinder myBinder = (bindServiceA.MyBinder) binder;
            service = myBinder.getService();//通过ServiceConnection 中的IBinder获取 绑定的service对象
            Log.i("Finn", "ActivityA - onServiceConnected");
            int num = service.getRandomNumber();//通过service对象可对  bindServiceA中的函数进行操作
            Log.i("Finn", "ActivityA - getRandomNumber = " + num);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBind = false;
            Log.i("Finn", "ActivityA - onServiceDisconnected");
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("Finn", "ActivityA - onCreate - Thread = " + Thread.currentThread().getName());
        bindservice();
        Button button = findViewById(R.id.button_first);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unbindservice();
                Intent intent = new Intent("com.example.basicpractice.MY_BROADCAST");
                //指明要发送的广播值
                sendBroadcast(intent);
            }
        });
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.basicpractice.MY_BROADCAST");
        myReceiver = new MyBroadcastReceiver();
        registerReceiver(myReceiver, intentFilter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onClickAddName(View view) {
        // Add a new student record
        ContentValues values = new ContentValues();

        values.put(StudentsProvider.NAME,
                ((EditText)findViewById(R.id.editText2)).getText().toString());

        values.put(StudentsProvider.GRADE,
                ((EditText)findViewById(R.id.editText3)).getText().toString());

        Uri uri = getContentResolver().insert(
                StudentsProvider.CONTENT_URI, values);

        Toast.makeText(getBaseContext(),
                uri.toString(), Toast.LENGTH_LONG).show();
    }

    @SuppressLint("Range")
    public void onClickRetrieveStudents(View view) {

        // Retrieve student records
        String URL = "content://com.example.provider.College/students";

        Uri students = Uri.parse(URL);
        Cursor c = managedQuery(students, null, null, null, "name");

        if (c.moveToFirst()) {
            do{
                Toast.makeText(this,
                        ((Cursor) c).getString(c.getColumnIndex(StudentsProvider._ID)) +
                                ", " +  c.getString(c.getColumnIndex(StudentsProvider.NAME)) +
                                ", " + c.getString(c.getColumnIndex(StudentsProvider.GRADE)),
                        Toast.LENGTH_SHORT).show();
            } while (c.moveToNext());
        }
    }
    private void bindservice(){
        Intent intent = new Intent(this, bindServiceA.class);
        intent.putExtra("from", "ActivityA");
        Log.i("Finn", "ActivityA 执行 bindServiceA");
        bindService(intent, conn, BIND_AUTO_CREATE);//通过该方法绑定服务（周期：onCreate（）------》onbind（））

    }
    private void unbindservice(){
        //单击了“unbindService”按钮
        Log.i("Finn", "ActivityA 执行 unbindService");
        unbindService(conn);//通过该方法解除绑定服务  周期：----先执行  onunbind（）---》onDestroy（）
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
        Log.i("Finn", "ActivityA - onDestroy");
    }
}