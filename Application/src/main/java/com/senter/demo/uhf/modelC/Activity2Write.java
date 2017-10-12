package com.senter.demo.uhf.modelC;

import android.widget.Toast;

import com.senter.demo.uhf.App;
import com.senter.demo.uhf.R;
import com.senter.demo.uhf.common.DestinationTagSpecifics;
import com.senter.demo.uhf.common.DestinationTagSpecifics.TargetTagType;
import com.senter.support.openapi.StUhf.Bank;
import com.senter.support.openapi.StUhf.Result.WriteResult;

public final class Activity2Write extends com.senter.demo.uhf.common.Activity2WriteCommonAbstract {
    protected DestinationTagSpecifics.TargetTagType[] getDestinationType() {
        return new DestinationTagSpecifics.TargetTagType[]{TargetTagType.SingleTag, TargetTagType.MatchTag};
    }

    @Override
    protected final void onWrite(final Bank bank, final int ptr, final byte[] data) {
        enableBtnWrite(false);
        new Thread() {
            public void run() {
                final WriteResult rr;

                try {
                    rr = App.uhfInterfaceAsModelC().writeDataToSingleTag(getDestinationTagSpecifics().getAccessPassword(), bank, ptr, data);
                } catch (Exception e) {
                    showToast(e.getMessage(), Toast.LENGTH_SHORT);
                    enableBtnWrite(true);
                    return;
                }

                if (rr == null || rr.isSucceeded() == false) {
                    showToast(getString(R.string.WriteFailure), Toast.LENGTH_SHORT);
                } else {
                    addNewMassageToListview(rr.getUii(), rr.getWrittenWordsNum());
                }
                enableBtnWrite(true);
            }
        }.start();
    }
}
