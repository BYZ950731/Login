package com.tonmx.inspection;

import android.content.Context;

public class TonmxLoggerAdapter {
//    private LogLevel mLogLevel = LogLevel.RELEASE; //default debug
//    private boolean mConsoleLog = false;
//    private String mLogFolder = null;
//    private String mTag = "TonmxLogger";
//
//    private int mLogDivision = Logger.ERROR;
//
//    @NonNull private final FormatStrategy mPrettyFormatStrategy;
//    @NonNull private final FormatStrategy mDiskFormatStrategy;
//
//    public TonmxLoggerAdapter(){
//        this(LogLevel.RELEASE,false,null,null);
//    }
//
//    public TonmxLoggerAdapter(LogLevel level,boolean consoleLog,String logFolder,String default_tag){
//        mLogLevel = level;
//        mConsoleLog = consoleLog;
//        mLogFolder = logFolder;
//
//        if(mLogLevel == LogLevel.DEBUG){
//            mLogDivision = Logger.DEBUG;
//        }else if(mLogLevel == LogLevel.RELEASE){
//            mLogDivision = Logger.ERROR;
//        }else if(mLogLevel == LogLevel.OFF){
//            mLogDivision = Logger.ASSERT + 1;
//        }
//
//        if(default_tag != null){
//            mTag = default_tag;
//        }
//
//        mPrettyFormatStrategy = PrettyFormatStrategy.newBuilder()
//                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
//                .methodCount(0)         // (Optional) How many method line to show. Default 2
//                .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
//                .tag(default_tag)               // (Optional) Global tag for every log. Default PRETTY_LOGGER
//                .build();
//
//        mDiskFormatStrategy = CsvFormatStrategy.newBuilder().tag(default_tag).logFolder(logFolder).build();
//    }
//    public TonmxLoggerAdapter(int level,boolean consoleLog,String logFolder,String default_tag){
//        //mLogLevel = level;
//        mLogDivision = level;
//        mConsoleLog = consoleLog;
//        mLogFolder = logFolder;
//
////        if(mLogLevel == LogLevel.DEBUG){
////            mLogDivision = Logger.DEBUG;
////        }else if(mLogLevel == LogLevel.RELEASE){
////            mLogDivision = Logger.ERROR;
////        }else if(mLogLevel == LogLevel.OFF){
////            mLogDivision = Logger.ASSERT + 1;
////        }
//
//        if(default_tag != null){
//            mTag = default_tag;
//        }
//
//        mPrettyFormatStrategy = PrettyFormatStrategy.newBuilder()
//                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
//                .methodCount(0)         // (Optional) How many method line to show. Default 2
//                .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
//                .tag(default_tag)               // (Optional) Global tag for every log. Default PRETTY_LOGGER
//                .build();
//
//        mDiskFormatStrategy = CsvFormatStrategy.newBuilder().tag(default_tag).logFolder(logFolder).build();
//    }
//
//    @Override
//    public boolean isLoggable(int priority, @Nullable String tag) {
//        if(priority >= mLogDivision){
//            return true;
//        } else {
//            return mConsoleLog;
//        }
//    }
//
//    @Override
//    public void log(int priority, @Nullable String tag, @NonNull String message) {
//        if(priority >= mLogDivision){
//            mDiskFormatStrategy.log(priority, tag, message);
//        }
//
//        if(mConsoleLog) {
//            mPrettyFormatStrategy.log(priority, tag, message);
//        }
//    }
//
//    public static void init(Context context, LogAdapter adapter){
//        //log config
//        Logger.addLogAdapter(adapter);
//    }
//
//    public static void updateAdapter(@NonNull TonmxLoggerAdapter adapter){
//        Logger.clearLogAdapters();
//        Logger.addLogAdapter(adapter);
//        LogUtil.setmAdapter(adapter);
//    }
//
//    public String getLogFolder(){
//        return mLogFolder;
//    }
//
//    public int getLogLevel(){
//        return mLogDivision;
//    }
//
//    public void setLogLevel(int logLevel){
//        mLogDivision = logLevel;
//    }
//
//    public String getTag(){
//        return mTag;
//    }
}
