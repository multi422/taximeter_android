package ir.mashhadict.taximeter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


@RequiresApi(api = Build.VERSION_CODES.O)
public class Single_Taximeter extends Activity {

    MediaPlayer netsound, gpssound, startSound, pauseSound, stopSound, finishSound;
    Button StartTimerButton;
    LinearLayout ManPlace;
    LinearLayout WomanPlace;
    private TextView tvLatitude = null, tvLongitude = null;
    TableLayout singleTaximeterScreen = null;
    boolean flagStart = false;
    long startTime = 0;
    long nowTime = System.currentTimeMillis();
    long Timer = 0;
    long elapsed = 0;
    double latitude1 = 0;
    double longitude1 = 0;
    double latitude2;
    double longitude2;
    final double[] distanceTraveled = {0};
    final double[] distanceTiming = {0};
    int timeWaiting = 0;
    final boolean[] pointFlag = {false};
    int hours = 0;
    int minutes = 0;
    int seconds = 0;
    double duration = 0;
    boolean weather = false;
    boolean shift = false;
    double rate = 1;
    double cost = 0;
    String ZoneStringText;
    String typeStringText;
    int initionalRate;
    int distanceRate;
    int timeRate;
    JsonPlaceHolderApi jsonPlaceHolderApi;
    boolean isMale = true;
    boolean canStart = false;

    int identityCode;
    String firstName;
    String lastName;
    String zoneString;
    String typeString;
    String carString;
    int zoneInt = 1;
    int typeInt = 1;
    int carInt = 1;
    String carModel;
    String pelak;
    String color;


    public static double distance(double lat1,
                                  double lat2, double lon1,
                                  double lon2) {

        // The math module contains a function
        // named toRadians which converts from
        // degrees to radians.
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2), 2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers. Use 3956
        // for miles
        double r = 6371;

