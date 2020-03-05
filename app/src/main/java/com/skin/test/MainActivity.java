package com.skin.test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.skin.libs.SkinManager;
import com.skin.libs.attr.SkinAttrSet;
import com.skin.libs.iface.OnSkinViewInterceptor;


public class MainActivity extends BaseActivity implements View.OnClickListener, OnSkinViewInterceptor{

    private Button btnDefault;

    private Button btnBlue;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnDefault = findViewById(R.id.btn_default);
        recyclerView = findViewById(R.id.recycler_view);
        btnBlue = findViewById(R.id.btn_blue);
        btnDefault.setOnClickListener(this);
        btnBlue.setOnClickListener(this);

        MyAdapter myAdapter = new MyAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(myAdapter);
        startActivity(new Intent(this, FragmentActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_default:
                SkinManager.getInstance().restoreDefaultTheme();
                break;
            case R.id.btn_blue:
                SkinManager.getInstance().loadSkin("night.skin");
                break;
        }
    }
    @Override
    public SkinAttrSet interceptorView(View view,Context context,AttributeSet attrs){
        if (view.getId() == R.id.s_view) {
            return new SkinAttrSet(view,attrs){
                @Override
                public boolean isIncludeAttr(String attributeName){
                    return true;
                }

                @Override
                public void apply(){
                    ((SView) view).setImageDrawable(SkinManager.getInstance().getDrawable(R.drawable.ic_bg));
                }
            };
        }
        return null;
    }
}
