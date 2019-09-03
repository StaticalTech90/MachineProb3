package my.controller;

import java.io.IOException;

import com.itextpdf.text.DocumentException;

import my.model.WordTable;
import my.view.WordTableView;

public class ApplicationEntry {
	
	/*
	 * MVC Reference: https://www.tutorialspoint.com/design_pattern/mvc_pattern.htm
	 * And also Sir Mon's samples in the group :)
	 */
	public static void main(String[] args) throws DocumentException, IOException {
		// TODO Auto-generated method stub
		WordTable myTableData = new WordTable();
		WordTableView view = new WordTableView();
		WordTableController controller = new WordTableController(myTableData, view);
		
		if(args.length == 0) {
			System.out.println("NO DATA IS IN THE ARGUMENTS");
		}
		else {
			myTableData.setUserWord(args[0]);
			myTableData.setEmbeddedWord(args[1]);
			controller.viewTableData();
		}
	}
}
