package com.example.kevinkj_lin.installedappinfors;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

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
            default:
                break;
        }

        return true;
    }

    public void saveAppInfosToSD(){
        //Toast.makeText(this, "click", Toast.LENGTH_SHORT).show();
        if ( mediaMounted() ) {
            saveToSdPermissionCheck();
        } else {
            Toast.makeText(this, "外部儲存裝置目前無法寫入或無法使用", Toast.LENGTH_SHORT).show();
        }
    }

    public void saveToSdPermissionCheck(){
        // 查看目前取得權限的狀態
        int permissionCheck = ContextCompat.checkSelfPermission(this,  Manifest.permission.WRITE_EXTERNAL_STORAGE);
        // 尚未取得寫入權限
        if ( PackageManager.PERMISSION_GRANTED !=  permissionCheck) {
            // 若使用者曾 拒絕同意權限
            // 則 shouldShowRequestPermissionRationale 會 return true
            // 因此可能就需要解釋一下為什麼需要這個權限
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Log.d("kkkk","saveToSdPermissionCheck: shouldShowRequestPermissionRationale return true");
                
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {
                // 表示是第一次安裝此 App，所以不需要加上解釋
                // 直接請求即可
                Log.d("kkkk", "saveToSdPermissionCheck: shouldShowRequestPermissionRationale return false");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:{
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("kkkk", "onRequestPermissionsResult: PERMISSION_GRANTED");
                } else {
                    Log.d("kkkk", "onRequestPermissionsResult: PERMISSION_DENIED");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }


    private boolean mediaMounted() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }
}

