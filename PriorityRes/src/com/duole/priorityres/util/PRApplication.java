package com.duole.priorityres.util;

import com.duole.priorityres.pojos.PreSource;

import android.app.Application;
import android.util.Log;

public class PRApplication extends Application {

	PreSource ps;

	private String basePath;

	public PreSource getPs() {
		return ps;
	}

	public void setPs(PreSource ps) {
		this.ps = ps;
	}

	public String getBasePath() {
		return basePath == null ? "" : basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	@Override
	public void onCreate() {
		
		basePath = "";
		// TODO Auto-generated method stub
		super.onCreate();
	}
		
}
