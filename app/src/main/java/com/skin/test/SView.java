package com.skin.test;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

public class SView extends ImageView {
    public SView(Context context) {
        super(context);
    }

    public SView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

}
