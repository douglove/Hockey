package com.douglove.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ActionFile {
	Set<String> mActions = new HashSet<String>();

	public ActionFile(String path){
		readFile(path);
	}
	
	public boolean isAction(String action){
		return mActions.contains(action);
	}

	private void readFile(String path){
		File file = new File(path);

		if(!file.exists()){
			System.out.println("Action file does not exist : " + path);
		}

		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));

			String line;

			while((line = reader.readLine()) != null){
				if(line.trim().length() > 0){
					mActions.add(line.trim());
				}
			}

			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
