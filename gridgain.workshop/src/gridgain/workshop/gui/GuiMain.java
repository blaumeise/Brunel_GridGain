package gridgain.workshop.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

public class GuiMain extends JFrame {
	private JTextField txt_input; 
	private JTextPane txt_output;
	
	public GuiMain() {
		 setTitle("7zip password breaker");
		 setSize(450, 170);
		 setDefaultCloseOperation(EXIT_ON_CLOSE); 
		initUI();
	}
	
	private void initUI() {
		
		JPanel panel = new JPanel();
		
		getContentPane().add(panel);

		panel.setLayout(null);
		panel.setToolTipText("Password Cracker");
		panel.setBounds(10,100, 400, 100);

		JButton btn_quit = new JButton("Find the password");
		btn_quit.setBackground(Color.ORANGE);
		btn_quit.setOpaque(true);
		btn_quit.setBounds(210, 60, 200, 30);
		btn_quit.setToolTipText("Start finding the password for the given file");
		

		panel.add(btn_quit);

		JButton btn_file = new JButton("File...");
		btn_file.setBounds(210, 20, 200, 30);
		btn_file.setToolTipText("Choose a 7zip file");
		btn_file.addActionListener(new OpenL());

		panel.add(btn_file);

		txt_input = new JTextField("");
		txt_input.setBounds(10, 20, 200, 30);
		
		panel.add(txt_input);

		txt_output = new JTextPane();
		txt_output.setBounds(10, 100, 400, 30);
		
		panel.add(txt_output);

	}
	
	  class OpenL implements ActionListener {
		    public void actionPerformed(ActionEvent e) {
		      JFileChooser c = new JFileChooser();
		      int rVal = c.showOpenDialog(GuiMain.this);
		      if (rVal == JFileChooser.APPROVE_OPTION) {
		    	  txt_input.setText(c.getSelectedFile().getName());
		    	  txt_output.setText(c.getSelectedFile().getName() + " selected!");
		        //...
		      }
		      if (rVal == JFileChooser.CANCEL_OPTION) {
		    	  txt_output.setText("No file selected!");
		      }
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
