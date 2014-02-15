package gridgain.workshop.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

public class GuiMain extends JFrame {
	private JTextField txt_input;
	private JTextPane txt_output;
	private GuiController controller;

	public GuiMain() {
		controller = new GuiController();
		setTitle("7zip password breaker");
		setSize(420, 170);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		initUI();
	}

	private void initUI() {

		JPanel panel = new JPanel();

		getContentPane().add(panel);

		panel.setLayout(null);
		panel.setToolTipText("Password Cracker");
		panel.setBounds(10, 100, 400, 100);

		JButton btn_find = new JButton("Find the password");
		btn_find.setBackground(Color.ORANGE);
		btn_find.setOpaque(true);
		btn_find.setBounds(210, 60, 200, 30);
		btn_find.setToolTipText("Start finding the password for the given file");
		btn_find.addActionListener(new FindPassword());

		panel.add(btn_find);

		JButton btn_file = new JButton("File...");
		btn_file.setBounds(210, 20, 200, 30);
		btn_file.setToolTipText("Choose a 7zip file");
		btn_file.addActionListener(new OpenFileChooser());

		panel.add(btn_file);

		txt_input = new JTextField("");
		txt_input.setBounds(10, 20, 200, 30);

		panel.add(txt_input);

		txt_output = new JTextPane();
		txt_output.setBounds(10, 100, 400, 30);

		panel.add(txt_output);
		controller.setOutput(txt_output);
	}

	class OpenFileChooser implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JFileChooser c = new JFileChooser();
			int rVal = c.showOpenDialog(GuiMain.this);
			if (rVal == JFileChooser.APPROVE_OPTION) {
				txt_input.setText(c.getSelectedFile().getName());
				txt_output.setText(c.getSelectedFile().getName() + " selected!");
				controller.setSelectedFile(c.getSelectedFile());
			}
			if (rVal == JFileChooser.CANCEL_OPTION) {
				txt_output.setText("No file selected!");
			}
		}
	}
	

	class FindPassword implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			txt_output.setText("-- Searching password --");
			controller.findPassword();
		}
	}

	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				GuiMain ex = new GuiMain();
				ex.setVisible(true);
			}
		});
	}

}
