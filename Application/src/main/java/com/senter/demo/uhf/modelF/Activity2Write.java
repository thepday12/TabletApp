package com.senter.demo.uhf.modelF;

import android.widget.Toast;

import com.senter.demo.uhf.App;
import com.senter.demo.uhf.R;
import com.senter.demo.uhf.common.DestinationTagSpecifics;
import com.senter.demo.uhf.common.DestinationTagSpecifics.TargetTagType;
import com.senter.support.openapi.StUhf.Bank;
import com.senter.support.openapi.StUhf.InterrogatorModelF.UmfResults.ResultOfWriting;

public final class Activity2Write extends com.senter.demo.uhf.common.Activity2WriteCommonAbstract {
    protected DestinationTagSpecifics.TargetTagType[] getDestinationType() {
        return new DestinationTagSpecifics.TargetTagType[]{TargetTagType.SingleTag, TargetTagType.MatchTag};
    }

    @Override
    protected final void onWrite(final Bank bank, final int ptr, final byte[] data) {
        enableBtnWrite(false);
        new Thread() {
            public void run() {
                try {
                    ResultOfWriting rr = App.uhfInterfaceAsModelF().writeDataToSingleTag(getDestinationTagSpecifics().getAccessPassword(), bank, ptr, data);

                    if (rr == null || rr.isSuccessful() == false) {
                        showToast(getString(R.string.WriteFailure), Toast.LENGTH_SHORT);
                    } else {
                        addNewMassageToListview(rr.getUii(), rr.getWrittenWordsNum());
                    }
                    enableBtnWrite(true);
                } catch (Exception e) {
                    showToast(e.getMessage(), Toast.LENGTH_SHORT);
                    enableBtnWrite(true);
                    return;
                }
            }
        }.start();
    }
}
