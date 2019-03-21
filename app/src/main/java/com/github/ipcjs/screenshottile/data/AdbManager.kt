package com.github.ipcjs.screenshottile.data

import android.util.Log
import com.cgutman.adblib.AdbCrypto
import com.cgutman.androidremotedebugger.AdbUtils
import com.cgutman.androidremotedebugger.devconn.DeviceConnection
import com.cgutman.androidremotedebugger.devconn.DeviceConnectionListener
import com.github.ipcjs.screenshottile.App
import kotlin.concurrent.thread

object AdbManager {
    private var _conn: AdbShellConnection? = null
    private val appContext = App.getInstance()

    private val conn: AdbShellConnection
        get() {
            _conn?.let {
                if (it.port != appContext.prefManager.adbPort) {
                    thread {
                        it.close()
                    }
                    _conn = null
                } else if (it.state == AdbShellConnection.STATE_CLOSED) {
                    _conn = null
                }
            }

            return _conn ?: createConnection().also { _conn = it }
        }

    fun sendCmd(cmd: String): Boolean {
        return conn.sendCmd(cmd)
    }

    private fun createConnection(): AdbShellConnection {
        return AdbShellConnection("localhost", appContext.prefManager.adbPort)
    }

    class AdbShellConnection(val host: String, val port: Int) : DeviceConnectionListener {
        companion object {
            const val STATE_CONNECTING = 1
            const val STATE_OPEN = 2
            const val STATE_CLOSED = 3
        }

        private val conn = DeviceConnection(this, host, port).apply {
            startConnect()
        }
        @Volatile
        var state = STATE_CONNECTING
            private set

        fun close() {
            conn.close()
            state = STATE_CLOSED
        }

        fun sendCmd(cmd: String): Boolean {
            Log.i("AdbManager", "sendCmd(cmd = [${cmd}]), state=$state")
            conn.queueCommand(cmd + "\n")
            return state != STATE_CLOSED // 不是关闭状态, 才有可能发送成功
        }

        override fun loadAdbCrypto(devConn: DeviceConnection): AdbCrypto {
            return AdbUtils.readCryptoConfig(appContext.filesDir)
        }

        override fun notifyConnectionEstablished(devConn: DeviceConnection) {
            Log.i("AdbManager", "notifyConnectionEstablished(" + "devConn = [${devConn}]" + ")")
            state = STATE_OPEN
        }

        override fun notifyStreamClosed(devConn: DeviceConnection) {
            Log.i("AdbManager", "notifyStreamClosed(" + "devConn = [${devConn}]" + ")")
            state = STATE_CLOSED
        }

        override fun notifyStreamFailed(devConn: DeviceConnection, e: Exception?) {
            Log.w("AdbManager", "notifyStreamFailed(" + "devConn = [${devConn}], e = [${e}]" + ")", e)
            state = STATE_CLOSED
        }

        override fun notifyConnectionFailed(devConn: DeviceConnection, e: Exception?) {
            Log.w("AdbManager", "notifyConnectionFailed(" + "devConn = [${devConn}], e = [${e}]" + ")", e)
            state = STATE_CLOSED
        }

        override fun receivedData(devConn: DeviceConnection, data: ByteArray, offset: Int, length: Int) {
            Log.i("AdbManager", "receivedData(" + "devConn = [${devConn}], data = [${String(data, offset, length)}], offset = [${offset}], length = [${length}]" + ")")
        }
    }
}