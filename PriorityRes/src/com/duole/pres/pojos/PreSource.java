package com.duole.pres.pojos;

import java.util.ArrayList;


public class PreSource {
	
	public PreSource(){
		
	}

	// The type of priority resource.
	private String type = "";

	// The bg of pr.
	private String bg;
	
	// The sound to play when answer is right.
	private String audio;
	
	// 
	private String vPic;
	
	//
	private String random;
	
	// The list of item show on the screen.
	private ArrayList<ResourceItem> alResItems = new ArrayList<ResourceItem>();

	// The list of target zone.
	private ArrayList<TargetZone> alTarZone = new ArrayList<TargetZone>();
	
	private Question question;
	
	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public String getRandom() {
		return random;
	}

	public void setRandom(String random) {
		this.random = random;
	}

	public String getAudio() {
		return audio;
	}

	public void setAudio(String audio) {
		this.audio = audio;
	}

	public String getvPic() {
		return vPic;
	}

	public void setvPic(String vPic) {
		this.vPic = vPic;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBg() {
		return bg;
	}

	public void setBg(String bg) {
		this.bg = bg;
	}

	public ArrayList<ResourceItem> getAlResItems() {
		return alResItems;
	}
	
	public ResourceItem getAlResItems(int index) {
		return alResItems.get(index);
	}

	public void addResItems(ResourceItem ri) {
		alResItems.add(ri);
	}

	public ArrayList<TargetZone> getAlTarZone() {
		return alTarZone;
	}
	
	public TargetZone getAlTarZone(int index) {
		return alTarZone.get(index);
	}

	public void addAlTarZone(TargetZone tz) {
		alTarZone.add(tz);
	}

}
