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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


@RequiresApi(api = Build.VERSION_CODES.O)
public class Multi_Taximeter extends Activity {

    MediaPlayer netsoundM, gpssoundM, startSoundM, pauseSoundM, stopSoundM, finishSoundM;
    private TextView tvLatitudeMulti, tvLongitudeMulti;
    Boolean[] flagStart = {false, false, false, false};
    long[] startTime = {0, 0, 0, 0};
    long[] nowTime = {System.currentTimeMillis(), System.currentTimeMillis(), System.currentTimeMillis(), System.currentTimeMillis()};
    long[] Timer = {0, 0, 0, 0};
    long[] elapsed = {0, 0, 0, 0};
    double[] latitude1 = {0, 0, 0, 0};
    double[] longitude1 = {0, 0, 0, 0};
    double[] startLatitude = new double[4];
    double[] startLongitude = new double[4];
    double[] latitude2 = {0, 0, 0, 0};
    double[] longitude2 = {0, 0, 0, 0};
    final double[] distanceTraveled = {0, 0, 0, 0};
    final double[] distanceTiming = {0, 0, 0, 0};
    int[] timeWaiting = {0, 0, 0, 0};
    final boolean[] pointFlag = {false, false, false, false};
    int[] hours = {0, 0, 0, 0};
    int[] minutes = {0, 0, 0, 0};
    int[] seconds = {0, 0, 0, 0};
    double[] duration = {0, 0, 0, 0};
    boolean weather = false;
    boolean shift = false;
    double rate = 1;
    String ZoneStringText;
    String typeStringText;
    int initionalRate;
    int distanceRate;
    int timeRate;
    double[] cost = {0, 0, 0, 0};
    String[] startDate = {"", "", ""};
    String[] finishDate = {"", "", ""};
    JsonPlaceHolderApi jsonPlaceHolderApi;
    boolean[] isStart = {false, false, false, false};
    double[] distanceNow = {0, 0, 0, 0};
    LinearLayout PassOneRegion;
    LinearLayout PassTwoRegion;
    LinearLayout PassThreeRegion;
    LinearLayout PassFourRegion;


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


    boolean doubleBackToExitPressedOnce = false;

