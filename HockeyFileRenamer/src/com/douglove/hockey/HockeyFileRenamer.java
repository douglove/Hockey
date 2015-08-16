package com.douglove.hockey;

import java.io.File;

import javax.swing.JFileChooser;

import com.douglove.file.ActionFile;
import com.douglove.file.ConfigFile;
import com.douglove.file.PlayerFile;
import com.douglove.file.RenameFile;

public class HockeyFileRenamer {
	public HockeyFileRenamer(String imagePath, String configPath, String playerPath, String actionPath){
		JFileChooser chooser = new JFileChooser(imagePath);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		chooser.showOpenDialog(null);
		
		File folder = chooser.getSelectedFile();
		
		if(folder == null){
			return;
		}
		
		System.out.println(folder.getAbsolutePath());
		
		ConfigFile config = new ConfigFile(configPath);
		PlayerFile player = new PlayerFile(playerPath);
		ActionFile action = new ActionFile(actionPath);
		
		File[] files = folder.listFiles();
		
		for(int i = 0; i < files.length; i++){
			File f = files[i];
			
			if(f.isFile() && f.getName().toLowerCase().endsWith(".jpg")){
				RenameFile rf = new RenameFile(f, config.getInitials(), player, action);
			}
		}
	}

	public static void main(String[] args){
		HockeyFileRenamer hfr = new HockeyFileRenamer(args[0], args[1], args[2], args[3]);
	}
}
