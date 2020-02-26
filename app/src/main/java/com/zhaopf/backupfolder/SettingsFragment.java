package com.zhaopf.backupfolder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import moe.shizuku.preference.EditTextPreference;
import moe.shizuku.preference.Preference;
import moe.shizuku.preference.PreferenceFragment;
import moe.shizuku.preference.PreferenceManager;

import static android.content.Context.MODE_PRIVATE;

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener,SharedPreferences.OnSharedPreferenceChangeListener,com.zhaopf.backupfolder.listener.VerifyAccount {
    private EditTextPreference et_password,et_account;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        getPreferenceManager().setDefaultPackages(new String[]{BuildConfig.APPLICATION_ID + "."});
        getPreferenceManager().setSharedPreferencesName("settings");
        getPreferenceManager().setSharedPreferencesMode(MODE_PRIVATE);
        setPreferencesFromResource(R.xml.app_setting, null);
        Preference pr_account = findPreference("web_dav_account");
        pr_account.setOnPreferenceClickListener(this);
        Preference ab_password = findPreference("web_dav_password");
        ab_password.setOnPreferenceClickListener(this);
        et_account = (EditTextPreference) findPreference("web_dav_account");
        et_password = (EditTextPreference) findPreference("web_dav_password");

        //sharedPref.registerOnSharedPreferenceChangeListener(listener);
//        Preference switch_backup = findPreference("backup");
//        switch_backup.setOnPreferenceClickListener(this);

    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case "web_dav_url":

                break;
            case "web_dav_account":
                break;
            default:
                break;
        }
        return false;
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
        switch (key) {
            case "web_dav_account":
                if (et_password.getText()!=""){
                    new com.zhaopf.backupfolder.verifyAccount(this).execute(et_account.getText(), et_password.getText());
                }else{
                    Toast.makeText(getActivity(), "请输入密码", Toast.LENGTH_SHORT).show();
                }
//                Toast.makeText(getActivity(), "您输入的内容为" + editTextPreference.getText(), Toast.LENGTH_SHORT).show();
                break;
            case "web_dav_password":
                if (et_account.getText()!=""){
                    new com.zhaopf.backupfolder.verifyAccount(this).execute(et_account.getText(), et_password.getText());
                }else{
                    Toast.makeText(getActivity(), "请输入账号", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void upload(boolean b) {
        Toast.makeText(getActivity(), b?"登录成功！":"登录失败！请检查账号密码", Toast.LENGTH_SHORT).show();
    }
}
