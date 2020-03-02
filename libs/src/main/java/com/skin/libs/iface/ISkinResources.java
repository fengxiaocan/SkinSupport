package com.skin.libs.iface;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;

public interface ISkinResources{
    int getColor(int resId);

    int getColor(String resName,int resId);

    /**
     * 加载指定资源颜色drawable,转化为ColorStateList，保证selector类型的Color也能被转换。
     *
     * @param resId
     * @return
     */
    ColorStateList getColorStateList(int resId);

    /**
     * 加载指定资源颜色drawable,转化为ColorStateList，保证selector类型的Color也能被转换。
     *
     * @param resName
     * @param resId
     * @return
     */
    ColorStateList getColorStateList(String resName,int resId);

    Drawable getDrawable(int resId);

    Drawable getDrawable(String resName,int resId);

    Drawable getDrawable(String resName,String resType,int resId);
}
