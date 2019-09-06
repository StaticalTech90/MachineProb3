package my.bean;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;


import my.utility.UserInput;

public class WordTableBean {
	private String userWord;
	private String embeddedWord;
	private String occuranceKey;
	private String invisibleASCIIWorded[];
	private String extendedASCII;
	private int totalVisibleCharacters;
	private int totalInvisibleCharacters;
	private int totalCharacters;
	private int occurancesEmbeddedWord;	
	private int occuranceTable[];
	/*
	 * Constructor
	 */
	public WordTableBean() {
		this.userWord = "";
		this.embeddedWord = "";
	}
	public WordTableBean(String userWord,String embeddedWord) throws IOException {
		this.userWord = userWord;
		this.embeddedWord = embeddedWord;
	}
	/*
	 * Setter and Getter
	 */
	
	public String getUserWord() {
		return userWord;
	}
	public void setUserWord(String userWord) {
		this.userWord = userWord;
	}
	public String getEmbeddedWord() {
		return embeddedWord;
	}
	public void setEmbeddedWord(String embeddedWord) {
		this.embeddedWord = embeddedWord;
	}
	public String getOccuranceKey() {
		return occuranceKey;
	}
	public String[] getInvisibleASCIIWorded() {
		return invisibleASCIIWorded;
	}
	public String getExtendedASCII() {
		return extendedASCII;
	}
	public int getTotalVisibleCharacters() {
		return totalVisibleCharacters;
	}
	public int getTotalInvisibleCharacters() {
		return totalInvisibleCharacters;
	}
	public int getTotalCharacters() {
		return totalCharacters;
	}
	public int getOccurancesEmbeddedWord() {
		return occurancesEmbeddedWord;
	}
	public int[] getOccuranceTable() {
		return occuranceTable;
	}
	
	/*
	 * Business Logic
	 */
	
	public void computeCharacters() throws IOException {
		this.totalVisibleCharacters = countVisibleCharacters();
		this.totalInvisibleCharacters = countInvisibleCharacters();
		this.totalCharacters = countTotalCharacters();
		this.occurancesEmbeddedWord = countEmbeddedWord();
	}
	
	private int countInvisibleCharacters() throws IOException{
		int total = 0;
		for(int ctr = 0; ctr < this.userWord.length();ctr++) {
			if(this.userWord.charAt(ctr) < 33 || this.userWord.charAt(ctr) == 127 || this.userWord.equals(Character.toString((char)0x00A0)))
				total+=1;
		}
		return total;
	}
	
	private int countVisibleCharacters() throws IOException {
		int total = 0; //ONE POINT OF EXIT
		for(int ctr = 0; ctr <this.userWord.length();ctr++) {
			/*
			 * If the char at userWord[ctr] is above the invisible letter but under the DEL(128)
			 * Add to total
			 */
			if(this.userWord.charAt(ctr) < 127 && this.userWord.charAt(ctr) > 32) {
				total++;
			}
			/*
			 * Check if the char doesn't fall under the standard ASCII 128 Table
			 * If it's greater than 128 but under the Extended ASCII Table, add +1 to total
			 */
			else if (this.userWord.charAt(ctr) > 127) {
				for (int i = 0; i < extendedASCII.length(); i++) {
					if(this.userWord.charAt(ctr) == extendedASCII.charAt(i)) {
						total++;
					}	
				}
			}
		}
		
		return total;
	}
	
	private int countTotalCharacters() throws IOException {
		return countVisibleCharacters() + countInvisibleCharacters();
	}
	
	private int countEmbeddedWord() {
		/*
		 * How it works
		 * 1.)Subtract the Original length to the length without the embedded word
		 * 2.)Divide the sum to the length of the embedded word
		 * 3.)Quotient is the total removed subString
		 */
		return (this.userWord.length()- this.userWord.replace(this.embeddedWord, "").length()) / this.embeddedWord.length();
	}
		
	private int countTotalLinesofInvisibleAsciiTextFile() throws FileNotFoundException, IOException {
		BufferedReader reader = UserInput.getInviAsciiFromFile();
		int total = 0;
		for(String tmp = ""; tmp != null ; tmp = reader.readLine()) {
			total+=1;
		}
		return total;
	}
	
	public void generateOccuranceList() throws UnsupportedEncodingException {
		occuranceKey = this.buildCharOccurancesKey();
		occuranceTable = this.buildCharOccurancesTable();
	}
	
