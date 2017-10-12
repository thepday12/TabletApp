package com.senter.demo.uhf.modelB;

import com.senter.demo.uhf.App;
import com.senter.support.openapi.StUhf.Frequency;

public final class Activity7Settings extends com.senter.demo.uhf.common.Activity7SettingsModelAB {


    @Override
    protected Frequency getFrequency() {
        return App.uhfInterfaceAsModelB().getFrequency();
    }

    @Override
    protected boolean setFrequency(Frequency frequency) {
        return App.uhfInterfaceAsModelB().setFrequency(frequency);
    }
}
