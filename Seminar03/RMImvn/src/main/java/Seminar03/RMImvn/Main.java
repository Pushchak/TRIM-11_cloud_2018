package Seminar03.RMImvn;

import java.rmi.NotBoundException;

public class Main {

	public static void main(String[] args) {
		RMI rmi = new RMI();
		try {
			rmi.connectAndWork();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
