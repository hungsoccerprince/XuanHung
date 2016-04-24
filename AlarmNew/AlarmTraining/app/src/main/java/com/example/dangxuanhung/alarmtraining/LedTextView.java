package com.example.dangxuanhung.alarmtraining;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Dang Xuan Hung on 29/02/2016.
 */
public class LedTextView extends TextView {

    public LedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        AssetManager assets = context.getAssets();
        final Typeface font = Typeface.createFromAsset(assets,"LED.ttf");

        setTypeface(font);
    }
}
