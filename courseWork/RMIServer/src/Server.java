import java.io.*;
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;

public class Server implements ICompute {

	private Registry registry;
	private ICompute stub;

	public void startServer() {

		try {
			registry = LocateRegistry.createRegistry(PORT);
			stub = (ICompute) UnicastRemoteObject.exportObject(this, PORT);
			registry.rebind(ICompute.SERVER_NAME, stub);

		} catch (Exception ex) {
			System.err.println("ComputeEngine exception: ");
		}
	}

	@Override
	public String ping() throws RemoteException {
		return "ping successful";
	}

	@Override
	public String echo(String text) throws RemoteException {
		return "ECHO: " + text;
	}

	@Override
	public <T> T executeTask(ITask<T> t) throws IOException, RemoteException {
		return t.execute();
	}

	public void closeServer() {
		try {
			if (registry != null) {
				registry.unbind(SERVER_NAME);
				registry = null;
			}

			if (stub != null) {
				UnicastRemoteObject.unexportObject(this, true);
				stub = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
