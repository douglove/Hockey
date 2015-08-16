package com.douglove.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PlayerFile {
	private Map<String, String> mNumberNameMap = new HashMap<String, String>();

	public PlayerFile(String path){
		readFile(path);
	}

	public String getName(String number){
		return mNumberNameMap.get(number);
	}
	
	public boolean isName(String number){
		return (getName(number) != null);
	}

	private void readFile(String path){
		File file = new File(path);

		if(!file.exists()){
			System.out.println("Player file does not exist : " + path);
		}

		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));

			String line;

			while((line = reader.readLine()) != null){
				String[] lineArr = line.split("\\|");

				if(lineArr.length == 2){
					mNumberNameMap.put(lineArr[0].trim(), lineArr[1].trim());
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
