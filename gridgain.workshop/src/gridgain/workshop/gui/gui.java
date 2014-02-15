package gridgain.workshop.gui;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class gui extends JFrame {

	public gui() {

		initUI();
	}

	private void initUI() {

		JPanel panel = new JPanel();
		getContentPane().add(panel);

		panel.setLayout(null);
		panel.setToolTipText("Password Cracker");
		panel.setBounds(0,100, 400, 100);

		JButton btn_quit = new JButton("Quit");
		btn_quit.setBounds(200, 60, 200, 30);
		btn_quit.setToolTipText("Quit");

		panel.add(btn_quit);

		JButton btn_ok = new JButton("Start search for password");
		btn_ok.setBounds(200, 20, 200, 30);
		btn_ok.setToolTipText("Start search for password");

		panel.add(btn_ok);

		JTextField txt_input = new JTextField("Choose file");
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
				gui ex = new gui();
				ex.setVisible(true);
			}
		});
	}
}