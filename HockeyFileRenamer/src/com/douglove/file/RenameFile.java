package com.douglove.file;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.formats.jpeg.JpegImageMetadata;
import org.apache.sanselan.formats.tiff.TiffField;
import org.apache.sanselan.formats.tiff.constants.TagInfo;

import com.douglove.exif.ExifUtil;

public class RenameFile {
	private Set<String> mPlayers = new HashSet<String>();
	private Set<String> mActions = new HashSet<String>();
	private String mDate = "";
	private String mFileName = "";
	
	public RenameFile(File file, String initials, PlayerFile player, ActionFile action){
		try {
			if(!file.exists()){
				System.out.println("File does not exist - " + file.getAbsolutePath());
				return;
			}
			
			mFileName = file.getName();//.substring(0, file.getName().length() - 4);
//			System.out.println(mFileName);
			
//			System.out.println("Initials - " + initials);
			
			JpegImageMetadata metadata = ExifUtil.getJpegImageMetadata(file);
			Map<String, String> tagMap = ExifUtil.getExifData(metadata);
			Set<String> keywords = ExifUtil.getKeywords(metadata);
			
			if(keywords.size() == 0){
				keywords = ExifUtil.getExifKeywords(metadata);
			}

			String createDate = tagMap.get("Create Date");
			Map<Integer, String> dateList = ExifUtil.getDateSet(createDate);
			
			mDate = dateList.get(ExifUtil.MONTH_DAY_YEAR);

			keywords.add(mDate);
//			System.out.println("Date - " + mDate);

			for(String k : keywords){
				if(player.isName(k)){
					String name = player.getName(k);
					mPlayers.add(name);
//					System.out.println("name : " + k + " - " + name);
				}
				
				if(action.isAction(k)){
					mActions.add(k);
//					System.out.println("action : " + k);
				}

			}
		} catch (ImageReadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		StringBuilder s = new StringBuilder();
		s.append(mDate + "-" + initials + "-");

		for(String n : mPlayers){
			s.append(n + "-");
		}

		for(String a : mActions){
			s.append(a + "-");
		}

		s.append(mFileName);

		System.out.println(s.toString());
		
		File renamedFile = new File(file.getParentFile().getAbsolutePath() + "/" + s.toString());
		
		file.renameTo(renamedFile);
	}
	
	public static void main(String[] args){
		PlayerFile p = new PlayerFile("./data/playerFile.txt");
		ActionFile a = new ActionFile("./data/actionFile.txt");
		File f = new File("./data/testImage.jpg");
		RenameFile r = new RenameFile(f, "dvl", p, a);
	}
}
