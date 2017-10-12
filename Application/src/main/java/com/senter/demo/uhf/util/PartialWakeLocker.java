package com.senter.demo.uhf.util;

import android.content.Context;
import android.os.PowerManager;

public final class PartialWakeLocker {
    private static final String TAG="PartialWakeLocker";
    private final Context context;
    private final String tag;
    private PowerManager mManager;
    private PowerManager.WakeLock mWakeLock;
    public PartialWakeLocker(Context context,String tag){
        if (context==null) throw new IllegalArgumentException();
        this.context=context;
        this.tag=TAG+":"+tag;
    }
    public void wakeLockAcquire(){
        if (mWakeLock!=null) {
            if (mWakeLock.isHeld()){
                return ;
            }else{
                mWakeLock.release();
                mWakeLock=null;
            }
        }

        if (mManager ==null){
            mManager = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
        }

        PowerManager.WakeLock wakeLock = mManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, tag);
        wakeLock.acquire();

        mWakeLock=wakeLock;
    }
    public void wakeLockRelease(){
        if (mWakeLock!=null) {
            mWakeLock.release();
            mWakeLock=null;
        }
    }
}