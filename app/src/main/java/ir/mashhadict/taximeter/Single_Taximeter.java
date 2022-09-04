package ir.mashhadict.taximeter;

import android.app.Activity;
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
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


@RequiresApi(api = Build.VERSION_CODES.O)
public class Single_Taximeter extends Activity {

    private TextView tvLatitude, tvLongitude;
    boolean flagStart = false;
    long startTime = 0;
    long nowTime = System.currentTimeMillis();
    long Timer = 0;
    long elapsed = 0;
    double latitude1 = 0;
    double longitude1 = 0;
    double startLatitude;
    double startLongitude;
    double latitude2;
    double longitude2;
    final double[] distanceTraveled = {0};
    final boolean[] pointFlag = {false};
    int hours = 0;
    int minutes = 0;
    int seconds = 0;
    double duration = 0;
    int eventsRate = 1;
    int weatherRate = 1;
    int districtRate = 1;
    double rate = 1;
    double cost = 0;
    String startDate;
    String finishDate;
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    JsonPlaceHolderApi jsonPlaceHolderApi;

    int identityCode;
    String firstName;
    String lastName;
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

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://taximeterict.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);


        TextView secondTextView = findViewById(R.id.secondCountdown);
        TextView minuteTextView = findViewById(R.id.minuteCountdown);
        TextView hourTextView = findViewById(R.id.hourCountdown);

        TextView distanceTextView = findViewById(R.id.distanceTextView);

        TextView eventsTextView = findViewById(R.id.eventsText);
        TextView weatherTextView = findViewById(R.id.weatherText);
        TextView districtTextView = findViewById(R.id.districtText);

        TextView costTextView = findViewById(R.id.costText);

        ImageView image = (ImageView) findViewById(R.id.countdownIcon);
        Resources res = getResources();
        image.setImageDrawable(res.getDrawable(R.drawable.stop));
        tvLatitude = (TextView) findViewById(R.id.latitude);
        tvLongitude = (TextView) findViewById(R.id.longitude);
        final Handler handler = new Handler();
        final int[] coefficient = {1};


