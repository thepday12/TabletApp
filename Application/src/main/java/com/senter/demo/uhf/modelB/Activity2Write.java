package com.senter.demo.uhf.modelB;

import android.widget.Toast;

import com.senter.demo.uhf.App;
import com.senter.demo.uhf.R;
import com.senter.demo.uhf.common.DestinationTagSpecifics;
import com.senter.demo.uhf.common.DestinationTagSpecifics.TargetTagType;
import com.senter.support.openapi.StUhf.Bank;
import com.senter.support.openapi.StUhf.Result.WriteResult;

public final class Activity2Write extends com.senter.demo.uhf.common.Activity2WriteCommonAbstract {
    protected DestinationTagSpecifics.TargetTagType[] getDestinationType() {
        return new DestinationTagSpecifics.TargetTagType[]{TargetTagType.SpecifiedTag, TargetTagType.SingleTag, TargetTagType.MatchTag};
    }

    @Override
    protected final void onWrite(final Bank bank, final int ptr, final byte[] data) {
        final boolean isUiiSepcified = getDestinationTagSpecifics().isOrderedUii();
        enableBtnWrite(false);
        new Thread() {
            public void run() {
                final WriteResult rr;

                try {
                    if (isUiiSepcified) {
                        if (data.length <= 2) {
                            rr = App.uhfInterfaceAsModelB().writeWordDataByUii(getDestinationTagSpecifics().getAccessPassword(), bank, ptr, data, getDestinationTagSpecifics().getDstTagUiiIfOrdered());
                        } else {
                            rr = App.uhfInterfaceAsModelB().writeBlockDataByUii(getDestinationTagSpecifics().getAccessPassword(), bank, ptr, data, getDestinationTagSpecifics().getDstTagUiiIfOrdered());
                        }
                    } else {
                        if (data.length <= 2) {
                            rr = App.uhfInterfaceAsModelB().writeWordDataToSingleTag(getDestinationTagSpecifics().getAccessPassword(), bank, ptr, data);
                        } else {
                            rr = App.uhfInterfaceAsModelB().writeBlockDataToSingleTag(getDestinationTagSpecifics().getAccessPassword(), bank, ptr, data);
                        }
                    }

                    if (rr == null || rr.isSucceeded() == false) {
                        showToast(getString(R.string.WriteFailure), Toast.LENGTH_SHORT);
                    } else {
                        addNewMassageToListview(rr.getUii(), rr.getWrittenWordsNum());
                    }
                } catch (Exception e) {
                    showToast(e.getMessage(), Toast.LENGTH_SHORT);
                }

                enableBtnWrite(true);
            }
        }.start();
    }
}
