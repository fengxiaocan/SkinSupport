package com.skin.test;

import android.app.Application;
import android.content.res.Resources;

import com.skin.libs.SkinManager;

/**
 * 描述:
 * DynamTheme-
 *
 * @Author thinkpad
 * @create 2018-08-25 13:01
 */
public class App extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.getInstance().init(this);
        SkinManager.getInstance().registerAssetSkin("skintheme.skin");
    }

}
