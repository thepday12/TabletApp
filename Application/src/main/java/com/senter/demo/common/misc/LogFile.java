package com.senter.demo.common.misc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public final class LogFile {
    private static boolean Allowe = true;
    private final File[] dirsForLog;
    private final boolean printInLogcatToo;
    private Executor executor = Executors.newSingleThreadExecutor();

    private LogFile(boolean printInLogcatToo, File... dirForLog) {
        this.printInLogcatToo = printInLogcatToo;
        this.dirsForLog = dirForLog;
        refreshFoss();
    }

    public static LogFile newInstanceOf(boolean printInLogcatToo, File... dirForLog) {
        return new LogFile(printInLogcatToo, dirForLog);
    }

    public void a(String tag, String msg) {
        if (Allowe == true) {
            if (printInLogcatToo == true) android.util.Log.println(android.util.Log.ASSERT, tag, "" + msg);
            writeLog2FileLn(Level.E, new Date(), tag, msg);
        }
    }

    public void e(String tag, String msg) {
        if (Allowe == true) {
            if (printInLogcatToo == true) android.util.Log.e(tag, "" + msg);
            writeLog2FileLn(Level.E, new Date(), tag, msg);
        }
    }

    public void w(String tag, String msg) {
        if (Allowe == true) {
            if (printInLogcatToo == true) android.util.Log.w(tag, "" + msg);
            writeLog2FileLn(Level.W, new Date(), tag, msg);
        }
    }

    public void i(String tag, String msg) {
        if (Allowe == true) {
            if (printInLogcatToo == true) android.util.Log.i(tag, "" + msg);
            writeLog2FileLn(Level.I, new Date(), tag, msg);
        }
    }

    public void d(String tag, String msg) {
        if (Allowe == true) {
            if (printInLogcatToo == true) android.util.Log.d(tag, "" + msg);
            writeLog2FileLn(Level.D, new Date(), tag, msg);
        }
    }

    public void v(String tag, String msg) {
        if (Allowe == true) {
            if (printInLogcatToo == true) android.util.Log.v(tag, "" + msg);
            writeLog2FileLn(Level.V, new Date(), tag, msg);
        }
    }

    public void exception(final Throwable msg) {
        if (msg == null) {
            writeLog2FileLn(Level.W, new Date(), "Exception", "exception(null) \r\n");
            return;
        }
        if (printInLogcatToo == true) msg.printStackTrace();
        if (Allowe == true) {
            confirmFossExist();
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    PrintStream ps = new PrintStream(baos, true);
                    msg.printStackTrace(ps);
                    writeLog2FileLn(Level.W, new Date(), "Exception", new String(baos.toByteArray()) + "\r\n");
                }
            });
        }
    }

    enum Level {
        V, D, I, W, E;
    }

    private void writeLog2FileLn(final Level level, final Date date, final String tag, final String string) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                confirmFossExist();
                if (fileOutputStreams == null || fileOutputStreams.length == 0) {
                    return;
                }

                StringBuilder sb = new StringBuilder();
                sb.append(level.name() + "\t");

                sb.append(dateFormator(date, "yyyy-MM-dd kk:mm:ss.SSS") + "\t");

                sb.append(tag + "\t");

                sb.append(string);

                sb.append("\r\n");

                for (FileOutputStream fos : fileOutputStreams) {
                    try {
                        fos.write(sb.toString().getBytes());
                        fos.flush();
                        continue;
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    refreshFoss();
                    try {
                        fos.write(sb.toString().getBytes());
                        fos.flush();
                        continue;
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private FileOutputStream[] fileOutputStreams;

    private void confirmFossExist() {
        if (fileOutputStreams == null) {
            fileOutputStreams = makeFileOutputStreams();
        }
    }

    private void refreshFoss() {
        if (fileOutputStreams != null) {
            for (int i = 0; i < fileOutputStreams.length; i++) {
                try {
                    fileOutputStreams[i].close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        fileOutputStreams = makeFileOutputStreams();
    }

    private FileOutputStream[] makeFileOutputStreams() {
        ArrayList<FileOutputStream> fosStreams = new ArrayList<FileOutputStream>();

        for (int i = 0; i < dirsForLog.length; i++) {
            File dirForLog = dirsForLog[i];
            dirForLog.mkdirs();

            File file = new File(dirForLog.getAbsolutePath() + "/" + dateFormator(new Date(), "yyyy-MM-dd_kk-mm-ss.SSS") + ".txt");///storage/sdcard0
            try {
                FileOutputStream fos = new FileOutputStream(file, true);
                fos.write("======================================================\r\n".getBytes());
                fos.write("=======================begain===========================\r\n".getBytes());
                fos.write("======================================================\r\n".getBytes());
                fosStreams.add(fos);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            continue;
        }

        FileOutputStream[] ret = new FileOutputStream[fosStreams.size()];

        for (int i = 0; i < ret.length; i++) {
            ret[i] = fosStreams.get(i);
        }
        return ret;
    }

    /**
     * format：yyyy-MM-dd kk:mm:ss.SSS
     *
     * @see SimpleDateFormat
     */
    private static String dateFormator(Date date, String formator) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(formator);
        String modDate = dateFormat.format(date);
        return modDate;
    }
}
