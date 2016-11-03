package com.example.kevinkj_lin.installedappinfors;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private final String TAG = "InstalledAppInfos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFragment();
    }

    private  void initFragment(){
        RecyclerViewFragment rvFragment = new RecyclerViewFragment();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.add(R.id.frameLayout, rvFragment);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.save:
                saveAppInfosToSD();
                break;
            case R.id.read:
                File dir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                readAppInfoFromSd(dir, "AppInfos");
                break;
            default:
                break;
        }

        return true;
    }

    public void saveAppInfosToSD() {
        if ( mediaMounted() ) {
            saveToSdPermissionCheck();
        } else {
            Toast.makeText(this, R.string.cannot_use_storage, Toast.LENGTH_SHORT).show();
        }
    }

    public void saveToSdPermissionCheck(){
        // 查看目前取得權限的狀態
        int permissionCheck = ContextCompat.checkSelfPermission(this,  Manifest.permission.WRITE_EXTERNAL_STORAGE);
        // 尚未取得寫入權限
        if ( PackageManager.PERMISSION_GRANTED !=  permissionCheck) {
            // 若使用者曾經 拒絕權限請求
            // 則 shouldShowRequestPermissionRationale 會 return true
            // 因此可能就需要解釋一下為什麼需要這個權限
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // 顯示 AlertDialog 說明要求這個權限的原因
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.alert_title)
                        .setIcon(R.drawable.ic_dialog_alert)
                        .setMessage(R.string.alert_msg)
                        .setPositiveButton(R.string.text_btYes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                // 再請求權限
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                            }
                        })
                        .setNegativeButton(R.string.text_btNo, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();

            } else {
                // 表示尚未詢問過，所以不需要加上原因解釋
                // 直接請求即可
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

            }
        } else {  // 已取得寫入權限
            File dir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            saveFile(dir, "AppInfos");
        }
    }

    private boolean mediaMounted() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:{
                // 若 取消請求 則 grantResults
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 將資料儲存至 私有外部公共 路徑
                    File dir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                    saveFile(dir, "AppInfos");
                }
                return;
            }
        }
    }

    private void saveFile(File dir, String fileName) {
        OutputStream os = null;

        try {
            // 檢查目錄是否存在
            if (!dir.exists()) {
                // 建立目錄
                if (!dir.mkdirs()) {
                    Toast.makeText(this, R.string.cannot_make_dir, Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            List<AppInfo> list = AppInfo.generateSampleList(getPackageManager());
            File file = new File(dir, fileName);
            os = new FileOutputStream(file);
            StringBuilder sb = new StringBuilder();

            for ( AppInfo info : list ) {
                //os.write(info.getName().getBytes());
                sb.append(info.getName()+"\n");
            }
            os.write(sb.toString().getBytes());

            Toast.makeText(this, getResources().getString(R.string.save_success) + dir, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        } finally {
            try {
                if (os != null) {
                    os.close();
                }

            } catch (IOException e) {
                Log.e(TAG, e.toString());
            }

        }
    }

    private void readAppInfoFromSd(File dir, String fileName) {
        if ( !mediaMounted() ) {
            Toast.makeText(this, R.string.cannot_use_storage, Toast.LENGTH_SHORT).show();
        }
        try{
            File file = new File(dir, fileName);
            if ( !file.exists() ) {
                Toast.makeText(this, R.string.never_save, Toast.LENGTH_SHORT).show();
                return;
            }

            // \\Z 是直接讀到 FILE 的結束 的 pattern
            String fileContent = new Scanner(file).useDelimiter("\\Z").next();
            Toast.makeText(this, getResources().getString(R.string.read_success) + fileContent, Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }

    }
}

