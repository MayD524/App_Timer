package me.sweden.JSONMaybe;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.json.JSONObject;

public class Gui implements ActionListener, ChangeListener {
	JFrame frame = new JFrame("App Timer");
	private static DecimalFormat df2 = new DecimalFormat("#.##");
	
	//List of results
	ArrayList<String> results;
	
	//Refresh btn and output area
	static JTextArea toOutput = new JTextArea();
	JButton refreshBtn;	
	
	//Panels and panes
	JTabbedPane tabsPane = new JTabbedPane();
	JPanel appsPanel = new JPanel();
	JPanel searchPanel = new JPanel();
	
	//Search stuff 
	static JTextArea searchResults = new JTextArea();
	JButton searchBtn = new JButton("Search");
	JTextField searchField = new JTextField("Type :help here and press search");
	public Gui() {
		//Making it so only the program can change the output in the search and apps areas
		toOutput.setEditable(false);
		searchResults.setEditable(false);
		//Creating scrollpanes
		JScrollPane appScroll = new JScrollPane(toOutput); 
		JScrollPane searchScroll = new JScrollPane(searchResults); 
		
		refreshBtn= new JButton("Refresh");
		//Setting the scrollbars
		appScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		searchScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		//Adding the tabs
		tabsPane.addTab("Apps", appsPanel);
		tabsPane.addTab("Search", searchPanel);
		//Adding the ActionListeners
		refreshBtn.addActionListener(this); 
		searchBtn.addActionListener(this);
		tabsPane.addChangeListener(this);
		
		//Layout for apps panel
		appsPanel.setLayout(new GridBagLayout());
		GridBagConstraints appsConstraints = new GridBagConstraints();
		appsConstraints.fill = GridBagConstraints.HORIZONTAL;
		appsConstraints.ipady = 100;
		appsConstraints.gridx = 0;
		appsConstraints.gridy = 0;
		appsPanel.add(appScroll,appsConstraints);
		//Last component for the apps panel
		appsConstraints.fill = GridBagConstraints.HORIZONTAL;
		appsConstraints.weightx = .5;
		appsConstraints.gridx = 0;
		appsConstraints.gridy = 1;
		appsConstraints.ipadx = 1;
		appsConstraints.ipady = 1;
		appsPanel.add(refreshBtn,appsConstraints);
		
		//Layout for search panel
		searchPanel.setLayout(new GridBagLayout());
		GridBagConstraints searchConstraints = new GridBagConstraints();
		searchConstraints.fill = GridBagConstraints.HORIZONTAL;
		searchConstraints.ipady = 100; 
		searchConstraints.ipadx = 300; 
		searchConstraints.gridx = 0;
		searchConstraints.gridy = 0;
		searchPanel.add(searchScroll,searchConstraints);
		//next component
		searchConstraints.weightx = .5;
		searchConstraints.weighty = .5;
		searchConstraints.ipady = 30; 
		searchConstraints.ipadx = 20; 
		searchConstraints.gridx = 0;
		searchConstraints.gridy = 1;
		searchPanel.add(searchField,searchConstraints);
		//last component for the search panel
		searchConstraints.fill = GridBagConstraints.HORIZONTAL;
 		searchConstraints.ipady = 50; 
		searchConstraints.ipadx = 0;
		searchConstraints.weightx = .5;
 		searchConstraints.gridx = 0;
		searchConstraints.gridy = 2;
 		searchPanel.add(searchBtn,searchConstraints);
		//Setting the frame visible and also calling readJSON + making sure it closes when wanted
		frame.setVisible(true);
		readJSON();
		frame.add(tabsPane);
		frame.setSize(new Dimension(320,210));
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void readJSON() {
		//Get the path for the apps.json file
		String path = System.getProperty("user.dir")+"\\apps.json";
		try {
			
			//Get the contents 
			String contents = new String((Files.readAllBytes(Paths.get(path))));
			JSONObject o = new JSONObject(contents);
			Set<String> te = o.keySet();
			//Loop through the keySet list
			for(String t : te) {
				int time = o.getInt(t);
				//Append it to the output
				toOutput.append(t+" : "+time+" sec \n");
				
			}
			
			}catch(Exception e) {
				e.printStackTrace();
			}
	}
	//This method handles the searching and fixing of the strings. Basicly like the readJSON method
	public ArrayList<String> searchResList(String prefix, String searchFor){
		ArrayList<String> results = new ArrayList<String>();
		String path = System.getProperty("user.dir")+"\\apps.json";
		try {
			String contents = new String((Files.readAllBytes(Paths.get(path))));
			JSONObject o = new JSONObject(contents);
			Set<String> te = o.keySet();
		 	for(String t : te) {
		 		//Checks prefix and does the correct action and adds it to results
				if(prefix.equalsIgnoreCase("starts")) {
					if(t.startsWith(searchFor)) {
						results.add(t+": "+o.getInt(t)+" sec");
					}
				}else if(prefix.equalsIgnoreCase("contains")) {
					if(t.contains(searchFor)) {
						results.add(t+": "+o.getInt(t)+" sec");
					}
				}else if (prefix.equalsIgnoreCase("ends")) {
					if(t.endsWith(searchFor)) {
						results.add(t+": "+o.getInt(t)+" sec");
					}
				}else if(prefix.equalsIgnoreCase("equ")) {
					if(t.equals(searchFor)) {
						results.add(t+": "+o.getInt(t)+" sec");
					}
				}else if(prefix.startsWith("toMin")) {
					if(t.equals(searchFor)) {
						double timeInMin = (double) o.getInt(t) / 60;
						
						results.add(t+": "+df2.format(timeInMin)+" min");
					}
				}else if(prefix.startsWith("toHour")) {
					if(t.equals(searchFor)) {
						double timeInMin = (double) o.getInt(t) / 60;
						double timeInH = timeInMin / 60;
						results.add(t+": "+df2.format(timeInH)+" h");
					}
				}else if(prefix.startsWith("greater")) {
					for(String ti : te) {
						int time = o.getInt(t);
						//Append it to the output
						if(time > Integer.parseInt(searchFor))
							searchResults.append(ti+" : "+time+" sec \n");
						
					}
				}else if(prefix.startsWith("less")) {
					for(String ti : te) {
						int time = o.getInt(t);
						//Append it to the output
						if(time < Integer.parseInt(searchFor))
							searchResults.append(ti+" : "+time+" sec \n");
						
					}
				}else if(prefix.startsWith("equT")) {
					for(String ti : te) {
						int time = o.getInt(t);
						//Append it to the output
						if(time == Integer.parseInt(searchFor))
							searchResults.append(ti+" : "+time+" sec \n");
						
					}
				}
				
				
			}
			
			}catch(Exception e) {
				e.printStackTrace();
			}
		return results;
	}
	public static boolean isNumeric(String strNum) {
	    if (strNum == null) {
	        return false;
	    }
	    try {
	        @SuppressWarnings("unused")
			int d = Integer.parseInt(strNum);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == refreshBtn) {
			toOutput.setText("");
			readJSON();
		}else if(e.getSource() == searchBtn) {
			//Checks if the search field is blank
			Macros m = new Macros();
			String path = System.getProperty("user.dir")+"\\macros.txt";
			
			String[] prefix = searchField.getText().split(":");
			
			if(isNumeric(prefix[0])) {
			 	prefix = m.readMacros(path, prefix[1],Integer.parseInt(prefix[0]));
			}
			if(!searchField.isEditable())
				System.out.println((char)69);
			else {
				//Removes previous results
				searchResults.setText("");
				//Prints how to use it
				if(searchField.getText().equalsIgnoreCase(":help")) {
					searchResults.append("Usage: \n");
					searchResults.append("equ:[name] \n");
					searchResults.append("starts:[name] \n");
					searchResults.append("ends:[name] \n");
					searchResults.append("contains:[name] \n");
					searchResults.append("toMin:[name] \n");
					searchResults.append("toHour:[name] \n");
					searchResults.append("greater:[timeInSec] \n");
					searchResults.append("less:[timeInSec] \n");
					searchResults.append("to use macros do [lineNr]:[procName] \n");
				} 
				else {
					//This is important for getting the prefix and what to search for
					//System.out.println(prefix[0]+" "+prefix[1]);
					results = searchResList(prefix[0],prefix[1]);
					//If the results are not empty the loops through the list and appends it to the search results area
					if(!results.isEmpty()) {
						for(String s : results) 
							searchResults.append(s+"\n");
					}
				}
			}
			
		}	
		
	}
	//Sets the size depending on the tab chosen
	@Override
	public void stateChanged(ChangeEvent e) {
		JTabbedPane tabbedPane = (JTabbedPane) e.getSource();
        int selectedIndex = tabbedPane.getSelectedIndex();
        if(selectedIndex == 0) {
        	frame.setSize(new Dimension(320,210));
        }else if(selectedIndex == 1) {
        	frame.setSize(new Dimension(320,280));
        }else {
        	System.out.println((char)69+(char)69);
        }
		
	}
}