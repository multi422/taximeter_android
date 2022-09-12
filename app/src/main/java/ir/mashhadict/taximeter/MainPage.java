package ir.mashhadict.taximeter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
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

import de.hdodenhof.circleimageview.CircleImageView;


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
        Button LogJourney = findViewById(R.id.logJourney);

        TextView IdCodeMainText = findViewById(R.id.idCodeMainText);
        TextView NameMainText = findViewById(R.id.nameMainText);
        TextView CarModelMainText = findViewById(R.id.carModelMainText);
        TextView ZoneMainText = findViewById(R.id.zoneMainText);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            identityCode = extras.getString("identityCode");
            firstName = extras.getString("firstName");
            lastName = extras.getString("lastName");
            zone = extras.getString("zone");
            type = extras.getString("type");
            car = extras.getString("car");
            carModel = extras.getString("carModel");
            pelak = extras.getString("pelak");
            color = extras.getString("color");

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

            if(color.equals("زرد")){
                singleTaximeter.setBackgroundColor(Color.rgb(255,152,0));
                MultiTaximeter.setBackgroundColor(Color.rgb(255,152,0));
                LogJourney.setBackgroundColor(Color.rgb(255,152,0));
                ImageView CarImage = (ImageView) findViewById(R.id.carImage);
                CircleImageView PersonImage = (CircleImageView) findViewById(R.id.personImage);
                Resources res = getResources();
                CarImage.setImageDrawable(res.getDrawable(R.mipmap.pegeoutyellow));
                PersonImage.setImageDrawable(res.getDrawable(R.mipmap.mohammadbagherhaddad));
            }else if(color.equals("سبز")){
                singleTaximeter.setBackgroundColor(Color.rgb(51,105,30));
                MultiTaximeter.setBackgroundColor(Color.rgb(51,105,30));
                LogJourney.setBackgroundColor(Color.rgb(51,105,30));
                ImageView CarImage = (ImageView) findViewById(R.id.carImage);
                ImageView PersonImage = (ImageView) findViewById(R.id.personImage);
                Resources res = getResources();
                CarImage.setImageDrawable(res.getDrawable(R.mipmap.samadgreen));
                PersonImage.setImageDrawable(res.getDrawable(R.mipmap.mostafafazli));
            }

            IdCodeMainText.setText(identityCode);
            NameMainText.setText(firstName + " " + lastName);
            ZoneMainText.setText(zoneString);
            CarModelMainText.setText(carModel);
        }


//        identityCode = "0925485640";
//        firstName = "مصطفی";
//        lastName = "فضلی";
//        zone = "2";
//        type = "1";
//        car = "2";
//        carModel = "سمند EF7";
//        pelak = "57 د 856 12";
//        color = "زرد";

        String zoneString = "گردشی و خطوط شهری";
        if (zone.equals("1")) {
            zoneString = "گردشی و خطوط شهری";
        } else if (zone.equals("2")) {
            zoneString = "راه آهن";
        } else if (zone.equals("3")) {
            zoneString = "پایانه مسافربری";
        } else if (zone.equals("4")) {
            zoneString = "بی سیم و آژانس";
        } else if (zone.equals("5")) {
            zoneString = "فرودگاه";
        } else if (zone.equals("6")) {
            zoneString = "قراردادی";
        }

        String typeString = "گردشی و خطوط شهری";
        if (type.equals("1")) {
            typeString = "خوردوهای سواری سبک";
        } else if (type.equals("2")) {
            typeString = "ون";
        }

        IdCodeMainText.setText(identityCode);
        NameMainText.setText(firstName + " " + lastName);
        ZoneMainText.setText(zoneString);
        CarModelMainText.setText(carModel);


        final Handler handler = new Handler();


        Runnable runnable = new Runnable() {
            public void run() {

                if (isNetworkConnected()) {
                    ImageView image2 = (ImageView) findViewById(R.id.checkInternetConncetionMainPage);
                    Resources res = getResources();
                    image2.setImageDrawable(res.getDrawable(R.drawable.netconnectedicon));

                } else {
                    ImageView image2 = (ImageView) findViewById(R.id.checkInternetConncetionMainPage);
                    Resources res = getResources();
                    image2.setImageDrawable(res.getDrawable(R.drawable.netnotconnectedicon));
                }

                final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    ImageView image3 = (ImageView) findViewById(R.id.checkGpsConnectionMainPage);
                    Resources res = getResources();
                    image3.setImageDrawable(res.getDrawable(R.drawable.gpsnotconnectedicon));
                } else {
                    ImageView image3 = (ImageView) findViewById(R.id.checkGpsConnectionMainPage);
                    Resources res = getResources();
                    image3.setImageDrawable(res.getDrawable(R.drawable.gpsconnectedicon));
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
        i9.putExtra("zone", zone);
        i9.putExtra("type", type);
        i9.putExtra("car", car);
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
        i10.putExtra("zone", zone);
        i10.putExtra("type", type);
        i10.putExtra("car", car);
        i10.putExtra("carModel", carModel);
        i10.putExtra("pelak", pelak);
        i10.putExtra("color", color);
        startActivityForResult(i10, ACTIVITY_CREATE);

    }

    public void openPageContractualTaximeter(View view) {
        Intent i11 = new Intent(MainPage.this, Contractual_Taximeter.class);
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
    }
}