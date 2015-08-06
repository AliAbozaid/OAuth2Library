package com.abozaid.oauth2library;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.abozaid.oauth2library.Controller.Controller.MethodsCallback;
import com.abozaid.oauth2library.Controller.Controller.getAccessTokens;
import com.abozaid.oauth2library.Controller.Controller.refreshToken;
import com.abozaid.oauth2library.Model.Credential;
import com.abozaid.oauth2library.singleton.RetrofitSingleton;

import java.util.Calendar;
import java.util.Date;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by aliabozaid on 8/1/15.
 */
public class OAuthToken {
    Context context;
    private SharedPreferences sharedpreferences;
    private final String ACCESS_TOKEN="ACCESS_TOKEN";
    private final String TOKEN_TYPE ="TOKEN_TYPE";
    private final String REFRESH_TOKEN="REFRESH_TOKEN";
    private final String EXPIRATION_DATE="EXPIRATION_DATE";
    private final String OAUTH_PREFERENCE = "OAUTH_PREFERENCE";
    public SharedPreferences.Editor editor;
    String baseUrl, clientID, secret;
    String basicAuth;



    public OAuthToken(Context context)
    {
        this.context = context;
    }

    public OAuthToken(String baseUrl, String clientID, String secret, Context context){

        this.context = context;
        this.baseUrl = baseUrl;
        this.clientID = clientID;
        this.secret = secret;

        byte[] credentials = (clientID+":"+secret).getBytes();
        basicAuth = "Basic " + Base64.encodeToString(credentials, Base64.NO_WRAP);

    }


    //check Credentials
    public boolean isExpired(String id)
    {
        if(id.equals("default"))
        {
            sharedpreferences = context.getSharedPreferences(OAUTH_PREFERENCE, Context.MODE_PRIVATE);
        }
        else{
            sharedpreferences = context.getSharedPreferences(id, Context.MODE_PRIVATE);
        }

        //expiry time
        long expiredate = sharedpreferences.getLong(EXPIRATION_DATE, -1);
        Date expireDate= new Date(expiredate);
        //current time
        Calendar c = Calendar.getInstance();
        Date currentLocalTime = c.getTime();
        //return true if current time greater than expiry date
        return  currentLocalTime.after(expireDate);
    }
    //save Expiry date
    public void saveExpiryDate(long expire, String id)
    {
        Calendar c = Calendar.getInstance();
        Date currentLocalTime = c.getTime();
        //Log.d("currentDate", currentLocalTime.getTime() + "");
        long newDate = currentLocalTime.getTime()+(expire * 1000);
        Date expireDate= new Date(newDate);
        //Log.d("expireDate", expireDate + "");
        if(id.equals("default"))
        {
            sharedpreferences = context.getSharedPreferences(OAUTH_PREFERENCE, Context.MODE_PRIVATE);
            editor = sharedpreferences.edit();
            editor.putLong(EXPIRATION_DATE, expireDate.getTime());//plus current time
            editor.commit();
        }
        else
        {
            sharedpreferences = context.getSharedPreferences(id, Context.MODE_PRIVATE);
            editor = sharedpreferences.edit();
            editor.putLong(EXPIRATION_DATE, expireDate.getTime());//plus current time
            editor.commit();
        }

    }
    //authonticate
    public void AuthentiacteUsingOAuth(String pathUrl,String userName, String password, String scope, String grant_type, final MethodsCallback callback )
    {
        getAccessTokens retrofitRegister = RetrofitSingleton.getInstance(baseUrl+""+pathUrl).create(getAccessTokens.class);
        retrofitRegister.get_access_token(grant_type, userName, password, scope, basicAuth, new Callback<Credential>() {

            @Override
            public void failure(RetrofitError arg0) {
                // TODO Auto-generated method stub

                callback.failure(arg0);

            }

            @Override
            public void success(Credential arg0, Response arg1) {
                // TODO Auto-generated method stub
                callback.success(arg0);
                //Toast.makeText(context, arg0.RequestTypeId + ": register", Toast.LENGTH_LONG).show();
            }
        });
    }

