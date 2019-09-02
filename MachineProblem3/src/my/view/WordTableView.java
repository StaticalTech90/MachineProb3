package my.view;

import my.model.WordTable;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.TabSettings;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import my.utility.UserInputHelper;

public class WordTableView {
	/*
	 * PRINTING METHODS
	 */
	public static void printToConsole(WordTable dataTable) throws IOException {
		/*
		 * Initialize variables
		 */
		int charFrequency[] = buildCharOccurancesList(dataTable.getUserWord());
		
		String[] inviCharWorded = getWordedInvisibleASCIICharacters(); // 0 - 32 && 127 && 255 ASCII
		String extendedAsciiString = getExtendedASCIICharacters(); // 127 - 254
		String keyForCharacterOccurances = buildCharOccurancesKey(dataTable.getUserWord());//used to get the frequency of a character
		//Top of the Console
		System.out.println("\tMagday's ASCII Table\n");
		System.out.println("User Word: \t" + dataTable.getUserWord());
		System.out.println("Embedded Word: \t" + dataTable.getEmbeddedWord());
		System.out.println("\nDEC\tChar\t\t\tOCC");
		/*
		 * Printing ASCII table to Console
		 */
		for (int i = 0; i < 256; i++) {
			char symbol = (char)i;
			//Print out the invisible chars from 0 - 32 && chars 127 and 255
			if(i < 33 || i == 127 || i == 255) {
				if(i == 127) {
					System.out.print(i+"\t"+inviCharWorded[33]+"\t\t\t");
				}else if (i == 255) {
					System.out.print(i+"\t"+inviCharWorded[34]+"\t\t");
				}else {
					System.out.print(i+"\t"+inviCharWorded[i]+"\t");
				}
				if(hasSymbol(Character.toString(symbol), dataTable.getUserWord())) {
					System.out.println(printCharOccurances(symbol,keyForCharacterOccurances, charFrequency));
				}
				else {
					System.out.println("0");
				}
				
				if(i == 127) {
					UserInputHelper.pressToContinue();
				}
			}
			//Extended Ascii
			else if(i > 127 && i < 255) {
				System.out.print(i+"\t"+ extendedAsciiString.charAt(i-128) +"\t\t\t");
				if(hasSymbol(Character.toString(extendedAsciiString.charAt(i-128)), dataTable.getUserWord())) {
					System.out.println(printCharOccurances(symbol,keyForCharacterOccurances, charFrequency));
				}
				else {
					System.out.println("0");
				}
			}
			//Standard Printable Char
			else {
				System.out.print(i+"\t"+ symbol +"\t\t\t");
				if(hasSymbol(Character.toString(symbol), dataTable.getUserWord())) {
					System.out.println(printCharOccurances(symbol,keyForCharacterOccurances, charFrequency));
				}
				else {
					System.out.println("0");
				}
			}
		}		//END OF FOR LOOP PRINTING
		System.out.println("Summary:");
		System.out.println("Total visible characters: " + countVisibleCharacters(dataTable));
		System.out.println("Total invisible characters: " + countInvisibleCharacters(dataTable));
		System.out.println("Total number of characters: " + countTotalCharacters(dataTable));
		System.out.println("No. of embedded searched word: " + countEmbeddedWord(dataTable));
	}
	
	public static void printToPDF(WordTable dataTable) throws DocumentException, IOException {
//		ASCII RELATED VARIABLES
		String docLoc = "/home/marcelo/Documents/asciiTable.pdf"; // DEFAULT IS LINUX
		String[] inviCharWorded = getWordedInvisibleASCIICharacters(); // 0 - 32 && 127 && 255 ASCII
		String extendedAsciiString = getExtendedASCIICharacters(); // 127 - 254
		String keyForCharacterOccurances = buildCharOccurancesKey(dataTable.getUserWord());//used to get the frequency of a character
		int charOccurances[] = buildCharOccurancesList(dataTable.getUserWord());
//		PDF VARIABLES
		String arialFontLoc = "C://Windows//Fonts//Arial.ttf";
		PdfPTable asciiTable = new PdfPTable(3); //Specifies to have 3 COLUMNS
//		FONTS
		BaseFont arialBaseFont = BaseFont.createFont(arialFontLoc,BaseFont.IDENTITY_H,BaseFont.EMBEDDED);
		Font headerFont = FontFactory.getFont(FontFactory.TIMES,18,BaseColor.BLACK);
		Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD,24,BaseColor.BLACK);
		Font subTitleFont = FontFactory.getFont(FontFactory.COURIER,19,BaseColor.DARK_GRAY);		
		Font cellSymbolFont = new Font(arialBaseFont);
//		HEADER PARAGRAPHS
		Paragraph deciCodeParagraph = new Paragraph("Decimal Code",headerFont);
		Paragraph charSymbolParagrapj = new Paragraph("Character",headerFont);
		Paragraph summaryTextParagraph = new Paragraph("Summary:");
		Paragraph occNumParagraph = new Paragraph("Occurances", headerFont);
		
