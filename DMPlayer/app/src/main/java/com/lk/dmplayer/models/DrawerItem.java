package com.lk.dmplayer.models;

import android.graphics.drawable.Drawable;

/**
 * Created by Le Kham on 10/23/2016.
 */
public class DrawerItem {
    private String title;
    private Drawable icon;

    public DrawerItem(String title, Drawable icon)
    {
        this.setTitle(title);
        this.setIcon(icon);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
}
