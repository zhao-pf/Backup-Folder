package com.zhaopf.backupfolder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;

import moe.shizuku.preference.Preference;
import moe.shizuku.preference.PreferenceFragment;

import static android.content.Context.MODE_PRIVATE;

public class SettingsFragment extends PreferenceFragment{
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        getPreferenceManager().setDefaultPackages(new String[]{BuildConfig.APPLICATION_ID + "."});
        getPreferenceManager().setSharedPreferencesName("settings");
        getPreferenceManager().setSharedPreferencesMode(MODE_PRIVATE);
        setPreferencesFromResource(R.xml.app_setting, null);

//        Preference ab_id = findPreference("url");
//        ab_id.setOnPreferenceClickListener(this);
//
//        Preference ab_code = findPreference("github");
//        ab_code.setOnPreferenceClickListener(this);

//        Preference switch_backup = findPreference("backup");
//        switch_backup.setOnPreferenceClickListener(this);

    }

//    @Override
//    public boolean onPreferenceClick(Preference preference) {
//        switch (preference.getKey()){
//            case "url":
//                Intent intent_id = new Intent(Intent.ACTION_VIEW,
//                        Uri.parse("http://www.coolapk.com/u/522069"));
//                startActivity(intent_id);
//                break;
//            case "github":
//                Intent intent_code = new Intent(Intent.ACTION_VIEW,
//                        Uri.parse("https://github.com"));
//                startActivity(intent_code);
//                break;
//            default:
//                break;
//        }
//        return false;
//    }

}
