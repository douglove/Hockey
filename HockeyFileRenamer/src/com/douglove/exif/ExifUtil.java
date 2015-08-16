package com.douglove.exif;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.common.ImageMetadata.Item;
import org.apache.sanselan.formats.jpeg.JpegImageMetadata;
import org.apache.sanselan.formats.tiff.TiffField;
import org.apache.sanselan.formats.tiff.TiffImageMetadata;
import org.apache.sanselan.formats.tiff.constants.TiffConstants;


public class ExifUtil {
	public static Integer YEAR = 0;
	public static Integer YEAR_MONTH = 1;
	public static Integer YEAR_MONTH_DAY = 2;
	public static Integer MONTH_DAY_YEAR = 3;
	
	/**
	 * Return the jpeg image metadata from the passed in file path
	 * Return null if file does not have a jpeg image metadata 
	 */
	public static JpegImageMetadata getJpegImageMetadata(String path) throws ImageReadException, IOException{
		return getJpegImageMetadata(new File(path));
	}
	
	/**
	 * Return the jpeg image metadata from the passed in file
	 * Return null if file does not have a jpeg image metadata 
	 */
	public static JpegImageMetadata getJpegImageMetadata(File file) throws ImageReadException, IOException{
		IImageMetadata metadata = Sanselan.getMetadata(file);
		

		if (metadata instanceof JpegImageMetadata){
			return (JpegImageMetadata) metadata;
		}
		else{
			return null;
		}
	}

	/**
	 * Return a set of keywords
	 */
	public static Set<String> getKeywords(JpegImageMetadata metadata){
		TiffField field = metadata.findEXIFValue(TiffConstants.EXIF_TAG_XPKEYWORDS); 
		
		List l = metadata.getItems();
		
		if (field == null) { 
			return new HashSet<String>();
		} else { 
			byte[] b;
			try {
				b = field.getByteArrayValue();
				return XpkeywordsUtil.getSetOfKeywords(b);
			} catch (ImageReadException e) {
				e.printStackTrace();
				return new HashSet<String>();
			}			
		} 
	}

	/**
	 * Return a map containing the tag info to value mapping
	 */
	public static Map<String, String> getExifData(JpegImageMetadata metadata){
		Map<String, String> resultMap = new HashMap<String, String>();

		ArrayList items = metadata.getItems();
		for (int i = 0; i < items.size(); i++)
		{
			if(items.get(i) instanceof Item){
				Item item = (Item)items.get(i);
				addValueToMap(item, resultMap);
			}
			else{
				System.out.println("..... " + items.get(i) + "     " + items.get(i).getClass().toString());
			}	
		}

		return resultMap;
	}
	
	public static Set<String> getExifKeywords(JpegImageMetadata metadata){
		Set<String> keywords = new HashSet<String>();

		ArrayList items = metadata.getItems();
		for (int i = 0; i < items.size(); i++)
		{
			if(items.get(i) instanceof Item){
				Item item = (Item)items.get(i);
				
				if(item.getKeyword().compareToIgnoreCase("keywords") == 0){
					keywords.add(item.getText());
				}
			}
			else{
				System.out.println("..... " + items.get(i) + "     " + items.get(i).getClass().toString());
			}	
		}

		return keywords;
	}

	//	/**
	//	 * Add the tagInfo and exif data value to the result map
	//	 */
	//	private static void addValueToMap(JpegImageMetadata jpegMetadata, TagInfo tagInfo, Map<String, String> resultMap)throws ImageReadException, IOException{
	//		TiffField field = jpegMetadata.findEXIFValue(tagInfo);
	//		if (field != null){
	//			System.out.println("add tag : " + tagInfo.getDescription() + " = " + field.getValueDescription());
	//			resultMap.put(tagInfo.getDescription(), field.getValueDescription());
	//		}
	//		else{
	//			System.out.println("tag not available " + tagInfo.getDescription());
	//		}
	//	}

	/**
	 * Add item keyword and value to the passed in map
	 */
	private static void addValueToMap(Item item, Map<String, String> resultMap){
		resultMap.put(item.getKeyword(), item.getText());
	}

	/**
	 * Return a set of date parts from the passed in date string
	 * 
	 * example date = '2009:11:27 19:32:27'
	 * 
	 * result list = 2009, 200911, 20091127
	 */
	public static Map<Integer, String> getDateSet(String date){
		Map<Integer, String> result = new HashMap<Integer, String>();

		if(date == null){
			return result;
		}

		String year = date.substring(1, 5);
		String month = date.substring(6, 8);
		String day = date.substring(9, 11);

		result.put(YEAR, year);
		result.put(YEAR_MONTH, year + month);
		result.put(YEAR_MONTH_DAY, year + month + day);
		result.put(MONTH_DAY_YEAR, month + day + year.substring(2, 4));

		return result;
	}
	
//	private String padZero(String number, int numberOfDigits){
//		if(number.length() < numberOfDigits){
//			StringBuilder s = new StringBuilder();
//			
//			for(int i = 0; i < numberOfDigits - number.length(); i++){
//				s.append("0");
//			}
//			s.append(number);
//			
//			return s.toString();
//		}
//		else{
//			return number;
//		}
//	}
	
	public static void main(String[] args){
		try {
			JpegImageMetadata metadata = ExifUtil.getJpegImageMetadata("./data/testImage.jpg");
			Map<String, String> tagMap = ExifUtil.getExifData(metadata);
			Set<String> keywords = ExifUtil.getKeywords(metadata);

			String createDate = tagMap.get("Create Date");
			Map<Integer, String> dateList = ExifUtil.getDateSet(createDate);

			keywords.addAll(dateList.values());

			for(String k : keywords){
				System.out.println("kw : " + k);
			}
		} catch (ImageReadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
