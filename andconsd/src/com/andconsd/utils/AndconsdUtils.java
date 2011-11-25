package com.andconsd.utils;

import java.io.File;

import com.andconsd.constants.Constants;

public class AndconsdUtils {

	/**
	 * Delete a file
	 */
	public static boolean deleteFileByName(String filepath){
		String path = Constants.ROOT_DIR ;
		try{
			if(!filepath.equals("")){
				File file = new File(path + "/"
						+ filepath);
				if(file.exists()){
					file.delete();
				}
			}
			return true;
		}catch(Exception e){
			return false;
		}

	}
}
