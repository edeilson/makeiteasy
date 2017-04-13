import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.swing.*;
import javax.swing.JFormattedTextField.AbstractFormatter;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

/**
 * 
 * @author Edeilson Silva on 07/04/2017.
 *
 */
public class Main {

	private static String pathToSaveFile = null, fileSource = null;
	
	public static void main(String[] args) {
		createAndShowGUI();
	}

	private static void createAndShowGUI() {

		// Create and set up the window.
		JFrame frame = new JFrame("ISE Make it easy - Update attendance");
		frame.setLayout(new BorderLayout());
		//centreWindow(frame);
		//frame.setSize(-400, 0);
		//frame.setLocationRelativeTo(null);
		// frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout() );
		panel.setSize(400, 400);
		
		
		// Create button to select the source file to generate a new on
		JButton cmdSelectFile = new JButton("Select the source file");

		cmdSelectFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Select the source file");
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				int result = fileChooser.showOpenDialog(frame);
				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					
					cmdSelectFile.setText(selectedFile.toString());
					fileSource = selectedFile.toString();
				}else{
					JOptionPane.showMessageDialog(null, "No file selected");
				}
				
			}
		});

		JButton cmdSelectPath = new JButton("Select the place where you want to save the new file");
		cmdSelectPath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int result;

				JFileChooser chooser = new JFileChooser();
				//chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				
				chooser.setDialogTitle("Save file in...");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				//
				// disable the "All files" option.
				//
				chooser.setAcceptAllFileFilterUsed(false);
				//
				if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {

					/*
					 * System.out.println("getCurrentDirectory(): " +
					 * chooser.getCurrentDirectory());
					 * System.out.println("getSelectedFile() : " +
					 * chooser.getSelectedFile());
					 */
					cmdSelectPath.setText(chooser.getSelectedFile().toString());
					pathToSaveFile = chooser.getSelectedFile().toString();
				} else {
					JOptionPane.showMessageDialog(null, "No file selected");
				}

			}
		});

		JLabel label1 = new JLabel("Select the date, \notherwise the system get today's date:");

		Properties p = new Properties();
		// p.put("text.today", "Today");
		// p.put("text.month", "Month");
		// p.put("text.year", "Year");

		UtilDateModel model = new UtilDateModel();
		JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
		JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

		JButton cmdGenerate = new JButton("Generate");
		cmdGenerate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// Get the date from DatePicker
				int iAuxMonth = model.getMonth() + 1;
				int iAuxDay = model.getDay();
				int iAuxYear = model.getYear();

				//TODO check if there is an file with the same name before generate a new onet
				if((fileSource!=null)&& (pathToSaveFile!=null)){
					generateNewFile(fileSource, iAuxDay , iAuxMonth, iAuxYear, pathToSaveFile);
				}else{
					JOptionPane.showMessageDialog(null, "File wasn't created.");
				}
				
				
			}
		});

		panel.add(cmdSelectFile);
	    panel.add( cmdSelectPath );
	    panel.add( label1 );
	    panel.add( datePicker);
	    panel.add( cmdGenerate );
		
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		
		/*frame.getContentPane().add(cmdSelectFile);
		frame.getContentPane().add(cmdSelectPath);
		frame.getContentPane().add(label1);
		frame.getContentPane().add(datePicker);
		frame.getContentPane().add(cmdGenerate);*/

		// Display the window.
		frame.pack();
		frame.setVisible(true);

	}

	// TODO Create a executable

	private static void generateNewFile(String fileSource, int auxDay, int auxMonth, int auxYear,
			String pathToSaveFile){
		 //TODO Find file; 
		String csvFile = fileSource;//"C:/Users/U/Desktop/MakeItEasy/test001.csv";
		  
		  BufferedReader br = null; 
		  String line = "";  
		  String cvsSplitBy = ",";
		  StringBuilder sb = new StringBuilder(); 
		  
		  try {
		  
		  br = new BufferedReader(new FileReader(csvFile));
		  
		  while ((line = br.readLine()) != null) { 
			  
			  for (int i = 0; i < 5; i++){ 
			  sb.append(line); 
			  //TODO Ask the date 
			  
			  
			  sb.append("," + (auxDay + i) + "/" + auxMonth + "/" +auxYear); 
		  
		  sb.append("\n"); 
		  }
			  }
		  } catch (Exception e) {
		  e.printStackTrace(); 
		  }
		  
		// TODO Generate path to save;
		String pathtoSave = pathToSaveFile;// "C:/Users/U/Desktop/MakeItEasy/";
		String fileName = "/result001.csv";
		File myRegisters = new File(pathtoSave.concat(fileName));
		 //File myRegisters = new File(pathToSave);

		try {
			myRegisters.createNewFile(); // The following lines create a HEAD
											// for the file

			FileWriter fstream = new FileWriter(myRegisters, true);
			// true // tells to // append // data.
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(sb.toString());
			out.close();
			fstream.close();
			//System.out.println(fileName + " was generated succesffully");
			JOptionPane.showMessageDialog(null, fileName + " was generated succesffully");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static void centreWindow(Window frame) {
	    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	    int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
	    int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
	    frame.setLocation(x, y);
	}

}
