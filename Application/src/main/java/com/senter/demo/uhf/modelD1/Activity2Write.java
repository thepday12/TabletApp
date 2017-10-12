package com.senter.demo.uhf.modelD1;

import android.os.Bundle;

import com.senter.demo.uhf.App;
import com.senter.demo.uhf.common.Activity2WriteCommonAbstract;
import com.senter.demo.uhf.common.DestinationTagSpecifics;
import com.senter.demo.uhf.common.DestinationTagSpecifics.TargetTagType;
import com.senter.support.openapi.StUhf.Bank;
import com.senter.support.openapi.StUhf.InterrogatorModelDs.UmdEpcMatchSetting;
import com.senter.support.openapi.StUhf.InterrogatorModelDs.UmdErrorCode;
import com.senter.support.openapi.StUhf.InterrogatorModelDs.UmdFrequencyPoint;
import com.senter.support.openapi.StUhf.InterrogatorModelDs.UmdOnIso18k6cWrite;
import com.senter.support.openapi.StUhf.UII;

public class Activity2Write extends Activity2WriteCommonAbstract {
    @SuppressWarnings("unused")
    private static final String Tag = "Activity2Write";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected DestinationTagSpecifics.TargetTagType[] getDestinationType() {
        return new TargetTagType[]{TargetTagType.SingleTag};
    }


    @Override
    protected void onWrite(Bank bank, int offset, byte[] data) {

        App.uhfInterfaceAsModelD1().iso18k6cWrite(getDestinationTagSpecifics().getAccessPassword(), bank, offset, data, new UmdOnIso18k6cWrite() {
            @Override
            public void onFailed(UmdErrorCode error) {
                showToast("error:" + error.name());
            }

            @Override
            public void onTagWrite(int tagCount, UII uii, UmdErrorCode errorCode, UmdFrequencyPoint frequencyPoint, Integer antennaId, int writeCount) {
                if (errorCode == UmdErrorCode.command_success) {
                    addNewMassageToListview(uii, writeCount);
                } else {
                    addNewMassageToListview(uii, errorCode.name());
                }
            }
        });

    }
}
