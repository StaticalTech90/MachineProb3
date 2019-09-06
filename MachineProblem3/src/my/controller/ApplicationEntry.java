package my.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.itextpdf.text.DocumentException;

import my.bean.WordTableBean;
import my.view.WordTableView;

public class ApplicationEntry {
	
	/*
	 * MVC Reference: https://www.tutorialspoint.com/design_pattern/mvc_pattern.html
	 * And also Sir Mon's samples in the group :)
	 */
	public static void main(String[] args) throws DocumentException, IOException {
		// TODO Auto-generated method stub
		WordTableBean myTableData = new WordTableBean();
		WordTableView view = new WordTableView();
		WordTableController controller = new WordTableController(myTableData, view);
		
		if(args.length == 0) {
			System.out.println("NO DATA IS IN THE ARGUMENTS");
		}
		else {
			controller.setUserWord(new String(args[0].getBytes(StandardCharsets.UTF_8.name())));
			controller.setEmbeddedWord(new String(args[1].getBytes(StandardCharsets.UTF_8.name())));
			controller.viewTableData();
		}
	}
}
