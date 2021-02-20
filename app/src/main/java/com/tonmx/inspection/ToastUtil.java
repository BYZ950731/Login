package com.tonmx.inspection;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ToastUtil {


    private static Toast toast = null;

    private static int originColor = 0;

    private static Handler mToastHandler = new Handler(Looper.getMainLooper());

    public static void showShortToast(Context context, int retId) {
        showToast(context, context.getString(retId), Toast.LENGTH_SHORT);
    }

    public static void showShortToast(Context context, String hint) {
        showToast(context, hint, Toast.LENGTH_SHORT);
    }

    public static void showLongToast(Context context, int retId) {
        showToast(context, context.getResources().getString(retId), Toast.LENGTH_LONG);
    }

    public static void showLongToast(Context context, String hint) {
        showToast(context, hint, Toast.LENGTH_LONG);
    }

    private static void showToast(final Context context, final String msg, final int duration) {
        if (!isMainThread()) {
            if (mToastHandler != null) {
                mToastHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        showToast(context, msg, duration);
                    }
                });
            }
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(context.getApplicationContext(), msg, duration);
        } else {
            toast.setText(msg);
            toast.setDuration(duration);
        }
        //add by bruce 警告Toast字体颜色为红色,在弹出警告Toast后将其他正常Toast恢复为原来颜色
        if (originColor != 0) {
            LinearLayout toastView = (LinearLayout) toast.getView();
            TextView toastText = (TextView) toastView.findViewById(android.R.id.message);
            toastText.setTextColor(originColor);
        }
        //add by bruce
        toast.show();
    }

    public static void getShortToast(final Context context, final int retId) {
        if (!isMainThread()) {
            if (mToastHandler != null) {
                mToastHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        getShortToast(context, retId);
                    }
                });
            }
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(context, retId, Toast.LENGTH_SHORT);
        } else {
            toast.setText(retId);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.show();
    }

    public static void getShortToastRed(final Context context, final int retId) {
        if (!isMainThread()) {
            if (mToastHandler != null) {
                mToastHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        getShortToastRed(context, retId);
                    }
                });
            }
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(context, retId, Toast.LENGTH_SHORT);
        } else {
            toast.setText(retId);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        LinearLayout toastView = (LinearLayout) toast.getView();
        TextView toastText = (TextView) toastView.findViewById(android.R.id.message);

        //add by bruce 在第一次弹警告Toast时记录正常Toast字体颜色
        if (originColor == 0) {
            originColor = toastText.getCurrentTextColor();
        }
        toastText.setTextColor(Color.RED);
        toast.show();
    }

    public static void getShortToastByString(final Context context, final String hint) {
        if (!isMainThread()) {
            if (mToastHandler != null) {
                mToastHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        getShortToastByString(context, hint);
                    }
                });
            }
            return;
        }
        try {
            if (toast == null) {
                toast = Toast.makeText(context, hint, Toast.LENGTH_SHORT);
            } else {
                toast.setText(hint);
                toast.setDuration(Toast.LENGTH_SHORT);
            }
            toast.show();
        } catch (Exception e) {
        } catch (Throwable e) {
        }
    }

    public static void getLongToast(final Context context, final int retId) {
        if (!isMainThread()) {
            if (mToastHandler != null) {
                mToastHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        getLongToast(context, retId);
                    }
                });
            }
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(context, retId, Toast.LENGTH_LONG);
        } else {
            toast.setText(retId);
            toast.setDuration(Toast.LENGTH_LONG);
        }
        toast.show();
    }

    private static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }
}
