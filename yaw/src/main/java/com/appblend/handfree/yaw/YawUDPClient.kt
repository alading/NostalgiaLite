package com.tcl.tv.ideo.yaw

import android.util.Log
import com.appblend.handfree.yaw.Constants
import com.appblend.handfree.yaw.Constants.Companion.UDP_PORT
import com.appblend.handfree.yaw.Constants.Companion.Yaw_Chair_Ignore
import com.tcl.sevend.yaw.YawChair
import kotlinx.coroutines.*
import java.io.IOException
import java.net.*

class YawUDPClient private constructor() {

    lateinit var destIPaddress: InetAddress
    private var coroutineJob: Job? = null
    private var udpSocket: DatagramSocket? = null
    val coroutineExceptionHandler = CoroutineExceptionHandler{ _, t ->
        run {
            t.printStackTrace()
        }
    }

    fun sendMessage(message: String) {

        if(udpSocket == null) {
            udpSocket = DatagramSocket()
            udpSocket?.broadcast = false
        }
        val buffer = message.toByteArray()
        val packet = DatagramPacket(
            buffer,
            buffer.size,
            InetAddress.getByName(Constants.Yaw_Chair_IpAddress),
            UDP_PORT
        )
        Log.v(TAG, "--> "+ message)
        udpSocket?.send(packet)

    }
    public fun broadcast(broadcastMessage: String, isBroadcast: Boolean, inetAddress: InetAddress = destIPaddress) : String?{

        var udpSocket: DatagramSocket? = DatagramSocket()
        udpSocket?.broadcast = isBroadcast
        val buffer = broadcastMessage.toByteArray()
        val packet = DatagramPacket(
            buffer,
            buffer.size,
            inetAddress,
            UDP_PORT
        )
        udpSocket?.send(packet)
        var run = true
        var text : String? = null
        var retryTime = 2
        while (run) {
            try {
                val messageBuffer = ByteArray(100)
                val udppacket = DatagramPacket(messageBuffer, messageBuffer.size)
                Log.i(
                    TAG,
                    "about to wait to receive"
                )
                udpSocket?.soTimeout = 10000
                udpSocket?.receive(udppacket)
                text = String(messageBuffer, 0, udppacket.length)
                Log.d(
                    TAG,
                    "Received text = $text"
                )
                destIPaddress = udppacket.address
                text = destIPaddress.hostAddress
                run = false
            } catch (e: SocketTimeoutException) {
                Log.e(TAG,"Timeout Exception"+ "UDP Connection: \n" +e)
                retryTime--;
                if(retryTime < 0) {
                    Yaw_Chair_Ignore = true
                    run = false
                } else {
                    udpSocket?.send(packet)
                }
            } catch (e: IOException) {
                Log.e(TAG, " UDP client has IOException" +"error: \n", e)
                run = false
                udpSocket?.close()
            }
        }
        udpSocket?.close()
        return text
    }

    public fun broadcastForAll(
        broadcastMessage: String,
        isBroadcast: Boolean,
        inetAddress: InetAddress = destIPaddress
    ): List<YawChair> {

        val listOfIpAddress = mutableListOf<YawChair>()
        var udpSocket: DatagramSocket? = DatagramSocket()
        udpSocket?.broadcast = isBroadcast
        val buffer = broadcastMessage.toByteArray()
        val packet = DatagramPacket(
            buffer,
            buffer.size,
            inetAddress,
            UDP_PORT
        )
        udpSocket?.send(packet)
        var run = true
        val maxDevices = 2
        var text: String? = null
        while (run) {
            try {
                val messageBuffer = ByteArray(100)
                val udppacket = DatagramPacket(messageBuffer, messageBuffer.size)
                Log.i(
                    TAG,
                    "about to wait to receive times = $run"
                )
                udpSocket?.soTimeout = 10000
                udpSocket?.receive(udppacket)
                text = String(messageBuffer, 0, udppacket.length)

                Log.d(TAG, "Received text = $text")
                /*
                Received text = YAWDEVICE;EMULATOR01;YAW_EMULATOR;50020;AVAILABLE

                YAWDEVICE;001;YawVRSimulator;50020;AVAILABLE , where

                YAWDEVICE is a literal, YAW2 will contain YAWDEVICE2
                001 is UUID, obsolete, does not matter. Ignore it
                YawVRSimulator is the device's name
                50020 is the port which the device listens for tcp connections.
                Users can't change simply change this, should be always 50020 (default)
                AVAILABLE means no game is checked in, RESERVED means a game is checked in
                (a small controller shown on our YawVR app, for example)
                 */

                val YawUDPResponse = text.split(";")
                val YawChair =
                    YawChair(YawUDPResponse[2], udppacket.address.hostAddress, YawUDPResponse[3])
                text = udppacket.address.hostAddress
                if (!listOfIpAddress.contains(YawChair)) {
                    listOfIpAddress.add(YawChair)
                }


            } catch (e: SocketTimeoutException) {
                Log.e(TAG, "Timeout Exception" + "UDP Connection: \n" + e)
                //udpSocket?.send(packet)
                run = false
            } catch (e: IOException) {
                Log.e(TAG, " UDP client has IOException" + "error: \n", e)
                run = false
            }
        }
        udpSocket?.close()
        return listOfIpAddress
    }

    fun disconnect() {}
    fun sendMessage(topic: String?, message: String?) {}

    companion object {
        var ins: YawUDPClient? = null
        private val TAG= "YAW_UDP_CLIENT"
        @Throws(SocketException::class)
        fun getInstance(): YawUDPClient? {
            if (ins == null) {
                ins = YawUDPClient()
            }
            return ins
        }
    }
}