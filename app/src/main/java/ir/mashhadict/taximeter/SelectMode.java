package ir.mashhadict.taximeter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class SelectMode extends AppCompatActivity {

    private static final int ACTIVITY_CREATE = 0;
    boolean doubleBackToExitPressedOnce = false;
    String identityCode;
    String firstName;
    String lastName;
    String zone;
    String type;
    String car;
    String carModel;
    String pelak;
    String color;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_mode_screen);

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView BetaModeWarning = findViewById(R.id.betaModeWarning);

        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1500); //You can manage the blinking time with this parameter
        anim.setStartOffset(40);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        BetaModeWarning.startAnimation(anim);


    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "برای خروج از برنامه دوباره دکمه بازگشت را بزنید", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 1000);
    }


    public void lineTaxiClicked(View view) {

        identityCode = "1004";
        firstName = "مصطفی";
        lastName = "فضلی";
        zone = "7";
        type = "1";
        car = "2";
        carModel = "سمند سری LX";
        pelak = "57 د 856 12";
        color = "سبز";



        Intent i10 = new Intent(SelectMode.this, MainPage.class);
        i10.putExtra(
                "identityCode", String.valueOf(identityCode));
        i10.putExtra("firstName", firstName);
        i10.putExtra("lastName", lastName);
        i10.putExtra("zone", zone);
        i10.putExtra("type", type);
        i10.putExtra("car", car);
        i10.putExtra("carModel", carModel);
        i10.putExtra("pelak", pelak);
        i10.putExtra("color", color);
        startActivityForResult(i10, ACTIVITY_CREATE);
        finish();
    }

    public void sightseeingTaxiClicked(View view) {

        identityCode = "1523";
        firstName = "محمدباقر";
        lastName = "حداد";
        zone = "5";
        type = "1";
        car = "2";
        carModel = "پژو 405";
        pelak = "23 د 632 12";
        color = "زرد";



        Intent i11 = new Intent(SelectMode.this, MainPage.class);
        i11.putExtra(
                "identityCode", String.valueOf(identityCode));
        i11.putExtra("firstName", firstName);
        i11.putExtra("lastName", lastName);
        i11.putExtra("zone", zone);
        i11.putExtra("type", type);
        i11.putExtra("car", car);
        i11.putExtra("carModel", carModel);
        i11.putExtra("pelak", pelak);
        i11.putExtra("color", color);
        startActivityForResult(i11, ACTIVITY_CREATE);
        finish();
    }


}