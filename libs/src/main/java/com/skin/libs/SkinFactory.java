package com.skin.libs;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.skin.libs.attr.IAttr;
import com.skin.libs.attr.SkinAttr;
import com.skin.libs.attr.SkinAttrFactory;
import com.skin.libs.attr.SkinAttrSet;
import com.skin.libs.iface.ISkinItem;
import com.skin.libs.iface.OnInflaterInterceptor;
import com.skin.libs.iface.OnSkinViewInterceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 */
public class SkinFactory implements OnInflaterInterceptor{

    private final List<ISkinItem> skinItems = new ArrayList<>();
    private OnSkinViewInterceptor interceptor;//需要更换皮肤的第三方View监听器


    /**
     * 设置View的监听器
     *
     * @param interceptor
     */
    public void setInterceptor(OnSkinViewInterceptor interceptor){
        this.interceptor = interceptor;
    }

    public void collectViewAttr(View view,Context context,AttributeSet attrs){
        if(view instanceof ISkinItem){
            //自定义view的皮肤支持
            skinItems.add((ISkinItem)view);
            if(SkinManager.getInstance().isHasSkin()){
                SkinManager.getInstance().apply((ISkinItem)view);
            }
        }
        SkinAttrSet skinAttrSet = null;
        if(interceptor != null){
            //拦截第三方控件
            skinAttrSet = interceptor.interceptorView(view,context,attrs);
        }

        SkinItem skinItem = null;
        int attCount = attrs.getAttributeCount();
        for(int i = 0;i < attCount;++ i){
            String attributeName = attrs.getAttributeName(i);
            //是否支持该类型attribute
            if(SkinAttrFactory.isSupportedAttr(attributeName)){
                String attributeValue = attrs.getAttributeValue(i);
                if(attributeValue.startsWith("@")){
                    int resId = Integer.parseInt(attributeValue.substring(1));
                    if(resId == 0){
                        continue;
                    }
                    String resName = context.getResources().getResourceEntryName(resId);
                    String attrType = context.getResources().getResourceTypeName(resId);

                    SkinAttr skinAttr = SkinAttrFactory.createAttr(attributeName,attrType,resName,resId);

                    if(skinItem == null){
                        skinItem = new SkinItem(view);
                    }

                    if(skinAttrSet != null && skinAttrSet.isIncludeAttr(attributeName)){
                        //拦截的View的属性,过滤到集合中
                        skinAttrSet.addAttr(skinAttr);
                    }

                    skinItem.addAttr(skinAttr);
                }
            } else if(skinAttrSet != null && skinAttrSet.isIncludeAttr(attributeName)){
                //拦截的View的属性,过滤到集合中
                String attributeValue = attrs.getAttributeValue(i);
                if(attributeValue.startsWith("@")){
                    int resId = Integer.parseInt(attributeValue.substring(1));
                    if(resId == 0){
                        continue;
                    }
                    String resName = context.getResources().getResourceEntryName(resId);
                    String attrType = context.getResources().getResourceTypeName(resId);
                    IAttr attrSet = new IAttr(attributeName,attrType,resName,resId);
                    skinAttrSet.addAttr(attrSet);
                }
            }
        }

        if(skinItem != null){
            if(SkinManager.getInstance().isHasSkin()){
                SkinManager.getInstance().apply(skinItem);
            }
            skinItems.add(skinItem);
        }
        //判断拦截的View的属性是否为空
        if(skinAttrSet != null){
            skinItems.add(skinAttrSet);
            if(SkinManager.getInstance().isHasSkin()){
                SkinManager.getInstance().apply(skinAttrSet);
            }
        }
    }

    public void apply(){
        for(ISkinItem item: skinItems){
            if(item != null){
                SkinManager.getInstance().apply(item);
            }
        }
    }

    public List<ISkinItem> getSkinItems(){
        return skinItems;
    }

    public void removeISkinItem(ISkinItem item){
        skinItems.remove(item);
    }

    public void recycler(){
        skinItems.clear();
        interceptor = null;
    }

    @Override
    public void interceptorCreateView(Context context,View view,String name,AttributeSet attrs){
        collectViewAttr(view,context,attrs);
    }
}
