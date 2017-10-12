package com.senter.demo.uhf.modelF;

import android.widget.Toast;

import com.senter.demo.uhf.App;
import com.senter.demo.uhf.R;
import com.senter.demo.uhf.common.DestinationTagSpecifics;
import com.senter.demo.uhf.common.DestinationTagSpecifics.TargetTagType;
import com.senter.demo.uhf.util.DataTransfer;
import com.senter.support.openapi.StUhf.InterrogatorModelF.UmfResults.ResultOfLocking;
import com.senter.support.openapi.StUhf.InterrogatorModelF.UmfTagMemPerm;
import com.senter.support.openapi.StUhf.InterrogatorModelF.UmfTagPwdPerm;

public final class Activity5Unlock extends com.senter.demo.uhf.common.Activity5UnlockCommonAbstract {
    protected DestinationTagSpecifics.TargetTagType[] getDestinationType() {
        return new DestinationTagSpecifics.TargetTagType[]{TargetTagType.SingleTag, TargetTagType.MatchTag};
    }


    @Override
    protected void onUnlock() {
        ResultOfLocking rslt = App.uhfInterfaceAsModelF().lockMemSingleTag(getDestinationTagSpecifics().getAccessPassword()
                , UmfTagPwdPerm.Accessible
                , UmfTagPwdPerm.Accessible
                , UmfTagMemPerm.Writeable
                , null
                , UmfTagMemPerm.Writeable
        );

        if (rslt == null || rslt.isSuccessful() == false) {
            Toast.makeText(Activity5Unlock.this, R.string.UnLockFailure, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(Activity5Unlock.this, R.string.UnLock + DataTransfer.xGetString(rslt.getUii().getBytes()) + R.string.successful, Toast.LENGTH_SHORT).show();
        }
    }
}
