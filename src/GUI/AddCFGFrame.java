package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import ContextFreeLanguage.CFGOperator;
import ContextFreeLanguage.ContextFreeGrammar;


public class AddCFGFrame extends JFrame {

	// Auto-generated UID
	private static final long serialVersionUID = 7589217999180208071L;
	
	private MainFrame mainFrame;

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
	public AddCFGFrame(MainFrame mainFrame) {
		try {
			this.mainFrame = mainFrame;
			initialize();
			mainFrame.setVisible(true);
			this.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.setTitle("Add Context Free Grammar");
//		this.setResizable(false);
		this.setBounds(100, 100, 500, 500);
		this.setMinimumSize(new Dimension(450, 300));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel mainPanel = new JPanel();
		
		// JLabels:
		
		JLabel lblAddCFGName = new JLabel("Enter a name for the Context Free Language:");
		JLabel lblAddCFGAdd = new JLabel("Enter a Context Free Grammar below:");
		
		// RL name input:

		JTextField txtAddCFGName = new JTextField();
		
		// Scrollable RL input box:
		
		JTextArea txtaAddRL = new JTextArea();
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(txtaAddRL);
		txtaAddRL.setToolTipText("");
		
		txtaAddRL.setToolTipText("<html>CFG format: S -> a S b | b | & </html>");

		
		// JButons:
		
		JButton btnAddCFGCancel = new JButton("Cancel");
		btnAddCFGCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AddCFGFrame.this.exit();
			}
		});
				
		JButton btnAddCFGAdd = new JButton("Add");
		btnAddCFGAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String name = txtAddCFGName.getText(); // Gets name from txtField
				if (name.equals("") || name.contains("[") || name.contains("]")) {
					JOptionPane.showMessageDialog(AddCFGFrame.this, "Invalid name!\n"
							+ "Name must not be empty.\nThe characters '[' and ']' are not allowed.");
					return;
				}
				else if (AddCFGFrame.this.mainFrame.getLanguage(name) != null) {
					int answer = JOptionPane.showConfirmDialog(
							AddCFGFrame.this,
							'"' + name + "\" already exists!\nOverwrite?",
							"Overwrite?",
							JOptionPane.YES_NO_OPTION
					);
					if (answer != JOptionPane.YES_OPTION) {
						return;
					}
				}
				String input = txtaAddRL.getText(); // Gets text from pane
				ContextFreeGrammar l = ContextFreeGrammar.isValidCFG(input); // Gets RL object
				if(l == null) { // If type is not valid
					JOptionPane.showMessageDialog(AddCFGFrame.this, "Invalid input!");
					return;
				}

				// add CFGto Main Panel
				l.setId(name);
				
				AddCFGFrame.this.mainFrame.addToPanel(l);
				AddCFGFrame.this.exit();
			}
		});
		this.getContentPane().setLayout(new BorderLayout(0, 0));
		
		// Close Window action:
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				AddCFGFrame.this.exit();
			}
		});
		
		// Layout definitions:
		
		GroupLayout gl_mainPanel = new GroupLayout(mainPanel);
		gl_mainPanel.setHorizontalGroup(
			gl_mainPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_mainPanel.createSequentialGroup()
					.addGap(13)
					.addGroup(gl_mainPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblAddCFGName, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 476, Short.MAX_VALUE)
						.addComponent(txtAddCFGName, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 476, Short.MAX_VALUE)
						.addComponent(lblAddCFGAdd, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 476, Short.MAX_VALUE)
						.addGroup(Alignment.LEADING, gl_mainPanel.createSequentialGroup()
							.addComponent(scrollPane)
							.addPreferredGap(ComponentPlacement.RELATED))
						.addGroup(gl_mainPanel.createSequentialGroup()
							.addComponent(btnAddCFGCancel, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnAddCFGAdd, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)))
					.addGap(13))
		);
		gl_mainPanel.setVerticalGroup(
			gl_mainPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_mainPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblAddCFGName)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(txtAddCFGName, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblAddCFGAdd)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_mainPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_mainPanel.createSequentialGroup()
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnAddCFGCancel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
						.addComponent(btnAddCFGAdd, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		
		
		mainPanel.setLayout(gl_mainPanel);
		
		this.getContentPane().add(mainPanel, BorderLayout.CENTER);
	}
}
