package androidx.appcompat.app;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.VectorEnabledTintResources;

public class AppCompatInflaterHelper{
    private static final String[] sClassPrefixList =
            {"android.widget.","android.webkit.","android.app.","android.view."};
    static final AppCompatViewInflater appCompatViewInflater = new AppCompatViewInflater();
    private static final boolean IS_PRE_LOLLIPOP = Build.VERSION.SDK_INT < 21;

    public static View createView(View parent,final String name,@NonNull Context context,@NonNull AttributeSet attrs)
    {
        View view = null;
        try{
            view = appCompatViewInflater.createView(parent,
                    name,
                    context,
                    attrs,
                    false,
                    IS_PRE_LOLLIPOP,
                    true,
                    VectorEnabledTintResources.shouldBeUsed());
        } catch(Throwable e){
        }
        if(view == null){
            try{
                switch(name){
                    case "LinearLayout":
                        view = createLinearLayout(context,attrs);
                        break;
                    case "RelativeLayout":
                        view = createRelativeLayout(context,attrs);
                        break;
                    case "FrameLayout":
                        view = createFrameLayout(context,attrs);
                        break;
                    default:
                        view = onCreateView(name, context, attrs);
                        break;
                }
            } catch(Throwable e){
            }
        }
        return view;
    }

    private static View onCreateView(final String name,@NonNull Context context,@NonNull AttributeSet attrs){
        View view = null;
        LayoutInflater from = LayoutInflater.from(context);
        if(name.indexOf(".") == -1){
            for(String prefix: sClassPrefixList){
                try{
                    view = from.createView(name,prefix,attrs);
                    if(view != null){
                       return view;
                    }
                } catch(Exception e){
                }
            }
        }else {
            try{
                view = from.createView(name,null,attrs);
            } catch(Exception e){
                e.printStackTrace();
            }
        }
        return view;
    }

    private static LinearLayout createLinearLayout(Context context,AttributeSet attrs){
        return new LinearLayout(context,attrs);
    }

    private static RelativeLayout createRelativeLayout(Context context,AttributeSet attrs){
        return new RelativeLayout(context,attrs);
    }

    private static FrameLayout createFrameLayout(Context context,AttributeSet attrs){
        return new FrameLayout(context,attrs);
    }
}
