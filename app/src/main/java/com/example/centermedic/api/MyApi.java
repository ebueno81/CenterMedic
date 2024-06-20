package com.example.centermedic.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class MyApi {
    private static final String URL_API = "https://clinicaupn.somee.com/api/";
    private static Retrofit instance = null;


    public  static Retrofit getInstance(){
        if(instance==null){

            instance = new Retrofit.Builder()
                    .baseUrl(URL_API)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return instance;

    }
}
