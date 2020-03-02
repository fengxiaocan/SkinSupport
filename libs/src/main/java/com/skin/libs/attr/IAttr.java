package com.skin.libs.attr;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 描述:
 */
public class IAttr implements Cloneable{
    protected String attrName;    //属性名（例如：background、textColor）
    protected String attrType;    //属性类型（例如：drawable、color）
    protected int resId;       //资源id（例如：123）
    protected String resName;     //资源名称（例如：ic_bg）

    public IAttr(@NonNull String attrName){
        this.attrName = attrName;
    }

    public IAttr(@NonNull String attrName,String attrType,String resName,int resId){
        this.attrName = attrName;
        this.attrType = attrType;
        this.resId = resId;
        this.resName = resName;
    }

    public String getAttrName(){
        return attrName;
    }

    public IAttr setAttrName(String attrName){
        this.attrName = attrName;
        return this;
    }

    public String getAttrType(){
        return attrType;
    }

    public IAttr setAttrType(String attrType){
        this.attrType = attrType;
        return this;
    }

    public int getResId(){
        return resId;
    }

    public IAttr setResId(int resId){
        this.resId = resId;
        return this;
    }

    public String getResName(){
        return resName;
    }

    public IAttr setResName(String resName){
        this.resName = resName;
        return this;
    }

    public final IAttr clone(String attrName,String attrType,String resName,int resId){
        IAttr clone = clone();
        if(clone != null){
            clone.attrName = attrName;
            clone.attrType = attrType;
            clone.resId = resId;
            clone.resName = resName;
        }
        return clone;
    }

    @NonNull
    @Override
    protected IAttr clone(){
        IAttr o = null;
        try{
            o = (IAttr)super.clone();
        } catch(CloneNotSupportedException e){
            e.printStackTrace();
        }
        return o;
    }

    @Override
    public boolean equals(@Nullable Object obj){
        if(obj instanceof IAttr){
            return attrName.equals(((IAttr)obj).attrName);
        }
        return false;
    }

}
