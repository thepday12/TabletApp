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
import com.senter.support.openapi.StUhf.InterrogatorModelDs.UmdIso18k6bWriteResult;
import com.senter.support.openapi.StUhf.UID;

public class Activity2Write extends Activity_Abstract {
    @SuppressWarnings("unused")
    private static final String Tag = "Activity2Write";
    private RecordsBoard recordsBoard;
    private DestinationTagSpecifics destinationTagSpecifics;

    private EditText ptrEditText;
    private EditText writeEditText;
    private Button writeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity22writeactivity_6b);

        ptrEditText = (EditText) findViewById(R.id.idE22WriteActivity6b_etPointer);
        ptrEditText.setText("18");
        writeEditText = (EditText) findViewById(R.id.idE22WriteActivity6b_etDataForWrite);

        writeButton = (Button) findViewById(R.id.idE22WriteActivity6b_btnWrite);
        writeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtnWrite();
            }
        });

        recordsBoard = new RecordsBoard(this, findViewById(R.id.idE22WriteActivity6b_inShow));
        destinationTagSpecifics = new DestinationTagSpecifics(this, findViewById(R.id.idE22WriteActivity6b_inDestTypes), ProtocolType.Iso18k6B, PasswordType.Non, TargetTagType.SpecifiedTag);
        destinationTagSpecifics.setOnReadyLisener(new DestinationTagSpecifics.OnDestOpTypesLisener() {
            @Override
            public void onReadyStateChanged(boolean now) {
                setViewEnable(writeButton, now);
            }
        });
    }

    private void onBtnWrite() {
        if (destinationTagSpecifics.isOrderedUid() == true && destinationTagSpecifics.getDstTagUidIfOrdered() == null) {
            Toast.makeText(this, R.string.InputCorrectLabel, Toast.LENGTH_SHORT).show();
            return;
        }

        final int ptr;
        final byte[] data;

        try {
            ptr = Integer.valueOf(ptrEditText.getText().toString());
        } catch (Exception e) {
            Toast.makeText(this, R.string.InputCorrectReadAddr, Toast.LENGTH_SHORT).show();
            return;
        }

        String dataStr = writeEditText.getText().toString();

        if (dataStr.length() == 0 || dataStr.length() % 4 != 0) {
            Toast.makeText(this, R.string.InputPrompt, Toast.LENGTH_SHORT).show();
            return;
        }
        data = new byte[dataStr.length() / 2];
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) (Integer.valueOf(dataStr.substring(2 * i, 2 * i + 2), 16) & 0xff);
        }

        onWrite(destinationTagSpecifics.getDstTagUidIfOrdered(), ptr, data);
    }

    protected void onWrite(UID uid, int startAddress, byte[] data) {
        UmdIso18k6bWriteResult r = App.uhfInterfaceAsModelD2().iso18k6bWrite(uid, startAddress, data);
        if (r == null) {
            showToast("failed");
            return;
        }

        if (r.isOperationDone() == false) {
            showToast("failed");
            return;
        }

        addNewMassageToListview(uid, r.getWrittenCount());
    }


    protected final void addNewMassageToListview(final UID uid, final int writen) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recordsBoard.addMassage("UID:" + (uid != null ? DataTransfer.xGetString(uid.getBytes()) : "unknown") + "\r\n" + getString(R.string.Length) + writen);
            }
        });
    }


    protected final void enableBtnWrite(final boolean enable) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                writeButton.setEnabled(enable);
            }
        });
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
