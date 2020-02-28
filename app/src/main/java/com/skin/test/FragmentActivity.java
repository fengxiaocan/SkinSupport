package com.skin.test;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.skin.libs.SkinManager;
import com.skin.libs.attr.SkinAttrSet;
import com.skin.libs.iface.OnSkinViewInterceptor;

public class FragmentActivity extends BaseActivity implements OnSkinViewInterceptor{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fragment);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container,new MainFragment());
        transaction.commit();
    }

    @Override
    public SkinAttrSet interceptorView(View view,Context context,AttributeSet attrs){
        if(view.getId() == R.id.s_view){
            return new SkinAttrSet(view,attrs){
                @Override
                public boolean isIncludeAttr(String attributeName){
                    return true;
                }

                @Override
                public void apply(){
                    ((SView)view).setImageDrawable(SkinManager.getInstance().getDrawable(R.drawable.ic_bg));
                }
            };
        }
        return null;
    }
}
