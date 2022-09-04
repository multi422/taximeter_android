package ir.mashhadict.taximeter;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


@RequiresApi(api = Build.VERSION_CODES.O)
public class Multi_Taximeter extends Activity {


    private TextView tvLatitudeMulti, tvLongitudeMulti;
    Boolean[] flagStart = {false, false, false};
    long[] startTime = {0, 0, 0};
    long[] nowTime = {System.currentTimeMillis(), System.currentTimeMillis(), System.currentTimeMillis()};
    long[] Timer = {0, 0, 0};
    long[] elapsed = {0, 0, 0};
    double[] latitude1 = {0, 0, 0};
    double[] longitude1 = {0, 0, 0};
    double[] startLatitude = new double[3];
    double[] startLongitude = new double[3];
    double[] latitude2 = {0, 0, 0};
    double[] longitude2 = {0, 0, 0};
    final double[] distanceTraveled = {0, 0, 0};
    final boolean[] pointFlag = {false, false, false};
    int[] hours = {0, 0, 0};
    int[] minutes = {0, 0, 0};
    int[] seconds = {0, 0, 0};
    double[] duration = {0, 0, 0};
    int eventsRate = 1;
    int weatherRate = 1;
    int districtRate = 1;
    double rate = 1;
    double[] cost = {0, 0, 0};
    String[] startDate = {"", "", ""};
    String[] finishDate = {"", "", ""};
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    JsonPlaceHolderApi jsonPlaceHolderApi;
    boolean[] isStart = {false, false, false};
    double[] distanceNow = {0, 0, 0};


    int identityCode;
    String firstName;
    String lastName;
    String carModel;
    String pelak;
    String color;


    boolean doubleBackToExitPressedOnce = false;

