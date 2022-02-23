package com.bignerdranch.android.bluetoothapp

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {
    private val REQUEST_CODE_ENABLE_BT: Int =1;
    private val REQUEST_CODE_DISCOVERABLE_BT: Int =1;

    //bluetooth adapter
    private lateinit var bAdapter:BluetoothAdapter
//    private lateinit var context:Context

    private lateinit var bluetoothStatusTextView: TextView
    private lateinit var bluetoothImageView: ImageView
    private lateinit var turnOnButton: Button
    private lateinit var turnOffButton: Button
    private lateinit var discoverableButton: Button
    private lateinit var pairedButton: Button
    private lateinit var pairedDevicesTextView: TextView

//    private val bluetoothAdapter: BluetoothAdapter by lazy {
//        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
//        bluetoothManager.adapter
//    }
//
//    override fun onResume() {
//        super.onResume()
//        if (!bluetoothAdapter.isEnabled) {
//            promptEnableBluetooth()
//        }
//    }
//
//    private fun promptEnableBluetooth() {
//        if (!bluetoothAdapter.isEnabled) {
//            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//            startActivityForResult(enableBtIntent, ENABLE_BLUETOOTH_REQUEST_CODE)
//        }
//    }
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            ENABLE_BLUETOOTH_REQUEST_CODE -> {
//                if (resultCode != Activity.RESULT_OK) {
//                    promptEnableBluetooth()
//                }
//            }
//        }
//    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    bluetoothStatusTextView = findViewById(R.id.bluetoothStatusTextView)
    bluetoothImageView = findViewById(R.id.bluetoothImageView)
    turnOnButton = findViewById(R.id.turnOnButton)
    turnOffButton = findViewById(R.id.turnOffButton)
    discoverableButton = findViewById(R.id.discoverableButton)
    pairedButton = findViewById(R.id.pairedButton)
    pairedDevicesTextView = findViewById(R.id.pairedDevicesTextView)
        //init bluetooth adapter
//        var bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
//        bAdapter=BluetoothAdapter.getDefaultAdapter()
//
//        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
//        if (bluetoothAdapter == null) {
//            // Device doesn't support Bluetooth
//        }
        bAdapter=BluetoothAdapter.getDefaultAdapter()
        if (bAdapter == null) {
            bluetoothStatusTextView.text ="Bluetooth is not available"
        }else{

            bluetoothStatusTextView.text ="Bluetooth is available"
        }

        //set image according to bluetooth status
        if (bAdapter.isEnabled) {
            bluetoothImageView.setImageResource(R.drawable.ic_bluetooth_on)
        }else{
            bluetoothImageView.setImageResource(R.drawable.ic_bluetooth_off)
        }

//        turn on bluetooth
        turnOnButton.setOnClickListener {
            if (bAdapter.isEnabled){
                Toast.makeText(this, "Already on", Toast.LENGTH_LONG).show()
            }else{
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.

                    return@setOnClickListener
                }
                startActivityForResult(intent,REQUEST_CODE_ENABLE_BT)
                // Receiver
//                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//                var getResult =
//                    registerForActivityResult(
//                        ActivityResultContracts.StartActivityForResult()
//                    ) {
//                        if (it.resultCode == Activity.RESULT_OK) {
//                            bluetoothImageView.setImageResource(R.drawable.ic_bluetooth_on)
//                            Toast.makeText(this, "Bluetooth is on", Toast.LENGTH_SHORT).show()
//                        }else{
//
//                            Toast.makeText(this, "Bluetooth is still off", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                //Caller
//                getResult.launch(intent)
            }
        }
        //turn off bluetooth
        turnOffButton.setOnClickListener {
            if (!bAdapter.isEnabled){
                //bluetooth is already off
                Toast.makeText(this, "Already off", Toast.LENGTH_LONG).show()
            }else{
                //turn off bluetooth
                bAdapter.disable()
                bluetoothImageView.setImageResource(R.drawable.ic_bluetooth_off)
                Toast.makeText(this, "Bluetooth turned off", Toast.LENGTH_LONG).show()
            }

//            if (!bAdapter.isEnabled){
//                Toast.makeText(this, "Already off", Toast.LENGTH_SHORT).show()
//            }else{
//                if (ActivityCompat.checkSelfPermission(
//                        this,
//                        Manifest.permission.BLUETOOTH_CONNECT
//                    ) != PackageManager.PERMISSION_GRANTED
//                ) {
//                    // T Consider calling
//                    //    ActivityCompat#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for ActivityCompat#requestPermissions for more details.
//                    return@setOnClickListener
//                }
//                bAdapter.disable()
//                bluetoothImageView.setImageResource(R.drawable.ic_bluetooth_off)
//                Toast.makeText(this, "Bluetooth turned off", Toast.LENGTH_SHORT).show()
        }
        //turn on Bluetooth discoverable
        discoverableButton.setOnClickListener {
            if(!bAdapter.isDiscovering){
                Toast.makeText(this, "Making your device discoverable", Toast.LENGTH_SHORT).show()
            }
            val intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
            startActivityForResult(intent,REQUEST_CODE_DISCOVERABLE_BT)
//                var getResult =
//                    registerForActivityResult(
//                        ActivityResultContracts.StartActivityForResult()
//                    ) {
//                        if (it.resultCode == Activity.RESULT_OK) {
//                            bluetoothImageView.setImageResource(R.drawable.ic_bluetooth_on)
//                            Toast.makeText(this, "Bluetooth is on", Toast.LENGTH_SHORT).show()
//                        }else{
//
//                            Toast.makeText(this, "Bluetooth is still off", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                //Caller
//                getResult.launch(intent)
        }

//      turn pairing bluetooth
        pairedButton.setOnClickListener {
            if(bAdapter.isEnabled){
                pairedDevicesTextView.text ="Paired Devices"
                // get list f paired devices
                val devices =bAdapter.bondedDevices
                for (device in devices){
                    val deviceName = device.name
                    pairedDevicesTextView.append("\n Device: $deviceName, $device")
                }
            }else{
                Toast.makeText(this, "Please turn on Bluetooth", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            REQUEST_CODE_ENABLE_BT->
                if(resultCode == Activity.RESULT_OK){
                    bluetoothImageView.setImageResource(R.drawable.ic_bluetooth_on)
                    Toast.makeText(this, "Bluetooth is on", Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(this, "Could not turn on Bluetooth", Toast.LENGTH_LONG).show()
                }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}