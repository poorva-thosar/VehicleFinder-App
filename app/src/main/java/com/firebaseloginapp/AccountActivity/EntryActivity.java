package com.firebaseloginapp.AccountActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.firebaseloginapp.R;

import java.util.Timer;
import java.util.TimerTask;


public class EntryActivity extends AppCompatActivity {
    Timer time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        time = new Timer();
        time.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent=new Intent(EntryActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },4000);
    }
}
