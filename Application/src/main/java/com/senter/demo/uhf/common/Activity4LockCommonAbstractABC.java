package com.senter.demo.uhf.common;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.senter.demo.uhf.App;
import com.senter.demo.uhf.R;
import com.senter.demo.uhf.util.DataTransfer;
import com.senter.support.openapi.StUhf.Function;
import com.senter.support.openapi.StUhf.InterrogatorModelC.UmcLockParamter;
import com.senter.support.openapi.StUhf.LockParameter;
import com.senter.support.openapi.StUhf.Result.LockResult;
import com.senter.support.openapi.StUhf.UII;

public abstract class Activity4LockCommonAbstractABC extends Activity4LockCommonAbstract {

    protected final void onLock() {
        if (getDestinationTagSpecifics().isOrderedUii() == false && App.uhf().isFunctionSupported(Function.LockMemFromSingleTag) == false) {
            Toast.makeText(this, R.string.CurrentModuleOnlySupportTheAssignLabelErasure, Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_main_lockparameterdlg, null);

        final CheckBox cbKPwd = (CheckBox) view.findViewById(R.id.idMainActivityLockDlg_KillPwd_cbPwdRW);
        final Spinner spKpwd = (Spinner) view.findViewById(R.id.idMainActivityLockDlg_KillPwd_spinnerPwdRW);
        final CheckBox cbKLck = (CheckBox) view.findViewById(R.id.idMainActivityLockDlg_KillPwd_cbPermaLock);
        final Spinner spKLck = (Spinner) view.findViewById(R.id.idMainActivityLockDlg_KillPwd_spinnerPermaLock);

        final CheckBox cbAPwd = (CheckBox) view.findViewById(R.id.idMainActivityLockDlg_AcesPwd_cbPwdRW);
        final Spinner spApwd = (Spinner) view.findViewById(R.id.idMainActivityLockDlg_AcesPwd_spinnerPwdRW);
        final CheckBox cbALck = (CheckBox) view.findViewById(R.id.idMainActivityLockDlg_AcesPwd_cbPermaLock);
        final Spinner spALck = (Spinner) view.findViewById(R.id.idMainActivityLockDlg_AcesPwd_spinnerPermaLock);

        final CheckBox cbEPwd = (CheckBox) view.findViewById(R.id.idMainActivityLockDlg_Epc_cbPwdRW);
        final Spinner spEpwd = (Spinner) view.findViewById(R.id.idMainActivityLockDlg_Epc_spinnerPwdRW);
        final CheckBox cbELck = (CheckBox) view.findViewById(R.id.idMainActivityLockDlg_Epc_cbPermaLock);
        final Spinner spELck = (Spinner) view.findViewById(R.id.idMainActivityLockDlg_Epc_spinnerPermaLock);

        final CheckBox cbTPwd = (CheckBox) view.findViewById(R.id.idMainActivityLockDlg_Tid_cbPwdRW);
        final Spinner spTpwd = (Spinner) view.findViewById(R.id.idMainActivityLockDlg_Tid_spinnerPwdRW);
        final CheckBox cbTLck = (CheckBox) view.findViewById(R.id.idMainActivityLockDlg_Tid_cbPermaLock);
        final Spinner spTLck = (Spinner) view.findViewById(R.id.idMainActivityLockDlg_Tid_spinnerPermaLock);

        final CheckBox cbUPwd = (CheckBox) view.findViewById(R.id.idMainActivityLockDlg_User_cbPwdRW);
        final Spinner spUpwd = (Spinner) view.findViewById(R.id.idMainActivityLockDlg_User_spinnerPwdRW);
        final CheckBox cbULck = (CheckBox) view.findViewById(R.id.idMainActivityLockDlg_User_cbPermaLock);
        final Spinner spULck = (Spinner) view.findViewById(R.id.idMainActivityLockDlg_User_spinnerPermaLock);

        switch (App.uhfInterfaceAsModel()) {
            case InterrogatorModelA:
            case InterrogatorModelB:
                break;
            case InterrogatorModelC:
                assosiateCheckBox(cbKPwd, cbKLck);
                assosiateCheckBox(cbAPwd, cbALck);
                assosiateCheckBox(cbEPwd, cbELck);
                assosiateCheckBox(cbTPwd, cbTLck);
                assosiateCheckBox(cbUPwd, cbULck);
                break;
            default:
                break;
        }


        ab.setView(view);
        ab.setPositiveButton(R.string.Locked, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean rslt = false;
                UII uii = null;
                switch (App.uhfInterfaceAsModel()) {
                    case InterrogatorModelA:
                    case InterrogatorModelB:
                        LockResult lockResult;
                        LockParameter lp = LockParameter.getNewInstance(getParamter(cbKPwd, spKpwd)
                                , getParamter(cbKLck, spKLck)
                                , getParamter(cbAPwd, spApwd)
                                , getParamter(cbALck, spALck)
                                , getParamter(cbEPwd, spEpwd)
                                , getParamter(cbELck, spELck)
                                , getParamter(cbTPwd, spTpwd)
                                , getParamter(cbTLck, spTLck),
                                                                        getParamter(cbUPwd, spUpwd), getParamter(cbULck, spULck));
                        if (getDestinationTagSpecifics().isOrderedUii()) {
                            lockResult = App.uhf().lockMemByUii(getDestinationTagSpecifics().getAccessPassword(), lp, getDestinationTagSpecifics().getDstTagUiiIfOrdered());
                        } else {
                            lockResult = App.uhf().lockMemFromSingleTag(getDestinationTagSpecifics().getAccessPassword(), lp);
                        }

                        if (lockResult == null || lockResult.isSucceeded() == false) {
                            rslt = false;
                        } else {
                            rslt = true;
                            uii = lockResult.getUii();
                        }
                        break;
                    case InterrogatorModelC:
                        lockResult = App.uhfInterfaceAsModelC().lockMemFromSingleTag(getDestinationTagSpecifics().getAccessPassword()
                                , getUmcLockParamter(cbKPwd, spKpwd, spKLck)
                                , getUmcLockParamter(cbAPwd, spApwd, spKLck)
                                , getUmcLockParamter(cbEPwd, spEpwd, spELck)
                                , getUmcLockParamter(cbTPwd, spTpwd, spTLck)
                                , getUmcLockParamter(cbUPwd, spUpwd, spULck)
                        );

                        if (lockResult == null || lockResult.isSucceeded() == false) {
                            rslt = false;
                        } else {
                            rslt = true;
                            uii = lockResult.getUii();
                        }
                        break;
                    default:
                        break;
                }
                if (rslt == false) {
                    Toast.makeText(Activity4LockCommonAbstractABC.this, R.string.LockFailure, Toast.LENGTH_SHORT).show();
                } else {
                    addNewMassageToListview(getString(R.string.Locked) + (uii != null ? DataTransfer.xGetString(uii.getBytes()) : "unknown") + getString(R.string.successful));
                    Toast.makeText(Activity4LockCommonAbstractABC.this, getString(R.string.Locked) + (uii != null ? DataTransfer.xGetString(uii.getBytes()) : "unknown") + getString(R.string.successful), Toast.LENGTH_SHORT).show();
                }
            }

            private Boolean getParamter(CheckBox cb, Spinner sp) {
                return cb.isChecked() ? (sp.getSelectedItemPosition() == 1 ? true : false) : null;
            }
        });

        ab.create().show();
    }


    private static void assosiateCheckBox(final CheckBox cb1, final CheckBox cb2) {
        final boolean[] selfchenge = new boolean[0];
        cb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (selfchenge[0] == true) {
                    selfchenge[0] = false;
                    return;
                }
                selfchenge[0] = true;
                cb2.setChecked(isChecked);
            }
        });

        cb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (selfchenge[0] == true) {
                    selfchenge[0] = false;
                    return;
                }
                selfchenge[0] = true;
                cb1.setChecked(isChecked);
            }
        });
    }

    private static UmcLockParamter getUmcLockParamter(CheckBox set, Spinner pwd, Spinner lock) {
        if (set.isChecked() == false) {
            return UmcLockParamter.NoChange;
        }

        Table<Integer, Integer, UmcLockParamter> table = HashBasedTable.create();
        table.put(0, 0, UmcLockParamter.Accessible);
        table.put(1, 0, UmcLockParamter.SecuredAccessible);
        table.put(0, 1, UmcLockParamter.AlwaysAccessible);
        table.put(1, 1, UmcLockParamter.AlwaysNotAccessible);

        return table.get(pwd.getSelectedItemPosition(), lock.getSelectedItemPosition());
    }
}
