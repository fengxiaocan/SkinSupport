package com.skin.libs;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.skin.libs.iface.ISkinResources;

public final class SkinResources implements ISkinResources{
    private final Context context;
    private Resources mSkinResources;
    private String skinPackageName;

    public SkinResources(Context context){
        this.context = context;
    }

    public void setSkinResources(Resources mSkinResources,String skinPackageName){
        this.mSkinResources = mSkinResources;
        this.skinPackageName = skinPackageName;
    }

    public void setSkinResources(Resources mSkinResources){
        this.mSkinResources = mSkinResources;
    }

    public void setSkinPackageName(String skinPackageName){
        this.skinPackageName = skinPackageName;
    }

    public boolean isHasSkin(){
        return mSkinResources != null;
    }

    /**
     * 获取原始颜色值
     *
     * @param resId 资源id
     * @return
     */
    private int getOriginColor(int resId){
        return ContextCompat.getColor(context,resId);
    }

    /**
     * 获取原始的颜色列表
     *
     * @param resId
     * @return
     */
    private ColorStateList getOriginColorStateList(int resId){
        return ContextCompat.getColorStateList(context,resId);
    }

    /**
     * 获取皮肤颜色值
     *
     * @param resourceName
     * @return
     */
    private int getSkinColor(String resourceName,int resId){
        try{
            int newResId = mSkinResources.getIdentifier(resourceName,"color",skinPackageName);
            if(newResId == 0){
                return getOriginColor(newResId);
            }
            return ResourcesCompat.getColor(mSkinResources,newResId,null);
        } catch(Throwable e){
            return getOriginColor(resId);
        }
    }

    /**
     * 获取皮肤的颜色值集合
     *
     * @param resourceName
     * @param resId
     * @return
     */
    private ColorStateList getSkinColorStateList(String resourceName,int resId){
        try{
            int newResId = mSkinResources.getIdentifier(resourceName,"color",skinPackageName);
            if(newResId == 0){
                return getOriginColorStateList(newResId);
            }
            return ResourcesCompat.getColorStateList(mSkinResources,newResId,null);
        } catch(Exception e){
            return getOriginColorStateList(resId);
        }
    }

    /**
     * 获取皮肤资源的名字
     *
     * @param resId
     * @return
     */
    public String getSkinResourceName(int resId){
        return context.getResources().getResourceEntryName(resId);
    }

    /**
     * 根据资源id获取颜色值
     *
     * @param resId
     * @return
     */
    public int getColor(int resId){
        if(isHasSkin()){
            return getSkinColor(getSkinResourceName(resId),resId);
        }
        return getOriginColor(resId);
    }

    /**
     * 获取颜色值
     *
     * @param resName
     * @param resId
     * @return
     */
    public int getColor(String resName,int resId){
        if(isHasSkin()){
            return getSkinColor(resName,resId);
        }
        return getOriginColor(resId);
    }

    /**
     * 获取原始的主题drawable
     *
     * @param resId
     * @return
     */
    private Drawable getOriginDrawable(int resId){
        return ContextCompat.getDrawable(context,resId);
    }

    /**
     * 获取其他皮肤的drawable
     *
     * @param resName
     * @param resId
     * @return
     */
    private Drawable getSkinDrawable(String resName,int resId){
        try{
            int newResId = mSkinResources.getIdentifier(resName,"drawable",skinPackageName);
            if(newResId == 0){
                //判断是否在mipmap文件夹中存放
                newResId = mSkinResources.getIdentifier(resName,"mipmap",skinPackageName);
            }
            if(newResId == 0){
                return getOriginDrawable(resId);
            }
            return ResourcesCompat.getDrawable(mSkinResources,newResId,null);
        } catch(Exception e){
            return getOriginDrawable(resId);
        }
    }

    /**
     * 获取drawable
     *
     * @param resName
     * @param resId
     * @return
     */
    public Drawable getDrawable(String resName,int resId){
        if(isHasSkin()){
            return getSkinDrawable(resName,resId);
        }
        return getOriginDrawable(resId);
    }

    @Override
    public Drawable getDrawable(String resName,String resType,int resId){
        if(isHasSkin()){
            try{
                int newResId = mSkinResources.getIdentifier(resName,resType,skinPackageName);
                if(newResId == 0){
                    return getOriginDrawable(resId);
                }
                return ResourcesCompat.getDrawable(mSkinResources,newResId,null);
            } catch(Exception e){
                return getOriginDrawable(resId);
            }
        }
        return getOriginDrawable(resId);
    }

    /**
     * 根据资源id获取drawable
     *
     * @param resId
     * @return
     */
    public Drawable getDrawable(int resId){
        if(isHasSkin()){
            return getSkinDrawable(getSkinResourceName(resId),resId);
        }
        return getOriginDrawable(resId);
    }


    @Override
    public ColorStateList getColorStateList(int resId){
        if(isHasSkin()){
            return getSkinColorStateList(getSkinResourceName(resId),resId);
        }
        return getOriginColorStateList(resId);
    }

    @Override
    public ColorStateList getColorStateList(String resName,int resId){
        if(isHasSkin()){
            return getSkinColorStateList(resName,resId);
        }
        return getOriginColorStateList(resId);
    }
}
