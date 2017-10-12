package com.senter.demo.uhf.modelB;

import android.widget.Toast;

import com.senter.demo.uhf.App;
import com.senter.demo.uhf.R;
import com.senter.demo.uhf.common.DestinationTagSpecifics;
import com.senter.demo.uhf.common.DestinationTagSpecifics.TargetTagType;
import com.senter.demo.uhf.common.ExceptionForToast;
import com.senter.support.openapi.StUhf.AccessPassword;
import com.senter.support.openapi.StUhf.Bank;
import com.senter.support.openapi.StUhf.Result.ReadResult;
import com.senter.support.openapi.StUhf.UII;

public final class Activity1Read extends com.senter.demo.uhf.common.Activity1ReadCommonAbstract {
    protected DestinationTagSpecifics.TargetTagType[] getDestinationType() {
        return new DestinationTagSpecifics.TargetTagType[]{TargetTagType.SpecifiedTag, TargetTagType.SingleTag, TargetTagType.MatchTag};
    }

    protected ReadResult readDataByUii(AccessPassword apwd,
                                       Bank bank,
                                       int offset,
                                       byte length,
                                       UII uii) throws ExceptionForToast {
        if (length != 0) {
            return App.uhfInterfaceAsModelB().readDataByUii(apwd, bank, offset, length, uii);
        } else {
            return App.uhfInterfaceAsModelB().readDataByUiiAfterIndex(apwd, bank, offset, uii);
        }
    }

    protected ReadResult readDataFromSingleTag(AccessPassword apwd,
                                               Bank bank,
                                               int offset,
                                               int length) throws ExceptionForToast {
        if (length != 0) {
            return App.uhfInterfaceAsModelB().readDataFromSingleTag(apwd, bank, offset, length);
        } else {
            return App.uhfInterfaceAsModelB().readDataFromSingleTagAfterIndex(apwd, bank, offset);
        }
    }


    @Override
    protected void onRead(final Bank bank, final int ptr, final int cnt) {
        enableBtnRead(false);
        new Thread() {// back ground work thread
            public void run() {
                try {
                    ReadResult rr = null;

                    switch (getDestinationTagSpecifics().getCurrentTargetTagType()) {
                        case SpecifiedTag:
                            rr = readDataByUii(getDestinationTagSpecifics().getAccessPassword(), bank, ptr, (byte) cnt, getDestinationTagSpecifics().getDstTagUiiIfOrdered());
                            break;
                        case SingleTag:
                        case MatchTag:
                            rr = readDataFromSingleTag(getDestinationTagSpecifics().getAccessPassword(), bank, ptr, (byte) cnt);
                            break;
                        default:
                            break;
                    }

                    if (rr == null || rr.isSucceeded() == false) {
                        showToast(getString(R.string.ReadDataFailure), Toast.LENGTH_SHORT);
                    } else {
                        addNewMassageToListview(rr.getUii(), rr.getData());
                    }
                } catch (ExceptionForToast e) {
                    showToast(e.getMessage());
                }
                enableBtnRead(true);
            }

            ;
        }.start();
    }
}
