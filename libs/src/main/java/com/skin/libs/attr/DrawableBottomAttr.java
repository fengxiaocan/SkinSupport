package com.skin.libs.attr;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import com.skin.libs.SkinManager;


/**
 * Created by _SOLID
 * Date:2017/2/15
 * Time:17:39
 * Desc:
 */

public class DrawableBottomAttr extends SkinAttr {
    public DrawableBottomAttr() {
        super("drawableBottom");
    }

    public DrawableBottomAttr(String attrName, String attrType, String resName, int resId) {
        super(attrName, attrType, resName, resId);
    }

    @Override
    public void applySkin(View view) {
        TextView textView = (TextView) view;
        Drawable[] drawables = textView.getCompoundDrawables();
        if (isDrawable()) {
            drawables[3] = SkinManager.getInstance().getDrawable(resName, attrType, resId);
        } else if (isColor()) {
            drawables[3] = new ColorDrawable(SkinManager.getInstance().getColor(resName, resId));
        }
        textView.setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
    }
}
