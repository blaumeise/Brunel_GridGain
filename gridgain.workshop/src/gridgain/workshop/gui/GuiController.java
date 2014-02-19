package gridgain.workshop.gui;

import gridgain.workshop.main.DictionaryExample;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import javax.swing.JTextPane;

import org.gridgain.grid.GridException;

public class GuiController {
	private JTextPane output;
	private File selectedFile;
	
	private File selectedDict;
	private boolean delete = false;
	private boolean remote = false;
	
	private boolean searchingPW = false;

	private DictionaryExample dex;
	private String[] args;
	
	public void setOutput(JTextPane output) {
		this.output = output;
	}

	public void setSelectedFile(File selectedFile) throws IOException, GridException {
		this.selectedFile = selectedFile;
		System.out.println("Selected 7zip-File: "+ selectedFile.getAbsolutePath());
	}
	
	public void setSelectedDict(File selectedDict) throws IOException, GridException {
		this.selectedDict = selectedDict;
		System.out.println("Selected Dictionary: "+ selectedDict.getAbsolutePath());
	}

	public GuiController(String[] args) {
		dex = new DictionaryExample();
		this.args = args;
	}

	public void findPassword(boolean deleteInputFile, boolean remoteUsed) throws IOException, GridException {
		if (selectedFile != null && selectedDict != null) {
			
			if(!searchingPW){
				searchingPW = true;
				// start grid search --> parameters GuiController, selectedFile
				this.delete = deleteInputFile;
				this.remote = remoteUsed;
				dex.executeDictionary(this.selectedFile.getAbsolutePath(), this.selectedDict.getAbsolutePath(), args, this, deleteInputFile, remoteUsed);			
			}else {
				output.setText("-- Wait please! Searching password! --");
			}
			
		} else {
			output.setText("Select files!");
		}
	}

	public void showPassword(String pw) {
		if(pw.isEmpty()){
			output.setBackground(Color.RED);
		}
		else{
			output.setBackground(Color.green);
		}
		output.setForeground(Color.BLACK);
		output.setText("Found password: " + pw);
		
		searchingPW = false;
	}
}
