package com.duole.pres.pojos;

public class ResourceItem {

	// The index of current item.used to locate the position of items.
	private String index = "";
	// The path of thumb of item.
	private String pic = "";
	// The sound effect when item is clicked.
	private String audio = "";
	// The position of where to locate item.
	private String posx = "";
	private String posy = "";
	// Whether the item is dragable.
	private String dragable = "";
	// When item is clicked
	private String picClicked = "";
	// is this item the answer.
	private String isAnswer = "";
	// the target of this item.
	private String tIndex = "";
	
	public String getIsAnswer() {
		return isAnswer;
	}

	public void setIsAnswer(String isAnswer) {
		this.isAnswer = isAnswer;
	}

	public String gettIndex() {
		return tIndex;
	}

	public void settIndex(String tIndex) {
		this.tIndex = tIndex;
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

	public String isDragable() {
		return dragable;
	}

	public void setDragable(String dragable) {
		this.dragable = dragable;
	}

	public String getPicClicked() {
		return picClicked;
	}

	public void setPicClicked(String picClicked) {
		this.picClicked = picClicked;
	}

	@Override
	public String toString() {

		return "The resource item in the position of " + getIndex()
				+ " contains following info:" + "The path of thumb: "
				+ getPic() + " \n" + "The path of sound effect when clicked: "
				+ getAudio() + " \n" + "The position where to locate it: "
				+ getPosx() + "*" + getPosy() + "\n"
				+ "Whether the item is dragable:" + isDragable();
	}

}
