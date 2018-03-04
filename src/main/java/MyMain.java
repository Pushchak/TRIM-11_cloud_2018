import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MyMain {

	public static void main(String[] args) throws IOException {
		System.out.println("Hello World");
		System.out.println("Enter the command:");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		Console_int cons = new Console_int(br.readLine());
        cons.Print();
	}

}
