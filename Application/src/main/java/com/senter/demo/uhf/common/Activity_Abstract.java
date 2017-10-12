package com.senter.demo.uhf.common;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.senter.demo.uhf.App;

public abstract class Activity_Abstract extends Activity {
    protected final Activity_Abstract mActivityAbstract;

    public Activity_Abstract() {
        mActivityAbstract = this;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                App.clearMaskSettings();
                break;

            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    public final void showToast(final String string, final int length) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mActivityAbstract, string, length).show();
            }
        });
    }

    public final void showToast(final String string) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mActivityAbstract, string, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public final void setViewEnable(final View view, final boolean en) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.setEnabled(en);
            }
        });
    }

    protected void setText(final EditText et, final String txt) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                et.setText(txt);
            }
        });
    }
}
