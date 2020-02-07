package com.skin.test;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.skin.libs.iface.OnSkinViewMonitor;
import com.skin.libs.SkinManager;

public class FragmentActivity extends BaseActivity implements OnSkinViewMonitor {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container,new MainFragment());
        transaction.commit();
    }

    @Override
    public boolean isSkinView(View view) {
        if (view.getId() == R.id.s_view) {
            return true;
        }
        return false;
    }

    @Override
    public boolean applySkin(View view) {
        if (view instanceof SView) {
            ((SView) view).setImageDrawable(SkinManager.getInstance().getDrawable(R.drawable.ic_bg));
            return true;
        }
        return false;
    }
}
