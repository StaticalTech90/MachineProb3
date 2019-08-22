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
	
	public static BufferedReader getInviAsciiFromFile() throws FileNotFoundException {
		final String osLinux = "Linux";
		String inviAsciiLoc = "E:\\invisibleAscii.txt"; //TODO Change BufferedReader Loc for invisible ASCII
		if(System.getProperty("os.name").contains(osLinux)) {
			inviAsciiLoc = "/home/marcelo/git/MachineProb3/MachineProblem3" + 
					"/src/my/files/invisibleAscii.txt";
		}
		return new BufferedReader(new FileReader(inviAsciiLoc));
	}
	public static BufferedReader getExtendedAsciiFromFile() throws FileNotFoundException{
		final String osLinux = "Linux";
		String extendedAsciiLoc = "E:\\extendedAscii.txt";//TODO Change BufferedReader loc for Extended Ascii
		if(System.getProperty("os.name").contains(osLinux)) {
			extendedAsciiLoc = "/home/marcelo/git/MachineProb3/MachineProblem3" + 
					"/src/my/files/extendedAscii.txt";
		}
		return new BufferedReader(new FileReader(extendedAsciiLoc));
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
