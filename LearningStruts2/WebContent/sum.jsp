<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
</br>
<%=request.getRealPath("/")%>
<s:form action="mystruts/sum.action" namespace="/mystruts" method="post">
	<s:textfield name="operate1" label=" 1" />
	<s:textfield name="operate2" label=" 2" />
	<s:submit value="sum" />
</s:form>
<s:form action="mystruts/submin.action" namespace="/mystruts">
	<s:textfield name="msg" label="please input" />
	<s:submit name="save" value="save" method="save" />
	<s:submit name="print" value="print" method="print" />
</s:form>
<br />
Validation
<s:actionerror />
<s:actionmessage />
<s:form action="mystruts/validate.action" namespace="/mystruts">
	<s:textfield name="msg" label="Please Input"></s:textfield>
	<s:fielderror key="msg.hello"></s:fielderror>
</s:form>
</body>
</html>