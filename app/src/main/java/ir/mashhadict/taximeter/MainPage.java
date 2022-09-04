package ir.mashhadict.taximeter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainPage extends AppCompatActivity {

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


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Button singleTaximeter = findViewById(R.id.singleTaximeter);
        Button MultiTaximeter = findViewById(R.id.multiTaximeter);
        Button registerButton = findViewById(R.id.registerButton);

        TextView IdCodeMainText = findViewById(R.id.idCodeMainText);
        TextView FirstnameMainText = findViewById(R.id.firstnameMainText);
        TextView LastnameMainText = findViewById(R.id.lastnameMainText);
        TextView CarModelMainText = findViewById(R.id.carModelMainText);
        TextView ZoneMainText = findViewById(R.id.zoneMainText);
        TextView TypeMainText = findViewById(R.id.typeMainText);
        TextView CarColorMainText = findViewById(R.id.carColorMainText);
        TextView CarPelakMainText = findViewById(R.id.carPelakMainText);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            identityCode = extras.getString("identityCode");
            firstName = extras.getString("firstName");
            lastName = extras.getString("lastName");
            zone = extras.getString("zone");
            type = extras.getString("type");
            car = extras.getString("car");

            String zoneString = "گردشی و خطوط شهری";
            if(zone.equals("1")){
                zoneString = "گردشی و خطوط شهری";
            }else if (zone.equals("2")){
                zoneString = "راه آهن";
            }else if (zone.equals("3")){
                zoneString = "پایانه مسافربری";
            }else if (zone.equals("4")){
                zoneString = "بی سیم و آژانس";
            }else if (zone.equals("5")){
                zoneString = "فرودگاه";
            }else if (zone.equals("6")){
                zoneString = "قراردادی";
            }

            String typeString = "گردشی و خطوط شهری";
            if(type.equals("1")){
                typeString = "خوردوهای سواری سبک";
            }else if (type.equals("2")){
                typeString = "ون";
            }

            carModel = extras.getString("carModel");
            pelak = extras.getString("pelak");
            color = extras.getString("color");

            IdCodeMainText.setText(identityCode);
            FirstnameMainText.setText(firstName);
            LastnameMainText.setText(lastName);
            ZoneMainText.setText(zoneString);
            TypeMainText.setText(typeString);
            CarModelMainText.setText(carModel);
            CarColorMainText.setText(pelak);
            CarPelakMainText.setText(color);
        }


        final Handler handler = new Handler();


        Runnable runnable = new Runnable() {
            public void run() {

                if (isNetworkConnected()) {
                    ImageView image2 = (ImageView) findViewById(R.id.checkInternetConncetionMainPage);
                    Resources res = getResources();
                    image2.setImageDrawable(res.getDrawable(R.drawable.netconnected));

                } else {
                    ImageView image2 = (ImageView) findViewById(R.id.checkInternetConncetionMainPage);
                    Resources res = getResources();
                    image2.setImageDrawable(res.getDrawable(R.drawable.netnotconnected));
                }

                final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    ImageView image3 = (ImageView) findViewById(R.id.checkGpsConnectionMainPage);
                    Resources res = getResources();
                    image3.setImageDrawable(res.getDrawable(R.drawable.gpsnotconnected));
                } else {
                    ImageView image3 = (ImageView) findViewById(R.id.checkGpsConnectionMainPage);
                    Resources res = getResources();
                    image3.setImageDrawable(res.getDrawable(R.drawable.gpsconnected));
                }
                handler.postDelayed(this, 3000);
            }

        };
        runnable.run();
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


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void OpenPageSingleTaximeter(View view) {
        Intent i9 = new Intent(MainPage.this, Single_Taximeter.class);
        i9.putExtra(
                "identityCode", String.valueOf(identityCode));
        i9.putExtra("firstName", firstName);
        i9.putExtra("lastName", lastName);
        i9.putExtra("carModel", carModel);
        i9.putExtra("pelak", pelak);
        i9.putExtra("color", color);
        startActivityForResult(i9, ACTIVITY_CREATE);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void OpenPageMultiTaximeter(View view) {
        Intent i10 = new Intent(MainPage.this, Multi_Taximeter.class);
        i10.putExtra(
                "identityCode", String.valueOf(identityCode));
        i10.putExtra("firstName", firstName);
        i10.putExtra("lastName", lastName);
        i10.putExtra("carModel", carModel);
        i10.putExtra("pelak", pelak);
        i10.putExtra("color", color);
        startActivityForResult(i10, ACTIVITY_CREATE);
    }
}