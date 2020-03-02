package com.zhaopf.backupfolder;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Objects;

import moe.shizuku.preference.EditTextPreference;
import moe.shizuku.preference.Preference;
import moe.shizuku.preference.PreferenceFragment;
import moe.shizuku.preference.SwitchPreference;

import static android.content.Context.MODE_PRIVATE;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener, com.zhaopf.backupfolder.listener.VerifyAccount, Preference.OnPreferenceClickListener {
    private EditTextPreference et_password, et_account, web_dav_url;

    private SwitchPreference sw;
    private Preference url;
    private Preference more;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        getPreferenceManager().setDefaultPackages(new String[]{BuildConfig.APPLICATION_ID + "."});
        getPreferenceManager().setSharedPreferencesName("settings");
        getPreferenceManager().setSharedPreferencesMode(MODE_PRIVATE);
        setPreferencesFromResource(R.xml.app_setting, null);
        initView();
        SharedPreferences sp = Objects.requireNonNull(getContext()).getSharedPreferences("settings", MODE_PRIVATE);

        web_dav_url.setSummary(sp.getString("web_dav_url", getString(R.string.default_webdav_url)));
        if (sp.getString("web_dav_account", "") != "") {
            et_account.setSummary(sp.getString("web_dav_account", String.valueOf(R.string.please_enter_account)));
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
        sw = (SwitchPreference) findPreference("switch_backup");
        url = findPreference("url");
        more = findPreference("more");
        sw.setOnPreferenceClickListener(this);
        url.setOnPreferenceClickListener(this);
        more.setOnPreferenceClickListener(this);
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
        SharedPreferences sp = Objects.requireNonNull(getContext()).getSharedPreferences("settings", MODE_PRIVATE);
        String webDavUrl = getContext().getSharedPreferences("settings", MODE_PRIVATE).getString("web_dav_url", "");
        String account = sp.getString("web_dav_account", "");
        String password = sp.getString("web_dav_password", "");
        switch (key) {
            case "web_dav_url":
                web_dav_url.setSummary(sp.getString("web_dav_url", String.valueOf(R.string.default_webdav_url)));
                break;
            case "web_dav_account":
                if (sp.getString("web_dav_password", "") != "") {
                    new com.zhaopf.backupfolder.verifyAccount(this).execute(webDavUrl, et_account.getText(), et_password.getText());
                } else {
                    Toast.makeText(getActivity(), R.string.please_enter_password, Toast.LENGTH_SHORT).show();
                }
                if (account.length() != 0) {
                    et_account.setSummary(sp.getString("web_dav_account", ""));
                } else {
                    et_account.setSummary(getString(R.string.please_enter_account));
                }
                break;
            case "web_dav_password":
                if (sp.getString("web_dav_account", "") != "") {
                    new com.zhaopf.backupfolder.verifyAccount(this).execute(webDavUrl, et_account.getText(), et_password.getText());
                } else {
                    Toast.makeText(getActivity(), R.string.please_enter_account, Toast.LENGTH_SHORT).show();
                }
                if (password.length() != 0) {
                    et_password.setSummary("********");
                } else {
                    et_password.setSummary(getString(R.string.please_enter_password));
                }
                break;
            default:
                break;
        }


    }

    @Override
    public void verify(boolean b) {
        Toast.makeText(getActivity(), b ? getString(R.string.login_success) : getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        switch (preference.getKey()) {
            case "switch_backup":
                if (sw.isChecked()) {
                    dialog.setTitle("提示");
                    dialog.setMessage(R.string.autosync_dialog_message);
                    dialog.setNegativeButton("了解", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dialog.show();
                }
                break;
            case "url":
                dialog.setTitle("提示");
                dialog.setMessage(R.string.my_messages);
                dialog.setNegativeButton("了解", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clipData= ClipData.newPlainText(null,"https://github.com/zhao-pf");
                        clipboardManager.setPrimaryClip(clipData);
                        Toast.makeText(getContext(), "github链接已复制到剪切板", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
                break;
            case "more":
                dialog.setTitle("我说的话");
                dialog.setMessage(R.string.my_words);
                dialog.setNegativeButton("了解", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog.show();
                break;
        }
        return false;
    }
}