        Runnable runnable = new Runnable() {
            public void run() {

                GpsTracker gpsTracker = new GpsTracker(Single_Taximeter.this);
                if (gpsTracker.canGetLocation()) {

                    createPost(identityCode, latitude2, longitude2);
                    // createPost(925485640, latitude2, longitude2, "2022-08-24T15:27:47Z");
                } else {
                    gpsTracker.showSettingsAlert();
                }
                handler.postDelayed(this, 14300);
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

                    cost = (distanceTraveled[0] * rate * 35) + 500 + (hours * rate * 0.6 * 3600) + (minutes * rate * 0.6 * 60) + (seconds * rate * 0.6);

                }

                handler.postDelayed(this, 1000);
            }

        };
        runnable2.run();


        Runnable runnable4 = new Runnable() {
            public void run() {
                GpsTracker gpsTracker = new GpsTracker(Single_Taximeter.this);
                ImageView checkNetSingleTaximeter = (ImageView) findViewById(R.id.checkInternetConnectionSingleMultimeter);
                ImageView checkGPSSingleTaximeter = (ImageView) findViewById(R.id.GpsConnectionSingleMultimeter);

                if (isNetworkConnected()) {
                    Resources res = getResources();
                    checkNetSingleTaximeter.setImageDrawable(res.getDrawable(R.drawable.netconnected));
                    netAlarmSingleTaximeter.setText("");
                    getRate();
                } else {
                    Resources res = getResources();
                    checkNetSingleTaximeter.setImageDrawable(res.getDrawable(R.drawable.netnotconnected));
                    netAlarmSingleTaximeter.setText("اینترنت تلفن همراه خود را روشن کنید !");
                    Animation anim = new AlphaAnimation(0.0f, 1.0f);
                    anim.setDuration(500); //You can manage the blinking time with this parameter
                    anim.setStartOffset(40);
                    anim.setRepeatMode(Animation.REVERSE);
                    anim.setRepeatCount(Animation.INFINITE);
                    netAlarmSingleTaximeter.startAnimation(anim);
                    districtRate = 1;
                    eventsRate = 1;
                    weatherRate = 1;
                }

                final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Resources res = getResources();
                    checkGPSSingleTaximeter.setImageDrawable(res.getDrawable(R.drawable.gpsnotconnected));
                    gpsalarmSingleTaximeter.setText("موقعیت مکانی خود را روشن کنید !");
                    Animation anim2 = new AlphaAnimation(0.0f, 1.0f);
                    anim2.setDuration(50); //You can manage the blinking time with this parameter
                    anim2.setStartOffset(20);
                    anim2.setRepeatMode(Animation.REVERSE);
                    anim2.setRepeatCount(Animation.INFINITE);
                    gpsalarmSingleTaximeter.startAnimation(anim2);
                } else {
                    Resources res = getResources();
                    checkGPSSingleTaximeter.setImageDrawable(res.getDrawable(R.drawable.gpsconnected));
                    gpsalarmSingleTaximeter.setText("");

                }

                if (gpsTracker.canGetLocation()) {
                    if (pointFlag[0] == false) {
                        latitude2 = gpsTracker.getLatitude();
                        longitude2 = gpsTracker.getLongitude();

                        if (seconds == 0) {
                            startLatitude = latitude2;
                            startLongitude = longitude2;
                        }

                        String formattedLatitude2 = String.format("%.5f", latitude2);
                        String formattedLongitude2 = String.format("%.5f", longitude2);
                        tvLatitude.setText("Lat: " + formattedLatitude2);
                        tvLongitude.setText("Lng: " + formattedLongitude2);
                    } else {
                        latitude1 = latitude2;
                        longitude1 = longitude2;
                        String formattedDistanceTraveled = String.format("%.3f", distanceTraveled[0]);
                        distanceTextView.setText(formattedDistanceTraveled + " کیلومتر");
                        latitude2 = gpsTracker.getLatitude();
                        longitude2 = gpsTracker.getLongitude();
                        double distanceNow = distance(latitude2, latitude1, longitude2, longitude1);
                        if (distanceNow > (coefficient[0] * 0.036)) {
                            coefficient[0]++;
                        } else if (distanceNow < (coefficient[0] * 0.036)) {
                            distanceTraveled[0] = distanceNow + distanceTraveled[0];
                            coefficient[0] = 0;
                        } else if (coefficient[0] == 8) {
                            distanceTraveled[0] = distanceNow + distanceTraveled[0];
                            coefficient[0] = 0;
                        }
                        String formattedLatitude2 = String.format("%.5f", latitude2);
                        String formattedLongitude2 = String.format("%.5f", longitude2);
                        tvLatitude.setText("Lat: " + formattedLatitude2);
                        tvLongitude.setText("Lng: " + formattedLongitude2);
                    }
                } else {
                    gpsTracker.showSettingsAlert();
                }
                if (flagStart == true) {
                    hourTextView.setText(Integer.toString(hours));
                    minuteTextView.setText(Integer.toString(minutes));
                    secondTextView.setText(Integer.toString(seconds));
                    String formattedCost = String.format("%.2f", cost);
                    costTextView.setText(formattedCost + " تومان");
                    eventsTextView.setText(Integer.toString(eventsRate));
                    weatherTextView.setText(Integer.toString(weatherRate));
                    districtTextView.setText(Integer.toString(districtRate));
                }
                handler.postDelayed(this, 1000);
            }

        };
        runnable4.run();
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


    private void createPost(int idCode, double lat, double lng) {

        Post post = new Post(idCode, lat, lng, flagStart, false, false, false);

        Call<Post> call = jsonPlaceHolderApi.createLocation(post);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Code: " + response.code());
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


    void startTimer() {
        if (flagStart == false) {
            flagStart = true;
            startTime = System.currentTimeMillis();
            LocalDateTime now = LocalDateTime.now();
            startDate = dateFormatter.format(now);
        } else {
            flagStart = true;
        }
        ImageView image = (ImageView) findViewById(R.id.countdownIcon);
        Resources res = getResources();
        image.setImageDrawable(res.getDrawable(R.drawable.play));
        pointFlag[0] = true;
    }


    void stopTimer() {
        flagStart = false;
        Timer = 0;
        seconds = 0;
        minutes = 0;
        hours = 0;
        elapsed = 0;
        pointFlag[0] = false;
        ImageView image = (ImageView) findViewById(R.id.countdownIcon);
        Resources res = getResources();
        image.setImageDrawable(res.getDrawable(R.drawable.stop));
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
        image.setImageDrawable(res.getDrawable(R.drawable.pause));
    }

    int printCountDown() {
        nowTime = System.currentTimeMillis();
        return (int) (elapsed + System.currentTimeMillis() - startTime);
    }


    public void StartCountdown(View view) {
        startTimer();
        flagStart = true;
    }

    public void PauseCountdown(View view) {
        pauseTimer();
    }

    public void StopCountdown(View view) {
        stopTimer();
    }

    public void finishJourneyClicked(View view) {
        flagStart = false;
        Timer = 0;
        seconds = 0;
        minutes = 0;
        hours = 0;
        elapsed = 0;
        pointFlag[0] = false;

        String formattedLatitude1 = String.format("%.5f", latitude1);
        String formattedLongitude1 = String.format("%.5f", longitude1);
        String formattedLatitude2 = String.format("%.5f", latitude2);
        String formattedLongitude2 = String.format("%.5f", longitude2);
        String formattedDistance = String.format("%.3f", distanceTraveled[0]);
        String formattedCost = String.format("%.3f", cost);

        distanceTraveled[0] = 0;
        cost = 0;

        Intent i3 = new Intent(Single_Taximeter.this, FinishSingleTaximeter.class);
        i3.putExtra("idDriver", String.valueOf(identityCode));
        i3.putExtra("startTime", String.valueOf(startTime));
        i3.putExtra("finishTime", String.valueOf(System.currentTimeMillis()));
        i3.putExtra("srcLat", formattedLatitude1);
        i3.putExtra("srcLng", formattedLongitude1);
        i3.putExtra("desLat", formattedLatitude2);
        i3.putExtra("desLng", formattedLongitude2);
        i3.putExtra("distance", formattedDistance);
        i3.putExtra("cost", formattedCost);
        startActivity(i3);

        LocalDateTime now = LocalDateTime.now();
        finishDate = dateFormatter.format(now);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss");

        Date startTimeDate = new Date(startTime);
        Date nowTimeDate = new Date(nowTime);
        String startTimeStr = simpleDateFormat.format(startTimeDate);
        String nowTimeStr = simpleDateFormat.format(nowTimeDate);

        System.out.println(startDate);
        System.out.println(startTimeStr);
        System.out.println(finishDate);
        System.out.println(nowTimeStr);


        createJourney(identityCode, startDate, startTimeStr, finishDate, nowTimeStr, startLatitude, startLongitude, latitude2, longitude2, distanceTraveled[0], cost);

    }


    private void createJourney(int IdDriver, String StartDate, String StartTime, String FinishDate, String FinishTime, Double SrcLat, Double SrcLng, Double DesLat, Double DesLng, Double Distance, Double Cost) {

        Journey journey = new Journey(IdDriver, StartDate, StartTime, FinishDate, FinishTime, SrcLat, SrcLng, DesLat, DesLng, Distance, Cost, 1);

        Call<Journey> call = jsonPlaceHolderApi.createJourney("application/json", journey);

        call.enqueue(new Callback<Journey>() {
            @Override
            public void onResponse(Call<Journey> call, Response<Journey> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Code: " + response.code());
                    return;
                }

                Journey postResponse = response.body();

                String content = "";
                content = "Code: " + response.code() + "\n";
                content += "ID: " + postResponse.getId() + "\n";
                content += "Start Date and Time: " + postResponse.getStartDate() + " " + postResponse.getStartTime() + "\n";
                content += "Finish Date and Time: " + postResponse.getFinishDate() + " " + postResponse.getFinishTime() + "\n";
                content += "Identity Code: " + postResponse.getIdDriver() + "\n";
                content += "Source Latitude: " + postResponse.getSrcLat() + "\n";
                content += "Source Longitude: " + postResponse.getSrcLng() + "\n\n";
                content += "Destination Longitude: " + postResponse.getDesLat() + "\n";
                content += "Destination Longitude: " + postResponse.getDesLng() + "\n";
                content += "Distance: " + postResponse.getDistance() + "\n";
                content += "Cost: " + postResponse.getCost() + "\n";
                System.out.println(content);

            }

            @Override
            public void onFailure(Call<Journey> call, Throwable t) {

            }
        });
    }

}