    private void createPost(int idCode, double lat, double lng) {

        boolean isWorkingPassOne = false;
        boolean isWorkingPassTwo = false;
        boolean isWorkingPassThree = false;
        boolean isWorkingPassFour = false;

        if (flagStart[0] == true) {
            isWorkingPassOne = true;
        }
        if (flagStart[1] == true) {
            isWorkingPassTwo = true;
        }
        if (flagStart[2] == true) {
            isWorkingPassThree = true;
        }
        if (flagStart[3] == true) {
            isWorkingPassFour = true;
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
        post = null;
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

    int printFourCountDown() {
        nowTime[3] = System.currentTimeMillis();
        return (int) (elapsed[3] + System.currentTimeMillis() - startTime[3]);
    }


    public void finishJourneyOne() {
        finishSoundM = MediaPlayer.create(Multi_Taximeter.this, R.raw.finish_journey);
        finishSoundM.start();
        flagStart[0] = false;
        Timer[0] = 0;
        seconds[0] = 0;
        minutes[0] = 0;
        hours[0] = 0;
        elapsed[0] = 0;
        pointFlag[0] = false;
        PassOneRegion.setBackgroundColor(Color.rgb(26, 35, 126));
        String formattedLatitude1 = String.format("%.5f", latitude1[0]);
        String formattedLongitude1 = String.format("%.5f", longitude1[0]);
        String formattedLatitude2 = String.format("%.5f", latitude2[0]);
        String formattedLongitude2 = String.format("%.5f", longitude2[0]);
        String formattedDistance = String.format("%.3f", distanceTraveled[0]);
        String formattedCost = String.format("%.3f", cost[0]);

        distanceTraveled[0] = 0;
        cost[0] = initionalRate / 10;

        Intent i3 = new Intent(Multi_Taximeter.this, FinishSingleTaximeter.class);
        i3.putExtra("idDriver", String.valueOf(identityCode));
        i3.putExtra("startTime", String.valueOf(startTime[0]));
        i3.putExtra("finishTime", String.valueOf(System.currentTimeMillis()));
        i3.putExtra("srcLat", formattedLatitude1);
        i3.putExtra("srcLng", formattedLongitude1);
        i3.putExtra("desLat", formattedLatitude2);
        i3.putExtra("desLng", formattedLongitude2);
        i3.putExtra("distance", formattedDistance);
        i3.putExtra("psgNum", "1");
        i3.putExtra("cost", formattedCost);
        startActivity(i3);

    }


    public void finishJourneyTwo() {
        finishSoundM = MediaPlayer.create(Multi_Taximeter.this, R.raw.finish_journey);
        finishSoundM.start();
        flagStart[1] = false;
        Timer[1] = 0;
        seconds[1] = 0;
        minutes[1] = 0;
        hours[1] = 0;
        elapsed[1] = 0;
        pointFlag[1] = false;
        PassTwoRegion.setBackgroundColor(Color.rgb(26, 35, 126));
        String formattedLatitude1 = String.format("%.5f", latitude1[1]);
        String formattedLongitude1 = String.format("%.5f", longitude1[1]);
        String formattedLatitude2 = String.format("%.5f", latitude2[1]);
        String formattedLongitude2 = String.format("%.5f", longitude2[1]);
        String formattedDistance = String.format("%.3f", distanceTraveled[1]);
        String formattedCost = String.format("%.3f", cost[1]);

        distanceTraveled[1] = 0;
        cost[1] = initionalRate / 10;

        Intent i3 = new Intent(Multi_Taximeter.this, FinishSingleTaximeter.class);
        i3.putExtra("idDriver", String.valueOf(identityCode));
        i3.putExtra("startTime", String.valueOf(startTime[1]));
        i3.putExtra("finishTime", String.valueOf(System.currentTimeMillis()));
        i3.putExtra("srcLat", formattedLatitude1);
        i3.putExtra("srcLng", formattedLongitude1);
        i3.putExtra("desLat", formattedLatitude2);
        i3.putExtra("desLng", formattedLongitude2);
        i3.putExtra("distance", formattedDistance);
        i3.putExtra("psgNum", "2");
        i3.putExtra("cost", formattedCost);
        startActivity(i3);

    }


    public void finishJourneyThree() {
        finishSoundM = MediaPlayer.create(Multi_Taximeter.this, R.raw.finish_journey);
        finishSoundM.start();
        flagStart[2] = false;
        Timer[2] = 0;
        seconds[2] = 0;
        minutes[2] = 0;
        hours[2] = 0;
        elapsed[2] = 0;
        pointFlag[2] = false;
        PassThreeRegion.setBackgroundColor(Color.rgb(26, 35, 126));
        String formattedLatitude1 = String.format("%.5f", latitude1[2]);
        String formattedLongitude1 = String.format("%.5f", longitude1[2]);
        String formattedLatitude2 = String.format("%.5f", latitude2[2]);
        String formattedLongitude2 = String.format("%.5f", longitude2[2]);
        String formattedDistance = String.format("%.3f", distanceTraveled[2]);
        String formattedCost = String.format("%.3f", cost[2]);

        distanceTraveled[2] = 0;
        cost[2] = initionalRate / 10;

        Intent i3 = new Intent(Multi_Taximeter.this, FinishSingleTaximeter.class);
        i3.putExtra("idDriver", String.valueOf(identityCode));
        i3.putExtra("startTime", String.valueOf(startTime[2]));
        i3.putExtra("finishTime", String.valueOf(System.currentTimeMillis()));
        i3.putExtra("srcLat", formattedLatitude1);
        i3.putExtra("srcLng", formattedLongitude1);
        i3.putExtra("desLat", formattedLatitude2);
        i3.putExtra("desLng", formattedLongitude2);
        i3.putExtra("distance", formattedDistance);
        i3.putExtra("psgNum", "3");
        i3.putExtra("cost", formattedCost);
        startActivity(i3);

    }


    public void finishJourneyFour() {
        finishSoundM = MediaPlayer.create(Multi_Taximeter.this, R.raw.finish_journey);
        finishSoundM.start();
        flagStart[3] = false;
        Timer[3] = 0;
        seconds[3] = 0;
        minutes[3] = 0;
        hours[3] = 0;
        elapsed[3] = 0;
        pointFlag[3] = false;
        PassFourRegion.setBackgroundColor(Color.rgb(26, 35, 126));
        String formattedLatitude1 = String.format("%.5f", latitude1[3]);
        String formattedLongitude1 = String.format("%.5f", longitude1[3]);
        String formattedLatitude2 = String.format("%.5f", latitude2[3]);
        String formattedLongitude2 = String.format("%.5f", longitude2[3]);
        String formattedDistance = String.format("%.3f", distanceTraveled[3]);
        String formattedCost = String.format("%.3f", cost[3]);

        distanceTraveled[3] = 0;
        cost[3] = initionalRate / 10;

        Intent i3 = new Intent(Multi_Taximeter.this, FinishSingleTaximeter.class);
        i3.putExtra("idDriver", String.valueOf(identityCode));
        i3.putExtra("startTime", String.valueOf(startTime[3]));
        i3.putExtra("finishTime", String.valueOf(System.currentTimeMillis()));
        i3.putExtra("srcLat", formattedLatitude1);
        i3.putExtra("srcLng", formattedLongitude1);
        i3.putExtra("desLat", formattedLatitude2);
        i3.putExtra("desLng", formattedLongitude2);
        i3.putExtra("distance", formattedDistance);
        i3.putExtra("psgNum", "4");
        i3.putExtra("cost", formattedCost);
        startActivity(i3);

    }


    void pauseOneTimer() {
        pauseSoundM = MediaPlayer.create(Multi_Taximeter.this, R.raw.pause_journey);
        pauseSoundM.start();
        if (flagStart[0] == true) {
            Timer[0] = System.currentTimeMillis() - startTime[0];
            elapsed[0] = Timer[0] + elapsed[0];
            Timer[0] = 0;
            flagStart[0] = false;
            pointFlag[0] = false;
        }
    }

    void pauseTwoTimer() {
        pauseSoundM = MediaPlayer.create(Multi_Taximeter.this, R.raw.pause_journey);
        pauseSoundM.start();
        if (flagStart[1] == true) {
            Timer[1] = System.currentTimeMillis() - startTime[1];
            elapsed[1] = Timer[1] + elapsed[1];
            Timer[1] = 0;
            flagStart[1] = false;
            pointFlag[1] = false;
        }
    }

    void pauseThreeTimer() {
        pauseSoundM = MediaPlayer.create(Multi_Taximeter.this, R.raw.pause_journey);
        pauseSoundM.start();
        if (flagStart[2] == true) {
            Timer[2] = System.currentTimeMillis() - startTime[2];
            elapsed[2] = Timer[2] + elapsed[2];
            Timer[2] = 0;
            flagStart[2] = false;
            pointFlag[2] = false;
        }
    }


    void pauseFourTimer() {
        pauseSoundM = MediaPlayer.create(Multi_Taximeter.this, R.raw.pause_journey);
        pauseSoundM.start();
        if (flagStart[3] == true) {
            Timer[3] = System.currentTimeMillis() - startTime[3];
            elapsed[3] = Timer[3] + elapsed[3];
            Timer[3] = 0;
            flagStart[3] = false;
            pointFlag[3] = false;
        }
    }


    void stopOneTimer() {
        stopSoundM = MediaPlayer.create(Multi_Taximeter.this, R.raw.stop_journey);
        stopSoundM.start();
        flagStart[0] = false;
        Timer[0] = 0;
        seconds[0] = 0;
        minutes[0] = 0;
        hours[0] = 0;
        elapsed[0] = 0;
        pointFlag[0] = false;
        distanceTraveled[0] = 0;
        cost[0] = initionalRate / 10;
    }

    void stopTwoTimer() {
        stopSoundM = MediaPlayer.create(Multi_Taximeter.this, R.raw.stop_journey);
        stopSoundM.start();
        flagStart[1] = false;
        Timer[1] = 0;
        seconds[1] = 0;
        minutes[1] = 0;
        hours[1] = 0;
        elapsed[1] = 0;
        pointFlag[1] = false;
        distanceTraveled[1] = 0;
        cost[1] = initionalRate / 10;
    }

    void stopThreeTimer() {
        stopSoundM = MediaPlayer.create(Multi_Taximeter.this, R.raw.stop_journey);
        stopSoundM.start();
        flagStart[2] = false;
        Timer[2] = 0;
        seconds[2] = 0;
        minutes[2] = 0;
        hours[2] = 0;
        elapsed[2] = 0;
        pointFlag[2] = false;
        distanceTraveled[2] = 0;
        cost[2] = initionalRate / 10;

    }

    void stopFourTimer() {
        stopSoundM = MediaPlayer.create(Multi_Taximeter.this, R.raw.stop_journey);
        stopSoundM.start();
        flagStart[3] = false;
        Timer[3] = 0;
        seconds[3] = 0;
        minutes[3] = 0;
        hours[3] = 0;
        elapsed[3] = 0;
        pointFlag[3] = false;
        distanceTraveled[3] = 0;
        cost[3] = initionalRate / 10;
    }


    void startOneTimer() {
        startSoundM = MediaPlayer.create(Multi_Taximeter.this, R.raw.start_journey);
        startSoundM.start();
        if (flagStart[0] == false) {
            flagStart[0] = true;
            startTime[0] = System.currentTimeMillis();
            createJourney(identityCode, latitude2[0], longitude2[0], distanceTraveled[0], cost[0], 1, true, false);
        } else {
            flagStart[0] = true;
        }
        pointFlag[0] = true;
    }

    void startTwoTimer() {
        startSoundM = MediaPlayer.create(Multi_Taximeter.this, R.raw.start_journey);
        startSoundM.start();
        if (flagStart[1] == false) {
            flagStart[1] = true;
            startTime[1] = System.currentTimeMillis();
            createJourney(identityCode, latitude2[0], longitude2[0], distanceTraveled[0], cost[0], 2, true, false);
        } else {
            flagStart[1] = true;
        }
        pointFlag[1] = true;
    }

    void startThreeTimer() {
        startSoundM = MediaPlayer.create(Multi_Taximeter.this, R.raw.start_journey);
        startSoundM.start();
        if (flagStart[2] == false) {
            flagStart[2] = true;
            startTime[2] = System.currentTimeMillis();
            createJourney(identityCode, latitude2[0], longitude2[0], distanceTraveled[0], cost[0], 3, true, false);
        } else {
            flagStart[2] = true;
        }
        pointFlag[2] = true;
    }

    void startFourTimer() {
        startSoundM = MediaPlayer.create(Multi_Taximeter.this, R.raw.start_journey);
        startSoundM.start();
        if (flagStart[3] == false) {
            flagStart[3] = true;
            startTime[3] = System.currentTimeMillis();
            createJourney(identityCode, latitude2[0], longitude2[0], distanceTraveled[0], cost[0], 4, true, false);
        } else {
            flagStart[3] = true;
        }
        pointFlag[3] = true;
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
                    if (staticRate.getZone() == 1 && (typeString.equals(String.valueOf(staticRate.getType()))) && (carString.equals(String.valueOf(staticRate.getVehicle())))) {

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

    private void createJourney(int IdDriver, double Lat, double Lng, double Distance, double Cost, int psgNum, boolean IsStart, boolean IsFinish) {

        Journey journey = new Journey(IdDriver, Lat, Lng, Distance, Cost, psgNum, IsStart, IsFinish, true, false);

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
            zoneString = extras.getString("zone");
            typeString = extras.getString("type");
            carString = extras.getString("car");
            pelak = extras.getString("pelak");
            color = extras.getString("color");
        }


        final int[] coefficient = {0, 0, 0, 0};
        tvLatitudeMulti = (TextView) findViewById(R.id.latitudeMultiTaximeter);
        tvLongitudeMulti = (TextView) findViewById(R.id.longitudeMultimeter);
        TextView DstOnePas = findViewById(R.id.dstOnePas);
        TextView DstTwoPas = findViewById(R.id.dstTwoPas);
        TextView DstThreePas = findViewById(R.id.dstThreePas);
        TextView DstFourPas = findViewById(R.id.dstFourPas);

        TextView MinuteOnePass = findViewById(R.id.minuteOnePass);
        TextView MinuteTwoPass = findViewById(R.id.minuteTwoPass);
        TextView MinuteThreePass = findViewById(R.id.minuteThreePass);
        TextView MinuteFourPass = findViewById(R.id.minuteFourPass);
        TextView SecOnePass = findViewById(R.id.secOnePass);
        TextView SecTwoPass = findViewById(R.id.secTwoPass);
        TextView SecThreePass = findViewById(R.id.secThreePass);
        TextView SecFourPass = findViewById(R.id.secFourPass);


        CheckBox WeatherCheckBoxMultimeter = findViewById(R.id.weatherCheckMultimeter);
        CheckBox ShiftCheckBoxMultimeter = findViewById(R.id.shiftCheckMultimeter);

        TextView CostOnePass = findViewById(R.id.costOnePass);
        TextView CostTwoPass = findViewById(R.id.costTwoPass);
        TextView CostThreePass = findViewById(R.id.costThreePass);
        TextView CostFourPass = findViewById(R.id.costFourPass);

        PassOneRegion = findViewById(R.id.passOneRegion);
        PassTwoRegion = findViewById(R.id.passTwoRegion);
        PassThreeRegion = findViewById(R.id.passThreeRegion);
        PassFourRegion = findViewById(R.id.passFourRegion);

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
                ImageView checkGPSMultiTaximeter = (ImageView) findViewById(R.id.gpsCheckIconMultitTaximeter);

                final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Resources res = getResources();
                    checkGPSMultiTaximeter.setImageDrawable(res.getDrawable(R.drawable.gpsnotconnectedicon));
                    gpsAlarmMultiTaximeter.setText("موقعیت مکانی خود را روشن کنید !");
                    Animation anim2 = new AlphaAnimation(0.0f, 1.0f);
                    anim2.setDuration(50); //You can manage the blinking time with this parameter
                    anim2.setStartOffset(20);
                    anim2.setRepeatMode(Animation.REVERSE);
                    anim2.setRepeatCount(Animation.INFINITE);
                    gpsAlarmMultiTaximeter.startAnimation(anim2);
                } else {
                    Resources res = getResources();
                    checkGPSMultiTaximeter.setImageDrawable(res.getDrawable(R.drawable.gpsconnectedicon));
                    gpsAlarmMultiTaximeter.setText("");
                }

                // ------------------------------------------------------------- //

                if (gpsTracker.canGetLocation()) {
                    if (pointFlag[0] == false) {
                        latitude2[0] = gpsTracker.getLatitude();
                        longitude2[0] = gpsTracker.getLongitude();

                        if (seconds[0] == 0) {
                            startLatitude[0] = latitude2[0];
                            startLongitude[0] = longitude2[0];
                        }

                    } else {
                        latitude1[0] = latitude2[0];
                        longitude1[0] = longitude2[0];
                        String formattedDistanceOneTraveled = String.format("%.3f", distanceTraveled[0]);
                        DstOnePas.setText(formattedDistanceOneTraveled + " کیلومتر");
                        latitude2[0] = gpsTracker.getLatitude();
                        longitude2[0] = gpsTracker.getLongitude();
                        distanceNow[0] = distance(latitude2[0], latitude1[0], longitude2[0], longitude1[0]);
                        timeWaiting[0] += 2;

                        if (distanceNow[0] > (coefficient[0] * 0.120)) {
                            coefficient[0]++;
                        } else if (distanceNow[0] < (coefficient[0] * 0.120)) {
                            distanceTraveled[0] = distanceNow[0] + distanceTraveled[0];
                            coefficient[0] = 0;
                        } else if (coefficient[0] == 8) {
                            distanceTraveled[0] = distanceNow[0] + distanceTraveled[0];
                            coefficient[0] = 0;
                        }

                        if (weather == false && shift == false) {
                            cost[0] += (distanceNow[0] * 1000 * (distanceRate / 200) / 10);
                        } else if (shift == true && weather == false) {
                            cost[0] += (distanceNow[0] * 1000 * (distanceRate / 200) / 10) + ((distanceNow[0] * 1000 * (distanceRate / 200) / 10) * 0.2);
                        } else if (shift == false && weather == true) {
                            cost[0] += (distanceNow[0] * 1000 * (distanceRate / 200) / 10) + ((distanceNow[0] * 1000 * (distanceRate / 200) / 10) * 0.2);
                        } else {
                            cost[0] += (distanceNow[0] * 1000 * (distanceRate / 200) / 10) + ((distanceNow[0] * 1000 * (distanceRate / 200) / 10) * 0.4);
                        }
                        distanceTraveled[0] = distanceNow[0] + distanceTraveled[0];
                        distanceTiming[0] += distanceNow[0];
                        if (timeWaiting[0] >= 15) {
                            if (distanceTiming[0] < 0.075) {
                                if (weather == false && shift == false) {
                                    cost[0] += 15 * timeRate / 100;
                                } else if (shift == true && weather == false) {
                                    cost[0] += (15 * timeRate / 100) + ((15 * timeRate / 100) * 0.2);
                                } else if (shift == false && weather == true) {
                                    cost[0] += (15 * timeRate / 100) + ((15 * timeRate / 100) * 0.2);
                                } else {
                                    cost[0] += (15 * timeRate / 100) + ((15 * timeRate / 100) * 0.4);
                                }
                                distanceTiming[0] = 0;
                                timeWaiting[0] = 0;
                            } else {
                                timeWaiting[0] = 0;
                                distanceTiming[0] = 0;
                            }
                        }


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
                        timeWaiting[1] += 2;
                        if (distanceNow[1] > (coefficient[1] * 0.120)) {
                            coefficient[1]++;
                        } else if (distanceNow[1] < (coefficient[1] * 0.120)) {
                            distanceTraveled[1] = distanceNow[1] + distanceTraveled[1];
                            coefficient[1] = 0;
                        } else if (coefficient[1] == 8) {
                            distanceTraveled[1] = distanceNow[1] + distanceTraveled[1];
                            coefficient[1] = 0;
                        }

                        if (weather == false && shift == false) {
                            cost[1] += (distanceNow[1] * 1000 * (distanceRate / 200) / 10);
                        } else if (shift == true && weather == false) {
                            cost[1] += (distanceNow[1] * 1000 * (distanceRate / 200) / 10) + ((distanceNow[1] * 1000 * (distanceRate / 200) / 10) * 0.2);
                        } else if (shift == false && weather == true) {
                            cost[1] += (distanceNow[1] * 1000 * (distanceRate / 200) / 10) + ((distanceNow[1] * 1000 * (distanceRate / 200) / 10) * 0.2);
                        } else {
                            cost[1] += (distanceNow[1] * 1000 * (distanceRate / 200) / 10) + ((distanceNow[1] * 1000 * (distanceRate / 200) / 10) * 0.4);
                        }
                        distanceTraveled[1] = distanceNow[1] + distanceTraveled[1];
                        distanceTiming[1] += distanceNow[1];
                        if (timeWaiting[1] >= 15) {
                            if (distanceTiming[1] < 0.075) {
                                if (weather == false && shift == false) {
                                    cost[1] += 15 * timeRate / 100;
                                } else if (shift == true && weather == false) {
                                    cost[1] += (15 * timeRate / 100) + ((15 * timeRate / 100) * 0.2);
                                } else if (shift == false && weather == true) {
                                    cost[1] += (15 * timeRate / 100) + ((15 * timeRate / 100) * 0.2);
                                } else {
                                    cost[1] += (15 * timeRate / 100) + ((15 * timeRate / 100) * 0.4);
                                }
                                distanceTiming[1] = 0;
                                timeWaiting[1] = 0;
                            } else {
                                timeWaiting[1] = 0;
                                distanceTiming[1] = 0;
                            }
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
                        timeWaiting[2] += 2;
                        if (distanceNow[2] > (coefficient[2] * 0.120)) {
                            coefficient[2]++;
                        } else if (distanceNow[2] < (coefficient[2] * 0.120)) {
                            distanceTraveled[2] = distanceNow[2] + distanceTraveled[2];
                            coefficient[2] = 0;
                        } else if (coefficient[2] == 8) {
                            distanceTraveled[2] = distanceNow[2] + distanceTraveled[2];
                            coefficient[2] = 0;
                        }

                        if (weather == false && shift == false) {
                            cost[2] += (distanceNow[2] * 1000 * (distanceRate / 200) / 10);
                        } else if (shift == true && weather == false) {
                            cost[2] += (distanceNow[2] * 1000 * (distanceRate / 200) / 10) + ((distanceNow[2] * 1000 * (distanceRate / 200) / 10) * 0.2);
                        } else if (shift == false && weather == true) {
                            cost[2] += (distanceNow[2] * 1000 * (distanceRate / 200) / 10) + ((distanceNow[2] * 1000 * (distanceRate / 200) / 10) * 0.2);
                        } else {
                            cost[2] += (distanceNow[2] * 1000 * (distanceRate / 200) / 10) + ((distanceNow[2] * 1000 * (distanceRate / 200) / 10) * 0.4);
                        }
                        distanceTraveled[2] = distanceNow[2] + distanceTraveled[2];
                        distanceTiming[2] += distanceNow[2];
                        if (timeWaiting[2] >= 15) {
                            if (distanceTiming[2] < 0.075) {
                                if (weather == false && shift == false) {
                                    cost[2] += 15 * timeRate / 100;
                                } else if (shift == true && weather == false) {
                                    cost[2] += (15 * timeRate / 100) + ((15 * timeRate / 100) * 0.2);
                                } else if (shift == false && weather == true) {
                                    cost[2] += (15 * timeRate / 100) + ((15 * timeRate / 100) * 0.2);
                                } else {
                                    cost[2] += (15 * timeRate / 100) + ((15 * timeRate / 100) * 0.4);
                                }
                                distanceTiming[2] = 0;
                                timeWaiting[2] = 0;
                            } else {
                                timeWaiting[2] = 0;
                                distanceTiming[2] = 0;
                            }
                        }
                    }


                    // ------------------------------------------- //

                    if (pointFlag[3] == false) {
                        latitude2[3] = gpsTracker.getLatitude();
                        longitude2[3] = gpsTracker.getLongitude();

                        if (seconds[3] == 0) {
                            startLatitude[3] = latitude2[3];
                            startLongitude[3] = longitude2[3];
                        }
                    } else {
                        latitude1[3] = latitude2[3];
                        longitude1[3] = longitude2[3];
                        String formattedDistanceFourTraveled = String.format("%.3f", distanceTraveled[3]);
                        DstFourPas.setText(formattedDistanceFourTraveled + " کیلومتر");
                        latitude2[3] = gpsTracker.getLatitude();
                        longitude2[3] = gpsTracker.getLongitude();
                        distanceNow[3] = distance(latitude2[3], latitude1[3], longitude2[3], longitude1[3]);
                        timeWaiting[3] += 2;
                        if (distanceNow[3] > (coefficient[3] * 0.120)) {
                            coefficient[3]++;
                        } else if (distanceNow[3] < (coefficient[3] * 0.120)) {
                            distanceTraveled[3] = distanceNow[3] + distanceTraveled[3];
                            coefficient[3] = 0;
                        } else if (coefficient[3] == 8) {
                            distanceTraveled[3] = distanceNow[3] + distanceTraveled[3];
                            coefficient[3] = 0;
                        }

                        if (weather == false && shift == false) {
                            cost[3] += (distanceNow[3] * 1000 * (distanceRate / 200) / 10);
                        } else if (shift == true && weather == false) {
                            cost[3] += (distanceNow[3] * 1000 * (distanceRate / 200) / 10) + ((distanceNow[3] * 1000 * (distanceRate / 200) / 10) * 0.2);
                        } else if (shift == false && weather == true) {
                            cost[3] += (distanceNow[3] * 1000 * (distanceRate / 200) / 10) + ((distanceNow[3] * 1000 * (distanceRate / 200) / 10) * 0.2);
                        } else {
                            cost[3] += (distanceNow[3] * 1000 * (distanceRate / 200) / 10) + ((distanceNow[3] * 1000 * (distanceRate / 200) / 10) * 0.4);
                        }
                        distanceTraveled[3] = distanceNow[3] + distanceTraveled[3];
                        distanceTiming[3] += distanceNow[3];
                        if (timeWaiting[3] >= 15) {
                            if (distanceTiming[3] < 0.075) {
                                if (weather == false && shift == false) {
                                    cost[3] += 15 * timeRate / 100;
                                } else if (shift == true && weather == false) {
                                    cost[3] += (15 * timeRate / 100) + ((15 * timeRate / 100) * 0.2);
                                } else if (shift == false && weather == true) {
                                    cost[3] += (15 * timeRate / 100) + ((15 * timeRate / 100) * 0.2);
                                } else {
                                    cost[3] += (15 * timeRate / 100) + ((15 * timeRate / 100) * 0.4);
                                }
                                distanceTiming[3] = 0;
                                timeWaiting[3] = 0;
                            } else {
                                timeWaiting[3] = 0;
                                distanceTiming[3] = 0;
                            }
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

                if (flagStart[3] == true) {
                    MinuteFourPass.setText(Integer.toString(minutes[3]));
                    SecFourPass.setText(Integer.toString(seconds[3]));
                    String formattedCost = String.format("%.2f", cost[3]);
                    CostFourPass.setText(formattedCost + " تومان");
                }


                handler.postDelayed(this, 2000);
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
                }


                duration[1] = printTwoCountDown() / 1000;
                if (flagStart[1] == true) {
                    hours[1] = (int) duration[1] / 3600;
                    minutes[1] = (int) (duration[1] % 3600) / 60;
                    seconds[1] = (int) (duration[1] % 60);
                }


                duration[2] = printThreeCountDown() / 1000;
                if (flagStart[2] == true) {
                    hours[2] = (int) duration[2] / 3600;
                    minutes[2] = (int) (duration[2] % 3600) / 60;
                    seconds[2] = (int) (duration[2] % 60);
                }


                duration[3] = printFourCountDown() / 1000;
                if (flagStart[3] == true) {
                    hours[3] = (int) duration[3] / 3600;
                    minutes[3] = (int) (duration[3] % 3600) / 60;
                    seconds[3] = (int) (duration[3] % 60);
                }


                handler.postDelayed(this, 1000);
            }

        };
        runnable2.run();

        final boolean[] flagInitialRate = {false};


        Runnable runnable3 = new Runnable() {
            public void run() {

                ImageView imageCheckNetMultiTaximeter = (ImageView) findViewById(R.id.netCheckIconMultitTaximeter);

                if (isNetworkConnected()) {
                    Resources res = getResources();
                    imageCheckNetMultiTaximeter.setImageDrawable(res.getDrawable(R.drawable.netconnectedicon));
                    netAlarmMultiTaximeter.setText("");
                    getSRate();
                    getVRate();

                    if (initionalRate != 0 && flagInitialRate[0] == false) {
                        cost[0] = initionalRate / 10;
                        cost[1] = initionalRate / 10;
                        cost[2] = initionalRate / 10;
                        cost[3] = initionalRate / 10;
                        flagInitialRate[0] = true;
                    }

                } else {
                    Resources res = getResources();
                    imageCheckNetMultiTaximeter.setImageDrawable(res.getDrawable(R.drawable.netnotconnectedicon));
                    netAlarmMultiTaximeter.setText("اینترنت تلفن همراه خود را روشن کنید !");
                    Animation anim = new AlphaAnimation(0.0f, 1.0f);
                    anim.setDuration(1000); //You can manage the blinking time with this parameter
                    anim.setStartOffset(40);
                    anim.setRepeatMode(Animation.REVERSE);
                    anim.setRepeatCount(Animation.INFINITE);
                    netAlarmMultiTaximeter.startAnimation(anim);
                    netsoundM = MediaPlayer.create(Multi_Taximeter.this, R.raw.net_alert);
                    netsoundM.start();
                }

                GpsTracker gpsTracker = new GpsTracker(Multi_Taximeter.this);
                if (gpsTracker.canGetLocation()) {
                    createPost(identityCode, latitude2[0], longitude2[0]);
                    // createPost(925485640, latitude2, longitude2, "2022-08-24T15:27:47Z");
                } else {
                    gpsTracker.showSettingsAlert();
                }


                if (weather == true) {
                    WeatherCheckBoxMultimeter.setChecked(true);
                } else {
                    WeatherCheckBoxMultimeter.setChecked(false);
                }
                if (shift == true) {
                    ShiftCheckBoxMultimeter.setChecked(true);
                } else {
                    ShiftCheckBoxMultimeter.setChecked(false);
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
                                PassOneRegion.setBackgroundColor(Color.rgb(0, 96, 100));
                            } else if (isStart[0] == true) {
                                isStart[0] = false;
                                finishJourneyOne();
                                stopOneTimer();
                                Toast.makeText(getApplicationContext(), "سفر مسافر 1 به اتمام رسید !", Toast.LENGTH_SHORT).show();
                                PassOneRegion.setBackgroundColor(Color.rgb(1, 87, 155));
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
                                PassTwoRegion.setBackgroundColor(Color.rgb(0, 96, 100));
                            } else if (isStart[1] == true) {
                                isStart[1] = false;
                                finishJourneyTwo();
                                stopTwoTimer();
                                Toast.makeText(getApplicationContext(), "تایمر مسافر 2 متوقف شد", Toast.LENGTH_SHORT).show();
                                PassTwoRegion.setBackgroundColor(Color.rgb(1, 87, 155));
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
                                PassThreeRegion.setBackgroundColor(Color.rgb(0, 96, 100));
                            } else if (isStart[2] == true) {
                                isStart[2] = false;
                                finishJourneyThree();
                                stopThreeTimer();
                                Toast.makeText(getApplicationContext(), "تایمر مسافر 3 متوقف شد", Toast.LENGTH_SHORT).show();
                                PassThreeRegion.setBackgroundColor(Color.rgb(1, 87, 155));
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


        PassFourRegion.setOnTouchListener(new View.OnTouchListener() {
            Handler handler4 = new Handler();

            int numberOfTaps4 = 0;
            long lastTapTimeMs4 = 0;
            long touchDownMs4 = 0;

            @Override
            public boolean onTouch(View v4, MotionEvent event4) {
                switch (event4.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        touchDownMs4 = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                        handler4.removeCallbacksAndMessages(null);

                        if ((System.currentTimeMillis() - touchDownMs4) > ViewConfiguration.getTapTimeout()) {
                            //it was not a tap
                            numberOfTaps4 = 0;
                            lastTapTimeMs4 = 0;
                            break;
                        }

                        if (numberOfTaps4 > 0
                                && (System.currentTimeMillis() - lastTapTimeMs4) < ViewConfiguration.getDoubleTapTimeout()) {
                            numberOfTaps4 += 1;
                        } else {
                            numberOfTaps4 = 1;
                            if (isStart[3] == false) {
                                isStart[3] = true;
                                startFourTimer();
                                Toast.makeText(getApplicationContext(), "تایمر مسافر 4 شروع به کار کرد", Toast.LENGTH_SHORT).show();
                                //CountdownFourIcon.setImageDrawable(res4.getDrawable(R.drawable.play));
                                PassFourRegion.setBackgroundColor(Color.rgb(0, 96, 100));
                            } else if (isStart[3] == true) {
                                isStart[3] = false;
                                finishJourneyFour();
                                stopFourTimer();
                                Toast.makeText(getApplicationContext(), "تایمر مسافر 4 متوقف شد", Toast.LENGTH_SHORT).show();
                                PassFourRegion.setBackgroundColor(Color.rgb(1, 87, 155));
                                //CountdownFourIcon.setImageDrawable(res4.getDrawable(R.drawable.stop));
                            }
                        }

                        lastTapTimeMs4 = System.currentTimeMillis();

                        if (numberOfTaps4 == 3) {

                            //handle triple tap
                        } else if (numberOfTaps4 == 2) {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //handle double tap
                                    pauseFourTimer();
                                    Toast.makeText(getApplicationContext(), "سفر مسافر 4 به اتمام رسید !", Toast.LENGTH_SHORT).show();
                                    //CountdownFourIcon.setImageDrawable(res2.getDrawable(R.drawable.stop));
                                    isStart[3] = false;
                                }
                            }, ViewConfiguration.getDoubleTapTimeout());
                        }
                }

                return true;
            }

        });

    }


}