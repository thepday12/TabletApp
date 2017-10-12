package com.senter.demo.uhf.modelD2;

import android.content.Context;

import com.senter.demo.uhf.App;
import com.senter.demo.uhf.util.Configuration;

class ConfigurationSettingsOfModelD2 {
    private Configuration mAppConfiguration;

    private static final String key_useProtocol = "key_useProtocol";

    private final Context ownersContext;


    public enum Protocol {/** 18k6c */I8k6c, /** 18k6b */I8k6b;}

    public final void useProtocol(Protocol protocol) {
        getConfiguration().setString(key_useProtocol, protocol.name());
    }

    public final Protocol usingProtocol() {
        String string = getConfiguration().getString(key_useProtocol, Protocol.I8k6c.name());

        try {
            return Protocol.valueOf(string);
        } catch (Exception e) {
            return Protocol.I8k6c;
        }
    }


    // -----------------------------------------------------------------------------

    private static final ConfigurationSettingsOfModelD2 instance = new ConfigurationSettingsOfModelD2(App.AppInstance());

    static ConfigurationSettingsOfModelD2 getInstance() {
        return instance;
    }

    private ConfigurationSettingsOfModelD2(Context ownersContext) {
        this.ownersContext = ownersContext;
    }

    private final Configuration getConfiguration() {
        if (mAppConfiguration == null) {
            mAppConfiguration = new Configuration(ownersContext, "ConfigurationSettingsOfModelD2", Context.MODE_PRIVATE);
        }
        return mAppConfiguration;
    }
}
