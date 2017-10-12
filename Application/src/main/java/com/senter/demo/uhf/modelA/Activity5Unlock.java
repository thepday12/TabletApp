package com.senter.demo.uhf.modelA;

import android.widget.Toast;

import com.senter.demo.uhf.App;
import com.senter.demo.uhf.R;
import com.senter.demo.uhf.common.DestinationTagSpecifics;
import com.senter.demo.uhf.common.DestinationTagSpecifics.TargetTagType;
import com.senter.demo.uhf.util.DataTransfer;
import com.senter.support.openapi.StUhf.LockParameter;
import com.senter.support.openapi.StUhf.Result.LockResult;

public final class Activity5Unlock extends com.senter.demo.uhf.common.Activity5UnlockCommonAbstract {
    protected DestinationTagSpecifics.TargetTagType[] getDestinationType() {
        return new DestinationTagSpecifics.TargetTagType[]{TargetTagType.SpecifiedTag};
    }

    @Override
    protected void onUnlock() {
        LockResult rslt = null;
        LockParameter lp = LockParameter.getNewInstance(false, null, false, null, false, null, null, null, false, null);

        if (getDestinationTagSpecifics().isOrderedUii()) {
            rslt = App.uhf().lockMemByUii(getDestinationTagSpecifics().getAccessPassword(), lp, getDestinationTagSpecifics().getDstTagUiiIfOrdered());
        } else {
            rslt = App.uhf().lockMemFromSingleTag(getDestinationTagSpecifics().getAccessPassword(), lp);
        }

        if (rslt == null || rslt.isSucceeded() == false) {
            Toast.makeText(Activity5Unlock.this, R.string.UnLockFailure, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(Activity5Unlock.this, R.string.UnLock + DataTransfer.xGetString(rslt.getUii().getBytes()) + R.string.successful, Toast.LENGTH_SHORT).show();
        }
    }
}
