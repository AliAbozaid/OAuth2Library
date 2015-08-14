# OAuth2Library
OAuth2Library is an extension for Retrofit that simplifies the process of authenticating against an OAuth 2 provider.
# Authentication

OAuthToken oAuthToken = new OAuthToken("http://www.yoururl.com/","abozaid","1234567", this);
1) Base URL.
2) Client ID.
3) Secret key.

# Authorizing Request

Credential credential = oAuthToken.getAccessTokenWithID("default");

If you want to use our default send parameter “default” if not send your keys.

if(credential == null || credential.getAccess_token()==null)
 {
    oAuthToken.AuthentiacteUsingOAuth("v1/api/oauth", "5592b3be0cf2921ec587d5",
      "935eadc6-cd58-4743-8f14-5f7af391c992", "read write", "password", 
        new MethodsCallback<Credential>()
        {
            @Override public void failure(RetrofitError arg0)
            { 
              Toast.makeText(MainActivity.this, arg0.getMessage(),Toast.LENGTH_SHORT).show(); 
            } 
            @Override public void success(Credential arg0)
            {
              if(credential != null) 
              { 
                oAuthToken.saveTokenWithID(arg0, "default"); 
                Toast.makeText(MainActivity.this, arg0.getAccess_token(), Toast.LENGTH_SHORT).show();
              }
            }
        });
 }


We first check our credential if it does not exist we call (AuthentiacteUsingOAuth) method to authenticate and here is it’s parameters:
1) path URL, and here we follow the standards., if your backend developer follow it, all you need is to put your path url except for (/token). if not, you need to clone the repo and import it as a module and change the URL.
2) UserName, your username returned from the server after the registration process.
3) Password,  your password returned from the server after the registration process.
4) Scope, it will be one of three choices(read, write, read write).
5) Grant type, it will be one of three choices(password, authorization_code, client_credential, refresh_token).
6) Response callback.

# Storing Credentials
if our credential returned in success method we call (saveTokenWithID) method and we pass the credential object and our id (“default”, or your id ).
# Refreshing Credentials

before any call to the server we call (isExpired(id)) method, if  it returns false we do our call but if it  returns true that means that we need to refresh our token so we need to call (refreshToken()) method and it’s parameter:
1) Path URL, same as in (AuthentiacteUsingOAuth)method path url except for  (/token).
2) Refresh token, get it by calling(getRefreshTokenWithId(id)) after storing it in (AuthentiacteUsingOAuth) method.
3) Response callback.
also you need to call  (saveTokenWithID) method in success method to save your new credentials.

# Delete Credentials
There is Two Methods To delete your Credential
deleteAllToken().
deleteTokenWithId(id), your id or “default”.

# Others Method
getAccessTokenWithId(id) return Credential object.
getTokenwithId(id) return Token String.
getRefreshTokenWithId(id) return RefreshToken String.
getExpiryDateWithId(id) return expiryDate Long.




