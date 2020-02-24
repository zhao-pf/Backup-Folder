package com.zhaopf.backupfolder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thegrizzlylabs.sardineandroid.DavResource;
import com.thegrizzlylabs.sardineandroid.Sardine;
import com.thegrizzlylabs.sardineandroid.impl.OkHttpSardine;
import com.zhaopf.backupfolder.adapder.RecyclerViewAdapder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements com.zhaopf.backupfolder.listener.UploadBackup {
    private MenuItem app_settings;
    private String account;
    private String password;
    private String backupPath;
    private String backupFilePath;
    private String backupName;
    private TextView tv_dir;
    private List<String> items=new ArrayList<String>();
    private List<String> backupItems=new ArrayList<String>();
    private RecyclerView recyclerView;
    private RecyclerViewAdapder adapter;
    private File f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        account = "1119101855@qq.com";
        password = "aiyrhkkkxjnfq936";
        backupFilePath = "/storage/emulated/0/Apk提取目录/Test.apk";
        backupPath = "/storage/emulated/0/Apk提取目录/";
        backupName = "Test.apk";
        recyclerView = findViewById(R.id.rv_loaddir);
        tv_dir = findViewById(R.id.tv_dir);
        adapter = new RecyclerViewAdapder(items,MainActivity.this);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

       // new com.zhaopf.backupfolder.UploadBackup(this).execute(items.get(1), backupItems.get(1), account, password);

        //adapter = new RecyclerViewAdapder(items,MainActivity.this);
        //adapter.notifyDataSetChanged();
        //new com.zhaopf.backupfolder.verifyAccount(this).execute(account, password);
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
        //new com.zhaopf.backupfolder.UploadBackup(this).execute(backupPath, backupAPKPath, account, password);
        dialog.show(getFragmentManager(), "SimpleFileChooserDialog");
    }

    @Override
    public void upload(boolean b) {
        if (b) {
            Toast.makeText(this, "备份成功！", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "备份失败！", Toast.LENGTH_LONG).show();
        }
    }

//    public void getAll(View view) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Sardine sardine = new OkHttpSardine();
//                sardine.setCredentials(account, password);
//                List<DavResource> resources = null;
//                try {
//                    resources = sardine.list("https://dav.jianguoyun.com/BackUp/");//如果是目录一定别忘记在后面加上一个斜杠
//                    for (DavResource res : resources) {
//                        //listNames=listNames+res+"\n";
//                        // Log.e("res.getName()", String.valueOf(res.getHref()));
//                        // Log.e("res.getName()", res.getName());
//                        if (res.isDirectory()) {
//                            Log.e("目录有：", res.getName());
//                        }
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//
//                }
//            }
//        }).start();
//    }

    public void getAllFile(File dir) {
        //遍历目录下的目录、文件
        for (File f : dir.listFiles()) {
            //如果是目录，继续遍历
            if (f.isDirectory()) {
                getAllFile(f);
            } else {
                //如果是文件，直接打印
                items.add(f.getName());
                backupItems.add(f.getPath());

            }
        }

    }

    public void getAll(View view) {
        for (int i=0;i<backupItems.size();i++){
            new com.zhaopf.backupfolder.UploadBackup(this).execute(items.get(i), backupItems.get(i), account, password);
            //new com.zhaopf.backupfolder.verifyAccount(this).execute(account, password);
        }
    }
}
