package com.senter.demo.uhf.modelD2;

import com.senter.demo.common.misc.ActivityHelper;
import com.senter.demo.uhf.App;
import com.senter.demo.uhf.R;
import com.senter.demo.uhf.common.ExceptionForToast;
import com.senter.demo.uhf.modelD2.ConfigurationSettingsOfModelD2;
import com.senter.demo.uhf.modelD2.ConfigurationSettingsOfModelD2.Protocol;

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

    private final ConfigurationSettingsOfModelD2 mConfigurationSettingsOfModelD2 = ConfigurationSettingsOfModelD2.getInstance();

    private Protocol mCurrentProtocolOfModelD2 = null;

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
        onCreateInitViews();
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
            if ((mConfigurationSettingsOfModelD2.usingProtocol() == Protocol.I8k6c) && (mCurrentProtocolOfModelD2 == Protocol.I8k6c)) {
                return;
            } else if ((mConfigurationSettingsOfModelD2.usingProtocol() == Protocol.I8k6b) && (mCurrentProtocolOfModelD2 == Protocol.I8k6b)) {
                return;
            } else {
                //through down
            }

            setContentView(R.layout.activity1_function_selection_d2);

            View tv6CTag, tv6BTag;
            View glApps6C, glApps6B;

            LinearLayout ll;

            tv6CTag = findViewById(R.id.idMain2ActivityD26C_tv6CTag);
            tv6BTag = findViewById(R.id.idMain2ActivityD26C_tv6BTag);
            glApps6C = findViewById(R.id.idMain2ActivityD26C_glApps);
            glApps6B = findViewById(R.id.idMain2ActivityD26B_glApps);

            if (mConfigurationSettingsOfModelD2.usingProtocol() == Protocol.I8k6c) {// 6C
                mCurrentProtocolOfModelD2 = Protocol.I8k6c;

                tv6BTag.setVisibility(View.GONE);
                glApps6B.setVisibility(View.GONE);

                tv6CTag.setVisibility(View.VISIBLE);
                glApps6C.setVisibility(View.VISIBLE);

                ll = (LinearLayout) findViewById(R.id.idMain2ActivityD2_glApps_inInventory);
                ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onIso6C_0Inventory();
                    }
                });

                ll = (LinearLayout) findViewById(R.id.idMain2ActivityD2_glApps_inRead);
                ll.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onIso6C_1Read();
                    }
                });

                ll = (LinearLayout) findViewById(R.id.idMain2ActivityD2_glApps_inWrite);
                ll.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onIso6C_2Write();
                    }
                });
                ll = (LinearLayout) findViewById(R.id.idMain2ActivityD2_glApps_inLock);
                ll.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onIso6C_4Lock();
                    }
                });
                ll = (LinearLayout) findViewById(R.id.idMain2ActivityD2_glApps_inKill);
                ll.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onIso6C_6Kill();
                    }
                });

            } else if (mConfigurationSettingsOfModelD2.usingProtocol() == Protocol.I8k6b) {// 6B

                mCurrentProtocolOfModelD2 = Protocol.I8k6b;
                tv6BTag.setVisibility(View.VISIBLE);
                glApps6B.setVisibility(View.VISIBLE);

                tv6CTag.setVisibility(View.GONE);
                glApps6C.setVisibility(View.GONE);

                ll = (LinearLayout) findViewById(R.id.idMain2ActivityD26b_glApps_inInventory);
                ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onIso6B_0Inventory();
                    }
                });

                ll = (LinearLayout) findViewById(R.id.idMain2ActivityD26b_glApps_inRead);
                ll.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onIso6B_1Read();
                    }
                });

                ll = (LinearLayout) findViewById(R.id.idMain2ActivityD26b_glApps_inWrite);
                ll.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onIso6B_2Write();
                    }
                });

                ll = (LinearLayout) findViewById(R.id.idMain2ActivityD26b_glApps_inLock);
                ll.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onIso6B_3Lock();
                    }
                });
                ll = (LinearLayout) findViewById(R.id.idMain2ActivityD26b_glApps_inUnlock);
                ll.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onIso6B_4QuearyLock();
                    }
                });
            } else {
                throw new IllegalStateException();
            }

            ll = (LinearLayout) findViewById(R.id.idMain2ActivityD2_glApps_inSettings);
            ll.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onActivitySettings();
                }
            });
        }
    }

    private void onCreateInitViews() {
        new Views();
    }

    private void onIso6C_0Inventory() {
        ah.startActivity(com.senter.demo.uhf.modelD2.Activity0Inventory.class);
    }

    private void onIso6C_1Read() {
        ah.startActivity(com.senter.demo.uhf.modelD2.Activity1Read.class);
    }

    private void onIso6C_2Write() {
        ah.startActivity(com.senter.demo.uhf.modelD2.Activity2Write.class);
    }

    private void onIso6C_4Lock() {
        ah.startActivity(com.senter.demo.uhf.modelD2.Activity3Lock.class);
    }

    private void onIso6C_6Kill() {
        ah.startActivity(com.senter.demo.uhf.modelD2.Activity4Kill.class);
    }

    private void onActivitySettings() {
        ah.startActivity(com.senter.demo.uhf.modelD2.Activity5Settings.class);
    }

    private void onIso6B_0Inventory() {
        ah.startActivity(com.senter.demo.uhf.modelD2.iso18k6b.Activity0Inventory.class);
    }

    private void onIso6B_1Read() {
        ah.startActivity(com.senter.demo.uhf.modelD2.iso18k6b.Activity1Read.class);
    }

    private void onIso6B_2Write() {
        ah.startActivity(com.senter.demo.uhf.modelD2.iso18k6b.Activity2Write.class);
    }

    private void onIso6B_3Lock() {
        ah.startActivity(com.senter.demo.uhf.modelD2.iso18k6b.Activity3Lock.class);
    }

    private void onIso6B_4QuearyLock() {
        ah.startActivity(com.senter.demo.uhf.modelD2.iso18k6b.Activity4QueryLock.class);
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