		Paragraph pdfTitleParagraph = new Paragraph("Magday's ASCII Table",titleFont);
		Paragraph totalVisibleCharParagraph = new Paragraph("Total visible characters: "
				+ Integer.toString(countVisibleCharacters(dataTable)));
		Paragraph totalInvisibleCharParagraph = new Paragraph("Total invisible characters: "
				+ Integer.toString(countInvisibleCharacters(dataTable)));
		Paragraph totalCharactersParagraph = new Paragraph("Total number of characters: "
				+ Integer.toString(countTotalCharacters(dataTable)));
		Paragraph freqOfEmbeddedWordParagraph = new Paragraph("Total occurances of embedded word: "
				+ Integer.toString(countEmbeddedWord(dataTable)));
		
		PdfPCell deciCodeIdentifier = new PdfPCell(deciCodeParagraph);
		PdfPCell charSymbolIdentifier = new PdfPCell(charSymbolParagrapj);
		PdfPCell occNumIdentifier = new PdfPCell(occNumParagraph);
//		Initialize PDF
		Document asciiDoc = new Document();
		if(UserInputHelper.isWindowsSystem()) {
			docLoc = "E:\\asciiTable.pdf"; //TODO Change loc @ School
		}

		PdfWriter.getInstance(asciiDoc, 
				new FileOutputStream(docLoc));
		
		asciiDoc.open();
		
//		PDF APPEARANCE
		asciiTable.setHeaderRows(1);
		asciiTable.setSpacingBefore(20f);
		
		pdfTitleParagraph.setAlignment(Element.ALIGN_CENTER);
		
		summaryTextParagraph.setIndentationLeft(50f);
		totalVisibleCharParagraph.setIndentationLeft(50f);
		totalInvisibleCharParagraph.setIndentationLeft(50f);
		totalCharactersParagraph.setIndentationLeft(50f);
		freqOfEmbeddedWordParagraph.setIndentationLeft(50f);
		
		asciiDoc.add(pdfTitleParagraph);;
		asciiDoc.add(createTabbedParagraph("User Word: ", dataTable.getUserWord(), subTitleFont));
		asciiDoc.add(createTabbedParagraph("Embedded Word: ", dataTable.getEmbeddedWord(), subTitleFont));
		
//		SET ALIGNMENT OF HEADERS
		deciCodeIdentifier.setHorizontalAlignment(Element.ALIGN_CENTER);
		charSymbolIdentifier.setHorizontalAlignment(Element.ALIGN_CENTER);
		occNumIdentifier.setHorizontalAlignment(Element.ALIGN_CENTER);
		
//		ADD CELLS TO TABLE
		asciiTable.addCell(deciCodeIdentifier).setBackgroundColor(BaseColor.LIGHT_GRAY);
		asciiTable.addCell(charSymbolIdentifier).setBackgroundColor(BaseColor.LIGHT_GRAY);
		asciiTable.addCell(occNumIdentifier).setBackgroundColor(BaseColor.LIGHT_GRAY);
        
