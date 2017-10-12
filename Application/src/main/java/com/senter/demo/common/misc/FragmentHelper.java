package com.senter.demo.common.misc;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;


public class FragmentHelper<TypeOfFragment extends Fragment> {
    private final TypeOfFragment ownersFragment;

    public FragmentHelper(TypeOfFragment activity) {
        ownersFragment = activity;
    }

    public final void showToastShort(final int text) {
        ownersFragment.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ownersFragment.getActivity(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public final void showToastShort(final String text) {
        ownersFragment.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ownersFragment.getActivity(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public final void showDialog(final Dialog dialog) {
        ownersFragment.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        });
    }

    public final void showToastLong(final String text) {
        ownersFragment.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ownersFragment.getActivity(), text, Toast.LENGTH_LONG).show();
            }
        });
    }

    public final void showToastLong(final int text) {
        ownersFragment.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ownersFragment.getActivity(), text, Toast.LENGTH_LONG).show();
            }
        });
    }

    public TypeOfFragment fragement() {
        return ownersFragment;
    }

    public <TypeOfTargetActivity extends Activity> void startActivity(Class<TypeOfTargetActivity> t) {
        ownersFragment.startActivity(new Intent(ownersFragment.getActivity(), t).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public final Button buttonWithClickListener(int id, View.OnClickListener onClickListener) {
        Button btn = (Button) ownersFragment.getView().findViewById(id);
        btn.setOnClickListener(onClickListener);
        return btn;
    }

    public final View findViewById(int id) {
        return ownersFragment.getView().findViewById(id);
    }

    public final ImageView imageViewWithClickListener(int id, View.OnClickListener onClickListener) {
        ImageView btn = (ImageView) ownersFragment.getView().findViewById(id);
        btn.setOnClickListener(onClickListener);
        return btn;
    }

    public final ListView listViewWithAdapterAndOnItemClickListener(int listViewId, BaseAdapter adapter, AdapterView.OnItemClickListener itemClickListener,
                                                                    AdapterView.OnItemLongClickListener itemLongClickListener) {
        ListView lv = (ListView) ownersFragment.getView().findViewById(listViewId);
        lv.setAdapter(adapter);
        if (itemClickListener != null) {
            lv.setOnItemClickListener(itemClickListener);
        }
        if (itemLongClickListener != null) {
            lv.setOnItemLongClickListener(itemLongClickListener);
        }
        return lv;
    }


    public int intValueOfText(TextView tv) throws NumberFormatException {
        return Integer.valueOf(tv.getText().toString());
    }

    public String textOf(TextView tv) throws NumberFormatException {
        return tv.getText().toString();
    }
}
