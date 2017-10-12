package com.senter.demo.uhf.modelB;

import com.senter.demo.uhf.App;
import com.senter.demo.uhf.common.DestinationTagSpecifics;
import com.senter.demo.uhf.common.DestinationTagSpecifics.TargetTagType;
import com.senter.demo.uhf.common.ExceptionForToast;
import com.senter.support.openapi.StUhf.AccessPassword;
import com.senter.support.openapi.StUhf.Bank;
import com.senter.support.openapi.StUhf.Result.EraseResult;
import com.senter.support.openapi.StUhf.UII;

public final class Activity3Erase extends com.senter.demo.uhf.common.Activity3EraseCommonAbstract {
    protected DestinationTagSpecifics.TargetTagType[] getDestinationType() {
        return new DestinationTagSpecifics.TargetTagType[]{TargetTagType.SpecifiedTag, TargetTagType.SingleTag, TargetTagType.MatchTag};
    }

    @Override
    protected EraseResult eraseDataByUii(AccessPassword apwd,
                                         Bank bank,
                                         int offset,
                                         byte length,
                                         UII uii) throws ExceptionForToast {
        return App.uhfInterfaceAsModelB().eraseDataByUii(apwd, bank, offset, length, uii);
    }

    @Override
    protected EraseResult eraseDataFromSingleTag(AccessPassword apwd,
                                                 Bank bank,
                                                 int offset,
                                                 byte length) throws ExceptionForToast {
        return App.uhfInterfaceAsModelB().eraseDataFromSingleTag(apwd, bank, offset, length);
    }
}
