package com.skin.test;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.skin.libs.SkinManager;

public class MainFragment extends Fragment implements View.OnClickListener {

    private Button btnDefault;

    private Button btnBlue;

    private RecyclerView recyclerView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        btnDefault = view.findViewById(R.id.btn_default);
        recyclerView = view.findViewById(R.id.recycler_view);
        btnBlue = view.findViewById(R.id.btn_blue);
        btnDefault.setOnClickListener(this);
        btnBlue.setOnClickListener(this);

        MyAdapter myAdapter = new MyAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(myAdapter);
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

}
