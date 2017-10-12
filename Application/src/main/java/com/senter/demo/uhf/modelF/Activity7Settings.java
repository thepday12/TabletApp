package com.senter.demo.uhf.modelF;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.senter.demo.uhf.App;
import com.senter.demo.uhf.R;
import com.senter.support.openapi.StUhf.InterrogatorModelF;
import com.senter.support.openapi.StUhf.InterrogatorModelF.UmfAntennaConfig;
import com.senter.support.openapi.StUhf.InterrogatorModelF.UmfQueryTagGroup;
import com.senter.support.openapi.StUhf.InterrogatorModelF.UmfQueryTagGroup.QuerySession;
import com.senter.support.openapi.StUhf.InterrogatorModelF.UmfSingluationAlgorithm;
import com.senter.support.openapi.StUhf.InterrogatorModelF.UmfSingluationAlgorithm.AlgorithmType;
import com.senter.support.openapi.StUhf.Q;

public final class Activity7Settings extends Activity {
    public static final String Tag = "Activity7SettingsC";
    InterrogatorModelF impl = App.uhfInterfaceAsModelF();
    EditText etEditText;
    Views views;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity27settingsactivityc);

        views = new Views();

        UmfAntennaConfig p = impl.getAntennaConfig();
        if (p != null) {
            views.setPower2Show((int) p.getPower());
        }

        onBtnPowerGet();
        onBtnSessionGet();
        onBtnAlgithmGet();
    }


    private void onBtnPowerGet() {
        UmfAntennaConfig p = impl.getAntennaConfig();
        if (p == null) {
            Toast.makeText(Activity7Settings.this, getString(R.string.idGetPowerFail), Toast.LENGTH_SHORT).show();
            return;
        }
        views.setPower2Show((int) p.getPower());
    }

    private void onBtnPowerSet() {
        Integer integer = views.getPowerShow();
        if (integer == null) {
            return;
        }

        Boolean float1 = impl.setPower((float) integer);

        if (float1 == null || float1 == false) {
            Toast.makeText(Activity7Settings.this, getString(R.string.idSetPowerFail), Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(Activity7Settings.this, getString(R.string.idSetPowerSuccess), Toast.LENGTH_SHORT).show();
    }

    private void onBtnSessionGet() {
        UmfQueryTagGroup tagGroup = impl.getQueryTagGroup();
        if (tagGroup != null) {
            views.sessionSpinner.setSelection(tagGroup.session().ordinal());
            Toast.makeText(Activity7Settings.this, getString(R.string.idGetSessionSucess), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(Activity7Settings.this, getString(R.string.idGetSessionFail), Toast.LENGTH_SHORT).show();
        }
    }

    private void onBtnSessionSet() {
        UmfQueryTagGroup tagGroup = impl.getQueryTagGroup();
        UmfQueryTagGroup newTagGroup = UmfQueryTagGroup.newInstanceOf(
                /**selected*/tagGroup.selected(),
                /**session*/QuerySession.values()[views.sessionSpinner.getSelectedItemPosition()],
                /**target*/tagGroup.sessionTarget()
        );
        Boolean rslt = impl.setQueryTagGroup(newTagGroup);

        if (rslt != null && rslt == true) {
            Toast.makeText(Activity7Settings.this, getString(R.string.idSetSessionSuccess), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(Activity7Settings.this, getString(R.string.idSetSessionFail), Toast.LENGTH_SHORT).show();
        }
    }

    private void onBtnAlgithmGet() {
        UmfSingluationAlgorithm usa = impl.getSingulationAlgorithm();
        if (usa == null) {
            Toast.makeText(Activity7Settings.this, getString(R.string.idGetSingulationAlgorithmFail), Toast.LENGTH_SHORT).show();
            return;
        }

        views.setSingluationAlgorithmOnUi(usa);
        Toast.makeText(Activity7Settings.this, getString(R.string.idGetSingulationAlgorithmSuccess), Toast.LENGTH_SHORT).show();
    }

    private void onBtnAlgithmSet() {
        UmfSingluationAlgorithm usa = views.getSingluationAlgorithmOnUi();
        if (usa == null) {
            Toast.makeText(Activity7Settings.this, getString(R.string.idInvalidSingulationAlgorithmFormat), Toast.LENGTH_SHORT).show();
            return;
        }
        if (impl.setSingulationAlgorithm(usa) == false) {
            Toast.makeText(Activity7Settings.this, getString(R.string.idSetSingulationAlgorithmFail), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(Activity7Settings.this, getString(R.string.idSetSingulationAlgorithmSuccess), Toast.LENGTH_SHORT).show();
        }
    }

    private class Views {
        EditText powerShowEditText = (EditText) findViewById(R.id.idE27SettingsActivityC_Power_etShow);
        Button powerReadButton = (Button) findViewById(R.id.idE27SettingsActivityC_Power_btnRead);
        Button powerSetButton = (Button) findViewById(R.id.idE27SettingsActivityC_Power_btnSet);

        Spinner sessionSpinner = (Spinner) findViewById(R.id.idE27SettingsActivityC_Session_sprSession);
        Button sessionReadButton = (Button) findViewById(R.id.idE27SettingsActivityC_Session_btnRead);
        Button sessionSetButton = (Button) findViewById(R.id.idE27SettingsActivityC_Session_btnSet);


        Spinner algorithmTypeSpinner = (Spinner) findViewById(R.id.idE27SettingsActivityC_Algrithm_sprAlgrithm);
        Spinner algorithmStartQSpinner = (Spinner) findViewById(R.id.idE27SettingsActivityC_Algrithm_sprStartQ);
        EditText algorithmRetryTimesEditText = (EditText) findViewById(R.id.idE27SettingsActivityC_Algrithm_etRetryTimes);
        Spinner algorithmRevertSpinner = (Spinner) findViewById(R.id.idE27SettingsActivityC_Algrithm_sprRevert);

        Spinner algorithmRepeatUnitlNoTagSpinner = (Spinner) findViewById(R.id.idE27SettingsActivityC_Algrithm_sprRepeatUntilNoTag);
        Spinner algorithmMinQSpinner = (Spinner) findViewById(R.id.idE27SettingsActivityC_Algrithm_sprMinQ);
        Spinner algorithmMaxQSpinner = (Spinner) findViewById(R.id.idE27SettingsActivityC_Algrithm_sprMaxQ);
        Spinner algorithmThresholdSpinner = (Spinner) findViewById(R.id.idE27SettingsActivityC_Algrithm_sprThresholdQ);

        Button algrithmReadButton = (Button) findViewById(R.id.idE27SettingsActivityC_Algrithm_btnGet);
        Button algrithmSetButton = (Button) findViewById(R.id.idE27SettingsActivityC_Algrithm_btnSet);

        private Views() {
            powerReadButton.setOnClickListener(new View.OnClickListener() {
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

            sessionReadButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    onBtnSessionGet();
                }
            });
            sessionSetButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    onBtnSessionSet();
                }
            });

            algrithmReadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBtnAlgithmGet();
                }
            });
            algrithmSetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onBtnAlgithmSet();

                }
            });


        }

        private Integer getPowerShow() {
            String string = powerShowEditText.getText().toString().trim();
            Integer power = null;
            try {
                power = Integer.valueOf(string);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (power == null) {
                Toast.makeText(Activity7Settings.this, getString(R.string.idInvalidPowerFormat), Toast.LENGTH_SHORT).show();
                return null;
            }
            if (power < 3 || 32 < power) {
                Toast.makeText(Activity7Settings.this, getString(R.string.idPleaseEnterTheCorrectPowerValue_3_32), Toast.LENGTH_SHORT).show();
                return null;
            }
            return power;
        }

        private void setPower2Show(final int power) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    powerShowEditText.setText("" + power);
                }
            });
        }

        private UmfSingluationAlgorithm getSingluationAlgorithmOnUi() {
            int retryTimes;
            try {
                retryTimes = Integer.valueOf(algorithmRetryTimesEditText.getText().toString());
            } catch (Exception e) {
                return null;
            }

            int type = algorithmTypeSpinner.getSelectedItemPosition();
            int startQ = algorithmStartQSpinner.getSelectedItemPosition();
            int revert = algorithmRevertSpinner.getSelectedItemPosition();

            int repeat = algorithmRepeatUnitlNoTagSpinner.getSelectedItemPosition();
            int minQ = algorithmMinQSpinner.getSelectedItemPosition();
            int maxQ = algorithmMaxQSpinner.getSelectedItemPosition();
            int threshold = algorithmThresholdSpinner.getSelectedItemPosition();


            UmfSingluationAlgorithm retAlgorithm = UmfSingluationAlgorithm.getInstance(
                    AlgorithmType.values()[type]
                    , Q.values()[startQ]
                    , retryTimes
                    , revert == 0 ? false : true
                    , repeat == 0 ? true : false
                    , Q.values()[minQ]
                    , Q.values()[maxQ]
                    , threshold);

            return retAlgorithm;
        }

        private void setSingluationAlgorithmOnUi(UmfSingluationAlgorithm usa) {
            algorithmTypeSpinner.setSelection(usa.getAlgorithm().ordinal());
            algorithmStartQSpinner.setSelection(usa.getStartQ().ordinal());
            algorithmRetryTimesEditText.setText("" + usa.getRetryTimes());
            algorithmRevertSpinner.setSelection(usa.toggleTarget() == false ? 0 : 1);

            algorithmRepeatUnitlNoTagSpinner.setSelection(usa.repeatUntilNoTagsInStatic() == true ? 0 : 1);
            algorithmMinQSpinner.setSelection(usa.minQValueInDynamic().ordinal());
            algorithmMaxQSpinner.setSelection(usa.maxQValueInDynamic().ordinal());
            algorithmThresholdSpinner.setSelection(usa.thresholdMultipliterInDynamic());
        }


    }
}
