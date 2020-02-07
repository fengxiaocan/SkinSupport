package com.skin.test.skin.base;

import android.app.Application;

import com.skin.test.skin.loader.SkinManager;


/**
 * Created by _SOLID
 * Date:2016/4/14
 * Time:10:54
 * Desc:
 */
public class SkinBaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.getInstance().init(this);
    }
}
