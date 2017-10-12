package com.senter.demo.uhf.modelA;

import com.senter.demo.uhf.App;
import com.senter.support.openapi.StUhf.Frequency;

public class Activity7Settings extends com.senter.demo.uhf.common.Activity7SettingsModelAB {

    @Override
    protected Frequency getFrequency() {
        return App.uhfInterfaceAsModelA().getFrequency();
    }

    @Override
    protected boolean setFrequency(Frequency frequency) {
        return App.uhfInterfaceAsModelA().setFrequency(frequency);
    }
}
