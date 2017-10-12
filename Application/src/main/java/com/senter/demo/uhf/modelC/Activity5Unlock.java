package com.senter.demo.uhf.modelC;

import android.widget.Toast;

import com.senter.demo.uhf.App;
import com.senter.demo.uhf.R;
import com.senter.demo.uhf.common.DestinationTagSpecifics;
import com.senter.demo.uhf.common.DestinationTagSpecifics.TargetTagType;
import com.senter.demo.uhf.util.DataTransfer;
import com.senter.support.openapi.StUhf.InterrogatorModelC.UmcLockParamter;
import com.senter.support.openapi.StUhf.Result.LockResult;

public final class Activity5Unlock extends com.senter.demo.uhf.common.Activity5UnlockCommonAbstract {
    protected DestinationTagSpecifics.TargetTagType[] getDestinationType() {
        return new DestinationTagSpecifics.TargetTagType[]{TargetTagType.SingleTag, TargetTagType.MatchTag};
    }


    @Override
    protected void onUnlock() {
        LockResult rslt = null;
        rslt = App.uhfInterfaceAsModelC().lockMemFromSingleTag(getDestinationTagSpecifics().getAccessPassword()
                , UmcLockParamter.Accessible
                , UmcLockParamter.Accessible
                , UmcLockParamter.Accessible
                , UmcLockParamter.Accessible
                , UmcLockParamter.Accessible
        );

        if (rslt == null || rslt.isSucceeded() == false) {
            Toast.makeText(Activity5Unlock.this, R.string.UnLockFailure, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(Activity5Unlock.this, R.string.UnLock + DataTransfer.xGetString(rslt.getUii().getBytes()) + R.string.successful, Toast.LENGTH_SHORT).show();
        }
    }
}
