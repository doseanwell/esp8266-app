package dw.wifi.dw_wifi_app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class Esp8266Connect extends Thread{
	static String ServerIp = "192.168.4.1";
	static int ServerPort = 80;
	static Socket socket = null;
	static PrintWriter writer = null;
	static BufferedReader reader = null;
	static String register_cmd;
	Thread thread;
	static int count = 0;
	static String line;
	static  boolean connect_flags = false;
	static Context WsActivity;
    @Override  
    public void run()  
    {  
    	try {
			socket = new Socket(ServerIp, ServerPort);
			if(socket == null){
				System.out.println("连接失败.");
				connect_flags = false;
			}
			else
			{
				//writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				writer = new PrintWriter(socket.getOutputStream(), true);
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				connect_flags = true;
			}		
		} catch (IOException e) {
			System.out.println("连接失败.");
		} 
    	try {
			Thread.sleep(200);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
    	while(connect_flags)
    	{
    		if(!connect_flags)
    		{
    			//System.out.println("退出连接wifi模组进程.");
    			break;
    		}
    		try {
				line = reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
//    		if(line.equals("Led open OK")){
//    			mHandler.sendEmptyMessage(1);
//    		}
//    		 if(line.equals("Led close OK")){
//    			mHandler.sendEmptyMessage(2);
//    		}
    		mHandler.sendEmptyMessage(1);
    	}
    } 
    @SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
	        // 重写handleMessage()方法，此方法在UI线程运行
	        @Override
	        public void handleMessage(Message msg) {
	            switch (msg.what) {
	            case 1:
	            		Toast.makeText(WsActivity, line, Toast.LENGTH_SHORT).show();
	            	break;
	            case 2:
            			Toast.makeText(WsActivity, line, Toast.LENGTH_SHORT).show();
            		break;	
	            }
	        }
	    };
    public void recive_para(Context acti) {
    	WsActivity = acti;	
	}
	public void unconnect() {
		try {
			if(connect_flags == true){
				writer.close();
				reader.close();
				socket.close();
				connect_flags = false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public void wifi_config(String cmd) {
		if(connect_flags == true){
		
			writer.print(cmd);
			writer.flush();
			System.out.println(cmd);
		}
	}
	public void Led_On() {
		if(connect_flags == true){
			writer.print("$wifi$/D01/1\r\n");
			writer.flush();
		}
	}
	public void Led_Off() {
		if(connect_flags == true){
			writer.print("$wifi$/D01/0\r\n");
			writer.flush();
		}
	}
	public void Beep_On() {
		if(connect_flags == true){
			writer.print("$wifi$/D02/1\r\n");
			writer.flush();
		}
	}
	public void Beep_Off() {
		if(connect_flags == true){
			writer.print("$wifi$/D02/0\r\n");
			writer.flush();
		}
	}
	public boolean connect_state() {
			return connect_flags;			
	}
	public BufferedReader get_reader()
    {
		return reader;
    }
}
