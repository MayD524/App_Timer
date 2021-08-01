package me.sweden.JSONMaybe;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import org.json.JSONArray;
import org.json.JSONObject;

public class Gui implements ActionListener {
	JFrame frame = new JFrame("App Timer");
	static JTextArea toOutput = new JTextArea();
	JButton refreshBtn;
	public Gui() {
		JScrollPane scrollPane = new JScrollPane(toOutput); 
		toOutput.setEditable(false);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		refreshBtn= new JButton("Refresh");	
		refreshBtn.addActionListener(this); 
		frame.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 100;
		c.gridx = 0;
		c.gridy = 0;
		frame.add(scrollPane,c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = .5;
		c.gridx = 0;
		c.gridy = 1;
		c.ipadx = 1;
		c.ipady = 1;
		frame.setSize(new Dimension(300,190));
		frame.setResizable(false);
		frame.add(refreshBtn,c);
		frame.setVisible(true);
		readJSON();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public static void readJSON() {
		String path = System.getProperty("user.dir")+"\\apps.json";
		System.out.println("Current dir using System:" + path);
		
		try {
			String contents = new String((Files.readAllBytes(Paths.get(path))));
			JSONObject o = new JSONObject(contents);
			Set<String> te = o.keySet();
		 
			for(String t : te) {
				int time = o.getInt(t);
				 
				toOutput.append(t+" : "+time+" sec \n");
				
			}
			
			}catch(Exception e) {
				e.printStackTrace();
			}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == refreshBtn) {
			toOutput.setText("");
			readJSON();
		}
		
	}
}
 