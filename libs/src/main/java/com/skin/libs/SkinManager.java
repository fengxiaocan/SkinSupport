package com.skin.libs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.LayoutInflater;

import com.skin.libs.iface.ISkinItem;
import com.skin.libs.iface.ISkinManager;
import com.skin.libs.iface.OnInflaterInterceptor;
import com.skin.libs.iface.OnSkinObserver;
import com.skin.libs.iface.OnSkinViewInterceptor;

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
public final class SkinManager implements ISkinManager{

    private static final SkinManager mInstance = new SkinManager();
    private static final String KEY = "skin_path";
    private Context context;
    private SkinResources skinResources;
    private Map<String,SkinFactory> skinFactories = new HashMap<>();
    private List<OnSkinObserver> listeners = new ArrayList<>();

    private SkinManager(){
    }

    public static SkinManager getInstance(){
        return mInstance;
    }

    /**
     * 是否是夜间模式
     *
     * @param configuration
     * @return
     */
    public static boolean isNightMode(Configuration configuration){
        int currentNightMode = configuration.uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES;
    }

    public static Context attachBaseContext(Context newBase){
        final LayoutInflater inflater = LayoutInflater.from(newBase);
        if(inflater instanceof SkinLayoutInflater)
            return newBase;
        return new LayoutInflaterContext(newBase);
    }

    /**
     * 是否是夜间模式
     *
     * @return
     */
    public boolean isNightMode(){
        Configuration configuration = context.getResources().getConfiguration();
        return isNightMode(configuration);
    }


    /**
     * 通知更新
     */
    private void notifySkinUpdate(){
        Set<String> keySet = skinFactories.keySet();
        for(String key: keySet){
            SkinFactory skinFactory = skinFactories.get(key);
            skinFactory.apply();
        }
        boolean hasSkin = isHasSkin();
        for(OnSkinObserver listener: listeners){
            listener.onSkinChange(hasSkin);
        }
    }

    /**
     * activity 初始化方法
     *
     * @param activity
     * @param factory
     */
    void installActivity(Activity activity,SkinFactory factory){
        String tag = activity.toString();
        if(activity instanceof OnSkinViewInterceptor){
            factory.setInterceptor((OnSkinViewInterceptor)activity);
        }
        if(activity instanceof OnSkinObserver){
            addSkinObserver(((OnSkinObserver)activity));
        }
        skinFactories.put(tag,factory);
    }


    @Override
    public void registerSkin(Activity activity){
        init(activity);

        LayoutInflater inflater = LayoutInflater.from(activity);
        LayoutInflater.Factory2 factory2 = inflater.getFactory2();
        SkinFactory skinFactory = new SkinFactory();
        if(factory2 instanceof SkinLayoutInflaterFactory){
            //先添加别的拦截器
            if(activity instanceof OnInflaterInterceptor){
                ((SkinLayoutInflaterFactory)factory2).addOnInflaterInterceptor((OnInflaterInterceptor)activity);
            }
            
            ((SkinLayoutInflaterFactory)factory2).addOnInflaterInterceptor(skinFactory);
        } else{
            SkinLayoutInflaterFactory inflaterFactory = new SkinLayoutInflaterFactory();
            //先添加别的拦截器
            if(activity instanceof OnInflaterInterceptor){
                inflaterFactory.addOnInflaterInterceptor((OnInflaterInterceptor)activity);
            }

            inflaterFactory.addOnInflaterInterceptor(skinFactory);
            inflater.setFactory2(inflaterFactory);
        }
        installActivity(activity,skinFactory);
    }

    @Override
    public void addSkinObserver(OnSkinObserver skinObserver){
        listeners.add(skinObserver);
    }

    @Override
    public void removeSkinObserver(OnSkinObserver skinObserver){
        listeners.remove(skinObserver);
    }

    /**
     * 取消activity的注册监听
     */
    @Override
    public void unregisterSkin(Activity activity){
        SkinFactory factory = skinFactories.remove(activity.toString());
        if(activity instanceof OnSkinObserver){
            removeSkinObserver(((OnSkinObserver)activity));
        }
        if(factory != null){
            factory.recycler();
        }
    }

    /**
     * 初始化
     */
    @Override
    public void init(Context context){
        if(this.context == null){
            this.context = context.getApplicationContext();
        }
        if(this.skinResources == null){
            this.skinResources = new SkinResources(context);
        }
    }