		for (int i = 0; i < 256; i++) {
			char symbol = (char) i;
			
			asciiTable.addCell(Integer.toString(i));			
			//Print out the invisible chars from 0 - 32 && chars 127 and 255
			if(i < 33 || i == 127 || i == 255) {
				if(i == 127) {
					asciiTable.addCell(inviCharWorded[33]);
				}else if (i == 255) {
					asciiTable.addCell(inviCharWorded[34]);
				}else {
					asciiTable.addCell(inviCharWorded[i]);
				}
				if(hasSymbol(Character.toString(symbol), dataTable.getUserWord())) {
					asciiTable.addCell(printCharOccurances(symbol,keyForCharacterOccurances, charOccurances));
				}
				else {
					asciiTable.addCell("0");
				}
			}
			//Extended Ascii
			else if(i > 127 && i < 255) {
				asciiTable.addCell(new Paragraph(Character.toString(extendedAsciiString.charAt(i-128)),cellSymbolFont));
				
				if(hasSymbol(Character.toString(extendedAsciiString.charAt(i-128)), dataTable.getUserWord())) {
					asciiTable.addCell(printCharOccurances(extendedAsciiString.charAt(i-128),keyForCharacterOccurances, charOccurances));;
				}
				else {
					asciiTable.addCell("0");
				}
			}
			//Standard Printable Char
			else {
				asciiTable.addCell(Character.toString(symbol));
				if(hasSymbol(Character.toString(symbol), dataTable.getUserWord())) {
					asciiTable.addCell(printCharOccurances(symbol,keyForCharacterOccurances, charOccurances));;
				}
				else {
					asciiTable.addCell("0");
				}
			}
			
		} //END OF FOR LOOP
		
		
		asciiDoc.add(asciiTable);
		//Bottom Section of PDF
		asciiDoc.add(summaryTextParagraph);
		asciiDoc.add(totalVisibleCharParagraph);
		asciiDoc.add(totalInvisibleCharParagraph);
		asciiDoc.add(totalCharactersParagraph);
		asciiDoc.add(freqOfEmbeddedWordParagraph);
		
