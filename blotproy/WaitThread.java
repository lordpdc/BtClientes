package bserver;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

public class WaitThread implements Runnable{

	private InputStream in;
	private OutputStream out;
	
	/** Constructor */
	public WaitThread() {
	}

	@Override
	public void run() {
		waitForConnection();		
	}

	/** Waiting for connection from devices */
	private void waitForConnection() {
		// retrieve the local Bluetooth device object
		LocalDevice local = null;

		StreamConnectionNotifier notifier;
		StreamConnection connection = null;

		// setup the server to listen for connection
		try {
			local = LocalDevice.getLocalDevice();
			local.setDiscoverable(DiscoveryAgent.GIAC);

			UUID uuid = new UUID("04c6093b00001000800000805f9b34fb", false);
			System.out.println(uuid.toString());

            String url = "btspp://localhost:" + uuid.toString() + ";name=RemoteBluetooth";
            notifier = (StreamConnectionNotifier)Connector.open(url);
        } catch (BluetoothStateException e) {
        	System.out.println("Bluetooth is not turned on.");
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		int cmd;
		// waiting for connection
		while(true){
			try {
				
                    System.out.println("waiting for connection...");
	            connection = notifier.acceptAndOpen();
	            System.out.println("Connected to: "+ connection);
	            in = connection.openDataInputStream();
	            out = connection.openDataOutputStream();
	            
			/*__________________________________________________________
			
			
			FileOutputStream fos = new FileOutputStream("/test.txt");
                    
                    	byte[] buffer = new byte[8192];
	            	int bytesRead = in.read(buffer, 0, buffer.length);
                    	int current = bytesRead;
                    
                    	do {
                        	bytesRead = in.read(buffer, current,
                        	buffer.length - current);

                            if (bytesRead >= 0)
                        	current += bytesRead;
                    	} while (bytesRead > -1);
                    	fos.write(buffer);
                    	fos.flush();
                    	fos.close();
			
			
			_____________________________________________________________*/
				
	            while(true)
	            {
	            	cmd = in.read();
	            	if(cmd == -1) break;
	            	procesaCmd(cmd);
	            }
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
	}
	private void procesaCmd(int cmd){
		System.out.println("Comando "+cmd);
		Robot robot = null;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(cmd == 1) robot.keyPress(KeyEvent.VK_PAGE_UP);
			
		if(cmd == 2) robot.keyPress(KeyEvent.VK_PAGE_DOWN);;
		if(cmd == 3){
			//llamar al metodo
		}
	}
	
	
}
