package com.tcl.tv.ideo.yaw

import android.util.Log

import java.net.InetAddress
import java.net.Socket
import java.util.*
import android.system.Os.socket
import java.io.*


class YawTCPClient private constructor(val ipAddress: InetAddress, val port: Int) {


    fun byteArrayOfInts(vararg ints: Int) = ByteArray(ints.size) { pos -> ints[pos].toByte() }


    public fun command(command: Int) : Boolean {
        val socket = Socket(ipAddress, port)
        val dataInputStream = DataInputStream(socket.getInputStream())
        val dataOutputStream = DataOutputStream(socket.getOutputStream())
        val arr = byteArrayOfInts(command)
        dataOutputStream.write(arr)

        val buffer = ByteArray(4)
        dataInputStream.read(buffer, 0, buffer.size)
        Log.i(TAG,"Server says " + Arrays.toString(buffer))
        dataInputStream.close()
        dataOutputStream.close()
        socket.close()
        return (buffer[0] == command.toByte())


    }


    companion object {
        var ins: YawTCPClient? = null
        private val TAG= "YAW_TCP_CLIENT"
        fun getInstance(ipAddress: InetAddress, port: Int): YawTCPClient? {
            if (ins == null) {
                ins= YawTCPClient(ipAddress,port);
            }
            return ins
        }
    }
}