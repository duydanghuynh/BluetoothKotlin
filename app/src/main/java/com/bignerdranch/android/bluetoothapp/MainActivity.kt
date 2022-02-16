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
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {
    //bluetooth adapter
    private lateinit var bAdapter:BluetoothAdapter
    private lateinit var context:Context

    private lateinit var bluetoothStatusTextView: TextView
    private lateinit var bluetoothImageView: ImageView
    private lateinit var turnOnButton: Button
    private lateinit var turnOffButton: Button
    private lateinit var pairingButton: Button
    private lateinit var pairedButton: Button
    private lateinit var pairedDevicesTextView: TextView

//    private val REQUEST_CODE_ENABLE_BT:Int=1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //init bluetooth adapter
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bAdapter=bluetoothManager.adapter

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

        //turn on bluetooth
        turnOnButton.setOnClickListener {

            if (bAdapter.isEnabled){
                Toast.makeText(this, "Already on", Toast.LENGTH_SHORT).show()
            }else{
//                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//                startActivityForResult(intent,REQUEST_CODE_ENABLE_BT)
                // Receiver
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                val getResult =
                    registerForActivityResult(
                        ActivityResultContracts.StartActivityForResult()
                    ) {
                        if (it.resultCode == Activity.RESULT_OK) {
                            bluetoothImageView.setImageResource(R.drawable.ic_bluetooth_on)
                            Toast.makeText(this, "Bluetooth is on", Toast.LENGTH_SHORT).show()
                        }else{

                            Toast.makeText(this, "Bluetooth is still off", Toast.LENGTH_SHORT).show()
                        }
                    }
                //Caller
                getResult.launch(intent)
            }

        }
        //turn off bluetooth
        turnOffButton.setOnClickListener {
            if (!bAdapter.isEnabled){
                Toast.makeText(this, "Already off", Toast.LENGTH_SHORT).show()
            }else{
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
                bAdapter.disable()
                bluetoothImageView.setImageResource(R.drawable.ic_bluetooth_off)
                Toast.makeText(this, "Bluetooth turned off", Toast.LENGTH_SHORT).show()
            }
            //turn on Bluetooth pairing
            pairingButton.setOnClickListener {
                if(!bAdapter.isDiscovering){
                    Toast.makeText(this, "Making your device discoverable", Toast.LENGTH_SHORT).show()
                }
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
                val getResult =
                    registerForActivityResult(
                        ActivityResultContracts.StartActivityForResult()
                    ) {
                        if (it.resultCode == Activity.RESULT_OK) {
                            bluetoothImageView.setImageResource(R.drawable.ic_bluetooth_on)
                            Toast.makeText(this, "Bluetooth is on", Toast.LENGTH_SHORT).show()
                        }else{

                            Toast.makeText(this, "Bluetooth is still off", Toast.LENGTH_SHORT).show()
                        }
                    }
                //Caller
                getResult.launch(intent)
            }

            //turn pairing bluetooth
            pairedButton.setOnClickListener {
                if(bAdapter.isEnabled){
                    pairedDevicesTextView.text ="Paired Devices"
                    // get list f paired devices
                    val devices =bAdapter.bondedDevices
                    for (device in devices){
                        val deviceName = device.name
                        val deviceAddress = device
                        pairedDevicesTextView.append("\n Device: $deviceName, $deviceAddress")

                    }
                }else{
                    Toast.makeText(this, "Please turn on Bluetooth", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

}