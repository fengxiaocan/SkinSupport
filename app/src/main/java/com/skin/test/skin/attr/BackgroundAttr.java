package com.skin.test.skin.attr;

import android.graphics.drawable.Drawable;
import android.view.View;

import com.skin.test.skin.attr.base.SkinAttr;
import com.skin.test.skin.utils.SkinResourcesUtils;


/**
 * Created by _SOLID
 * Date:2016/4/13
 * Time:21:46
 */
public class BackgroundAttr extends SkinAttr {

    @Override
    protected void applySkin(View view) {
        if (isColor()) {
            int color = SkinResourcesUtils.getColor(attrValueRefId);
            view.setBackgroundColor(color);
        } else if (isDrawable()) {
            Drawable bg = SkinResourcesUtils.getDrawable(attrValueRefId);
            view.setBackgroundDrawable(bg);
        }
    }

    @Override
    protected void applyNightMode(View view) {
        if (isColor()) {
            view.setBackgroundColor(SkinResourcesUtils.getNightColor(attrValueRefId));
        } else if (isDrawable()) {
            view.setBackgroundDrawable(SkinResourcesUtils.getNightDrawable(attrValueRefName));
        }
    }
}
