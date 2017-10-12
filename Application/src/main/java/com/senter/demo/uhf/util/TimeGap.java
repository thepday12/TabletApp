package com.senter.demo.uhf.util;

import android.os.SystemClock;

public class TimeGap {
    long pre;
    int gap;

    public TimeGap(int gap) {
        this.gap = gap;
    }

    public synchronized boolean gap() {
        long now = SystemClock.elapsedRealtime();
        if (now < pre || pre + gap <= now) {
            pre = SystemClock.elapsedRealtime();
            return true;
        }
        return false;
    }

    public synchronized void reset() {
        pre = 0;
    }
}
