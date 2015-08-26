package dw.wifi.dw_wifi_app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class HeartBeatThread extends Thread{
	static String ServerIp = "182.92.189.151";
//	static String ServerIp = "192.168.1.101";
	static int ServerPort = 2020;
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
    	writer.print(register_cmd);
		writer.flush();
    	while(connect_flags)
    	{
    		if(!connect_flags)
    		{
    			//System.out.println("退出服务器连接进程.");
    			break;
    		}
    		if(count >= 100){
    			count = 0;
    			writer.print("Here\r\n");
				writer.flush();
				System.out.println("Send next heartbeat.");
    		}
    		try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    		count++;
    	}
    } 
    public void user_register(String cmd, final Context tActivity)
    {
		register_cmd = cmd;
		WsActivity = tActivity;
		System.out.println(register_cmd);
    }
    public BufferedReader get_reader()
    {
		return reader;
    }
    public boolean get_state()
    {
		return connect_flags;
    }
    public boolean Led_On()
    {
    	if(connect_flags == true){
	    	writer.print("$wifi$/D01/1\r\n");
	    	writer.flush();
	    	return true;
		}
		return false;
    }
    public boolean Led_Off()
    {
    	if(connect_flags == true){
	    	writer.print("$wifi$/D01/0\r\n");
	    	writer.flush();
	    	return true;
    	}
    	return false;
    }
    public boolean Beep_On()
    {
    	if(connect_flags == true){
	    	writer.print("$wifi$/D02/1\r\n");
	    	writer.flush();
	    	return true;
		}
		return false;
    }
    public boolean Beep_Off()
    {
    	if(connect_flags == true){
	    	writer.print("$wifi$/D02/0\r\n");
	    	writer.flush();
	    	return true;
    	}
    	return false;
    }
    public void Unconnect()
    {
    	if(connect_flags == true){
	    	try {
	    		connect_flags = false;
	    		writer.close();
				reader.close();
				socket.close();			
			} catch (IOException e) {
				System.out.println("断开连接失败.");
			}
    	}
    }
}
