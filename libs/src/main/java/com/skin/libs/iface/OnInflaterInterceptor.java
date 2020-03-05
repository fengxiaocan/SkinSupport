package com.skin.libs.iface;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public interface OnInflaterInterceptor{
    void interceptorCreateView(Context context,View view,String name,AttributeSet attrs);
}