	private String buildCharOccurancesKey() throws UnsupportedEncodingException {
		/*
		 * Strings are immutable therefore in order to get rid of the extra characters I need to store the string as a character array
		 * The Outer Loop is for the element at X
		 * The Inner Loop is comparing for the Elements at X+1
		 * Should the Inner Loop find X and X+1 Equal then convert the char at X+1 to null or (char) 0
		 */
		char usrWordArr[] = userWord.toCharArray();
		for (int outerCtr = 0; outerCtr < usrWordArr.length; outerCtr++) {
			for (int innerCtr = outerCtr+1; innerCtr < usrWordArr.length; innerCtr++) {
				if(usrWordArr[outerCtr] < 128) {
					if(usrWordArr[outerCtr] ==  usrWordArr[innerCtr]) {
						usrWordArr[innerCtr] = (char)0;
					}
				}
				else {
					if(usrWordArr[outerCtr] == usrWordArr[innerCtr]) {
						usrWordArr[innerCtr] = (char)0; //"Delete" that character
					}
				}
			}
		}
		
		return new String(String.copyValueOf(usrWordArr).getBytes(StandardCharsets.UTF_8.name()));
	}
	
	private int[] buildCharOccurancesTable() throws UnsupportedEncodingException {
		/*
		 * Strings are immutable therefore in order to get rid of the extra characters I need to store the string as a character array
		 * The Outer Loop is for the element at X
		 * The Inner Loop is comparing for the Elements at X+1
		 * Should the Inner Loop find X and X+1 Equal then convert the char at X+1 to null or (char) 0
		 */
		int occuranceKeySize = occuranceKey.length(); 
		char usrWordArr[] = userWord.toCharArray();
		int tmp[] = new int[occuranceKeySize]; //Array that contains frequency of characters
		/*
		 * HOW IT WORKS
		 * OuterCtr = Element at X
		 * InnerCtr = Element at X+1
		 * if usrWord charAt X and charAt X+1 are equal, then increment the value of tmp[x] by 1
		 * and also replace the character the charAt innerCtr to null/emptyString
		 */
		for (int outerCtr = 0; outerCtr < occuranceKeySize; outerCtr++) {
			tmp[outerCtr] = 1;
			for(int innerCtr = outerCtr+1; innerCtr < occuranceKeySize; innerCtr++) {
				if(usrWordArr[outerCtr] < 128) {
					if(usrWordArr[outerCtr] ==  usrWordArr[innerCtr]) {
						tmp[outerCtr] +=1 ;
						usrWordArr[innerCtr] = (char)0; //"Delete" that character
					}
				}
				else {
					if(usrWordArr[outerCtr] == usrWordArr[innerCtr]) {
						tmp[outerCtr] +=1 ;
						usrWordArr[innerCtr] = (char)0; //"Delete" that character
					}
				}
				
			}
		}
		return tmp;
	}
	
	public void generateRequiredAscii() throws FileNotFoundException, IOException {
		this.invisibleASCIIWorded = this.readInvisibleAsciiFile();
		this.extendedASCII = this.readExtendedAsciiFile();
	}
	
	private String readExtendedAsciiFile() throws IOException {
		StringBuilder strBuild = new StringBuilder();//Store the Extended ASCII char to one String
		String tempStr = "";//Used to append to strBuild
		boolean toEnd = false;//Cond to end the loop
		//Get from file
		BufferedReader reader = UserInput.getExtendedAsciiFromFile();
			
		while(!toEnd) {
			tempStr = reader.readLine();//read a line of the text file
			if(tempStr != null) {
				strBuild.append(tempStr);//append the char to strBuild
			}else {
				toEnd = true;
			}
		}
		tempStr = new String(strBuild.toString().getBytes(StandardCharsets.UTF_8.name()));//store the built string to tempStr
		return tempStr;//Throw tempStr
	}
	
	private String[] readInvisibleAsciiFile() throws FileNotFoundException, IOException {
		int totalStringsToGet = countTotalLinesofInvisibleAsciiTextFile();
		String tmp[] = new String[totalStringsToGet];
		BufferedReader reader = UserInput.getInviAsciiFromFile();//Open File
		
		for(int i = 0; i < totalStringsToGet; i++) {
			tmp[i] = reader.readLine();
		}
		return tmp;
	}
	
	public boolean containsCharacter(char symbol) {
		boolean hasSym = this.userWord.contains(Character.toString(symbol)) ? true : false;
		return hasSym;
	}
	
}
