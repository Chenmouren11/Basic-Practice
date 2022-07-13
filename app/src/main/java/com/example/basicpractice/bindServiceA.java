package com.example.basicpractice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Random;

public class bindServiceA extends Service {
    //client 可以通过Binder获取Service实例
    public class MyBinder extends Binder {
        public bindServiceA getService() {
            return bindServiceA.this;
        }
    }

    //通过binder实现调用者client与Service之间的通信 在onBind中返回该对象
    private MyBinder binder = new MyBinder();

    private final Random generator = new Random();

    @Override
    public void onCreate() {
        Log.i("Finn", "bindServiceA - onCreate - Thread = " + Thread.currentThread().getName());
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Finn", "bindServiceA - onStartCommand - startId = " + startId + ", Thread = " + Thread.currentThread().getName());
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("Finn", "bindServiceA - onBind - Thread = " + Thread.currentThread().getName());
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i("Finn", "bindServiceA - onUnbind - from = " + intent.getStringExtra("from"));
        return false;
    }

    @Override
    public void onDestroy() {
        Log.i("Finn", "bindServiceA - onDestroy - Thread = " + Thread.currentThread().getName());
        super.onDestroy();
    }

    //getRandomNumber是Service暴露出去供client调用的公共方法
    public int getRandomNumber() {
        return generator.nextInt();
    }
}