    /**
     * 加载皮肤
     *
     * @param name 皮肤的名字
     */
    @Override
    public void loadSkin(String name){
        if(TextUtils.isEmpty(name))
            return;
        new LoadTask().execute(name);
    }

    @Override
    public void loadLastSkin(){
        //加载上一次的
        String skinPath = SPUtil.get(context,KEY,"");
        if(! TextUtils.isEmpty(skinPath)){
            File file = new File(skinPath);
            if(file.exists()){
                loadSkin(file.getName());
            }
        }
    }

    /**
     * 重置 默认的主题
     */
    @Override
    public void restoreDefaultTheme(){
        skinResources.setSkinResources(null,null);
        SPUtil.put(context,KEY,"");
        notifySkinUpdate();
    }

    /**
     * 提交皮肤更改
     *
     * @param skinItem
     */
    @Override
    public void apply(ISkinItem skinItem){
        if(skinItem != null){
            skinItem.apply();
        }
    }

    @Override
    public SkinFactory getSkinFactory(Activity activity){
        return skinFactories.get(activity.toString());
    }

    @Override
    public boolean isHasSkin(){
        return skinResources.isHasSkin();
    }

    /**
     * 获取皮肤所在文件夹
     *
     * @return
     */
    @Override
    public File getSkinDir(){
        File skinDir = new File(context.getFilesDir().getParentFile(),"skin");
        skinDir.mkdir();
        return skinDir;
    }

    /**
     * 把asset中的皮肤文件复制到内存卡中
     */
    @Override
    public void registerAssetSkin(String name){
        try{
            InputStream open = context.getAssets().open(name);
            registerSkin(open,name);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void registerFileSkin(String fileName){
        try{
            InputStream open = new FileInputStream(fileName);
            registerSkin(open,fileName);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 注册皮肤
     */
    @Override
    public void registerSkin(final InputStream is,final String name){
        File skinDir = getSkinDir();
        File file = new File(skinDir,name);
        FileOutputStream fos = null;
        try{
            byte[] data = new byte[2048];
            int nbread = 0;
            fos = new FileOutputStream(file,false);
            while((nbread = is.read(data)) > - 1){
                fos.write(data,0,nbread);
            }
        } catch(Exception ex){
        } finally{
            closeIo(is);
            closeIo(fos);
        }
    }

    private void closeIo(Closeable closeable){
        try{
            if(closeable != null){
                closeable.close();
            }
        } catch(IOException e){
        }
    }

    @Override
    public int getColor(int resId){
        return skinResources.getColor(resId);
    }

    @Override
    public int getColor(String resName,int resId){
        return skinResources.getColor(resName,resId);
    }

    @Override
    public ColorStateList getColorStateList(int resId){
        return skinResources.getColorStateList(resId);
    }

    @Override
    public ColorStateList getColorStateList(String resName,int resId){
        return skinResources.getColorStateList(resName,resId);
    }

    @Override
    public Drawable getDrawable(int resId){
        return skinResources.getDrawable(resId);
    }

    @Override
    public Drawable getDrawable(String resName,int resId){
        return skinResources.getDrawable(resName,resId);
    }

    @Override
    public Drawable getDrawable(String resName,String resType,int resId){
        return skinResources.getDrawable(resName,resType,resId);
    }

    @SuppressLint("StaticFieldLeak")
    class LoadTask extends AsyncTask<String,Void,Resources>{
        @Override
        protected Resources doInBackground(String... paths){
            try{
                File skinFile = new File(getSkinDir(),paths[0]);
                if(! skinFile.exists()){
                    return null;
                }
                String skinPkgPath = skinFile.getAbsolutePath();

                PackageManager mPm = context.getPackageManager();
                PackageInfo mInfo = mPm.getPackageArchiveInfo(skinPkgPath,PackageManager.GET_ACTIVITIES);
                skinResources.setSkinPackageName(mInfo.packageName);

                AssetManager assetManager = AssetManager.class.newInstance();
                Method addAssetPath = assetManager.getClass().getMethod("addAssetPath",String.class);
                addAssetPath.invoke(assetManager,skinPkgPath);
                Resources superRes = context.getResources();
                Resources skinResource = new Resources(assetManager,
                        superRes.getDisplayMetrics(),
                        superRes.getConfiguration());
                //保持皮肤
                SPUtil.put(context,KEY,skinPkgPath);
                return skinResource;
            } catch(Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Resources resources){
            skinResources.setSkinResources(resources);
            if(skinResources.isHasSkin()){
                notifySkinUpdate();
            }
        }
    }
}
