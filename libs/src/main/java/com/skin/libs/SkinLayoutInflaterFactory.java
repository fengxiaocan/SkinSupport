package com.skin.libs;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatInflaterHelper;

import com.skin.libs.iface.OnInflaterInterceptor;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.HashSet;

public class SkinLayoutInflaterFactory implements LayoutInflater.Factory2{
    //缓存已经找到的控件
    protected static final Class<?>[] sConstructorSignature = new Class[]{Context.class,AttributeSet.class};
    protected static final HashMap<String,Constructor<? extends View>> sConstructorMap = new HashMap<>();

    protected final HashSet<LayoutInflater.Factory2> factorySet = new HashSet<>();
    protected final HashSet<OnInflaterInterceptor> interceptorSet = new HashSet<>();

    public SkinLayoutInflaterFactory(){
    }

    public void addFactory2(LayoutInflater.Factory2 factory2){
        if(factory2 != null){
            factorySet.add(factory2);
        }
    }

    @Override
    public View onCreateView(String name,Context context,AttributeSet attrs){
        return onCreateView(null,name,context,attrs);
    }

    @Override
    public View onCreateView(View parent,String name,Context context,AttributeSet attrs){
        View view = null;
        for(LayoutInflater.Factory2 factory2: factorySet){
            try{
                view = factory2.onCreateView(parent,name,context,attrs);
            } catch(Exception e){
            }

            if(view != null){
                disposeCreateView(context,view,name,attrs);
                return view;
            }
        }

        try{
            view = AppCompatInflaterHelper.createView(parent,name,context,attrs);
        } catch(Throwable e){
        }
        if(view == null){
            view = createView(name,context,attrs);
        }

        if(view == null){
            Log.w("SkinSupport","create view be defeated;view name is " + name);
        } else{
            disposeCreateView(context,view,name,attrs);
        }
        return view;
    }

    private View createView(String name,Context context,AttributeSet attrs){
        Constructor<? extends View> constructor = sConstructorMap.get(name);
        if(constructor == null){
            try{
                Class<? extends View> aClass = context.getClassLoader().loadClass(name).asSubclass(View.class);
                constructor = aClass.getConstructor(sConstructorSignature);
                constructor.setAccessible(true);
                sConstructorMap.put(name,constructor);
            } catch(Throwable e){
                e.printStackTrace();
            }
        }
        if(constructor != null){
            try{
                return constructor.newInstance(context,attrs);
            } catch(Throwable e){
                e.printStackTrace();
            }
        }
        return null;
    }

    public void addOnInflaterInterceptor(OnInflaterInterceptor onInflaterInterceptor){
        if(onInflaterInterceptor != null){
            interceptorSet.add(onInflaterInterceptor);
        }
    }

    public void removeOnInflaterInterceptor(OnInflaterInterceptor onInflaterInterceptor){
        if(onInflaterInterceptor != null){
            interceptorSet.remove(onInflaterInterceptor);
        }
    }

    public void disposeCreateView(Context context,View view,String name,AttributeSet attrs){
        if(interceptorSet != null){
            for(OnInflaterInterceptor interceptor: interceptorSet){
                interceptor.interceptorCreateView(context,view,name,attrs);
            }
        }
    }
}
