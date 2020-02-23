package com.zhaopf.backupfolder;

import android.os.AsyncTask;
import android.os.FileUtils;
import android.util.Log;
import android.widget.Toast;

import com.thegrizzlylabs.sardineandroid.DavResource;
import com.thegrizzlylabs.sardineandroid.Sardine;
import com.thegrizzlylabs.sardineandroid.impl.OkHttpSardine;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public class UploadBackup extends AsyncTask<String, Void, Boolean> {

    private com.zhaopf.backupfolder.listener.UploadBackup uploadBackup;

    public UploadBackup(MainActivity uploadBackup){
        this.uploadBackup = uploadBackup;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        String backupName = strings[0];
        String backupFilePath = strings[1];
        String account = strings[2];
        String password = strings[3];
        //ZipUtil.pack(new File(backupPath),new File(backupZipPath));
        Sardine sardine = new OkHttpSardine();
        sardine.setCredentials(account, password);
        try {
            sardine.createDirectory("https://dav.jianguoyun.com/dav/" + "BackUp");
            sardine.put("https://dav.jianguoyun.com/dav/BackUp/"+backupName,new File(backupFilePath),"application/x-www-form-urlencoded");

            //noinspection ResultOfMethodCallIgnored
            //new File(backupZipPath).delete();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean b) {
        super.onPostExecute(b);

        uploadBackup.upload(b);
    }
}
