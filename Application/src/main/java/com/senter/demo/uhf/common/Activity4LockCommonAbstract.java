package com.senter.demo.uhf.common;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.senter.demo.uhf.R;
import com.senter.demo.uhf.common.DestinationTagSpecifics.ProtocolType;
import com.senter.demo.uhf.common.DestinationTagSpecifics.PasswordType;


public abstract class Activity4LockCommonAbstract extends Activity_Abstract {
    private RecordsBoard recordsBoard;
    private DestinationTagSpecifics destinationTagSpecifics;

    private Button btnLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity24lockactivity);

        btnLock = (Button) findViewById(R.id.idE24LockActivity_btnLock);
        btnLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtnLock();
            }
        });

        recordsBoard = new RecordsBoard(this, findViewById(R.id.idE24LockActivity_inShow));
        destinationTagSpecifics = new DestinationTagSpecifics(this, findViewById(R.id.idE24LockActivity_inDestTypes), ProtocolType.Iso18k6C, PasswordType.Apwd, getDestinationType());
        destinationTagSpecifics.clearApwdAndKpwd();

        destinationTagSpecifics.setOnReadyLisener(new DestinationTagSpecifics.OnDestOpTypesLisener() {
            @Override
            public void onReadyStateChanged(boolean now) {
                setViewEnable(btnLock, now);
            }
        });
    }

    protected abstract DestinationTagSpecifics.TargetTagType[] getDestinationType();

    private void onBtnLock() {
        if (destinationTagSpecifics.isOrderedUii() == true && destinationTagSpecifics.getDstTagUiiIfOrdered() == null) {
            Toast.makeText(this, R.string.InputCorrectLabel, Toast.LENGTH_SHORT).show();
            return;
        }

        onLock();
    }

    protected final DestinationTagSpecifics getDestinationTagSpecifics() {
        return destinationTagSpecifics;
    }

    protected final void addNewMassageToListview(String msg) {
        recordsBoard.addMassage(msg);
    }


    protected abstract void onLock();


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
