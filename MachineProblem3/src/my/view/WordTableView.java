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
	public static void displayAscii(WordTable dataTable) throws IOException {
		/*
		 * Get Data from Model
		 */
		String userWord = dataTable.getUserWord();
		String embeddedWord = dataTable.getEmbeddedWord();
		/*
		 * Initialize Some variables
		 */
		String[] inviCharWorded = new String[34]; // 0 - 32 && 127 ASCII
		int totalVisi = countVisibleCharacters(userWord);
		int totalInvi = countInvisibleCharacters(userWord);
		int totalEmbdWord = countEmbeddedWord(userWord, embeddedWord);
		int charFrequency[] = new int[userWord.length()];
		char userWordArr[] = userWord.toCharArray();
		
		charOccurances(userWordArr, charFrequency);
		initializeInviCharArr(inviCharWorded);
		
		//Top of the Console
		System.out.println("\tMagday's ASCII Table\n");
		System.out.println("User Word: \t" + dataTable.getUserWord());
		System.out.println("Embedded Word: \t" + dataTable.getEmbeddedWord());
		System.out.println("\nDEC\tChar\t\t\tOCC");
		
		/*
		 * Printing ASCII table to Console
		 */
		for (int ctr = 0; ctr <=255; ctr++) {
			char symbol = (char) ctr;
			
			//Wording out Invi Chars
			if(ctr < 33) {//Invisible Chars
				System.out.print(ctr+"\t"+inviCharWorded[ctr]+"\t");
				if(containsChar(symbol, userWordArr)) {
					printCharOccurances(symbol, userWordArr, charFrequency);
				}
				else {
					System.out.println("0"); // userWord doesn't contain any invi character until 32
				}
			}
			else if (ctr == 127) { //DEL ASCII
				System.out.print(ctr+"\t"+inviCharWorded[33]+"\t\t\t");
				if(containsChar(symbol, userWordArr)) {
					printCharOccurances(symbol, userWordArr, charFrequency);
				}
				else {
					System.out.println("0");
				}
			}
			else { //Visible Chars
				System.out.print(ctr+"\t"+symbol+"\t\t\t");
				if(containsChar(symbol, userWordArr)) {
					printCharOccurances(symbol, userWordArr, charFrequency);
				}
				else {
					System.out.println("0");
				}
			}
			//pause on ASCII 127 (press any key resumes from 128 to 255)
			if(ctr == 127) {
				UserInputHelper.pressToContinue();
			}
		
		} //END OF FOR LOOP PRINTING
		
		System.out.println("Total visible characters: " + totalVisi);
		System.out.println("Total invisible characters: " + totalInvi);
		System.out.println("Total number of characters: " + countVisibleInvisibleCharacters(totalVisi, totalInvi));
		System.out.println("No. of embedded searched word: " + totalEmbdWord);
	}
	
	public static void printCharOccurances(char currentSymbol,char strArr[], int freqArr[]) {
		for(int i = 0; i < strArr.length; i++) {
			if(strArr[i] == currentSymbol) {
				System.out.println(freqArr[i]);
			}
		}
	}
	
	public static void printToPDF(WordTable dataTable) throws DocumentException, IOException {
		/*
		 * Some variables
		 */
		char userWordArr[] = dataTable.getUserWord().toCharArray();
		int frequencyArr[] = new int[userWordArr.length];
		int totalVisibleChar = countVisibleCharacters(dataTable.getUserWord());
		int totalInvisibleChar = countInvisibleCharacters(dataTable.getUserWord());
		int totalCharacters = countVisibleInvisibleCharacters(totalVisibleChar, totalInvisibleChar);
		int freqOfEmbeddedWord = countEmbeddedWord(dataTable.getUserWord(), dataTable.getEmbeddedWord());
		String[] inviCharWorded = new String[34]; //Wording out invisible characters based on ASCII Table description
		
		charOccurances(userWordArr, frequencyArr);
		initializeInviCharArr(inviCharWorded);
		/*
		 * Initialize the PDF
		 */
		Document asciiDoc = new Document(); 
		PdfWriter.getInstance(asciiDoc, 
				new FileOutputStream("E:\\ASCIITable1.pdf"));
		
		asciiDoc.open();
		
		PdfPTable asciiTable = new PdfPTable(3); //Specifies to have 3 COLUMNS
		
		/*
		 * COLUMN IDENTIFIERS && PDF APPEARANCE
		 */
		Font headerFont = FontFactory.getFont(FontFactory.TIMES,18,BaseColor.BLACK);
		Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD,24,BaseColor.BLACK);
		Font subTitleFont = FontFactory.getFont(FontFactory.COURIER,19,BaseColor.DARK_GRAY);
		
		Paragraph deciCodeParagraph = new Paragraph("Decimal Code",headerFont);
		Paragraph charSymbolParagrapj = new Paragraph("Character",headerFont);
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
			
			if(i < 33) {
				asciiTable.addCell(inviCharWorded[i]);
			}
			else if(i == 127) {
				asciiTable.addCell(inviCharWorded[33]); // last element of the worded array
			}
			else {
				asciiTable.addCell(Character.toString(symbol));
			}
			
			if(containsChar(symbol, userWordArr)) {
				for(int k = 0; k < userWordArr.length; k++) {
					if(userWordArr[k] == symbol) {
						asciiTable.addCell(Integer.toString(frequencyArr[k]));
					}
				}
				
			}else {
				asciiTable.addCell("0");
			}
		} //END OF FOR LOOP
		
		
		asciiDoc.add(asciiTable);
		//Bottom Section of PDF
		asciiDoc.add(totalVisibleCharParagraph);
		asciiDoc.add(totalInvisibleCharParagraph);
		asciiDoc.add(totalCharactersParagraph);
		asciiDoc.add(freqOfEmbeddedWordParagraph);
		
		String imgLoc = "E:\\progger.png"; //Change @school
		Image img = Image.getInstance(imgLoc);
		img.setAlignment(Element.ALIGN_CENTER);
		asciiDoc.add(img);
		
		//FLUSH CONTENTS TO PDF
		asciiDoc.close();
	}
	/*
	 * COUNTING METHODS
	 */
	
	private static int countVisibleCharacters(String userWord) {
		char userCharArr[] = userWord.toCharArray();
		int total = 0; //ONE POINT OF EXIT
		for(int ctr = 0; ctr < userCharArr.length;ctr++) {
			if(userCharArr[ctr] < 127 && userCharArr[ctr] > 32)
				total+=1;
		}
		
		return total;
	}
	
	private static int countInvisibleCharacters(String userWord) {
		char userCharArr[] = userWord.toCharArray();
		int total = 0;
		for(int ctr = 0; ctr < userCharArr.length;ctr++) {
			if(userCharArr[ctr] < 33 || userCharArr[ctr] == 127)
				total+=1;
		}
		
		return total;
	}
	
	private static int countVisibleInvisibleCharacters(int totalVisi, int totalInvi) {
		return totalVisi + totalInvi;
	}
	
	private static void charOccurances(char wordArr[], int freqArr[]) {
		for(int outerCtr = 0; outerCtr < wordArr.length; outerCtr++) {
			freqArr[outerCtr] = 1;
			for(int innerCtr = outerCtr+1; innerCtr < wordArr.length; innerCtr++)
				if(wordArr[outerCtr] == wordArr[innerCtr]) {
					freqArr[outerCtr]+=1;
					wordArr[innerCtr] = (char) 0; // Set To null so we dont go back reading the character
				}
		}
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
	private static boolean containsChar(char symbol, char strArr[]) {
		boolean hasChar = false;
		if(symbol != (char) 0) {
			for(int i = 0; i < strArr.length; i++) {
				if(symbol == strArr[i]) {
					hasChar = true;
				}
			}
		}
		return hasChar;
	}
	
	/*
	 * OTHERS
	 */
	private static void initializeInviCharArr(String inviChars[]) throws FileNotFoundException, IOException {
		BufferedReader reader = UserInputHelper.getFileReader();
		for(int ctr = 0; ctr < inviChars.length; ctr++) {
			inviChars[ctr] = reader.readLine();
		}
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
