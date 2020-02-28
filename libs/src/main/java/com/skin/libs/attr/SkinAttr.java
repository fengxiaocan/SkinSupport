package com.skin.libs.attr;

import androidx.annotation.NonNull;

import com.skin.libs.iface.ISkin;

/**
 * 描述:
 * DynamTheme-
 *
 * @Author thinkpad
 * @create 2018-08-25 13:05
 */
public abstract class SkinAttr extends IAttr implements ISkin{
    protected static final String RES_TYPE_NAME_COLOR = "color";
    protected static final String RES_TYPE_NAME_DRAWABLE = "drawable";
    protected static final String RES_TYPE_NAME_MIPMAP = "mipmap";

    public SkinAttr(@NonNull String attrName){
        super(attrName);
    }

    public SkinAttr(@NonNull String attrName,String attrType,String resName,int resId){
        super(attrName,attrType,resName,resId);
    }

    protected boolean isDrawable(){
        return RES_TYPE_NAME_DRAWABLE.equals(attrType) || RES_TYPE_NAME_MIPMAP.equals(attrType);
    }

    protected boolean isColor(){
        return RES_TYPE_NAME_COLOR.equals(attrType);
    }

    public String getAttrName(){
        return attrName;
    }

    public String getAttrType(){
        return attrType;
    }


    public int getResId(){
        return resId;
    }


    public String getResName(){
        return resName;
    }


    public final SkinAttr clone(String attrType,String resName,int resId){
        SkinAttr clone = this.clone();
        if(clone != null){
            clone.attrName = this.attrName;
            clone.attrType = attrType;
            clone.resId = resId;
            clone.resName = resName;
        }
        return clone;
    }

    @NonNull
    @Override
    protected SkinAttr clone(){
        SkinAttr o = (SkinAttr)super.clone();
        return o;
    }

}
