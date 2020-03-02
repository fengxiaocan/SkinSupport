package com.skin.libs;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

@Deprecated
final class SkinInflater extends LayoutInflater{
    private static final String[] sClassPrefixList = {"android.widget.","android.webkit.","android.app."};

    SkinInflater(Context newContext,LayoutInflater original){
        super(original,newContext);
        Factory2 factory2 = original.getFactory2();
        super.setFactory2(new SkinFactory().setFactory2(factory2));
    }

    @Override
    public LayoutInflater cloneInContext(Context newContext){
        return new SkinInflater(newContext,this);
    }

    @Override
    public void setFactory(Factory factory){
        SkinFactory f = (SkinFactory)getFactory2();
        f.setFactory(factory);
    }

    @Override
    public void setFactory2(Factory2 factory2){
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for(StackTraceElement element: stackTrace){
            Log.v("StackTraceElement",new StringBuilder("所在类:").append(element.getClassName()).append(";所在方法:").append(
                    element.getMethodName()).append(";所在行数").append(element.getLineNumber()).toString());
        }
        SkinFactory f = (SkinFactory)getFactory2();
        f.setFactory2(factory2);
    }

    @Override
    protected View onCreateView(String name,AttributeSet attrs) throws ClassNotFoundException{
        for(String prefix: sClassPrefixList){
            try{
                View view = createView(name,prefix,attrs);
                if(view != null){
                    return view;
                }
            } catch(Exception e){
            }
        }
        return super.onCreateView(name,attrs);
    }
}
