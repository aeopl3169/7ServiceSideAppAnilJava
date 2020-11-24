package com.shiva.a7servicesideapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    static final String SERVICE = "Service";
    private boolean mStopLoop;
    private Intent serviceIntent;
    private Button buttonStart, buttonStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serviceIntent = new Intent(getApplicationContext(), MyService.class);
        buttonStart = (Button)findViewById(R.id.buttonStart);
        buttonStop = (Button)findViewById(R.id.buttonStop);
        buttonStart.setOnClickListener(this);
        buttonStop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonStart:
                mStopLoop = true;
                startService(serviceIntent); // start service.
                break;
            case R.id.buttonStop:
//                mStopLoop = false;
                stopService(serviceIntent); // We can stop service explicitly with stopService.
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }
}