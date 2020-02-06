package com.skin.libs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 描述:
 * 皮肤管理器
 * 步骤:
 * 1.先在application中初始化 {@link #init(Context)}
 * 2.继续在application中注册所有的皮肤文件 {@link #registerAssetSkin},{@link #registerFileSkin},{@link #registerSkin}
 * 3.
 */
public class SkinManager {

    private static final SkinManager mInstance = new SkinManager();
    //    private final String KEY = "skin_path";
    private Resources mSkinResources;
    private Context context;
    private String skinPackageName;
    private boolean isExternalSkin;
    private Map<String, SkinFactory> skinFactories = new HashMap<>();
    private List<OnSkinObserver> listeners = new ArrayList<>();

    private SkinManager() { }

    public static SkinManager getInstance() {
        return mInstance;
    }


    /**
     * 断言
     */
    private void judge() {
        if (context == null) {
            throw new IllegalStateException("context is null:SkinManager is must init in application");
        }
    }

    /**
     * 通知更新
     */
    private void notifySkinUpdate() {
        Set<String> keySet = skinFactories.keySet();
        for (String key : keySet) {
            SkinFactory skinFactory = skinFactories.get(key);
            skinFactory.apply();
        }
        for (OnSkinObserver listener : listeners) {
            listener.onSkinChange();
        }
    }

    /**
     * 在activity中注册
     */
    public void registerSkin(AppCompatActivity activity) {
        String tag = activity.toString();
        SkinFactory factory = new SkinFactory(activity);
        if (activity instanceof OnSkinViewMonitor) {
            factory.setViewMonitor((OnSkinViewMonitor) activity);
        }
        skinFactories.put(tag, factory);
    }

    public void addSkinObserver(OnSkinObserver skinObserver){
        listeners.add(skinObserver);
    }

    public void removeSkinObserver(OnSkinObserver skinObserver){
        listeners.add(skinObserver);
    }

//    /**
//     * 获取皮肤路径
//     *
//     * @return
//     */
//    public String getSkinPath() {
//        judge();
//        String skinPath = (String) SPUtil.get(context, KEY, "");
//        return TextUtils.isEmpty(skinPath) ? null : skinPath;
//    }

//    /**
//     * 保存皮肤
//     *
//     * @param path
//     */
//    public void saveSkinPath(String path) {
//        judge();
//        SPUtil.put(context, KEY, path);
//    }

    /**
     * 取消activity的注册监听
     *
     * @param activity
     */
    public void unregisterSkin(AppCompatActivity activity) {
        SkinFactory factory = skinFactories.remove(activity.toString());
        if (factory != null) {
            factory.recycler();
        }
    }

    /**
     * 是否有设置皮肤
     *
     * @return
     */
    public boolean isExternalSkin() {
        judge();
        return isExternalSkin;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        this.context = context.getApplicationContext();
//        String skinPath = (String) SPUtil.get(context, KEY, "");
//        isExternalSkin = !TextUtils.isEmpty(skinPath);
//        loadSkin(getSkinPath());
    }

    /**
     * 加载皮肤
     *
     * @param name 皮肤的名字
     */
    public void loadSkin(String name) {
        judge();
        if (name == null)
            return;
        new LoadTask().execute(name);
    }

    /**
     * 获取原始颜色值
     *
     * @param resId
     * @return
     */
    private int getOriginColor(int resId) {
        judge();
        return context.getResources().getColor(resId);
    }

    /**
     * 获取皮肤颜色值
     *
     * @param resourceName
     * @return
     */
    private int getSkinColor(String resourceName, int resId) {
        try {
            int newResId = mSkinResources.getIdentifier(resourceName, "color", skinPackageName);
            return mSkinResources.getColor(newResId);
        } catch (Exception e) {
            return getOriginColor(resId);
        }
    }

    /**
     * 是否没有皮肤
     *
     * @return
     */
    private boolean isOriginTheme() {
        return mSkinResources == null || !isExternalSkin;
    }

    /**
     * 获取皮肤资源的名字
     *
     * @param resId
     * @return
     */
    public String getSkinResourceName(int resId) {
        String entryName = context.getResources().getResourceEntryName(resId);
//        String resourceName = context.getResources().getResourceName(resId);
//        Log.e("noah","entryName="+entryName+" resourceName="+resourceName);
//        resourceName = resourceName.replace(packageName, skinPackageName);
        return entryName;
    }

    /**
     * 根据资源id获取颜色值
     *
     * @param resId
     * @return
     */
    public int getColor(int resId) {
        if (isOriginTheme()) {
            return getOriginColor(resId);
        }
        return getSkinColor(getSkinResourceName(resId), resId);
    }

    /**
     * 获取颜色值
     *
     * @param resName
     * @param resId
     * @return
     */
    public int getColor(String resName, int resId) {
        if (isOriginTheme()) {
            return getOriginColor(resId);
        }
        return getSkinColor(resName, resId);
    }

    /**
     * 获取原始的主题drawable
     *
     * @param resId
     * @return
     */
    private Drawable getOriginDrawable(int resId) {
        judge();
        return context.getResources().getDrawable(resId);
    }

    /**
     * 获取其他皮肤的drawable
     *
     * @param resName
     * @param resId
     * @return
     */
    private Drawable getSkinDrawable(String resName, int resId) {
        try {
            int newResId = mSkinResources.getIdentifier(resName, "drawable", skinPackageName);
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                return mSkinResources.getDrawable(newResId);
            } else {
                return mSkinResources.getDrawable(newResId, null);
            }
        } catch (Exception e) {
            return getOriginDrawable(resId);
        }
    }

    /**
     * 获取drawable
     *
     * @param resName
     * @param resId
     * @return
     */
    public Drawable getDrawable(String resName, int resId) {
        if (isOriginTheme()) {
            return getOriginDrawable(resId);
        }
        return getSkinDrawable(resName, resId);
    }

    /**
     * 根据资源id获取drawable
     *
     * @param resId
     * @return
     */
    public Drawable getDrawable(int resId) {
        if (isOriginTheme()) {
            return getOriginDrawable(resId);
        }
        return getSkinDrawable(getSkinResourceName(resId), resId);
    }

    /**
     * 重置 默认的主题
     */
    public void restoreDefaultTheme() {
        judge();
//        SPUtil.put(context, KEY, "");
        isExternalSkin = false;
        mSkinResources = null;
        notifySkinUpdate();
    }

    /**
     * 提交皮肤更改
     *
     * @param skinItem
     */
    public void apply(ISkinItem skinItem) {
        if (skinItem != null) {
            skinItem.apply();
        }
    }

    /**
     * 获取皮肤所在文件夹
     *
     * @return
     */
    public File getSkinDir() {
        File skinDir = new File(context.getFilesDir().getParentFile(), "skin");
        skinDir.mkdir();
        return skinDir;
    }

    /**
     * 把asset中的皮肤文件复制到内存卡中
     */
    public void registerAssetSkin(String name) {
        try {
            InputStream open = context.getAssets().open(name);
            registerSkin(open, name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registerFileSkin(String fileName) {
        try {
            InputStream open = new FileInputStream(fileName);
            registerSkin(open, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 注册皮肤
     */
    public void registerSkin(InputStream is, String name) {
        judge();
        File skinDir = getSkinDir();
        File file = new File(skinDir, name);
        FileOutputStream fos = null;
        try {
            byte[] data = new byte[2048];
            int nbread = 0;
            fos = new FileOutputStream(file, false);
            while ((nbread = is.read(data)) > -1) {
                fos.write(data, 0, nbread);
            }
        } catch (Exception ex) {
        } finally {
            closeIo(is);
            closeIo(fos);
        }
    }

    private void closeIo(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
        }
    }

    @SuppressLint("StaticFieldLeak")
    class LoadTask extends AsyncTask<String, Void, Resources> {
        @Override
        protected Resources doInBackground(String... paths) {
            try {
                File skinFile = new File(getSkinDir(), paths[0]);
                if (!skinFile.exists()) {
                    return null;
                }
                String skinPkgPath = skinFile.getAbsolutePath();

                PackageManager mPm = context.getPackageManager();
                PackageInfo mInfo = mPm.getPackageArchiveInfo(skinPkgPath, PackageManager.GET_ACTIVITIES);
                skinPackageName = mInfo.packageName;
                AssetManager assetManager = AssetManager.class.newInstance();
                Method addAssetPath = assetManager.getClass().getMethod("addAssetPath",
                        String.class);
                addAssetPath.invoke(assetManager, skinPkgPath);
                Resources superRes = context.getResources();
                Resources skinResource = new Resources(assetManager, superRes
                        .getDisplayMetrics(), superRes.getConfiguration());
//                saveSkinPath(skinPkgPath);
                return skinResource;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Resources resources) {
            super.onPostExecute(resources);
            mSkinResources = resources;
            if (mSkinResources != null) {
                isExternalSkin = true;
                notifySkinUpdate();
            }
        }
    }
}
