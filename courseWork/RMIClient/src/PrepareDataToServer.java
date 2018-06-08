import java.io.*;
import java.rmi.*;

public class PrepareDataToServer {

	private static final int ELEMENTS = 1000_000;
	private static final int MAX_VALUE = 1000_000_000;

	private final ICompute remoteICompute;

	public PrepareDataToServer(ICompute remoteICompute) {
		this.remoteICompute = remoteICompute;
	}

	public void ping() throws RemoteException {
		System.out.println(remoteICompute.ping());
	}

	public void echo(String[] parameters) throws RemoteException {
		if (checkerParameters(2, parameters.length))
			System.out.println(remoteICompute.echo(parameters[1]));
		else
			System.out.println("please enter a message");
	}

	public void search(String[] parameters) throws RemoteException, IOException {
		if (checkerParameters(3, parameters.length)) {

			String[] stringMass = new String[ELEMENTS];

			for (int i = 0; i < stringMass.length; i++) {
				stringMass[i] = String.valueOf((int) (Math.random() * MAX_VALUE));
			}

			File file = write(parameters[1], stringMass);
			System.out.println("Sending file to the server and waiting for response");
			
			
			int[] valuePositions = remoteICompute.executeTask( new ICompute.SearchAlgorithm(new ICompute.FileInstance(file),Integer.parseInt(parameters[2])) );
			System.out.println("Search done!");
			
			if(valuePositions[0] == -1) {
				System.out.println("Value not found");
			}else {
				System.out.println("Value " + parameters[2] +" was found "+ valuePositions[0] +" times, at positions: ");
				
				for(int i = 1; i < valuePositions.length; i++) {
					System.out.println(valuePositions[i]);
				}
			}
		}
	}

	private boolean checkerParameters(int good, int our) {
		return (good == our) ? true : false;
	}

	private File write(String name, String[] inMass) {
		File newFile = new File(name);
		try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(newFile))) {
			for (String element : inMass) {
				dos.writeBytes(element + " ");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return newFile;
	}

	public void exit() throws RemoteException {
		System.out.println("Exit from server");
		Start.workerFlag = false;
	}
}
