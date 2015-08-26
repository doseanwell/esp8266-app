package dw.wifi.dw_wifi_app;

import java.io.BufferedReader;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class WiFiConfigActivity extends Activity {
	private Button btnWifiCfgStart;
	private EditText editTPassword, editTSSID;
	private EditText editTSN, editTPort;
	private CheckBox checkWifi;
	private SharedPreferences sp;
	private String userSSID, passwordValue, portValue, snValue;  
	Esp8266Connect cfgThread = null;
	static BufferedReader reader = null;
	static String line;
	@SuppressLint("WorldReadableFiles")
	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wificfg);
		
		cfgThread = new Esp8266Connect();
		cfgThread.recive_para(WiFiConfigActivity.this);
		cfgThread.start();
			
		editTPassword = (EditText) findViewById(R.id.editTPassword);
		editTSSID = (EditText) findViewById(R.id.editTSSID);
		editTSN = (EditText) findViewById(R.id.editTSN);
		editTPort = (EditText) findViewById(R.id.editTPort);
		
		checkWifi = (CheckBox) findViewById(R.id.checkWifi);
		btnWifiCfgStart = (Button) findViewById(R.id.btnWifiCfgStart);
		btnWifiCfgStart.setOnClickListener(new ButtonClickEvent());
		
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(cfgThread.connect_state()){
			btnWifiCfgStart.setEnabled(true);
			Toast.makeText(WiFiConfigActivity.this,"����wifi�ɹ�", Toast.LENGTH_SHORT).show(); 
		}
		else{
			btnWifiCfgStart.setEnabled(false);
			Toast.makeText(WiFiConfigActivity.this,"����ʧ�� �����³���", Toast.LENGTH_SHORT).show(); 
		}
			
		sp = this.getSharedPreferences("wifiinfo", Context.MODE_WORLD_READABLE);
		
		//�ж��Զ���½��ѡ��״̬  
        if(sp.getBoolean("ISCHECK", false))  
        { 
        	//����Ĭ���Ǽ�¼����״̬  
        	checkWifi.setChecked(true); 
        	editTPassword.setText(sp.getString("password", ""));  
        	editTSSID.setText(sp.getString("SSID", "")); 
        	editTSN.setText(sp.getString("SN_NUM", "")); 
        	editTPort.setText(sp.getString("port", "")); 
        }
        
//        reader = cfgThread.get_reader();
//		Thread th = new Thread(new Runnable() { 
//
//            @Override 
//            public void run() { 
//            	while(true){
//            		if(cfgThread.connect_state()){
//						try {
//							line = reader.readLine();
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//						mHandler.sendEmptyMessage(1);
//					}
//					else{
//						break;
//					}
//            	}
//            } 
//        }); 
//        th.start(); 
	}
//	@SuppressLint("HandlerLeak")
//	private Handler mHandler = new Handler() {
//	        // ��дhandleMessage()�������˷�����UI�߳�����
//	        @Override
//	        public void handleMessage(Message msg) {
//	            switch (msg.what) {
//	            case 1:
//	            		Toast.makeText(WiFiConfigActivity.this,line, Toast.LENGTH_SHORT).show();
//	            }
//	        }
//	    };
	class ButtonClickEvent implements View.OnClickListener {
		public void onClick(View v) {

			if (v == btnWifiCfgStart) {
				userSSID = editTSSID.getText().toString();  
				passwordValue = editTPassword.getText().toString();
				portValue = editTPort.getText().toString(); 
				snValue = editTSN.getText().toString();
                if(checkWifi.isChecked())  //��¼�ɹ��ͼ�ס�����Ϊѡ��״̬�ű����û���Ϣ
                {  
                 //��ס�û��������롢  
                  Editor editor = sp.edit();  
                  editor.putString("SSID", userSSID); 
                  editor.putString("password", passwordValue);
                  editor.putString("SN_NUM", snValue);
                  editor.putString("port", portValue);
                  editor.putBoolean("ISCHECK", true).commit(); 
                  editor.commit();  
                } 
                if(cfgThread.connect_state()){
                	cfgThread.wifi_config("$wifi$/WIC/1/" + editTSSID.getText().toString() + 
						"&" + editTPassword.getText().toString() + "&182.092.189.151&"+ 
						editTPort.getText().toString() + "&" + editTSN.getText().toString() + "&0\r\n");
                	
                	try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
                	
                	Toast.makeText(WiFiConfigActivity.this,"������� �뷵�ص�½", Toast.LENGTH_SHORT).show(); 
                }
			}
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(cfgThread.connect_state()){
			cfgThread.unconnect();
		}
	}
}
