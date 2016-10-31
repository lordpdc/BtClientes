package computomovil.btclientes;

import android.bluetooth.BluetoothDevice;

/**
 * Created by raoman on 31/10/2016.
 */

public class DeviceContain {
    private static BluetoothDevice device;
    private static DeviceContain dc;

    public static DeviceContain getInstance(){
        if(dc==null){
            dc=new DeviceContain();
            System.out.println("instancia archivo de nuevo");
        }
        return dc;
    }

    private void DeviceContain(){

    }

    public void setDevice(BluetoothDevice device){
        this.device=device;
    }
    public BluetoothDevice getDevice(){
        return device;
    }
}