    private void createPost(int idCode, double lat, double lng) {

        boolean isWorkingPassOne = false;
        boolean isWorkingPassTwo = false;
        boolean isWorkingPassThree = false;

        if (flagStart[0] == true) {
            isWorkingPassOne = true;
        }
        if (flagStart[1] == true) {
            isWorkingPassTwo = true;
        }
        if (flagStart[2] == true) {
            isWorkingPassThree = true;
        }

        Post post = new Post(idCode, lat, lng, false, isWorkingPassOne, isWorkingPassTwo, isWorkingPassThree);

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
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "برای خروج از صفحه تاکسی متر چندنفره دوباره دکمه بازگشت را بزنید", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 1000);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }


    int printOneCountDown() {
        nowTime[0] = System.currentTimeMillis();
        return (int) (elapsed[0] + System.currentTimeMillis() - startTime[0]);
    }

    int printTwoCountDown() {
        nowTime[1] = System.currentTimeMillis();
        return (int) (elapsed[1] + System.currentTimeMillis() - startTime[1]);
    }

    int printThreeCountDown() {
        nowTime[2] = System.currentTimeMillis();
        return (int) (elapsed[2] + System.currentTimeMillis() - startTime[2]);
    }

    void pauseOneTimer() {
        if (flagStart[0] == true) {
            Timer[0] = System.currentTimeMillis() - startTime[0];
            elapsed[0] = Timer[0] + elapsed[0];
            Timer[0] = 0;
            flagStart[0] = false;
            pointFlag[0] = false;
        }
    }

    void pauseTwoTimer() {
        if (flagStart[1] == true) {
            Timer[1] = System.currentTimeMillis() - startTime[1];
            elapsed[1] = Timer[1] + elapsed[1];
            Timer[1] = 0;
            flagStart[1] = false;
            pointFlag[1] = false;
        }
    }

    void pauseThreeTimer() {
        if (flagStart[2] == true) {
            Timer[2] = System.currentTimeMillis() - startTime[2];
            elapsed[2] = Timer[2] + elapsed[2];
            Timer[2] = 0;
            flagStart[2] = false;
            pointFlag[2] = false;
        }
    }


    void stopOneTimer() {
        flagStart[0] = false;
        Timer[0] = 0;
        seconds[0] = 0;
        minutes[0] = 0;
        hours[0] = 0;
        elapsed[0] = 0;
        pointFlag[0] = false;
        distanceTraveled[0] = 0;
    }

    void stopTwoTimer() {
        flagStart[1] = false;
        Timer[1] = 0;
        seconds[1] = 0;
        minutes[1] = 0;
        hours[1] = 0;
        elapsed[1] = 0;
        pointFlag[1] = false;
        distanceTraveled[1] = 0;
    }

    void stopThreeTimer() {
        flagStart[2] = false;
        Timer[2] = 0;
        seconds[2] = 0;
        minutes[2] = 0;
        hours[2] = 0;
        elapsed[2] = 0;
        pointFlag[2] = false;
        distanceTraveled[2] = 0;
    }


    void startOneTimer() {
        if (flagStart[0] == false) {
            flagStart[0] = true;
            startTime[0] = System.currentTimeMillis();
            LocalDateTime now = LocalDateTime.now();
            startDate[0] = dateFormatter.format(now);
        } else {
            flagStart[0] = true;
        }
        pointFlag[0] = true;
    }

    void startTwoTimer() {
        if (flagStart[1] == false) {
            flagStart[1] = true;
            startTime[1] = System.currentTimeMillis();
            LocalDateTime now = LocalDateTime.now();
            startDate[1] = dateFormatter.format(now);
        } else {
            flagStart[1] = true;
        }
        pointFlag[1] = true;
    }

    void startThreeTimer() {
        if (flagStart[2] == false) {
            flagStart[2] = true;
            startTime[2] = System.currentTimeMillis();
            LocalDateTime now = LocalDateTime.now();
            startDate[2] = dateFormatter.format(now);
        } else {
            flagStart[2] = true;
        }
        pointFlag[2] = true;
    }

    public void getRate() {
        Call<List<Rate>> call = jsonPlaceHolderApi.getRate();
        call.enqueue(new Callback<List<Rate>>() {
            @Override
            public void onResponse(Call<List<Rate>> call, Response<List<Rate>> response) {
                if (!response.isSuccessful()) {
                    //System.out.println("Code: " + response.code());
                    return;
                }
                List<Rate> posts = response.body();
                for (Rate rate : posts) {
                    eventsRate = rate.getEvents();
                    weatherRate = rate.getWeather();
                    districtRate = rate.getDistrict();
                }
                rate = eventsRate * weatherRate * districtRate;
            }

            @Override
            public void onFailure(Call<List<Rate>> call, Throwable t) {
                //System.out.println("not response");
            }
        });
    }


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


    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.multi_taximeter);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        TextView gpsAlarmMultiTaximeter = findViewById(R.id.gpsAlarmMultiTaximeterText);
        TextView netAlarmMultiTaximeter = findViewById(R.id.netAlarmMultiTaximeterText);


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
            pelak = extras.getString("pelak");
            color = extras.getString("color");

        }


        final int[] coefficient = {0, 0, 0};
        tvLatitudeMulti = (TextView) findViewById(R.id.latitudeMultiTaximeter);
        tvLongitudeMulti = (TextView) findViewById(R.id.longitudeMultimeter);
        TextView DstOnePas = findViewById(R.id.dstOnePas);
        TextView DstTwoPas = findViewById(R.id.dstTwoPas);
        TextView DstThreePas = findViewById(R.id.dstThreePas);

        TextView MinuteOnePass = findViewById(R.id.minuteOnePass);
        TextView MinuteTwoPass = findViewById(R.id.minuteTwoPass);
        TextView MinuteThreePass = findViewById(R.id.minuteThreePass);
        TextView SecOnePass = findViewById(R.id.secOnePass);
        TextView SecTwoPass = findViewById(R.id.secTwoPass);
        TextView SecThreePass = findViewById(R.id.secThreePass);

        TextView eventsTextMultiView = findViewById(R.id.eventsTextMulti);
        TextView weatherTextMultiView = findViewById(R.id.weatherTextMulti);
        TextView districtTextMultiView = findViewById(R.id.districtTextMulti);

        TextView CostOnePass = findViewById(R.id.costOnePass);
        TextView CostTwoPass = findViewById(R.id.costTwoPass);
        TextView CostThreePass = findViewById(R.id.costThreePass);

        LinearLayout PassOneRegion = findViewById(R.id.passOneRegion);
        LinearLayout PassTwoRegion = findViewById(R.id.passTwoRegion);
        LinearLayout PassThreeRegion = findViewById(R.id.passThreeRegion);

        Resources res = getResources();
        Resources res2 = getResources();
        Resources res3 = getResources();

