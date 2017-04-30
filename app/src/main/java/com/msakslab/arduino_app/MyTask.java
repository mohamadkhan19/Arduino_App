package com.msakslab.arduino_app;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static java.lang.Thread.sleep;

/**
 * Created by shafa on 4/29/2017.
 */

public class MyTask extends AsyncTask<Void, Void, Void> {

    Context context;
   /* TextView textView;
    Button button;
    ProgressDialog progressDialog;*/
   String BASE_URL = "https://packers-backend.herokuapp.com";


    //Constructor
    MyTask(Context context){
        this.context = context;
        /*this.textView = textView;
        this.button = button;*/

    }

    @Override
    protected Void doInBackground(Void... params){
        synchronized (this){
            while(true){

                try {
                    notification();
                    Log.d("success","hi");
                    sleep(1000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        //return null;
    }

    public void addNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle("Packers")
                        .setContentText("Empty room, Switch off the light.")
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        Intent notificationIntent = new Intent(context, Dashboard.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    public void notification(){
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL) //Setting the Root URL
                .build();

        AppConfig.notification api = adapter.create(AppConfig.notification.class);
        api.notificationData(new Callback<Response>() {
                                 @Override
                                 public void success(Response result, Response response) {

                                     try {

                                         BufferedReader reader = new BufferedReader(new InputStreamReader(result.getBody().in()));
                                         String resp;
                                         resp = reader.readLine();
                                         Log.d("success", "" + resp);

                                         JSONObject jObj = new JSONObject(resp);
                                         Boolean notification = jObj.getBoolean("notification");

                                         if(notification == true){
                                             //Toast.makeText(getApplicationContext(), "led is "+led, Toast.LENGTH_SHORT).show();;
                                             Log.d("inside if", "");
                                             addNotification();
                                             //sleep(60000);
                                         } else{
                                             // Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                                         }


                                     } catch (IOException e) {
                                         Log.d("Exception", e.toString());
                                     } catch (JSONException e) {
                                         Log.d("JsonException", e.toString());
                                     } /*catch (InterruptedException e) {
                                         e.printStackTrace();
                                     }*/
                                 }

                                 @Override
                                 public void failure(RetrofitError error) {
                                     //Toast.makeText(Dashboard.this, error.toString(), Toast.LENGTH_LONG).show();
                                 }
                             }
        );
    }
   /* @Override
    protected void onPreExecute() {


        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Upload In progress....");
        progressDialog.setMax(counter);
        progressDialog.setProgress(0);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(String result) {
        progressDialog.hide();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        progressDialog.setProgress(progress);
        mStatusTracker.setStatus( "\nOutput:"+i, "\ntemperature:"+temperature+"F"+"\nhumidity:"+humidity+"%"+"\nactivity:"+activity);
        Utils.printStatus(output);
    }*/
}