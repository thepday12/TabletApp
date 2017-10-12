package com.senter.demo.http;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public abstract class AbstractHttpServicePost extends AsyncTask<Void, Void, String> {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();
    private String mKeyPre;

    private static final String log_tag = "AbstractHttpServicePost";

    protected Context context;

    protected abstract void doOnPreExecute();

    protected abstract String setURLHttpServicePost();

    protected abstract JSONObject setJSONObject();

    protected abstract void doOnPostExecuteSuccessfully(String stringResult);

    protected abstract void doOnPostExecuteFailure(int code, String stringResult);

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        doOnPreExecute();
    }

    @Override
    protected String doInBackground(Void... params) {
//		context = WhizzKidApplication.mContext;
        String url = setURLHttpServicePost();
        Log.e(log_tag, url);
        JSONObject jsonObject = setJSONObject();
        Log.w(log_tag, jsonObject.toString());
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        Request request;

        try {
            request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Response response = null;


            response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String stringResult) {
        super.onPostExecute(stringResult);
        Log.d(log_tag, stringResult + "");
        if (stringResult != null) {
            // Success
            doOnPostExecuteSuccessfully(stringResult);
        } else {
            // Failure
            doOnPostExecuteFailure(0, "");
        }
    }

}
