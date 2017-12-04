package GUI;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;

import ContextFreeLanguage.ContextFreeGrammar;


public class MainFrame extends JFrame {

	// Auto-generated UID
	private static final long serialVersionUID = 3421512408406584422L;
	
	private JList<String> jListMainCFL;
	private HashMap<String, ContextFreeGrammar> languages = new HashMap<String, ContextFreeGrammar>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		Toolkit xToolkit = Toolkit.getDefaultToolkit();
		java.lang.reflect.Field awtAppClassNameField;
		try {
			// Set application name
			awtAppClassNameField = xToolkit.getClass().getDeclaredField("awtAppClassName");
			awtAppClassNameField.setAccessible(true);
			awtAppClassNameField.set(xToolkit, "Context Free Grammar Manager");
			
			// Set native window theme
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame window = new MainFrame();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}

	/**
	 * Create the application.
	 */
	public MainFrame() {
		initialize();
	}


	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.setTitle("Context Free Grammar Manager");
//		this.setResizable(false);
		this.setBounds(100, 100, 500, 400);
		this.setMinimumSize(new Dimension(400, 300));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel mainPanel = new JPanel();
		this.getContentPane().add(mainPanel, BorderLayout.CENTER);
		
		// JButtons:
		
		JButton btnMainAddCFG = new JButton("Add a CFG");
		btnMainAddCFG.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new AddCFGFrame(MainFrame.this);
			}
		});
		
		JButton btnMainViewEdit = new JButton("View/Edit");
		btnMainViewEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isLanguageSelected()) {
					String idSelected = jListMainCFL.getSelectedValue();
					new ViewEditFrame(MainFrame.this, languages.get(idSelected));
				}
			}
		});
		
		JButton btnMainRemoveCFG = new JButton("Remove");
		btnMainRemoveCFG.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	MainFrame.this.removeSelected();
		    }
		});
		
		JButton btnMainRenameCFG = new JButton("Rename");
		btnMainRenameCFG.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	MainFrame.this.renameSelected();
		    }
		});
		
		JButton btnMainClear = new JButton("Clear");
		btnMainClear.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	MainFrame.this.clearList();
		    }
		});
		
		JButton btnMainOperations = new JButton("CFG Operations");
		btnMainOperations.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	new OperationsFrame(MainFrame.this);
		    }
		});
		
		JButton btnMainVerifications = new JButton("CFG Properties");
		btnMainVerifications.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	new PropertiesFrame(MainFrame.this);
		    }
		});
		
		// Scrollable JList:
		
		jListMainCFL = new JList<String>();
		jListMainCFL.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		updateJList();
		
		JScrollPane scrollPaneMainCFG = new JScrollPane();
		scrollPaneMainCFG.setViewportView(jListMainCFL);
		
		// Close Window action:
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (MainFrame.this.confirm("Quit") == JOptionPane.YES_OPTION) {
					MainFrame.this.dispose();
				}
			}
		});
		
		// Layout definitions:
		
		GroupLayout gl_mainPanel = new GroupLayout(mainPanel);
		gl_mainPanel.setHorizontalGroup(
			gl_mainPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_mainPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPaneMainCFG, GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_mainPanel.createParallelGroup(Alignment.LEADING, false)
						.addComponent(btnMainAddCFG, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnMainViewEdit, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnMainRenameCFG, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnMainRemoveCFG, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnMainClear, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
				.addGroup(gl_mainPanel.createSequentialGroup()
					.addGap(46)
					.addComponent(btnMainOperations, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
					.addGap(14)
					.addComponent(btnMainVerifications, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
					.addGap(40))
		);
		gl_mainPanel.setVerticalGroup(
			gl_mainPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_mainPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_mainPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_mainPanel.createSequentialGroup()
							.addComponent(scrollPaneMainCFG)
							.addGap(40))
						.addGroup(gl_mainPanel.createSequentialGroup()
							.addComponent(btnMainAddCFG, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnMainViewEdit, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
							.addGap(7)
							.addComponent(btnMainRenameCFG, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
							.addGap(7)
							.addComponent(btnMainRemoveCFG, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
							.addGap(7)
							.addComponent(btnMainClear, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 254, Short.MAX_VALUE)
							.addGroup(gl_mainPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(btnMainOperations, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnMainVerifications, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))))
					.addContainerGap())
		);
		
		mainPanel.setLayout(gl_mainPanel);
	}
	
	// Get selected item from the list
	public String getSelectedFromList() {
		return jListMainCFL.getSelectedValue();
	}
	
	// Add Regular Language to JList
	public void addToPanel(ContextFreeGrammar cfg) {
		this.languages.put(cfg.getId(), cfg);
		this.updateJList();
		this.jListMainCFL.setSelectedValue(cfg.getId(), true);
	}
	
	// Get Regular Language by name
	public ContextFreeGrammar getLanguage(String name) {
		return this.languages.get(name);
	}
	
	// Get Regular Languages names array
	public String[] getLanguagesNames() {
		return this.languages.keySet()
				.stream()
				.toArray(size -> new String[size]);
	}
	
	// Update JList elements
	private void updateJList() {
		this.jListMainCFL.setListData(this.getLanguagesNames());
	}	
	
	// Ask for action confirmation
	private int confirm (String action) {
		return JOptionPane.showConfirmDialog(
				this,
				"Are you sure you want to " + action + "?",
				action + '?',
				JOptionPane.YES_NO_OPTION
		);
	}
	
	// Clear languages list on confirmation
	private void clearList() {
		if (this.confirm("Clear List") == JOptionPane.YES_OPTION) {
			MainFrame.this.languages.clear();
	    	updateJList();
		}
	}
	
	// Remove language selected on JList
	private void removeSelected() {
		if (isLanguageSelected()) {
			String id = jListMainCFL.getSelectedValue();
			if (this.confirm("Remove \"" + id + '"') == JOptionPane.YES_OPTION) {
				MainFrame.this.languages.remove(id);
		    	updateJList();
			}
		}
	}
	
	// Rename language selected on JList
	private void renameSelected() {
		if (isLanguageSelected()) {
			String id = jListMainCFL.getSelectedValue();
			Object input = JOptionPane.showInputDialog
					(this, "Enter new name:", "Rename RL", JOptionPane.PLAIN_MESSAGE, null, null, id);
			
			if (input != null) {
				String newName = input.toString();
				if (newName.equals("") || newName.contains("[") || newName.contains("]")) {
					JOptionPane.showMessageDialog(this, "Invalid name!\n"
							+ "Name must not be empty.\nThe characters '[' and ']' are not allowed.");
					return;
				}
				if (this.getLanguage(newName) != null) {
					int answer = JOptionPane.showConfirmDialog(
							this,
							'"' + newName + "\" already exists!\nOverwrite?",
							"Overwrite?",
							JOptionPane.YES_NO_OPTION
					);
					if (answer != JOptionPane.YES_OPTION) {
						return;
					}
				}
				ContextFreeGrammar l = this.getLanguage(id);
				this.languages.remove(id);
				l.setId(newName);
				this.addToPanel(l);
			}
		}
	}
	
	// Verifies if a language was selected
	private boolean isLanguageSelected() {
		if (jListMainCFL.isSelectionEmpty()) {
			JOptionPane.showMessageDialog(this, "No language selected!");
			return false;
		}
		return true;
	}
	
	// Return the Regular Language list
	public HashMap<String, ContextFreeGrammar> getLanguages() {
		return this.languages;
	}
		
}
