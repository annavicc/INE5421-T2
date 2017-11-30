package GUI;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ContextFreeLanguage.ContextFreeGrammar;

public class PropertiesFrame extends JFrame {

	private MainFrame mainFrame;
	private JComboBox<ContextFreeGrammar> cbPropertiesCFG;
	private JPanel contentPane;

	
	/**
	 * Exit back to main frame
	 */
	public void exit() {
		mainFrame.setVisible(true);
		this.dispose();
	}
	
	/**
	 * Create the application.
	 */
	public PropertiesFrame(MainFrame mainFrame) {
		try {
			this.mainFrame = mainFrame;
			initialize();
			this.setVisible(true);
			mainFrame.setVisible(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setTitle("Context Free Grammar Properties");
		this.setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 200, 750, 190);
		contentPane = new JPanel();

		this.getContentPane().setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel propertiesFramePanel = new JPanel();
		propertiesFramePanel.setBounds(0, 0, 10, 10);
		contentPane.add(propertiesFramePanel);
		
		JLabel lblGrammarSelection = new JLabel("Select the Grammar Below:");
		lblGrammarSelection.setBounds(41, 34, 221, 15);
		contentPane.add(lblGrammarSelection);
		
		cbPropertiesCFG = new JComboBox();
		cbPropertiesCFG.setBounds(570, 61, 32, 24);
		contentPane.add(cbPropertiesCFG);
		
		JComboBox cbPropertiesGrammar = new JComboBox();
		cbPropertiesGrammar.addItem("Has Left Recursion?");
		cbPropertiesGrammar.addItem("Is Factored?");
		
		cbPropertiesGrammar.setBounds(105, 61, 32, 24);
		contentPane.add(cbPropertiesGrammar);
		
		JLabel lblPropertySelection = new JLabel("Select the Verification Below:");
		lblPropertySelection.setBounds(479, 34, 221, 15);
		contentPane.add(lblPropertySelection);
		
		JButton btnVerify = new JButton("Verify");
		btnVerify.setBounds(621, 153, 117, 25);
		contentPane.add(btnVerify);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(479, 153, 117, 25);
		contentPane.add(btnCancel);
		
	}
}
