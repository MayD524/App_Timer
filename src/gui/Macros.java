package me.sweden.JSONMaybe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Macros {
	 
	public int createMacro(String path, String cmd) {
		//Getting the amount of lines
		int lines = 0;
		BufferedReader reader;
		//Content for writing to the file again
		ArrayList<String> content = new ArrayList<>();
		try {
			//Read the file
			reader = new BufferedReader(new FileReader(path));
			String line = reader.readLine(); 
			//Add it to content
			content.add(line);
			//Count the lines
			while (line != null) {
				line = reader.readLine();
				content.add(line);
				lines++;
			} 
			//Add 1 cause that is the next one
			lines++;
			//Create the string for the macro
			String macroStr = lines+": "+cmd+" %name%";
			content.add(macroStr);
			reader.close();
			//Creating a file writer
			FileWriter fWriter = new FileWriter(path);
			//Getting the null string 
			int emptyNr = 0;
			for(String toOut : content) {
				if(toOut == null)
					break;
				emptyNr++;
			}
			//Removing the string that is null
			content.remove(emptyNr);
			emptyNr = 0;
			//Write it to the file
			for (String toOut : content) {
				fWriter.write(toOut+"\n");
			} 
			System.out.println("Successfully wrote to the file.");
			fWriter.close();
		 
		} catch (IOException e) {
			System.out.println((char)69);
			e.printStackTrace();
		}
		
		
		return content.size();
	} 
	public String[] readMacros(String path, String cmd, int line) {
		//Getting the file 
		File file = new File(path);
		Scanner sc;
		//Creating some strings and string arrays
		String test = "";
		String[] corrStr = new String[1];
		String tempStr = "";
		
		//Check if file exists
		if(!file.exists()) {
			System.out.println((char)69);
		}
		try {
			//Telling the scanner to read the file
			sc = new Scanner(file);
			//Check if there is any lines 
			while (sc.hasNextLine()) {
				//toDo is important for stuff
				String[] toDo = sc.nextLine().split(":" );
				//Check if the line is existing
				if(!Gui.isNumeric(toDo[0]))
					break;
				if(Integer.parseInt(toDo[0]) == line) {
					//Removing the space
					String i = toDo[1].replaceFirst(" ", "");
					test += i+toDo[1].replaceFirst("%name%", cmd);
					//Creating a new string array
					String[] newStr = test.split(" ");
					//Needed to start at pos 3 in the array otherwise it won't workaaaaaa
					for(int x = 2; x < newStr.length; x++) {
						tempStr += newStr[x]+" ";
						 
					}
					//Creating temporary array
					String[] tempArr = tempStr.split(" ");
					//Setting correct string to tmp array
					corrStr = tempArr; 
					test = "";
				
				}
			}		 	 
		} catch (FileNotFoundException e) {	 
			e.printStackTrace();
		}
		
		
 
		return corrStr;
	}
}
