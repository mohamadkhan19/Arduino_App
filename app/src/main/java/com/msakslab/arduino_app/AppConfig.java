package com.msakslab.arduino_app;

import com.google.gson.JsonElement;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.PATCH;
import retrofit.http.POST;
import retrofit.http.PUT;

public class AppConfig {

    public interface signin {
        @FormUrlEncoded
        @POST("/users/signin")
        void login(
                @Field("email") String email,
                @Field("password") String password,
                Callback<Response> callback);
    }

    public interface signup {
        @FormUrlEncoded
        @POST("/users/signup")
        void buyproduct(
                @Field("firstname") String firstname,
                @Field("lastname") String lastname,
                @Field("address") String address,
                @Field("email") String email,
                @Field("password") String password,
                Callback<Response> callback);
    }

    public interface read {
        @GET("/num")
        void readData(Callback<JsonElement> callback);


    }

    public interface led {
        @FormUrlEncoded
        @PUT("/led_data/58e6eee3f36d2834e13a1bca")
        void ledData(
                @Field("token") String token,
                @Field("led") Boolean led,
                Callback<Response> callback);
    }

    public interface sensor {
        @GET("/latest_sensor_data")
        void sensorData(Callback<Response> callback);
    }

    public interface notification {
        @GET("/notification")
        void notificationData(Callback<Response> callback);
    }


}
