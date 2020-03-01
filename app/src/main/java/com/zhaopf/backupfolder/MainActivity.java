package com.zhaopf.backupfolder;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zhaopf.backupfolder.adapder.RecyclerViewAdapder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements com.zhaopf.backupfolder.listener.UploadBackup, com.zhaopf.backupfolder.listener.VerifyAccount {
    //请求状态码
    private static final int PERMISSION_REQUEST_CODE = 1; //权限请求码
    //读写权限
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private MenuItem app_settings;
    private String account;
    private String password;
    private String webDavUrl;
    private String source;
    private TextView tv_dir;
    private List<String> items = new ArrayList<String>();
    private List<String> backupItems = new ArrayList<String>();
    private RecyclerView recyclerView;
    private RecyclerViewAdapder adapter;
    private File f;
    private Button getPermission;
    private Button btGetDir;
    private Button btUpload;
    private boolean switch_backup;
    private TextView loginResult;
    private TextView backupQuantity;
    private int havaPermissions = 1;
    private int quantity = 0;

    @Override
    protected void onStart() {
        super.onStart();
        webDavUrl = getSharedPreferences("settings", MODE_PRIVATE).getString("web_dav_url", "https://dav.jianguoyun.com/dav/");
        account = getSharedPreferences("settings", MODE_PRIVATE).getString("web_dav_account", "");
        password = getSharedPreferences("settings", MODE_PRIVATE).getString("web_dav_password", "");

        verifyAccount();
    }

    private void verifyAccount() {
        if (account.length() != 0 || password.length() != 0) {
            new com.zhaopf.backupfolder.verifyAccount(MainActivity.this).execute(webDavUrl, account, password);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        account = "1119101855@qq.com";
//        password = "aiyrhkkkxjnfq936";
        initView();//初始化
        adapderSet();//设置适配器


        getPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS_STORAGE, PERMISSION_REQUEST_CODE);
                }
            }
        });
        if (account.length() == 0 || password.length() == 0) {
            showDialog();
        }

        if (source.length() != 0) {
            tv_dir.setText(source);
            f = new File(source);
            getAllFile(f);
            adapter.putData(items);
            adapter.notifyDataSetChanged();
            btGetDir.setText("更换备份目录");
            if (switch_backup) {
                for (int i = 0; i < backupItems.size(); i++) {
                    new com.zhaopf.backupfolder.UploadBackup(this).execute(items.get(i), backupItems.get(i), webDavUrl, account, password);
                }
            }
        } else {
            btGetDir.setText("选择备份目录");
        }
    }

    private void adapderSet() {
        adapter = new RecyclerViewAdapder(items, MainActivity.this);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void initView() {
        loginResult = findViewById(R.id.loginResult);
        recyclerView = findViewById(R.id.rv_loaddir);
        tv_dir = findViewById(R.id.tv_dir);
        getPermission = findViewById(R.id.getPermission);
        btGetDir = findViewById(R.id.bt_getDir);
        btUpload = findViewById(R.id.bt_upload);
        backupQuantity = findViewById(R.id.backupQuantity);
        switch_backup = getSharedPreferences("settings", MODE_PRIVATE).getBoolean("switch_backup", false);
        source = getSharedPreferences("settings", MODE_PRIVATE).getString("source", "");
        webDavUrl = getSharedPreferences("settings", MODE_PRIVATE).getString("web_dav_url", "https://dav.jianguoyun.com/dav/");
        account = getSharedPreferences("settings", MODE_PRIVATE).getString("web_dav_account", "");
        password = getSharedPreferences("settings", MODE_PRIVATE).getString("web_dav_password", "");
        SharedPreferences.Editor sp = getSharedPreferences("settings", MODE_PRIVATE).edit();
        sp.putString("web_dav_url", "https://dav.jianguoyun.com/dav/");
        sp.apply();
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS_STORAGE, PERMISSION_REQUEST_CODE);
        }//权限
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //授权
                    havaPermissions = 1;
                    Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
                    getPermission.setVisibility(View.GONE);
                    verifyAccount();
                } else {
                    //未授权
                    havaPermissions = 0;
                    Toast.makeText(MainActivity.this, "未获得存储权限", Toast.LENGTH_SHORT).show();
                    btGetDir.setEnabled(false);
                    btUpload.setEnabled(false);
                    getPermission.setVisibility(View.VISIBLE);
                }
                break;
            default:
                break;
        }
    }

    private void showDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("提示");
        alertDialog.setMessage("首次进入请设置账号密码");
        alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });
        alertDialog.setNegativeButton("退出应用", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_actions, menu);
        app_settings = menu.findItem(R.id.app_settings);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(this, SettingsActivity.class));
        return super.onOptionsItemSelected(item);
    }

    public void backupDir(View view) {
        SimpleFileChooser dialog = new SimpleFileChooser();
        dialog.setOnChosenListener(new SimpleFileChooser.SimpleFileChooserListener() {
            @Override
            public void onFileChosen(File file) {
                Toast.makeText(MainActivity.this, " 请选择一个目录 ", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onDirectoryChosen(File directory) {
                SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("source", directory.getAbsolutePath());
                editor.apply();
                backupItems.clear();
                items.clear();
                f = new File(directory.getAbsolutePath());
                getAllFile(f);
                adapter.putData(items);
                adapter.notifyDataSetChanged();
                tv_dir.setText(directory.getAbsolutePath());

            }

            @Override
            public void onCancel() {

            }
        });
        dialog.show(getFragmentManager(), "SimpleFileChooserDialog");
    }

    public void getAllFile(File dir) {
        //遍历目录下的目录、文件
        for (File f : dir.listFiles()) {
            //如果是目录，继续遍历
            if (f.isDirectory()) {
                getAllFile(f);
            } else {
                //如果是文件，添加名字和路径
                items.add(f.getName());
                backupItems.add(f.getPath());

            }
        }

    }

    public void getAll(View view) {
        for (int i = 0; i < backupItems.size(); i++) {
            new com.zhaopf.backupfolder.UploadBackup(this).execute(items.get(i), backupItems.get(i), webDavUrl, account, password);
        }
    }

    @Override
    public void upload(boolean b) {
        // Toast.makeText(this, b ? "上传成功！" : "上传失败", Toast.LENGTH_SHORT).show();
        if (b) {
            quantity++;
            backupQuantity.setText("已成功备份" + quantity + "个文件");
        } else {
            backupQuantity.setText("上传失败!");
        }
    }

    @Override
    public void verify(boolean b) {
        loginResult.setText(b ? "登录成功！" : "登录失败");
        if (b) {
            if (havaPermissions == 1) {
                btGetDir.setEnabled(true);
                btUpload.setEnabled(true);
            }
        } else {
            btGetDir.setEnabled(false);
            btUpload.setEnabled(false);
        }
    }
}
