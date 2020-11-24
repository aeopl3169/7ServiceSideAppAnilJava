package com.shiva.a7servicesideapp;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Random;

import static com.shiva.a7servicesideapp.MainActivity.SERVICE;

public class MyService extends Service {
    private int mRandomNumber;
    private boolean mIsRandomGeneratorOn;
    private final int MIN = 0;
    private final int MAX = 100;
    public static final int GET_COUNT = 0;

    private class RandomNumberRequestHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) { // "what" will be holding the flag sent from other app.
                case GET_COUNT:
                    Message messageSendRandomNumber = Message.obtain(null, GET_COUNT); // Holding the message
                    messageSendRandomNumber.arg1 = getRandomNumber(); // set the random number in Message
                    try {
                        msg.replyTo.send(messageSendRandomNumber); // replyTo will return Messenger and sending the Message back using send method.
                    } catch (RemoteException e) {
                        Log.i(SERVICE, "handleMessage: " + e.getMessage());
                        e.printStackTrace();
                    }
            }
            super.handleMessage(msg);
        }
    }

    // Here we are passing message through Messenger.
    // Messenger holds handler as argument
    // Handler will receive and process the message.
    private Messenger randomNumberMessenger = new Messenger(new RandomNumberRequestHandler());

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        mIsRandomGeneratorOn = true;
        Log.i(SERVICE, "onStartCommand: ");
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                startRandomNumberGenerator();
            }
        }).start();*/
//        new Thread(() -> startRandomNumberGenerator()).start();
        new Thread(this::startRandomNumberGenerator).start();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(SERVICE, "onBind: "+intent.getPackage());
        /*if (intent.getPackage().equals("com.shiva.serviceanil")){
            return randomNumberMessenger.getBinder(); // directly getting IBinder interface from Messenger.
        } else {
            Toast.makeText(getApplicationContext(), "Wrong Package",Toast.LENGTH_SHORT).show();
            return null;
        }*/
        return randomNumberMessenger.getBinder();
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.i(SERVICE, "onRebind: ");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(SERVICE, "onDestroy: ");
        stopRandomNumberGenerator();
    }

    private void startRandomNumberGenerator() {
        while (mIsRandomGeneratorOn) { // If true then generates random number
            try {
                Thread.sleep(1000);
                if (mIsRandomGeneratorOn) {
                    mRandomNumber = new Random().nextInt(MAX) + MIN;
                    Log.i(SERVICE, "stopRandomNumberGenerator: Thread id: " + Thread.currentThread().getId() + " Random number: " + mRandomNumber);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void stopRandomNumberGenerator() {
        mIsRandomGeneratorOn = false;
    }

    public int getRandomNumber() {
        return mRandomNumber;
    }
}