package Seminar02.ClientTCP;



public interface Parser {
	byte[] parseRequest(String userInput);
	String parseResponse(String userCommand, byte[] responseContent);
}
