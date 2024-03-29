package my.utility;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class UserInputHelper {
	private static BufferedReader getReader(){
		return new BufferedReader(new InputStreamReader(System.in));
	}
	
	public static boolean isWindowsSystem() {
		final String osWindows = "Windows";
		boolean isWindows = false;
		if(System.getProperty("os.name").contains(osWindows)) {
			isWindows = true;
		}
		return isWindows;
	}
	
	public static BufferedReader getInviAsciiFromFile() throws FileNotFoundException {
		String inviAsciiLoc = "/home/marcelo/git/MachineProb3/MachineProblem3/src/my/files/invisibleAscii.txt"; //TODO Change BufferedReader Loc for invisible ASCII
		if(isWindowsSystem()) {
			inviAsciiLoc = "E:\\invisibleAscii.txt";
		}
		return new BufferedReader(new FileReader(inviAsciiLoc));
	}

	public static BufferedReader getExtendedAsciiFromFile() throws FileNotFoundException, UnsupportedEncodingException{
		String extendedAsciiLoc = "/home/marcelo/git/MachineProb3/MachineProblem3/src/my/files/extendedAscii.txt";//TODO Change BufferedReader loc for Extended Ascii
		if(isWindowsSystem()) {
			extendedAsciiLoc = "E:\\extendedAscii.txt";
		}
		return new BufferedReader(new InputStreamReader(new FileInputStream(extendedAsciiLoc),StandardCharsets.UTF_8.name()));
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
		System.out.println("Press ||ANY KEY|| THEN PRESS ||ENTER|| to continue!");
		getReader().readLine();
	}
}
