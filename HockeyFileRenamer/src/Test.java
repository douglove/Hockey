import java.awt.Dimension;
import java.awt.color.ICC_Profile;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Map;

import org.apache.sanselan.*;
import org.apache.sanselan.common.IImageMetadata;

public class Test {

	public Test(){
		File file = new File("data/testImage.jpg");
		try {
			MetadataExample.metadataExample(file);
		} catch (ImageReadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	
	
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Test t = new Test();
	}

}
