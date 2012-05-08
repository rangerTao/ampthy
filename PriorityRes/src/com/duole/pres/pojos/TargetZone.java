package com.duole.pres.pojos;

import java.util.StringTokenizer;

import android.R.integer;

public class TargetZone {

	// The index of target zone.If there are more than two zone.is's used to
	// locate zone.
	private String index = "";
	// The path of thumb of zone.
	private String pic = "";
	// The sound effect when drag the item into the zone.
	private String audio = "";
	private String w_audio = "";
	// The position of zone.
	private String posx = "";
	private String posy = "";
	// The size of the zone.
	private String width = "";
	private String height = "";
	private int count = 0;
	private int itemCount = 0;
	
	public String getW_audio() {
		return w_audio;
	}

	public void setW_audio(String w_audio) {
		this.w_audio = w_audio;
	}

	public int getItemCount() {
		return itemCount;
	}

	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}

	public int getCount() {
		return count;
	}

	public void setCount(String count) {
		try{
			this.count = Integer.parseInt(count);
		}catch (Exception e) {
			this.count = 0;
		}
		
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getAudio() {
		return audio;
	}

	public void setAudio(String audio) {
		this.audio = audio;
	}

	public String getPosx() {
		return posx;
	}

	public void setPosx(String posx) {
		this.posx = posx;
	}

	public String getPosy() {
		return posy;
	}

	public void setPosy(String posy) {
		this.posy = posy;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}
	
	@Override
	public String toString() {

		return "The target zone contains following info:" + "The path of thumb: "
				+ getPic() + " \n" + "The path of sound effect when clicked: "
				+ getAudio() + " \n" + "The position where to locate it: "
				+ getPosx() + "*" + getPosy() 
				+ "\nThe size of zone : " + getWidth() + "*" + getHeight();
	}

}
