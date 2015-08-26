package dw.wifi.dw_wifi_app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class LoginActivity extends Activity {

	private EditText username, SNnumber, userkey;
	private CheckBox chcInfo;
	private Button btnEnter;
	private Button btnWifiCfg;
	private Button btnFastTest;
	private SharedPreferences sp;
	
	private String userNameValue, passwordValue, userKeyValue;  
	@SuppressWarnings("deprecation")
	@SuppressLint("WorldReadableFiles")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		username = (EditText) findViewById(R.id.username);
		userkey = (EditText) findViewById(R.id.userkey);
		SNnumber = (EditText) findViewById(R.id.SNnumber);
		chcInfo = (CheckBox) findViewById(R.id.chcInfo);
		btnEnter = (Button) findViewById(R.id.btnEnter);
		btnWifiCfg = (Button) findViewById(R.id.btnWifiCfg);
		btnFastTest = (Button) findViewById(R.id.btnFastTest);	
		
		btnEnter.setOnClickListener(new ButtonClickEvent());
		btnWifiCfg.setOnClickListener(new ButtonClickEvent());
		btnFastTest.setOnClickListener(new ButtonClickEvent());
		
		sp = this.getSharedPreferences("userinfo", Context.MODE_WORLD_READABLE);
		
		//判断自动登陆多选框状态  
        if(sp.getBoolean("ISCHECK", false))  
        { 
        	//设置默认是记录密码状态  
        	chcInfo.setChecked(true); 
        	username.setText(sp.getString("USER_NAME", ""));  
        	userkey.setText(sp.getString("KEY", "")); 
        	SNnumber.setText(sp.getString("SN_NUM", "")); 
        }
	}

	// ----------------------------------------------------清除按钮、发送按钮
	class ButtonClickEvent implements View.OnClickListener {
		public void onClick(View v) {

			if (v == btnEnter) {
				userNameValue = username.getText().toString();  
				userKeyValue = userkey.getText().toString();
                passwordValue = SNnumber.getText().toString(); 
//                Toast.makeText(LoginActivity.this,"登录成功", Toast.LENGTH_SHORT).show();  
                if(chcInfo.isChecked())  //登录成功和记住密码框为选中状态才保存用户信息
                {  
                 //记住用户名、密码、  
                  Editor editor = sp.edit();  
                  editor.putString("USER_NAME", userNameValue); 
                  editor.putString("KEY", userKeyValue);
                  editor.putString("SN_NUM", passwordValue);
                  editor.putBoolean("ISCHECK", true).commit(); 
                  editor.commit();  
                } 
                
                Intent intent = new Intent(LoginActivity.this,WifiServerActivity.class);  
                Bundle bundle = new Bundle();
                bundle.putString("reg_cmd", "$wifi$/UIN&" + username.getText().toString() + "&" + 
                				userkey.getText().toString() + "&" + 
                				SNnumber.getText().toString() + "\r\n");
                intent.putExtras(bundle);
                startActivity(intent);
			}
			else if(v == btnWifiCfg){
				Intent intent = new Intent(LoginActivity.this,WiFiConfigActivity.class);  
                startActivity(intent);				
			}
			else if(v == btnFastTest){
				Intent intent = new Intent(LoginActivity.this,WIfiFastTestActivity.class);  
                startActivity(intent);				
			}
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
//		System.exit(0);
	}
}
