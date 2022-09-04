package ir.mashhadict.taximeter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.os.HandlerCompat;
import android.support.v7.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);


        int SPLASH_TIME_OUT = 100;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreen.this, Authentication.class));
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}