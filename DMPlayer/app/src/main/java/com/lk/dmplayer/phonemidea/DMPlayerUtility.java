package com.lk.dmplayer.phonemidea;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.lk.dmplayer.untilily.ApplicationDMPlayer;

/**
 * Created by Le Kham on 10/26/2016.
 */
public class DMPlayerUtility {
    public static String getAudioDuration(long durationLong) {
        long totalSecs = durationLong / 1000;
        long hours = totalSecs / 3600;
        long minutes = (totalSecs % 3600) / 60;
        long seconds = totalSecs % 60;

        String duration = "";
        if (hours != 0) {
            duration = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            duration = String.format("%02d:%02d", minutes, seconds);
        }

        return duration;
    }

    public static void runOnUIThread(Runnable runnable) {
        runOnUIThread(runnable, 0);
    }

    public static void runOnUIThread(Runnable runnable, int deplay) {
        if (deplay == 0) {
            ApplicationDMPlayer.handler.post(runnable);
        } else {
            ApplicationDMPlayer.handler.postDelayed(runnable, deplay);
        }
    }

    private static final DecelerateInterpolator DECCELERATE_INTERPOLATOR = new DecelerateInterpolator();
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);

    public static void animateHeartButton(View view) {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f);
        rotationAnim.setDuration(300);
        rotationAnim.setInterpolator(ACCELERATE_INTERPOLATOR);

        ObjectAnimator bounceAnimX = ObjectAnimator.ofFloat(view, "scaleX", 0.2f, 1f);
        bounceAnimX.setDuration(300);
        bounceAnimX.setInterpolator(ACCELERATE_INTERPOLATOR);
        ObjectAnimator bounceAnimY = ObjectAnimator.ofFloat(view, "scaleY", 0.2f, 1f);
        bounceAnimY.setDuration(300);
        bounceAnimY.setInterpolator(ACCELERATE_INTERPOLATOR);
        animatorSet.play(bounceAnimX).with(bounceAnimY).after(rotationAnim);
        animatorSet.start();
    }

    public static Cursor query(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder, int limit) {
        try {
            ContentResolver contentResolver = context.getContentResolver();
            if (limit > 0) {
                uri = uri.buildUpon().appendQueryParameter("limit", "" + limit).build();
            }
            return contentResolver.query(uri, projection, selection, selectionArgs, sortOrder);
        } catch (Exception ex) {
            return null;
        }
    }
}
