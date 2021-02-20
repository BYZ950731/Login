package com.tonmx.inspection;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public  class MyApplication extends Application {
    private static MyApplication mInstance = null;

    public static MyApplication get() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        int pid = android.os.Process.myPid();
        if(isMainProcess(pid)){
            PrefData.init(this);
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), InspectionService.class);
           // startService(intent);
        }else if(isMonitorProcess(pid)){
            writePid("monitor.pid",pid);
        }else{
            // don't add new process
        }
        //write process pid end

    }

    private boolean isMainProcess(int pid){
        String packageName = getPackageName();
        return compareProcessName(packageName,pid);
    }

    private boolean isMonitorProcess(int pid){
        String packageName = getPackageName() + ":Monitor";
        return compareProcessName(packageName,pid);
    }
    private boolean compareProcessName(String process,int pid){
        ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return process.equals(appProcess.processName);
            }
        }
        return false;
    }
    private void writePid(String filename,int pid){
        File pidFile = new File(getFilesDir(),filename);
        if(pidFile.exists()){
            pidFile.delete();
        }

        boolean isCreateSucc = false;
        try {
            isCreateSucc = pidFile.createNewFile();
        } catch (IOException e) {
            //can not create pid file
            return;
        }

        if(isCreateSucc){
            if(pidFile.canWrite()){
                FileOutputStream os = null;
                PrintWriter writer = null;
                try {
                    os = new FileOutputStream(pidFile);
                    writer = new PrintWriter(os);
                    //we need write string as we can check it easily!!!
                    writer.write(Integer.toString(pid));
                    writer.flush();
                } catch (FileNotFoundException e) {
                    //
                } finally {
                    if(writer != null) {
                        writer.close();
                    }

                    if(os != null) {
                        try {
                            os.close();
                        } catch (IOException e) {
                            //quiet
                        }
                    }
                }
            }else {
                //file can not be written
            }
        }
    }
}
