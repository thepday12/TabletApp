package com.senter.demo.uhf.modelD2.iso18k6b;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.senter.demo.uhf.App;
import com.senter.demo.uhf.R;
import com.senter.demo.uhf.common.Activity_Abstract;
import com.senter.demo.uhf.common.DestinationTagSpecifics;
import com.senter.demo.uhf.common.DestinationTagSpecifics.TargetTagType;
import com.senter.demo.uhf.common.DestinationTagSpecifics.ProtocolType;
import com.senter.demo.uhf.common.DestinationTagSpecifics.PasswordType;
import com.senter.demo.uhf.common.RecordsBoard;
import com.senter.demo.uhf.util.DataTransfer;
import com.senter.support.openapi.StUhf.InterrogatorModelDs.UmdIso18k6bReadResult;
import com.senter.support.openapi.StUhf.UID;
import com.senter.support.openapi.StUhf.UII;

public class Activity1Read extends Activity_Abstract {
    private RecordsBoard recordsBoard;
    private DestinationTagSpecifics destinationTagSpecifics;

    private EditText etPtr;
    private EditText etCnt;
    private Button btnRead;


    protected final DestinationTagSpecifics getDestinationTagSpecifics() {
        return destinationTagSpecifics;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity21readactivity_6b);

        etPtr = (EditText) findViewById(R.id.idE21ReadActivity6b_etPointer);
        etPtr.setText("18");
        etCnt = (EditText) findViewById(R.id.idE21ReadActivity6b_etOrdedCount);

        btnRead = (Button) findViewById(R.id.idE21ReadActivity6b_btnRead);
        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtnRead();
            }
        });

        recordsBoard = new RecordsBoard(this, findViewById(R.id.idE21ReadActivity6b_inShow));
        destinationTagSpecifics = new DestinationTagSpecifics(this, findViewById(R.id.idE21ReadActivity6b_inDestTypes), ProtocolType.Iso18k6B, PasswordType.Non, TargetTagType.SpecifiedTag);
        destinationTagSpecifics.setOnReadyLisener(new DestinationTagSpecifics.OnDestOpTypesLisener() {
            @Override
            public void onReadyStateChanged(boolean now) {
                setViewEnable(btnRead, now);
            }
        });
    }


    protected final void enableBtnRead(final boolean enable) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btnRead.setEnabled(enable);
            }
        });
    }

    protected void onBtnRead() {
        if (destinationTagSpecifics.isOrderedUid() == true && destinationTagSpecifics.getDstTagUidIfOrdered() == null) {
            Toast.makeText(this, R.string.InputCorrectLabel, Toast.LENGTH_SHORT).show();
            return;
        }

        final int ptr;
        final int cnt;

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

        onRead(destinationTagSpecifics.getDstTagUidIfOrdered(), ptr, cnt);
    }

    private void onRead(UID uid, final int startIndex, final int length) {
        UmdIso18k6bReadResult r = App.uhfInterfaceAsModelD2().iso18k6bRead(uid, startIndex, length);
        if (r == null) {
            showToast("failed");
            return;
        }

        if (r.isOperationDone() == false) {
            showToast("failed");
            return;
        }

        addNewMassageToListview(uid, r.getReadData());
    }


    protected final void addNewMassageToListview(final UII uii, final byte[] data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                byte[] dataShow = data;
                if (data == null) {
                    dataShow = new byte[]{};
                }
                recordsBoard.addMassage(getString(R.string.Label) + (uii != null ? DataTransfer.xGetString(uii.getBytes()) : "unknown") + "\r\n" + getString(R.string.Length) + dataShow.length / 2 + " " + getString(R.string.Data) + DataTransfer.xGetString(dataShow));
            }
        });
    }

    protected final void addNewMassageToListview(final UID uid, final byte[] data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                byte[] dataShow = data;
                if (data == null) {
                    dataShow = new byte[]{};
                }
                recordsBoard.addMassage("UID:" + (uid != null ? DataTransfer.xGetString(uid.getBytes()) : "unknown") + "\r\n" + getString(R.string.Length) + dataShow.length / 2 + " " + getString(R.string.Data) + DataTransfer.xGetString(dataShow));
            }
        });
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
