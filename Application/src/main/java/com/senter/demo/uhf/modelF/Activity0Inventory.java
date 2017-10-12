package com.senter.demo.uhf.modelF;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.senter.demo.uhf.App;
import com.senter.demo.uhf.R;
import com.senter.demo.uhf.util.PartialWakeLocker;
import com.senter.support.openapi.StKeyManager;
import com.senter.support.openapi.StUhf.InterrogatorModelF.UmfAntennaConfig;
import com.senter.support.openapi.StUhf.InterrogatorModelF.UmfInventoriedTagInfo;
import com.senter.support.openapi.StUhf.InterrogatorModelF.UmfOnNewUiiInventoried;
import com.senter.support.openapi.StUhf.InterrogatorModelF.UmfResults.ResultOfInventory;
import com.senter.support.openapi.StUhf.UII;

public final class Activity0Inventory extends com.senter.demo.uhf.common.Activity0InventoryCommonAbstract {

    private final PartialWakeLocker mPartialWakeLocker =new PartialWakeLocker(this, TAG);
    StKeyManager.ShortcutKeyMonitor monitorScan = StKeyManager.ShortcutKeyMonitor.isShortcutKeyAvailable(StKeyManager.ShortcutKey.Scan) ?
            StKeyManager.getInstanceOfShortcutKeyMonitor(StKeyManager.ShortcutKey.Scan) : null;
    private boolean isScaning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getViews().setModes(InventoryMode.SingleStep, InventoryMode.AntiCollision);
        if (monitorScan != null) monitorScan.reset(this, listener, null);
    }

    private UII inventorySingleStep() {
        UmfInventoriedTagInfo tagInfo = App.uhfInterfaceAsModelF().inventorySingleTag();
        if (tagInfo == null || tagInfo.getUii() == null) {
            return null;
        }
        return tagInfo.getUii();
    }

    Thread continousThread;

    private boolean startInventoryAntiCollision() {
        {//make module invenotries no stop untill a stopOperation() method called
            UmfAntennaConfig preCfg = App.uhfInterfaceAsModelF().getAntennaConfig();
            if (preCfg == null) {
                preCfg = App.uhfInterfaceAsModelF().getAntennaConfig();
            }
            if (preCfg == null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast("Communication error");
                    }
                });
                return false;
            } else {
                UmfAntennaConfig newCfg = UmfAntennaConfig.newInstance(
                        preCfg.getPower(),
                        0,
                        0xFfFfFfFfL);
                App.uhfInterfaceAsModelF().setAntennaConfig(newCfg);
            }
        }

        getViews().enableInventoryButton(false);
        continousThread = new Thread() {
            @Override
            public void run() {
                getViews().enableInventoryButton(true);
                final ResultOfInventory rslt = App.uhfInterfaceAsModelF().inventory(new UmfOnNewUiiInventoried() {
                    @Override
                    public void onNewTagInventoried(UmfInventoriedTagInfo tagInfo) {

                        if (tagInfo != null) {
                            addNewUiiMassageToListview(tagInfo.getUii());
                        }
                    }
                });
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast(getString(R.string.idInventoryFinished_Colon) + "" + (rslt != null ? rslt.errorCode() : "null"));
                    }
                });
                mPartialWakeLocker.wakeLockRelease();
                getViews().setStateStoped();
            }
        };
        continousThread.start();
        return true;
    }

    private boolean stopInventory() {
        Thread thread = continousThread;
        for (; ; ) {
            if (thread.isAlive() == false) {
                return true;
            }
            App.stop();
        }
    }

    private void showSetQActivity() {
        startActivity(new Intent(Activity0Inventory.this, Activity7Settings.class));
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

    private final boolean startInventory() {
        boolean ret = false;
        switch (getViews().getSpecifiedInventoryMode()) {
            case SingleStep: {
                enableBtnInventory(false);
                new Thread() {
                    public void run() {
                        UII uii = null;

                        uii = inventorySingleStep();
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
            }
            case AntiCollision: {
                ret = startInventoryAntiCollision();
                if (ret == false) {
                    App.stop();
                }
                return ret;
            }
            default:
                break;
        }
        return ret;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (getViews().isStateInventoring()) {
                    App.stop();
                }
                break;
            case 222:
                if (getViews().isStateInventoring() == false) {
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
        super.onPrepareOptionsMenu(menu);

        menu.add(0, 3, 0, R.string.idSetQ);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 3) {
            if (getViews().isStateInventoring()) {
                showToast(getString(R.string.unPleaseStopInventoryFirst));
            } else {
                showSetQActivity();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    StKeyManager.ShortcutKeyMonitor.ShortcutKeyListener listener = new StKeyManager.ShortcutKeyMonitor.ShortcutKeyListener() {

        public void onKeyDown(int keycode,int repeatCount,StKeyManager.ShortcutKeyMonitor.ShortcutKeyEvent event){
            if(isScaning==false){
                uiOnInverntryButton();
                isScaning=true;
            }
        }

        public void onKeyUp(int keycode,int repeatCount,StKeyManager.ShortcutKeyMonitor.ShortcutKeyEvent event){
            isScaning=false;
        }

    };

    @Override
    protected void onResume() {
        super.onResume();
        if(monitorScan!=null){
            monitorScan.startMonitor();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(monitorScan!=null){
            monitorScan.stopMonitor();
        }
        if (isFinishing()){
            mPartialWakeLocker.wakeLockRelease();
        }
    }

    @Override
    protected void onDestroy() {
        mPartialWakeLocker.wakeLockRelease();
        super.onDestroy();
    }
}
