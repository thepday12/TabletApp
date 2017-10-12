package com.senter.demo.uhf.modelD1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.senter.demo.uhf.App;
import com.senter.demo.uhf.R;
import com.senter.demo.uhf.common.Activity4LockCommonAbstract;
import com.senter.demo.uhf.common.DestinationTagSpecifics.TargetTagType;
import com.senter.demo.uhf.util.DataTransfer;
import com.senter.support.openapi.StUhf.InterrogatorModelDs.UmdEpcMatchSetting;
import com.senter.support.openapi.StUhf.InterrogatorModelDs.UmdErrorCode;
import com.senter.support.openapi.StUhf.InterrogatorModelDs.UmdFrequencyPoint;
import com.senter.support.openapi.StUhf.InterrogatorModelDs.UmdLockField;
import com.senter.support.openapi.StUhf.InterrogatorModelDs.UmdLockType;
import com.senter.support.openapi.StUhf.InterrogatorModelDs.UmdOnIso18k6cLock;
import com.senter.support.openapi.StUhf.UII;

public class Activity3Lock extends Activity4LockCommonAbstract {
    @Override
    protected TargetTagType[] getDestinationType() {
        return new TargetTagType[]{TargetTagType.SingleTag};
    }

    @Override
    protected void onLock() {

        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_main_lockparameterdlgd, null);

        final Spinner spFields = (Spinner) view.findViewById(R.id.idMainActivityLockDlgD_spnrFields);
        final CheckBox cbLock = (CheckBox) view.findViewById(R.id.idMainActivityLockDlgD_cbLock);
        final CheckBox cbPerma = (CheckBox) view.findViewById(R.id.idMainActivityLockDlgD_cbPerma);

        ab.setView(view);
        ab.setPositiveButton(R.string.Locked, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                UmdLockField[] lockFields = new UmdLockField[]{UmdLockField.UserBank, UmdLockField.TidBank, UmdLockField.EpcBank, UmdLockField.AccessPassword, UmdLockField.KillPassword};

                UmdLockField LockedField = lockFields[spFields.getSelectedItemPosition()];

                App.uhfInterfaceAsModelD1().iso18k6cLock(
                        getDestinationTagSpecifics().getAccessPassword()
                        , LockedField
                        , UmdLockType.ValueOf(cbLock.isChecked(), cbPerma.isChecked())
                        , new UmdOnIso18k6cLock() {
                            @Override
                            public void onFailed(UmdErrorCode error) {
                                showToast("faild");
                            }

                            @Override
                            public void onTagLock(int tagCount, UII uii, UmdErrorCode errorCode, UmdFrequencyPoint frequencyPoint, Integer antennaId, int lockCount) {
                                if (errorCode != null && errorCode == UmdErrorCode.command_success) {
                                    addNewMassageToListview(getString(R.string.Locked) + DataTransfer.xGetString(uii.getBytes()) + getString(R.string.successful));
                                } else {
                                    addNewMassageToListview(getString(R.string.Locked) + DataTransfer.xGetString(uii.getBytes()) + errorCode.name());
                                }
                            }
                        }
                );

            }
        });

        ab.create().show();
    }


}








