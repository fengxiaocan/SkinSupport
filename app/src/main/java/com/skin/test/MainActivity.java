package com.skin.test;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.skin.libs.OnSkinViewMonitor;
import com.skin.libs.SkinManager;


public class MainActivity extends BaseActivity implements View.OnClickListener, OnSkinViewMonitor {

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
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(myAdapter);
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
                SkinManager.getInstance().loadSkin("skin.skin");
                break;
        }
    }

    @Override
    public boolean inflateSkin(View view) {
        if (view.getId() == R.id.s_view) {

            return true;
        }
        return false;
    }

    @Override
    public boolean applySkin(View view) {
        if (view instanceof SView) {
            ((SView) view).setImageDrawable(SkinManager.getInstance().getDrawable(R.drawable.ic_bg));
        }
        return false;
    }
}
