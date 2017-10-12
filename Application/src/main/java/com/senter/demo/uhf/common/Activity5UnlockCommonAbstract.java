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

public abstract class Activity5UnlockCommonAbstract extends Activity_Abstract {
    RecordsBoard recordsBoard;
    DestinationTagSpecifics destinationTagSpecifics;

    Button btnUnlock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity25unlockactivity);

        btnUnlock = (Button) findViewById(R.id.idE25UnlockActivity_btnUnlock);
        btnUnlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    onBtnUnlock();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        recordsBoard = new RecordsBoard(this, findViewById(R.id.idE25UnlockActivity_inShow));
        destinationTagSpecifics = new DestinationTagSpecifics(this, findViewById(R.id.idE25UnlockActivity_inDestTypes), ProtocolType.Iso18k6C, PasswordType.Apwd, getDestinationType());
        destinationTagSpecifics.clearApwdAndKpwd();

        destinationTagSpecifics.setOnReadyLisener(new DestinationTagSpecifics.OnDestOpTypesLisener() {
            @Override
            public void onReadyStateChanged(boolean now) {
                setViewEnable(btnUnlock, now);
            }
        });
    }

    protected final DestinationTagSpecifics getDestinationTagSpecifics() {
        return destinationTagSpecifics;
    }

    protected abstract DestinationTagSpecifics.TargetTagType[] getDestinationType();

    private final void onBtnUnlock() throws InterruptedException {
        if (destinationTagSpecifics.isOrderedUii() == true && destinationTagSpecifics.getDstTagUiiIfOrdered() == null) {
            Toast.makeText(this, R.string.InputCorrectLabel, Toast.LENGTH_SHORT).show();
            return;
        }
        onUnlock();
    }

    protected abstract void onUnlock();


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
