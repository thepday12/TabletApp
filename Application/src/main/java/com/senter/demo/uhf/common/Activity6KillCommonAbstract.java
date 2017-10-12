package com.senter.demo.uhf.common;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.senter.demo.uhf.App;
import com.senter.demo.uhf.R;
import com.senter.demo.uhf.common.DestinationTagSpecifics.ProtocolType;
import com.senter.demo.uhf.common.DestinationTagSpecifics.PasswordType;


public abstract class Activity6KillCommonAbstract extends Activity_Abstract {

    private RecordsBoard recordsBoard;
    private DestinationTagSpecifics destinationTagSpecifics;

    private Button btnKill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity26killactivity);

        btnKill = (Button) findViewById(R.id.idE26KillActivity_btnKill);
        btnKill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    onBtnKill();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        recordsBoard = new RecordsBoard(this, findViewById(R.id.idE26KillActivity_inShow));

        switch (App.uhfInterfaceAsModel()) {
            case InterrogatorModelA:
            case InterrogatorModelB:
            case InterrogatorModelD1:
            case InterrogatorModelD2:
                destinationTagSpecifics = new DestinationTagSpecifics(this, findViewById(R.id.idE26KillActivity_inDestTypes), ProtocolType.Iso18k6C, PasswordType.Kpwd, getDestinationType());
                break;
            case InterrogatorModelC:
                destinationTagSpecifics = new DestinationTagSpecifics(this, findViewById(R.id.idE26KillActivity_inDestTypes), ProtocolType.Iso18k6C, PasswordType.ApwdAndKpwd, getDestinationType());
                break;
            default:
                throw new IllegalArgumentException();
        }
        destinationTagSpecifics.clearApwdAndKpwd();
        destinationTagSpecifics.setOnReadyLisener(new DestinationTagSpecifics.OnDestOpTypesLisener() {
            @Override
            public void onReadyStateChanged(boolean now) {
                setViewEnable(btnKill, now);
            }
        });
    }


    protected abstract DestinationTagSpecifics.TargetTagType[] getDestinationType();

    private void onBtnKill() throws InterruptedException {
        if (destinationTagSpecifics.isOrderedUii() == true && destinationTagSpecifics.getDstTagUiiIfOrdered() == null) {
            Toast.makeText(this, R.string.InputCorrectLabel, Toast.LENGTH_SHORT).show();
            return;
        }

        onKill();
    }

    protected abstract void onKill();


    protected final void addNewMassageToListview(String string) {
        recordsBoard.addMassage(getString(R.string.DestructFailure));
    }

    protected final DestinationTagSpecifics getDestinationTagSpecifics() {
        return destinationTagSpecifics;
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
