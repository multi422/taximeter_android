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
public class Contractual_Taximeter extends Activity {

    MediaPlayer netsound, gpssound, startSound, pauseSound, stopSound, finishSound;
    Button StartTimerButton;
    LinearLayout ManPlace;
    LinearLayout WomanPlace;
    private TextView tvLatitude = null, tvLongitude = null;
    TableLayout ContractualTaximeterScreen = null;
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
        Toast.makeText(this, "برای خروج از صفحه تاکسی متر قراردادی دوباره دکمه بازگشت را بزنید", Toast.LENGTH_SHORT).show();

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
        setContentView(R.layout.contractual_taximeter);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        TextView gpsalarmContractualTaximeter = findViewById(R.id.gpsalarmContractualTaximeterText);
        TextView netAlarmContractualTaximeter = findViewById(R.id.netAlarmContractualTaximeterText);
        ContractualTaximeterScreen = findViewById(R.id.contractual_taximeter_screen);

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


        TextView secondTextView = findViewById(R.id.secondCountdownContractual);
        TextView minuteTextView = findViewById(R.id.minuteCountdownContractual);
        TextView hourTextView = findViewById(R.id.hourCountdownContractual);

        CheckBox WeatherCheckBox = findViewById(R.id.weatherCheckContractual);
        CheckBox ShiftCheckBox = findViewById(R.id.shiftCheckContractual);

        ManPlace = (LinearLayout) findViewById(R.id.manPlaceContractual);
        WomanPlace = (LinearLayout) findViewById(R.id.womanPlaceContractual);
        StartTimerButton = (Button) findViewById(R.id.startTimerButtonContractual);

        TextView costTextView = findViewById(R.id.costTextContractual);

        ImageView image = (ImageView) findViewById(R.id.countdownIconContractual);
        Resources res = getResources();
        image.setImageDrawable(res.getDrawable(R.drawable.stopicon));
        final Handler handler = new Handler();

        final boolean[] flagInitialRate = {false};

        Runnable runnable = new Runnable() {
            public void run() {

                ImageView checkNetContractualTaximeter = (ImageView) findViewById(R.id.checkInternetConnectionContractualMultimeter);

                GpsTracker gpsTracker = new GpsTracker(Contractual_Taximeter.this);
                if (gpsTracker.canGetLocation()) {

                    createPost(identityCode, latitude2, longitude2);
                    // createPost(925485640, latitude2, longitude2, "2022-08-24T15:27:47Z");
                } else {
                    gpsTracker.showSettingsAlert();
                }

                if (isNetworkConnected()) {
                    Resources res = getResources();
                    checkNetContractualTaximeter.setImageDrawable(res.getDrawable(R.drawable.netconnectedicon));
                    netAlarmContractualTaximeter.setText("");
                    getVRate();
                    getSRate();

                    if (initionalRate != 0 && flagInitialRate[0] == false) {
                        cost = initionalRate / 10;
                        flagInitialRate[0] = true;
                    }

                } else {
                    Resources res = getResources();
                    checkNetContractualTaximeter.setImageDrawable(res.getDrawable(R.drawable.netnotconnectedicon));
                    netAlarmContractualTaximeter.setText("اینترنت تلفن همراه خود را روشن کنید !");
                    Animation anim = new AlphaAnimation(0.0f, 1.0f);
                    anim.setDuration(1000); //You can manage the blinking time with this parameter
                    anim.setStartOffset(40);
                    anim.setRepeatMode(Animation.REVERSE);
                    anim.setRepeatCount(Animation.INFINITE);
                    netAlarmContractualTaximeter.startAnimation(anim);
                    netsound = MediaPlayer.create(Contractual_Taximeter.this, R.raw.net_alert);
                    netsound.start();
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
                }

                handler.postDelayed(this, 1000);
            }

        };
        runnable2.run();


