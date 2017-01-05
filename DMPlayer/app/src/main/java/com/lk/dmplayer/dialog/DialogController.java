package com.lk.dmplayer.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.lk.dmplayer.untilily.DeviceUtil;

/**
 * Created by dlkham on 1/5/2017.
 */
public class DialogController extends Dialog {
    public DialogController(Context context, View view) {
        super(context);
        this.setCanceledOnTouchOutside(false);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        this.setContentView(view);
        setupDialogSize();
    }

    /**
     * setup dialog size
     **/
    protected void setupDialogSize() {
        int screenW = DeviceUtil.getScreenWidth(getContext());
        int screenH = DeviceUtil.getScreenHeight(getContext());
        int width = screenW < screenH ? screenW : screenH;
        width *= 0.9;
        if (DeviceUtil.isLarge(getContext())) {
            width *= 0.8;
        } else if (DeviceUtil.isXLarge(getContext())) {
            width *= 0.7;
        }
        setDialogWidth(width);
        //setDialogHeight(width);
    }

    /**
     * notify to know activity is resumed
     **/
    public void onResumeActivity() {
    }

    /**
     * Callback when activity is paused.
     */
    public void onPauseActivity() {
    }

    /**
     * Callback when activity is destroyed.
     */
    public void onDestroyActivity() {
    }

    /**
     * catch configuration changed.
     *
     * @param newConfig
     */
    public void onConfigurationChanged(Configuration newConfig) {
        updateOrientation(newConfig.orientation);
    }

    /**
     * when orientation changes, update layout if have changes..
     *
     * @param orientation
     */
    public void updateOrientation(int orientation) {
    }

    /**
     * setup dialog width
     *
     * @param width
     */
    protected void setDialogWidth(int width) {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = width;
        getWindow().setAttributes(params);
    }

    /**
     * setup dialog height
     *
     * @param width
     */
    protected void setDialogHeight(int height) {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = height;
        getWindow().setAttributes(params);
    }
}
