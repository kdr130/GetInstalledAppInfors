package com.example.kevinkj_lin.installedappinfors;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 *該 Fragment 僅放一個 RecyclerView
 */
public class RecyclerViewFragment extends Fragment {

    public RecyclerViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        RecyclerView rvAppInfos = (RecyclerView) view.findViewById(R.id.recyclerView);
        Activity parentActivity = getActivity();

        AppinfosAdapter adapter = new AppinfosAdapter(AppInfo.generateSampleList(parentActivity.getPackageManager()));
        rvAppInfos.setAdapter(adapter);
        rvAppInfos.setLayoutManager(new LinearLayoutManager(parentActivity));
        // 使用 support lib 內建給 RecyclerView 的項目間隔線
        rvAppInfos.addItemDecoration(new DividerItemDecoration(parentActivity, DividerItemDecoration.VERTICAL));
        return view;
    }

}
