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
import com.senter.support.openapi.StUhf.InterrogatorModelDs.UmdIso18k6bQueryLockResult;
import com.senter.support.openapi.StUhf.UID;

public class Activity4QueryLock extends Activity_Abstract {
    private RecordsBoard recordsBoard;
    private DestinationTagSpecifics destinationTagSpecifics;

    private Button btnLock;
    private EditText ptrEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity24lockactivity_6b);
        setTitle(R.string.idRequestLock);

        btnLock = (Button) findViewById(R.id.idE24LockActivity6b_btnLock);
        btnLock.setText(R.string.get);
        btnLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtnLock();
            }
        });
        ptrEditText = (EditText) findViewById(R.id.idE24LockActivity6b_etPointer);
        ptrEditText.setText("18");

        recordsBoard = new RecordsBoard(this, findViewById(R.id.idE24LockActivity6b_inShow));
        destinationTagSpecifics = new DestinationTagSpecifics(this, findViewById(R.id.idE24LockActivity6b_inDestTypes), ProtocolType.Iso18k6B, PasswordType.Non, TargetTagType.SpecifiedTag);
        destinationTagSpecifics.clearApwdAndKpwd();

        destinationTagSpecifics.setOnReadyLisener(new DestinationTagSpecifics.OnDestOpTypesLisener() {
            @Override
            public void onReadyStateChanged(boolean now) {
                setViewEnable(btnLock, now);
            }
        });
    }


    private void onBtnLock() {
        if (destinationTagSpecifics.isOrderedUid() == true && destinationTagSpecifics.getDstTagUidIfOrdered() == null) {
            Toast.makeText(this, R.string.InputCorrectLabel, Toast.LENGTH_SHORT).show();
            return;
        }

        final int ptr;

        try {
            ptr = Integer.valueOf(ptrEditText.getText().toString());
        } catch (Exception e) {
            Toast.makeText(this, R.string.InputCorrectReadAddr, Toast.LENGTH_SHORT).show();
            return;
        }
        onQuaryLock(destinationTagSpecifics.getDstTagUidIfOrdered(), ptr);
    }

    protected final DestinationTagSpecifics getDestinationTagSpecifics() {
        return destinationTagSpecifics;
    }

    protected final void addNewMassageToListview(String msg) {
        recordsBoard.addMassage(msg);
    }


    protected void onQuaryLock(UID uid, int lockAddress) {
        UmdIso18k6bQueryLockResult r = App.uhfInterfaceAsModelD2().iso18k6bQueryLock(uid, lockAddress);
        if (r == null) {
            showToast("failed");
            return;
        }

        if (r.isOperationDone() == false) {
            showToast("failed");
            return;
        }
        addNewMassageToListview("uid:" + DataTransfer.xGetString(uid.getBytes()) + "\r\n" + "address:" + lockAddress + " status:" + r.getLockStatus().name());
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        menu.add(0, 0, 0, R.string.EmptyData);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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








