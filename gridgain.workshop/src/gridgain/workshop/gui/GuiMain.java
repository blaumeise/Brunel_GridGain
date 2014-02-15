package gridgain.workshop.gui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class GuiMain extends JFrame {
	public GuiMain() {
		 setTitle("7zip password breaker");
		 setSize(450, 150);
		 setDefaultCloseOperation(EXIT_ON_CLOSE); 
		initUI();
	}
	
	private void initUI() {
		
		JPanel panel = new JPanel();
		
		getContentPane().add(panel);

		panel.setLayout(null);
		panel.setToolTipText("Password Cracker");
		panel.setBounds(0,100, 400, 100);

		JButton btn_quit = new JButton("Find the password");
		btn_quit.setBounds(200, 60, 200, 30);
		btn_quit.setToolTipText("Start finding the password for the given file");

		panel.add(btn_quit);

		JButton btn_ok = new JButton("File...");
		btn_ok.setBounds(200, 20, 200, 30);
		btn_ok.setToolTipText("Choose a 7zip file");

		panel.add(btn_ok);

		JTextField txt_input = new JTextField("");
		txt_input.setBounds(0, 20, 200, 30);
		
		panel.add(txt_input);

		JTextField txt_output = new JTextField("Receive Password here");
		txt_output.setBounds(0, 60, 200, 30);
		
		panel.add(txt_output);

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
