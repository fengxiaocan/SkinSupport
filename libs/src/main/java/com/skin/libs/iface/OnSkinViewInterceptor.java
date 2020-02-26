package com.skin.libs.iface;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.skin.libs.attr.SkinAttrSet;

/**
 * 拦截器,拦截的时候
 */
public interface OnSkinViewInterceptor{
    SkinAttrSet interceptorView(View view,Context context,AttributeSet attrs);
}
