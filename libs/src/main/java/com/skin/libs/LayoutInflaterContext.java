package com.skin.libs;

import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;

public class LayoutInflaterContext extends ContextWrapper{
    private SkinLayoutInflater layoutInflater;

    public LayoutInflaterContext(Context base){
        super(base);
    }

    @Override
    public Object getSystemService(String name){
        if(LAYOUT_INFLATER_SERVICE.equals(name)){
            if(layoutInflater == null){
                LayoutInflater original = LayoutInflater.from(getBaseContext()).cloneInContext(this);
                layoutInflater = new SkinLayoutInflater(original,getBaseContext());
            }
            return layoutInflater;
        }
        return super.getSystemService(name);
    }
}
