package com.senter.demo.uhf.common;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.senter.demo.uhf.R;

public class RecordsBoard {
    private final Activity owner;

    private final ListView lv;
    private final ArrayList<HashMap<String, String>> dataMerged = new ArrayList<HashMap<String, String>>();
    private final SimpleAdapter adapterMergedData;

    private final TextView tvCount;

    public RecordsBoard(Activity context, View ll) {
        owner = context;

        adapterMergedData = new SimpleAdapter(owner, dataMerged, R.layout.common_lv2overlap_tv2, new String[]{"0", "1"}, new int[]{R.id.idE2CommenLv2OverlapTv1_tv0, R.id.idE2CommenLv2OverlapTv1_tv1});

        lv = (ListView) ll.findViewById(R.id.idE2CommenLv2Overlap_lvShow);
        lv.setAdapter(adapterMergedData);


        tvCount = (TextView) ll.findViewById(R.id.idE2CommenLv2Overlap_tvTagsCount);
    }

    public void addMassage(final String string) {
        owner.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // update the merged data list
                int index = 0;
                for (; index < dataMerged.size(); index++) {
                    HashMap<String, String> omap = dataMerged.get(index);
                    if (omap.get("0").equals(string)) {// found it
                        int num = 1;

                        String numString = omap.get("1");
                        if (numString != null) {
                            try {
                                num = Integer.valueOf(numString);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        num++;
                        omap.put("1", "" + num);
                        break;
                    }
                }
                if (index == dataMerged.size()) {// not found in merged data list
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("0", string);
                    map.put("1", "" + 1);
                    dataMerged.add(map);
                }
                tvCount.setText("" + dataMerged.size());

                adapterMergedData.notifyDataSetChanged();
            }
        });
    }

    public void resetMassages(final ArrayList<HashMap<String, String>> msgs) {
        owner.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dataMerged.clear();
                dataMerged.addAll(msgs);

                tvCount.setText("" + dataMerged.size());

                adapterMergedData.notifyDataSetChanged();
            }
        });
    }


    public void clearMsg() {// clear data
        owner.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dataMerged.clear();
                tvCount.setText("" + dataMerged.size());
                adapterMergedData.notifyDataSetChanged();
            }
        });
    }

    public ArrayList<HashMap<String, String>> getData() {
        ArrayList<HashMap<String, String>> ret = new ArrayList<HashMap<String, String>>();
        ret.addAll(dataMerged);
        return ret;
    }

    public boolean isEmpty() {
        return dataMerged.isEmpty();
    }
}