		if(UserInputHelper.isWindowsSystem()) {
			String imgLoc = "E:\\progger.png"; //Change img loc @ school
			Image img = Image.getInstance(imgLoc);
			img.setAlignment(Element.ALIGN_CENTER);
			asciiDoc.add(img);
		}
		
		
		//FLUSH CONTENTS TO PDF
		asciiDoc.close();
	}

	private static int countVisibleCharacters(WordTable temp) throws IOException {
		String extendedAsciiString = getExtendedASCIICharacters();
		int total = 0; //ONE POINT OF EXIT
		for(int ctr = 0; ctr < temp.getUserWord().length();ctr++) {
			/*
			 * If the char at userWord[ctr] is above the invisible letter but under the DEL(128)
			 * Add to total
			 */
			if(temp.getUserWord().charAt(ctr) < 127 && temp.getUserWord().charAt(ctr) > 32) {
				total++;
			}
			/*
			 * Check if the char doesn't fall under the standard ASCII 128 Table
			 * If it's greater than 128 but under the Extended ASCII Table, add +1 to total
			 */
			else if (temp.getUserWord().charAt(ctr) > 127) {
				for (int i = 0; i < extendedAsciiString.length(); i++) {
					if(temp.getUserWord().charAt(ctr) == extendedAsciiString.charAt(i)) {
						total++;
					}	
				}
			}
		}
		
		return total;
	}
	
	private static int countInvisibleCharacters(WordTable temp) throws IOException{
		int total = 0;
		for(int ctr = 0; ctr < temp.getUserWord().length();ctr++) {
			if(temp.getUserWord().charAt(ctr) < 33 || temp.getUserWord().charAt(ctr) == 127 || temp.getUserWord().equals(Character.toString((char)0x00A0)))
				total+=1;
		}
		
		return total;
	}
	
	private static int countTotalCharacters(WordTable temp) throws IOException {
		return countVisibleCharacters(temp) + countInvisibleCharacters(temp);
	}
	
	private static int countEmbeddedWord(WordTable temp) {
		int total = 0;
		/*
		 * How it works
		 * 1.)Subtract the Original length to the length without the embedded word
		 * 2.)Divide the sum to the length of the embedded word
		 * 3.)Quotient is the total removed subString
		 */
		if(!temp.getUserWord().isEmpty() && !temp.getEmbeddedWord().isEmpty()) {
			total = (temp.getUserWord().length()- temp.getUserWord().replace(temp.getEmbeddedWord(), "").length()) / temp.getEmbeddedWord().length();
		}
		return total;
	}
	
	private static int countTotalLinesofInvisibleAsciiTextFile() throws FileNotFoundException, IOException {
		BufferedReader reader = UserInputHelper.getInviAsciiFromFile();
		int total = 0;
		for(String tmp = ""; tmp != null ; tmp = reader.readLine()) {
			total+=1;
		}
		return total;
	}
	
	private static String buildCharOccurancesKey(String usrWord) {
		/*
		 * Strings are immutable therefore in order to get rid of the extra characters I need to store the string as a character array
		 * The Outer Loop is for the element at X
		 * The Inner Loop is comparing for the Elements at X+1
		 * Should the Inner Loop find X and X+1 Equal then convert the char at X+1 to null or (char) 0
		 */
		char usrWordArr[] = usrWord.toCharArray();
		for (int outerCtr = 0; outerCtr < usrWordArr.length; outerCtr++) {
			for (int innerCtr = outerCtr+1; innerCtr < usrWordArr.length; innerCtr++) {
				if(usrWordArr[outerCtr] == usrWordArr[innerCtr]) {
					usrWordArr[innerCtr] = (char) 0; //Puts an empty character on the String
				}
			}
		}
		usrWord = String.copyValueOf(usrWordArr);
		return usrWord;
	}
	
	private static int[] buildCharOccurancesList(String usrWord) {
		/*
		 * Strings are immutable therefore in order to get rid of the extra characters I need to store the string as a character array
		 * The Outer Loop is for the element at X
		 * The Inner Loop is comparing for the Elements at X+1
		 * Should the Inner Loop find X and X+1 Equal then convert the char at X+1 to null or (char) 0
		 */
		int occuranceKeySize = buildCharOccurancesKey(usrWord).length(); 
		char usrWordArr[] = usrWord.toCharArray();
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
				if(usrWord.charAt(outerCtr) == usrWord.charAt(innerCtr)) {
					tmp[outerCtr] +=1 ;
					usrWordArr[innerCtr] = (char)0; //"Delete" that character
				}
			}
		}
		return tmp;
	}
	
	private static boolean hasSymbol(String symbol,String userWord) {
		boolean hasSym = userWord.contains(symbol) ? true : false;
		return hasSym;
	}
	
	private static String[] getWordedInvisibleASCIICharacters() throws FileNotFoundException, IOException {
		int totalStringsToGet = countTotalLinesofInvisibleAsciiTextFile();
		String tmp[] = new String[totalStringsToGet];
		BufferedReader reader = UserInputHelper.getInviAsciiFromFile();//Open File
		
		for(int i = 0; i < totalStringsToGet; i++) {
			tmp[i] = reader.readLine();
		}
		return tmp;
	}
	
	private static String getExtendedASCIICharacters() throws IOException {
		StringBuilder strBuild = new StringBuilder();//Store the Extended ASCII char to one String
		String tempStr = "";//Used to append to strBuild
		boolean toEnd = false;//Cond to end the loop
		//Get from file
		BufferedReader reader = UserInputHelper.getExtendedAsciiFromFile();
			
		while(!toEnd) {
			tempStr = reader.readLine();//read a line of the text file
			if(tempStr != null) {
				strBuild.append(tempStr);//append the char to strBuild
			}else {
				toEnd = true;
			}
		}
		tempStr = strBuild.toString();//store the built string to tempStr
		return tempStr;//Throw tempStr
	}
	
	private static String printCharOccurances(char symbol, String keyForOccurances, int freqArr[]) {
		String temp = "";
		for (int i = 0; i < keyForOccurances.length(); i++) {
			if(keyForOccurances.charAt(i) ==  symbol) {
				temp=Integer.toString(freqArr[i]);
			}
		}
		return temp;
	}
	
	private static Paragraph createTabbedParagraph(String key, String data,Font font) {
		Paragraph temp = new Paragraph(key,font);
		temp.setIndentationLeft(50f);
		temp.setAlignment(Element.ALIGN_MIDDLE);
		temp.setTabSettings(new TabSettings(100f));
		temp.add(Chunk.TABBING);
		temp.add(data);
		return temp;
	}
	
}
