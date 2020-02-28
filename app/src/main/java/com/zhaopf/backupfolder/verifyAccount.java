package com.zhaopf.backupfolder;

import android.os.AsyncTask;
import android.util.Log;

import com.thegrizzlylabs.sardineandroid.Sardine;
import com.thegrizzlylabs.sardineandroid.impl.OkHttpSardine;

import java.io.IOException;

/**
 * Created by 赵鹏飞 on 2020/2/23 15:21
 */
public class verifyAccount extends AsyncTask<String, Void, Boolean> {
    String TAG = "verifyAccount.java---->";
    private com.zhaopf.backupfolder.listener.VerifyAccount uploadBackup;

    public verifyAccount(SettingsFragment uploadBackup) {
        this.uploadBackup = uploadBackup;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        String account = strings[0];
        String password = strings[1];
        Sardine sardine = new OkHttpSardine();
        sardine.setCredentials(account, password);
        try {
            sardine.createDirectory("https://dav.jianguoyun.com/dav/" + "BackUp");
            Log.e(TAG, "return true");
            //noinspection ResultOfMethodCallIgnored
            //new File(backupZipPath).delete();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "return false");
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean b) {
        super.onPostExecute(b);
        Log.e(TAG, b.toString());
        uploadBackup.upload(b);
    }
}
