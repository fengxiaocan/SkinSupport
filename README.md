# SkinSupport
一个皮肤管理器,能够实现无缝切换皮肤,主题等,简单操作
# 默认方法只能处理background跟textColor的皮肤切换
1.先在application中初始化 

    SkinManager.getInstance().init(this);
        
2.在application中注册皮肤文件(在loadSkin(skinname)之前注册也行)

    SkinManager.getInstance().registerAssetSkin("skintheme.skin");
        
3.在BaseActivity中onCreate方法之前注册 

    SkinManager.getInstance().registerSkin(this);
        
4.在BaseActivity中onDestroy 注销

    SkinManager.getInstance().unregisterSkin(this);

5.在需要切换皮肤的时候调用加载皮肤方法

    SkinManager.getInstance().loadSkin("skin.skin");
        
6.重置默认主题

    SkinManager.getInstance().restoreDefaultTheme();

7.需要切换第三方控件皮肤的可以在activity中实现OnSkinViewMonitor接口

    //查找需要换肤的View
    @Override
    public boolean inflateSkin(View view) {
        if (view.getId() == R.id.s_view) {
            //可以使用setTag方法设置皮肤更换监听,不用在applySkin方法中做换肤操作
            //view.setTag(new ISkinItem() {
            //@Override
            //public void apply() {
                //sView.setImageDrawable(SkinManager.getInstance().getDrawable(R.drawable.ic_bg));
            //}
            //});
            return true;
        }
        return false;
    }

    //换肤操作
    @Override
    public boolean applySkin(View view) {
        if (view.getId() == R.id.s_view && view instanceof SView) {
            ((SView) view).setImageDrawable(SkinManager.getInstance().getDrawable(R.drawable.ic_bg));
            //是否已经处理过换肤
            return true;
        }
        return false;
    }

8.自定义View可以实现ISkinItem接口,不需要在inflateSkin中过滤判断

    @Override
    public void apply() {
        //可以使用皮肤管理器获取color或者drawable
        setTextColor(SkinManager.getInstance().getColor(R.color.mainText));
    }
    
    
