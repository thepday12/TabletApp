package com.senter.demo.uhf.record;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.senter.demo.uhf.R;

public class ActivityRecordsList extends Activity {
    RecordsAdapter mListAdapterBrowseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.records_list);
        ListView lv = (ListView) findViewById(R.id.idRecords_Records_listview);
        mListAdapterBrowseList = new RecordsAdapter(ActivityRecordsList.this, lv);

        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File file = mListAdapterBrowseList.getFileByPosition(position);
                String fpn = file.getAbsolutePath();
                startActivity(new Intent(ActivityRecordsList.this, ActivityRecordDisplayer.class).putExtra("Name", fpn));
            }
        });

        ((Button) findViewById(R.id.btn_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listFiles();
    }

    void listFiles() {
        String path = RecordRWer.path();
        File fDevice = new File(path);

        List<File> mflist = new ArrayList<File>();
        String[] fs = fDevice.list();
        if (fs != null) {
            for (int i = 0; i < fs.length; i++) {
                if (fs[i] != null) {
                    if (fs[i].endsWith(RecordRWer.suffixRecord)) {
                        mflist.add(new File(path + fs[i]));
                    }
                }
            }
        }
        mListAdapterBrowseList.show(mflist);
    }
}

class RecordsAdapter extends BaseAdapter {

    private List<File> mFs = null;
    protected Context context;
    ListView mListView;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:MM");

    public RecordsAdapter(Context context, ListView lv) {
        this.context = context;
        mFs = new ArrayList<File>();
        mListView = lv;
        mListView.setAdapter(this);
    }

    private class ViewHolder {
        TextView name;
        TextView size;
        TextView time;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            LayoutInflater myInflater = LayoutInflater.from(context);
            view = myInflater.inflate(R.layout.records_list_ele, null);
            holder = new ViewHolder();
            holder.name = (TextView) view.findViewById(R.id.idRecordsListEle_tvFileName);
            holder.size = (TextView) view.findViewById(R.id.idRecordsListEle_tvLength);
            holder.time = (TextView) view.findViewById(R.id.idRecordsListEle_tvTime);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.name.setText(mFs.get(position).getName());

        String string = "";
        float iSize = mFs.get(position).length();
        if (iSize > 1024 * 1024) {
            string = String.format("%.2f M", (iSize / (1024 * 1024)));
        } else if (iSize > 1024) {
            string = (iSize / 1024 + 1) + "kB";
        } else {
            string = iSize + "B";
        }
        holder.size.setText(string);

        holder.time.setText(dateFormat.format(new Date(mFs.get(position).lastModified())));

        view.setOnClickListener(mOnClickListener);
        view.setOnKeyListener(mOnKeyListener);
        return view;
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            try {
                ViewHolder mvh = (ViewHolder) v.getTag();
                String string = mvh.name.getText().toString();
                for (int i = 0; i < mFs.size(); i++) {
                    if (mFs.get(i).getName().equals(string)) {
                        string = mFs.get(i).getAbsolutePath();
                        string = mFs.get(i).getName();
                        string = mFs.get(i).getAbsoluteFile().getAbsolutePath();
                        break;
                    }
                }
                context.startActivity(new Intent(context, ActivityRecordDisplayer.class).putExtra(ActivityRecordDisplayer.KeyOnBundleExtra4FileName, string));
                ((Activity) context).finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    View.OnKeyListener mOnKeyListener = new View.OnKeyListener() {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            // TODO Auto-generated method stub
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                try {
                    ViewHolder mvh = (ViewHolder) v.getTag();
                    String string = mvh.name.getText().toString();
                    for (int i = 0; i < mFs.size(); i++) {
                        if (mFs.get(i).getName().equals(string)) {
                            string = mFs.get(i).getAbsolutePath();
                        }
                    }
                    context.startActivity(new Intent(context, ActivityRecordDisplayer.class).putExtra(ActivityRecordDisplayer.KeyOnBundleExtra4FileName, string));
                    ((Activity) context).finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return false;
        }
    };

    @Override
    public int getCount() {
        return mFs != null ? mFs.size() : 0;
    }

    @Override
    public Object getItem(int arg0) {
        if ((mFs != null && mFs.size() > 0) && (arg0 >= 0 && arg0 < mFs.size())) {
            return mFs.get(arg0);
        }
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    public void show(List<File> fs) {
        mFs.clear();
        mFs.addAll(fs);
        notifyDataSetChanged();
    }

    public File getFileByPosition(int i) {
        return mFs.get(i);
    }
}
