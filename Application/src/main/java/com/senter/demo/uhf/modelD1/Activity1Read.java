package com.senter.demo.uhf.modelD1;

import android.os.Bundle;

import com.senter.demo.uhf.App;
import com.senter.demo.uhf.common.Activity1ReadCommonAbstract;
import com.senter.demo.uhf.common.DestinationTagSpecifics;
import com.senter.demo.uhf.common.DestinationTagSpecifics.TargetTagType;
import com.senter.support.openapi.StUhf.Bank;
import com.senter.support.openapi.StUhf.InterrogatorModelDs.UmdEpcMatchSetting;
import com.senter.support.openapi.StUhf.InterrogatorModelDs.UmdErrorCode;
import com.senter.support.openapi.StUhf.InterrogatorModelDs.UmdFrequencyPoint;
import com.senter.support.openapi.StUhf.InterrogatorModelDs.UmdOnIso18k6cRead;
import com.senter.support.openapi.StUhf.UII;

public class Activity1Read extends Activity1ReadCommonAbstract {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected DestinationTagSpecifics.TargetTagType[] getDestinationType() {
        return new TargetTagType[]{TargetTagType.SingleTag};
    }


    @Override
    protected void onRead(Bank bank, int ptr, int cnt) {

        App.uhfInterfaceAsModelD1().iso18k6cRead(getDestinationTagSpecifics().getAccessPassword(), bank, ptr, cnt, new UmdOnIso18k6cRead() {

            @Override
            public void onFailed(UmdErrorCode error) {
                showToast("error:" + error.name());
            }

            @Override
            public void onTagRead(int tagCount, UII uii, byte[] data, UmdFrequencyPoint frequencyPoint, Integer antennaId, int readCount) {
                addNewMassageToListview(uii, data);
            }
        });
    }
}
