package com.cgutman.androidremotedebugger.devconn;

import com.cgutman.adblib.AdbCrypto;

public interface DeviceConnectionListener {
	public AdbCrypto loadAdbCrypto(DeviceConnection devConn);

	public void notifyConnectionEstablished(DeviceConnection devConn);

	public void notifyConnectionFailed(DeviceConnection devConn, Exception e);

	public void notifyStreamFailed(DeviceConnection devConn, Exception e);

	public void notifyStreamClosed(DeviceConnection devConn);

	public void receivedData(DeviceConnection devConn, byte[] data, int offset, int length);

//	public boolean canReceiveData();

//	public boolean isConsole();
	
//	public void consoleUpdated(DeviceConnection devConn, ConsoleBuffer console);
}
