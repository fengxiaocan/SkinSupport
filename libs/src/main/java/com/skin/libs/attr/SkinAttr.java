package com.skin.libs.attr;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.skin.libs.iface.ISkin;

/**
 * 描述:
 * DynamTheme-
 *
 * @Author thinkpad
 * @create 2018-08-25 13:05
 */
public abstract class SkinAttr implements ISkin,Cloneable {
    protected static final String RES_TYPE_NAME_COLOR = "color";
    protected static final String RES_TYPE_NAME_DRAWABLE = "drawable";
    protected static final String RES_TYPE_NAME_MIPMAP = "mipmap";

    protected final @NonNull String attrName;    //属性名（例如：background、textColor）
    protected String attrType;    //属性类型（例如：drawable、color）
    protected int resId;       //资源id（例如：123）
    protected String resName;     //资源名称（例如：ic_bg）

    public SkinAttr(@NonNull String attrName) {
        this.attrName = attrName;
    }

    public SkinAttr(@NonNull String attrName, String attrType, String resName, int resId) {
        this.attrName = attrName;
        this.attrType = attrType;
        this.resId = resId;
        this.resName = resName;
//        Log.e("noah","attrType="+attrType);
    }

    protected boolean isDrawable() {
        return RES_TYPE_NAME_DRAWABLE.equals(attrType)
                || RES_TYPE_NAME_MIPMAP.equals(attrType);
    }

    protected boolean isColor() {
        return RES_TYPE_NAME_COLOR.equals(attrType);
    }

    public String getAttrName() {
        return attrName;
    }

    public String getAttrType() {
        return attrType;
    }

    public void setAttrType(String attrType) {
        this.attrType = attrType;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public SkinAttr clone(String attrType, String resName, int resId) {
        SkinAttr clone = clone();
        if (clone != null) {
            clone.attrType = attrType;
            clone.resId = resId;
            clone.resName = resName;
        }
        return clone;
    }

    @NonNull
    @Override
    protected SkinAttr clone() {
        SkinAttr o = null;
        try {
            o = (SkinAttr) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return o;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof SkinAttr) {
            return attrName.equals(((SkinAttr) obj).attrName);
        }
        return false;
    }
}
