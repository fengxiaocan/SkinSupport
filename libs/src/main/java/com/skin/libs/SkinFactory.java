package com.skin.libs;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.skin.libs.attr.SkinAttr;
import com.skin.libs.attr.SkinAttrFactory;
import com.skin.libs.attr.SkinAttrSet;
import com.skin.libs.iface.ISkinItem;
import com.skin.libs.iface.OnSkinViewInterceptor;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 * DynamTheme-
 *
 * @Author thinkpad
 * @create 2018-08-25 13:02
 */
public class SkinFactory implements LayoutInflater.Factory2{

    private LayoutInflater.Factory factory;
    private LayoutInflater.Factory2 factory2;
    private AppCompatDelegate appCompatDelegate;

    private OnSkinViewInterceptor interceptor;//需要更换皮肤的第三方View监听器
    private List<SoftReference<ISkinItem>> skinItems = new ArrayList<>();

    public SkinFactory(AppCompatActivity activity){
        LayoutInflater inflater = activity.getLayoutInflater();
        this.factory = inflater.getFactory();
        this.factory2 = inflater.getFactory2();
        appCompatDelegate = activity.getDelegate();
        inflater.setFactory2(this);
    }

    public SkinFactory(Activity activity){
        LayoutInflater inflater = activity.getLayoutInflater();
        this.factory = inflater.getFactory();
        this.factory2 = inflater.getFactory2();
        inflater.setFactory2(this);
    }

    public SkinFactory(LayoutInflater.Factory2 factory2){
        this.factory2 = factory2;
    }

    public SkinFactory(LayoutInflater.Factory factory){
        this.factory = factory;
    }

    /**
     * 设置View的监听器
     *
     * @param interceptor
     */
    public void setInterceptor(OnSkinViewInterceptor interceptor){
        this.interceptor = interceptor;
    }

    @Override
    public View onCreateView(String name,Context context,AttributeSet attrs){
        return onCreateView(null,name,context,attrs);
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent,@NonNull String name,@NonNull Context context,
            @NonNull AttributeSet attrs)
    {
        View view = null;
        if(factory2 != null){
            view = factory2.onCreateView(parent,name,context,attrs);
        } else if(factory != null){
            view = factory.onCreateView(name,context,attrs);
        } else if(appCompatDelegate != null){
            view = appCompatDelegate.createView(parent,name,context,attrs);
        }
        if(view == null){
            view = createView(name,context,attrs);
        }
        if(view != null){
            collectViewAttr(view,context,attrs);
        }
        return view;
    }

    private View createView(String name,Context context,AttributeSet attrs){
        View view = null;
        try{
            if(- 1 == name.indexOf('.')){    //不带".",说明是系统的View -> 带".",说明是自定义的View
                if("View".equals(name)){
                    view = LayoutInflater.from(context).createView(name,"android.view.",attrs);
                }
                if(view == null){
                    view = LayoutInflater.from(context).createView(name,"android.widget.",attrs);
                }
                if(view == null){
                    view = LayoutInflater.from(context).createView(name,"android.webkit.",attrs);
                }
            } else{
                view = LayoutInflater.from(context).createView(name,null,attrs);
            }
        } catch(Exception e){
        }
        return view;
    }

    private void collectViewAttr(View view,Context context,AttributeSet attrs){
        if(view instanceof ISkinItem){
            //自定义view的皮肤支持
            skinItems.add(new SoftReference<>((ISkinItem)view));
            if(SkinManager.getInstance().isHasSkin()){
                SkinManager.getInstance().apply((ISkinItem)view);
            }
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
                    skinItem.addAttr(skinAttr);
                }
            }
        }

        if(skinItem != null){
            if(SkinManager.getInstance().isHasSkin()){
                SkinManager.getInstance().apply(skinItem);
            }
            skinItems.add(new SoftReference<ISkinItem>(skinItem));
        }

        if(interceptor != null){
            //拦截第三方控件
            SkinAttrSet attrSet = interceptor.interceptorView(view,context,attrs);
            if(attrSet != null){
                //返回的拦截结果不为空,添加到集合中
                skinItems.add(new SoftReference<ISkinItem>(attrSet));
                if(SkinManager.getInstance().isHasSkin()){
                    SkinManager.getInstance().apply(attrSet);
                }
            }
        }
    }

    public void apply(){
        for(SoftReference<ISkinItem> item: skinItems){
            if(item != null){
                SkinManager.getInstance().apply(item.get());
            }
        }
    }

    public void recycler(){
        skinItems.clear();
        skinItems = null;
        interceptor = null;
    }

}