        Runnable runnable4 = new Runnable() {
            public void run() {
                GpsTracker gpsTracker = new GpsTracker(Contractual_Taximeter.this);
                ImageView checkGPSContractualTaximeter = (ImageView) findViewById(R.id.GpsConnectionContractualMultimeter);


                final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Resources res = getResources();
                    checkGPSContractualTaximeter.setImageDrawable(res.getDrawable(R.drawable.gpsnotconnectedicon));
                    gpsalarmContractualTaximeter.setText("موقعیت مکانی خود را روشن کنید !");
                    Animation anim2 = new AlphaAnimation(0.0f, 1.0f);
                    anim2.setDuration(50); //You can manage the blinking time with this parameter
                    anim2.setStartOffset(20);
                    anim2.setRepeatMode(Animation.REVERSE);
                    anim2.setRepeatCount(Animation.INFINITE);
                    gpsalarmContractualTaximeter.startAnimation(anim2);
                    gpssound = MediaPlayer.create(Contractual_Taximeter.this, R.raw.gps_alert);
                    gpssound.start();
                } else {
                    Resources res = getResources();
                    checkGPSContractualTaximeter.setImageDrawable(res.getDrawable(R.drawable.gpsconnectedicon));
                    gpsalarmContractualTaximeter.setText("");

                }


                double tempCost = timeRate / 600;
                if (weather == false && shift == false) {
                    cost += tempCost;
                } else if (shift == true && weather == false) {
                    cost += tempCost + ((tempCost) * 0.2);
                } else if (shift == false && weather == true) {
                    cost += (tempCost) + ((tempCost) * 0.2);
                } else {
                    cost += (tempCost) + ((tempCost) * 0.4);
                }


                // *********************** method without Noise GPS cansalation ********************************* //
//                        if(weather == false && shift == false){
//                            cost += (distanceNow * 1000 * (distanceRate / 200) / 10);
//                        }else if(shift == true && weather == false){
//                            cost += (distanceNow * 1000 * (distanceRate / 200) / 10) + ((distanceNow * 1000 * (distanceRate / 200) / 10) * 0.2);
//                        }else if(shift == false && weather == true){
//                            cost += (distanceNow * 1000 * (distanceRate / 200) / 10) + ((distanceNow * 1000 * (distanceRate / 200) / 10) * 0.2);
//                        }else{
//                            cost += (distanceNow * 1000 * (distanceRate / 200) / 10) + ((distanceNow * 1000 * (distanceRate / 200) / 10) * 0.4);
//                        }
//                        distanceTraveled[0] = distanceNow + distanceTraveled[0];
//                        distanceTiming[0] += distanceNow;
//                        if (timeWaiting >= 15) {
//                            if (distanceTiming[0] < 0.075) {
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


                if (flagStart == true) {
                    String formattedHour = String.format("%02d", hours);
                    String formattedMinute = String.format("%02d", minutes);
                    String formattedSecond = String.format("%02d", seconds);
                    hourTextView.setText(formattedHour);
                    minuteTextView.setText(formattedMinute);
                    secondTextView.setText(formattedSecond);
                    String formattedCost = String.format("%.2f", cost);
                    costTextView.setText(formattedCost);
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
                zoneString = "6";
                for (StaticRate staticRate : srates) {
                    if ((zoneString.equals(String.valueOf(staticRate.getZone()))) && (typeString.equals(String.valueOf(staticRate.getType()))) && (carString.equals(String.valueOf(staticRate.getVehicle())))) {
                        ZoneStringText = "گردشی و خطوط شهری";
                        if (zoneString.equals("1")) {
                            ZoneStringText = "گردشی و خطوط شهری";
                        } else if (zoneString.equals("2")) {
                            ZoneStringText = "راه آهن";
                        } else if (zoneString.equals("3")) {
                            ZoneStringText = "پایانه مسافربری";
                        } else if (zoneString.equals("4")) {
                            ZoneStringText = "بی سیم و آژانس";
                        } else if (zoneString.equals("5")) {
                            ZoneStringText = "فرودگاه";
                        } else if (zoneString.equals("6")) {
                            ZoneStringText = "قراردادی";
                        }

                        typeStringText = "گردشی و خطوط شهری";
                        if (typeString.equals("1")) {
                            typeStringText = "خوردوهای سواری سبک";
                        } else if (typeString.equals("2")) {
                            typeString = "ون";
                        }

                        initionalRate = 0;
                        distanceRate = 0;
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
    }


    void startTimer() {
        if (flagStart == false) {
            flagStart = true;
            startTime = System.currentTimeMillis();
            createJourney(identityCode, latitude2, longitude2, 0, 0, true, false);
        } else {
            flagStart = true;
        }
        ImageView image = (ImageView) findViewById(R.id.countdownIconContractual);
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
        ImageView image = (ImageView) findViewById(R.id.countdownIconContractual);
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
        ImageView image = (ImageView) findViewById(R.id.countdownIconContractual);
        Resources res = getResources();
        image.setImageDrawable(res.getDrawable(R.drawable.pauseicon));
    }

    int printCountDown() {
        nowTime = System.currentTimeMillis();
        return (int) (elapsed + System.currentTimeMillis() - startTime);
    }


    public void StartCountdownContractualClicked(View view) {
        startSound = MediaPlayer.create(Contractual_Taximeter.this, R.raw.start_journey);
        startSound.start();
        startTimer();
        ContractualTaximeterScreen.setBackgroundColor(Color.rgb(0, 77, 64));
        flagStart = true;
    }

    public void PauseCountdownContractualClicked(View view) {
        pauseSound = MediaPlayer.create(Contractual_Taximeter.this, R.raw.pause_journey);
        pauseSound.start();
        pauseTimer();
        ContractualTaximeterScreen.setBackgroundColor(Color.rgb(191, 54, 12));
    }

    public void StopCountdownContractualClicked(View view) {
        stopSound = MediaPlayer.create(Contractual_Taximeter.this, R.raw.stop_journey);
        stopSound.start();
        canStart = false;
        ManPlace.setEnabled(true);
        WomanPlace.setEnabled(true);
        ManPlace.setBackgroundColor(Color.rgb(53, 53, 53));
        WomanPlace.setBackgroundColor(Color.rgb(53, 53, 53));
        stopTimer();
        ContractualTaximeterScreen.setBackgroundColor(Color.rgb(183, 28, 28));
    }

    public void finishJourneyContractualClicked(View view) {
        finishSound = MediaPlayer.create(Contractual_Taximeter.this, R.raw.finish_journey);
        finishSound.start();
        flagStart = false;
        Timer = 0;
        seconds = 0;
        minutes = 0;
        hours = 0;
        elapsed = 0;
        pointFlag[0] = false;
        ContractualTaximeterScreen.setBackgroundColor(Color.rgb(26, 35, 126));
        String formattedCost = String.format("%.3f", cost);

        Intent i3 = new Intent(Contractual_Taximeter.this, FinishSingleTaximeter.class);
        i3.putExtra("idDriver", String.valueOf(identityCode));
        i3.putExtra("startTime", String.valueOf(startTime));
        i3.putExtra("finishTime", String.valueOf(System.currentTimeMillis()));
        i3.putExtra("srcLat", "1.0");
        i3.putExtra("srcLng", "1.0");
        i3.putExtra("desLat", "2.0");
        i3.putExtra("desLng", "2.0");
        i3.putExtra("distance", "3");
        i3.putExtra("psgNum", "0");
        i3.putExtra("cost", formattedCost);
        startActivity(i3);
        finish();

        cost = initionalRate / 10;

    }


    private void createJourney(int IdDriver, double Lat, double Lng, double Distance, double Cost, boolean IsStart, boolean IsFinish) {

        Journey journey = new Journey(IdDriver, 1, 1, 0, 0, 0, IsStart, IsFinish, isMale, false);

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


    public void womanPlaceContractualClicked(View view) {
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

    public void manPlaceContractualClicked(View view) {
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