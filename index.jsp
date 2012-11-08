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
  </head>
  
  <body>
  	<script type="text/javascript">
  		swfobject.embedSWF("bin_debug/flex.swf","upload","500","200","10.0.0"); 
  		
  		var setUploadFileName = function(name){
			document.getElementById('file').value=name
  		}  
  		
  		//var submitt = function(){
  			//var form = document.getElementById('form')
  			//form.action = "downing/"+encodeURI(document.getElementById('fileName').value)
  			//form.submit()
  		//}
  		
  		function onLinkClick(o){
			var usr = document.getElementById('userid').value
		//	document.getElementById('userid').value = usr
			var file = document.getElementById('file').value
			//file = 'myeclipse6.5.zip'
			if(!file){
				alert('先上传')
				return;
			}
		//	window.open(o.href+"?usr="+usr+"&file="+encodeURI("%E6%9D%82%E8%80%8D%E4%BA%BA%E6%96%B9%E6%B3%95%E7%BB%88%E6%9E%81%E7%89%88%20(1).rar"))
			var ip = document.getElementById('ip').value
			window.open(o.href+"?usr="+usr+"&file="+file+"&ip="+ip)
		}
  	</script>
  	<div>
  		<div style="margin-left: 400;">
  			<div id="upload" ></div>
  		</div>
  	</div>
  	<!-- 
    <embed style="margin-left: 400" width="500" height="200" src="bin_debug/flex.swf"></embed>
     --> 
    <hr>
    用户名<input type="text" id="userid">
    IP<input id="ip" type="text">
    <a target="_blank" href="download.jsp" onclick="onLinkClick(this);return false;">下载</a>
    <input id="file" type="hidden">
    
    <hr>
    <!-- 
    <form id="form" method="post" action="" onsubmit="return false;">
    	<input type="submit" value="下 载"  onclick="submitt();"/>
    	<input id="fileName" type="hidden" name="fileName">
    </form>
     -->
  </body>
</html>
