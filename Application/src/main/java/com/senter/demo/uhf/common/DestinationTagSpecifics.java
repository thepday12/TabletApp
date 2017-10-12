package com.senter.demo.uhf.common;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.senter.demo.uhf.App;
import com.senter.demo.uhf.R;
import com.senter.demo.uhf.common.DestinationTagSpecifics.ProtocolType;
import com.senter.demo.uhf.util.DataTransfer;
import com.senter.support.openapi.StUhf.AccessPassword;
import com.senter.support.openapi.StUhf.Bank;
import com.senter.support.openapi.StUhf.InterrogatorModelB.UmbSelectionRecord.SelectionCommand;
import com.senter.support.openapi.StUhf.InterrogatorModelC.UmcMask;
import com.senter.support.openapi.StUhf.InterrogatorModelC.UmcMask.MaskMode;
import com.senter.support.openapi.StUhf.InterrogatorModelC.UmcOnNewUiiInventoried;
import com.senter.support.openapi.StUhf.InterrogatorModelDs.UmdErrorCode;
import com.senter.support.openapi.StUhf.InterrogatorModelDs.UmdFrequencyPoint;
import com.senter.support.openapi.StUhf.InterrogatorModelDs.UmdOnIso18k6bInventory;
import com.senter.support.openapi.StUhf.InterrogatorModelDs.UmdOnIso18k6cRealTimeInventory;
import com.senter.support.openapi.StUhf.InterrogatorModelDs.UmdRssi;
import com.senter.support.openapi.StUhf.KillPassword;
import com.senter.support.openapi.StUhf.OnNewUiiInventoried;
import com.senter.support.openapi.StUhf.UID;
import com.senter.support.openapi.StUhf.UII;


public class DestinationTagSpecifics {
    Activity mContext;
    View mDestOpLayout;
    View mDestOpLayout_dst;

    Spinner mSpinner;

    AdditionalSpecifidTag opType_SpecifidTag;
    AdditionalAnyTag opType_anyTag;
    AdditionalMatchedTag opType_matchedTag;
    TargetTagType[] opTypes;
    private final ProtocolType protocolType;


    EditText mAccessPasswordEditText;
    LinearLayout mAccessPasswordLayout;

    LinearLayout mKillPasswordLayout;
    EditText mKillPasswordEditText;

    boolean isKillPasswordNeeded = false;
    boolean isAccessPasswordNeeded = false;

