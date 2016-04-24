package com.abozaid.oauth2library.Controller;


import com.abozaid.oauth2library.Model.Credential;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;


/**
 * Created by aliabozaid on 8/3/15.
 */
public class Controller {
    public interface MethodsCallback<T> {
        public void failure(Throwable arg0);

        public void success(T arg0);

        public void responseBody(Call<T> call);
    }

    public interface GetAccessToken {
        @POST("/token?filters[0][operator]=equals")
        @FormUrlEncoded
        @Headers({"Accept: application/json"})
        Call<Credential> getAccessToken(@Query("grant_type") String grant_type,
                                        @Field("username") String username,
                                        @Field("password") String password,
                                        @Field("scope") String scope,
                                        @Header("Authorization") String authorization);
    }

    public interface RefreshToken {
        @FormUrlEncoded
        @POST("/token?filters[0][operator]=equals")
        Call<Credential> refreshToken(@Query("grant_type") String grant_type,
                                      @Field("refresh_token") String refreshToken,
                                      @Header("Authorization") String authorization);
    }
}
