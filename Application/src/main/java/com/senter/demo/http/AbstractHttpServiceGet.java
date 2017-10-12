package com.senter.demo.http;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public abstract class AbstractHttpServiceGet extends AsyncTask<Void, Void, String> {

	private static final String TAG = "AbstractHttpServiceGet";

	protected Context context;

    protected abstract void doOnPreExecute();

	protected abstract String setURLHttpServiceGet();

	protected abstract void doOnGetExecuteSuccessfully(String stringResult);

	protected abstract void doOnGetExecuteFailure(int code, String stringResult);

	private OkHttpClient client =  new OkHttpClient.Builder()
			.connectTimeout(10, TimeUnit.SECONDS)
			.writeTimeout(10, TimeUnit.SECONDS)
			.readTimeout(30, TimeUnit.SECONDS)
			.build();

	private Call call;
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		doOnPreExecute();
	}

	@Override
	protected String doInBackground(Void... params) {
//		context = WhizzKidApplication.mContext;
		String url = setURLHttpServiceGet();
		Log.e(TAG, url);
		Request request = null;


		try {
			request = new Request.Builder().url(url).build();
			call = client.newCall(request);

			Response response = call.execute();
			return response.body().string();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected void onPostExecute(String stringResult) {
		super.onPostExecute(stringResult);
		Log.d(TAG, stringResult + "");
		if (stringResult != null) {
			// Success
			doOnGetExecuteSuccessfully(stringResult);
		}else{
			// Failure
			doOnGetExecuteFailure(0, "");
		}
	}
}
