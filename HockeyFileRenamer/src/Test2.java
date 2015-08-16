import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.Sanselan;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.formats.jpeg.JpegImageMetadata;
import org.apache.sanselan.formats.jpeg.exifRewrite.ExifRewriter;
import org.apache.sanselan.formats.tiff.TiffField;
import org.apache.sanselan.formats.tiff.TiffImageMetadata;
import org.apache.sanselan.formats.tiff.constants.TagInfo;
import org.apache.sanselan.formats.tiff.constants.TiffConstants;
import org.apache.sanselan.formats.tiff.constants.TiffFieldTypeConstants;
import org.apache.sanselan.formats.tiff.write.TiffOutputDirectory;
import org.apache.sanselan.formats.tiff.write.TiffOutputField;
import org.apache.sanselan.formats.tiff.write.TiffOutputSet;

import com.douglove.exif.XpkeywordsUtil;


public class Test2 {
	/** 
	 * Read metadata from image file and display it. 
	 * @param file 
	 */ 
	public static void readMetaData(File file) { 
		IImageMetadata metadata = null; 
		try { 
			metadata = Sanselan.getMetadata(file); 
		} catch (ImageReadException e) { 
			e.printStackTrace(); 
		} catch (IOException e) { 
			e.printStackTrace(); 
		} 

		if (metadata instanceof JpegImageMetadata) { 
			JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata; 
			System.out.println("\nFile: " + file.getPath()); 

//			printTagValue(jpegMetadata, TiffConstants.TIFF_TAG_XRESOLUTION); 
//			printTagValue(jpegMetadata, TiffConstants.TIFF_TAG_DATE_TIME); 
//			printTagValue(jpegMetadata, TiffConstants.EXIF_TAG_DATE_TIME_ORIGINAL); 
//			printTagValue(jpegMetadata, TiffConstants.EXIF_TAG_CREATE_DATE); 
//			printTagValue(jpegMetadata, TiffConstants.EXIF_TAG_ISO); 
//			printTagValue(jpegMetadata, TiffConstants.EXIF_TAG_SHUTTER_SPEED_VALUE); 
//			printTagValue(jpegMetadata, TiffConstants.EXIF_TAG_APERTURE_VALUE); 
//			printTagValue(jpegMetadata, TiffConstants.EXIF_TAG_BRIGHTNESS_VALUE); 
			printTagValue(jpegMetadata, TiffConstants.EXIF_TAG_XPKEYWORDS); 

			// simple interface to GPS data 
			TiffImageMetadata exifMetadata = jpegMetadata.getExif(); 
			if (exifMetadata != null) { 
				try { 
					TiffImageMetadata.GPSInfo gpsInfo = exifMetadata.getGPS(); 
					if (null != gpsInfo) { 
						double longitude = gpsInfo.getLongitudeAsDegreesEast(); 
						double latitude = gpsInfo.getLatitudeAsDegreesNorth(); 
						System.out.println("    " + "GPS Description: " + gpsInfo); 
						System.out.println("    " + "GPS Longitude (Degrees East): " + longitude); 
						System.out.println("    " + "GPS Latitude (Degrees North): " + latitude); 
					} 
				} catch (ImageReadException e) { 
					e.printStackTrace(); 
				} 
			} 

//			System.out.println("EXIF items -"); 
			//            ArrayList<?> items = jpegMetadata.getItems(); 
			//            for (int i = 0; i < items.size(); i++) { 
			//                Object item = items.get(i); 
			////                System.out.print(((TiffImageMetadata.Item)item).getTiffField()); 
			////                System.out.println("    " + "item: " + item);
			//                System.out.println(((TiffImageMetadata.Item)item).getKeyword() + "       " + ((TiffImageMetadata.Item)item).getText());
			//                
			//            } 
//			ArrayList items = jpegMetadata.getItems();
//			for (int i = 0; i < items.size(); i++)
//			{
//				Object item = items.get(i);
//				System.out.println("	" + "item: " + item);
//			}

			System.out.println(); 
		} 
	} 

