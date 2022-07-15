package com.example.basicpractice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

// 继承BroadcastReceivre基类
public class MyBroadcastReceiver extends BroadcastReceiver {

    // 复写onReceive()方法
    @Override
    public void onReceive(Context context, Intent intent) {
        //写入接收广播后的操作
        Toast.makeText(context.getApplicationContext(),"我接受的广播了",Toast.LENGTH_SHORT).show();
    }
}