package Seminar04.SOAPmvn;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import lpi.server.soap.*;

public class Soap implements Connector {
	private String command;
	private String requestCommand;
	private String sessionId;
	private Parser parser = new Parser();
	private String[] parseCommand;
	private boolean loginStatus = false;
	private ChatServer serverWrapper = new ChatServer();
	private IChatServer serverProxy = serverWrapper.getChatServerProxy();
	public Message mess = new Message();
	private Timer timer;
	private FileInfo file = new FileInfo();
	private Scanner scan = new Scanner(System.in);
	
	private TimerTask createNewMsgTask() {
		return new TimerTask() {
			@Override
			public void run() {
				try {
					Message msg = serverProxy.receiveMessage(sessionId);
					if (msg != null) {
						System.out.println("=>"+ msg.getSender() + ": "+ msg.getMessage());
					}
				} catch (ArgumentFault e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ServerFault e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
	}
	
	private TimerTask createNewFileTask() {
		return new TimerTask() {
			@Override
			public void run() {
				try {
					serverProxy.receiveFile(sessionId);
				} catch (ArgumentFault e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ServerFault e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
	}

	@Override
	public void ConnectAndWork() {
		
		System.out.println("Client connect...");
		System.out.println("Enter the command:");	
		
	
		
		while (true) {
			command = scan.nextLine().trim();
			parseCommand = parser.Parse(command);
			
			
			if (parseCommand != null) {
				if (parseCommand[0].equals("ping")) {
					Ping();
				} else if (parseCommand[0].equals("echo")) {
					Echo(parseCommand[1]);
				} else if (parseCommand[0].equals("login")) {
					
					sessionId = Login(parseCommand[1], parseCommand[2]);
					if (sessionId != null) {
						loginStatus = true;
						mess.setSender(parseCommand[1]);
						file.setSender(parseCommand[1]);
					}
					if(sessionId!=null){
						timer = new Timer();
						timer.scheduleAtFixedRate(createNewFileTask(), 200, 200);
						timer.scheduleAtFixedRate(createNewMsgTask(), 200, 200);
					}
					
				}

				if ((parseCommand[0].equals("list") || parseCommand[0].equals("msg") || parseCommand[0].equals("file")
						|| parseCommand[0].equals("exit")) && loginStatus == false) {
					System.out.println("You should first login");
				} else if ((parseCommand[0].equals("list") || parseCommand[0].equals("msg")
						|| parseCommand[0].equals("file") || parseCommand[0].equals("exit")) && loginStatus == true) {

					switch (parseCommand[0]) {
					case "list": {
						List(sessionId);
						break;
					}
					case "msg": {
						SendMsg(sessionId, parseCommand[1], parseCommand[2]);
						break;
					}
					case "exit": {
						Exit(sessionId);
						break;
					}
					case "file":{
						SendFile(sessionId, parseCommand[1], parseCommand[2]);
						break;
					}
					}
				}

			}

		}

	}

	// =========================== ping =======================
	private void Ping() {
		serverProxy.ping();
		System.out.println("ping successful");
	}

	// =========================== echo =======================
	private void Echo(String parameters) {
		System.out.println(serverProxy.echo(parameters));
	}

	// =========================== login =======================
	private String Login(String login, String password) {
		String sessionId = null;
		try {
			sessionId = serverProxy.login(login, password);
			System.out.println("sessionId: "+sessionId);
			System.out.println("login ok");
		} catch (ArgumentFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LoginFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServerFault e) {
			System.out.println("invalid password");
			e.printStackTrace();
		}
		return sessionId;
	}

	// =========================== list =======================
	private void List(String sessionId) {
		List<String> listUser = new ArrayList<String>();
		try {
			listUser = serverProxy.listUsers(sessionId);
			System.out.println(listUser);
		} catch (ArgumentFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServerFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// =========================== exit =======================
	private void Exit(String sessionId) {
		timer.cancel();
		try {
			serverProxy.exit(sessionId);
		} catch (ArgumentFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServerFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Client disconnect");
	}
// =========================== send Msg =======================
		private void SendMsg(String sessionId, String receiver, String message) {
			mess.setReceiver(receiver);
			mess.setMessage(message);
			try {
				serverProxy.sendMessage(sessionId, mess);
			} catch (ArgumentFault e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ServerFault e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
//=============================== send File ================
		private void SendFile(String sessionId, String receiver, String pathToFile) {
			file.setReceiver(receiver);
			Serialization ser = new Serialization();
			Path path = FileSystems.getDefault().getPath(pathToFile);
			file.setFilename(path.getFileName().toString());
			byte[] fileByte;
			try {
				fileByte = Files.readAllBytes(path);
				file.setFileContent(fileByte);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			try {
				serverProxy.sendFile(sessionId, file);
			} catch (ArgumentFault e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ServerFault e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
}
