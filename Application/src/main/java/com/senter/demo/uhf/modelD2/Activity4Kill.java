package com.senter.demo.uhf.modelD2;

import com.google.common.primitives.Bytes;
import com.senter.demo.uhf.App;
import com.senter.demo.uhf.common.DestinationTagSpecifics;
import com.senter.demo.uhf.common.DestinationTagSpecifics.TargetTagType;
import com.senter.demo.uhf.util.DataTransfer;
import com.senter.support.openapi.StUhf.InterrogatorModelDs.UmdEpcMatchSetting;
import com.senter.support.openapi.StUhf.InterrogatorModelDs.UmdErrorCode;
import com.senter.support.openapi.StUhf.InterrogatorModelDs.UmdFrequencyPoint;
import com.senter.support.openapi.StUhf.InterrogatorModelDs.UmdOnIso18k6cKill;
import com.senter.support.openapi.StUhf.UII;

public class Activity4Kill extends com.senter.demo.uhf.common.Activity6KillCommonAbstract {
    protected DestinationTagSpecifics.TargetTagType[] getDestinationType() {
        return new DestinationTagSpecifics.TargetTagType[]{TargetTagType.SingleTag, TargetTagType.SpecifiedTag};
    }

    @Override
    protected void onKill() {
        if (getDestinationTagSpecifics().isOrderedUii()) {
            UII uii = getDestinationTagSpecifics().getDstTagUiiIfOrdered();

            Boolean boolean1 = App.uhfInterfaceAsModelD2().iso18k6cSetAccessEpcMatch(UmdEpcMatchSetting.newInstanceOfMatchingEpcFieldInUii(uii.getEpc().getBytes()));

            if (boolean1 == null || boolean1 == false) {
                showToast("failed");
                return;
            }
        }

        if (getDestinationTagSpecifics().isOrderedUii() == true) {
            UII uii = getDestinationTagSpecifics().getDstTagUiiIfOrdered();
            UmdEpcMatchSetting epcMatchSetting = UmdEpcMatchSetting.newInstanceOfMatchingEpcFieldInUii(Bytes.toArray(Bytes.asList(uii.getBytes()).subList(2, uii.getUiiLength())));
            Boolean ret = App.uhfInterfaceAsModelD2().iso18k6cSetAccessEpcMatch(epcMatchSetting);
            if (ret == null || ret == false) {
                showToast("set epc match error");
                return;
            }
        }
        App.uhfInterfaceAsModelD2().iso18k6cKill(getDestinationTagSpecifics().getKillPassword(), new UmdOnIso18k6cKill() {
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
