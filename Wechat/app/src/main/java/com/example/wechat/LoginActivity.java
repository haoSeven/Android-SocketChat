package com.example.wechat;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

/**
 *  登录
 */

public class LoginActivity extends Activity implements OnClickListener{

    private String loginName;
    private String password;

    /*   定义控件   */
    private EditText etLoginName;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnRegister;
    private CheckBox btnRemember;
    private SharedPreferences sharedPreferences;
    private MyOpenHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login); 
        initView();

    }

    private void initView(){
        etLoginName = (EditText) findViewById(R.id.login_name);
        etPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.login);
        btnRegister = (Button) findViewById(R.id.register);
        btnRemember = (CheckBox) findViewById(R.id.remember);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        dbHelper = new MyOpenHelper(LoginActivity.this, "wechat.db", null, 1,0);
        sharedPreferences = getSharedPreferences("Data", Context.MODE_PRIVATE);
		boolean isRemember = sharedPreferences.getBoolean("remember_password", false);
		if (isRemember) {
			//将账号和密码都设置到文本框中
			String account = sharedPreferences.getString("account", "");
			String password = sharedPreferences.getString("password", "");
			etLoginName.setText(account);
			etPassword.setText(password);
			btnRemember.setChecked(true);//设置成选中状态
		}
    }

  
   
    public void onClick(View v) {
    	loginName=etLoginName.getText().toString().trim();
    	password=etPassword.getText().toString().trim();
    	  final SQLiteDatabase db = dbHelper.getReadableDatabase();
        switch (v.getId()){
        case R.id.login:
			Cursor cursor=db.rawQuery(
					"select pwd from userTable where name=?",
					new String[] { loginName }, null);
			if (cursor.moveToFirst()) {
				Log.i("test", cursor.getString(cursor.getColumnIndex("pwd")));
				if (password.equals(cursor.getString(cursor.getColumnIndex("pwd")))) {
					Editor editor=sharedPreferences.edit();	
    		        editor.putBoolean("remember_password", true);
    				editor.putString("account", loginName);
    			//判断多选框是否被选中，如果选中存入数据，"remember_password", true
    				if (btnRemember.isChecked()) {
    				
    					editor.putString("password", password);				
    					}
    				else {
    					editor.clear();
    					}
    				editor.commit();
    				Intent a = new Intent(LoginActivity.this, MainActivity.class);
    				startActivity(a);
    				finish();
				}
				else {
					Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_LONG).show();
					}
				
			}else{
				Toast.makeText(LoginActivity.this, "用户名不存在", Toast.LENGTH_LONG).show();
				}	
        	break;
        case R.id.register:
        	if (loginName.equals("") || password.equals("")) {
        		Toast.makeText(LoginActivity.this, "请输入用户名/密码", Toast.LENGTH_LONG).show();
        	}
        	else {
				Cursor b = db.rawQuery(
						"select * from userTable where name=?",
						new String[] { loginName }, null);
				if (b.getCount() > 0)
				{
					Toast.makeText(LoginActivity.this, "用户名已经被注册", Toast.LENGTH_LONG).show();
				}
				else 
				{
					ContentValues values = new ContentValues();
					// ��ʼ������
					values.put("name", etLoginName.getText().toString());
					values.put("pwd", etPassword.getText().toString());
					db.insert("userTable", null, values);
					values.clear();
					Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_LONG).show();
				}
			}
        	break;
        }
    }


 
}
