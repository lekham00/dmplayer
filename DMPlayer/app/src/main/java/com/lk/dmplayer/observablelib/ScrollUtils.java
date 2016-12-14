package com.lk.dmplayer.observablelib;

/**
 * Created by Kham on 12/14/2016.
 */

public class ScrollUtils {
    /**
     * Create a color integer value with specified alpha.
     * <p>This may be useful to change alpha value of background color.</p>
     *
     * @param alpha     Alpha value from 0.0f to 1.0f.
     * @param baseColor Base color. alpha value will be ignored.
     * @return A color with alpha made from base color.
     */
    public static int getColorWithAlpha(float alpha, int baseColor) {
        int a = Math.min(255, Math.max(0, (int) (alpha * 255))) << 24;
        int rgb = 0x00ffffff & baseColor;
        return a + rgb;
    }
}
