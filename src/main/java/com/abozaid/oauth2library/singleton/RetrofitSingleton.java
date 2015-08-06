package com.abozaid.oauth2library.singleton;

import retrofit.RestAdapter;

public class RetrofitSingleton {
	private static RestAdapter restAdapter = null;

	private RetrofitSingleton() {

	}

	public static RestAdapter getInstance(String Url) {
		if (restAdapter == null) {
			restAdapter = new RestAdapter.Builder().setEndpoint(Url)
					//.setLogLevel(RestAdapter.LogLevel.FULL)
					.build();
		}
		return restAdapter;
	}

}
