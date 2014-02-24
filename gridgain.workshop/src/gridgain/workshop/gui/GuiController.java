package gridgain.workshop.gui;

import gridgain.workshop.main.DictionaryExample;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import javax.swing.JTextPane;

import org.gridgain.grid.GridException;

/**
 * 
 * @author Ana Fernandez +??
 * @modified Matthias Riedel and Philipp Trumpp
 *
 */
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

	/**
	 * Method setSelectedFile
	 * Method transfers path to zip-archive to logic
	 * @param selectedFile
	 * @throws IOException
	 * @throws GridException
	 */
	public void setSelectedFile(File selectedFile) throws IOException, GridException {
		this.selectedFile = selectedFile;
		System.out.println("Selected 7zip-File: "+ selectedFile.getAbsolutePath());
	}
	
	/**
	 * Method setSelectedDict
	 * Method transfers path to password-dictionary to logic
	 * @param selectedDict
	 * @throws IOException
	 * @throws GridException
	 */
	public void setSelectedDict(File selectedDict) throws IOException, GridException {
		this.selectedDict = selectedDict;
		System.out.println("Selected Dictionary: "+ selectedDict.getAbsolutePath());
	}

	public GuiController(String[] args) {
		dex = new DictionaryExample();
		this.args = args;
	}

	/**
	 * Method findPassword
	 * Method collects gui information and invokes dictionary attack
	 * @param deleteInputFile
	 * @param remoteUsed
	 * @throws IOException
	 * @throws GridException
	 */
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

	/**
	 * Method showPassword
	 * Method updates gui to result of logic
	 * @param pw
	 */
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
