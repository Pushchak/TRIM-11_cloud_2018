import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ICompute extends Remote {

	public static final int PORT = 4321;
	public static final String SERVER_NAME = "rmi.server.ua";

	public <T> T executeTask(ITask<T> t) throws IOException, RemoteException;

	public String echo(String text) throws RemoteException;

	public String ping() throws RemoteException;

	public static class FileInstance implements Serializable {

		private static final long serialVersionUID = 229L;
		private byte[] fileContent;
		private String filename;

		public String getFilename() {
			return filename;
		}

		public FileInstance(File file) throws IOException {
			fileContent = Files.readAllBytes(file.toPath());
			filename = file.getName();
		}

		public byte[] getFileContent() {
			return fileContent;
		}
	}

	public class SearchAlgorithm implements ITask<int[]>, Serializable {

		private static final long serialVersionUID = 227L;

		private final ICompute.FileInstance fileInstance;

		private int value;

		public SearchAlgorithm(FileInstance fileInstance, int value) throws IOException {
			this.fileInstance = fileInstance;
			this.value = value;
		}

		@Override
		public int[] execute() throws IOException {
			String fileContent = new String(fileInstance.getFileContent(), StandardCharsets.UTF_8).trim();
	        String[] proxyArray = (fileContent.equals("")) ? new String[]{} : fileContent.split(" ");
	        int[] numbers;
	        if (proxyArray.length == 0) {
	            numbers = new int[]{};
	        } else {
	            numbers = new int[proxyArray.length];
	            for (int i = 0; i < proxyArray.length; i++) {
	                numbers[i] = Integer.parseInt(proxyArray[i]);
	            }
	        }
			return search(numbers);
		}

		private int[] search(int[] numbers) {
	        int size = numbers.length;
	        int[] returned;
	        ArrayList<Integer> returnArray = new ArrayList<Integer>();
	        for (int i = 0; i < size; i++) {
	        	if(numbers[i] == value)
	        		returnArray.add(i);
	        }
	        
	        if(returnArray.isEmpty()) {
	        	returned = new int [] {-1};
			} else {
				returned = new int[returnArray.size() + 1];
				returned[0] = returnArray.size();
		        for (int i = 0; i < returnArray.size(); i++) {
		        	returned[i+1] = returnArray.get(i).intValue();	
		        }
	        }
	      
			return returned;
	    }
	}
}
