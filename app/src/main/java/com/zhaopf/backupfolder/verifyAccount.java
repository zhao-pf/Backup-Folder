package com.zhaopf.backupfolder;

import android.os.AsyncTask;
import android.util.Log;

import com.thegrizzlylabs.sardineandroid.Sardine;
import com.thegrizzlylabs.sardineandroid.impl.OkHttpSardine;

import java.io.File;
import java.io.IOException;

import javax.security.auth.login.LoginException;

/**
 * Created by 赵鹏飞 on 2020/2/23 15:21
 */
public class verifyAccount extends AsyncTask<String,Void,Boolean> {
    private com.zhaopf.backupfolder.listener.UploadBackup uploadBackup;
    String TAG="verifyAccount.java---->";
    public verifyAccount(MainActivity uploadBackup){
        this.uploadBackup = uploadBackup;
    }
    @Override
    protected Boolean doInBackground(String... strings) {
        String account = strings[0];
        String password = strings[1];
        //ZipUtil.pack(new File(backupPath),new File(backupZipPath));
        Sardine sardine = new OkHttpSardine();
        sardine.setCredentials(account, password);
        try {
            sardine.createDirectory("https://dav.jianguoyun.com/dav/" + "BackUp");
            Log.e(TAG, "return true" );
            //noinspection ResultOfMethodCallIgnored
            //new File(backupZipPath).delete();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "return false" );
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        Log.e(TAG, aBoolean.toString() );
    }
}
