package com.qw.freemusic.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.qw.freemusic.R;
import com.qw.freemusic.foundation.BaseActivity;
import com.qw.freemusic.utils.PreferencesUtility;

/**
 * created by QY
 * description:
 */
public class LoginActivity extends BaseActivity {

    private EditText userEd, passwordEd;
    private Button loginBt;

    @Override
    protected String TAG() {
        return "LoginActivity";
    }

    @Override
    protected int getViewId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        if (verPermissions()) {
            setView();
        }
    }

    private void login() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private boolean verPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent lIntent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                lIntent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(lIntent, 1024);
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    private void setView() {
        if (PreferencesUtility.getInstance().isLogin()) {
            login();
            return;
        }
        userEd = findViewById(R.id.login_input_username);
        passwordEd = findViewById(R.id.login_input_password);
        loginBt = findViewById(R.id.login_btn);
        loginBt.setOnClickListener(pView ->{
            if (userEd.getText().toString().equals("admin") && passwordEd.getText().toString().equals("1223456")) {
                login();
                PreferencesUtility.getInstance().setLogin(true);
            } else {
                showToastL("请检查用户名密码");
                userEd.setText("");
                passwordEd.setText("");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1024 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            setView();
        }  else {
            finish();
        }
    }
}
