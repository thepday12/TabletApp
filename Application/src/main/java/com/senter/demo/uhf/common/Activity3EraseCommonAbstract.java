package com.senter.demo.uhf.common;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.senter.demo.uhf.R;
import com.senter.demo.uhf.common.DestinationTagSpecifics.ProtocolType;
import com.senter.demo.uhf.common.DestinationTagSpecifics.PasswordType;
import com.senter.demo.uhf.util.DataTransfer;
import com.senter.support.openapi.StUhf.AccessPassword;
import com.senter.support.openapi.StUhf.Bank;
import com.senter.support.openapi.StUhf.Result.EraseResult;
import com.senter.support.openapi.StUhf.UII;


public abstract class Activity3EraseCommonAbstract extends Activity_Abstract {

    RecordsBoard recordsBoard;
    DestinationTagSpecifics destinationTagSpecifics;

    Spinner bankSpinner;
    EditText etPtr;
    EditText etCnt;
    Button btnErase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity23eraseactivity);

        bankSpinner = (Spinner) findViewById(R.id.idE23EraseActivity_spinnerBanks);
        etPtr = (EditText) findViewById(R.id.idE23EraseActivity_etPointer);
        etCnt = (EditText) findViewById(R.id.idE23EraseActivity_etCount);

        bankSpinner.setSelection(bankSpinner.getCount() - 1);

        btnErase = (Button) findViewById(R.id.idE23EraseActivity_btnErease);
        btnErase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                erase();
            }
        });

        recordsBoard = new RecordsBoard(this, findViewById(R.id.idE23EraseActivity_inShow));
        destinationTagSpecifics = new DestinationTagSpecifics(this, findViewById(R.id.idE23EraseActivity_inDestTypes), ProtocolType.Iso18k6C, PasswordType.Apwd, getDestinationType());
        destinationTagSpecifics.setOnReadyLisener(new DestinationTagSpecifics.OnDestOpTypesLisener() {
            @Override
            public void onReadyStateChanged(boolean now) {
                setViewEnable(btnErase, now);
            }
        });
    }

    protected abstract DestinationTagSpecifics.TargetTagType[] getDestinationType();

    private void erase() {

        final int ptr;
        final byte cnt;

        final Bank bank = Bank.ValueOf(bankSpinner.getSelectedItemPosition());

        try {
            ptr = Integer.valueOf(etPtr.getText().toString());
        } catch (Exception e) {
            etPtr.setText("");
            Toast.makeText(this, R.string.InputCorrectReadAddr, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            cnt = (byte) (Integer.valueOf(etCnt.getText().toString()) & 0xff);
        } catch (Exception e) {
            etCnt.setText("");
            Toast.makeText(this, R.string.InputCorrectErasNumber, Toast.LENGTH_SHORT).show();
            return;
        }

        setViewEnable(btnErase, false);
        new Thread() {
            public void run() {
                try {
                    EraseResult rr = null;

                    if (destinationTagSpecifics.isOrderedUii()) {
                        rr = eraseDataByUii(destinationTagSpecifics.getAccessPassword(), bank, ptr, cnt, destinationTagSpecifics.getDstTagUiiIfOrdered());

                    } else {
                        rr = eraseDataFromSingleTag(destinationTagSpecifics.getAccessPassword(), bank, ptr, cnt);
                    }
                    if (rr == null || rr.isSucceeded() == false) {
                        showToast(getString(R.string.EraDataFailure), Toast.LENGTH_SHORT);
                    } else {
                        final EraseResult r = rr;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recordsBoard.addMassage(getString(R.string.EraDataSuccessful) + DataTransfer.xGetString(r.getUii().getBytes()));
                            }
                        });
                    }
                } catch (ExceptionForToast e) {
                    showToast(e.getMessage());
                }
                setViewEnable(btnErase, true);
            }

            ;
        }.start();

    }

    protected abstract EraseResult eraseDataByUii(AccessPassword apwd, Bank bank, int offset, byte length, UII uii) throws ExceptionForToast;
    protected abstract EraseResult eraseDataFromSingleTag(AccessPassword apwd, Bank bank, int offset, byte length) throws ExceptionForToast;


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
