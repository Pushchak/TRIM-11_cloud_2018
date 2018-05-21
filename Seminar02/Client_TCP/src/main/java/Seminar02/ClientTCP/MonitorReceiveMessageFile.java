package Seminar02.ClientTCP;


import java.io.IOException;
import java.net.Socket;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class MonitorReceiveMessageFile extends Thread {
	
	ClientTcp client = null;
	Socket socketClient = null;
	Serialization ser = new Serialization();
	ExpressionAnalysis exAnalyz = null;
	private boolean closed = false;
	
	public MonitorReceiveMessageFile(ClientTcp client, ExpressionAnalysis exAnalyz) {
		this.client = client;
		this.exAnalyz = exAnalyz;
	}
	
	@Override
	public void run() {
		try {
			while (true) {

				byte[] reqMsg = new byte[] {25};
				client.send(1, reqMsg);

				byte[] resp = client.receive();
				processResponse(resp);
				Thread.sleep(200);
				
				byte[] reqFile = new byte[] {30};
				client.send(1, reqFile);
				resp = client.receive();
				processResponse(resp);
				Thread.sleep(200);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void close() throws InterruptedException {
		closed = true;
		this.join();
	}
	
	private void processResponse(byte[] resp) {
		if(resp.length != 1) {
			Object[] obj;
			try {
				obj = (Object[])ser.Deserialize(resp);
				if(obj.length == 2) {
					System.out.println("Incomming message from [" + obj[0].toString()+"]");
					System.out.println("Message: "+obj[1].toString());
				}else {
					System.out.println("Incomming file's " + obj[1].toString()+ " from "+obj[0].toString());
					Path pathToSaveFiles = FileSystems.getDefault().getPath(System.getProperty("user.home") + "/Desktop/" + obj[1]);
					System.out.println("File saved to \""+pathToSaveFiles+"\"");
					Files.write(pathToSaveFiles, (byte[]) obj[2]);
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	
}
