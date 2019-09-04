package my.controller;

import java.io.IOException;

import com.itextpdf.text.DocumentException;

import my.bean.WordTableBean;
import my.view.WordTableView;
public class WordTableController {
	private WordTableBean model;
	private WordTableView view;
	
	public WordTableController(WordTableBean model, WordTableView view) {
		this.model = model;
		this.view = view;
	}
	
	public void setUserWord(String usrWord) {
		model.setUserWord(usrWord);
	}
	
	public String getUserWord() {
		return model.getUserWord();
	}
	
	public void setEmbeddedWord(String embeddedWord) {
		model.setEmbeddedWord(embeddedWord);
	}
	
	public String getEmbeddedWord() {
		return model.getEmbeddedWord();
	}
	
	public void viewTableData() throws DocumentException, IOException {
		view.printToConsole(model);
		view.printToPDF(model);
	}
}