        // calculate the result
        return (c * r);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "برای خروج از صفحه تاکسی متر دربستی دوباره دکمه بازگشت را بزنید", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 1000);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_taximeter);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        TextView gpsalarmSingleTaximeter = findViewById(R.id.gpsalarmSingleTaximeterText);
        TextView netAlarmSingleTaximeter = findViewById(R.id.netAlarmSingleTaximeterText);
        singleTaximeterScreen = findViewById(R.id.single_taximeter_screen);

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            identityCode = Integer.parseInt(extras.getString("identityCode"));
            firstName = extras.getString("firstName");
            lastName = extras.getString("lastName");
            carModel = extras.getString("carModel");
            zoneString = extras.getString("zone");
            typeString = extras.getString("type");
            carString = extras.getString("car");
            pelak = extras.getString("pelak");
            color = extras.getString("color");

        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://taximeterict.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        TextView secondTextView = findViewById(R.id.secondCountdown);
        TextView minuteTextView = findViewById(R.id.minuteCountdown);
        TextView hourTextView = findViewById(R.id.hourCountdown);

        TextView distanceTextView = findViewById(R.id.distanceTextView);

        CheckBox WeatherCheckBox = findViewById(R.id.weatherCheck);
        CheckBox ShiftCheckBox = findViewById(R.id.shiftCheck);


        ManPlace = (LinearLayout) findViewById(R.id.manPlace);
        WomanPlace = (LinearLayout) findViewById(R.id.womanPlace);
        StartTimerButton = (Button) findViewById(R.id.startTimerButton);

        TextView costTextView = findViewById(R.id.costText);

        ImageView image = (ImageView) findViewById(R.id.countdownIcon);
        Resources res = getResources();
        image.setImageDrawable(res.getDrawable(R.drawable.stopicon));
        tvLatitude = (TextView) findViewById(R.id.latitude);
        tvLongitude = (TextView) findViewById(R.id.longitude);
        final Handler handler = new Handler();
        final int[] coefficient = {1};
        final boolean[] flagInitialRate = {false};


        Runnable runnable = new Runnable() {
            public void run() {

                ImageView checkNetSingleTaximeter = (ImageView) findViewById(R.id.checkInternetConnectionSingleMultimeter);


                if (isNetworkConnected()) {
                    Resources res = getResources();
                    checkNetSingleTaximeter.setImageDrawable(res.getDrawable(R.drawable.netconnectedicon));
                    netAlarmSingleTaximeter.setText("");
                    getSRate();
                    getVRate();

                    if (initionalRate != 0 && flagInitialRate[0] == false) {
                        if(zoneString.equals("1")) {
                            cost = (initionalRate / 10) * 4;
                        }else{
                            cost = (initionalRate / 10) ;
                        }
                        flagInitialRate[0] = true;
                    }

                } else {
                    Resources res = getResources();
                    checkNetSingleTaximeter.setImageDrawable(res.getDrawable(R.drawable.netnotconnectedicon));
                    netAlarmSingleTaximeter.setText("اینترنت تلفن همراه خود را روشن کنید !");
                    Animation anim = new AlphaAnimation(0.0f, 1.0f);
                    anim.setDuration(1000); //You can manage the blinking time with this parameter
                    anim.setStartOffset(40);
                    anim.setRepeatMode(Animation.REVERSE);
                    anim.setRepeatCount(Animation.INFINITE);
                    netAlarmSingleTaximeter.startAnimation(anim);
                    netsound = MediaPlayer.create(Single_Taximeter.this, R.raw.net_alert);
                    netsound.start();
                }

                GpsTracker gpsTracker = new GpsTracker(Single_Taximeter.this);

                if (gpsTracker.canGetLocation()) {

                    createPost(identityCode, latitude2, longitude2);
                    // createPost(925485640, latitude2, longitude2, "2022-08-24T15:27:47Z");
                } else {
                    gpsTracker.showSettingsAlert();
                }

                if (weather == true) {
                    WeatherCheckBox.setChecked(true);
                } else {
                    WeatherCheckBox.setChecked(false);
                }
                if (shift == true) {
                    ShiftCheckBox.setChecked(true);
                } else {
                    ShiftCheckBox.setChecked(false);
                }

                handler.postDelayed(this, 7000);
            }

        };
        runnable.run();

        Runnable runnable2 = new Runnable() {
            public void run() {

                duration = printCountDown() / 1000;

                if (flagStart == true) {
                    hours = (int) duration / 3600;
                    minutes = (int) (duration % 3600) / 60;
                    seconds = (int) (duration % 60);
                }

                handler.postDelayed(this, 1000);
            }

        };
        runnable2.run();

        Runnable runnable4 = new Runnable() {
            public void run() {
                GpsTracker gpsTracker = new GpsTracker(Single_Taximeter.this);
                ImageView checkGPSSingleTaximeter = (ImageView) findViewById(R.id.GpsConnectionSingleMultimeter);


                final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Resources res = getResources();
                    checkGPSSingleTaximeter.setImageDrawable(res.getDrawable(R.drawable.gpsnotconnectedicon));
                    gpsalarmSingleTaximeter.setText("موقعیت مکانی خود را روشن کنید !");
                    Animation anim2 = new AlphaAnimation(0.0f, 1.0f);
                    anim2.setDuration(50); //You can manage the blinking time with this parameter
                    anim2.setStartOffset(20);
                    anim2.setRepeatMode(Animation.REVERSE);
                    anim2.setRepeatCount(Animation.INFINITE);
                    gpsalarmSingleTaximeter.startAnimation(anim2);
                    gpssound = MediaPlayer.create(Single_Taximeter.this, R.raw.gps_alert);
                    gpssound.start();
                } else {
                    Resources res = getResources();
                    checkGPSSingleTaximeter.setImageDrawable(res.getDrawable(R.drawable.gpsconnectedicon));
                    gpsalarmSingleTaximeter.setText("");

                }

                if (gpsTracker.canGetLocation()) {
                    if (pointFlag[0] == false) {
                        latitude2 = gpsTracker.getLatitude();
                        longitude2 = gpsTracker.getLongitude();
                        String formattedLatitude2 = String.format("%.5f", latitude2);
                        String formattedLongitude2 = String.format("%.5f", longitude2);
                        tvLatitude.setText("Lat: " + formattedLatitude2);
                        tvLongitude.setText("Lng: " + formattedLongitude2);
                    } else {
                        latitude1 = latitude2;
                        longitude1 = longitude2;
                        String formattedDistanceTraveled = String.format("%.3f", distanceTraveled[0]);
                        distanceTextView.setText(formattedDistanceTraveled);
                        latitude2 = gpsTracker.getLatitude();
                        longitude2 = gpsTracker.getLongitude();
                        double distanceNow = distance(latitude2, latitude1, longitude2, longitude1);
                        timeWaiting += 1;

                        // *********************** method with Noise GPS cansalation ********************************* //

                        if (distanceNow > (coefficient[0] * 0.100)) {
                            coefficient[0]++;
                        } else if (distanceNow < (coefficient[0] * 0.100)) {
                            double tempCost = (distanceNow * 1000 * (distanceRate / 200) / 10) * 4;
                            if (weather == false && shift == false) {
                                cost += tempCost * 4 ;
                            } else if (shift == true && weather == false) {
                                cost +=tempCost + ((tempCost) * 0.2) * 4;
                            } else if (shift == false && weather == true) {
                                cost += (tempCost) + ((tempCost) * 0.2) * 4;
                            } else {
                                cost += (tempCost) + ((tempCost) * 0.4) * 4;
                            }

                            distanceTraveled[0] = distanceNow + distanceTraveled[0];
                            distanceTiming[0] += distanceNow;
                        } else if (coefficient[0] == 3 ) {
                            distanceTraveled[0] = distanceNow + distanceTraveled[0];
                            coefficient[0] = 0;
                        } else if (timeWaiting == 15) {
                            if (distanceTiming[0] < 0.075) {
                                //cost += 15 * timeRate / 100;
                                if(weather == false && shift == false){
                                    cost += 15 * timeRate / 100;
                                }else if(shift == true && weather == false){
                                    cost += (15 * timeRate / 100) + ((15 * timeRate / 100) * 0.2);
                                }else if(shift == false && weather == true){
                                    cost += (15 * timeRate / 100) + ((15 * timeRate / 100) * 0.2);
                                }else{
                                    cost += (15 * timeRate / 100) + ((15 * timeRate / 100) * 0.4);
                                }
                                distanceTiming[0] = 0;
                                timeWaiting = 0;
                            } else {
                                timeWaiting = 0;
                                distanceTiming[0] = 0;
                            }
                        }



                        // *********************** method without Noise GPS cansalation ********************************* //
//                        if(weather == false && shift == false){
//                            if(zoneString.equals("1")) {
//                                cost += (distanceNow * 1000 * (distanceRate / 200) / 10) * 4;
//                            }else {
//                                cost += (distanceNow * 1000 * (distanceRate / 200) / 10);
//                            }
//                        }else if(shift == true && weather == false){
//                            if(zoneString.equals("1")) {
//                                cost += ((distanceNow * 1000 * (distanceRate / 200) / 10) + ((distanceNow * 1000 * (distanceRate / 200) / 10) * 0.2)) * 4;
//                            }else{
//                                cost += ((distanceNow * 1000 * (distanceRate / 200) / 10) + ((distanceNow * 1000 * (distanceRate / 200) / 10) * 0.2));
//                            }
//                        }else if(shift == false && weather == true){
//                            if(zoneString.equals("1")) {
//                                cost += ((distanceNow * 1000 * (distanceRate / 200) / 10) + ((distanceNow * 1000 * (distanceRate / 200) / 10) * 0.2)) * 4;
//                            }else{
//                                cost += ((distanceNow * 1000 * (distanceRate / 200) / 10) + ((distanceNow * 1000 * (distanceRate / 200) / 10) * 0.2)) ;
//                            }
//                        }else{
//                            if(zoneString.equals("1")) {
//                                cost += ((distanceNow * 1000 * (distanceRate / 200) / 10) + ((distanceNow * 1000 * (distanceRate / 200) / 10) * 0.4)) * 4;
//                            }else{
//                                cost += ((distanceNow * 1000 * (distanceRate / 200) / 10) + ((distanceNow * 1000 * (distanceRate / 200) / 10) * 0.4));
//
//                            }
//                        }
//                        distanceTraveled[0] = distanceNow + distanceTraveled[0];
//                        distanceTiming[0] += distanceNow;
//                        if (timeWaiting >= 15) {
//                            if (distanceTiming[0] < 0.075) {
//                                if(weather == false && shift == false){
//                                    if(zoneString.equals("1")) {
//                                        cost += (15 * timeRate / 100) * 4;
//                                    }else{
//                                        cost += (15 * timeRate / 100);
//                                    }
//                                }else if(shift == true && weather == false){
//                                    if(zoneString.equals("1")) {
//                                        cost += ((15 * timeRate / 100) + ((15 * timeRate / 100) * 0.2)) * 4;
//                                    }else{
//                                        cost += ((15 * timeRate / 100) + ((15 * timeRate / 100) * 0.2)) ;
//                                    }
//                                }else if(shift == false && weather == true){
//                                    if(zoneString.equals("1")) {
//                                        cost += ((15 * timeRate / 100) + ((15 * timeRate / 100) * 0.2)) * 4;
//                                    }else{
//                                        cost += ((15 * timeRate / 100) + ((15 * timeRate / 100) * 0.2)) ;
//                                    }
//                                }else{
//                                    if(zoneString.equals("1")) {
//                                        cost += ((15 * timeRate / 100) + ((15 * timeRate / 100) * 0.4)) * 4;
//                                    }else{
//                                        cost += ((15 * timeRate / 100) + ((15 * timeRate / 100) * 0.4));
//                                    }
//                                }
//                                distanceTiming[0] = 0;
//                                timeWaiting = 0;
//                            } else {
//                                timeWaiting = 0;
//                                distanceTiming[0] = 0;
//                            }
//                        }


                        // *********************** method with Noise GPS cansalation ********************************* //

//                        if (distanceNow > (coefficient[0] * 0)) {
//                            coefficient[0]++;
//                        } else if (distanceNow < (coefficient[0] * 1)) {
//                            double tempCost = (distanceNow * 1000 * (distanceRate / 200) / 10) * 4;
//                            if (weather == false && shift == false) {
//                                cost += tempCost;
//                            } else if (shift == true && weather == false) {
//                                cost +=tempCost + ((tempCost) * 0.2);
//                            } else if (shift == false && weather == true) {
//                                cost += (tempCost) + ((tempCost) * 0.2);
//                            } else {
//                                cost += (tempCost) + ((tempCost) * 0.4);
//                            }
//                            distanceTraveled[0] = distanceNow + distanceTraveled[0];
//                            distanceTiming[0] += distanceNow;
//                            coefficient[0] = 0;
//                        } else if (coefficient[0] == 20) {
//                            distanceTraveled[0] = distanceNow + distanceTraveled[0];
//                            coefficient[0] = 0;
//                        } else if (timeWaiting == 15) {
//                            if (distanceTiming[0] < 0.075) {
//                                //cost += 15 * timeRate / 100;
//                                if(weather == false && shift == false){
//                                    cost += 15 * timeRate / 100;
//                                }else if(shift == true && weather == false){
//                                    cost += (15 * timeRate / 100) + ((15 * timeRate / 100) * 0.2);
//                                }else if(shift == false && weather == true){
//                                    cost += (15 * timeRate / 100) + ((15 * timeRate / 100) * 0.2);
//                                }else{
//                                    cost += (15 * timeRate / 100) + ((15 * timeRate / 100) * 0.4);
//                                }
//                                distanceTiming[0] = 0;
//                                timeWaiting = 0;
//                            } else {
//                                timeWaiting = 0;
//                                distanceTiming[0] = 0;
//                            }
//                        }

                        String formattedLatitude2 = String.format("%.5f", latitude2);
                        String formattedLongitude2 = String.format("%.5f", longitude2);
                        tvLatitude.setText("Lat: " + formattedLatitude2);
                        tvLongitude.setText("Lng: " + formattedLongitude2);
                    }
                } else {
                    gpsTracker.showSettingsAlert();
                }
                if (flagStart == true) {
                    String formattedHour = String.format("%02d", hours);
                    String formattedMinute = String.format("%02d", minutes);
                    String formattedSecond = String.format("%02d", seconds);
                    hourTextView.setText(formattedHour);
                    minuteTextView.setText(formattedMinute);
                    secondTextView.setText(formattedSecond);
                    String formattedCost = String.format("%.2f", cost);
                    costTextView.setText(formattedCost);

                }
                handler.postDelayed(this, 1000);
            }

        };
        runnable4.run();
    }


    public void getVRate() {

        Call<List<VariableRate>> call = jsonPlaceHolderApi.getVRate();
        call.enqueue(new Callback<List<VariableRate>>() {
            @Override
            public void onResponse(Call<List<VariableRate>> call, Response<List<VariableRate>> response) {
                if (!response.isSuccessful()) {
                    //System.out.println("Code: " + response.code());
                    return;
                }
                List<VariableRate> posts = response.body();
                for (VariableRate variableRate : posts) {
                    shift = variableRate.isTime();
                    weather = variableRate.isWeather();
                }
                // rate = eventsRate * weatherRate * districtRate;
            }

            @Override
            public void onFailure(Call<List<VariableRate>> call, Throwable t) {
                //System.out.println("not response");
            }
        });
        call = null;
    }


    public void getSRate() {

        Call<List<StaticRate>> call = jsonPlaceHolderApi.getSRate();
        call.enqueue(new Callback<List<StaticRate>>() {
            @Override
            public void onResponse(Call<List<StaticRate>> call, Response<List<StaticRate>> response) {
                if (!response.isSuccessful()) {
                    //System.out.println("Code: " + response.code());
                    return;
                }
                List<StaticRate> srates = response.body();
                for (StaticRate staticRate : srates) {
                    if (zoneString.equals(String.valueOf(staticRate.getZone())) && (typeString.equals(String.valueOf(staticRate.getType()))) && (carString.equals(String.valueOf(staticRate.getVehicle())))) {


                        initionalRate = staticRate.getInitional();
                        distanceRate = staticRate.getDistance();
                        timeRate = staticRate.getTime();
                    }
                }
                //  rate = eventsRate * weatherRate * districtRate;
            }

            @Override
            public void onFailure(Call<List<StaticRate>> call, Throwable t) {
                //System.out.println("not response");
            }
        });
        call = null;
    }


    private void createPost(int idCode, double lat, double lng) {

        Post post = new Post(idCode, lat, lng, flagStart, false, false, false);

        Call<Post> call = jsonPlaceHolderApi.createLocation(post);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (!response.isSuccessful()) {
                    //System.out.println("Code: " + response.code());
                    return;
                }

//                Post postResponse = response.body();

//                String content = "";
//                content = "Code: " + response.code() + "\n";
//                content = "ID: " + postResponse.getId() + "\n";
//                content += "Identity Code: " + postResponse.getIdentityCode() + "\n";
//                content += "Latitude: " + postResponse.getLatitude() + "\n";
//                content += "Longitude: " + postResponse.getLongitude() + "\n\n";
//                System.out.println(content);

            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });
        post = null;

    }


    void startTimer() {
        if (flagStart == false) {
            flagStart = true;
            startTime = System.currentTimeMillis();
            createJourney(identityCode, latitude2, longitude2, 0, 0, true, false);
        } else {
            flagStart = true;
        }
        ImageView image = (ImageView) findViewById(R.id.countdownIcon);
        Resources res = getResources();
        image.setImageDrawable(res.getDrawable(R.drawable.playicon));
        pointFlag[0] = true;
    }


    void stopTimer() {
        flagStart = false;
        Timer = 0;
        seconds = 0;
        minutes = 0;
        hours = 0;
        elapsed = 0;
        cost = initionalRate / 10;
        pointFlag[0] = false;
        ImageView image = (ImageView) findViewById(R.id.countdownIcon);
        Resources res = getResources();
        image.setImageDrawable(res.getDrawable(R.drawable.stopicon));
        distanceTraveled[0] = 0;
    }

    void pauseTimer() {
        if (flagStart == true) {
            Timer = System.currentTimeMillis() - startTime;
            elapsed = Timer + elapsed;
            Timer = 0;
            flagStart = false;
            pointFlag[0] = false;
        }
        ImageView image = (ImageView) findViewById(R.id.countdownIcon);
        Resources res = getResources();
        image.setImageDrawable(res.getDrawable(R.drawable.pauseicon));
    }

    int printCountDown() {
        nowTime = System.currentTimeMillis();
        return (int) (elapsed + System.currentTimeMillis() - startTime);
    }


    public void StartCountdown(View view) {
        startSound = MediaPlayer.create(Single_Taximeter.this, R.raw.start_journey);
        startSound.start();
        startTimer();
        singleTaximeterScreen.setBackgroundColor(Color.rgb(0, 77, 64));
        flagStart = true;
    }

    public void PauseCountdown(View view) {
        pauseSound = MediaPlayer.create(Single_Taximeter.this, R.raw.pause_journey);
        pauseSound.start();
        pauseTimer();
        singleTaximeterScreen.setBackgroundColor(Color.rgb(191, 54, 12));
    }

    public void StopCountdown(View view) {
        stopSound = MediaPlayer.create(Single_Taximeter.this, R.raw.stop_journey);
        stopSound.start();
        canStart = false;
        ManPlace.setEnabled(true);
        WomanPlace.setEnabled(true);
        ManPlace.setBackgroundColor(Color.rgb(53, 53, 53));
        WomanPlace.setBackgroundColor(Color.rgb(53, 53, 53));
        stopTimer();
        singleTaximeterScreen.setBackgroundColor(Color.rgb(183, 28, 28));
    }

    public void finishJourneyClicked(View view) {
        finishSound = MediaPlayer.create(Single_Taximeter.this, R.raw.finish_journey);
        finishSound.start();
        flagStart = false;
        Timer = 0;
        seconds = 0;
        minutes = 0;
        hours = 0;
        elapsed = 0;
        pointFlag[0] = false;
        singleTaximeterScreen.setBackgroundColor(Color.rgb(26, 35, 126));
        String formattedLatitude1 = String.format("%.5f", latitude1);
        String formattedLongitude1 = String.format("%.5f", longitude1);
        String formattedLatitude2 = String.format("%.5f", latitude2);
        String formattedLongitude2 = String.format("%.5f", longitude2);
        String formattedDistance = String.format("%.3f", distanceTraveled[0]);
        String formattedCost = String.format("%.3f", cost);

        distanceTraveled[0] = 0;
        cost = initionalRate / 10;

        Intent i3 = new Intent(Single_Taximeter.this, FinishSingleTaximeter.class);
        i3.putExtra("idDriver", String.valueOf(identityCode));
        i3.putExtra("startTime", String.valueOf(startTime));
        i3.putExtra("finishTime", String.valueOf(System.currentTimeMillis()));
        i3.putExtra("srcLat", formattedLatitude1);
        i3.putExtra("srcLng", formattedLongitude1);
        i3.putExtra("desLat", formattedLatitude2);
        i3.putExtra("desLng", formattedLongitude2);
        i3.putExtra("distance", formattedDistance);
        i3.putExtra("isMale", isMale);
        i3.putExtra("cost", formattedCost);
        startActivity(i3);

        finish();
    }


    private void createJourney(int IdDriver, double Lat, double Lng, double Distance, double Cost, boolean IsStart, boolean IsFinish) {

        Journey journey = new Journey(IdDriver, Lat, Lng, Distance, Cost, 1, IsStart, IsFinish, isMale,false);

        Call<Journey> call = jsonPlaceHolderApi.createJourney("application/json", journey);

        call.enqueue(new Callback<Journey>() {
            @Override
            public void onResponse(Call<Journey> call, Response<Journey> response) {
                if (!response.isSuccessful()) {
                    //System.out.println("Code: " + response.code());
                    return;
                }

                Journey postResponse = response.body();

//                String content = "";
//                content = "Code: " + response.code() + "\n";
//                content += "ID: " + postResponse.getId() + "\n";
//                content += "Identity Code: " + postResponse.getIdDriver() + "\n";
//                content += "Distance: " + postResponse.getDistance() + "\n";
//                content += "Cost: " + postResponse.getCost() + "\n";
                //System.out.println(content);

            }

            @Override
            public void onFailure(Call<Journey> call, Throwable t) {

            }
        });
        journey = null;
    }


    public void womanPlaceClicked(View view) {
        isMale = false;
        if (canStart == false) {
            ManPlace.setEnabled(false);
            WomanPlace.setEnabled(false);
            canStart = true;
        }

        if (canStart == true) {
            StartTimerButton.setEnabled(true);
            if (isMale == true) {
                ManPlace.setBackgroundColor(Color.rgb(255, 0, 0));
            } else {
                WomanPlace.setBackgroundColor(Color.rgb(255, 0, 0));

            }
        } else {
            StartTimerButton.setEnabled(false);
        }

    }

    public void manPlaceClicked(View view) {
        isMale = true;
        if (canStart == false) {
            ManPlace.setEnabled(false);
            WomanPlace.setEnabled(false);
            canStart = true;
        }

        if (canStart == true) {
            StartTimerButton.setEnabled(true);
            if (isMale == true) {
                ManPlace.setBackgroundColor(Color.rgb(255, 0, 0));
            } else {
                WomanPlace.setBackgroundColor(Color.rgb(255, 0, 0));

            }
        } else {
            StartTimerButton.setEnabled(false);
        }

    }
}