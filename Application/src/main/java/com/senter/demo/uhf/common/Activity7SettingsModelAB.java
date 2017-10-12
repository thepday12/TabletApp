package com.senter.demo.uhf.common;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.senter.demo.uhf.App;
import com.senter.demo.uhf.R;
import com.senter.support.openapi.StUhf.Frequency;
import com.senter.support.openapi.StUhf.Frequency.Base;
import com.senter.support.openapi.StUhf.Frequency.Hop;

public abstract class Activity7SettingsModelAB extends Activity {
    ViewPower mViewPower;
    ViewFrequency mViewFrequency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity27settingsactivity);

        mViewFrequency = new ViewFrequency();
        mViewPower = new ViewPower();

        mViewFrequency.onBtnGet();// update frequency

        mViewPower.powerRead();// update power

    }

    protected abstract Frequency getFrequency();
    protected abstract boolean setFrequency(Frequency frequency);


    class ViewPower {

        Button btnPowerGet = (Button) findViewById(R.id.idE27SettingsActivity_Power_btnRead);
        Button btnPowerSet = (Button) findViewById(R.id.idE27SettingsActivity_Power_btnSet);
        EditText etPower = (EditText) findViewById(R.id.idE27SettingsActivity_Power_etShow);

        public ViewPower() {
            btnPowerGet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    powerRead();
                }
            });
            btnPowerSet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    powerSet();
                }
            });
        }

        private void powerRead() {
            Integer integer = App.uhf().getPower();
            if (integer == null) {
                etPower.setText("");
            } else {
                etPower.setText("" + integer);
            }
        }

        private void powerSet() {
            String string = etPower.getText().toString();
            if (string.length() == 0) {
                Toast.makeText(Activity7SettingsModelAB.this, R.string.InputValueAndUnit, Toast.LENGTH_SHORT).show();
                return;
            }

            Integer power;
            try {
                power = Integer.valueOf(string);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(Activity7SettingsModelAB.this, R.string.InputValueAndUnit, Toast.LENGTH_SHORT).show();
                etPower.setText("");
                return;
            }

            if (power < 10 || power > 30) {
                Toast.makeText(Activity7SettingsModelAB.this, R.string.CurrentModulePowerRange, Toast.LENGTH_SHORT).show();
                return;
            }

            boolean b = false;
            b = App.uhf().setPower(power);
            if (b == true) {
                Toast.makeText(Activity7SettingsModelAB.this, R.string.SetOk, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Activity7SettingsModelAB.this, R.string.SetFailure, Toast.LENGTH_SHORT).show();
            }
        }

    }

    /**
     * frequncy begains at 840Mhz
     */
    class ViewFrequency {
        private final int FrequencyMin = 1000 * 840;
        private final int FrequencyMax = 1000 * 960;

        Spinner spnrMode = (Spinner) findViewById(R.id.idE27SettingsActivity_Frequency_spnrMode);
        RadioButton rb50Khz = (RadioButton) findViewById(R.id.idE27SettingsActivity_Frequency_cb50khz);
        RadioButton rb125Khz = (RadioButton) findViewById(R.id.idE27SettingsActivity_Frequency_cb125khz);
        TextView tvBf = (TextView) findViewById(R.id.idE27SettingsActivity_Frequency_tvBegainFrequency);
        SeekBar sbBf = (SeekBar) findViewById(R.id.idE27SettingsActivity_Frequency_sbBegainFrequency);

        EditText etCN = (EditText) findViewById(R.id.idE27SettingsActivity_Frequency_etChannelNum);
        Spinner spnrCS = (Spinner) findViewById(R.id.idE27SettingsActivity_Frequency_spnrChannelSpace);
        TextView tvCS = (TextView) findViewById(R.id.idE27SettingsActivity_Frequncey_tvChannelSpace);
        Spinner spnrHop = (Spinner) findViewById(R.id.idE27SettingsActivity_Frequency_spnrHop);

        Button btnFrequencyGet = (Button) findViewById(R.id.idE27SettingsActivity_Frequency_btnFrequencyGet);
        Button btnFrequencySet = (Button) findViewById(R.id.idE27SettingsActivity_Frequency_btnFrequencySet);

        public ViewFrequency() {
            sbBf.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        onSeekBarChanged(progress, fromUser);
                    }
                }
            });

            btnFrequencyGet.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    onBtnGet();
                }
            });
            btnFrequencySet.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    try {
                        onBtnSet();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            rb50Khz.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    onRbSelectionChanged();
                }
            });

            spnrMode.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    onModeSeletionChanged(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            spnrCS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    onCsSeletionChanged(arg2);
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {

                }
            });
        }

        protected void onSeekBarChanged(int progress, boolean fromUser) {
            arrangeUi();
        }

        protected void onRbSelectionChanged() {
            if (rb50Khz.isChecked()) {// 50
                sbBf.setKeyProgressIncrement(50);
            } else {// 150
                sbBf.setKeyProgressIncrement(125);
            }
            sbBf.setProgress(0);
            etCN.setText("1");
            spnrCS.setSelection(0);
            arrangeUi();
        }

        protected void onModeSeletionChanged(int position) {
            if (position <= 3) {
                freezeFrequencyDitails(false);
            } else {
                freezeFrequencyDitails(true);
            }

            switch (position) {
                case 0:
                    showFrequencyDitails(Frequency.getNewInstanceOfChineseStandard920To925());
                    break;

                case 1:
                    showFrequencyDitails(Frequency.getNewInstanceOfChineseStandard840To845());
                    break;

                case 2:
                    showFrequencyDitails(Frequency.getNewInstanceOfETSIStandard());
                    break;

                case 3:
                    showFrequencyDitails(Frequency.getNewInstanceOfFixedFrequnce());
                    break;

                case 4:

                    break;

                default:
                    break;
            }
        }

        private void showFrequency(Frequency frequency) {
            showFrequencyMode(frequency);
            showFrequencyDitails(frequency);
        }

        protected Frequency arrangeUi() {
            if (rb50Khz.isChecked()) {// 50
                sbBf.setKeyProgressIncrement(50);
            } else {// 150
                sbBf.setKeyProgressIncrement(125);
            }
            int b = sbBf.getKeyProgressIncrement();
            int begainFrequencyByKhz = (sbBf.getProgress() / b) * b + FrequencyMin;

            // set start frequency
            tvBf.setText(String.format("%.3f", ((float) begainFrequencyByKhz) / 1000));
            // set start frequency
            mFreRange.setB(begainFrequencyByKhz);

            int cn = 1;
            try {
                String string = etCN.getText().toString();
                cn = Integer.valueOf(string);
                if (cn == 0) {
                    Toast.makeText(Activity7SettingsModelAB.this, R.string.ChannelNumberCannotZero, Toast.LENGTH_SHORT).show();
                    return null;
                }
                if (cn > 255) {
                    Toast.makeText(Activity7SettingsModelAB.this, R.string.ChannelNumberOutModuleSupport, Toast.LENGTH_SHORT).show();
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                etCN.setText("" + cn);
                Toast.makeText(Activity7SettingsModelAB.this, R.string.ChannelNumberIllegal, Toast.LENGTH_SHORT).show();
                return null;
            }

            int nbase = spnrCS.getSelectedItemPosition() + 1;
            int base = sbBf.getKeyProgressIncrement();

            if (nbase * base <= 1000) {// channal band
                tvCS.setText("" + nbase * base);
            } else {
                Toast.makeText(Activity7SettingsModelAB.this, R.string.ChannelBandWidthCannotGreaterThan1000, Toast.LENGTH_SHORT).show();
                return null;
            }

            int frequencyBand = (cn - 1) * nbase * base;
            if (rb50Khz.isChecked()) {
                if (frequencyBand > 1000 * 12) {
                    Toast.makeText(Activity7SettingsModelAB.this, R.string.broadbandCannotGreaterThan12Mhz, Toast.LENGTH_SHORT).show();
                    return null;
                }
            } else if (rb125Khz.isChecked()) {
                if (frequencyBand > 1000 * 32) {
                    Toast.makeText(Activity7SettingsModelAB.this, R.string.broadbandCannotGreaterThan32Mhz, Toast.LENGTH_SHORT).show();
                    return null;
                }
            } else {
                Toast.makeText(Activity7SettingsModelAB.this, R.string.InError, Toast.LENGTH_SHORT).show();
                return null;
            }

            int endfrequencyByKhz = frequencyBand + begainFrequencyByKhz;
            mFreRange.setE(endfrequencyByKhz);
            if (endfrequencyByKhz <= FrequencyMax) {// ok
            } else {
                Toast.makeText(Activity7SettingsModelAB.this, R.string.FMAXCannotPass960Mhz, Toast.LENGTH_SHORT).show();
                return null;
            }

            Frequency frequency = Frequency.getNewInstanceOfCustom((rb50Khz.isChecked()) ? Base.FB50Khz : Base.FB125Khz, begainFrequencyByKhz / 1000, (begainFrequencyByKhz % 1000) / sbBf.getKeyProgressIncrement(), cn, nbase, Hop.values()[spnrHop.getSelectedItemPosition()]);

            return frequency;
        }

        private void showFrequencyMode(Frequency frequency) {
            // mode
            if (frequency.asSameAs((Frequency.getNewInstanceOfChineseStandard920To925()))) {
                spnrMode.setSelection(0);
            } else if (frequency.asSameAs(Frequency.getNewInstanceOfChineseStandard840To845())) {
                spnrMode.setSelection(1);
            } else if (frequency.asSameAs(Frequency.getNewInstanceOfETSIStandard())) {
                spnrMode.setSelection(2);
            } else if (frequency.asSameAs(Frequency.getNewInstanceOfFixedFrequnce())) {
                spnrMode.setSelection(3);
            } else {
                spnrMode.setSelection(4);
            }
        }

        private void showFrequencyDitails(Frequency frequency) {
            Base f_Base = frequency.getFrequencyBase();
            int f_bfI = frequency.getBeginFrequencyIntByMhz();
            int f_bfD_base = frequency.getBeginFrequencyDecimalByMhzByBase();
            int f_CC = frequency.getChannelCount();
            int f_CS_base = frequency.getChennalSpaceByBase();
            Hop f_Hp_Mode = frequency.getHopMode();

            // base frequency
            if (f_Base == Base.FB50Khz) {
                rb50Khz.setChecked(true);
            } else {
                rb125Khz.setChecked(true);
            }
            sbBf.setKeyProgressIncrement(f_Base.getMatchedFrequencyByKhz());

            // frequency range
            int rangeBI = f_bfI * 1000 + f_bfD_base * f_Base.getMatchedFrequencyByKhz();
            mFreRange.setB(rangeBI);

            int rangeEI = rangeBI + (f_CC - 1) * f_CS_base * f_Base.getMatchedFrequencyByKhz();
            mFreRange.setE(rangeEI);

            //  seekbar of start frequency
            sbBf.setProgress(rangeBI - FrequencyMin);

            // channel count
            etCN.setText("" + f_CC);

            // channel band
            spnrCS.setSelection(f_CS_base - 1);

            // hop
            if (f_Hp_Mode == Hop.Random) {
                spnrHop.setSelection(0);
            } else if (f_Hp_Mode == Hop.Descending) {
                spnrHop.setSelection(1);
            } else if (f_Hp_Mode == Hop.Ascending) {
                spnrHop.setSelection(2);
            } else {
                spnrHop.setSelection(0);
            }

            arrangeUi();

        }

        protected void onBtnGet() {
            Frequency frequency = null;
            frequency = getFrequency();
            if (frequency == null) {
                Toast.makeText(Activity7SettingsModelAB.this, R.string.FrequencyGetFailure, Toast.LENGTH_SHORT).show();
                return;
            }
            showFrequency(frequency);
        }


        protected void onBtnSet() throws InterruptedException {
            Frequency frequency = arrangeUi();
            if (frequency != null) {
                boolean b = setFrequency(frequency);
                if (b) {
                    Toast.makeText(Activity7SettingsModelAB.this, R.string.FrequencySetSuccessful, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Activity7SettingsModelAB.this, R.string.FrequencySetFailure, Toast.LENGTH_SHORT).show();
                }
            }
        }

        protected void onCsSeletionChanged(int arg2) {
            arrangeUi();
        }

        private void freezeFrequencyDitails(boolean enable) {
            rb125Khz.setEnabled(enable);
            rb50Khz.setEnabled(enable);
            sbBf.setEnabled(enable);
            etCN.setEnabled(enable);
            spnrCS.setEnabled(enable);
            spnrHop.setEnabled(enable);
        }

        FreRange mFreRange = new FreRange();

        class FreRange {
            TextView tvRange = (TextView) findViewById(R.id.idE27SettingsActivity_Frequency_tvRange);

            int b;
            int e;

            int getB() {
                return b;
            }

            int getE() {
                return e;
            }

            void setB(int k) {
                b = k;
                tvRange.setText(String.format("%.3f", ((float) b) / 1000) + "-" + String.format("%.3f", ((float) e) / 1000));
            }

            void setE(int k) {
                e = k;
                tvRange.setText(String.format("%.3f", ((float) b) / 1000) + "-" + String.format("%.3f", ((float) e) / 1000));
            }
        }

    }

}
