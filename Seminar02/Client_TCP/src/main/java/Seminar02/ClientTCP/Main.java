package Seminar02.ClientTCP;

public class Main {

	public static void main(String[] args) {
		ClientTcp client = new ClientTcp();
		try {
			
			client.ConnectAndWork();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}

	}

}
