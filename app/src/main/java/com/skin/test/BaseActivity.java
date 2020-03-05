package com.skin.test;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.skin.libs.SkinManager;


/**
 * 描述:
 * DynamTheme-
 *
 * @Author thinkpad
 * @create 2018-08-25 13:02
 */
public abstract class BaseActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        SkinManager.getInstance().registerSkin(this);
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onDestroy(){
        super.onDestroy();
        SkinManager.getInstance().unregisterSkin(this);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        if(SkinManager.isNightMode(newConfig)){
            SkinManager.getInstance().loadSkin("night.skin");
        } else{
            SkinManager.getInstance().restoreDefaultTheme();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase){
        super.attachBaseContext(SkinManager.attachBaseContext(newBase));
    }
}
