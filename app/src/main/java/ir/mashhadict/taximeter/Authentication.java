package ir.mashhadict.taximeter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class Authentication extends AppCompatActivity {

    private static final int ACTIVITY_CREATE = 0;

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.authentication);

        if(!isNetworkConnected()){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

            // Setting Dialog Title
            alertDialog.setTitle("دستگاه به اینترنت متصل نیست !");
            alertDialog.setMessage("برای ثبت نام یا ورود به حساب کاربری خود ابتدا باید دستگاه را به اینترنت متصل کنید");
        }

    }


    public void loginClicked(View view) {
        Intent i4 = new Intent(this, LoginScreen.class);
        startActivityForResult(i4, ACTIVITY_CREATE);
    }

    public void registerClicked(View view) {
        Intent i2 = new Intent(this, RegisterScreen.class);
        startActivityForResult(i2, ACTIVITY_CREATE);
    }

}