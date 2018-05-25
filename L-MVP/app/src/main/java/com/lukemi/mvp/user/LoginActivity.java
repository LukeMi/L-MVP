package com.lukemi.mvp.user;

import android.app.ProgressDialog;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lukemi.mvp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 登录接口
 */
public class LoginActivity extends AppCompatActivity {

    private EditText etAccount;
    private EditText etPwd;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etAccount = ((EditText) findViewById(R.id.et_account));
        etPwd = ((EditText) findViewById(R.id.et_pwd));
        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        RecyclerView d = new RecyclerView(this);
    }

    private void login() {
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("登录中");
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String account = etAccount.getText().toString();
                String pwd = etPwd.getText().toString();
                String uri = "http://192.168.0.117:3030?account=" + account + "&pwd=" + pwd;
                Log.i("ccc:", uri);
                try {
                    URL url = new URL(uri);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    // 设置是否向HttpURLConnection输出
                    conn.setDoOutput(false);
                    // 设置是否从httpUrlConnection读入
                    conn.setDoInput(true);
                    conn.setRequestMethod("GET");
                    conn.connect();
                    final StringBuffer sb = new StringBuffer();
                    int responseCode = conn.getResponseCode();
                    if (responseCode == 200) {
                        InputStream inputStream = conn.getInputStream();
                        BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
                        String line;
                        while ((line = r.readLine()) != null) {
                            sb.append(new String(line));
                        }
                        r.close();
                        inputStream.close();
                    }
                    conn.disconnect();
                    Log.i("CCC:", sb.toString());
                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject object = new JSONObject(sb.toString());
                                if (object.getInt("rsm") == 1) {
                                    closeDlg(true);
                                } else {
                                    closeDlg(false);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();

                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    closeDlg(false);
                }

            }
        }).start();
    }

    private void closeDlg(boolean result) {
        if (result) {
            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
            handler();
        } else {
            Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
            progressDialog.cancel();
        }

    }

    private void handler() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.setMessage("登陆成功");
                progressDialog.cancel();
                finish();
            }
        }, 1500);
    }


}
