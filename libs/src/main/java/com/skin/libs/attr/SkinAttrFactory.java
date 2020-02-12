package com.skin.libs.attr;


import androidx.collection.ArraySet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by _SOLID
 * Date:2016/4/14
 * Time:9:47
 */
public class SkinAttrFactory {

    private static LinkedHashSet<SkinAttr> sSupportAttr = new LinkedHashSet<>();

    static {
        sSupportAttr.add(new BackgroundAttr());
        sSupportAttr.add( new TextColorAttr());
    }

    /**
     * 创建支持的attr
     * @param attrName
     * @param attrType
     * @param resName
     * @param resId
     * @return
     */
    public static SkinAttr createAttr(String attrName, String attrType, String resName, int resId) {
        for (SkinAttr skinAttr : sSupportAttr) {
            if (skinAttr.getAttrName().equals(attrName)) {
                return skinAttr.clone(attrType, resName, resId);
            }
        }
        return null;
    }

    /**
     * 是否支持该类型的AttrName
     * @param attrName
     * @return
     */
    public static boolean isSupportedAttr(String attrName) {
        for (SkinAttr skinAttr : sSupportAttr) {
            if (skinAttr.getAttrName().equals(attrName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 添加支持的类型
     * @param skinAttr
     */
    public static void addSupportAttr(SkinAttr skinAttr) {
        sSupportAttr.add(skinAttr);
    }

    public static void addSupportSrcAttr() {
        sSupportAttr.add(new ImageViewSrcAttr());
    }

}
