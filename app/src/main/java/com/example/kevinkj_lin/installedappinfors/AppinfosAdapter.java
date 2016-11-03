package com.example.kevinkj_lin.installedappinfors;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class AppinfosAdapter extends RecyclerView.Adapter<AppinfosAdapter.ViewHolder> {
    // ViewHolder 為 item layout
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView appNameTextView;
        public ImageView appIconTextView;

        public ViewHolder(View itemView){
            super(itemView);

            appNameTextView = (TextView) itemView.findViewById(R.id.tv_name);
            appIconTextView = (ImageView) itemView.findViewById(R.id.iv_icon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), appNameTextView.getText(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        /*
        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick: ");
            Toast.makeText(view.getContext(), "Click ", Toast.LENGTH_SHORT).show();
        }
        */
    }

    private List<AppInfo> mAppInfos;

    public AppinfosAdapter(List<AppInfo> appInfos){
        mAppInfos = appInfos;
    }

    @Override
    public int getItemCount() {
        return mAppInfos.size();
    }

    // 將 layout 轉成 ViewHolder
    // 相比 listView，可以減少 findViewById 的次數
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        View contactView = LayoutInflater.from(context).inflate(R.layout.item_appinfo, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(contactView);

        return viewHolder;
    }

    // 從 list 中取得 App 資料 並放到 viewHolder 的對應元件內
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        AppInfo appInfo = mAppInfos.get(position);
        TextView nameTextView = viewHolder.appNameTextView;
        ImageView iconImageView = viewHolder.appIconTextView;

        nameTextView.setText(appInfo.getName());
        iconImageView.setImageDrawable(appInfo.getIcon());
    }
}
