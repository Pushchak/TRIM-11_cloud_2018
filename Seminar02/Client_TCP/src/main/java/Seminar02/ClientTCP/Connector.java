package Seminar02.ClientTCP;


import java.net.ConnectException;

public interface Connector {
	
	void connectAndWork() throws ConnectException;
	void send(int length, byte[] requestBody);
	byte[] receive();
	void disconnect();

}
