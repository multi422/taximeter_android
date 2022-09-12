package ir.mashhadict.taximeter;

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


public class QRPay extends AppCompatActivity {
    JsonPlaceHolderApi jsonPlaceHolderApi;
    String cost = "0";
    String idDriver;
    long duration;
    String desLat;
    String desLng;
    String distance;
    String isMale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_screen);
        Bundle extras = getIntent().getExtras();


        ImageView qrCodeIV  = findViewById(R.id.idIVQrcode);

        Bitmap bitmap;
        QRGEncoder qrgEncoder;

        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);

        // initializing a variable for default display.
        Display display = manager.getDefaultDisplay();

        // creating a variable for point which
        // is to be displayed in QR Code.
        Point point = new Point();
        display.getSize(point);

        // getting width and
        // height of a point
        int width = point.x;
        int height = point.y;

        // generating dimension from width and height.
        int dimen = width < height ? width : height;
        dimen = dimen * 4 / 5;

        // setting this dimensions inside our qr code
        // encoder to generate our qr code.
        qrgEncoder = new QRGEncoder("https://wallet.mashhad.ir/hot_link/88022000000".concat(cost), null, QRGContents.Type.TEXT, dimen);
        try {
            // getting our qrcode in the form of bitmap.
            bitmap = qrgEncoder.encodeAsBitmap();
            // the bitmap is set inside our image
            // view using .setimagebitmap method.
            qrCodeIV.setImageBitmap(bitmap);
        } catch (WriterException e) {
            // this method is called for
            // exception handling.
        }

        if (extras != null) {
            idDriver = extras.getString("idDriver");
            String startTime = extras.getString("startTime");
            String finishTime = extras.getString("finishTime");
            duration = Long.parseLong(finishTime) - Long.parseLong(startTime);
            String srcLat = extras.getString("srcLat");
            String srcLng = extras.getString("srcLng");
            desLat = extras.getString("desLat");
            desLng = extras.getString("desLng");
            distance = extras.getString("distance");
            isMale = extras.getString("isMale");
            cost = extras.getString("cost");
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://taximeterict.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);


    }

    public void backFinishClicked(View view) {
        finish();
    }

    public void cashPayInQRClicked(View view) {
        createJourney(Integer.parseInt(idDriver), Double.parseDouble(desLat), Double.parseDouble(desLng), Double.parseDouble(distance),Double.parseDouble(cost), false, true, false);
        finish();
    }

    private void createJourney(int IdDriver, double Lat, double Lng, double Distance, double Cost, boolean IsStart, boolean IsFinish, boolean payMet) {

        Journey journey = new Journey(IdDriver, Lat, Lng, Distance, Cost, 1, IsStart, IsFinish, Boolean.parseBoolean(isMale), payMet);

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
}