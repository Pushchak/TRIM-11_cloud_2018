package Seminar02.ClientTCP;

import java.io.File;
import java.io.IOException;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class ExpressionAnalysis implements Parser {
	ClientTcp socketClient = null;
	Serialization ser = new Serialization();

	public ExpressionAnalysis(ClientTcp socketClient) {
		this.socketClient = socketClient;
	}

	@Override
	public byte[] parseRequest(String userInput) {
		byte[] content = null;
		StringBuilder sb;
		String[] userIn = WorldWithoutSpace(userInput);

		switch (userIn[0]) {
		case "ping": {
			content = new byte[1];
			content[0] = 1;
			break;
		}
		case "echo": {
			sb = new StringBuilder(userInput);
			String parameter_STR = sb.delete(0, userIn[0].length()).toString().trim();

			byte[] Parameters = parameter_STR.getBytes();
			content = new byte[Parameters.length + 1];
			content[0] = 3;
			for (int i = 1, j = 0; i < content.length; i++, j++) {
				content[i] = Parameters[j];
			}
			break;
		}
		case "login": {
			if (userIn.length >= 3) {
				String[] LogPass = new String[2];
				LogPass[0] = userIn[1];
				LogPass[1] = userIn[2];
				try {
					byte[] serializeArr = ser.Serialize(LogPass);
					content = new byte[serializeArr.length + 1];
					content[0] = 5;
					System.arraycopy(serializeArr, 0, content, 1, serializeArr.length);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (userIn.length < 3) {
				content = new byte[1];
				content[0] = 2;
				System.out.println("login or/and password is has not been entered");
			}
			break;
		}
		case "list": {
			content = new byte[1];
			content[0] = 10;
			break;
		}
		case "msg": {
			if (userIn.length >= 3) {
				String[] LogPass = new String[2];
				LogPass[0] = userIn[1];
				sb = new StringBuilder(userInput);
				String parameter_STR = sb.delete(0, userIn[0].length()).toString().trim();
				String[] newSTR = WorldWithoutSpace(parameter_STR);
				sb = new StringBuilder(parameter_STR);
				LogPass[1] = sb.delete(0, newSTR[0].length()).toString().trim();
				try {
					byte[] serializeArr = ser.Serialize(LogPass);
					content = new byte[serializeArr.length + 1];
					content[0] = 15;
					System.arraycopy(serializeArr, 0, content, 1, serializeArr.length);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (userIn.length == 2) {
				System.out.println("You did not enter a message");
			} else if (userIn.length == 1) {
				System.out.println("no receiver name");
			}
			break;
		}
		case "file": {
			if (userIn.length == 3) {
				String receiverName = userIn[1];
				String pathToFile = userIn[2];
				File f = new File(pathToFile);
				if (f.exists()) {
					Path path = FileSystems.getDefault().getPath(pathToFile);
					String nameFile = path.getFileName().toString();
					byte[] fileByte;
					try {
						fileByte = Files.readAllBytes(path);
						Object obj = new Object[] { receiverName, nameFile, fileByte };
						byte[] serializeArr = ser.Serialize(obj);
						content = new byte[serializeArr.length + 1];
						content[0] = 20;
						System.arraycopy(serializeArr, 0, content, 1, serializeArr.length);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					System.out.println("Problems with file access");
					content = new byte[] {};
				}
				
			} else if (userIn.length <= 2) {
				System.out.println("destination user or/and file path has not been entered");
			}

			break;
		}
		case "exit": {
			socketClient.Disconnect();
			System.out.println("Ñlient disconnected.");
			break;
		}
		case "info": {
			PrintInfo();
			break;
		}
		default: {
			System.out.println("Command not found");
			break;
		}
		}

		return content;
	}

	@Override
	public String parseResponse(String userCommand, byte[] responseContent) {
		String response = AnalizResp(responseContent);
		if (userCommand.indexOf("echo") >= 0 && response == null) {
			response = new String(responseContent);
		}

		if (userCommand.indexOf("list") >= 0 && response == null) {
			try {
				String[] str = (String[]) (ser.Deserialize(responseContent));
				response = Arrays.toString(str);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return response;
	}

	// ================= AnalizResp ================================
	public static String AnalizResp(byte[] responseContent) {
		String resp = null;

		if (responseContent.length == 1) {
			switch (responseContent[0]) {
			case 6: {
				resp = "new user, registration ok";
				break;
			}
			case 2: {
				resp = "ping done successfully";
				break;
			}
			case 7: {
				resp = "login ok";
				break;
			}
			case 16: {
				resp = "message sent";
				break;
			}
			case 21: {
				resp = "file sent";
				break;
			}
			case 26: {
				resp = "no message";
				break;
			}
			case 31: {
				resp = "no files waiting";
				break;
			}
			case 100: {
				resp = "ERROR [100] - internal Server Error occurred";
				break;
			}
			case 101: {
				resp = "ERROR [101] - the transfer size is below 0 or above 10MB";
				break;
			}
			case 102: {
				resp = "ERROR [102] - server failed to deserialize content";
				break;
			}
			case 103: {
				resp = "ERROR [103] - server did not unserstand the command";
				break;
			}
			case 104: {
				resp = "ERROR [104] - incorrect number or content of parameters";
				break;
			}
			case 110: {
				resp = "ERROR [110] - the specified password is incorrect (this login was used before with another password)";
				break;
			}
			case 112: {
				resp = "ERROR [112] - this command requires login first";
				break;
			}
			case 113: {
				resp = "ERROR [113] - failed to send the message or file";
				break;
			}
			}
		}
		return resp;
	}

	// ===================== WorldWithoutSpace ==================================
	private String[] WorldWithoutSpace(String userInput) {
		String[] T;
		int count = 0;
		T = userInput.split(" ");
		for (int i = 0; i < T.length; i++) {
			if (T[i].equals(""))
				count++;
		}
		String[] tem = new String[T.length - count];
		for (int i = 0, j = 0; i < T.length; i++) {
			if (T[i].equals(""))
				continue;
			else {
				tem[j] = T[i];
				j++;
			}
		}
		return tem;
	}

	// ===========================================================
	// ====== PrintInfo ======
	// ===========================================================

	private static void PrintInfo() {
		System.out.println("___________________________________________________");
		System.out.println("ping");
		System.out.println("echo <any text>");
		System.out.println("list");
		System.out.println("login <login> <password>                   (login and password must be without space)");
		System.out.println("msg <destination user> <text>              (destionation user must be without space)");
		System.out.println("file <destination user> <path to file>     (destionation user must be without space)");
	}

}