	private static void printTagValue(JpegImageMetadata jpegMetadata, TagInfo tagInfo) { 
		TiffField field = jpegMetadata.findEXIFValue(tagInfo); 
		
		if (field == null) { 
			System.out.println(tagInfo.name + ": " + "Not Found."); 
		} else { 
			System.out.println(tagInfo.name + ": " + field.getValueDescription()); 
		} 

		try {
			byte [] b = field.getByteArrayValue();
			
			Set<String> keywords = XpkeywordsUtil.getSetOfKeywords(b);
			
			for(String k : keywords){
				System.out.println(k);
			}
			
			System.out.println();			
			
			System.out.println(XpkeywordsUtil.getTagStringFromList(keywords));
		} catch (ImageReadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		List<String> keywords = Xpkeywords.getListOfKeywords(field.getValue());
//
//		for(String w : keywords){
//			System.out.println(w);
//		}
} 


	/** 
	 * Example of adding an EXIF item to metadata, in this case using ImageHistory field. 
	 * (I have no idea if this is an appropriate use of ImageHistory, or not, just picked 
	 * a field to update that looked like it wasn't commonly mucked with.) 
	 * @param file 
	 */ 
	public static void addXPComment(File file) { 
		File dst = null; 
		IImageMetadata metadata = null; 
		JpegImageMetadata jpegMetadata = null; 
		TiffImageMetadata exif = null; 
		OutputStream os = null; 
		TiffOutputSet outputSet = new TiffOutputSet(); 

		// establish metadata 
		try { 
			metadata = Sanselan.getMetadata(file); 
		} catch (ImageReadException e) { 
			e.printStackTrace(); 
		} catch (IOException e) { 
			e.printStackTrace(); 
		} 

		// establish jpegMedatadata 
		if (metadata != null) { 
			jpegMetadata = (JpegImageMetadata) metadata; 
		} 

		// establish exif 
		if (jpegMetadata != null) { 
			exif = jpegMetadata.getExif(); 
		} 

		// establish outputSet 
		if (exif != null) { 
			try { 
				outputSet = exif.getOutputSet(); 
			} catch (ImageWriteException e) { 
				e.printStackTrace(); 
			} 
		} 

		if (outputSet != null) {         
			// check if field already EXISTS - if so remove         
			TiffOutputField xpCommentPre = outputSet.findField(TiffConstants.EXIF_TAG_XPKEYWORDS); 

			if (xpCommentPre != null) { 
				System.out.println("REMOVE"); 
				outputSet.removeField(TiffConstants.EXIF_TAG_XPKEYWORDS); 
			}   
			
			// add field 
			try {   
				String fieldData = "Hallo"; 
				TiffOutputField xpComment = new TiffOutputField(TiffConstants.EXIF_TAG_XPKEYWORDS, TiffFieldTypeConstants.FIELD_TYPE_BYTE, fieldData.length(), fieldData.getBytes());           
				TiffOutputDirectory exifDirectory = outputSet.getOrCreateExifDirectory(); 
				exifDirectory.add(xpComment); 
			} catch (ImageWriteException e) { 
				e.printStackTrace(); 
			} 
		} 

		try { 
			dst = new File("data/testImage2.jpg"); 
			os = new FileOutputStream(dst); 
			os = new BufferedOutputStream(os); 
		} catch (IOException e) { 
			e.printStackTrace(); 
		} 

		// write/update EXIF metadata to output stream 
		try { 
			new ExifRewriter().updateExifMetadataLossless(file, os, outputSet); 
		} catch (ImageReadException e) { 
			e.printStackTrace(); 
		} catch (ImageWriteException e) { 
			e.printStackTrace(); 
		} catch (IOException e) { 
			e.printStackTrace(); 
		} finally { 
			if (os != null) { 
				try { 
					os.close(); 
				} catch (IOException e) { 
				} 
			} 
		} 

		//        file.delete(); 
		// dst.renameTo(file);         
	} 



	public static void main(String[] args) { 
		File root = new File("");
		File file = new File(root.getAbsolutePath() + "/data/testImage.jpg");
		System.out.println(file.getAbsolutePath());
		//                readMetaData(file); 
		addXPComment(file); 
		readMetaData(file); 
	} 


}
