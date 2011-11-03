package com.andconsd.constants;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.Bitmap;

public class Constants {
	public static final String ROOT_DIR = "/sdcard/DuoleCache/picture";
	public static final String BLANK_LINE = "\r\n\r\n";
	public static final String FILE_NOT_FOUND_TEXT = "File not found!";
	public static final int DEFAULT_PORT = 8080;
	
	public static final long apCountDown = 1000 * 30;
	
	public static boolean service_running = true;
	
	public static HashMap<String,SoftReference<Bitmap>> ImgCache = new HashMap<String, SoftReference<Bitmap>>();
	
	public static ArrayList<File> files = new ArrayList<File>();
}
