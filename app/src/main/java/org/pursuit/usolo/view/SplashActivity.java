package org.pursuit.usolo.view;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.pursuit.usolo.R;

public final class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            Intent hostIntent = new Intent(SplashActivity.this, HostActivity.class);
            startActivity(hostIntent);
            finish();
        }, SPLASH_TIME_OUT);
    }
}
