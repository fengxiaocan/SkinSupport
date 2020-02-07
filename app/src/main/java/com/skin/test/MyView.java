package com.skin.test;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.skin.libs.iface.ISkinItem;
import com.skin.libs.SkinManager;

public class MyView extends TextView implements ISkinItem {

    public MyView(Context context) {
        super(context);
        init();
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setTextColor(SkinManager.getInstance().getColor(R.color.mainText));
    }

    @Override
    public void apply() {
        setTextColor(SkinManager.getInstance().getColor(R.color.mainText));
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
