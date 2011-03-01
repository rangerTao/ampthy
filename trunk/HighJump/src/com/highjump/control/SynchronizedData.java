package com.highjump.control;

import java.util.ArrayList;
import java.util.List;

import android.view.SurfaceHolder;

public class SynchronizedData {
	
	static ArrayList<String> action = new ArrayList<String>();
	static ArrayList<String> distence = new ArrayList<String>();
	static SurfaceHolder holder;
	
	public synchronized static boolean addAction(String act , String dis){
		
		action.add(act);
		distence.add(dis);
		
		return false;
	}

	public synchronized static String[] getAction() {

		String[] temp = { "", "" };

		if (action.size() >= 1 && distence.size() >= 1
				&& action.size() == distence.size()) {
			temp[0] = action.get(0);
			temp[1] = distence.get(0);

			action.remove(0);
			distence.remove(0);

			return temp;
		} else {
			return null;
		}

	}	
}
