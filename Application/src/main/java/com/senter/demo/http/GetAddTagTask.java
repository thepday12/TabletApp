package com.senter.demo.http;

import android.content.Context;


public class GetAddTagTask extends AbstractHttpServiceGet {
    private OnGetAddTagListener listener;
    private String beaconId;
    private String tagId;

    public interface OnGetAddTagListener {
        public void onPreGetAddTag();

        public void onGetAddTagSuccessfully(String stringResult);

        public void onGetAddTagFailure(int code, String stringResult);
    }

    public void setGetAddTagListener(OnGetAddTagListener listener) {
        this.listener = listener;
    }

    public GetAddTagTask(String beaconId, String tagId, Context context) {
        this.beaconId = beaconId;
        this.tagId = tagId;
        this.context = context;
    }

    @Override
    protected void doOnPreExecute() {
        if (listener != null)
            listener.onPreGetAddTag();
    }

    @Override
    protected String setURLHttpServiceGet() {
        return String.format(UhfURL.GET_ADD_TAG, beaconId, tagId);
    }

    @Override
    protected void doOnGetExecuteSuccessfully(String stringResult) {
        if (listener != null)
            listener.onGetAddTagSuccessfully(stringResult);
    }

    @Override
    protected void doOnGetExecuteFailure(int code, String stringResult) {
        if (listener != null)
            listener.onGetAddTagFailure(code, stringResult);
    }

}
