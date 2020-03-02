package com.zhaopf.backupfolder;

import android.os.AsyncTask;

import com.thegrizzlylabs.sardineandroid.Sardine;
import com.thegrizzlylabs.sardineandroid.impl.OkHttpSardine;

import java.io.File;
import java.io.IOException;


class UploadBackup extends AsyncTask<String, Void, Boolean> {

    private final com.zhaopf.backupfolder.listener.UploadBackup uploadBackup;

    public UploadBackup(MainActivity uploadBackup) {
        this.uploadBackup = uploadBackup;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        String backupName = strings[0];
        String backupFilePath = strings[1];
        String davurl = strings[2];
        String account = strings[3];
        String password = strings[4];
        Sardine sardine = new OkHttpSardine();
        sardine.setCredentials(account, password);
        //ZipUtil.pack(new File(backupPath),new File(backupZipPath));
        try {

            sardine.createDirectory(davurl + "BackUp");
            sardine.put(davurl + "BackUp/" + backupName, new File(backupFilePath), "application/x-www-form-urlencoded");
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
