package com.skin.test;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skin.libs.ISkinItem;
import com.skin.libs.SkinManager;

public class MyHolder extends RecyclerView.ViewHolder {
    public MyView textView;
    public SView sView;

    public MyHolder(@NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.tv_message);
        sView = itemView.findViewById(R.id.s_view);
        sView.setTag(new ISkinItem() {
            @Override
            public void apply() {
                sView.setImageDrawable(SkinManager.getInstance().getDrawable(R.drawable.ic_bg));
            }
        });
    }

}
