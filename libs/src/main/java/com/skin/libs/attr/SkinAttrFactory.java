package com.skin.libs.attr;


import java.util.HashMap;

/**
 * Created by _SOLID
 * Date:2016/4/14
 * Time:9:47
 */
public class SkinAttrFactory{

    private static HashMap<String,SkinAttr> sSupportAttr = new HashMap<>();

    static{
        BackgroundAttr backgroundAttr = new BackgroundAttr();
        sSupportAttr.put(backgroundAttr.getAttrName(),backgroundAttr);
        TextColorAttr textColorAttr = new TextColorAttr();
        sSupportAttr.put(textColorAttr.getAttrName(),textColorAttr);
    }

    /**
     * 创建支持的attr
     *
     * @param attrName
     * @param attrType
     * @param resName
     * @param resId
     * @return
     */
    public static SkinAttr createAttr(String attrName,String attrType,String resName,int resId){
        SkinAttr skinAttr = sSupportAttr.get(attrName);
        if(skinAttr != null){
            return skinAttr.clone(attrType,resName,resId);
        }
        return null;
    }

    /**
     * 是否支持该类型的AttrName
     *
     * @param attrName
     * @return
     */
    public static boolean isSupportedAttr(String attrName){
        return sSupportAttr.containsKey(attrName);
    }

    /**
     * 添加支持的类型
     *
     * @param skinAttr
     */
    public static void addSupportAttr(SkinAttr skinAttr){
        sSupportAttr.put(skinAttr.getAttrName(),skinAttr);
    }

    public static void addSupportSrcAttr(){
        ImageViewSrcAttr srcAttr = new ImageViewSrcAttr();
        sSupportAttr.put(srcAttr.getAttrName(),srcAttr);
    }

}
