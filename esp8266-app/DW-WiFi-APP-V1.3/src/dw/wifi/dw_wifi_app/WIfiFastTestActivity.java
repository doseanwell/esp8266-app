package dw.wifi.dw_wifi_app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class WIfiFastTestActivity extends Activity {
	private ToggleButton toggleLedBtn, toggleBeepBtn;
	Esp8266Connect cfgThread = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fasttest);
		
		cfgThread = new Esp8266Connect();
		cfgThread.recive_para(WIfiFastTestActivity.this);
		cfgThread.start();
		
		toggleLedBtn = (ToggleButton) findViewById(R.id.toggleLedBtn);
		toggleBeepBtn = (ToggleButton) findViewById(R.id.toggleBeepBtn);
		
		toggleLedBtn.setOnCheckedChangeListener(new ToggleButtonCheckedChangeEvent());
		toggleBeepBtn.setOnCheckedChangeListener(new ToggleButtonCheckedChangeEvent());
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(cfgThread.connect_state()){
			toggleLedBtn.setEnabled(true);
			toggleBeepBtn.setEnabled(true);
			Toast.makeText(WIfiFastTestActivity.this,"连接wifi成功", Toast.LENGTH_SHORT).show(); 
		}
		else{
			toggleLedBtn.setEnabled(false);
			toggleBeepBtn.setEnabled(false);
			Toast.makeText(WIfiFastTestActivity.this,"连接失败 请重新尝试", Toast.LENGTH_SHORT).show(); 
		}
	}
	class ToggleButtonCheckedChangeEvent implements
	ToggleButton.OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (buttonView == toggleLedBtn) {
				if (isChecked) {
					cfgThread.Led_On();
				} else {
					cfgThread.Led_Off();
				}
			}
			else if (buttonView == toggleBeepBtn) {
				if (isChecked) {
					cfgThread.Beep_On();
				} else {
					cfgThread.Beep_Off();
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
