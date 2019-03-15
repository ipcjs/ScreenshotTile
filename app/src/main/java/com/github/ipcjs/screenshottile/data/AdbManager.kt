package com.github.ipcjs.screenshottile.data

import android.util.Log
import com.cgutman.adblib.AdbCrypto
import com.cgutman.androidremotedebugger.AdbUtils
import com.cgutman.androidremotedebugger.devconn.DeviceConnection
import com.cgutman.androidremotedebugger.devconn.DeviceConnectionListener
import com.github.ipcjs.screenshottile.App
import kotlin.concurrent.thread

object AdbManager {
    private var _conn: DeviceConnection? = null
    private val appContext = App.getInstance()

    private val conn: DeviceConnection
        get() {
            _conn?.let {
                if (it.port != appContext.prefManager.adbPort) {
                    thread {
                        it.close()
                    }
                    _conn = null
                }
            }

            return _conn ?: createConnection().also { _conn = it }
        }

    fun sendCmd(cmd: String) {
        Log.i("AdbManager", "sendCmd(" + "cmd = [${cmd}]" + ")")
        conn.queueCommand(cmd + "\n")
    }

    private fun createConnection(): DeviceConnection {
        return DeviceConnection(object : DeviceConnectionListener {
            override fun loadAdbCrypto(devConn: DeviceConnection?): AdbCrypto {
                return AdbUtils.readCryptoConfig(appContext.filesDir)
            }

            override fun notifyConnectionEstablished(devConn: DeviceConnection) {
                Log.i("AdbManager", "notifyConnectionEstablished(" + "devConn = [${devConn}]" + ")")
            }

            override fun notifyStreamClosed(devConn: DeviceConnection) {
                Log.i("AdbManager", "notifyStreamClosed(" + "devConn = [${devConn}]" + ")")
            }

            override fun notifyStreamFailed(devConn: DeviceConnection, e: Exception?) {
                Log.w("AdbManager", "notifyStreamFailed(" + "devConn = [${devConn}], e = [${e}]" + ")", e)
            }

            override fun notifyConnectionFailed(devConn: DeviceConnection, e: Exception?) {
                Log.w("AdbManager", "notifyConnectionFailed(" + "devConn = [${devConn}], e = [${e}]" + ")", e)
            }

            override fun receivedData(devConn: DeviceConnection, data: ByteArray, offset: Int, length: Int) {
                Log.i("AdbManager", "receivedData(" + "devConn = [${devConn}], data = [${String(data, offset, length)}], offset = [${offset}], length = [${length}]" + ")")
            }
        }, "localhost", appContext.prefManager.adbPort)
                .apply { startConnect() }
    }
}