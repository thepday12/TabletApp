package com.senter.demo.uhf.modelB;

import android.os.Bundle;
import android.view.KeyEvent;

import com.senter.demo.uhf.App;
import com.senter.demo.uhf.util.Configuration;
import com.senter.demo.uhf.util.ConfigurationSettings;
import com.senter.support.openapi.StKeyManager;
import com.senter.support.openapi.StUhf.OnNewUiiInventoried;
import com.senter.support.openapi.StUhf.Q;
import com.senter.support.openapi.StUhf.UII;

public class Activity0Inventory extends com.senter.demo.uhf.common.Activity0InventoryCommonAbstractAB {
    StKeyManager.ShortcutKeyMonitor monitorScan = StKeyManager.ShortcutKeyMonitor.isShortcutKeyAvailable(StKeyManager.ShortcutKey.Scan) ?
            StKeyManager.getInstanceOfShortcutKeyMonitor(StKeyManager.ShortcutKey.Scan) : null;
    private boolean isScaning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configuration = new Configuration(Activity0Inventory.this, "Settings", MODE_PRIVATE);
        getViews().setModes(new InventoryMode[]{InventoryMode.SingleTag, InventoryMode.SingleStep, InventoryMode.AntiCollision});

        if (monitorScan != null) monitorScan.reset(this, listener, null);
    }


    protected UII startInventorySingleStep() {
        return App.uhfInterfaceAsModelB().inventorySingleStep();
    }

    @Override
    protected final boolean startInventorySingleTag() {
        return App.uhfInterfaceAsModelB().startInventorySingleTag(new OnNewUiiInventoried() {
            @Override
            public void onNewUiiReceived(UII uii) {
                if (uii != null) {
                    addNewUiiMassageToListview(uii);
                }
            }
        });
    }

    @Override
    protected final boolean startInventoryAntiCollision() {
        return App.uhfInterfaceAsModelB().startInventoryWithAntiCollision(getQ(), new OnNewUiiInventoried() {
            @Override
            public void onNewUiiReceived(UII uii) {
                if (uii != null) {
                    addNewUiiMassageToListview(uii);
                }
            }
        });
    }

    protected boolean stopInventory() {
        if (App.stop()) {
            return true;
        }
        return false;
    }


    private Configuration configuration;

    @Override
    protected Q getQ() {
        return Q.values()[configuration.getInt(ConfigurationSettings.key_Q, 3)];
    }

    @Override
    protected boolean setQ(Q q) {
        return configuration.setInt(ConfigurationSettings.key_Q, q.ordinal());
    }

    StKeyManager.ShortcutKeyMonitor.ShortcutKeyListener listener = new StKeyManager.ShortcutKeyMonitor.ShortcutKeyListener() {

        @Override
        public void onKeyDown(int keycode, int repeatCount, StKeyManager.ShortcutKeyMonitor.ShortcutKeyEvent event) {
            if(isScaning==false){
                uiOnInverntryButton();
                isScaning=true;
            }
        }

        @Override
        public void onKeyUp(int keycode, int repeatCount, StKeyManager.ShortcutKeyMonitor.ShortcutKeyEvent event) {
            isScaning=false;
        }

    };

    @Override
    protected void onResume() {
        super.onResume();
        if (monitorScan != null) monitorScan.startMonitor();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (monitorScan != null) monitorScan.stopMonitor();

    }
}
