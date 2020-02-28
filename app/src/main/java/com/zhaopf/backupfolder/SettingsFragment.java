package com.zhaopf.backupfolder;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import moe.shizuku.preference.EditTextPreference;
import moe.shizuku.preference.PreferenceFragment;

import static android.content.Context.MODE_PRIVATE;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener, com.zhaopf.backupfolder.listener.VerifyAccount {
    private EditTextPreference et_password, et_account, web_dav_url;


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        getPreferenceManager().setDefaultPackages(new String[]{BuildConfig.APPLICATION_ID + "."});
        getPreferenceManager().setSharedPreferencesName("settings");
        getPreferenceManager().setSharedPreferencesMode(MODE_PRIVATE);
        setPreferencesFromResource(R.xml.app_setting, null);
        initView();
        SharedPreferences sp = getContext().getSharedPreferences("settings", MODE_PRIVATE);
        if (sp.getString("web_dav_url", "") == "") {
            web_dav_url.setSummary(sp.getString("web_dav_url", "https://dav.jianguoyun.com/dav/"));
        }
        if (sp.getString("web_dav_account", "") != "") {
            et_account.setSummary(sp.getString("web_dav_account", "请输入账号"));
        }
        if (sp.getString("web_dav_password", "") != "") {
            et_password.setSummary("********");
        }
    }

    //初始化控件
    private void initView() {
        web_dav_url = (EditTextPreference) findPreference("web_dav_url");
        et_account = (EditTextPreference) findPreference("web_dav_account");
        et_password = (EditTextPreference) findPreference("web_dav_password");

    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        SharedPreferences sp = getContext().getSharedPreferences("settings", MODE_PRIVATE);
        switch (key) {
            case "web_dav_url":
                web_dav_url.setSummary(sp.getString("web_dav_url", "https://dav.jianguoyun.com/"));
                break;
            case "web_dav_account":
                if (sp.getString("web_dav_password", "") != "") {
                    new com.zhaopf.backupfolder.verifyAccount(this).execute(et_account.getText(), et_password.getText());
                } else {
                    Toast.makeText(getActivity(), "请输入密码", Toast.LENGTH_SHORT).show();
                }
                if (et_account.getSummary() != "") {
                    et_account.setSummary(sp.getString("web_dav_account", ""));
                } else {
                    et_account.setSummary("输入密码");
                }
                break;
            case "web_dav_password":
                if (sp.getString("web_dav_account", "") != "") {
                    new com.zhaopf.backupfolder.verifyAccount(this).execute(et_account.getText(), et_password.getText());
                } else {
                    Toast.makeText(getActivity(), "请输入账号", Toast.LENGTH_SHORT).show();
                }
                if (et_password.getText() != "") {
                    et_password.setSummary("********");
                } else {
                    et_password.setSummary("输入密码");
                }
                break;
            default:
                break;
        }


    }

    @Override
    public void upload(boolean b) {
        Toast.makeText(getActivity(), b ? "登录成功！" : "登录失败！请检查账号密码", Toast.LENGTH_SHORT).show();
    }
}
