package com.msakslab.arduino_app;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.msakslab.arduino_app.R.id.temperature;
import static java.lang.Thread.sleep;

public class Dashboard extends AppCompatActivity {

    String BASE_URL = "https://packers-backend.herokuapp.com";
    public String token;
    Switch onOfLedSwitch;
    Boolean led;
    public TextView sensorTemperature, sensorBrightness, sensorMotion;
    public MyTask myTask = new MyTask(Dashboard.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        token = (String) getIntent().getSerializableExtra("token");
        Log.d("token", token);
        onOfLedSwitch= (Switch) findViewById(R.id.led1);

        sensorTemperature=(TextView)findViewById(R.id.temperature);
        sensorBrightness=(TextView)findViewById(R.id.brightness);
        sensorMotion=(TextView)findViewById(R.id.motion);

        sensorValues();
        myTask.execute();


    }

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            sensorValues();
            handler.postDelayed(this, 1000);
        }
    };

    public void onbuttonSensorData(View v){
        handler.postDelayed(runnable, 1000);
    }


    public void ledSwtich(View v){

        if (onOfLedSwitch.isChecked()) {
            led = Boolean.valueOf(onOfLedSwitch.getTextOn().toString());
            Log.d("led", String.valueOf(led));
        }
        else {
            led = Boolean.valueOf(onOfLedSwitch.getTextOff().toString());
            Log.d("led", String.valueOf(led));
        }
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL) //Setting the Root URL
                .build();

        AppConfig.led api = adapter.create(AppConfig.led.class);
        api.ledData(
                token,
                led,
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
                                Toast.makeText(getApplicationContext(), "led is "+led, Toast.LENGTH_SHORT).show();;
                            } else{
                                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                            }


                        } catch (IOException e) {
                            Log.d("Exception", e.toString());
                        } catch (JSONException e) {
                            Log.d("JsonException", e.toString());
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(Dashboard.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        );

    }

    public void sensorValues(){
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL) //Setting the Root URL
                .build();

        AppConfig.sensor api = adapter.create(AppConfig.sensor.class);
        api.sensorData(new Callback<Response>() {
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
                        //Toast.makeText(getApplicationContext(), "led is "+led, Toast.LENGTH_SHORT).show();;

                            JSONArray obj = jObj.getJSONArray("obj");
                            JSONObject values = (JSONObject) obj.get(0);
                            String temperature = values.getString("temperature");
                            String brightness = values.getString("brightness");
                            String motion = values.getString("motion");
                            Log.d("temperature", "" + temperature);
                            sensorTemperature.setText(temperature);
                            sensorBrightness.setText(brightness);
                            sensorMotion.setText(motion);



                    } else{
                       // Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                    }


                } catch (IOException e) {
                    Log.d("Exception", e.toString());
                } catch (JSONException e) {
                    Log.d("JsonException", e.toString());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(Dashboard.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }
        );
    }




}

