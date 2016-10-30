package computomovil.btclientes;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ListDispActivity extends Activity {
	
	// Return Intent extra
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    //Codigos para los intents
	private static final int REQUEST_ENABLE_BT = 2;
	//manejador del bluetooth
	private BluetoothAdapter mBluetoothAdapter;
	private ArrayAdapter<String> listDispAdapter;
	ListDispActivity thisAct;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_dispositivos);
        
        setResult(Activity.RESULT_CANCELED);
        
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(mReceiver, filter);
		
		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		registerReceiver(mReceiver, filter);
		
		listDispAdapter = new ArrayAdapter<String>(this,R.layout.nombre_dispositivo);
		ListView listaDispositivos = (ListView) findViewById(R.id.list_disp_emparejados);
		listaDispositivos.setAdapter(listDispAdapter);
		listaDispositivos.setOnItemClickListener(mDispositivosClickListener);
		
		//Adicionar c�digo para que la lista reaccione el click del usuario
		
        
    }
    
	@Override
	protected void onStart() {
		super.onStart();
	
		if (!mBluetoothAdapter.isEnabled()) {
            Toast.makeText(this, "Bluetooth is not running", Toast.LENGTH_SHORT).show();
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
		}		
		thisAct = this;
		
	}
	
	@Override
    protected void onDestroy() {
        super.onDestroy();

        // Make sure we're not doing discovery anymore
        // Unregister broadcast listeners
        this.unregisterReceiver(mReceiver);
    }

    // The on-click listener for all devices in the ListViews
    private OnItemClickListener mDispositivosClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            // Cancel discovery because it's costly and we're about to connect
            mBluetoothAdapter.cancelDiscovery();
            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView)v).getText().toString();
            String address = info.substring(info.length() - 17);
            // Create the result Intent and include the MAC address
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DEVICE_ADDRESS, address);
            // Set result and finish this Activity
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    };	
	
	
	public void buscarBtnClick(View v){

		buscarDisp();
	}
	
	private void buscarDisp(){
		setProgressBarIndeterminateVisibility(true);
		setTitle(R.string.titulo_buscando_disp);
		if (mBluetoothAdapter.isDiscovering()) {
			mBluetoothAdapter.cancelDiscovery();
        }
		mBluetoothAdapter.startDiscovery();
	}

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed already
                    listDispAdapter.add(device.getName() + "|" + device.getAddress());
            // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setProgressBarIndeterminateVisibility(false);
                setTitle(R.string.titulo_app);
                if (listDispAdapter.getCount() == 0) {
                    String noDevices = getResources().getText(R.string.no_encontrado).toString();
                    listDispAdapter.add(noDevices);
                }
            }
        }
    };
}
