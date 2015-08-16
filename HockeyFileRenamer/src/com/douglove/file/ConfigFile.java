package com.douglove.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ConfigFile {
	private String mInitials = "";
	
	public ConfigFile(String path){
		readFile(path);
	}
	
	public String getInitials(){
		return mInitials;
	}

	private void readFile(String path){
		File file = new File(path);

		if(!file.exists()){
			System.out.println("Config file does not exist : " + path);
		}

		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));

			String line;

			while((line = reader.readLine()) != null){
				if(line.trim().length() > 0){
					mInitials = line.trim();
					reader.close();
					return;
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
