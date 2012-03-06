package com.duole.priorityres.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.duole.priorityres.pojos.PRType;
import com.duole.priorityres.pojos.XmlElement;

public class XmlUtil {

	
	public static boolean getTypeOfPR(String path){
		
		String type = readNodeValue(path, XmlElement.XML_PRETYPE);
		
		if(type.equals(PRType.en_word)){
			
			
			return true;
		}
		
		return false;
	}
	
	public static String readNodeValue(String file,String nodename){
		try {
			FileInputStream iStream = new FileInputStream(file);
			
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(iStream, "UTF-8");
			int event = parser.getEventType();
			
			while(event!=XmlPullParser.END_DOCUMENT){
				switch(event){
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					if (nodename.equals(parser.getName())) {
						return parser.nextText();
					}
					break;
				case XmlPullParser.END_TAG:
					if(XmlElement.XML_END.equals(parser.getName())){
					}
					break;
					
				}
				event = parser.next();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "";
	}
}