;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://taximeterict.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);


        final Handler handler = new Handler();


        Runnable runnable = new Runnable() {
            public void run() {
                GpsTracker gpsTracker = new GpsTracker(Multi_Taximeter.this);
                ImageView imageCheckNetMultiTaximeter = (ImageView) findViewById(R.id.netCheckIconMultitTaximeter);
                ImageView checkGPSMultiTaximeter = (ImageView) findViewById(R.id.gpsCheckIconMultitTaximeter);


                if (isNetworkConnected()) {
                    Resources res = getResources();
                    imageCheckNetMultiTaximeter.setImageDrawable(res.getDrawable(R.drawable.netconnected));
                    netAlarmMultiTaximeter.setText("");
                    getRate();
                } else {
                    Resources res = getResources();
                    imageCheckNetMultiTaximeter.setImageDrawable(res.getDrawable(R.drawable.netnotconnected));
                    netAlarmMultiTaximeter.setText("اینترنت تلفن همراه خود را روشن کنید !");
                    Animation anim = new AlphaAnimation(0.0f, 1.0f);
                    anim.setDuration(500); //You can manage the blinking time with this parameter
                    anim.setStartOffset(40);
                    anim.setRepeatMode(Animation.REVERSE);
                    anim.setRepeatCount(Animation.INFINITE);
                    netAlarmMultiTaximeter.startAnimation(anim);
                    districtRate = 1;
                    eventsRate = 1;
                    weatherRate = 1;
                }

                final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Resources res = getResources();
                    checkGPSMultiTaximeter.setImageDrawable(res.getDrawable(R.drawable.gpsnotconnected));
                    gpsAlarmMultiTaximeter.setText("موقعیت مکانی خود را روشن کنید !");
                    Animation anim2 = new AlphaAnimation(0.0f, 1.0f);
                    anim2.setDuration(50); //You can manage the blinking time with this parameter
                    anim2.setStartOffset(20);
                    anim2.setRepeatMode(Animation.REVERSE);
                    anim2.setRepeatCount(Animation.INFINITE);
                    gpsAlarmMultiTaximeter.startAnimation(anim2);
                } else {
                    Resources res = getResources();
                    checkGPSMultiTaximeter.setImageDrawable(res.getDrawable(R.drawable.gpsconnected));
                    gpsAlarmMultiTaximeter.setText("");
                }

                eventsTextMultiView.setText(Integer.toString(eventsRate));
                weatherTextMultiView.setText(Integer.toString(weatherRate));
                districtTextMultiView.setText(Integer.toString(districtRate));

                // ------------------------------------------------------------- //

                if (gpsTracker.canGetLocation()) {
                    if (pointFlag[0] == false) {
                        latitude2[0] = gpsTracker.getLatitude();
                        longitude2[0] = gpsTracker.getLongitude();

                        if (seconds[0] == 0) {
                            startLatitude[0] = latitude2[0];
                            startLongitude[0] = longitude2[0];
                        }

                        String formattedLatitude2 = String.format("%.5f", latitude2[0]);
                        String formattedLongitude2 = String.format("%.5f", longitude2[0]);
                        tvLatitudeMulti.setText("Lat: " + formattedLatitude2);
                        tvLongitudeMulti.setText("Lng: " + formattedLongitude2);
                    } else {
                        latitude1[0] = latitude2[0];
                        longitude1[0] = longitude2[0];
                        String formattedDistanceOneTraveled = String.format("%.3f", distanceTraveled[0]);
                        DstOnePas.setText(formattedDistanceOneTraveled + " کیلومتر");
                        latitude2[0] = gpsTracker.getLatitude();
                        longitude2[0] = gpsTracker.getLongitude();
                        distanceNow[0] = distance(latitude2[0], latitude1[0], longitude2[0], longitude1[0]);
                        if (distanceNow[0] > (coefficient[0] * 0.036)) {
                            coefficient[0]++;
                        } else if (distanceNow[0] < (coefficient[0] * 0.036)) {
                            distanceTraveled[0] = distanceNow[0] + distanceTraveled[0];
                            coefficient[0] = 0;
                        } else if (coefficient[0] == 8) {
                            distanceTraveled[0] = distanceNow[0] + distanceTraveled[0];
                            coefficient[0] = 0;
                        }
                        String formattedLatitude2 = String.format("%.5f", latitude2[0]);
                        String formattedLongitude2 = String.format("%.5f", longitude2[0]);
                        tvLatitudeMulti.setText("Lat: " + formattedLatitude2);
                        tvLongitudeMulti.setText("Lng: " + formattedLongitude2);
                    }


                    // ------------------------------------------- //

                    if (pointFlag[1] == false) {
                        latitude2[1] = gpsTracker.getLatitude();
                        longitude2[1] = gpsTracker.getLongitude();

                        if (seconds[1] == 0) {
                            startLatitude[1] = latitude2[1];
                            startLongitude[1] = longitude2[1];
                        }
                    } else {
                        latitude1[1] = latitude2[1];
                        longitude1[1] = longitude2[1];
                        String formattedDistanceTwoTraveled = String.format("%.3f", distanceTraveled[1]);
                        DstTwoPas.setText(formattedDistanceTwoTraveled + " کیلومتر");
                        latitude2[1] = gpsTracker.getLatitude();
                        longitude2[1] = gpsTracker.getLongitude();
                        distanceNow[1] = distance(latitude2[1], latitude1[1], longitude2[1], longitude1[1]);
                        if (distanceNow[1] > (coefficient[1] * 0.036)) {
                            coefficient[1]++;
                        } else if (distanceNow[1] < (coefficient[1] * 0.036)) {
                            distanceTraveled[1] = distanceNow[1] + distanceTraveled[1];
                            coefficient[1] = 0;
                        } else if (coefficient[1] == 8) {
                            distanceTraveled[1] = distanceNow[1] + distanceTraveled[1];
                            coefficient[1] = 0;
                        }
                    }


                    // ------------------------------------------- //

                    if (pointFlag[2] == false) {
                        latitude2[2] = gpsTracker.getLatitude();
                        longitude2[2] = gpsTracker.getLongitude();

                        if (seconds[2] == 0) {
                            startLatitude[2] = latitude2[2];
                            startLongitude[2] = longitude2[2];
                        }
                    } else {
                        latitude1[2] = latitude2[2];
                        longitude1[2] = longitude2[2];
                        String formattedDistanceThreeTraveled = String.format("%.3f", distanceTraveled[2]);
                        DstThreePas.setText(formattedDistanceThreeTraveled + " کیلومتر");
                        latitude2[2] = gpsTracker.getLatitude();
                        longitude2[2] = gpsTracker.getLongitude();
                        distanceNow[2] = distance(latitude2[2], latitude1[2], longitude2[2], longitude1[2]);
                        if (distanceNow[2] > (coefficient[2] * 0.036)) {
                            coefficient[2]++;
                        } else if (distanceNow[2] < (coefficient[2] * 0.036)) {
                            distanceTraveled[2] = distanceNow[2] + distanceTraveled[2];
                            coefficient[2] = 0;
                        } else if (coefficient[2] == 8) {
                            distanceTraveled[2] = distanceNow[2] + distanceTraveled[2];
                            coefficient[2] = 0;
                        }
                    }

                } else {
                    gpsTracker.showSettingsAlert();
                }
                if (flagStart[0] == true) {
                    MinuteOnePass.setText(Integer.toString(minutes[0]));
                    SecOnePass.setText(Integer.toString(seconds[0]));
                    String formattedCost = String.format("%.2f", cost[0]);
                    CostOnePass.setText(formattedCost + " تومان");
                }

                if (flagStart[1] == true) {
                    MinuteTwoPass.setText(Integer.toString(minutes[1]));
                    SecTwoPass.setText(Integer.toString(seconds[1]));
                    String formattedCost = String.format("%.2f", cost[1]);
                    CostTwoPass.setText(formattedCost + " تومان");
                }

                if (flagStart[2] == true) {
                    MinuteThreePass.setText(Integer.toString(minutes[2]));
                    SecThreePass.setText(Integer.toString(seconds[2]));
                    String formattedCost = String.format("%.2f", cost[2]);
                    CostThreePass.setText(formattedCost + " تومان");
                }

                handler.postDelayed(this, 1000);
            }

        };
        runnable.run();


        Runnable runnable2 = new Runnable() {
            public void run() {

                duration[0] = printOneCountDown() / 1000;
                if (flagStart[0] == true) {
                    hours[0] = (int) duration[0] / 3600;
                    minutes[0] = (int) (duration[0] % 3600) / 60;
                    seconds[0] = (int) (duration[0] % 60);
                    cost[0] = (distanceTraveled[0] * rate * 35) + 500 + (hours[0] * rate * 0.6 * 3600) + (minutes[0] * rate * 0.6 * 60) + (seconds[0] * rate * 0.6);
                }


                duration[1] = printTwoCountDown() / 1000;
                if (flagStart[1] == true) {
                    hours[1] = (int) duration[1] / 3600;
                    minutes[1] = (int) (duration[1] % 3600) / 60;
                    seconds[1] = (int) (duration[1] % 60);
                    cost[1] = (distanceTraveled[1] * rate * 35) + 500 + (hours[1] * rate * 0.6 * 3600) + (minutes[1] * rate * 0.6 * 60) + (seconds[1] * rate * 0.6);
                }


                duration[2] = printThreeCountDown() / 1000;
                if (flagStart[2] == true) {
                    hours[2] = (int) duration[2] / 3600;
                    minutes[2] = (int) (duration[2] % 3600) / 60;
                    seconds[2] = (int) (duration[2] % 60);
                    cost[2] = (distanceTraveled[2] * rate * 35) + 500 + (hours[2] * rate * 0.6 * 3600) + (minutes[2] * rate * 0.6 * 60) + (seconds[2] * rate * 0.6);
                }


                handler.postDelayed(this, 1000);
            }

        };
        runnable2.run();


        Runnable runnable3 = new Runnable() {
            public void run() {

                GpsTracker gpsTracker = new GpsTracker(Multi_Taximeter.this);
                if (gpsTracker.canGetLocation()) {
                    createPost(identityCode, latitude2[0], longitude2[0]);
                    // createPost(925485640, latitude2, longitude2, "2022-08-24T15:27:47Z");
                } else {
                    gpsTracker.showSettingsAlert();
                }
                handler.postDelayed(this, 14300);
            }

        };
        runnable3.run();


        PassOneRegion.setOnTouchListener(new View.OnTouchListener() {
            Handler handler = new Handler();

            int numberOfTaps = 0;
            long lastTapTimeMs = 0;
            long touchDownMs = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        touchDownMs = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                        handler.removeCallbacksAndMessages(null);

                        if ((System.currentTimeMillis() - touchDownMs) > ViewConfiguration.getTapTimeout()) {
                            //it was not a tap

                            numberOfTaps = 0;
                            lastTapTimeMs = 0;
                            break;
                        }

                        if (numberOfTaps > 0
                                && (System.currentTimeMillis() - lastTapTimeMs) < ViewConfiguration.getDoubleTapTimeout()) {
                            numberOfTaps += 1;
                        } else {
                            numberOfTaps = 1;
                            if (isStart[0] == false) {
                                isStart[0] = true;
                                startOneTimer();
                                Toast.makeText(getApplicationContext(), "تایمر مسافر 1 شروع به کار کرد", Toast.LENGTH_SHORT).show();
                                PassOneRegion.setBackgroundColor(Color.rgb(0,96,100));
                            } else if (isStart[0] == true) {
                                isStart[0] = false;
                                stopOneTimer();
                                Toast.makeText(getApplicationContext(), "سفر مسافر 1 به اتمام رسید !", Toast.LENGTH_SHORT).show();
                                PassOneRegion.setBackgroundColor(Color.rgb(1,87,155));
                                //CountdownOneIcon.setImageDrawable(res.getDrawable(R.drawable.stop));
                            }
                        }

                        lastTapTimeMs = System.currentTimeMillis();

                        if (numberOfTaps == 3) {

                            //handle triple tap
                        } else if (numberOfTaps == 2) {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //handle double tap
                                    pauseOneTimer();
                                    Toast.makeText(getApplicationContext(), "سفر مسافر 1 به اتمام رسید !", Toast.LENGTH_SHORT).show();
                                    //CountdownOneIcon.setImageDrawable(res.getDrawable(R.drawable.stop));
                                    isStart[0] = false;
                                }
                            }, ViewConfiguration.getDoubleTapTimeout());
                        }
                }

                return true;
            }

        });


        PassTwoRegion.setOnTouchListener(new View.OnTouchListener() {
            Handler handler2 = new Handler();

            int numberOfTaps2 = 0;
            long lastTapTimeMs2 = 0;
            long touchDownMs2 = 0;

            @Override
            public boolean onTouch(View v2, MotionEvent event2) {
                switch (event2.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        touchDownMs2 = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                        handler2.removeCallbacksAndMessages(null);

                        if ((System.currentTimeMillis() - touchDownMs2) > ViewConfiguration.getTapTimeout()) {
                            //it was not a tap
                            numberOfTaps2 = 0;
                            lastTapTimeMs2 = 0;
                            break;
                        }

                        if (numberOfTaps2 > 0
                                && (System.currentTimeMillis() - lastTapTimeMs2) < ViewConfiguration.getDoubleTapTimeout()) {
                            numberOfTaps2 += 1;
                        } else {
                            numberOfTaps2 = 1;
                            if (isStart[1] == false) {
                                isStart[1] = true;
                                startTwoTimer();
                                Toast.makeText(getApplicationContext(), "تایمر مسافر 2 شروع به کار کرد", Toast.LENGTH_SHORT).show();
                               // CountdownTwoIcon.setImageDrawable(res2.getDrawable(R.drawable.play));
                                PassTwoRegion.setBackgroundColor(Color.rgb(0,96,100));
                            } else if (isStart[1] == true) {
                                isStart[1] = false;
                                stopTwoTimer();
                                Toast.makeText(getApplicationContext(), "تایمر مسافر 2 متوقف شد", Toast.LENGTH_SHORT).show();
                                PassTwoRegion.setBackgroundColor(Color.rgb(1,87,155));
                                //CountdownTwoIcon.setImageDrawable(res2.getDrawable(R.drawable.stop));
                            }
                        }

                        lastTapTimeMs2 = System.currentTimeMillis();

                        if (numberOfTaps2 == 3) {

                            //handle triple tap
                        } else if (numberOfTaps2 == 2) {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //handle double tap
                                    pauseTwoTimer();
                                    Toast.makeText(getApplicationContext(), "سفر مسافر 2 به اتمام رسید !", Toast.LENGTH_SHORT).show();
                                    //CountdownTwoIcon.setImageDrawable(res2.getDrawable(R.drawable.stop));
                                    isStart[1] = false;
                                }
                            }, ViewConfiguration.getDoubleTapTimeout());
                        }
                }

                return true;
            }

        });


        PassThreeRegion.setOnTouchListener(new View.OnTouchListener() {
            Handler handler3 = new Handler();

            int numberOfTaps3 = 0;
            long lastTapTimeMs3 = 0;
            long touchDownMs3 = 0;

            @Override
            public boolean onTouch(View v3, MotionEvent event3) {
                switch (event3.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        touchDownMs3 = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                        handler3.removeCallbacksAndMessages(null);

                        if ((System.currentTimeMillis() - touchDownMs3) > ViewConfiguration.getTapTimeout()) {
                            //it was not a tap
                            numberOfTaps3 = 0;
                            lastTapTimeMs3 = 0;
                            break;
                        }

                        if (numberOfTaps3 > 0
                                && (System.currentTimeMillis() - lastTapTimeMs3) < ViewConfiguration.getDoubleTapTimeout()) {
                            numberOfTaps3 += 1;
                        } else {
                            numberOfTaps3 = 1;
                            if (isStart[2] == false) {
                                isStart[2] = true;
                                startThreeTimer();
                                Toast.makeText(getApplicationContext(), "تایمر مسافر 3 شروع به کار کرد", Toast.LENGTH_SHORT).show();
                                //CountdownThreeIcon.setImageDrawable(res3.getDrawable(R.drawable.play));
                                PassThreeRegion.setBackgroundColor(Color.rgb(0,96,100));
                            } else if (isStart[2] == true) {
                                isStart[2] = false;
                                stopThreeTimer();
                                Toast.makeText(getApplicationContext(), "تایمر مسافر 3 متوقف شد", Toast.LENGTH_SHORT).show();
                                PassThreeRegion.setBackgroundColor(Color.rgb(1,87,155));
                                //CountdownThreeIcon.setImageDrawable(res3.getDrawable(R.drawable.stop));
                            }
                        }

                        lastTapTimeMs3 = System.currentTimeMillis();

                        if (numberOfTaps3 == 3) {

                            //handle triple tap
                        } else if (numberOfTaps3 == 2) {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //handle double tap
                                    pauseThreeTimer();
                                    Toast.makeText(getApplicationContext(), "سفر مسافر 3 به اتمام رسید !", Toast.LENGTH_SHORT).show();
                                    //CountdownThreeIcon.setImageDrawable(res2.getDrawable(R.drawable.stop));
                                    isStart[2] = false;
                                }
                            }, ViewConfiguration.getDoubleTapTimeout());
                        }
                }

                return true;
            }

        });


    }

}