    public DestinationTagSpecifics(Activity context, View ll, ProtocolType dstType, PasswordType pwdType, TargetTagType... funcs) {
        this.mContext = context;
        this.mDestOpLayout = ll;
        this.mDestOpLayout_dst = mDestOpLayout.findViewById(R.id.idE2CommenDestOpTypes_llDest);

        this.protocolType = dstType;
        this.opTypes = funcs.clone();

        LinearLayout llSpecifiedTag = (LinearLayout) ll.findViewById(R.id.idE2CommenDestOpTypes_llAdditional_llSpecifiedTag);
        opType_SpecifidTag = new AdditionalSpecifidTag(llSpecifiedTag);

        LinearLayout llAnyTag = (LinearLayout) ll.findViewById(R.id.idE2CommenDestOpTypes_llAdditional_llAnyTag);
        opType_anyTag = new AdditionalAnyTag(llAnyTag);

        LinearLayout llMatchedTag = (LinearLayout) ll.findViewById(R.id.idE2CommenDestOpTypes_llAdditional_llMatchedTag);
        opType_matchedTag = new AdditionalMatchedTag(llMatchedTag);

        mSpinner = (Spinner) ll.findViewById(R.id.idE2CommenDestOpTypes_spnrTypes);

        final ArrayList<HashMap<String, String>> spinnerElements = new ArrayList<HashMap<String, String>>();
        {
            for (int i = 0; i < funcs.length; i++) {
                switch (funcs[i]) {
                    case SpecifiedTag:

                        HashMap<String, String> hashMap = new HashMap<String, String>();
                        hashMap.put("0", mContext.getString(R.string.SpecifyLabel));
                        hashMap.put(TargetTagType.class.getSimpleName(), TargetTagType.SpecifiedTag.name());
                        spinnerElements.add(hashMap);
                        break;

                    case SingleTag:

                        HashMap<String, String> hashMap1 = new HashMap<String, String>();
                        hashMap1.put("0", mContext.getString(R.string.AnyLabel));
                        hashMap1.put(TargetTagType.class.getSimpleName(), TargetTagType.SingleTag.name());
                        spinnerElements.add(hashMap1);
                        break;

                    case MatchTag:

                        HashMap<String, String> hashMap2 = new HashMap<String, String>();
                        hashMap2.put("0", mContext.getString(R.string.AnyMatch));
                        hashMap2.put(TargetTagType.class.getSimpleName(), TargetTagType.MatchTag.name());
                        spinnerElements.add(hashMap2);
                        break;

                    default:
                        break;
                }
            }
            SimpleAdapter sa = new SimpleAdapter(mContext, spinnerElements, android.R.layout.simple_spinner_item, new String[]{"0"}, new int[]{android.R.id.text1});
            sa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            mSpinner.setAdapter(sa);
        }
        mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String t = spinnerElements.get(position).get(TargetTagType.class.getSimpleName());
                TargetTagType type = TargetTagType.valueOf(t);
                switch (type) {
                    case SpecifiedTag:
                        opType_anyTag.display(false);
                        opType_matchedTag.display(false);
                        opType_SpecifidTag.display(true);
                        break;

                    case SingleTag:
                        opType_SpecifidTag.display(false);
                        opType_matchedTag.display(false);
                        opType_anyTag.display(true);
                        break;

                    case MatchTag:
                        opType_SpecifidTag.display(false);
                        opType_anyTag.display(false);
                        opType_matchedTag.display(true);
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                notifiCurrentState();
            }
        });


        mAccessPasswordLayout = (LinearLayout) mDestOpLayout.findViewById(R.id.idE2CommenDestOpTypes_llApwd);
        mAccessPasswordEditText = (EditText) ll.findViewById(R.id.idE2CommenDestOpTypes_llApwd_etApwd);
        mAccessPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                notifiCurrentState();
            }
        });

        mKillPasswordLayout = (LinearLayout) mDestOpLayout.findViewById(R.id.idE2CommenDestOpTypes_llKpwd);
        mKillPasswordEditText = (EditText) ll.findViewById(R.id.idE2CommenDestOpTypes_llKpwd_etKpwd);
        mKillPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                notifiCurrentState();
            }
        });


        switch (pwdType) {
            case Non: {
                mAccessPasswordLayout.setVisibility(View.GONE);
                mKillPasswordLayout.setVisibility(View.GONE);
                break;
            }
            case Apwd: {
                isAccessPasswordNeeded = true;
                mAccessPasswordLayout.setVisibility(View.VISIBLE);
                mKillPasswordLayout.setVisibility(View.GONE);
                break;
            }
            case Kpwd: {
                isKillPasswordNeeded = true;
                mAccessPasswordLayout.setVisibility(View.GONE);
                mKillPasswordLayout.setVisibility(View.VISIBLE);
                break;
            }
            case ApwdAndKpwd: {
                isAccessPasswordNeeded = isKillPasswordNeeded = true;
                mAccessPasswordLayout.setVisibility(View.VISIBLE);
                mKillPasswordLayout.setVisibility(View.VISIBLE);
                break;
            }
            default:
                break;
        }


    }

    OnDestOpTypesLisener mOnReadyLisener;

    public void setOnReadyLisener(OnDestOpTypesLisener lisener) {
        this.mOnReadyLisener = lisener;
        if (lisener != null) {
            lisener.onReadyStateChanged(checkReady());
        }
    }

    /**
     * Check whether the current condition is satisfied
     */
    public boolean checkReady() {
        boolean okWithoutKpwd = (opType_SpecifidTag.isReady() || opType_anyTag.isReady() || opType_matchedTag.isReady());
        if (okWithoutKpwd == false) {
            return false;
        }
        if (this.isAccessPasswordNeeded == true && getAccessPassword() == null) {
            return false;
        }
        if (this.isKillPasswordNeeded == true && getKillPassword() == null) {
            return false;
        }

        return true;
    }

    /**
     * notify Activity whether or not settings on ui is ready
     */
    private void notifiCurrentState() {
        OnDestOpTypesLisener ll = mOnReadyLisener;
        if (ll != null) {
            ll.onReadyStateChanged(checkReady());
        }
    }

    /**
     * get the uii on ui element
     */
    public UII getDstTagUiiIfOrdered() {
        if (isOrderedUii()) {
            try {
                return opType_SpecifidTag.getUii();
            } catch (Exception e) {
            }
        }
        return null;
    }


    /**
     * get uid
     */
    public UID getDstTagUidIfOrdered() {
        if (isOrderedUid()) {
            try {
                return opType_SpecifidTag.getUid();
            } catch (Exception e) {
            }
        }
        return null;
    }

    public boolean isOrderedUii() {
        return (protocolType == ProtocolType.Iso18k6C) && (getCurrentTargetTagType() == TargetTagType.SpecifiedTag);
    }

    public boolean isOrderedUid() {
        return (protocolType == ProtocolType.Iso18k6B) && (getCurrentTargetTagType() == TargetTagType.SpecifiedTag);
    }

    public void setVisible(int visibility) {
        mDestOpLayout.setVisibility(visibility);
    }

    public void setDestinationVisible(int visibility) {
        mDestOpLayout_dst.setVisibility(visibility);
    }

    /**
     * get killpassword from ui element
     */
    public KillPassword getKillPassword() {
        if (this.isKillPasswordNeeded == false) {
            throw new IllegalAccessError("apwd is no need now");
        }
        String pwd = mKillPasswordEditText.getText().toString();
        pwd = pwd.replaceAll(" ", "");

        if (pwd.length() == 8) {
            byte[] bs = DataTransfer.getBytesByHexString(pwd);
            if (bs == null || bs.length != 4) {
                return null;
            }
            KillPassword ap = KillPassword.getNewInstance(bs);
            return ap;
        }
        return null;
    }

    /**
     * get access password from ui element
     */
    public AccessPassword getAccessPassword() {
        if (this.isAccessPasswordNeeded == false) {
            throw new IllegalAccessError("apwd is no need now");
        }
        String pwd = mAccessPasswordEditText.getText().toString();
        pwd = pwd.replaceAll(" ", "");

        if (pwd.length() == 8) {
            byte[] bs = DataTransfer.getBytesByHexString(pwd);
            if (bs == null || bs.length != 4) {
                return null;
            }
            AccessPassword ap = AccessPassword.getNewInstance(bs);
            return ap;
        }
        return null;
    }

    /**
     * clear password on ui
     */
    public void clearApwdAndKpwd() {
        mAccessPasswordEditText.setText("");
        mKillPasswordEditText.setText("");
    }

    public static enum TargetTagType {
        SingleTag, SpecifiedTag, MatchTag;
    }

    public static enum PasswordType {
        Non, Apwd, Kpwd, ApwdAndKpwd;
    }

    public static enum ProtocolType {
        Iso18k6C, Iso18k6B;
    }

    /**
     * get target tag type
     */
    public TargetTagType getCurrentTargetTagType() {
        if (opType_SpecifidTag.isDiaplaying()) {
            return TargetTagType.SpecifiedTag;
        }
        if (opType_anyTag.isDiaplaying()) {
            return TargetTagType.SingleTag;
        }
        if (opType_matchedTag.isDiaplaying()) {
            return TargetTagType.MatchTag;
        }
        return null;
    }

    public static interface OnDestOpTypesLisener {
        public void onReadyStateChanged(boolean isReadyNow);
    }

    private class AdditionalAbstarct {
        LinearLayout mLayout;
        private boolean absIsReady = false;

        public AdditionalAbstarct(LinearLayout ll) {
            mLayout = ll;
        }

        public boolean isDiaplaying() {
            return mLayout.getVisibility() == View.VISIBLE;
        }

        public void display(boolean b) {
            if (b) {
                mLayout.setVisibility(View.VISIBLE);
            } else {
                mLayout.setVisibility(View.GONE);
            }
            notifiCurrentState();
        }

        public void notifyOwnerToast(final String string, final int duration) {
            mContext.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(mContext, string, duration).show();
                }
            });
        }

        protected void updateStateAndNotify(boolean ok) {
            absIsReady = ok;
            if (isDiaplaying()) {
                DestinationTagSpecifics.this.notifiCurrentState();
            }
        }

        public boolean isReady() {
            return isDiaplaying() && absIsReady;
        }
    }

    private class AdditionalSpecifidTag extends AdditionalAbstarct {
        EditText etEpc;
        Button btnFind;

        public AdditionalSpecifidTag(LinearLayout ll) {
            super(ll);
            etEpc = (EditText) ll.findViewById(R.id.idE2CommenDestOpTypes_llAdditional_llSpecifiedTag_etFoundTag);
            btnFind = (Button) ll.findViewById(R.id.idE2CommenDestOpTypes_llAdditional_llSpecifiedTag_btnFindATag);
            btnFind.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onFind();
                }
            });
        }

        private void onFind() {
            SearchDlg sDlg = new SearchDlg(mContext, protocolType) {
                @Override
                protected void onUiiSelected(final String uii) {
                    mContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            etEpc.setText(uii);
                            notifiCurrentState();
                        }
                    });
                }
            };
            try {
                sDlg.show();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void display(boolean b) {
            super.display(b);
            if (b) {
                switch (protocolType) {
                    case Iso18k6C: {
                        if (this.getUii() != null) {
                            updateStateAndNotify(true);
                        }
                        break;
                    }
                    case Iso18k6B: {
                        if (this.getUid() != null) {
                            updateStateAndNotify(true);
                        }
                        break;
                    }
                    default:
                        break;
                }
            }
        }

        public UII getUii() {
            UII ret = null;
            try {
                byte[] uii = DataTransfer.getBytesByHexString(etEpc.getText().toString());
                ret = UII.getNewInstanceByBytes(uii, 0);
                if (uii == null || uii.length < 2) {
                    ret = null;
                }
            } catch (Exception e) {// swallow the cause exception message
                // SuppressedException e.printStackTrace();
            }
            return ret;
        }

        public UID getUid() {
            UID ret = null;
            try {
                byte[] uid = DataTransfer.getBytesByHexString(etEpc.getText().toString());
                ret = UID.newUidInstance(uid);
            } catch (Exception e) {// // swallow the cause exception message
                // SuppressedException e.printStackTrace();
            }
            return ret;
        }

        @Override
        public boolean isReady() {
            if (isDiaplaying() == false) {
                return false;
            }

            switch (protocolType) {
                case Iso18k6C: {
                    if (this.getUii() != null) {
                        return true;
                    }
                    break;
                }
                case Iso18k6B: {
                    if (this.getUid() != null) {
                        return true;
                    }
                    break;
                }
                default:
                    break;
            }

            return false;
        }
    }

    private class AdditionalAnyTag extends AdditionalAbstarct {
        public AdditionalAnyTag(LinearLayout ll) {
            super(ll);
            updateStateAndNotify(true);// settings ready
        }

        @Override
        public void display(boolean b) {
            super.display(b);
            if (b == true) {
                App.clearMaskSettings();
                updateStateAndNotify(true);// ready
            }
        }
    }

    private class AdditionalMatchedTag extends AdditionalAbstarct {
        Button setMatch;

        public AdditionalMatchedTag(LinearLayout ll) {
            super(ll);
            setMatch = (Button) ll.findViewById(R.id.idE2CommenDestOpTypes_llAdditional_llMatchedTag_btnSetMatch);
            setMatch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onMatchSet();
                }
            });
        }

        private void onMatchSet() {
            View view = LayoutInflater.from(mContext).inflate(R.layout.common_dstoptypes_matchdlg, null);
            final Spinner sBank = (Spinner) view.findViewById(R.id.idMainActivityAddSelectionDlg_spinerBank);
            final EditText sPointer = (EditText) view.findViewById(R.id.idMainActivityAddSelectionDlg_etPointer);
            final EditText sMaskLen = (EditText) view.findViewById(R.id.idMainActivityAddSelectionDlg_etMaskLength);

            final EditText sMask = (EditText) view.findViewById(R.id.idMainActivityAddSelectionDlg_etMask);
            sBank.setSelection(2);// set Tid as default?

            sBank.setEnabled(false);
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setView(view);
            builder.setPositiveButton(R.string.Set, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int bank = sBank.getSelectedItemPosition();
                    int poit;
                    try {
                        poit = Integer.valueOf(sPointer.getText().toString(), 16);
                    } catch (Exception e) {
                        e.printStackTrace();
                        notifyOwnerToast(mContext.getString(R.string.InputStartAddr), Toast.LENGTH_SHORT);
                        updateStateAndNotify(false);
                        return;
                    }

                    String msk = sMask.getText().toString();
                    if (msk.length() == 0 || msk.length() % 2 != 0) {
                        notifyOwnerToast(mContext.getString(R.string.MastInput_Prompt), Toast.LENGTH_SHORT);
                        updateStateAndNotify(false);
                        return;
                    }
                    byte[] mask = new byte[msk.length() / 2];
                    for (int i = 0; i < msk.length() / 2; i++) {
                        mask[i] = (byte) (Integer.valueOf(msk.substring(2 * i, 2 * i + 2), 16) & 0xff);
                    }

                    int maskLength = 0;
                    String mskLen = sMaskLen.getText().toString();
                    if (mskLen.length() != 0) {
                        maskLength = Integer.valueOf(mskLen);
                    }

                    if (maskLength > mask.length * 8) {
                        notifyOwnerToast(mContext.getString(R.string.MastLength_Insufficient), Toast.LENGTH_SHORT);
                        return;
                    }
                    Boolean rslt = false;

                    switch (App.uhfInterfaceAsModel()) {
                        case InterrogatorModelB:
                            SelectionCommand cmd = SelectionCommand.getNewInstance(Bank.ValueOf(bank), poit + 32, maskLength, mask);
                            rslt = App.uhfInterfaceAsModelB().addFilter(1, cmd);
                            if (rslt == true) {
                                rslt = App.uhfInterfaceAsModelB().selectFilterByIndex(1, 1);
                            }
                            break;
                        case InterrogatorModelC:
                            rslt = App.uhfInterfaceAsModelC().setMaskEnable(UmcMask.getInstanceToMatchEpcInEpcBank(MaskMode.Match, poit, maskLength, mask));
                        default:
                            break;
                    }
                    if (rslt == true) {
                        notifyOwnerToast(mContext.getString(R.string.SetOk), Toast.LENGTH_SHORT);

                        {// notify activty the settings is ready
                            updateStateAndNotify(true);
                        }
                    } else {
                        notifyOwnerToast(mContext.getString(R.string.SetFailure), Toast.LENGTH_SHORT);

                        switch (App.uhfInterfaceAsModel()) {
                            case InterrogatorModelB:
                                App.uhfInterfaceAsModelB().selectFilterByIndex(0, 1);
                                break;
                            case InterrogatorModelC:
                                App.uhfInterfaceAsModelC().setMaskDisable();
                                break;
                            default:
                                break;
                        }

                        {//  notify activty the settings is ready
                            updateStateAndNotify(false);
                        }
                    }
                }
            });
            builder.create().show();
        }

        @Override
        public void display(boolean b) {
            super.display(b);
            if (b == false) {
                App.clearMaskSettings();
                updateStateAndNotify(false);
            }
        }
    }
}

