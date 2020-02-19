package com.skin.libs;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.skin.libs.attr.SkinAttr;
import com.skin.libs.attr.SkinAttrFactory;
import com.skin.libs.iface.ISkinItem;
import com.skin.libs.iface.OnSkinViewMonitor;

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
public class SkinFactory implements LayoutInflater.Factory {

    private OnSkinViewMonitor viewMonitor;//需要更换皮肤的第三方View监听器
    private List<SoftReference<ISkinItem>> skinItems = new ArrayList<>();
    private List<SoftReference<View>> skinViews;

    public SkinFactory(AppCompatActivity activity) {
        activity.getLayoutInflater().setFactory(this);
    }

    /**
     * 设置View的监听器
     *
     * @param viewMonitor
     */
    public void setViewMonitor(OnSkinViewMonitor viewMonitor) {
        this.viewMonitor = viewMonitor;
        this.skinViews = new ArrayList<>();
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = createView(name, context, attrs);
        if (view != null) {
            collectViewAttr(view, context, attrs);
            if (viewMonitor != null) {
                if (viewMonitor.isSkinView(view)) {
                    skinViews.add(new SoftReference<View>(view));
                    if (SkinManager.getInstance().isHasSkin()) {
                        viewMonitor.applySkin(view);
                    }
                }
            }
        }
        return view;
    }

    private View createView(String name, Context context, AttributeSet attrs) {
        View view = null;
        try {
            if (-1 == name.indexOf('.')) {    //不带".",说明是系统的View
                if ("View".equals(name)) {
                    view = LayoutInflater.from(context).createView(name, "android.view.", attrs);
                }
                if (view == null) {
                    view = LayoutInflater.from(context).createView(name, "android.widget.", attrs);
                }
                if (view == null) {
                    view = LayoutInflater.from(context).createView(name, "android.webkit.", attrs);
                }
            } else {    //带".",说明是自定义的View
                view = LayoutInflater.from(context).createView(name, null, attrs);
                if (view instanceof ISkinItem) {
                    //自定义view的皮肤支持
                    skinItems.add(new SoftReference<ISkinItem>((ISkinItem) view));
                    if (SkinManager.getInstance().isHasSkin()) {
                        SkinManager.getInstance().apply((ISkinItem) view);
                    }
                }
            }
        } catch (Exception e) {
            view = null;
        }
        return view;
    }

    private void collectViewAttr(View view, Context context, AttributeSet attrs) {
        SkinItem skinItem = null;
        int attCount = attrs.getAttributeCount();
        for (int i = 0; i < attCount; ++i) {
            String attributeName = attrs.getAttributeName(i);
            //是否支持该类型attribute
            if (SkinAttrFactory.isSupportedAttr(attributeName)) {
                String attributeValue = attrs.getAttributeValue(i);
                if (attributeValue.startsWith("@")) {
                    int resId = Integer.parseInt(attributeValue.substring(1));
                    if (resId == 0) {
                        continue;
                    }
                    String resName = context.getResources().getResourceEntryName(resId);
                    String attrType = context.getResources().getResourceTypeName(resId);

                    SkinAttr skinAttr = SkinAttrFactory.createAttr(attributeName, attrType, resName, resId);
                    if (skinItem == null) {
                        skinItem = new SkinItem(view);
                    }
                    skinItem.addAttr(skinAttr);
                }
            }
        }
        if (skinItem != null) {
            if (SkinManager.getInstance().isHasSkin()) {
                SkinManager.getInstance().apply(skinItem);
            }
            skinItems.add(new SoftReference<ISkinItem>(skinItem));
        }
    }

    public void apply() {
        for (SoftReference<ISkinItem> item : skinItems) {
            if (item != null) {
                SkinManager.getInstance().apply(item.get());
            }
        }
        //设置了监听器的,说明不是background或者textColor,需要另外更换
        if (viewMonitor != null && skinViews != null) {
            for (SoftReference<View> skinView : skinViews) {
                if (skinView.get() != null) {
                    if (!viewMonitor.applySkin(skinView.get())) {
                        //如果监听器没有在activity中处理的,那么就是在创建View之后的setTag中设置了监听器
                        Object tag = skinView.get().getTag();
                        if (tag instanceof ISkinItem) {
                            ((ISkinItem) tag).apply();
                        }
                    }
                }
            }
        }
    }

    public void recycler() {
        skinItems.clear();
        if (skinViews != null) {
            skinViews.clear();
        }
        skinViews = null;
        skinItems = null;
        viewMonitor = null;
    }
}
