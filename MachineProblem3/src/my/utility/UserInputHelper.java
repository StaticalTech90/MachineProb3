package my.utility;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UserInputHelper {
	private static BufferedReader getReader(){
		return new BufferedReader(new InputStreamReader(System.in));
	}
	
	public static BufferedReader getFileReader() throws FileNotFoundException {
		return new BufferedReader(new FileReader("E:\\intializeAscii.txt"));
	}
	public static String readString(String msg) throws IOException{
		System.out.print(msg);
		return getReader().readLine();
	}
	
	public static int readInt(String msg) throws IOException{
		System.out.print(msg);
		return Integer.parseInt(getReader().readLine());
	}
	public static double readDouble(String msg) throws IOException{
		System.out.print(msg);
		return Double.parseDouble(getReader().readLine());
	}
	
	public static void pressToContinue() throws IOException {
		System.out.println("Press ||ENTER|| to continue!");
		getReader().readLine();
	}
}
