package com.senter.demo.common.misc;

import java.util.HashMap;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityHelper<TypeOfActivity extends Activity> {
    private final TypeOfActivity ownersActivity;

    public ActivityHelper(TypeOfActivity activity) {
        ownersActivity = activity;
    }

    public final void showToastShort(final int text) {
        ownersActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ownersActivity, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public final void showToastShort(final String text) {
        ownersActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ownersActivity, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public final void showDialog(final Dialog dialog) {
        ownersActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        });
    }

    public final void showToastLong(final String text) {
        ownersActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ownersActivity, text, Toast.LENGTH_LONG).show();
            }
        });
    }

    public final void showToastLong(final int text) {
        ownersActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ownersActivity, text, Toast.LENGTH_LONG).show();
            }
        });
    }

    public TypeOfActivity activity() {
        return ownersActivity;
    }

    public <TypeOfTargetActivity extends Activity> void startActivity(Class<TypeOfTargetActivity> t) {
        ownersActivity.startActivity(new Intent(ownersActivity, t).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public final Button buttonWithClickListener(int id, View.OnClickListener onClickListener) {
        Button btn = (Button) ownersActivity.findViewById(id);
        btn.setOnClickListener(onClickListener);
        return btn;
    }

    public final View findViewById(int id) {
        return ownersActivity.findViewById(id);
    }

    public final ImageView imageViewWithClickListener(int id, View.OnClickListener onClickListener) {
        ImageView btn = (ImageView) ownersActivity.findViewById(id);
        btn.setOnClickListener(onClickListener);
        return btn;
    }

    public final ListView listViewWithAdapterAndOnItemClickListener(int listViewId, BaseAdapter adapter, OnItemClickListener itemClickListener,
                                                                    OnItemLongClickListener itemLongClickListener) {
        ListView lv = (ListView) ownersActivity.findViewById(listViewId);
        lv.setAdapter(adapter);
        if (itemClickListener != null) {
            lv.setOnItemClickListener(itemClickListener);
        }
        if (itemLongClickListener != null) {
            lv.setOnItemLongClickListener(itemLongClickListener);
        }
        return lv;
    }


    private final HashMap<Integer, OnActivityResultProcessor> onActivityResultProcessors = new HashMap<Integer, OnActivityResultProcessor>();


    public void removeOnActivityResultProcessor(int requestCode) {
        if (onActivityResultProcessors.containsKey(requestCode) == false) throw new IllegalArgumentException();
        onActivityResultProcessors.remove(requestCode);
    }

    public void setOnActivityResultProcessor(int requestCode, OnActivityResultProcessor onActivityResultProcessor) {
        if (onActivityResultProcessor == null) throw new IllegalArgumentException("if you want to remove,use removeOnActivityResultProcessor");
        onActivityResultProcessors.put(requestCode, onActivityResultProcessor);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        OnActivityResultProcessor p = onActivityResultProcessors.get(requestCode);
        if (p == null) {
            return;
        }
        p.onActivityResult(requestCode, resultCode, data);
    }

    public static abstract class OnActivityResultProcessor {
        protected abstract void onActivityResult(int requestCode, int resultCode, Intent intent);
    }

    public int intValueOfText(TextView tv) throws NumberFormatException {
        return Integer.valueOf(tv.getText().toString());
    }

    public String textOf(TextView tv) throws NumberFormatException {
        return tv.getText().toString();
    }
}