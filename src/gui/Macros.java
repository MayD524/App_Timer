package me.sweden.JSONMaybe;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Macros {
	 
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
