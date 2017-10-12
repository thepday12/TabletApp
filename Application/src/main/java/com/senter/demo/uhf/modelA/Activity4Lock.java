package com.senter.demo.uhf.modelA;

import com.senter.demo.uhf.common.DestinationTagSpecifics;
import com.senter.demo.uhf.common.DestinationTagSpecifics.TargetTagType;

public final class Activity4Lock extends com.senter.demo.uhf.common.Activity4LockCommonAbstractABC {
    protected DestinationTagSpecifics.TargetTagType[] getDestinationType() {
        return new DestinationTagSpecifics.TargetTagType[]{TargetTagType.SpecifiedTag};
    }
}
