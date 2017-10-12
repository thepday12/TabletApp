package com.senter.demo.common.misc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DcByteTools {

    public static final String hexStringOfBySimple(byte... bs) {
        if (bs == null) {
            throw new NullPointerException();
        }

        return hexStringOf(bs);
    }

    public static final String hexStringOf(String prefix, String format4byte, String suffix, byte[] bs) {
        if (format4byte == null || bs == null) {
            throw new NullPointerException();
        }

        StringBuffer sb = new StringBuffer();
        sb.append(prefix);
        for (int i = 0; bs != null && i < bs.length; i++) {
            sb.append(String.format(format4byte, bs[i]));
        }
        sb.append(suffix);
        return sb.toString();
    }

    public static final byte[] bytesOf(Byte... org) {
        byte[] ret = new byte[org.length];
        for (int i = 0; i < ret.length; i++) {
            if (org[i] == null) {
                throw new NullPointerException();
            }
            ret[i] = org[i];
        }
        return ret;
    }

    public static char[] charsOfAsBigEndian(byte[] bytes) {
        if (bytes.length % 2 == 1) {
            throw new IllegalArgumentException();
        }
        char[] ret = new char[bytes.length / 2];
        for (int i = 0; i < ret.length; i++) {
            ret[i] |= DcByteTools.intOfAsH2L(bytes[i * 2]);
            ret[i] <<= 8;
            ret[i] |= DcByteTools.intOfAsH2L(bytes[i * 2 + 1]);
        }
        return ret;
    }

    public static byte[] bytesOfAsBigEndian(char[] chars) {

        byte[] ret = new byte[chars.length * 2];
        for (int i = 0; i < chars.length; i++) {
            ret[i] = (byte) (chars[i] >> 8);
            ret[i + 1] = (byte) chars[i];
        }
        return ret;
    }

    public static final byte[] bytesOfHexString(String hexString) {
        hexString = hexString.replaceAll("\\s", "");
        if (hexString.length() % 2 != 0) {
            throw new IllegalArgumentException();
        }
        BytesBuilder bb = newBytesBuilder();
        for (int i = 0; i < hexString.length() / 2; i++) {
            String ele = hexString.substring(i * 2, i * 2 + 2);
            Integer b = Integer.valueOf(ele, 16);
            bb.append(b.byteValue());
        }
        return bb.toByteArray();
    }

    public static final byte[] subBytesOf(List<Byte> org, int start, int endExcluding) {
        if (endExcluding < start) {
            throw new IllegalArgumentException();
        }
        if (start < 0) {
            throw new IllegalArgumentException();
        }
        if (endExcluding > org.size()) {
            throw new IllegalArgumentException();
        }
        byte[] ret = new byte[endExcluding - start];
        for (int i = 0; i < ret.length; i++) {
            Byte ele = org.get(start + i);
            if (ele == null) {
                throw new NullPointerException();
            }
            ret[i] = ele;
        }
        return ret;
    }

    public static final byte[] subBytesOf(byte[] org, int start, int endExcluding) {
        if (endExcluding < start) {
            throw new IllegalArgumentException();
        }
        if (start < 0) {
            throw new IllegalArgumentException();
        }
        if (endExcluding > org.length) {
            throw new IllegalArgumentException();
        }
        byte[] ret = new byte[endExcluding - start];
        for (int i = 0; i < ret.length; i++) {
            Byte ele = org[start + i];
            ret[i] = ele;
        }
        return ret;
    }

    public static final byte[] subBytesFrom(byte[] org, int start, int endExcluding) {
        byte[] ret = new byte[endExcluding - start];
        for (int i = start; i < endExcluding; i++) {
            ret[i - start] = org[i];
        }
        return ret;
    }

    private static final Integer getIntAsH2L(byte... hi2low) {
        if (hi2low == null) {
            return null;
        }
        if (hi2low.length > 4) {
            throw new IllegalArgumentException();
        }

        byte[] bs = new byte[4];
        int gap = bs.length - hi2low.length;
        for (int i = 0; i < hi2low.length; i++) {
            bs[gap + i] = hi2low[i];
        }

        int i0 = bs[0];
        i0 = i0 & 0xff;
        int i1 = bs[1];
        i1 = i1 & 0xff;
        int i2 = bs[2];
        i2 = i2 & 0xff;
        int i3 = bs[3];
        i3 = i3 & 0xff;
        i0 = i0 << 24;
        i1 = i1 << 16;
        i2 = i2 << 8;
        return i0 | i1 | i2 | i3;
    }

    public static final int intOfAsH2L(byte... hi2low) {
        if (hi2low == null) {
            throw new IllegalArgumentException();
        }
        if (hi2low.length > 4) {
            throw new IllegalArgumentException();
        }

        // if length is 1
        if (hi2low.length == 1) {
            int ret = hi2low[0];
            ret &= 0xff;
            return ret;
        }

        byte[] bs = new byte[hi2low.length];
        for (int i = 0; i < bs.length; i++) {
            byte eleByte = hi2low[i];
            bs[i] = eleByte;
        }

        return getIntAsH2L(bs);
    }

    public static final int unsignedIntOf(byte b) {
        int retVal = b;
        retVal &= 0xff;
        return retVal;
    }

    public static final int intOfAsL2H(byte... low2hi) {
        if (low2hi == null) {
            throw new IllegalArgumentException();
        }
        if (low2hi.length > 4) {
            throw new IllegalArgumentException();
        }

        return getIntAsH2L(swappedBytes(low2hi));
    }

    public final static byte[] bytesOfLongAsL2H(long v, int bytesLength) {
        if (bytesLength < 0) {
            throw new IllegalArgumentException();
        }
        if (bytesLength == 0) {
            return new byte[0];
        }
        byte[] ret = new byte[bytesLength];
        for (int i = 0; i < ret.length; i++) {
            byte b = (byte) (v >> (i * 8));
            ret[i] = b;
        }
        return ret;
    }

    public final static byte[] bytesOfLongAsH2L(long v, int bytesLength) {
        byte[] bb = bytesOfLongAsL2H(v, bytesLength);
        return swappedBytes(bb);
    }

    public final static byte[] swappedBytes(byte... bs) {
        byte[] ret = new byte[bs.length];
        for (int i = 0; i < bs.length; i++) {
            ret[i] = bs[bs.length - 1 - i];
        }
        return ret;
    }

    public static final long longOfAsL2H(byte... low2hi) {
        if (low2hi == null)
            throw new IllegalArgumentException();

        return LongOfAsL2H(low2hi);
    }

    public static final Long LongOfAsL2H(byte... low2hi) {
        return LongOfAsH2L(swappedBytes(low2hi));
    }

    public static final long longOfAsH2L(byte... hi2low) {
        if (hi2low == null)
            throw new IllegalArgumentException();

        return LongOfAsH2L(hi2low);
    }

    public static final Long LongOfAsH2L(byte... hi2low) {
        if (hi2low == null) {
            return null;
        }
        if (hi2low.length > 8) {
            throw new IllegalArgumentException();
        }

        byte[] bs = new byte[8];
        int gap = bs.length - hi2low.length;
        for (int i = 0; i < hi2low.length; i++) {
            bs[gap + i] = hi2low[i];
        }

        long i0 = bs[0] & 0xff;
        long i1 = bs[1] & 0xff;
        long i2 = bs[2] & 0xff;
        long i3 = bs[3] & 0xff;
        long i4 = bs[4] & 0xff;
        long i5 = bs[5] & 0xff;
        long i6 = bs[6] & 0xff;
        long i7 = bs[7] & 0xff;
        i0 = i0 << 56;
        i1 = i1 << 48;
        i2 = i2 << 40;
        i3 = i3 << 32;
        i4 = i4 << 24;
        i5 = i5 << 16;
        i6 = i6 << 8;
        // i7=i7;
        return i0 | i1 | i2 | i3 | i4 | i5 | i6 | i7;
    }

    public static final String hexStringOf(byte... bs) {
        if (bs == null) {
            return "NULL";
        }

        char[] cs = new char[]{"0".charAt(0), "1".charAt(0), "2".charAt(0), "3".charAt(0), "4".charAt(0),
                "5".charAt(0), "6".charAt(0), "7".charAt(0), "8".charAt(0), "9".charAt(0), "A".charAt(0),
                "B".charAt(0), "C".charAt(0), "D".charAt(0), "E".charAt(0), "F".charAt(0)};
        char space = " ".charAt(0);
        StringBuffer sb = new StringBuffer(bs.length * 4);
        sb.append(space);
        for (int i = 0; bs != null && i < bs.length; i++) {
            byte b = bs[i];
            int bh = (b >> 4) & 0x0f;
            int bl = b & 0x0f;

            sb.append(cs[bh]);
            sb.append(cs[bl]);
            sb.append(space);
        }
        return sb.toString();
    }

    public static final String hexStringOf(List<Byte> bs) {
        if (bs == null) {
            return "NULL";
        }

        char[] cs = new char[]{"0".charAt(0), "1".charAt(0), "2".charAt(0), "3".charAt(0), "4".charAt(0),
                "5".charAt(0), "6".charAt(0), "7".charAt(0), "8".charAt(0), "9".charAt(0), "A".charAt(0),
                "B".charAt(0), "C".charAt(0), "D".charAt(0), "E".charAt(0), "F".charAt(0)};
        char space = " ".charAt(0);
        StringBuffer sb = new StringBuffer(bs.size() * 4);
        sb.append(space);
        for (int i = 0; bs != null && i < bs.size(); i++) {
            byte b = bs.get(i);
            int bh = (b >> 4) & 0x0f;
            int bl = b & 0x0f;

            sb.append(cs[bh]);
            sb.append(cs[bl]);
            sb.append(space);
        }
        return sb.toString();
    }

    public static final Byte[] BytesOf(byte... org) {
        Byte[] ret = new Byte[org.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = org[i];
        }
        return ret;
    }

    public static final byte[] bytesOf(List<Byte> org) {
        byte[] ret = new byte[org.size()];
        for (int i = 0; i < ret.length; i++) {
            if (org.get(i) == null) {
                throw new NullPointerException();
            }
            ret[i] = org.get(i);
        }
        return ret;
    }

    /**
     * return -1 as failed
     */
    public static final int indexOf(byte[] sample, byte[] pattern) {
        if (sample == null || pattern == null) {
            throw new NullPointerException();
        }

        if (pattern.length == 0) {
            throw new IllegalArgumentException();
        }
        int ret = -1;
        int indexInSample = 0;
        int leavedInSample = sample.length;
        for (; leavedInSample >= pattern.length; indexInSample++, leavedInSample--) {
            if (sample[indexInSample] == pattern[0]) {
                for (int i = 0; i < pattern.length; i++) {
                    if (sample[indexInSample + i] == pattern[i]) {
                        if (i == (pattern.length - 1)) {
                            ret = indexInSample;// success,
                        } else {
                            continue;
                        }
                    }
                    break;// match finish
                }
                if (ret != -1) {
                    break;
                }
            }
        }

        return ret;
    }

    public static final List<Byte> listBytesOf(byte... org) {
        ArrayList<Byte> ret = new ArrayList<Byte>();
        for (int i = 0; i < org.length; i++) {
            ret.add(org[i]);
        }
        return ret;
    }

    public final static boolean eqeal(byte[] bs1, byte[] bs2) {
        if (bs1 == null && bs2 == null) {
            return true;
        }
        if (bs1 == null || bs2 == null) {
            return false;
        }

        if (bs1.length != bs2.length) {
            return false;
        }

        for (int i = 0; i < bs2.length; i++) {
            if (bs1[i] != bs2[i]) {
                return false;
            }
        }
        return true;
    }

    public static final class BytesBuilder {
        private ByteArrayOutputStream bo;

        private BytesBuilder(int i) {
            bo = new ByteArrayOutputStream(i);
        }

        private BytesBuilder() {
            bo = new ByteArrayOutputStream();
        }

        public BytesBuilder append(byte... bs) {
            try {
                bo.write(bs);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return this;
        }

        public byte[] toByteArray() {
            return bo.toByteArray();
        }
    }

    public static final BytesBuilder newBytesBuilder() {
        return new BytesBuilder();
    }

    public static final BytesBuilder newBytesBuilder(int capacity) {
        return new BytesBuilder(capacity);
    }
}