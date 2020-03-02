package com.skin.libs.attr;

import android.util.AttributeSet;
import android.view.View;

import com.skin.libs.iface.ISkinItem;

import java.util.HashSet;
import java.util.Set;

public abstract class SkinAttrSet implements ISkinItem{

    protected View view;
    protected AttributeSet attrs;
    protected Set<IAttr> hashSet;

    public SkinAttrSet(View view,AttributeSet attrs){
        this.view = view;
        this.attrs = attrs;
        hashSet = new HashSet<>();
    }

    public View getView(){
        return view;
    }

    public AttributeSet getAttrs(){
        return attrs;
    }

    public Set<IAttr> getAttrSet(){
        return hashSet;
    }

    public abstract boolean isIncludeAttr(String attributeName);

    public final void addAttr(IAttr attrSet){
        hashSet.add(attrSet);
    }

}
