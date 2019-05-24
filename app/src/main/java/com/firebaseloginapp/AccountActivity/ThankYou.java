package com.firebaseloginapp.AccountActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


import com.firebaseloginapp.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import java.util.Timer;
import java.util.TimerTask;

public class ThankYou extends Activity {
    Timer time;

    private AdView mAdView;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.you_thank);

        MobileAds.initialize(this, "ca-app-pub-6499962255375293~2118262346");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        time = new Timer();
        time.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent=new Intent(ThankYou.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },4000);
    }
}
