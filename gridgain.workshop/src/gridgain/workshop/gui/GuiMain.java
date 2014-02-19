package gridgain.workshop.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

import org.gridgain.grid.GridException;

public class GuiMain extends JFrame {
	private JTextField txt_input;
	private JTextField txt_dict;
	private JTextPane txt_output;
	private GuiController controller;

	private JCheckBox box_delete;
	private JCheckBox box_remote;
	
	public GuiMain(String args[]) {
		controller = new GuiController(args);
		setTitle("7zip password breaker");
		setSize(440, 220);
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
		btn_find.setBounds(210, 100, 200, 30);
		btn_find.setToolTipText("Start finding the password for the given file");
		btn_find.addActionListener(new FindPassword());

		panel.add(btn_find);

		JButton btn_file = new JButton("File...");
		btn_file.setBounds(210, 20, 200, 30);
		btn_file.setToolTipText("Choose a 7zip file");
		btn_file.addActionListener(new OpenFileChooser());

		panel.add(btn_file);

		JButton btn_dict = new JButton("Dictionary...");
		btn_dict.setBounds(210, 60, 200, 30);
		btn_dict.setToolTipText("Choose a password file");
		btn_dict.addActionListener(new DictionaryChooser());

		panel.add(btn_dict);
		
		box_delete = new JCheckBox("Delete local file");
		box_delete.setBounds(10, 95, 130, 15);
		box_delete.setToolTipText("Delete the local copy of file transfered");
		
		panel.add(box_delete);
	
		box_remote = new JCheckBox("Use remote nodes");
		box_remote.setBounds(10, 115, 130, 15);
		box_remote.setToolTipText("Shoud the attack use remote nodes, no file transfer, make it manuel");
		
		panel.add(box_remote);
		
		txt_input = new JTextField("");
		txt_input.setBounds(10, 20, 200, 30);

		panel.add(txt_input);

		txt_output = new JTextPane();
		txt_output.setBounds(10, 140, 400, 30);

		panel.add(txt_output);
		
		txt_dict = new JTextField("");
		txt_dict.setBounds(10, 60, 200, 30);

		panel.add(txt_dict);

		controller.setOutput(txt_output);
	}

	class OpenFileChooser implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JFileChooser c = new JFileChooser();
			int rVal = c.showOpenDialog(GuiMain.this);
			if (rVal == JFileChooser.APPROVE_OPTION) {
				txt_input.setText(c.getSelectedFile().getName());
				txt_output.setText(c.getSelectedFile().getName() + " selected!");
				try {
					controller.setSelectedFile(c.getSelectedFile());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (GridException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if (rVal == JFileChooser.CANCEL_OPTION) {
				txt_output.setText("No file selected!");
			}
		}
	}
	
	class DictionaryChooser implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JFileChooser c = new JFileChooser();
			int rVal = c.showOpenDialog(GuiMain.this);
			if (rVal == JFileChooser.APPROVE_OPTION) {
				txt_dict.setText(c.getSelectedFile().getName());
				try {
					controller.setSelectedDict(c.getSelectedFile());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (GridException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if (rVal == JFileChooser.CANCEL_OPTION) {
				txt_output.setText("No file selected!");
			}
		}
	}
	
	class FindPassword implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			txt_output.setText("-- Searching password --");
			try {
				System.out.println(box_delete.isSelected() + " - " + box_remote.isSelected());
				controller.findPassword(box_delete.isSelected(), box_remote.isSelected());
			} catch (IOException | GridException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	public static void main(final String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				GuiMain ex = new GuiMain(args);
				ex.setVisible(true);
			}
		});
	}

}
