package ir.mashhadict.taximeter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FinishSingleTaximeter extends AppCompatActivity {

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finish_signle_taximeter);
        Bundle extras = getIntent().getExtras();
        TextView IdCodeDriver= findViewById(R.id.idCodeDriver);

        TextView StartTimeText = findViewById(R.id.startTimeText);
        TextView FinishTimeText = findViewById(R.id.finishTimeText);

        TextView SecondsDurationTimeText = findViewById(R.id.secondsDurationTimeText);
        TextView MinutesDurationTimeText = findViewById(R.id.minutesDurationTimeText);
        TextView HoursDurationTimeText = findViewById(R.id.hoursDurationTimeText);

        TextView SourceLatText = findViewById(R.id.sourceLatText);
        TextView SourceLngText = findViewById(R.id.sourceLngText);
        TextView DestinationLatText = findViewById(R.id.destinationLatText);
        TextView DestinationLngText = findViewById(R.id.destinationLngText);
        TextView DistanceText = findViewById(R.id.distanceText);
        TextView CostTextBill = findViewById(R.id.costTextBill);

        if (extras != null) {
            String idDriver = extras.getString("idDriver");
            String startTime = extras.getString("startTime");
            String finishTime = extras.getString("finishTime");
            long duration = Long.parseLong(finishTime) - Long.parseLong(startTime);
            String srcLat = extras.getString("srcLat");
            String srcLng = extras.getString("srcLng");
            String desLat = extras.getString("desLat");
            String desLng = extras.getString("desLng");
            String distance = extras.getString("distance");
            String cost = extras.getString("cost");

            System.out.println(idDriver);
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
            MinutesDurationTimeText.setText(Integer.toString(minutes) + " دقیقه و ");
            HoursDurationTimeText.setText(Integer.toString(hours) + " ساعت و ");


            SourceLatText.setText(srcLat);
            SourceLngText.setText(srcLng);
            DestinationLatText.setText(desLat);
            DestinationLngText.setText(desLng);
            DistanceText.setText(distance);
            CostTextBill.setText(cost);

        }
    }


}