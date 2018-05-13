package Seminar02.ClientTCP;


import java.net.ConnectException;

public interface Connector {
	
	void ConnectAndWork() throws ConnectException;
	void Send(int length, byte[] requestBody);
	byte[] Receive();
	void Disconnect();

}
