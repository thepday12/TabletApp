package com.senter.demo.http;

import android.os.AsyncTask;
import android.util.Log;


import java.io.File;
import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PostAddTagTask extends AsyncTask<Void, Void, String> {
    public static final String TAG = "PostAddTagTask";

    private String beaconId;
    private String tagId;
    private AddTagTaskListener listener;

    private static final String IMGUR_CLIENT_ID = "...";
    private static final MediaType MEDIA_TYPE_IMAGE = MediaType.parse("image/*");

    private final OkHttpClient client = new OkHttpClient();

    public PostAddTagTask(String beaconId, String tagId) {
        this.beaconId = beaconId;
        this.tagId = tagId;
    }

    public void setOnAddTagTaskListener(AddTagTaskListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (listener != null) {
            listener.onPreAddTagTask();
        }
    }

    @Override
    protected String doInBackground(Void... params) {
        Log.e(TAG, UhfURL.GET_ADD_TAG);
        try {
            RequestBody formBody = new FormBody.Builder()
                    .add("beaconId", beaconId)
                    .add("productTag", tagId)
                    .build();
            Request request = new Request.Builder()
                    .url(UhfURL.GET_ADD_TAG)
                    .post(formBody)
                    .build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful())
                return null;

            return response.body().string();
        } catch (Exception e) {

        }
        return null;
    }

    @Override
    protected void onPostExecute(String stringResult) {
        super.onPostExecute(stringResult);
        if (listener != null) {
            if (stringResult != null && !stringResult.isEmpty()) {
                // Success
                listener.onAddTagTaskSuccess(stringResult);
            } else {
                listener.onAddTagTaskFailure(0, "String result is empty or null");
            }
        }
    }

    public interface AddTagTaskListener {
        public void onPreAddTagTask();

        public void onAddTagTaskSuccess(String stringResult);

        public void onAddTagTaskFailure(int statusCode, String error);

        public void transferred(long num);
    }
}
