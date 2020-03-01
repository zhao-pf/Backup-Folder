package com.zhaopf.backupfolder;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
/**
 * 设置
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = this;

        setContentView(R.layout.activity_settings);
        if (savedInstanceState == null) {
            SettingsFragment fragment = new SettingsFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_settings, fragment).commit();
        }
    }
}
