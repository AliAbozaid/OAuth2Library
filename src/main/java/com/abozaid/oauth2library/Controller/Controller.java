package com.abozaid.oauth2library.Controller;


import com.abozaid.oauth2library.Model.Credential;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by aliabozaid on 8/3/15.
 */
public class Controller {
    public interface MethodsCallback<T>{
        public void failure(RetrofitError arg0);
        public void success(T arg0);
    }

    public interface getAccessTokens {
        @POST("/token?filters[0][operator]=equals")
        @FormUrlEncoded
        @Headers({"Accept: application/json"})
        void get_access_token(@Query("grant_type") String grant_type,
                              @Field("username") String username,
                              @Field("password") String password,
                              @Field("scope") String scope,
                              @Header("Authorization") String authorization,
                              Callback<Credential> callback);
    }

    public interface refreshToken {
        @FormUrlEncoded
        @POST("/token?filters[0][operator]=equals")
        void refresh_token(@Query("grant_type") String grant_type,
                           @Field("refresh_token") String refreshToken,
                           @Header("Authorization") String authorization,
                           Callback<Credential> callback);
    }
}
