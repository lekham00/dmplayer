package com.lk.dmplayer.manager;

import android.content.Context;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;

/**
 * Created by admin on 1/2/17.
 */

public class EquilizerController {
    Equalizer eq = null;
    BassBoost bb = null;
    int audioSession;
    Context context;

    public EquilizerController(Context context, int audioSession) {
        context = context;
        audioSession = audioSession;
    }
}
