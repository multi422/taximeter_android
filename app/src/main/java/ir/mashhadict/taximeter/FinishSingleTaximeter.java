package ir.mashhadict.taximeter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.WriterException;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class FinishSingleTaximeter extends AppCompatActivity {

    String cost;
    String idDriver;
    long duration;
    String desLat;
    String desLng;
    String distance;
    String startTime;
    String finishTime;
    String srcLat;
    String srcLng;
    String isMale;
    String psgNum;
    JsonPlaceHolderApi jsonPlaceHolderApi;


    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finish_signle_taximeter);
        Bundle extras = getIntent().getExtras();
        TextView IdCodeDriver = findViewById(R.id.idCodeDriver);

        TextView StartTimeText = findViewById(R.id.startTimeText);
        TextView FinishTimeText = findViewById(R.id.finishTimeText);

        TextView SecondsDurationTimeText = findViewById(R.id.secondsDurationTimeText);
        TextView MinutesDurationTimeText = findViewById(R.id.minutesDurationTimeText);
        TextView HoursDurationTimeText = findViewById(R.id.hoursDurationTimeText);

        TextView DistanceText = findViewById(R.id.distanceText);
        TextView CostTextBill = findViewById(R.id.costTextBill);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://taximeterict.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);


        if (extras != null) {
            idDriver = extras.getString("idDriver");
            startTime = extras.getString("startTime");
            finishTime = extras.getString("finishTime");
            duration = Long.parseLong(finishTime) - Long.parseLong(startTime);
            srcLat = extras.getString("srcLat");
            srcLng = extras.getString("srcLng");
            desLat = extras.getString("desLat");
            desLng = extras.getString("desLng");
            distance = extras.getString("distance");
            isMale = extras.getString("isMale");
            psgNum = extras.getString("psgNum");
            cost = extras.getString("cost");

            IdCodeDriver.setText(idDriver);

            Date startTimeDate = new Date(Long.parseLong(startTime));
            String startTimeStr = simpleDateFormat.format(startTimeDate);
            StartTimeText.setText(startTimeStr);

            Date finishTimeDate = new Date(Long.parseLong(finishTime));
            String finishTimeStr = simpleDateFormat.format(finishTimeDate);
            FinishTimeText.setText(finishTimeStr);

            int hours = (int) duration / 3600000;
            int minutes = (int) (duration % 3600000) / 60000;
            int seconds = (int) (duration % 60000) / 1000;

            SecondsDurationTimeText.setText(Integer.toString(seconds) + " ثانیه ");
            MinutesDurationTimeText.setText(Integer.toString(minutes) + " دقیقه ");
            HoursDurationTimeText.setText(Integer.toString(hours) + " ساعت ");


            DistanceText.setText(distance);
            CostTextBill.setText(cost);

        }
    }

    private void createJourney(int IdDriver, double Lat, double Lng, double Distance, double Cost, boolean IsStart, boolean IsFinish, boolean payMet) {

        Journey journey = new Journey(IdDriver, Lat, Lng, Distance, Cost, Integer.parseInt(psgNum), IsStart, IsFinish, Boolean.parseBoolean(isMale), payMet);

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
                content += "Identity Code: " + postResponse.getIdDriver() + "\n";
                content += "Distance: " + postResponse.getDistance() + "\n";
                content += "Cost: " + postResponse.getCost() + "\n";
                System.out.println(content);

            }

            @Override
            public void onFailure(Call<Journey> call, Throwable t) {

            }
        });

        journey = null;
    }


    public void qrPayClicked(View view) {

        createJourney(Integer.parseInt(idDriver), Double.parseDouble(desLat), Double.parseDouble(desLng), Double.parseDouble(distance), Double.parseDouble(cost), false, true, true);
        Intent i8 = new Intent(FinishSingleTaximeter.this, QRPay.class);
        i8.putExtra("idDriver", idDriver);
        i8.putExtra("startTime", String.valueOf(startTime));
        i8.putExtra("finishTime", String.valueOf(System.currentTimeMillis()));
        i8.putExtra("srcLat", srcLat);
        i8.putExtra("srcLng", srcLng);
        i8.putExtra("desLat", desLat);
        i8.putExtra("desLng", desLng);
        i8.putExtra("distance", distance);
        i8.putExtra("isMale", isMale);
        i8.putExtra("psgNum", psgNum);
        i8.putExtra("cost", cost);
        startActivity(i8);

    }

    public void cashPayClicked(View view) {
        createJourney(Integer.parseInt(idDriver), Double.parseDouble(desLat), Double.parseDouble(desLng), Double.parseDouble(distance), Double.parseDouble(cost), false, true, false);
        finish();
    }

    public void mainPageClicked(View view) {
        finish();
    }
}