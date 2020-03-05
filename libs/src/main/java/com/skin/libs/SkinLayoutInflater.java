package com.skin.libs;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;

import java.lang.reflect.Field;

public class SkinLayoutInflater extends LayoutInflater{

    public SkinLayoutInflater(LayoutInflater original,Context newContext){
        super(original,newContext);
        Factory2 f2 = getFactory2();
        if(! (f2 instanceof SkinLayoutInflaterFactory)){
            setPrivateFactory2().addFactory2(f2);
        }
    }

    @Override
    public LayoutInflater cloneInContext(Context newContext){
        return new SkinLayoutInflater(this,newContext);
    }

    @Override
    public void setFactory2(Factory2 factory){
        Factory2 f2 = getFactory2();
        if(f2 instanceof SkinLayoutInflaterFactory){
            ((SkinLayoutInflaterFactory)f2).addFactory2(factory);
        } else{
            setPrivateFactory2().addFactory2(factory);
        }
    }

    private SkinLayoutInflaterFactory setPrivateFactory2(){
        SkinLayoutInflaterFactory factory2 = new SkinLayoutInflaterFactory();
        try{
            super.setFactory2(factory2);
            return factory2;
        } catch(Exception e){
            try{
                Field field = LayoutInflater.class.getDeclaredField("mFactory2");
                //取消语言访问检查
                field.setAccessible(true);
                field.set(this,factory2);
            } catch(Exception ex){
                Log.w("SkinSupport","SkinLayoutInflater setPrivateFactory2 be defeated;");
            }
            return factory2;
        }
    }
}
