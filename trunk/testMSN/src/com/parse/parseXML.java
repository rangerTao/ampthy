package com.parse;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class parseXML {
	public static String parsexml(InputStream xml){
		
		
		try {
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			
			DocumentBuilder dBuilder = dbf.newDocumentBuilder();
			
			Document document = dBuilder.parse(xml);
			
			Node node = document.getElementsByTagName("wsse:BinarySecurityToken").item(0).getFirstChild();
			Node secret = document.getElementsByTagName("wst:BinarySecret").item(0).getFirstChild();

			return node.getNodeValue()+" " +secret.getNodeValue();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return "";
		} catch (SAXException e) {
			e.printStackTrace();
			return "";
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
		
		//return xml;
		
	}
	
	public static void main(String args[]){
		//System.out.println(parsexml("E:\\workspace\\testMSN\\conf\\NewFile.xml"));
	}
}
