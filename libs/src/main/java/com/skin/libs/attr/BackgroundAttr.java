package com.skin.libs.attr;

import android.graphics.drawable.Drawable;
import android.view.View;

import com.skin.libs.SkinManager;


/**
 * Created by _SOLID
 * Date:2016/4/13
 * Time:21:46
 */
public class BackgroundAttr extends SkinAttr {
    public BackgroundAttr() {
        super("background");
    }

    public BackgroundAttr(String attrName, String attrType, String resName, int resId) {
        super(attrName, attrType, resName, resId);
    }

    @Override
    public void applySkin(View view) {
        if (isColor()) {
            int color = SkinManager.getInstance().getColor(resName, resId);
            view.setBackgroundColor(color);
        } else if (isDrawable()) {
            Drawable bg = SkinManager.getInstance().getDrawable(resName, attrType, resId);
            view.setBackgroundDrawable(bg);
        }
    }
}
