package com.lk.dmplayer.fragments;

import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lk.dmplayer.R;
import com.lk.dmplayer.manager.MediaController;

/**
 * Created by dlkham on 12/27/2016.
 */
public class FragmentEquilizer extends Fragment implements VerticalSeekBar.OnSeekBarChangeListener,
        CompoundButton.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener,
        View.OnClickListener {
    public static final String TAG = FragmentEquilizer.class.getSimpleName();
    TextView bass_boost_label = null;
    SeekBar bass_boost = null;
    CheckBox enabled = null;
    Button flat = null;

    Equalizer eq = null;
    BassBoost bb = null;

    int min_level = 0;
    int max_level = 100;

    static final int MAX_SLIDERS = 8; // Must match the XML layout
    VerticalSeekBar sliders[] = new VerticalSeekBar[MAX_SLIDERS];
    TextView slider_labels[] = new TextView[MAX_SLIDERS];
    LinearLayout linearLayout_silder[] = new LinearLayout[MAX_SLIDERS];
    int num_sliders = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_equalizer, null);
        enabled = (CheckBox) view.findViewById(R.id.enabled);
        enabled.setOnCheckedChangeListener(this);

        flat = (Button) view.findViewById(R.id.flat);
        flat.setOnClickListener(this);

        bass_boost = (SeekBar) view.findViewById(R.id.bass_boost);
        bass_boost.setOnSeekBarChangeListener(this);
        bass_boost_label = (TextView) view.findViewById(R.id.bass_boost_label);

        sliders[0] = (VerticalSeekBar) view.findViewById(R.id.slider_1);
        slider_labels[0] = (TextView) view.findViewById(R.id.slider_label_1);
        linearLayout_silder[0]= (LinearLayout) view.findViewById(R.id.ll1);
        sliders[1] = (VerticalSeekBar) view.findViewById(R.id.slider_2);
        slider_labels[1] = (TextView) view.findViewById(R.id.slider_label_2);
        linearLayout_silder[1]= (LinearLayout) view.findViewById(R.id.ll2);
        sliders[2] = (VerticalSeekBar) view.findViewById(R.id.slider_3);
        slider_labels[2] = (TextView) view.findViewById(R.id.slider_label_3);
        linearLayout_silder[2]= (LinearLayout) view.findViewById(R.id.ll3);
        sliders[3] = (VerticalSeekBar) view.findViewById(R.id.slider_4);
        slider_labels[3] = (TextView) view.findViewById(R.id.slider_label_4);
        linearLayout_silder[3]= (LinearLayout) view.findViewById(R.id.ll4);
        sliders[4] = (VerticalSeekBar) view.findViewById(R.id.slider_5);
        slider_labels[4] = (TextView) view.findViewById(R.id.slider_label_5);
        linearLayout_silder[4]= (LinearLayout) view.findViewById(R.id.ll5);
        sliders[5] = (VerticalSeekBar) view.findViewById(R.id.slider_6);
        slider_labels[5] = (TextView) view.findViewById(R.id.slider_label_6);
        linearLayout_silder[5]= (LinearLayout) view.findViewById(R.id.ll6);
        sliders[6] = (VerticalSeekBar) view.findViewById(R.id.slider_7);
        slider_labels[6] = (TextView) view.findViewById(R.id.slider_label_7);
        linearLayout_silder[6]= (LinearLayout) view.findViewById(R.id.ll7);
        sliders[7] = (VerticalSeekBar) view.findViewById(R.id.slider_8);
        slider_labels[7] = (TextView) view.findViewById(R.id.slider_label_8);
        linearLayout_silder[7]= (LinearLayout) view.findViewById(R.id.ll8);

        eq = new Equalizer(0, MediaController.getInstance().getMediaPlayer().getAudioSessionId());
        if (eq != null) {
            eq.setEnabled(true);
            int num_bands = eq.getNumberOfBands();
            num_sliders = num_bands;
            short r[] = eq.getBandLevelRange();
            min_level = r[0];
            max_level = r[1];
            for (int i = 0; i < num_sliders && i < MAX_SLIDERS; i++) {
                int freq_range = eq.getCenterFreq((short) i);
                sliders[i].setOnSeekBarChangeListener(this);
                slider_labels[i].setText(milliHzToString(freq_range));
            }
        }
        for (int i = num_sliders; i < MAX_SLIDERS; i++) {
            sliders[i].setVisibility(View.GONE);
            slider_labels[i].setVisibility(View.GONE);
            linearLayout_silder[i].setVisibility(View.GONE);
        }

        bb = new BassBoost(0, MediaController.getInstance().getMediaPlayer().getAudioSessionId());
        if (bb != null) {
        } else {
            bass_boost.setVisibility(View.GONE);
            bass_boost_label.setVisibility(View.GONE);
        }

        updateUI();
        return view;
    }

    /*=============================================================================
        updateUI
    =============================================================================*/
    public void updateUI() {
        updateSliders();
        updateBassBoost();
        enabled.setChecked(eq.getEnabled());
    }


    /*=============================================================================
    setFlat
=============================================================================*/
    public void setFlat() {
        if (eq != null) {
            for (int i = 0; i < num_sliders; i++) {
                eq.setBandLevel((short) i, (short) 0);
            }
        }

        if (bb != null) {
            bb.setEnabled(false);
            bb.setStrength((short) 0);
        }

        updateUI();
    }

    /*=============================================================================
        updateBassBoost
    =============================================================================*/
    public void updateBassBoost() {
        if (bb != null)
            bass_boost.setProgress(bb.getRoundedStrength());
        else
            bass_boost.setProgress(0);
    }

    /*=============================================================================
    formatBandLabel
=============================================================================*/
    public String formatBandLabel(int[] band) {
        return milliHzToString(band[0]) + "-" + milliHzToString(band[1]);
    }

    /*=============================================================================
        milliHzToString
    =============================================================================*/
    public String milliHzToString(int milliHz) {
        if (milliHz < 1000) return "";
        if (milliHz < 1000000)
            return "" + (milliHz / 1000) + "Hz";
        else
            return "" + (milliHz / 1000000) + "kHz";
    }

    /*=============================================================================
        updateSliders
    =============================================================================*/
    public void updateSliders() {
        for (int i = 0; i < num_sliders; i++) {
            int level;
            if (eq != null)
                level = eq.getBandLevel((short) i);
            else
                level = 0;
            int pos = 100 * level / (max_level - min_level) + 50;
            sliders[i].setProgress(pos);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton view, boolean isChecked) {
        if (view == (View) enabled) {
            eq.setEnabled(isChecked);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == (View) flat) {
            setFlat();
        }
    }

    @Override
    public void onProgressChanged(VerticalSeekBar seekBar, int level, boolean fromUser) {

        if (eq != null) {
            int new_level = min_level + (max_level - min_level) * level / 100;

            for (int i = 0; i < num_sliders; i++) {
                if (sliders[i] == seekBar) {
                    eq.setBandLevel((short) i, (short) new_level);
                    break;
                }
            }
        }
    }

    @Override
    public void onStartTrackingTouch(VerticalSeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(VerticalSeekBar seekBar) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int level, boolean b) {
        if (seekBar == bass_boost) {
            bb.setEnabled(level > 0 ? true : false);
            bb.setStrength((short) level); // Already in the right range 0-1000
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
