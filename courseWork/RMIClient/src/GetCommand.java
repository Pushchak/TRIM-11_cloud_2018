public class GetCommand {

	ICompute remoteICompute;
	Parser parser;
	PrepareDataToServer prepareDataToServer;

	public GetCommand(ICompute remoteICompute) {
		this.remoteICompute = remoteICompute;
		parser = new Parser();
		prepareDataToServer = new PrepareDataToServer(remoteICompute);
	}

	public void getCommand(String inLine) {
		try {
			String[] parameters = parser.getParameters(inLine);

			switch (parameters[0]) {

			case "ping":
				prepareDataToServer.ping();
				break;

			case "echo":
				prepareDataToServer.echo(parameters);
				break;

			case "search":
				prepareDataToServer.search(parameters);
				break;

			case "exit":
				prepareDataToServer.exit();
				break;

			default:
				System.out.println("unknown command");
				break;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
