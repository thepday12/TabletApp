package com.senter.demo.common.misc;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollectionsAndArrays {
    public static <T> boolean contain(T[] source, T o) {

        if (source == null) throw new IllegalArgumentException();

        for (T t : source) {
            if (t == o) {
                return true;
            } else if (t == null || o == null) {
                continue;
            } else if (t.hashCode() != o.hashCode()) {
                continue;
            } else if (t.equals(o)) {
                return true;
            } else {
                continue;
            }
        }
        return false;
    }

    public static <T> boolean contain(Collection<T> source, T o) {
        if (source == null) throw new IllegalArgumentException();

        for (T t : source) {
            if (t == o) {
                return true;
            } else if (t == null || o == null) {
                continue;
            } else if (t.hashCode() != o.hashCode()) {
                continue;
            } else if (t.equals(o)) {
                return true;
            } else {
                continue;
            }
        }
        return false;
    }

    public static <T> ArrayList<T> add(List<T> source, T... os) {
        return add(source, source != null ? source.size() : 0, os);
    }

    public static <T> ArrayList<T> add(List<T> source, int index, T... os) {

        if (index < 0) throw new IllegalArgumentException();

        ArrayList<T> retList = new ArrayList<T>();

        if (source != null) {
            for (int i = 0; i < source.size(); i++) {
                retList.add(source.get(i));
            }
        }
        if (os != null) {
            for (int i = 0; i < os.length; i++) {
                retList.add(index + i, os[i]);
            }
        }

        return retList;
    }

    public static <T> T[] add(T[] source, T... os) {
        return add(source, source != null ? source.length : 0, os);
    }

    public static <T> T[] add(T[] source, int index, T... os) {
        if (index < 0) throw new IllegalArgumentException();

        List<T> retList = new ArrayList<T>();

        if (source != null) {
            for (int i = 0; i < source.length; i++) {
                retList.add(source[i]);
            }
        }
        if (os != null) {
            for (int i = 0; i < os.length; i++) {
                retList.add(index + i, os[i]);
            }
        }
        return retList.toArray(newTs(source, 0));
    }

    @SuppressWarnings("unchecked")
    private static <T> T[] newTs(T[] ts, int length) {
        if (ts == null) throw new IllegalArgumentException();
        if (length < 0) throw new IllegalArgumentException();

        return (T[]) Array.newInstance(ts.getClass().getComponentType(), length);
    }

    public static <T> int firstIndex(T[] ts, T o) {
        for (int i = 0; i < ts.length; i++) {
            T t = ts[i];
            if (t == o) {
                return i;
            } else if (t == null || o == null) {
                continue;
            } else if (t.hashCode() != o.hashCode()) {
                continue;
            } else if (t.equals(o)) {
                return i;
            } else {
                continue;
            }
        }
        return -1;
    }
}
