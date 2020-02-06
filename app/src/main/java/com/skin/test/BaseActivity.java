package com.skin.test;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.skin.libs.SkinManager;


/**
 * 描述:
 * DynamTheme-
 * @Author thinkpad
 * @create 2018-08-25 13:02
 */
public abstract class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        SkinManager.getInstance().registerSkin(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SkinManager.getInstance().unregisterSkin(this);
    }
}
