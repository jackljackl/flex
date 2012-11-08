<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8" language="java"  %> 
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="swfobject.js"></script>
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript" src="ajax-pushlet-client.js"></script>
	<script type="text/javascript" src="test.js"></script>
	
  </head>
  
  <body>
  	<div id="message">正在准备下载...</div>
  	<div id="count"></div>
  	<div id="file"></div>
  	<form id="dwonform" action="" method="post">
  		<input id="userid" name="userid" type="hidden"></input>
  		<input name="flag" type="hidden" value="true">
  	</form>
  </body>
  <script type="text/javascript">
		usr = '<%=request.getParameter("usr")%>'
		document.getElementById('userid').value = usr
		var file = '<%=request.getParameter("file")%>'
		var ip = '<%=request.getParameter("ip")%>'
		document.getElementById('file').value = file
		
		ajaxReq('for/?userid='+usr+'&ip='+ip)
	</script>
</html>
