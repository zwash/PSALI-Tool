package ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

public class TestGUI {

	private JFrame frmPsaliFormatter;
	private JTextField textFieldInputFile;
	private JTextField txtNameNewdat;
	private JTextField textFieldFileName;
	private JTextField textFieldConsole;
	private BufferedReader input;
	private PrintWriter writer;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
					TestGUI window = new TestGUI();
					window.frmPsaliFormatter.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TestGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmPsaliFormatter = new JFrame();
		frmPsaliFormatter.setTitle("CTL PSALI tool");
		frmPsaliFormatter.setBounds(100, 100, 450, 300);
		frmPsaliFormatter.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmPsaliFormatter.getContentPane().setLayout(null);
		
		JButton btnInputFile = new JButton("Input File...");
		btnInputFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setDialogTitle("Choose CTL file");
				if (fc.showOpenDialog(btnInputFile) == JFileChooser.APPROVE_OPTION) {
					textFieldInputFile.setText(fc.getSelectedFile().getAbsolutePath());
					try {
						input = new BufferedReader(new FileReader(fc.getSelectedFile()));
					} catch (Exception ex) {
						textFieldConsole.setText("Failed to read selected file");
						input = null;
					}
				}else {
					textFieldInputFile.setText("No file choosen");
					input = null;
				}
			}
		});
		btnInputFile.setBounds(10, 25, 107, 38);
		frmPsaliFormatter.getContentPane().add(btnInputFile);
		
		textFieldInputFile = new JTextField();
		textFieldInputFile.setEditable(false);
		textFieldInputFile.setBounds(119, 25, 305, 38);
		frmPsaliFormatter.getContentPane().add(textFieldInputFile);
		textFieldInputFile.setColumns(10);
		
		txtNameNewdat = new JTextField();
		txtNameNewdat.setHorizontalAlignment(SwingConstants.LEFT);
		txtNameNewdat.setEditable(false);
		txtNameNewdat.setText("Name new .dat file:");
		txtNameNewdat.setBounds(10, 74, 117, 20);
		frmPsaliFormatter.getContentPane().add(txtNameNewdat);
		txtNameNewdat.setColumns(10);
		
		textFieldFileName = new JTextField();
		textFieldFileName.setBounds(10, 96, 414, 20);
		frmPsaliFormatter.getContentPane().add(textFieldFileName);
		textFieldFileName.setColumns(10);
		
		JButton btnRun = new JButton("Run");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (input ==  null) {
					textFieldConsole.setText("Please select file first");
				}else if (textFieldFileName.getText() == null || textFieldFileName.getText().trim().isEmpty()) {
					textFieldConsole.setText("Enter a non-blank name for this file");
				}else {
					try {
						String printPath = System.getProperty("user.home") + "\\Desktop\\";
						writer = new PrintWriter(printPath + textFieldFileName.getText() + ".dat");
						String s = input.readLine();
						int length = s.length();
						if (length % 512 == 0) {
							int lines = length / 512;
							int start = 0;
							int stop = 512;
							for (int i = 0; i < lines; i++) {
								String record = s.substring(start, stop);
								if (!record.startsWith("UHL") && !record.startsWith("UTL")) {
									record  = record.substring(0, 260) + "PSALI" + record.substring(265, 475) + "PSALI" + record.substring(480);
								}
								writer.println(record);
								start += 512;
								stop += 512;
							}
							textFieldConsole.setText("New file saved to " + System.getProperty("user.home") + "\\Desktop");
							writer.close();
						}else {
							textFieldConsole.setText("File not in correct 512 format");
							writer.close();
						}
					} catch (Exception ex) {
						textFieldConsole.setText("name for this file could not be used.");
					}
				}
			}
		});
		btnRun.setBounds(144, 127, 150, 51);
		frmPsaliFormatter.getContentPane().add(btnRun);
		
		textFieldConsole = new JTextField();
		textFieldConsole.setEditable(false);
		textFieldConsole.setBounds(10, 189, 403, 61);
		frmPsaliFormatter.getContentPane().add(textFieldConsole);
		textFieldConsole.setColumns(10);
	}
}
