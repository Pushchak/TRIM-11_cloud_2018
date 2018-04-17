package Seminar02.Client_TCP;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class ClientTcp implements Connector {
	private Socket socketClient;
	private DataInputStream inputReader = null;
	private DataOutputStream outWriter = null;
	private MonitorReceiveMessageFile monitoring;
	private ExpressionAnalysis exAnalyz = new ExpressionAnalysis(this);

	private int length;
	private byte[] request = null;
	private Scanner scan = null;
	
	public ExpressionAnalysis getAnalizator() {
		return exAnalyz;
	}
	
	
	@Override
	public void ConnectAndWork() throws ConnectException {
		try {
			socketClient = new Socket("localhost",4321);
			inputReader = new DataInputStream(socketClient.getInputStream());
			outWriter = new DataOutputStream(socketClient.getOutputStream());
			
			monitoring = new MonitorReceiveMessageFile(this,exAnalyz);
			
			monitoring.start();
			scan = new Scanner(System.in);
			System.out.println("Client is running...Enter <info> for command reference");
			System.out.print("Enter the command: ");
			
			while(socketClient.isConnected()) {
				
				String userInput = scan.nextLine();
				userInput.trim();
				request = exAnalyz.parseRequest(userInput);
				length = request.length;
				
				//Sending command
				Send(length,request);
				
				//Reading answer
				byte[] res = Receive();
				String Response_STR = exAnalyz.parseResponse(userInput,res);
				if(Response_STR!=null) {
					System.out.println(Response_STR);
					}
				}
			} catch (UnknownHostException e) {
			//	e.printStackTrace();
			} catch (IOException e) {
				//e.printStackTrace();
				System.out.println("Soory, server is not available.");
			}
		
		
	}

	@Override
	public synchronized void Send(int length, byte[] requestBody) {

			try {
				outWriter.writeInt(length);
				outWriter.write(requestBody);
				outWriter.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		
		
	}

	@Override
	public synchronized byte[] Receive() {
		int responseSize;
		byte[] responseContent = null;
		try {
			responseSize = inputReader.readInt();
			responseContent = new byte[responseSize];
			inputReader.readFully(responseContent);
			outWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return responseContent;
	}

	@Override
	public void Disconnect() {
		if(inputReader!=null) {
			try {
				inputReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				inputReader = null;
			}
		}
		if(outWriter!=null) {
			try {
				outWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				outWriter = null;
			}
		}
		
		if(socketClient!=null) {
			try {
				monitoring.stop();
				monitoring.join();
				socketClient.close();
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				socketClient = null;
			}
		}
		
	}


}


