package com.senter.demo.uhf.modelA;

import com.senter.demo.common.misc.ActivityHelper;
import com.senter.demo.uhf.App;
import com.senter.demo.uhf.R;
import com.senter.demo.uhf.common.ExceptionForToast;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class Activity_FunctionSelection extends Activity {
    public static final String Tag = "Main2Activity";

    private ActivityHelper<Activity_FunctionSelection> ah = new ActivityHelper<Activity_FunctionSelection>(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            App.uhfInit();
        } catch (ExceptionForToast e) {
            e.printStackTrace();
            App.uhfClear();
            App.appCfgSaveModelClear();
            ah.showToastShort(e.getMessage());
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.exit(0);
    }

    @Override
    protected void onResume() {
        new Views();
        super.onResume();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        menu.add(0, 0, 0, "clear remembered model");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getGroupId()) {
            case 0: {
                switch (item.getItemId()) {
                    case 0: {
                        switch (item.getOrder()) {
                            case 0: {
                                App.uhfUninit();
                                App.uhfClear();
                                App.appCfgSaveModelClear();
                                System.exit(0);
                                finish();
                                return true;
                            }
                            default:
                                break;
                        }
                        break;
                    }
                    default:
                        break;
                }
                break;
            }
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class Views {
        public Views() {
            setContentView(R.layout.activity1_function_selection);

            LinearLayout ll = (LinearLayout) findViewById(R.id.idMain2Activity_glApps_inInventory);
            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onIso6C_0Inventory();
                }
            });

            ll = (LinearLayout) findViewById(R.id.idMain2Activity_glApps_inRead);
            ll.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onIso6C_1Read();
                }
            });

            ll = (LinearLayout) findViewById(R.id.idMain2Activity_glApps_inWrite);
            ll.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onIso6C_2Write();
                }
            });
            ll = (LinearLayout) findViewById(R.id.idMain2Activity_glApps_inErease);
            ll.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onIso6C_3Erase();
                }
            });
            ll = (LinearLayout) findViewById(R.id.idMain2Activity_glApps_inLock);
            ll.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onIso6C_4Lock();
                }
            });
            ll = (LinearLayout) findViewById(R.id.idMain2Activity_glApps_inUnlock);
            ll.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onIso6C_5UnLock();
                }
            });
            ll = (LinearLayout) findViewById(R.id.idMain2Activity_glApps_inKill);
            ll.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onIso6C_6Kill();
                }
            });
            ll = (LinearLayout) findViewById(R.id.idMain2Activity_glApps_inSettings);
            ll.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onActivitySettings();
                }
            });
        }
    }

    private void onIso6C_0Inventory() {
        ah.startActivity(com.senter.demo.uhf.modelA.Activity0Inventory.class);
    }

    private void onIso6C_1Read() {
        ah.startActivity(com.senter.demo.uhf.modelA.Activity1Read.class);
    }

    private void onIso6C_2Write() {
        ah.startActivity(com.senter.demo.uhf.modelA.Activity2Write.class);
    }

    private void onIso6C_3Erase() {
        ah.startActivity(com.senter.demo.uhf.modelA.Activity3Erase.class);
    }

    private void onIso6C_4Lock() {
        ah.startActivity(com.senter.demo.uhf.modelA.Activity4Lock.class);
    }

    private void onIso6C_5UnLock() {
        ah.startActivity(com.senter.demo.uhf.modelA.Activity5Unlock.class);
    }

    private void onIso6C_6Kill() {
        ah.startActivity(com.senter.demo.uhf.modelA.Activity6Kill.class);
    }

    private void onActivitySettings() {
        ah.startActivity(com.senter.demo.uhf.modelA.Activity7Settings.class);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            App.uhfUninit();
            finish();

            android.os.Process.killProcess(android.os.Process.myPid());
        }
        return super.onKeyDown(keyCode, event);
    }
}
