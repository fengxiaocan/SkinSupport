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
        } catch(Exception e){
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
                        view = LayoutInflater.from(context).createView(name,null,attrs);
                        break;
                }
            } catch(Exception e){
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
