package com.skin.libs.iface;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;

import androidx.appcompat.app.AppCompatActivity;

import com.skin.libs.SkinFactory;

import java.io.File;
import java.io.InputStream;

public interface ISkinManager extends ISkinResources{
    /**
     * 初始化
     *
     * @param context
     */
    void init(Context context);

    void registerSkin(Activity activity);

    void addSkinObserver(OnSkinObserver skinObserver);

    void removeSkinObserver(OnSkinObserver skinObserver);

    void unregisterSkin(Activity activity);

    /**
     * 加载皮肤
     *
     * @param name 皮肤的名字
     */
    void loadSkin(String name);

    /**
     * 加载上次的主题
     */
    void loadLastSkin();

    /**
     * 重置 默认的主题
     */
    void restoreDefaultTheme();

    /**
     * 提交皮肤更改
     */
    void apply(ISkinItem skinItem);

    SkinFactory getSkinFactory(Activity activity);

    boolean isHasSkin();

    /**
     * 获取皮肤所在文件夹
     */
    File getSkinDir();

    /**
     * 把asset中的皮肤文件复制到内存卡中
     */
    void registerAssetSkin(String name);

    void registerFileSkin(String fileName);

    /**
     * 注册皮肤
     */
    void registerSkin(InputStream is,String name);
}
