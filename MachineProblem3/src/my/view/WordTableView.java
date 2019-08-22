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
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import my.utility.UserInputHelper;

/*
 * TODO LIST
 * FINISHED count the number of each character occurence and display it on the table using ASCII table.
 * FINISHED count the number of visible characters
 * FINISHED count the number of invisible characters
 * FINISHED count the total number of visible and invisible characters
 * count the number of embedded search words
 */

public class WordTableView {
	/*
	 * PRINTING METHODS
	 */
	public static void printToConsole(WordTable dataTable) throws IOException {
		/*
		 * Initialize Some variables
		 */
		int totalVisi = countVisibleCharacters(dataTable.getUserWord());
		int totalInvi = countInvisibleCharacters(dataTable.getUserWord());
		int totalEmbdWord = countEmbeddedWord(dataTable.getUserWord(), dataTable.getEmbeddedWord());
		int charFrequency[] = new int[dataTable.getUserWord().length()];
		
		String[] inviCharWorded = new String[35]; // 0 - 32 && 127 && 255 ASCII
		char[] extendedAsciiArr = initializeExtendedAsciiArr(); // 127 - 254
		char userWordAsCharArray[] = countCharOccurances(dataTable.getUserWord(), charFrequency);		
		
		initializeInviCharArr(inviCharWorded);
		
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
					printCharOccurances(symbol,userWordAsCharArray, charFrequency);
				}
				else {
					System.out.println("0");
				}
			}
			//Extended Ascii
			else if(i > 127 && i < 255) {
				System.out.print(i+"\t"+ extendedAsciiArr[i-128] +"\t\t\t");
				if(hasSymbol(Character.toString(extendedAsciiArr[i-128]), dataTable.getUserWord())) {
					printCharOccurances(extendedAsciiArr[i-128],userWordAsCharArray, charFrequency);
				}
				else {
					System.out.println("0");
				}
			}
			//Standard Printable Char
			else {
				System.out.print(i+"\t"+ symbol +"\t\t\t");
				if(hasSymbol(Character.toString(symbol), dataTable.getUserWord())) {
					printCharOccurances(symbol,userWordAsCharArray, charFrequency);
				}
				else {
					System.out.println("0");
				}
			}
		}
		//END OF FOR LOOP PRINTING
		System.out.println("Summary:");
		System.out.println("Total visible characters: " + totalVisi);
		System.out.println("Total invisible characters: " + totalInvi);
		System.out.println("Total number of characters: " + countVisibleInvisibleCharacters(totalVisi, totalInvi));
		System.out.println("No. of embedded searched word: " + totalEmbdWord);
	}
	
	public static void printToPDF(WordTable dataTable) throws DocumentException, IOException {
		/*
		 * Some variables
		 */
		final String typeLinux = "Linux";
		String osType = System.getProperty("os.name");
		String docLoc = "";
		String[] inviCharWorded = new String[35]; //Wording out invisible characters based on ASCII Table description
		char[] extendedAsciiArr = initializeExtendedAsciiArr(); // 127 - 254
		int totalVisibleChar = countVisibleCharacters(dataTable.getUserWord());
		int totalInvisibleChar = countInvisibleCharacters(dataTable.getUserWord());
		int totalCharacters = countVisibleInvisibleCharacters(totalVisibleChar, totalInvisibleChar);
		int freqOfEmbeddedWord = countEmbeddedWord(dataTable.getUserWord(), dataTable.getEmbeddedWord());
		int freqOfCharacter[] = new int[dataTable.getUserWord().length()];
		
		countCharOccurances(dataTable.getUserWord(), freqOfCharacter);
		initializeInviCharArr(inviCharWorded);
		/*
		 * Initialize the PDF
		 */
		Document asciiDoc = new Document();
		if(osType.contains(typeLinux)) {
			docLoc = "/home/marcelo/Documents/asciiTable.pdf"; //Me programming on my linux laptop
		}
		else {
			docLoc = "E:\\asciiTable.pdf"; //TODO Change loc @ School
		}
		PdfWriter.getInstance(asciiDoc, 
				new FileOutputStream(docLoc));
		
		asciiDoc.open();
		/*
		 * COLUMN IDENTIFIERS && PDF APPEARANCE
		 */
		
		PdfPTable asciiTable = new PdfPTable(3); //Specifies to have 3 COLUMNS
		
		Font headerFont = FontFactory.getFont(FontFactory.TIMES,18,BaseColor.BLACK);
		Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD,24,BaseColor.BLACK);
		Font subTitleFont = FontFactory.getFont(FontFactory.COURIER,19,BaseColor.DARK_GRAY);
		
		Paragraph deciCodeParagraph = new Paragraph("Decimal Code",headerFont);
		Paragraph charSymbolParagrapj = new Paragraph("Character",headerFont);
		Paragraph summaryTextParagraph = new Paragraph("Summary:");
		Paragraph occNumParagraph = new Paragraph("Occurances", headerFont);
		Paragraph pdfTitleParagraph = new Paragraph("Magday's ASCII Table",titleFont);
		Paragraph totalVisibleCharParagraph = new Paragraph("Total visible characters: "
				+ Integer.toString(totalVisibleChar));
		Paragraph totalInvisibleCharParagraph = new Paragraph("Total invisible characters: "
				+ Integer.toString(totalInvisibleChar));
		Paragraph totalCharactersParagraph = new Paragraph("Total number of characters: "
				+ Integer.toString(totalCharacters));
		Paragraph freqOfEmbeddedWordParagraph = new Paragraph("Total occurances of embedded word: "
				+ Integer.toString(freqOfEmbeddedWord));
		PdfPCell deciCodeIdentifier = new PdfPCell(deciCodeParagraph);
		PdfPCell charSymbolIdentifier = new PdfPCell(charSymbolParagrapj);
		PdfPCell occNumIdentifier = new PdfPCell(occNumParagraph);
		
		asciiTable.setHeaderRows(1);
		asciiTable.setSpacingBefore(20f);
		
		pdfTitleParagraph.setAlignment(Element.ALIGN_CENTER);
		
		summaryTextParagraph.setIndentationLeft(50f);
		totalVisibleCharParagraph.setIndentationLeft(50f);
		totalInvisibleCharParagraph.setIndentationLeft(50f);
		totalCharactersParagraph.setIndentationLeft(50f);
		freqOfEmbeddedWordParagraph.setIndentationLeft(50f);
		
		asciiDoc.add(pdfTitleParagraph);;
		asciiDoc.add(createParagraphTabbed("User Word: ", dataTable.getUserWord(), subTitleFont));
		asciiDoc.add(createParagraphTabbed("Embedded Word: ", dataTable.getEmbeddedWord(), subTitleFont));
		/*
		 * SET ALIGNMENT OF HEADERS
		 */
		deciCodeIdentifier.setHorizontalAlignment(Element.ALIGN_CENTER);
		charSymbolIdentifier.setHorizontalAlignment(Element.ALIGN_CENTER);
		occNumIdentifier.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		/*
		 * ADD CELLS TO TABLE
		 */
		asciiTable.addCell(deciCodeIdentifier).setBackgroundColor(BaseColor.LIGHT_GRAY);
		asciiTable.addCell(charSymbolIdentifier).setBackgroundColor(BaseColor.LIGHT_GRAY);
		asciiTable.addCell(occNumIdentifier).setBackgroundColor(BaseColor.LIGHT_GRAY);
        
		/*
		 * ASCII PRINTING
		 */

		for (int i = 0; i < 256; i++) {
			char symbol = (char) i;
			
			asciiTable.addCell(Integer.toString(i));
			
		} //END OF FOR LOOP
		
		
		asciiDoc.add(asciiTable);
		//Bottom Section of PDF
		asciiDoc.add(summaryTextParagraph);
		asciiDoc.add(totalVisibleCharParagraph);
		asciiDoc.add(totalInvisibleCharParagraph);
		asciiDoc.add(totalCharactersParagraph);
		asciiDoc.add(freqOfEmbeddedWordParagraph);
		
		if(!System.getProperty("os.name").equals(typeLinux)) {
			String imgLoc = "E:\\progger.png"; //Change img loc @ school
			Image img = Image.getInstance(imgLoc);
			img.setAlignment(Element.ALIGN_CENTER);
			asciiDoc.add(img);
		}
		
		
		//FLUSH CONTENTS TO PDF
		asciiDoc.close();
	}
	private static void printCharOccurances(char symbol, char[] usrWord, int freqArr[]) {
		for (int i = 0; i < usrWord.length; i++) {
			if(usrWord[i] ==  symbol) {
				System.out.println(freqArr[i]);
			}
		}
	}
	/*
	 * COUNTING METHODS
	 */
	
	private static int countVisibleCharacters(String userWord) throws IOException {
		char extendedAsciiArr[] = initializeExtendedAsciiArr();
		int total = 0; //ONE POINT OF EXIT
		for(int ctr = 0; ctr < userWord.length();ctr++) {
			/*
			 * If the char at userWord[ctr] is above the invisible letter but under the DEL(128)
			 * Add to total
			 */
			if(userWord.charAt(ctr) < 127 && userWord.charAt(ctr) > 32) {
				total++;
			}
			/*
			 * Check if the char doesn't fall under the standard ASCII 128 Table
			 * If it's greater than 128 but under the Extended ASCII Table, add +1 to total
			 */
			else if (userWord.charAt(ctr) > 127) {
				for (int i = 0; i < extendedAsciiArr.length; i++) {
					if(userWord.charAt(ctr) == extendedAsciiArr[i]) {
						total++;
					}	
				}
			}
		}
		
		return total;
	}
	
	private static int countInvisibleCharacters(String userWord) throws IOException{
		String inviCharWorded[] = new String [35];
		initializeInviCharArr(inviCharWorded);
		int total = 0;
		for(int ctr = 0; ctr < userWord.length();ctr++) {
			if(userWord.charAt(ctr) < 33 || userWord.charAt(ctr) == 127 || userWord.equals(inviCharWorded[34]))
				total+=1;
		}
		
		return total;
	}
	
	private static int countVisibleInvisibleCharacters(int totalVisi, int totalInvi) {
		return totalVisi + totalInvi;
	}
	
	private static char[] countCharOccurances(String usrWord, int freqArr[]) {
		char wordArr[] = usrWord.toCharArray();
		for(int outerCtr = 0; outerCtr < wordArr.length; outerCtr++) {
			freqArr[outerCtr] = 1;
			for(int innerCtr = outerCtr+1; innerCtr < wordArr.length; innerCtr++)
				if(wordArr[outerCtr] == wordArr[innerCtr]) {
					freqArr[outerCtr]+=1;
					wordArr[innerCtr] = (char)0; // Set To null so we dont go back reading the character
				}
		}
		return wordArr;
	}
	
	private static int countEmbeddedWord(String userWord, String embeddedWord) {
		int total = 0;
		/*
		 * How it works
		 * 1.)Subtract the Original length to the length without the embedded word
		 * 2.)Divide the sum to the length of the embedded word
		 * 3.)Quotient is the total removed subString
		 */
		if(!userWord.isEmpty() && !embeddedWord.isEmpty()) {
			total = (userWord.length() - userWord.replace(embeddedWord, "").length()) / embeddedWord.length();
		}
		return total;
	}
	/*
	 * BOOLEAN METHODS
	 */
	private static boolean hasSymbol(String symbol,String userWord) {
		boolean hasSym = userWord.contains(symbol) ? true : false;
		return hasSym;
	}
	/*
	 * OTHERS
	 */
	private static void initializeInviCharArr(String inviChars[]) throws FileNotFoundException, IOException {
		BufferedReader reader = UserInputHelper.getInviAsciiFromFile();//Open File
		for(int ctr = 0; ctr < inviChars.length; ctr++) {
			inviChars[ctr] = reader.readLine(); //Store contents of each line of the file to a String
		}
	}
	
	private static char[] initializeExtendedAsciiArr() throws IOException {
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
		return tempStr.toCharArray();//Throw tempStr
	}
	private static Paragraph createParagraphTabbed(String key, String data,Font font) {
		Paragraph temp = new Paragraph(key,font);
		temp.setIndentationLeft(50f);
		temp.setAlignment(Element.ALIGN_MIDDLE);
		temp.setTabSettings(new TabSettings(100f));
		temp.add(Chunk.TABBING);
		temp.add(data);
		return temp;
	}
}
