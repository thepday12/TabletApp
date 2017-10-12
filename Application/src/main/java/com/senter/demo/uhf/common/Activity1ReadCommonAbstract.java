package com.senter.demo.uhf.common;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.os.Environment;
import android.os.ParcelUuid;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.senter.demo.http.GetAddTagTask;
import com.senter.demo.http.PostAddTagTask;
import com.senter.demo.uhf.Activity0ModuleSelection;
import com.senter.demo.uhf.R;
import com.senter.demo.uhf.common.DestinationTagSpecifics.ProtocolType;
import com.senter.demo.uhf.common.DestinationTagSpecifics.PasswordType;
import com.senter.demo.uhf.util.DataTransfer;
import com.senter.support.openapi.StUhf.Bank;
import com.senter.support.openapi.StUhf.UII;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


public abstract class Activity1ReadCommonAbstract extends Activity_Abstract {
    private RecordsBoard recordsBoard;
    private DestinationTagSpecifics destinationTagSpecifics;

    private Spinner spnrBank;
    private EditText etPtr;
    private EditText etCnt;
    private Button btnRead;


    protected final DestinationTagSpecifics getDestinationTagSpecifics() {
        return destinationTagSpecifics;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity21readactivity);

        spnrBank = (Spinner) findViewById(R.id.idE21ReadActivity_spinnerBanks);
        etPtr = (EditText) findViewById(R.id.idE21ReadActivity_etPointer);
        etCnt = (EditText) findViewById(R.id.idE21ReadActivity_etOrdedCount);

        spnrBank.setSelection(spnrBank.getCount() - 1);

        btnRead = (Button) findViewById(R.id.idE21ReadActivity_btnRead);
        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtnRead();
            }
        });

        recordsBoard = new RecordsBoard(this, findViewById(R.id.idE21ReadActivity_inShow));
        destinationTagSpecifics = new DestinationTagSpecifics(this, findViewById(R.id.idE21ReadActivity_inDestTypes), ProtocolType.Iso18k6C, PasswordType.Apwd, getDestinationType());
        destinationTagSpecifics.setOnReadyLisener(new DestinationTagSpecifics.OnDestOpTypesLisener() {
            @Override
            public void onReadyStateChanged(boolean now) {
                setViewEnable(btnRead, now);
            }
        });

        // tuyen
        String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        BEACON_ID = android_id.substring(0, 12);


        init();
        setTxPowerSelectionListener();
        setTxModeSelectionListener();

        // tuyen end
    }


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    // tuyen start
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

    private void stopAdvertising() {
//        Log.i(TAG, "Stopping ADV");
        adv.stopAdvertising(advertiseCallback);
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

    private boolean isValidHex(String s, int len) {
        return !(s == null || s.isEmpty()) && (s.length() / 2) == len && s.matches("[0-9A-F]+");
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

    private String randomHexString(int length) {
        byte[] buf = new byte[length];
        new Random().nextBytes(buf);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(String.format("%02X", buf[i]));
        }
        return stringBuilder.toString();
    }



    // tuyen end


    protected abstract DestinationTagSpecifics.TargetTagType[] getDestinationType();

    protected final void enableBtnRead(final boolean enable) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btnRead.setEnabled(enable);
            }
        });
    }

    public String convertTime(long time){
        Date date = new Date(time);
        Format format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
        return format.format(date);
    }
    private void onBtnRead() {

        if (destinationTagSpecifics.isOrderedUii() == true && destinationTagSpecifics.getDstTagUiiIfOrdered() == null) {
            Toast.makeText(this, R.string.InputCorrectLabel, Toast.LENGTH_SHORT).show();
            return;
        }

        final Bank bank;
        final int ptr;
        final int cnt;

        bank = Bank.ValueOf(spnrBank.getSelectedItemPosition());

        try {
            ptr = Integer.valueOf(etPtr.getText().toString());
        } catch (Exception e) {
            Toast.makeText(this, R.string.InputCorrectReadAddr, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            cnt = Integer.valueOf(etCnt.getText().toString());
        } catch (Exception e) {
            Toast.makeText(this, R.string.InputCorrectReadLength, Toast.LENGTH_SHORT).show();
            return;
        }

        onRead(bank, ptr, cnt);

    }

    protected abstract void onRead(final Bank bank, final int ptr, final int cnt);

    private void saveFile(String data) {
        File log = new File(Environment.getExternalStorageDirectory(), "UhfDataLog.txt");
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(log.getAbsolutePath(), log.exists()));
            out.write(data);
            out.write("\n");
            out.close();
        } catch (IOException e) {
            Log.e("AppService", "Exception appending to log file", e);
        }
    }
    protected final void addNewMassageToListview(final UII uii, final byte[] data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                byte[] dataShow = data;
                if (data == null) {
                    dataShow = new byte[]{};
                }
                String save = "uii.getBytes():" + DataTransfer.xGetString(uii.getBytes()) + "\r\n"
                        + "uii.hashCode():" + uii.hashCode() + "\r\n"
                        + "uii.getEpc().getBytes(): " + DataTransfer.xGetString(uii.getEpc().getBytes()) + "\r\n"
                        + "uii.getEpc().hashCode(): " + uii.getEpc().hashCode() + "\r\n"
                        + "uii.getPc().getBytes(): " + DataTransfer.xGetString(uii.getPc().getBytes()) + "\r\n"
                        + "uii.getPc().hashCode(): " + uii.getPc().hashCode() + "\r\n";
                saveFile(save);
                recordsBoard.addMassage(getString(R.string.Label) + (uii != null ? DataTransfer.xGetString(uii.getBytes()) : "unknown") + "\r\n" + getString(R.string.Length) + dataShow.length / 2 + " " + getString(R.string.Data) + DataTransfer.xGetString(dataShow));
                addTag(DataTransfer.xGetString(uii.getBytes()));
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
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

    @Override
    public final boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        menu.add(0, 0, 0, R.string.EmptyData);
        return true;
    }

    @Override
    public final boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0: {
                recordsBoard.clearMsg();
                break;
            }
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
