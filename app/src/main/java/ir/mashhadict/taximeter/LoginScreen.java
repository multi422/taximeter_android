package ir.mashhadict.taximeter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LoginScreen extends AppCompatActivity {

    private static final int ACTIVITY_CREATE = 0;
    boolean flag = false;


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final Handler handler = new Handler();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        if (!isNetworkConnected()) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

            alertDialog.setTitle("دستگاه به اینترنت متصل نیست !");
            alertDialog.setMessage("برای ورود به حساب کاربری خود باید اینترنت خود را فعال کنید");
        }

    }


    public void loginButtonClicked(View view) {

        EditText IdCode = findViewById(R.id.idCode);
        EditText Password = findViewById(R.id.password);
        //System.out.println(IdCode.getText().equals("") + " and " + Password.getText().equals(""));
        if (!isNetworkConnected()) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            Toast.makeText(LoginScreen.this, "دستگاه شما به اینترنت متصل نمی باشد، لطفا اینترنت دستگاه را فعال کنید و دوباره تلاش کنید",
                    Toast.LENGTH_LONG).show();
        } else {
            if (!String.valueOf(IdCode.getText()).equals("") && !String.valueOf(Password.getText()).equals("")) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://taximeterict.pythonanywhere.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                JsonPlaceHolderApi jsonPlaceHolderApi;

                jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
                Call<List<Driver>> call = jsonPlaceHolderApi.getDriver();

                call.enqueue(new Callback<List<Driver>>() {
                    @Override
                    public void onResponse(Call<List<Driver>> call, Response<List<Driver>> response) {
                        if (!response.isSuccessful()) {
                            System.out.println("Code: " + response.code());
                            return;
                        }
                        List<Driver> drivers = response.body();
                        for (Driver driver : drivers) {
                            System.out.println(driver.getPassword());
                            System.out.println(String.valueOf(Password.getText()));
                            System.out.println(String.valueOf(Password.getText()).hashCode());
                            int passwordInputHashed = String.valueOf(Password.getText()).hashCode();
                            System.out.println(passwordInputHashed);
                            if (Integer.parseInt(String.valueOf(IdCode.getText())) == driver.getIdentityCode() && passwordInputHashed == driver.getPassword()) {
                                flag = true;
                                IdCode.setTextColor(Color.GREEN);
                                Password.setTextColor(Color.GREEN);
                                Toast.makeText(LoginScreen.this, "شما با موفقیت وارد حساب کاربری خود شدید",
                                        Toast.LENGTH_LONG).show();
                                Intent i8 = new Intent(LoginScreen.this, MainPage.class);
                                i8.putExtra("identityCode", Integer.toString(driver.getIdentityCode()));
                                i8.putExtra("firstName", driver.getFirstName());
                                i8.putExtra("lastName", driver.getLastName());
                                i8.putExtra("zone", Integer.toString(driver.getZone()));
                                i8.putExtra("type", Integer.toString(driver.getType()));
                                i8.putExtra("car", Integer.toString(driver.getVehicle()));
                                i8.putExtra("carModel", driver.getCarModel());
                                i8.putExtra("pelak", driver.getPelak());
                                i8.putExtra("color", driver.getColor());
                                startActivityForResult(i8, ACTIVITY_CREATE);
                            }

                        }
                        if (flag == false) {
                            Toast.makeText(LoginScreen.this, "کد ملی یا رمز عبور وارد شده اشتباه است، لطفا دوباره تلاش کنید",
                                    Toast.LENGTH_LONG).show();
                            IdCode.setTextColor(Color.RED);
                            Password.setTextColor(Color.RED);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Driver>> call, Throwable t) {
                        System.out.println("not response");
                    }
                });
            } else {
                Toast.makeText(LoginScreen.this, "برای ورود به حساب کاربری باید تمامی قسمت های مورد نیاز را پر کنید",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}