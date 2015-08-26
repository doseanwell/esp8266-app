package dw.wifi.dw_wifi_app;

import java.io.BufferedReader;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class WifiServerActivity extends Activity {
	private ImageView imageLed;
	private ToggleButton toggleLedBtn, toggleBeepBtn;
	HeartBeatThread myThread = null;
	static BufferedReader reader = null;
	static String line;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
				
		myThread = new HeartBeatThread();
		Bundle bundle = this.getIntent().getExtras();
		String cmd = bundle.getString("reg_cmd");
//		System.out.println(cmd);
		myThread.user_register(cmd, WifiServerActivity.this);
		myThread.start();
//		myThread.connect(WifiServerActivity.this);
//		ServerConnect.connect(WifiServerActivity.this);		
//		ServerConnect.user_register(cmd, WifiServerActivity.this);
		
//		imageLed = (ImageView) findViewById(R.id.imageLed);
		toggleLedBtn = (ToggleButton) findViewById(R.id.toggleLedBtn);
		toggleBeepBtn = (ToggleButton) findViewById(R.id.toggleBeepBtn);
		
		toggleLedBtn.setOnCheckedChangeListener(new ToggleButtonCheckedChangeEvent());
		toggleBeepBtn.setOnCheckedChangeListener(new ToggleButtonCheckedChangeEvent());
		if(myThread == null){
			System.exit(0); 
		}
		else{
			Toast.makeText(WifiServerActivity.this,"������������", Toast.LENGTH_SHORT).show(); 
			
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				System.out.println("��ʱ.");
			}
			
//			Toast.makeText(WifiServerActivity.this,"ע��ɹ�", Toast.LENGTH_SHORT).show(); 
			
		}
		reader = myThread.get_reader();
		Thread th = new Thread(new Runnable() { 

            @Override 
            public void run() { 
            	while(true){
            		if(myThread.get_state()){
						try {
							line = reader.readLine();
						} catch (IOException e) {
							e.printStackTrace();
						}
						mHandler.sendEmptyMessage(1);
					}
					else{
						break;
					}
            	}
            } 
        }); 
        th.start(); 
	}
	 @SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
	        // ��дhandleMessage()�������˷�����UI�߳�����
	        @Override
	        public void handleMessage(Message msg) {
	            switch (msg.what) {
	            case 1:
	            	if(line.equals("No find sn.")){
	            		toggleLedBtn.setEnabled(false);
	            		toggleBeepBtn.setEnabled(false);
	            		Toast.makeText(WifiServerActivity.this,"δ���ֵ�SN����", Toast.LENGTH_SHORT).show();
	            	}
	            	if(line.equals("User connect seccess!")){
	            		toggleLedBtn.setEnabled(true);
	            		toggleBeepBtn.setEnabled(true);
	            		Toast.makeText(WifiServerActivity.this,"ע��ɹ�", Toast.LENGTH_SHORT).show();
	            	}
	                break;
	            }
	        }
	    };
	class ToggleButtonCheckedChangeEvent implements
			ToggleButton.OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (buttonView == toggleLedBtn) {
				if (isChecked) {
//					imageLed.setImageResource(R.drawable.bulbon);
					myThread.Led_On();
				} else {
//					imageLed.setImageResource(R.drawable.bulboff);
					myThread.Led_Off();
				}
			}
			else if (buttonView == toggleBeepBtn) {
				if (isChecked) {
					myThread.Beep_On();
				} else {
					myThread.Beep_Off();
				}
			}
		}

	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		myThread.Unconnect();
		Toast.makeText(WifiServerActivity.this,"�������ѶϿ�", Toast.LENGTH_SHORT).show(); 
		System.out.println("�ر�����.");
		  
	}
}
