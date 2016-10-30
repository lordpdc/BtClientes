package computomovil.btclientes;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class RemoteBluetooth extends Activity {
	
	private BToothConnectThread thread1;

	private static final int REQUEST_ENABLE_BT = 2;	
	private static final int REQUEST_DEVICE_TO_CONNECT = 1;
    private static final int REQUEST_SEND_FILE = 3;
    private static final int REQUEST_PREVIUS_PAGE = 4;
    private static final int REQUEST_NEXT_PAGE = 5;



    private BluetoothAdapter mBluetoothAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!mBluetoothAdapter.isEnabled()) {
            Toast.makeText(this, "Bluetooth is not running", Toast.LENGTH_SHORT).show();
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;

    }

    public void btnClickHandler(View view){
        Intent intent = new Intent(this,ListDispActivity.class);
        startActivityForResult(intent,REQUEST_DEVICE_TO_CONNECT);
    }

    public void pgDownClickListener(View view){
        thread1.pgDown();
    }

    public void pgUpClickListener(View view){
        thread1.pgUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent;
        switch(item.getItemId()){
            case R.id.buscar_disp:
                Toast.makeText(this,"Menu item 1 seleccionado", Toast.LENGTH_LONG)
                        .show();
                intent = new Intent(this,ListDispActivity.class);
                startActivityForResult(intent,REQUEST_DEVICE_TO_CONNECT);
                break;
            default:
                break;
        }
        return true;
    }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_DEVICE_TO_CONNECT:
                // When ListDispActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    // Get the device MAC address
                    String address = data.getExtras()
                            .getString(ListDispActivity.EXTRA_DEVICE_ADDRESS);
                    // Get the BLuetoothDevice object
                    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                    TextView txt = (TextView)findViewById(R.id.dispositivo);
                    txt.setText(device.getName());

                    // Thread used to manage connection.
                    thread1 = new BToothConnectThread(this,device);
                    thread1.connectDevice();
                }
                break;
        }
    }

}