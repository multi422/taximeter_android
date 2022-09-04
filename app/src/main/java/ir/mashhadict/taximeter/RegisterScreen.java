package ir.mashhadict.taximeter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterScreen extends AppCompatActivity {

    private static final int ACTIVITY_CREATE = 0;

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_screen);
    }


    int id;
    String pass;
    String passRepeat;
    String firstName;
    String lastName;
    String zone;
    String type;
    String vehicle;
    String carModel;
    String pelak;
    String color;
    boolean isConfirmed;

    JsonPlaceHolderApi jsonPlaceHolderApi;


    private void createPost(
            int identityCode,
            int password,
            String firstName,
            String lastName,
            String Zone,
            String Type,
            String Vehicle,
            String carModel,
            String pelak,
            String color
    ) {
        int ZoneInt = Integer.parseInt(Zone);
        int TypeInt = Integer.parseInt(Type);
        int VehicleInt = Integer.parseInt(Vehicle);
        Driver driver = new Driver(identityCode, password, firstName, lastName, ZoneInt, TypeInt, VehicleInt, carModel, pelak, color);
// int identityCode, int password, String firstName, String lastName, int zone, int type, int vehicle, String carModel, String pelak, String color, boolean isConfirmed
        Call<Driver> call = jsonPlaceHolderApi.createDriver("application/json", driver);

        call.enqueue(new Callback<Driver>() {
            @Override
            public void onResponse(Call<Driver> call, Response<Driver> response) {
                if (!response.isSuccessful()) {
                    System.out.println(response.errorBody());
                    System.out.println("Code: " + response.code());
                    Toast.makeText(RegisterScreen.this, "مشکلی در فرآیند ثبت نام بوجود آمده است، لطفا دوباره تلاش کنید",
                            Toast.LENGTH_LONG).show();
                    return;
                } else {

                    Driver postResponse = response.body();

                    String content = "";
                    content = "Code: " + response.code() + "\n";
                    content = "ID: " + postResponse.getId() + "\n";
                    content += "Identity Code: " + postResponse.getIdentityCode() + "\n";
                    content += "First Name: " + postResponse.getFirstName() + "\n";
                    content += "LastName: " + postResponse.getLastName() + "\n\n";
                    content += "Car Model: " + postResponse.getCarModel() + "\n\n";
                    content += "Pelak: " + postResponse.getPelak() + "\n\n";
                    content += "Color: " + postResponse.getColor() + "\n\n";


                    Toast.makeText(RegisterScreen.this, "ثبت نام شما با موفقیت انجام شد، برای دفعات بعدی می توانید از قسمت ورود، وارد اکانت کاربری خود شوید",
                            Toast.LENGTH_LONG).show();

                    Intent i7 = new Intent(RegisterScreen.this, MainPage.class);
                    i7.putExtra("identityCode", Integer.toString(id));
                    i7.putExtra("firstName", firstName);
                    i7.putExtra("lastName", lastName);
                    i7.putExtra("zone", Zone);
                    i7.putExtra("type", Type);
                    i7.putExtra("car", Vehicle);
                    i7.putExtra("carModel", carModel);
                    i7.putExtra("pelak", pelak);
                    i7.putExtra("color", color);
                    startActivityForResult(i7, ACTIVITY_CREATE);
                }
            }

            @Override
            public void onFailure(Call<Driver> call, Throwable t) {

            }
        });
    }


    public void registerButtonClicked(View view) {

        if (!isNetworkConnected()) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            Toast.makeText(RegisterScreen.this, "دستگاه شما به اینترنت متصل نمی باشد، لطفا اینترنت دستگاه را فعال کنید و دوباره تلاش کنید",
                    Toast.LENGTH_LONG).show();
        } else {

            boolean check = false;
            boolean flag = true;

            EditText idText = findViewById(R.id.idInput);
            EditText passText = findViewById(R.id.passInput);
            EditText passRepeatText = findViewById(R.id.passRepeatInput);
            EditText firstNameText = findViewById(R.id.firstNameInput);
            EditText lastNameText = findViewById(R.id.lastNameInput);
            EditText directionText = findViewById(R.id.directionInput);
            EditText typeText = findViewById(R.id.typeInput);
            EditText carText = findViewById(R.id.carInput);
            EditText carModelText = findViewById(R.id.carModelInput);
            EditText pelakText = findViewById(R.id.pelakInput);
            EditText colorText = findViewById(R.id.carColorInput);
            CheckBox checkBoxInformation = findViewById(R.id.checkboxInformation);


            if (String.valueOf(idText.getText()).equals("")) {
                flag = false;
            }

            if (flag == true) {
                id = Integer.parseInt(String.valueOf(idText.getText()));
            } else {
                Toast.makeText(RegisterScreen.this, "تمامی فیلدها باید به طور کامل و با اطلاعات صحیح پر شوند، لطفا دوباره تلاش کنید",
                        Toast.LENGTH_LONG).show();
            }

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://taximeterict.pythonanywhere.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();


            jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

            pass = String.valueOf(passText.getText());
            passRepeat = String.valueOf(passRepeatText.getText());
            firstName = String.valueOf(firstNameText.getText());
            lastName = String.valueOf(lastNameText.getText());
            zone = String.valueOf(directionText.getText());
            type = String.valueOf(typeText.getText());
            vehicle = String.valueOf(carText.getText());
            carModel = String.valueOf(carModelText.getText());
            pelak = String.valueOf(pelakText.getText());
            color = String.valueOf(colorText.getText());
            check = checkBoxInformation.isChecked();

            System.out.println("Check: " + check);

            if (!pass.equals(passRepeat)) {
                Toast.makeText(RegisterScreen.this, "رمز ورودی شما با تکرار رمز مطابقت ندارد، لطفا دوباره تلاش کنید",
                        Toast.LENGTH_LONG).show();
                passText.setTextColor(Color.RED);
                passRepeatText.setTextColor(Color.RED);
            }
            if (passText.equals("") || passRepeatText.equals("") || firstNameText.equals("") || lastNameText.equals("") || carModelText.equals("") || pelakText.equals("") || colorText.equals("") || directionText.equals("") || carModelText.equals("") || typeText.equals("") || check == false) {
                Toast.makeText(RegisterScreen.this, "تمامی فیلدها باید به طور کامل و با اطلاعات صحیح پر شوند، لطفا دوباره تلاش کنید",
                        Toast.LENGTH_LONG).show();
            } else if (pass.equals(passRepeat) && !passText.equals("") && !passRepeatText.equals("") && !firstNameText.equals("") && !lastNameText.equals("") && !carModelText.equals("") && !pelakText.equals("") && !colorText.equals("") && !directionText.equals("") && !carModelText.equals("") && !typeText.equals("") && check == true) {
                //createPost(id, pass.hashCode(), firstName, lastName, zone, type, vehicle, carModel, pelak, color);
                createPost(id, pass.hashCode(), firstName, lastName, zone, type, vehicle, carModel, pelak, color);
            }
        }
    }


}