package com.skin.libs.attr;

import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ImageView;

import com.skin.libs.SkinManager;


/**
 * Created by _SOLID
 * Date:2017/2/15
 * Time:17:39
 * Desc:
 */

public class ImageViewSrcAttr extends SkinAttr {

    public ImageViewSrcAttr() {
        super("src");
    }

    public ImageViewSrcAttr(String attrName, String attrType, String resName, int resId) {
        super(attrName, attrType, resName, resId);
    }

    @Override
    public void applySkin(View view) {
        ImageView iv = (ImageView) view;
        if (isDrawable()) {
            iv.setImageDrawable(SkinManager.getInstance().getDrawable(resName, attrType, resId));
        } else if (isColor()) {
            iv.setImageDrawable(new ColorDrawable(SkinManager.getInstance().getColor(resName, resId)));
        }
    }
}
