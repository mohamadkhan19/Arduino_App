package com.msakslab.arduino_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class BuyProduct extends AppCompatActivity {
    String BASE_URL = "https://packers-backend.herokuapp.com";
    public EditText firstname, lastname, address, email, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
    public void onBuyProduct(View v){
        firstname= (EditText)findViewById(R.id.firstname);
        lastname= (EditText)findViewById(R.id.lastname);
        address= (EditText)findViewById(R.id.address);
        email= (EditText)findViewById(R.id.email);
        password= (EditText)findViewById(R.id.password);

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL) //Setting the Root URL
                .build();

        AppConfig.signup api = adapter.create(AppConfig.signup.class);
        api.buyproduct(
                firstname.getText().toString(),
                lastname.getText().toString(),
                address.getText().toString(),
                email.getText().toString(),
                password.getText().toString(),
                new Callback<Response>() {
                    @Override
                    public void success(Response result, Response response) {

                        try {

                            BufferedReader reader = new BufferedReader(new InputStreamReader(result.getBody().in()));
                            String resp;
                            resp = reader.readLine();
                            Log.d("success", "" + resp);

                            JSONObject jObj = new JSONObject(resp);
                            int success = jObj.getInt("success");

                            if(success == 1){
                                Toast.makeText(getApplicationContext(), "Buy Product Successful, Please Log in", Toast.LENGTH_SHORT).show();;
                                Intent intent = new Intent();
                                intent.setClass(BuyProduct.this, Login.class);
                                startActivity(intent);
                            } else{
                                Toast.makeText(getApplicationContext(), "Buy Product Fail", Toast.LENGTH_SHORT).show();
                            }


                        } catch (IOException e) {
                            Log.d("Exception", e.toString());
                        } catch (JSONException e) {
                            Log.d("JsonException", e.toString());
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(BuyProduct.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        );
    }
}
