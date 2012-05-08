package com.duole.pres.util;

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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Xml;

import com.duole.pres.PRWordActivity;
import com.duole.pres.PriorityResActivity;
import com.duole.pres.pojos.PRType;
import com.duole.pres.pojos.PreSource;
import com.duole.pres.pojos.Question;
import com.duole.pres.pojos.ResourceItem;
import com.duole.pres.pojos.TargetZone;
import com.duole.pres.pojos.XmlElement;

public class XmlUtil {

	
	public static boolean getPR(Context context ,PRApplication pra,String path){
		
		try {
			readFile(context, pra, path);
			
			if(pra.getPs().getType().equals(PRType.en_word)){
				
				//Start the activity of word.
				Intent intent = new Intent(context, PRWordActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
				return true;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return false;
	}
	
	public static void readFile(Context context ,PRApplication pra,String filePath) throws SAXException, IOException, XmlPullParserException, TransformerException, ParserConfigurationException {

		FileInputStream iStream = new FileInputStream(new File(
				filePath));

		XmlPullParser parser = Xml.newPullParser();
		
		parser.setInput(iStream, "UTF-8");
		
		PreSource ps = new PreSource();
		
		ResourceItem ri = null;
		TargetZone tz = null;
		Question question = null;
		
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
					if(XmlElement.XML_QUESTION.equals(parser.getName())){
						question = new Question();
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
						if(XmlElement.TAR_WRONG.equals(parser.getName())){
							tz.setW_audio(parser.nextText());
						}
					}
					
					if(question != null){
						if(XmlElement.QUES_PIC.equals(parser.getName())){
							question.setThumb(parser.nextText());
						}
						if(XmlElement.QUES_SOUND.equals(parser.getName())){
							question.setSound(parser.nextText());
						}
						if(XmlElement.QUES_POSX.equals(parser.getName())){
							question.setPosx(parser.nextText());
						}
						if(XmlElement.QUES_POSY.equals(parser.getName())){
							question.setPosy(parser.nextText());
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
					if(XmlElement.XML_QUESTION.equals(parser.getName())){
						ps.setQuestion(question);
						question = null;
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
