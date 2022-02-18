package com.tcl.tv.ideo.player.ui.yawvr

import android.app.Fragment
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appblend.handfree.yaw.Constants
import com.appblend.handfree.yaw.R
import com.appblend.handfree.yaw.YawActivity
import com.appblend.handfree.yaw.YawChairListAdapter
import com.tcl.sevend.yaw.YawChair
import com.tcl.tv.ideo.yaw.YawUDPClient
import kotlinx.coroutines.*
import java.io.IOException
import java.net.*
import java.util.*

/**
 * An example full-screen fragment that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class YAWConnectFragment : Fragment() {

    private var lisOfChair: List<YawChair>? = null
    private lateinit var rvChairs: RecyclerView
    private val BROADCASTING_PORT = 50010
    private var tvDisplay: TextView? = null
    private lateinit var  okButton: Button
    private lateinit var  retryButton: Button
    private lateinit var  testButton: Button
    private var sevenDayFreeTrialButton: View? = null
    private var coroutineJob: Job? = null
    val coroutineExceptionHandler = CoroutineExceptionHandler{ _, t ->
        run {
            t.printStackTrace()
        }
    }



    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_yaw_connect, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
        Diable Login process
         */

        rvChairs = view.findViewById<RecyclerView>(R.id.rv_yaw_chair)
        val lmMovie: RecyclerView.LayoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        rvChairs.setLayoutManager(lmMovie)


        okButton = view.findViewById<Button>(R.id.okButton)
        retryButton = view.findViewById<Button>(R.id.retry)
        testButton = view.findViewById<Button>(R.id.test)
        tvDisplay = view.findViewById<TextView>(R.id.tv_guest)
        okButton?.setOnClickListener {
            activity.finish()

        }
        retryButton?.setOnClickListener {
            tvDisplay?.text = "Searching motion chair, please wait..."
            okButton.visibility = View.GONE
            retryButton.visibility = View.GONE
            testButton.visibility = View.GONE
            getYawDevice()
        }
        testButton?.setOnClickListener {
            Toast.makeText(it.context,"click test", Toast.LENGTH_SHORT).show()
            val hostActivity = activity as YawActivity
            val testFragment = YAWTestFragment();
            hostActivity.replaceFragment(testFragment)

        }

        getYawDevice()


    }





    override fun onDestroy() {
        super.onDestroy()
        tvDisplay = null
        sevenDayFreeTrialButton = null
    }





    private fun getYawDevice() {


        coroutineJob = CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch {

            var output : String? = null
            val message = "YAW_CALLING"
            //var inetAddressNew = InetAddress.getByName("255.255.255.255")
            //output = broadcast(message, inetAddressNew)

            val yawUDPClient = YawUDPClient.getInstance()
            val inetAddress = InetAddress.getByName("255.255.255.255")


            lisOfChair = yawUDPClient?.broadcastForAll(message,true,inetAddress)

            withContext(Dispatchers.Main) {

                if(lisOfChair == null || lisOfChair!!.isEmpty()) {
                    tvDisplay?.text = "Chair not found, click ok button to ignore"
                    okButton.visibility = View.VISIBLE
                    retryButton.visibility = View.VISIBLE
                } else {

                    rvChairs.visibility = View.VISIBLE
                    val yawListAdapter = YawChairListAdapter(lisOfChair, ::onClickYawChairList)
                    rvChairs.adapter = yawListAdapter
                    yawListAdapter.notifyDataSetChanged()
                    tvDisplay?.text = "Below chair found "+"\n please choose one of chair to continue"
                    //okButton.visibility = View.VISIBLE
                    //testButton.visibility = View.VISIBLE
                }
            }


        }

    }


    private fun onClickYawChairList(pos: Int) {

        Constants.Yaw_Chair_IpAddress = lisOfChair?.get(pos)?.ipAddress
        activity.finish()
    }


    @Throws(IOException::class)
    private fun broadcast(broadcastMessage: String, broadcastAddress: InetAddress) : String? {
        val socket = DatagramSocket()
        socket.broadcast = true
        val buffer = broadcastMessage.toByteArray()
        val packet = DatagramPacket(
            buffer,
            buffer.size,
            broadcastAddress,
            BROADCASTING_PORT
        )
        socket.send(packet)
        var run = true
        var text : String? = null
        while (run) {
            try {
                val messageBuffer = ByteArray(100)
                val udppacket = DatagramPacket(messageBuffer, messageBuffer.size)
                Log.i(
                    TAG,
                    "about to wait to receive"
                )
                socket.soTimeout = 10000
                socket.receive(udppacket)
                text = String(messageBuffer, 0, udppacket.length)
                Log.d(
                    TAG,
                    "Received text = $text"
                )
                text = udppacket.address.hostAddress +":50020"

                run = false
            } catch (e: SocketTimeoutException) {
                Log.e(TAG,"Timeout Exception" +"UDP Connection: \n" + e)
                run = false
                socket.close()
            } catch (e: IOException) {
                Log.e(TAG," UDP client has IOException"+ "error: \n"+ e)
                run = false
                socket.close()
            }
        }
        socket.close()
        return text
    }




    @Throws(SocketException::class)
    fun listAllBroadcastAddresses(): List<InetAddress>? {
        val broadcastList: MutableList<InetAddress> = ArrayList()
        val interfaces = NetworkInterface.getNetworkInterfaces()
        while (interfaces.hasMoreElements()) {
            val networkInterface = interfaces.nextElement()
            if (networkInterface.isLoopback || !networkInterface.isUp) {
                continue
            }
            for (interfaceAddress in networkInterface.interfaceAddresses) {
                val inetAddress = interfaceAddress.broadcast
                if (inetAddress != null) {
                    broadcastList.add(inetAddress)
                }
            }
        }
        return broadcastList
    }


    companion object {
        private const val TAG = "YawConnectFragment"
    }
}