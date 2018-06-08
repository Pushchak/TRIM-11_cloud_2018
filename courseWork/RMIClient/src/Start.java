
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Start {

	private final GetCommand getCommand;
	private final Registry registry;
	private final ICompute remoteICompute;

	public static boolean workerFlag = true;

	public Start() throws Exception {
		registry = LocateRegistry.getRegistry(ICompute.PORT);
		remoteICompute = (ICompute) registry.lookup(ICompute.SERVER_NAME);
		getCommand = new GetCommand(remoteICompute);
	}

	public void waitCommand() {
		System.out.println("You are connected to rmi.server.ua");

		try (Scanner scanner = new Scanner(System.in)) {
			while (workerFlag) {
				System.out.print(">");
				getCommand.getCommand(scanner.nextLine());
			}

		}
	}
}