    //refresh token
    public void refreshToken(String pathUrl, String refreshToken, final MethodsCallback callback )
    {
        //input refreshToken
        //this.pathUrl = pathUrl;
        refreshToken retrofitRegister = RetrofitSingleton.getInstance(baseUrl+""+pathUrl).create(refreshToken.class);
        retrofitRegister.refresh_token("refresh_token", refreshToken, basicAuth, new Callback<Credential>() {

            @Override
            public void failure(RetrofitError arg0) {
                // TODO Auto-generated method stub

                callback.failure(arg0);

            }

            @Override
            public void success(Credential arg0, Response arg1) {
                // TODO Auto-generated method stub
                callback.success(arg0);
                //Toast.makeText(context, arg0.RequestTypeId + ": register", Toast.LENGTH_LONG).show();
            }
        });
    }
    //delete all tokens
    public void deleteAllToken()
    {
        editor.clear().commit();
    }
    //delete toke with ID
    public void deleteTokenWithId(String id)
    {
        if(id.equals("default"))
        {
            sharedpreferences = context.getSharedPreferences(OAUTH_PREFERENCE, 0);
            sharedpreferences.edit().remove(OAUTH_PREFERENCE).commit();
        }
        else
        {
            sharedpreferences = context.getSharedPreferences(id, 0);
            sharedpreferences.edit().remove(id).commit();
        }
    }

    //save token in shared
    public void saveTokenWithID(Credential credential, String id)
    {
        if(id.equals("default")|| id==null)
        {
            sharedpreferences = context.getSharedPreferences(OAUTH_PREFERENCE, Context.MODE_PRIVATE);
            editor = sharedpreferences.edit();
            editor.putString(ACCESS_TOKEN, credential.getAccess_token());
            editor.putString(TOKEN_TYPE, credential.getToken_type());
            //editor.putString(EXPIRATION_DATE, credential.expires_in);//plus current time
            editor.putString(REFRESH_TOKEN, credential.getRefresh_token());
            editor.commit();
            saveExpiryDate(credential.getExpires_in(), "default");
        }
        else
        {
            sharedpreferences = context.getSharedPreferences(id, Context.MODE_PRIVATE);
            editor = sharedpreferences.edit();
            editor.putString(ACCESS_TOKEN, credential.getAccess_token());
            //editor.putString(EXPIRATION_DATE, credential.expires_in);//plus current time
            editor.putString(REFRESH_TOKEN, credential.getRefresh_token());
            editor.commit();
            saveExpiryDate(credential.getExpires_in(), id);
        }


    }

    //return current token
    public Credential getAccessTokenWithID(String id)
    {
        if(id.equals("default")|| id==null)
        {
            sharedpreferences = context.getSharedPreferences(OAUTH_PREFERENCE, Context.MODE_PRIVATE);
            Credential credential = new Credential();
            credential.setAccess_token(sharedpreferences.getString(ACCESS_TOKEN, null));
            credential.setExpires_in(sharedpreferences.getLong(EXPIRATION_DATE, -1));
            credential.setRefresh_token(sharedpreferences.getString(REFRESH_TOKEN, null));
            credential.setToken_type(sharedpreferences.getString(TOKEN_TYPE, null));

            return  credential;
        }
        else
        {
            sharedpreferences = context.getSharedPreferences(id, Context.MODE_PRIVATE);
            Credential credential = new Credential();
            credential.setAccess_token(sharedpreferences.getString(ACCESS_TOKEN, null));
            credential.setExpires_in(sharedpreferences.getLong(EXPIRATION_DATE, -1));
            credential.setRefresh_token(sharedpreferences.getString(REFRESH_TOKEN, null));
            credential.setToken_type(sharedpreferences.getString(TOKEN_TYPE, null));

            return  credential;
        }

    }
    //return string access_token
    public String getTokenWithId(String id)
    {
        if(id.equals("default"))
        {
            sharedpreferences = context.getSharedPreferences(OAUTH_PREFERENCE, Context.MODE_PRIVATE);
            return sharedpreferences.getString(ACCESS_TOKEN, null);
        }
        else
        {
            sharedpreferences = context.getSharedPreferences(id, Context.MODE_PRIVATE);
            return sharedpreferences.getString(ACCESS_TOKEN, null);
        }
    }

    //return string refresh_token
    public String getRefreshTokenWithId(String id)
    {
        if(id.equals("default"))
        {
            sharedpreferences = context.getSharedPreferences(OAUTH_PREFERENCE, Context.MODE_PRIVATE);
            return sharedpreferences.getString(REFRESH_TOKEN, null);
        }
        else
        {
            sharedpreferences = context.getSharedPreferences(id, Context.MODE_PRIVATE);
            return sharedpreferences.getString(REFRESH_TOKEN, null);
        }
    }

    //return string expiry_date
    public long getExpiryDateWithId(String id)
    {
        if(id.equals("default"))
        {
            sharedpreferences = context.getSharedPreferences(OAUTH_PREFERENCE, Context.MODE_PRIVATE);
            return sharedpreferences.getLong(EXPIRATION_DATE, -1);

        }
        else
        {
            sharedpreferences = context.getSharedPreferences(id, Context.MODE_PRIVATE);
            return sharedpreferences.getLong(EXPIRATION_DATE, -1);

        }
    }




}
