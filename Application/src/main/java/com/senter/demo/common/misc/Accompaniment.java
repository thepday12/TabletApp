package com.senter.demo.common.misc;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

public final class Accompaniment {
    public MediaPlayer mBarcodeMediaPlayer = null;
    Context ownersContext = null;
    private final int myResSoundId;
    private boolean isReleased = false;

    private Accompaniment(Context context, int resId) {
        if (context == null) throw new IllegalArgumentException("context cannot be null");
        ownersContext = context;
        myResSoundId = resId;
    }

    /**
     * @param resId resource id of accompaniment
     */
    public static final Accompaniment newInstanceOfResource(Context contxt, int resId) {
        return new Accompaniment(contxt, resId);
    }

    public enum RingerMode {SILENT, VIBRATE, NORMAL}

    private AudioManager mAudioManager;

    public RingerMode ringerMode() {
        if (mAudioManager == null) {
            mAudioManager = (AudioManager) ownersContext.getSystemService(Context.AUDIO_SERVICE);
        }
        return RingerMode.values()[mAudioManager.getRingerMode()];
    }

    public boolean start() {
        return start(false);
    }

    public boolean startForce() {
        return start(true);
    }

    private synchronized final boolean start(boolean forceInSilence) {
        if (isReleased) {
            return false;
        }
        if (mBarcodeMediaPlayer == null) {
            mBarcodeMediaPlayer = MediaPlayer.create(ownersContext, myResSoundId);
            if (mBarcodeMediaPlayer == null) {
                return false;
            }
        }

        if (forceInSilence == false && ringerMode() != RingerMode.NORMAL) {
            return false;
        }

        mBarcodeMediaPlayer.start();

        return true;
    }

    public synchronized void release() {
        isReleased = true;
        if (mBarcodeMediaPlayer != null) {
            mBarcodeMediaPlayer.release();
            mBarcodeMediaPlayer = null;
        }
    }
}
