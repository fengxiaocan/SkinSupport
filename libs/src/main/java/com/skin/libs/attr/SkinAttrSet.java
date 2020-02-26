package com.skin.libs.attr;

import android.util.AttributeSet;
import android.view.View;

import com.skin.libs.iface.ISkinItem;

public abstract class SkinAttrSet implements ISkinItem{

    protected View view;
    protected AttributeSet attrs;

    public SkinAttrSet(View view,AttributeSet attrs){
        this.view = view;
        this.attrs = attrs;
    }

}
