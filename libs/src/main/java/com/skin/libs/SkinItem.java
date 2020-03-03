package com.skin.libs;

import android.view.View;
import android.view.ViewGroup;

import com.skin.libs.attr.SkinAttr;
import com.skin.libs.iface.ISkinItem;

import java.util.ArrayList;
import java.util.List;

public class SkinItem implements ISkinItem{

    private View view;
    private List<SkinAttr> attrs;

    public SkinItem(View view,List<SkinAttr> attrs){
        this.view = view;
        this.attrs = attrs;
    }

    public SkinItem(View view){
        this.view = view;
        this.attrs = new ArrayList<>();
    }

    public void addAttr(SkinAttr attr){
        this.attrs.add(attr);
    }

    public View getView(){
        return view;
    }

    @Override
    public void apply(){
        if(view == null || attrs == null)
            return;
        for(SkinAttr attr: attrs){
            attr.applySkin(view);
        }
    }
}
