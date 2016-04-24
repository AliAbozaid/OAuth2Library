package com.abozaid.oauth2library.singleton;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitSingleton {
    private static Retrofit restAdapter = null;

    private RetrofitSingleton() {

    }

    public static Retrofit getInstance(String url) {
        if (restAdapter == null) {
            restAdapter = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return restAdapter;
    }

}
