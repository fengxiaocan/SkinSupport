package com.skin.libs.attr;

import android.view.View;
import android.widget.TextView;

import com.skin.libs.SkinManager;


/**
 * Created by _SOLID
 * Date:2016/4/13
 * Time:22:53
 */
public class TextColorAttr extends SkinAttr {
    public TextColorAttr() {
        super("textColor");
    }

    public TextColorAttr(String attrName, String attrType, String resName, int resId) {
        super(attrName, attrType, resName, resId);
    }

    @Override
    public void applySkin(View view) {
        if (view instanceof TextView) {
            TextView tv = (TextView) view;
            tv.setTextColor(SkinManager.getInstance().getColorStateList(resName, resId));
        }
    }
}
