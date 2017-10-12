package com.senter.demo.uhf.modelA;

import android.os.Bundle;
import android.widget.Toast;

import com.senter.demo.uhf.App;
import com.senter.demo.uhf.R;
import com.senter.demo.uhf.common.DestinationTagSpecifics;
import com.senter.demo.uhf.common.DestinationTagSpecifics.TargetTagType;
import com.senter.support.openapi.StUhf;
import com.senter.support.openapi.StUhf.Bank;
import com.senter.support.openapi.StUhf.Result.ReadResult;

public final class Activity1Read extends com.senter.demo.uhf.common.Activity1ReadCommonAbstract {
    protected DestinationTagSpecifics.TargetTagType[] getDestinationType() {
        return new DestinationTagSpecifics.TargetTagType[]{TargetTagType.SpecifiedTag, TargetTagType.SingleTag};
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App.appCfgSaveModel(StUhf.InterrogatorModel.InterrogatorModelA);
        App.getUhf(App.appCfgSavedModel());
    }

    @Override
    protected void onRead(final Bank bank, final int ptr, final int cnt) {
        enableBtnRead(false);
        new Thread() {// back ground work thread
            public void run() {
                ReadResult rr = null;

                switch (getDestinationTagSpecifics().getCurrentTargetTagType()) {
                    case SpecifiedTag:
                        rr = App.uhfInterfaceAsModelA().readDataByUii(getDestinationTagSpecifics().getAccessPassword(), bank, ptr, (byte) cnt, getDestinationTagSpecifics().getDstTagUiiIfOrdered());
                        break;
                    case SingleTag:
                        rr = App.uhfInterfaceAsModelA().readDataFromSingleTag(getDestinationTagSpecifics().getAccessPassword(), bank, ptr, (int) (byte) cnt);
                        break;
                    default:
                        break;
                }

                if (rr == null || rr.isSucceeded() == false) {
                    showToast(getString(R.string.ReadDataFailure), Toast.LENGTH_SHORT);
                } else {
                    addNewMassageToListview(rr.getUii(), rr.getData());
                }
                enableBtnRead(true);
            }

            ;
        }.start();
    }
}
