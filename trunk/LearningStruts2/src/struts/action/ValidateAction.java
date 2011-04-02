package struts.action;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;

public class ValidateAction extends ActionSupport implements ServletRequestAware{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String msg ="";
	@Override
	public void setServletRequest(HttpServletRequest arg0) {		
	}
	
	public String execute(){
		System.out.println(SUCCESS);
		return SUCCESS;
	}
	
	public void validate(){
		if(!msg.equalsIgnoreCase("hello")){
			System.out.println(INPUT);
			this.addFieldError("msg.hello","must input hello");
			this.addActionError("Action Error");
		}else{
			this.addActionMessage("Submit Success");
		}
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	

}
