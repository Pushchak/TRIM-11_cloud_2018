import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		Server server = new Server();
		server.startServer();

		System.out.println("RMI server started, press any key to close server");

		try {
			System.in.read();
			server.closeServer();
		} catch (IOException ioex) {
			ioex.printStackTrace();
		}

		System.out.println(" Server closed");
	}
}
