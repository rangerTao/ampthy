package struts.action;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;

public class MultiAction extends ActionSupport implements ServletRequestAware{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6980414003373424597L;

	private String msg = "";
	private HttpServletRequest request;
	
	public void setServletRequest(HttpServletRequest servlet){
		this.request = servlet;
	}
	
	public String execute(){
		return msg;
	}
	
	public String save(){
		request.setAttribute("result", "baocun");
		return "save";
	}
	
	public String print(){
		request.setAttribute("print", "print");
		return "print";
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
	
}
