package com.douglove.exif;

import java.util.HashSet;
import java.util.Set;

public class XpkeywordsUtil {
	public static Set<String> getSetOfKeywords(String asciiInput){
		Set<String> results = new HashSet<String>();

		// split on the comma character
		String[] parts = asciiInput.split(",");

		// string builder
		StringBuilder s = new StringBuilder();

		// loop over the string array of the parts
		// skip every other entry as it is just a zero
		for(int i = 0; i < parts.length; i+=2){
			// string to byte
			byte v = Byte.valueOf(parts[i].trim());

			// byte to char
			char c = (char)v;

//			System.out.println(c + "   " + v);

			// match the divider character ";"
			if(v == 59){
				results.add(s.toString());

				// create a new string builder for the next keyword
				s = new StringBuilder();
			}
			else{
				// add the char to the string
				s.append(c);
			}
		}

		if(s.length() > 0){
			// if the string builder is not empty the add it to the results list
			results.add(s.toString());
		}

		return results;
	}
	
	public static Set<String> getSetOfKeywords(byte[] byteArrayInput){
		Set<String> results = new HashSet<String>();

		// string builder
		StringBuilder s = new StringBuilder();

		// loop over the string array of the parts
		// skip every other entry as it is just a zero
		for(int i = 0; i < byteArrayInput.length; i+=2){
			// string to byte
			byte v = byteArrayInput[i];

			// byte to char
			char c = (char)v;

//			System.out.println(c + "   " + v);
			
			// if the number equals zero then skip
			if(v == 0){
				continue;
			}

			// match the divider character ";"
			if(v == 59){
				results.add(s.toString());

				// create a new string builder for the next keyword
				s = new StringBuilder();
			}
			else{
				// add the char to the string
				s.append(c);
			}
		}

		if(s.length() > 0){
			// if the string builder is not empty the add it to the results list
			results.add(s.toString());
		}

		return results;		
	}

	public static String getTagStringFromList(Set<String> tagList){
		// string builder
		StringBuilder s = new StringBuilder();

		// first character flag
		boolean firstChar = true;

		// loop over the tag list
		for(String tag : tagList){
			// for each tag in the list loop over the tag length
			for(int i = 0; i < tag.length(); i++){
				// get the char at the index i of the tag string
				char c = tag.charAt(i);

				// char to int
				int v = c;

				if(!firstChar){
					// if not the first character then append a comma
					s.append(", ");
				}
				else{
					// clear the first character flag if it is the first character
					firstChar = false;
				}

				// append the ascii number for the char and a zero
				s.append(v + ", 0");
			}

			// append the ascii number for the semicolon separator
			s.append(", 59, 0");
		}

		return s.toString();
	}
	
	public static void main(String[] args){
		String s = "50, 0, 48, 0, 48, 0, 57, 0, 49, 0, 49, 0, 59, 0, 50, 0, 48, 0, 48, 0, 57, 0, 59, 0, 104, 0, 111, 0, 99, 0, 107, 0, 101, 0, 121, 0, 59, 0, 116, 0, 114, 0, 105, 0, 32, 0, 99, 0, 105, 0";
		System.out.println(s);
		
		Set<String> keywords = XpkeywordsUtil.getSetOfKeywords(s);

		for(String w : keywords){
			System.out.println(w);
		}

		String t = XpkeywordsUtil.getTagStringFromList(keywords);
		System.out.println(t);

		keywords = XpkeywordsUtil.getSetOfKeywords(t);

		for(String w : keywords){
			System.out.println(w);
		}}
}

