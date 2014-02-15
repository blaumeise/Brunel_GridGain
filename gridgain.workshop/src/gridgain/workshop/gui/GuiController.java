package gridgain.workshop.gui;

import java.io.File;

import javax.swing.JTextPane;

public class GuiController {
	private JTextPane output;
	private File selectedFile;

	public void setOutput(JTextPane output) {
		this.output = output;
	}

	public void setSelectedFile(File selectedFile) {
		this.selectedFile = selectedFile;
		System.out.println(selectedFile.getAbsolutePath());
	}

	public GuiController() {}

	public void findPassword() {
		if (selectedFile != null) {
			// start grid search --> parameters GuiController, selectedFile
			// ...
		} else {
			output.setText("Select a file!");
		}
	}

	public void showPassword(String pw) {
		output.setText("Finded password: \n" + pw);
	}
}
