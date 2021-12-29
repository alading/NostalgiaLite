package com.tcl.tv.ideo.yaw

import android.util.Log
import com.appblend.handfree.yaw.Constants.Companion.UDP_PORT
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
            destIPaddress,
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
                udpSocket?.send(packet)
            } catch (e: IOException) {
                Log.e(TAG, " UDP client has IOException" +"error: \n", e)
                run = false
                udpSocket?.close()
            }
        }
        udpSocket?.close()
        return text
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