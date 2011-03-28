package struts.action;

import com.opensymphony.xwork2.ActionSupport;

public class FirstAction extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5332018505950629281L;

	private int operate1 = 0;
	private int operate2 = 0;
	
	public String execute(){
		if(getSum()>0){
			return "positive";
		}else{
			return "negative";
		}
	}
	
	public int getOperate1(){
		return operate1;
	}
	
	public int getOperate2(){
		return operate2;
	}
	
	public void setOperate1(int in){
		operate1 = in;
	}
	
	public void setOperate2(int in){
		operate2 = in;
	}
	
	public int getSum(){
		return operate1+operate2;
	}
}
