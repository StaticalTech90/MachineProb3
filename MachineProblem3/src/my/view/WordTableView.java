package my.view;

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

import my.bean.WordTableBean;
import my.utility.UserInput;

public class WordTableView {
	/*
	 * PRINTING METHODS
	 */
	public void printToConsole(WordTableBean dataTable) throws IOException {
		/*
		 * Initialize variables
		 */		
		String[] invisibleAscii = dataTable.getInvisibleASCIIWorded(); // 0 - 32 && 127 && 255 ASCII
		String extendedAscii = dataTable.getExtendedASCII(); // 127 - 254
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
					System.out.print(i+"\t"+invisibleAscii[33]+"\t\t\t");
				}else if (i == 255) {
					System.out.print(i+"\t"+invisibleAscii[34]+"\t\t");
				}else {
					System.out.print(i+"\t"+invisibleAscii[i]+"\t");
				}
				if(dataTable.containsCharacter(symbol)) {
					System.out.println(printCharOccurances(dataTable, symbol));
				}
				else {
					System.out.println("0");
				}
				
				if(i == 127) {
					UserInput.pressToContinue();
				}
			}
			//Extended Ascii
			else if(i > 127 && i < 255) {
				System.out.print(i+"\t"+ extendedAscii.charAt(i-128) +"\t\t\t");
				if(dataTable.containsCharacter(extendedAscii.charAt(i-128))) {
					System.out.println(printCharOccurances(dataTable, extendedAscii.charAt(i-128)));
				}
				else {
					System.out.println("0");
				}
			}
			//Standard Printable Char
			else {
				System.out.print(i+"\t"+ symbol +"\t\t\t");
				if(dataTable.containsCharacter(symbol)) {
					System.out.println(printCharOccurances(dataTable, symbol));
				}
				else {
					System.out.println("0");
				}
			}
		}		//END OF FOR LOOP PRINTING
		System.out.println("Summary:");
		System.out.println("Total visible characters: " + dataTable.getTotalVisibleCharacters());
		System.out.println("Total invisible characters: " + dataTable.getTotalInvisibleCharacters());
		System.out.println("Total number of characters: " + dataTable.getTotalCharacters());
		System.out.println("No. of embedded searched word: " + dataTable.getOccurancesEmbeddedWord());
	}
	
	public void printToPDF(WordTableBean dataTable) throws DocumentException, IOException {
//		ASCII RELATED VARIABLES
		String docLoc = "/home/marcelo/Documents/asciiTable.pdf"; // DEFAULT IS LINUX
		String[] invisibleAsciiWorded = dataTable.getInvisibleASCIIWorded(); // 0 - 32 && 127 && 255 ASCII
		String extendedAscii = dataTable.getExtendedASCII(); // 127 - 254
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
				+ Integer.toString(dataTable.getTotalVisibleCharacters()));
		Paragraph totalInvisibleCharParagraph = new Paragraph("Total invisible characters: "
				+ Integer.toString(dataTable.getTotalInvisibleCharacters()));
		Paragraph totalCharactersParagraph = new Paragraph("Total number of characters: "
				+ Integer.toString(dataTable.getTotalCharacters()));
		Paragraph freqOfEmbeddedWordParagraph = new Paragraph("Total occurances of embedded word: "
				+ Integer.toString(dataTable.getOccurancesEmbeddedWord()));
		
		PdfPCell deciCodeIdentifier = new PdfPCell(deciCodeParagraph);
		PdfPCell charSymbolIdentifier = new PdfPCell(charSymbolParagrapj);
		PdfPCell occNumIdentifier = new PdfPCell(occNumParagraph);
//		Initialize PDF
		Document asciiDoc = new Document();
		if(UserInput.isWindowsSystem()) {
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
					asciiTable.addCell(invisibleAsciiWorded[33]);
				}else if (i == 255) {
					asciiTable.addCell(invisibleAsciiWorded[34]);
				}else {
					asciiTable.addCell(invisibleAsciiWorded[i]);
				}
				if(dataTable.containsCharacter(symbol)) {
					asciiTable.addCell(printCharOccurances(dataTable, symbol));
				}
				else {
					asciiTable.addCell("0");
				}
			}
			//Extended Ascii
			else if(i > 127 && i < 255) {
				asciiTable.addCell(new Paragraph(Character.toString(extendedAscii.charAt(i-128)),cellSymbolFont));
				
				if(dataTable.containsCharacter(extendedAscii.charAt(i-128))) {
					asciiTable.addCell(printCharOccurances(dataTable, extendedAscii.charAt(i-128)));;
				}
				else {
					asciiTable.addCell("0");
				}
			}
			//Standard Printable Char
			else {
				asciiTable.addCell(Character.toString(symbol));
				if(dataTable.containsCharacter(symbol)) {
					asciiTable.addCell(printCharOccurances(dataTable, symbol));;
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
		
		if(UserInput.isWindowsSystem()) {
			String imgLoc = "E:\\progger.png"; //Change img loc @ school
			Image img = Image.getInstance(imgLoc);
			img.setAlignment(Element.ALIGN_CENTER);
			asciiDoc.add(img);
		}
		
		
		//FLUSH CONTENTS TO PDF
		asciiDoc.close();
	}
	
	private String printCharOccurances(WordTableBean dataTable, char symbol) {
		String temp = "";
		for (int ctr = 0; ctr < dataTable.getOccuranceKey().length(); ctr++) {
			if(dataTable.getOccuranceKey().charAt(ctr) ==  symbol) {
				temp=Integer.toString(dataTable.getOccuranceTable()[ctr]);
			}
		}
		return temp;
	}
	
	private Paragraph createTabbedParagraph(String key, String data,Font font) {
		Paragraph temp = new Paragraph(key,font);
		temp.setIndentationLeft(50f);
		temp.setAlignment(Element.ALIGN_MIDDLE);
		temp.setTabSettings(new TabSettings(100f));
		temp.add(Chunk.TABBING);
		temp.add(data);
		return temp;
	}
	
}
