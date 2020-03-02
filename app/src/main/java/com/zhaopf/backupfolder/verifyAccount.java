package com.zhaopf.backupfolder;

import android.os.AsyncTask;

import com.thegrizzlylabs.sardineandroid.Sardine;
import com.thegrizzlylabs.sardineandroid.impl.OkHttpSardine;

import java.io.IOException;

/**
 * Created by 赵鹏飞 on 2020/2/23 15:21
 */
class verifyAccount extends AsyncTask<String, Void, Boolean> {
    private final com.zhaopf.backupfolder.listener.VerifyAccount verify;

    public verifyAccount(com.zhaopf.backupfolder.listener.VerifyAccount verify) {
        this.verify = verify;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        String davurl = strings[0];
        String account = strings[1];
        String password = strings[2];
        Sardine sardine = new OkHttpSardine();
        sardine.setCredentials(account, password);

        try {
            sardine.createDirectory(davurl + "BackUp");

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean b) {
        super.onPostExecute(b);
        verify.verify(b);
    }
}
