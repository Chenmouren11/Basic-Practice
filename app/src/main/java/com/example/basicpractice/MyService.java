package com.example.basicpractice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.Nullable;

public class MyService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new AidlBinder();
    }
    class  AidlBinder extends IMyAidlInterface.Stub{
        @Override
        public int add(int a, int b) throws RemoteException {
            return a + b + 100;
        }
    }
}