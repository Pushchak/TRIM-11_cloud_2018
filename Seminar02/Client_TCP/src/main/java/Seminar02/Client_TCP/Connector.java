package Seminar02.Client_TCP;


import java.net.ConnectException;

public interface Connector {
	
	void ConnectAndWork() throws ConnectException;
	void Send(int length, byte[] requestBody);
	byte[] Receive();
	void Disconnect();

}
