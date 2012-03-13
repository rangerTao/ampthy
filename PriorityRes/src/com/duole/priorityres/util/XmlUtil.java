package com.duole.priorityres.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Xml;

import com.duole.priorityres.PRWordActivity;
import com.duole.priorityres.PriorityResActivity;
import com.duole.priorityres.pojos.PRType;
import com.duole.priorityres.pojos.PreSource;
import com.duole.priorityres.pojos.ResourceItem;
import com.duole.priorityres.pojos.TargetZone;
import com.duole.priorityres.pojos.XmlElement;

public class XmlUtil {

	
	public static boolean getPR(String path){
		
		PRApplication pra = (PRApplication) PriorityResActivity.appref.getApplication();
		
		try {
			readFile(path);
			
			if(pra.getPs().getType().equals(PRType.en_word)){
				
				//Start the activity of word.
				Intent intent = new Intent(PriorityResActivity.appref, PRWordActivity.class);
				PriorityResActivity.appref.startActivity(intent);
				return true;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return false;
	}
	
	public static void readFile(String filePath) throws SAXException, IOException, XmlPullParserException, TransformerException, ParserConfigurationException {

		PRApplication pra = (PRApplication) PriorityResActivity.appref.getApplication();
		
		FileInputStream iStream = new FileInputStream(new File(
				filePath));

		XmlPullParser parser = Xml.newPullParser();
		
		parser.setInput(iStream, "UTF-8");
		
		PreSource ps = new PreSource();
		
		ResourceItem ri = null;
		TargetZone tz = null;
		
		int event = parser.getEventType();
		try{
			while(event!=XmlPullParser.END_DOCUMENT){
				switch(event){
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					if(XmlElement.XML_RES.equals(parser.getName())){
						ri = new ResourceItem();
					}
					if(XmlElement.XML_TARGET.equals(parser.getName())){
						tz = new TargetZone();
					}
					
					if(ri != null){
						
						if(XmlElement.RES_INDEX.equals(parser.getName())){
							ri.setIndex(parser.nextText());
						}
						if(XmlElement.RES_PIC.equals(parser.getName())){
							ri.setPic(parser.nextText());
						}
						if(XmlElement.RES_AUDIO.equals(parser.getName())){
							ri.setAudio(parser.nextText());
						}
						if(XmlElement.RES_POSX.equals(parser.getName())){
							ri.setPosx(parser.nextText());
						}
						if(XmlElement.RES_POSY.equals(parser.getName())){
							ri.setPosy(parser.nextText());
						}
						if(XmlElement.RES_DRAG.equals(parser.getName())){
							ri.setDragable(parser.nextText());
						}
						if(XmlElement.RES_PIC_CLICKED.equals(parser.getName())){
							ri.setPicClicked(parser.nextText());
						}
						if(XmlElement.RES_ISANSWER.equals(parser.getName())){
							ri.setIsAnswer(parser.nextText());
						}
						if(XmlElement.RES_TINDEX.equals(parser.getName())){
							ri.settIndex(parser.nextText());
						}
					}
					
					if(tz != null){
						if(XmlElement.TAR_INDEX.equals(parser.getName())){
							tz.setIndex(parser.nextText());
						}
						if(XmlElement.TAR_PIC.equals(parser.getName())){
							tz.setPic(parser.nextText());
						}
						if(XmlElement.TAR_AUDIO.equals(parser.getName())){
							tz.setAudio(parser.nextText());
						}
						if(XmlElement.TAR_POSX.equals(parser.getName())){
							tz.setPosx(parser.nextText());
						}
						if(XmlElement.TAR_POSY.equals(parser.getName())){
							tz.setPosy(parser.nextText());
						}
						if(XmlElement.TAR_WIDTH.equals(parser.getName())){
							tz.setWidth(parser.nextText());
						}
						if(XmlElement.TAR_HEIGHT.equals(parser.getName())){
							tz.setHeight(parser.nextText());
						}
						if(XmlElement.TAR_COUNT.equals(parser.getName())){
							tz.setCount(parser.nextText());
						}
					}
					
					
					if (XmlElement.XML_PRETYPE.equals(parser.getName())) {
						ps.setType(parser.nextText());
					}
					if (XmlElement.XML_BG.equals(parser.getName())) {
						ps.setBg(parser.nextText());
					}
					if(XmlElement.XML_AUDIO.equals(parser.getName())){
						ps.setAudio(parser.nextText());
					}
					if(XmlElement.XML_V_PIC.equals(parser.getName())){
						ps.setvPic(parser.nextText());
					}
					if(XmlElement.XML_RANDOM.equals(parser.getName())){
						ps.setRandom(parser.nextText());
					}
					break;
				case XmlPullParser.END_TAG:
					if(XmlElement.XML_RES.equals(parser.getName())){
						ps.addResItems(ri);
						ri = null;
					}
					if(XmlElement.XML_TARGET.equals(parser.getName())){
						ps.addAlTarZone(tz);
						tz = null;
					}
					break;
					
				}
				event = parser.next();
			}
			
			pra.setPs(ps);
			
		}catch (Exception e){
			e.printStackTrace();

		}
	}
}
