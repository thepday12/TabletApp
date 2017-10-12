package com.senter.demo.uhf.common;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import com.senter.demo.uhf.App;
import com.senter.demo.uhf.R;
import com.senter.demo.uhf.util.PartialWakeLocker;
import com.senter.support.openapi.StUhf.Q;
import com.senter.support.openapi.StUhf.UII;

public abstract class Activity0InventoryCommonAbstractAB extends Activity0InventoryCommonAbstract {
    public static final String TAG = "Activity0CmnAbstractABC";

    private final PartialWakeLocker mPartialWakeLocker =new PartialWakeLocker(this, TAG);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()){
            mPartialWakeLocker.wakeLockRelease();
        }
    }

    @Override
    protected void onDestroy() {
        mPartialWakeLocker.wakeLockRelease();
        super.onDestroy();
    }

    protected void uiOnInverntryButton() {
        if (getViews().isStateInventoring()) {
            if (stopInventory() == true) {
                mPartialWakeLocker.wakeLockRelease();
                getViews().setStateStoped();
            }
            return;
        } else {
            if (startInventory()) {
                mPartialWakeLocker.wakeLockAcquire();
                getViews().setStateStarted();
            }
        }
    }

    protected abstract UII startInventorySingleStep();

    protected abstract boolean startInventorySingleTag();

    protected abstract boolean startInventoryAntiCollision();

    protected abstract boolean stopInventory();


    protected final boolean startInventory() {
        boolean ret = false;

        switch (getViews().getSpecifiedInventoryMode()) {
            case SingleStep:
                enableBtnInventory(false);
                new Thread() {
                    public void run() {
                        UII uii = null;

                        uii = startInventorySingleStep();
                        Log.v(TAG, "startInventorySingleStep finished");

                        if (uii != null) {
                            addNewUiiMassageToListview(uii);
                        }
                        enableBtnInventory(true);
                    }

                    ;
                }.start();
                ret = false;
                return ret;
            case SingleTag:
                ret = startInventorySingleTag();
                if (ret == false) {
                    App.stop();
                }
                return ret;
            case AntiCollision:
                ret = startInventoryAntiCollision();
                if (ret == false) {
                    App.stop();
                }
                return ret;
            default:
                break;
        }
        return ret;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (getViews().isStateInventoring())// uhf module is inventoring
                {
                    App.stop();
                }
                break;
            case 222:
                if (getViews().isStateInventoring() == false) {
                    finish();
                    uiOnInverntryButton();
                }
                break;
            default:
                break;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 222:
                if (getViews().isStateInventoring()) {
                    uiOnInverntryButton();
                }
                break;
            default:
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean superRslt=super.onPrepareOptionsMenu(menu);
        if (superRslt==false) return superRslt;

        if (getViews().isStateInventoring()) return true;

        SubMenu sm = menu.addSubMenu(getString(R.string.idSetQ));//R.string.SetQ

        int order = 0;

        Q q = getQ();
        int now = -1;
        if (q != null) {
            now = q.ordinal();
        }
        Log.v(TAG, "current Q value:" + now);

        int groupId = 1;
        int itmeid = 10;
        for (int i = 0; i <= 15; i++) {
            MenuItem item = sm.add(groupId, itmeid++, order++, "" + i);
            item.setCheckable(true);
            if (now == i) {
                item.setChecked(true);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25: {
                Log.v(TAG, "Q will be set is：" + (item.getItemId() - 10));

                if (setQ(Q.values()[item.getItemId() - 10]) == false) {
                    showToast("q setting error");
                } else {
                    showToast("q setting ok");
                }
                break;
            }
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected abstract Q getQ();
    protected abstract boolean setQ(Q q);
}
