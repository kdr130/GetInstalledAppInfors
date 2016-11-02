package com.example.kevinkj_lin.installedappinfors;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * AppInfo 儲存 App 需要顯示的資訊
 *  name: App 名稱
 *  icon: App 的圖示
 */

public class AppInfo {
    private String name;
    private Drawable icon;

    public AppInfo() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    // 根據傳入的 PackageManager 取得目前 使用者所安裝的 APP 資料
    public static List<AppInfo> generateSampleList(PackageManager pm){
        // 用來儲存 使用者安裝的 APP 資訊
        List<AppInfo> appInfos = new ArrayList<>();
        // 取得目前 所有 APP 的資訊(包括 系統 APP 以及 使用者所安裝的 APP)
        List<ApplicationInfo> list = pm.getInstalledApplications(0);

        // 對所有的 APP 資訊
        // 取得其中的 flag 判斷是否為系統 APP
        // 若與 ApplicationInfo.FLAG_SYSTEM 做 & 後為 1
        // 表示為 系統 APP
        // 若與 ApplicationInfo.FLAG_SYSTEM 做 & 後為 0
        // 表示為 使用者安裝 APP
        for( ApplicationInfo info : list ) {
            if ( (info.flags & ApplicationInfo.FLAG_SYSTEM ) == 0  ) {
                AppInfo appInfo = new AppInfo();
                appInfo.setName(pm.getApplicationLabel(info).toString());
                appInfo.setIcon(pm.getApplicationIcon(info));

                appInfos.add(appInfo);
            }
        }

        // 根據 APP Name 整理順序
        Collections.sort(appInfos, new Comparator<AppInfo>() {
            @Override
            public int compare(AppInfo o1, AppInfo o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        return appInfos;
    }
}
