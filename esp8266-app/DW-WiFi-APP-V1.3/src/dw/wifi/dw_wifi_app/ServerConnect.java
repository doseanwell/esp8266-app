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

public class ServerConnect {
	static String ServerIp = "182.92.189.151";
	static int ServerPort = 2020;
	static Socket socket = null;
	static PrintWriter writer = null;
	static BufferedReader reader = null;
	static  boolean connect_flags = false;
	static String register_cmd;
	static Context WsActivity;
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
						try {
							Thread.sleep(200);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						writer.print(register_cmd);
						writer.flush();
						try {
							String line;
							while ((line = reader.readLine())!= null) {
								publishProgress(line);
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
	public static void user_register(String cmd, final Context tActivity)
    {
		register_cmd = cmd;
		WsActivity = tActivity;
		System.out.println(register_cmd);
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
