package Seminar02.Client_TCP;



public interface Parser {
	byte[] parseRequest(String userInput);
	String parseResponse(String userCommand, byte[] responseContent);
}
