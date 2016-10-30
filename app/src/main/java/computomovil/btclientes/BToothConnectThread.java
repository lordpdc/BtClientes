package computomovil.btclientes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.widget.Toast;

public class BToothConnectThread {

	private final BluetoothSocket socket;
	private final BluetoothDevice deviceSrv;
	private BluetoothAdapter mBluetoothAdapter;
	
	private InputStream in;
	private OutputStream out;
	private Activity act;
	
	private static final UUID MY_UUID = UUID.fromString("04c6093b-0000-1000-8000-00805f9b34fb");
	
	public BToothConnectThread(Activity act, BluetoothDevice server){
		BluetoothSocket tmp =  null;
		deviceSrv = server;
		this.act = act;
		try{
			tmp = deviceSrv.createRfcommSocketToServiceRecord(MY_UUID);
			
		}catch(IOException exp){
			
		}
		socket = tmp;
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	}
	
	public void connectDevice(){
		mBluetoothAdapter.cancelDiscovery();
		try{
			socket.connect();
			out = socket.getOutputStream();
			in = socket.getInputStream();
		}catch(IOException connectException){
			try{
				socket.close();
			}catch(IOException exp){}
			return;
		}
		
	}
	
	public void pgDown(){
		try {
			out.write(1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void pgUp(){
		try {
			out.write(2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void sendFile(){

	}
	
	
}
