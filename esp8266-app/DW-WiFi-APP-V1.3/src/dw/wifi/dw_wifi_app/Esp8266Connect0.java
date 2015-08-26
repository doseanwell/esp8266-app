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

public class Esp8266Connect0 {
	static String ServerIp = "192.168.4.1";
	static int ServerPort = 80;
	static Socket socket = null;
	static PrintWriter writer = null;
	static BufferedReader reader = null;
	static  boolean connect_flags = false;
	public static void connect(final Context tActivity) {
		AsyncTask<Void, String, Void> read = new AsyncTask<Void, String, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				try {	
//					socket = new Socket(ip.getText().toString(), Integer.decode(port.getText().toString()));
					socket = new Socket(ServerIp, ServerPort);
					if(socket == null){
						connect_flags = false;
					}
					else
					{
						connect_flags = true;
						//writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
						writer = new PrintWriter(socket.getOutputStream(), true);
						reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));	
//						publishProgress("@success");
						try {
							String line;
							while ((line = reader.readLine())!= null) {
								publishProgress(line);
//								publishProgress("@success");
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				} catch (UnknownHostException e1) {
					Toast.makeText(tActivity, "无法建立链接", Toast.LENGTH_SHORT).show();
				} catch (IOException e1) {
					Toast.makeText(tActivity, "无法建立链接", Toast.LENGTH_SHORT).show();
				}
				return null;
			}

			@Override
			protected void onProgressUpdate(String... values) {
				if (values[0].equals("@success")) {
					Toast.makeText(tActivity, "连接wifi成功", Toast.LENGTH_SHORT).show();
				}
				else{
					Toast.makeText(tActivity, values[0], Toast.LENGTH_SHORT).show();
				}
				
				super.onProgressUpdate(values);
			}
		};
		read.execute();		
	}
	public static void unconnect() {
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
	public static void wifi_config(String cmd) {
		if(connect_flags == true){
		
			writer.print(cmd);
			writer.flush();
			System.out.println(cmd);
		}
	}
	public static void Led_On() {
		if(connect_flags == true){
			writer.print("$wifi$/D01/1\r\n");
			writer.flush();
		}
	}
	public static void Led_Off() {
		if(connect_flags == true){
			writer.print("$wifi$/D01/0\r\n");
			writer.flush();
		}
	}
	public static void Beep_On() {
		if(connect_flags == true){
			writer.print("$wifi$/D02/1\r\n");
			writer.flush();
		}
	}
	public static void Beep_Off() {
		if(connect_flags == true){
			writer.print("$wifi$/D02/0\r\n");
			writer.flush();
		}
	}
	public static boolean connect_state() {
		if(connect_flags == true){
			return true;
		}
		else{
			return false;
		}
			
	}
}
