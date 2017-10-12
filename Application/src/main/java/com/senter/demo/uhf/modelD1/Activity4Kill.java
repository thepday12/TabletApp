package com.senter.demo.uhf.modelD1;

import com.senter.demo.uhf.App;
import com.senter.demo.uhf.common.DestinationTagSpecifics;
import com.senter.demo.uhf.common.DestinationTagSpecifics.TargetTagType;
import com.senter.demo.uhf.util.DataTransfer;
import com.senter.support.openapi.StUhf.InterrogatorModelDs.UmdErrorCode;
import com.senter.support.openapi.StUhf.InterrogatorModelDs.UmdFrequencyPoint;
import com.senter.support.openapi.StUhf.InterrogatorModelDs.UmdOnIso18k6cKill;
import com.senter.support.openapi.StUhf.UII;

public class Activity4Kill extends com.senter.demo.uhf.common.Activity6KillCommonAbstract {
    protected DestinationTagSpecifics.TargetTagType[] getDestinationType() {
        return new DestinationTagSpecifics.TargetTagType[]{TargetTagType.SingleTag};
    }

    @Override
    protected void onKill() {
        App.uhfInterfaceAsModelD1().iso18k6cKill(getDestinationTagSpecifics().getKillPassword(), new UmdOnIso18k6cKill() {
            @Override
            public void onFailed(UmdErrorCode error) {
                showToast("kill error");
            }

            @Override
            public void onTagKill(int tagCount, UII uii, UmdErrorCode errorCode, UmdFrequencyPoint frequencyPoint, Integer antennaId, int killCount) {
                addNewMassageToListview("Killed:" + DataTransfer.xGetString(uii.getBytes()));
            }
        });
    }
}
