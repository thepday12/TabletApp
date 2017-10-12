package com.senter.demo.uhf.modelD1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.senter.demo.uhf.App;
import com.senter.demo.uhf.R;
import com.senter.demo.uhf.common.Activity_Abstract;

public class Activity5Settings extends Activity_Abstract {
    private EditText powerEditText;
    private Button powerGetButton;
    private Button powerSetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity27settingsactivityd1);

        powerEditText = (EditText) findViewById(R.id.idE27SettingsActivityD1_Power_etShow);
        powerGetButton = (Button) findViewById(R.id.idE27SettingsActivityD1_Power_btnRead);
        powerSetButton = (Button) findViewById(R.id.idE27SettingsActivityD1_Power_btnSet);

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
    }

    protected void onBtnPowerGet() {
        Integer power = App.uhfInterfaceAsModelD1().getOutputPower();
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

        if (power < 0 || power > 26) {
            showToast("power must be in [0,26]");
            return;
        }

        Boolean ret = App.uhfInterfaceAsModelD1().setOutputPower(power);
        if (ret == null || ret == false) {
            showToast("set power failed");
        } else {
            showToast("power set successfully");
        }
    }

}
