package Seminar04.SOAPmvn;

public class Parser {
	public String[] request;
	private String[] userIn;
	private StringBuilder sb;
	
	public String[] parse(String command) {
		request = null;
		userIn = worldWithoutSpace(command);
		
		
		switch(userIn[0]){
		case "ping":{
			if(userIn.length == 1) {
				request = new String[] {userIn[0]};
			}
			break;
		}
		case "echo":{
			if (userIn.length == 1) {
				request = new String[] { userIn[0], "<empty text>" };
			} else {
				sb = new StringBuilder(command);
				String parameter_STR = sb.delete(0, userIn[0].length()).toString().trim();
				request = new String[] { userIn[0], parameter_STR };
			}
			break;
		}
		case "login":{
			if (userIn.length == 3) {
				request = new String[] { userIn[0], userIn[1], userIn[2] };
			} else
				System.out.println("login or/and password is empty");
			break;
		}
		case "list":{
			if(userIn.length ==1) {
				request = new String[1];
				request[0] = "list";
			}else {
				System.out.println("Command list must be without parameters");
			}
			
			break;
		}
		case "msg":{
			if (userIn.length == 1) {
				System.out.println("not input receiver User");
			} else  if(userIn.length == 2){
				request = new String[] { userIn[0], " "};
			}else  if(userIn.length > 2){
				sb = new StringBuilder(command);
				String parameter_STR = sb.delete(0, userIn[0].length()).toString().trim();
				request = new String[3];
				sb = new StringBuilder(parameter_STR);
				request[0] = userIn[0];
				request[1] = userIn[1];
				request[2] = sb.delete(0, userIn[1].length()).toString().trim();
			}
			break;
			
		}
		case "file":{
			if (userIn.length == 1) {
				System.out.println("not input receiver User or path to file");
			}else {
				sb = new StringBuilder(command);
				String parameter_STR = sb.delete(0, userIn[0].length()).toString().trim();
				request = new String[3];
				sb = new StringBuilder(parameter_STR);
				request[0] = userIn[0];
				request[1] = userIn[1];
				request[2] = sb.delete(0, userIn[1].length()).toString().trim();
			}
			break;
		}
		case "exit":{
			request = new String[] { userIn[0]};
			break;
		}
		default:{
			System.out.println("Command not found. Reentered command");
		}
		}
		return request;
	}
	
//===========================================================
	private String[] worldWithoutSpace(String userInput) {
		String[] T;
		int count = 0;
		T = userInput.split(" ");
		for (int i =0 ; i < T.length; i++) {
			if(T[i].equals(""))
				count++;
		}
		String[] tem = new String[T.length - count];
		for(int i = 0, j = 0; i < T.length; i++) {
			if(T[i].equals(""))
				continue;
			else {
				tem[j] = T[i];
				j++;
			}
		}
		return tem;
	}	
}
