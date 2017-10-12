package com.senter.demo.http;

import android.content.Context;


public class GetListProduceBeaconTask extends AbstractHttpServiceGet {
    private OnGetListProduceBeaconListener listener;
    private String beaconId;

    public interface OnGetListProduceBeaconListener {
        public void onPreGetListProduceBeacon();

        public void onGetListProduceBeaconSuccessfully(String stringResult);

        public void onGetListProduceBeaconFailure(int code, String stringResult);
    }

    public void setGetListProduceBeaconListener(OnGetListProduceBeaconListener listener) {
        this.listener = listener;
    }

    public GetListProduceBeaconTask(String beaconId, Context context) {
        this.beaconId = beaconId;
        this.context = context;
    }

    @Override
    protected void doOnPreExecute() {
        if (listener != null)
            listener.onPreGetListProduceBeacon();
    }

    @Override
    protected String setURLHttpServiceGet() {
        return String.format(UhfURL.GET_PRODUCE_BEACON, beaconId);
    }

    @Override
    protected void doOnGetExecuteSuccessfully(String stringResult) {
        if (listener != null)
            listener.onGetListProduceBeaconSuccessfully(stringResult);
    }

    @Override
    protected void doOnGetExecuteFailure(int code, String stringResult) {
        if (listener != null)
            listener.onGetListProduceBeaconFailure(code, stringResult);
    }

}
