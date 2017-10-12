package com.senter.demo.common.misc;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;

public class BroadcastReceiverHelper {

    public static final BroadcastReceiverHelper newInstance(Context ownersContext) {
        if (ownersContext == null) {throw new IllegalArgumentException();}

        return new BroadcastReceiverHelper(ownersContext);
    }

    private Context ownersContext;

    private BroadcastReceiverHelper(Context ownersContext) {
        if (ownersContext == null) {throw new IllegalArgumentException();}

        this.ownersContext = ownersContext;
    }

    private HashMap<String, ArrayList<BroadcastProcessor>> actionsHashMap = new HashMap<String, ArrayList<BroadcastProcessor>>();

    String[] actions;

    public synchronized BroadcastReceiverHelper on(String... actions) {
        if (actions == null || actions.length == 0) {throw new IllegalArgumentException();}
        if (isUninted) throw new IllegalStateException();
        if (frozen) throw new IllegalStateException();
        if (this.actions != null) throw new IllegalStateException();

        this.actions = actions.clone();
        return this;
    }

    public synchronized BroadcastReceiverHelper exec(BroadcastProcessor exec) {
        if (exec == null) {throw new IllegalArgumentException();}
        if (isUninted) throw new IllegalStateException();
        if (frozen) throw new IllegalStateException();
        if (actions == null) throw new IllegalStateException();

        for (String s : this.actions) {
            ArrayList<BroadcastProcessor> ls = actionsHashMap.get(s);
            if (ls == null) {
                ls = new ArrayList<BroadcastProcessor>();
            }
            ls.add(exec);
            actionsHashMap.put(s, ls);
        }
        this.actions = null;
        return this;
    }

    private boolean frozen = false;
    private boolean regested = false;

    BroadcastReceiver broadcastReceiver;

    public synchronized BroadcastReceiverHelper start() {
        if (isUninted) throw new IllegalStateException();

        if (frozen == false) frozen = true;

        if (broadcastReceiver == null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    ArrayList<BroadcastProcessor> brls = actionsHashMap.get(intent.getAction());
                    if (brls != null) {
                        for (BroadcastProcessor brl : brls) {
                            try {
                                brl.process(context, intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        throw new IllegalStateException();
                    }
                }
            };
        }
        if (regested == false) {
            IntentFilter ifFilter = new IntentFilter();
            for (String s : actionsHashMap.keySet()) {
                ifFilter.addAction(s);
            }
            ownersContext.registerReceiver(broadcastReceiver, ifFilter, broadcastPermission, handler);
            regested = true;
        }
        return this;
    }

    public synchronized void stop() {
        if (regested) {
            ownersContext.unregisterReceiver(broadcastReceiver);
            regested = false;
        }
    }

    private boolean isUninted = false;

    public synchronized void uninit() {
        if (isUninted == true) {
            return;
        }
        stop();
        if (handler != null) {
            handler.getLooper().quit();
        }
        isUninted = true;
    }

    Handler handler;

    public synchronized BroadcastReceiverHelper setHandlerWithNewLooper(String name) {
        if (frozen) throw new IllegalStateException();
        if (regested) throw new IllegalStateException();
        if (isUninted) throw new IllegalStateException();
        if (handler != null) throw new IllegalStateException();
        HandlerThread ht = new HandlerThread(name != null ? name : "");
        ht.start();
        handler = new Handler(ht.getLooper());

        return this;
    }

    String broadcastPermission;

    public synchronized BroadcastReceiverHelper setBroadcastPermission(String broadcastPermission) {
        if (frozen) throw new IllegalStateException();
        if (regested) throw new IllegalStateException();
        if (isUninted) throw new IllegalStateException();
        if (broadcastPermission != null) throw new IllegalStateException();
        this.broadcastPermission = broadcastPermission;

        return this;
    }

    public interface BroadcastProcessor {
        public void process(Context context, Intent intent) throws Exception;
    }
}
