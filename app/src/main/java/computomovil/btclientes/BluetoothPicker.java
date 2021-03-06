package computomovil.btclientes;


import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by raoman on 31/10/2016.
 */

public class BluetoothPicker extends Activity {
    private String mLaunchPackage;
    private String mLaunchClass;

    public static final String EXTRA_NEED_AUTH = "android.bluetooth.devicepicker.extra.NEED_AUTH";
    public static final String EXTRA_FILTER_TYPE = "android.bluetooth.devicepicker.extra.FILTER_TYPE";
    public static final String EXTRA_LAUNCH_PACKAGE = "android.bluetooth.devicepicker.extra.LAUNCH_PACKAGE";
    public static final String EXTRA_LAUNCH_CLASS = "android.bluetooth.devicepicker.extra.DEVICE_PICKER_LAUNCH_CLASS";

    public static final String ACTION_DEVICE_SELECTED = "android.bluetooth.devicepicker.action.DEVICE_SELECTED";
    public static final String ACTION_LAUNCH = "android.bluetooth.devicepicker.action.LAUNCH";

    /** Ask device picker to show all kinds of BT devices */
    public static final int FILTER_TYPE_ALL = 0;
    /** Ask device picker to show BT devices that support AUDIO profiles */
    public static final int FILTER_TYPE_AUDIO = 1;
    /** Ask device picker to show BT devices that support Object Transfer */
    public static final int FILTER_TYPE_TRANSFER = 2;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        DeviceContain dc=DeviceContain.getInstance();


        BluetoothDevice device = dc.getDevice();
        if (device == null) {
            finish();
            return;
        }

        Intent intent = getIntent();
        //mNeedAuth = intent.getBooleanExtra(EXTRA_NEED_AUTH, false);
        //setFilter(intent.getIntExtra(EXTRA_FILTER_TYPE, FILTER_TYPE_ALL));
        mLaunchPackage = intent.getStringExtra(EXTRA_LAUNCH_PACKAGE);
        mLaunchClass = intent.getStringExtra(EXTRA_LAUNCH_CLASS);

        sendDevicePickedIntent(device);

        finish();
    }
    private void sendDevicePickedIntent(BluetoothDevice device) {
        Intent intent = new Intent(ACTION_DEVICE_SELECTED);
        intent.putExtra(BluetoothDevice.EXTRA_DEVICE, device);

        if (mLaunchPackage != null && mLaunchClass != null) {
            intent.setClassName(mLaunchPackage, mLaunchClass);
        }

        sendBroadcast(intent);
    }



}

