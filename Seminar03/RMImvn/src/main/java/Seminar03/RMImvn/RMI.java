package Seminar03.RMImvn;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Proxy;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import lpi.server.rmi.IServer;
import lpi.server.rmi.IServer.ArgumentException;
import lpi.server.rmi.IServer.FileInfo;
import lpi.server.rmi.IServer.Message;
import lpi.server.rmi.IServer.ServerException;

public class RMI implements Connector {
	private Registry registry;
	private String response;
	private String[] req;
	private String sessionId = null;
	private Message mess;
	private String requestCommand;
	private ParseCommand execute;
	private IServer proxy;
	private Timer timer;
	private FileInfo file;

	private TimerTask createNewMsgTask() {
		return new TimerTask() {
			@Override
			public void run() {
				try {
					mess = proxy.receiveMessage(sessionId);
					if (mess != null) {
						System.out.println(mess);
					}
				} catch (ArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ServerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (RemoteException e) {
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
					file = new FileInfo();
					file = proxy.receiveFile(sessionId);

					if (file != null) {
						System.out.println(
								"Incomming file's " + "\"" + file.getFilename() + "\"" + " from " + file.getSender());
						Path pathToSaveFiles = FileSystems.getDefault()
								.getPath(System.getProperty("user.home") + "/Desktop/");
						try {
							file.saveFileTo(new File(pathToSaveFiles.toString()));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.out.println("File saved to \"" + pathToSaveFiles + "\"");
					}

				} catch (ArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ServerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
	}

	@Override
	public void ConnectAndWork() throws NotBoundException {

		try {
			
			registry = LocateRegistry.getRegistry("localhost", 4321);
			proxy = (IServer) registry.lookup(IServer.RMI_SERVER_NAME);
			System.out.println("Client connecting... Enter \"info\" to view available commands");
			System.out.println("Enter the command: ");
			Scanner scan = new Scanner(System.in);

			execute = new ParseCommand(this);

			while (true) {
				scan = new Scanner(System.in);
				requestCommand = scan.nextLine().trim();
				req = execute.ParseRequest(requestCommand);

				if (req != null) {
					if (req[0].equals("ping")) {
						proxy.ping();
						System.out.println("ping successful");
					} else if (req[0].equals("echo")) {
						response = proxy.echo(req[1]);
						System.out.println(response);
					} else if (req[0].equals("login")) {
						if (sessionId == null) {
							try {
								response = proxy.login(req[1], req[2]);
								sessionId = response;
							} catch (java.rmi.ServerException ex) {
								System.out.println("invalid password");
							}

							file = new FileInfo();
							file.setSender(req[1]);

							if (sessionId != null) {
								System.out.println("login ok");
								timer = new Timer();
								timer.scheduleAtFixedRate(createNewMsgTask(), 200, 200);
								timer.scheduleAtFixedRate(createNewFileTask(), 200, 200);
							}
						}

					} else if ((req[0].equals("exit") || req[0].equals("list") || req[0].equals("msg")
							|| req[0].equals("file")) && sessionId != null) {
						if (req[0].equals("exit")) {
							proxy.exit(sessionId);
							timer.cancel();
							sessionId = null;
							System.out.println("Client disconnect");
						} else if (req[0].equals("list")) {
							System.out.println(Arrays.toString(proxy.listUsers(sessionId)));
						} else if (req[0].equals("msg")) {

							mess = new Message(req[1], req[2]);
							proxy.sendMessage(sessionId, mess);
						} else if (req[0].equals("file")) {

							String receiver = req[1].trim();
							String pathToFile = req[2].trim();
							try {
								Path path = Paths.get(pathToFile);
								String filename = path.getFileName().toString();
								byte[] content = null;
								content = Files.readAllBytes(path);
								file = new FileInfo();
								FileInfo fileInfo = new FileInfo(receiver, "semen", filename, content);
								proxy.sendFile(sessionId, fileInfo);
							}catch(java.nio.file.NoSuchFileException ex) {
								System.out.println("Error path to file");
							}catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						
						}
					} else if ((req[0].equals("exit") || req[0].equals("list") || req[0].equals("msg"))
							&& sessionId == null) {
						System.out.println("You should login first");
					}
				}
			}
		}catch(java.rmi.ConnectException rmi) {
			System.out.println("Server is not available");
		}catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
