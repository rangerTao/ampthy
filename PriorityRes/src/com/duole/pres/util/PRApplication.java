package com.duole.pres.util;

import com.duole.pres.pojos.PreSource;

import android.app.Application;
import android.util.Log;

public class PRApplication extends Application {

	PreSource ps;

	private String pkgname;

	private String basePath;

	public String getPkgname() {
		return pkgname;
	}

	public void setPkgname(String pkgname) {
		this.pkgname = pkgname;
	}

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
