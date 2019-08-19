package my.controller;

import java.io.IOException;

import com.itextpdf.text.DocumentException;

import my.model.WordTable;
import my.view.WordTableView;
public class WordTableController {

	public static void main(String[] args) throws IOException, DocumentException {
		WordTable myTableData = new WordTable();
		
		myTableData.setUserWord(args[0]);
		myTableData.setEmbeddedWord(args[1]);
		WordTableView.printToPDF(myTableData);
		WordTableView.displayAscii(myTableData);
		
	}
}
