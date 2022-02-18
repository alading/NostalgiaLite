package com.tcl.tv.ideo.player.ui.yawvr

import android.app.Fragment
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.annotation.RequiresApi
import com.appblend.handfree.yaw.Constants
import com.appblend.handfree.yaw.R
import com.tcl.tv.ideo.yaw.YawTCPClient
import com.tcl.tv.ideo.yaw.YawUDPClient
import kotlinx.coroutines.*
import java.net.InetAddress

/**
 * An example full-screen fragment that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class YAWTestFragment : Fragment(){

    private lateinit var submitButton: Button
    private lateinit var backButton: Button

    private lateinit var yawSpinner: Spinner
    private lateinit var pitchSpinner: Spinner
    private lateinit var rollSpinner: Spinner

    private lateinit var ampSpinner: Spinner
    private lateinit var frequencySpinner: Spinner

    private lateinit var tvCommand: TextView
    private var coroutineJob: Job? = null
    val coroutineExceptionHandler = CoroutineExceptionHandler{ _, t ->
        run {
            t.printStackTrace()
        }
    }

    val itemClickListener = object: OnItemSelectedListener {

            override fun onItemSelected(var1: AdapterView<*>?, view: View?, position: Int, id: Long) {
                tvCommand.text = COMMAND.format(
                    yawSpinner.getSelectedItem().toString(),
                    pitchSpinner.getSelectedItem().toString(),
                    rollSpinner.getSelectedItem().toString(),
                    ampSpinner.getSelectedItem().toString(),
                    frequencySpinner.getSelectedItem().toString(),
                    )
            }
            override fun onNothingSelected(var1: AdapterView<*>?) {

            }

    }


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_yaw_test, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvCommand = view.findViewById<Button>(R.id.tv_command)
        submitButton = view.findViewById<Button>(R.id.bt_sumit)
        submitButton?.setOnClickListener {
            coroutineJob = CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch {

                YawUDPClient.getInstance()?.sendMessage(tvCommand.text.toString())
                withContext(Dispatchers.Main) {
                    Log.d(TAG, tvCommand.text.toString())
                }

            }
        }


        backButton = view.findViewById<Button>(R.id.bt_back)
        backButton?.setOnClickListener {
            activity.onBackPressed()
        }

        var moveAngle = Array(360) {
            it.toString()
        }


        yawSpinner = view.findViewById<Spinner>(R.id.spinner_yaw)
        val yawAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, moveAngle)
        yawAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        yawSpinner.setAdapter(yawAdapter)
        yawSpinner.onItemSelectedListener = itemClickListener

        pitchSpinner = view.findViewById<Spinner>(R.id.spinner_pitch)
        val pitchAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, moveAngle)
        pitchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        pitchSpinner.setAdapter(pitchAdapter)
        pitchSpinner.onItemSelectedListener = itemClickListener

        rollSpinner = view.findViewById<Spinner>(R.id.spinner_roll)
        val rollAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, moveAngle)
        rollAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        rollSpinner.setAdapter(rollAdapter)
        rollSpinner.onItemSelectedListener = itemClickListener

        var vibration = Array(100) {
            it.toString()
        }

        ampSpinner = view.findViewById<Spinner>(R.id.spinner_amp)
        val ampAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, vibration)
        ampAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        ampSpinner.setAdapter(ampAdapter)
        ampSpinner.onItemSelectedListener = itemClickListener

        frequencySpinner = view.findViewById<Spinner>(R.id.spinner_frequency)
        val frequencyAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, vibration)
        frequencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        frequencySpinner.setAdapter(frequencyAdapter)
        frequencySpinner.onItemSelectedListener = itemClickListener


        // TCP Connect
        CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch {
            Constants.Yaw_Chair_IpAddress?.let {
                val inetAddress = InetAddress.getByName(it)
                val startTCPOK = (YawTCPClient.getInstance(inetAddress, Constants.TCP_PORT)?.command(
                    Constants.START
                )!!)
                withContext(Dispatchers.Main) {
                    if(!startTCPOK) {
                        Toast.makeText(context,"\nActivate Yaw failure, please click me to try again", Toast.LENGTH_SHORT).show()
                    }

                }
            }

        }

    }



    companion object {
        private const val TAG = "YawTestFragment"
        private const val COMMAND = "Y[%1\$s]P[%2\$s]R[%3\$s]V[%4\$s,%4\$s,%4\$s,%5\$s]"
    }


}