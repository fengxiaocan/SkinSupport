package com.skin.test;

import android.app.Application;

import com.skin.libs.SkinManager;

/**
 * 描述:
 * DynamTheme-
 *
 * @Author thinkpad
 * @create 2018-08-25 13:01
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.getInstance().init(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                SkinManager.getInstance().registerAssetSkin("night.skin");
                SkinManager.getInstance().loadLastSkin();
            }
        }).start();
    }

}
