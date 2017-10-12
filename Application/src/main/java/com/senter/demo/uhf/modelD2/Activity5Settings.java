package com.senter.demo.uhf.modelD2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.senter.demo.uhf.App;
import com.senter.demo.uhf.R;
import com.senter.demo.uhf.common.Activity_Abstract;
import com.senter.demo.uhf.modelD2.ConfigurationSettingsOfModelD2.Protocol;
import com.senter.support.openapi.StUhf.InterrogatorModel;

public class Activity5Settings extends Activity_Abstract {
    private EditText powerEditText;
    private Button powerGetButton;
    private Button powerSetButton;

    private TextView temporaryTextView;
    private Button temporaryGetButton;

    private RadioGroup rgProtocolSelect;

    private final ConfigurationSettingsOfModelD2 configurationSettings = ConfigurationSettingsOfModelD2.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity27settingsactivityd2);

        powerEditText = (EditText) findViewById(R.id.idE27SettingsActivityD2_Power_etShow);
        powerGetButton = (Button) findViewById(R.id.idE27SettingsActivityD2_Power_btnRead);
        powerSetButton = (Button) findViewById(R.id.idE27SettingsActivityD2_Power_btnSet);

        powerGetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtnPowerGet();
            }
        });

        powerSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtnPowerSet();
            }
        });
        onBtnPowerGet();

        if (App.uhfInterfaceAsModel() == InterrogatorModel.InterrogatorModelD2) {
            LinearLayout ll = (LinearLayout) findViewById(R.id.idE27SettingsActivityD2_Temporary_ll);
            ll.setVisibility(View.VISIBLE);
            temporaryTextView = (TextView) findViewById(R.id.idE27SettingsActivityD2_Temporary_tvShow);
            temporaryGetButton = (Button) findViewById(R.id.idE27SettingsActivityD2_Temporary_btnRead);
            temporaryGetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBtnTemporaryGet();
                }
            });

            onBtnTemporaryGet();
        }

        rgProtocolSelect = (RadioGroup) findViewById(R.id.idE27SettingsActivityD2_Protocol_rg);
        rgProtocolSelect.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.idE27SettingsActivityD2_Protocol_rb6C:
                        configurationSettings.useProtocol(Protocol.I8k6c);
                        break;
                    case R.id.idE27SettingsActivityD2_Protocol_rb6B:
                        configurationSettings.useProtocol(Protocol.I8k6b);
                        break;

                    default:
                        break;
                }
            }
        });

        if (configurationSettings.usingProtocol() == Protocol.I8k6c) {// 6C
            rgProtocolSelect.check(R.id.idE27SettingsActivityD2_Protocol_rb6C);
        } else if (configurationSettings.usingProtocol() == Protocol.I8k6b) {// 6B
            rgProtocolSelect.check(R.id.idE27SettingsActivityD2_Protocol_rb6B);
        } else {
            throw new IllegalStateException("only 6c and 6b,no other protocol");
        }
    }

    protected void onBtnPowerGet() {
        Integer power = App.uhfInterfaceAsModelD2().getOutputPower();
        if (power == null) {
            showToast("power get failed");
        }
        powerEditText.setText("" + power);
    }

    protected void onBtnPowerSet() {
        Integer power = null;
        try {
            power = Integer.valueOf(powerEditText.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
            showToast("power format error");
            return;
        }

        if (power < 0 || power > 32) {
            showToast("power must be in [0,33]");
            return;
        }

        Boolean ret = App.uhfInterfaceAsModelD2().setOutputPower(power);
        if (ret == null || ret == false) {
            showToast("set power failed");
        } else {
            showToast("power set successfully");
        }
    }

    protected void onBtnTemporaryGet() {
        Integer t = App.uhfInterfaceAsModelD2().getReadersTemperature();

        if (t == null) {
            temporaryTextView.setText("");
        } else {
            temporaryTextView.setText("" + t + " ℃");
        }
    }

}
