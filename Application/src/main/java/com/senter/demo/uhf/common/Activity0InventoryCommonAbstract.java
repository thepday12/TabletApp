package com.senter.demo.uhf.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.ParcelUuid;
import android.provider.Settings;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.senter.demo.common.misc.Accompaniment;
import com.senter.demo.http.PostAddTagTask;
import com.senter.demo.http.TagObject;
import com.senter.demo.uhf.R;
import com.senter.demo.uhf.record.ActivityRecordsList;
import com.senter.demo.uhf.record.RecordRWer;
import com.senter.demo.uhf.util.DataTransfer;
import com.senter.demo.uhf.util.PartialWakeLocker;
import com.senter.support.openapi.StUhf.UII;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public abstract class Activity0InventoryCommonAbstract extends Activity_Abstract {
    public static final String TAG = "Activity0CmnAbstract";
    private final Accompaniment accompaniment = Accompaniment.newInstanceOfResource(Activity0InventoryCommonAbstract.this, R.raw.tag_inventoried);
    private Handler accompainimentsHandler;

    private final Runnable accompainimentRunnable = new Runnable() {
        @Override
        public void run() {
            accompaniment.start();
            accompainimentsHandler.removeCallbacks(this);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity20inventory);
        HandlerThread htHandlerThread = new HandlerThread("");
        htHandlerThread.start();
        accompainimentsHandler = new Handler(htHandlerThread.getLooper());
        views = new Views();


        // tuyen
        String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        BEACON_ID = android_id.substring(0, 12);


        init();
        setTxPowerSelectionListener();
        setTxModeSelectionListener();

        // tuyen end
    }

    @Override
    protected void onDestroy() {
        if (accompainimentsHandler != null) {
            accompainimentsHandler.getLooper().quit();
        }
        accompaniment.release();
        super.onDestroy();
    }

    /**
     * 显示扫描信息
     */


    // tuyennx
    private List<TagObject> listTags = new ArrayList<TagObject>();
    private static int Time_Repeat = 5000;

    protected final void addNewUiiMassageToListview(UII uii) {
        trigTagAccompainiment();
        String stringUii = DataTransfer.xGetString(uii.getBytes());
        views.recordsBoard.addMassage("Uii:" + stringUii);
        //tuyen start
        boolean have = false;
        long currentTime = System.currentTimeMillis();
        for(int i=0; i< listTags.size(); i++){
            TagObject tag = listTags.get(i);
            if(tag.getId().equals(stringUii)){
                have = true;
                if(currentTime - tag.getTime() > Time_Repeat){
                    tag.setTime(currentTime);
                    addTag(stringUii);
                }
                break;
            }
        }
        if(have == false){
            TagObject tag = new TagObject();
            tag.setId(stringUii);
            tag.setTime(currentTime);
            listTags.add(tag);
        }
        //tuyen end
    }


    private void addTag(String tagId){
        PostAddTagTask task = new PostAddTagTask(BEACON_ID, tagId);
        task.setOnAddTagTaskListener(new PostAddTagTask.AddTagTaskListener() {
            @Override
            public void onPreAddTagTask() {

            }

            @Override
            public void onAddTagTaskSuccess(String stringResult) {
                Log.d("tuyen", stringResult);
            }

            @Override
            public void onAddTagTaskFailure(int statusCode, String error) {
                Log.d("tuyen", error);
            }

            @Override
            public void transferred(long num) {

            }
        });
        task.execute();
    }

    protected final void addNewUiiMassageToListview(String msg) {
        trigTagAccompainiment();
        views.recordsBoard.addMassage(msg);
    }

    protected final void enableBtnInventory(final boolean enable) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                views.btnInventoryOrStop.setEnabled(enable);
            }
        });
    }

    protected abstract void uiOnInverntryButton();

    private void trigTagAccompainiment() {
        accompainimentsHandler.post(accompainimentRunnable);
    }

    /**
     * define three common menu items
     * 1、clear data on screen
     * 2、save data as record
     * 3、review record
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        menu.add(0, 0, 0, R.string.EmptyData);
        if (!views.isStateInventoring()){
            menu.add(0, 1, 0, R.string.SaveRecord);
            menu.add(0, 2, 0, R.string.CheckRecord);
        }
        return true;
    }

    /**
     * defined three common menu items
     * 1、clear data on screen
     * 2、save data as record
     * 3、review record
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0: {
                views.recordsBoard.clearMsg();
                break;
            }
            case 1: {
                if (views.recordsBoard.isEmpty() == false) {// save  if data exist
                    new RecodesSaver(Activity0InventoryCommonAbstract.this).showAskWhetherSaveRecodesDialog();
                } else {// tip user if data not exist
                    Toast.makeText(this, getString(R.string.WithoutDataToSave), Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case 2: {
                startActivity(new Intent(this, ActivityRecordsList.class));
                break;
            }
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private Views views;

    protected final Views getViews() {
        return views;
    }

    protected final class Views {
        private final Button btnInventoryOrStop = (Button) findViewById(R.id.idE20InventoryMain_llInvestory_btnStart);
        private final RecordsBoard recordsBoard = new RecordsBoard(Activity0InventoryCommonAbstract.this, findViewById(R.id.idE20InventoryActivity_inShow));
        private final RadioGroup rGroup = (RadioGroup) findViewById(R.id.idE20InventoryMain_llInvestory_rgRbs);
        private final View.OnClickListener mBtnInventoryClickLisener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uiOnInverntryButton();
            }
        };

        public Views() {
            rGroup.check(R.id.idE20InventoryMain_llInvestory_rbSingleStep);
            btnInventoryOrStop.setOnClickListener(mBtnInventoryClickLisener);
        }

        public Views setModes(InventoryMode... modes) {
            for (InventoryMode mode : modes) {
                switch (mode) {
                    case SingleTag: {
                        rGroup.setVisibility(View.VISIBLE);
                        findViewById(R.id.idE20InventoryMain_llInvestory_rbSingleTag).setVisibility(View.VISIBLE);
                        break;
                    }
                    case SingleStep: {
                        rGroup.setVisibility(View.VISIBLE);
                        findViewById(R.id.idE20InventoryMain_llInvestory_rbSingleStep).setVisibility(View.VISIBLE);
                        break;
                    }
                    case AntiCollision: {
                        rGroup.setVisibility(View.VISIBLE);
                        findViewById(R.id.idE20InventoryMain_llInvestory_rbAntiCollision).setVisibility(View.VISIBLE);
                        break;
                    }
                    case Custom: {
                        rGroup.setVisibility(View.GONE);
                    }
                    default:
                        break;
                }
            }
            return this;
        }

        public final InventoryMode getSpecifiedInventoryMode() {
            if (rGroup.getVisibility() != View.VISIBLE) {
                return InventoryMode.Custom;
            }
            if (((RadioButton) findViewById(R.id.idE20InventoryMain_llInvestory_rbSingleTag)).isChecked()) {
                return InventoryMode.SingleTag;
            } else if (((RadioButton) findViewById(R.id.idE20InventoryMain_llInvestory_rbSingleStep)).isChecked()) {
                return InventoryMode.SingleStep;
            } else if (((RadioButton) findViewById(R.id.idE20InventoryMain_llInvestory_rbAntiCollision)).isChecked()) {
                return InventoryMode.AntiCollision;
            } else {
                throw new IllegalStateException();
            }
        }

        public final void setStateStoped() {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    views.rGroup.setEnabled(true);
                    views.btnInventoryOrStop.setText(R.string.Inventory);
                }
            });
        }

        public final void enableInventoryButton(final boolean enable) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    btnInventoryOrStop.setEnabled(enable);
                }
            });
        }

        public final void setStateStarted() {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    views.rGroup.setEnabled(false);
                    views.btnInventoryOrStop.setText(R.string.Stop);
                }
            });
        }


        public final boolean isStateInventoring() {
            return rGroup.isEnabled() == false;
        }
    }

    private final class RecodesSaver {
        public static final String Preference_Name = "com_android_serialread_SerialPortRead";
        public static final String Preference_Prefix_Default = "Def";
        public static final String Preference_Prefix_Key = "Preference_Prefix_Key";
        private final Activity ownersActivity;

        public RecodesSaver(Activity activity) {
            ownersActivity = activity;
        }


        protected final void showAskWhereSaveRecodesDialog() {
            final Dialog dialog;
            LayoutInflater myInflater = LayoutInflater.from(ownersActivity);
            final View view = myInflater.inflate(R.layout.main_asksavepath2, null);

            AlertDialog.Builder dBuilder = new AlertDialog.Builder(ownersActivity).setView(view).setCancelable(false);
            {// show default prefix on ui
                String nString = getSharedPreferences(Preference_Name, 0).getString(Preference_Prefix_Key, Preference_Prefix_Default);
                ((EditText) view.findViewById(R.id.idAskSavePath_edittextPrefix)).setText(nString);
            }
            {// show time as middle name on ui

                Time time = new Time(Time.getCurrentTimezone());
                time.setToNow();
                String string = String.format(Locale.getDefault(), "%4d%02d%02d%02d%02d%02d", time.year, time.month + 1, time.monthDay, time.hour, time.minute, time.second);
                // SimpleDateFormat dfDateFormat=new SimpleDateFormat("yyyyMMddHHMMss");dfDateFormat.format(new Date());
                ((EditText) view.findViewById(R.id.idAskSavePath_edittextName)).setText(string);
            }
            dialog = dBuilder.create();
            View.OnClickListener lOk = new View.OnClickListener() {// yes
                @Override
                public void onClick(View v) {
                    String pathName = RecordRWer.path();
                    String preN = "";
                    String midN = "";
                    try {
                        preN = ((EditText) view.findViewById(R.id.idAskSavePath_edittextPrefix)).getText().toString();
                        midN = ((EditText) view.findViewById(R.id.idAskSavePath_edittextName)).getText().toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (preN == null || midN == null || preN.length() == 0 || midN.length() == 0) {
                        Toast.makeText(Activity0InventoryCommonAbstract.this, R.string.PrefixAndFileNameCannotEmpty, Toast.LENGTH_SHORT).show();
                    } else {
                        {// save current prefix as default for future.
                            getSharedPreferences(Preference_Name, 0).edit().putString(Preference_Prefix_Key, preN).commit();
                        }
                        pathName = pathName + preN + "-" + midN + RecordRWer.suffixRecord;
                        try {
                            RecordRWer.XmlOper.saveRecode2File(pathName, views.recordsBoard.getData());
                            Toast.makeText(Activity0InventoryCommonAbstract.this, R.string.SaveSuccessful, Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(Activity0InventoryCommonAbstract.this, R.string.SaveFailure, Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                        finish();
                    }
                }
            };
            View.OnClickListener lCancel = new View.OnClickListener() {// no
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    finish();
                }
            };

            ((Button) view.findViewById(R.id.idAskSavePath_btnOk)).setOnClickListener(lOk);
            ((Button) view.findViewById(R.id.idAskSavePath_btnCancel)).setOnClickListener(lCancel);

            dialog.show();
        }

        protected final void showAskWhetherSaveRecodesDialog() {
            AlertDialog.Builder aBuilder = new AlertDialog.Builder(ownersActivity).setTitle(R.string.AreYouSave).setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {// no
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            }).setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {// yes
                @Override
                public void onClick(DialogInterface dialog, int which) {// create a diaglog to allow the user to confirm whether or not to save the record
                    showAskWhereSaveRecodesDialog();
                    dialog.dismiss();
                }
            }).setOnKeyListener(new DialogInterface.OnKeyListener() {// back
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        return true;
                    }
                    return false;
                }
            }).setCancelable(false);
            aBuilder.create().show();
            return;
        }
    }

    public enum InventoryMode {
        SingleTag, SingleStep, AntiCollision, Custom;
    }

    //tuyen
    private String BEACON_ID;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if (resultCode == Activity.RESULT_OK) {
                init();
            } else {
                finish();
            }
        }
    }

    private BluetoothLeAdvertiser adv;
    private AdvertiseCallback advertiseCallback;
    private int txPowerLevel;
    private int advertiseMode;
    private static final int REQUEST_ENABLE_BLUETOOTH = 1;
    private static final byte FRAME_TYPE_UID = 0x00;
    private static final ParcelUuid SERVICE_UUID =
            ParcelUuid.fromString("0000FEAA-0000-1000-8000-00805F9B34FB");

    private void init() {
        BluetoothManager manager = (BluetoothManager) getApplicationContext().getSystemService(
                Context.BLUETOOTH_SERVICE);
        BluetoothAdapter btAdapter = manager.getAdapter();
        if (btAdapter == null) {
            showFinishingAlertDialog("Bluetooth Error", "Bluetooth not detected on device");
        } else if (!btAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            this.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BLUETOOTH);
        } else if (!btAdapter.isMultipleAdvertisementSupported()) {
            showFinishingAlertDialog("Not supported", "BLE advertising not supported on this device");
        } else {
            adv = btAdapter.getBluetoothLeAdvertiser();
            advertiseCallback = createAdvertiseCallback();
            startAdvertising();
        }
    }

    private void showFinishingAlertDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).show();
    }

    private AdvertiseCallback createAdvertiseCallback() {
        return new AdvertiseCallback() {
            @Override
            public void onStartFailure(int errorCode) {
                switch (errorCode) {
                    case ADVERTISE_FAILED_DATA_TOO_LARGE:
                        showToastAndLogError("ADVERTISE_FAILED_DATA_TOO_LARGE");
                        break;
                    case ADVERTISE_FAILED_TOO_MANY_ADVERTISERS:
                        showToastAndLogError("ADVERTISE_FAILED_TOO_MANY_ADVERTISERS");
                        break;
                    case ADVERTISE_FAILED_ALREADY_STARTED:
                        showToastAndLogError("ADVERTISE_FAILED_ALREADY_STARTED");
                        break;
                    case ADVERTISE_FAILED_INTERNAL_ERROR:
                        showToastAndLogError("ADVERTISE_FAILED_INTERNAL_ERROR");
                        break;
                    case ADVERTISE_FAILED_FEATURE_UNSUPPORTED:
                        showToastAndLogError("ADVERTISE_FAILED_FEATURE_UNSUPPORTED");
                        break;
                    default:
                        showToastAndLogError("startAdvertising failed with unknown error " + errorCode);
                        break;
                }
            }
        };
    }

    private void showToastAndLogError(String message) {
        showToast(message);
    }

    private void setTxPowerSelectionListener() {
        txPowerLevel = AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM;
    }

    private void setTxModeSelectionListener() {
        advertiseMode = AdvertiseSettings.ADVERTISE_MODE_BALANCED;
    }

    private void startAdvertising() {

        AdvertiseSettings advertiseSettings = new AdvertiseSettings.Builder()
                .setAdvertiseMode(advertiseMode)
                .setTxPowerLevel(txPowerLevel)
                .setConnectable(true)
                .build();

        byte[] serviceData = null;
        try {
            serviceData = buildServiceData();
        } catch (IOException e) {
        }

        AdvertiseData advertiseData = new AdvertiseData.Builder()
                .addServiceData(SERVICE_UUID, serviceData)
                .addServiceUuid(SERVICE_UUID)
                .setIncludeTxPowerLevel(false)
                .setIncludeDeviceName(false)
                .build();

        adv.startAdvertising(advertiseSettings, advertiseData, advertiseCallback);
    }
    private byte txPowerLevelToByteValue() {
        switch (txPowerLevel) {
            case AdvertiseSettings.ADVERTISE_TX_POWER_HIGH:
                return (byte) -16;
            case AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM:
                return (byte) -26;
            case AdvertiseSettings.ADVERTISE_TX_POWER_LOW:
                return (byte) -35;
            default:
                return (byte) -59;
        }
    }

    private byte[] buildServiceData() throws IOException {
        byte txPower = txPowerLevelToByteValue();
        byte[] namespaceBytes = toByteArray("00010203040506070809");
        byte[] instanceBytes = toByteArray(BEACON_ID);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(new byte[]{FRAME_TYPE_UID, txPower});
        os.write(namespaceBytes);
        os.write(instanceBytes);
        return os.toByteArray();
    }
    private byte[] toByteArray(String hexString) {
        // hexString guaranteed valid.
        int len = hexString.length();
        byte[] bytes = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            bytes[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return bytes;
    }
    //tuyen end
}