abstract class SearchDlg {
    Activity ownerContext;
    AlertDialog dialog;
    ArrayAdapter<String> la;
    ArrayList<String> laData = new ArrayList<String>();
    ProtocolType protocolType;

    SearchDlg(Activity context, ProtocolType protocolType) {
        ownerContext = context;
        this.protocolType = protocolType;

        View view = LayoutInflater.from(ownerContext).inflate(R.layout.tagsearchdialog, null);
        final ListView lv = (ListView) view.findViewById(R.id.idTagSearchDialog_lvTags);
        la = new ArrayAdapter<String>(ownerContext, android.R.layout.simple_list_item_single_choice, laData);
        lv.setAdapter(la);
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        AlertDialog.Builder adb = new AlertDialog.Builder(ownerContext);
        adb.setView(view);
        adb.setTitle(R.string.ScanningLabel);
        adb.setPositiveButton(R.string.OK, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                App.stop();
                String s;
                if (lv.getCheckedItemPosition() == ListView.INVALID_POSITION) {
                    s = "";
                } else {
                    int p = lv.getCheckedItemPosition();
                    s = laData.get(p);
                }
                onUiiSelected(s);
            }
        });
        adb.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                App.stop();
            }
        });
        dialog = adb.create();
    }

    public void show() throws InterruptedException {
        ownerContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        });
        App.stop();

        switch (App.uhfInterfaceAsModel()) {
            case InterrogatorModelA: {
                App.uhfInterfaceAsModelA().startInventorySingleTag(new OnNewUiiInventoried() {
                    @Override
                    public void onNewUiiReceived(UII uii) {
                        addTag(uii);
                    }
                });
                break;
            }
            case InterrogatorModelB: {
                App.uhfInterfaceAsModelB().startInventorySingleTag(new OnNewUiiInventoried() {
                    @Override
                    public void onNewUiiReceived(UII uii) {
                        addTag(uii);
                    }
                });
                break;
            }
            case InterrogatorModelC: {
                App.uhfInterfaceAsModelC().startInventorySingleTag(new UmcOnNewUiiInventoried() {

                    @Override
                    public void onNewTagInventoried(UII uii) {
                        addTag(uii);
                    }

                    @Override
                    public void onEnd(int errorId) {

                    }
                });
                break;
            }
            case InterrogatorModelD2: {

                switch (protocolType) {
                    case Iso18k6C: {
                        App.uhfInterfaceAsModelD2().iso18k6cRealTimeInventory(1, new UmdOnIso18k6cRealTimeInventory() {

                            @Override
                            public void onTagInventory(UII uii, UmdFrequencyPoint frequencyPoint, Integer antennaId, UmdRssi rssi) {
                                addTag(uii);
                            }

                            @Override
                            public void onFinishedWithError(UmdErrorCode error) {

                            }

                            @Override
                            public void onFinishedSuccessfully(Integer antennaId, int readRate, int totalRead) {

                            }
                        });
                        break;
                    }
                    case Iso18k6B: {
                        App.uhfInterfaceAsModelD2().iso18k6bInventory(new UmdOnIso18k6bInventory() {

                            @Override
                            public void onTagInventory(Iso18k6bInventoryResult rslt, int antId) {
                                addTag(rslt.getUid());
                            }

                            @Override
                            public void onFinishedWithError(UmdErrorCode error) {

                            }

                            @Override
                            public void onFinishedSuccessfully(int antId, int tagFound) {

                            }
                        });
                        break;
                    }
                    default:
                        break;
                }
                break;
            }
            default:
                break;
        }
    }

    private void addTag(final UII uii) {
        ownerContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.setTitle(R.string.SelectOneLabel);

                String string = DataTransfer.xGetString(uii.getBytes());
                for (int i = 0; i < laData.size(); i++) {
                    if (string.equals(laData.get(i))) {
                        return;
                    }
                }
                laData.add(string);
                la.notifyDataSetChanged();
            }
        });
    }

    private void addTag(final UID uid) {
        ownerContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.setTitle(R.string.SelectOneLabel);

                String string = DataTransfer.xGetString(uid.getBytes());
                for (int i = 0; i < laData.size(); i++) {
                    if (string.equals(laData.get(i))) {
                        return;
                    }
                }
                laData.add(string);
                la.notifyDataSetChanged();
            }
        });
    }

    protected abstract void onUiiSelected(String uii);
}
