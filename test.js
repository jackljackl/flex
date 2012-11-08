
var usr
//function onLinkClick(o){
//	usr = document.getElementById('userid').value
////	document.getElementById('userid').value = usr
//	var file = document.getElementById('file').value
////	file = 'myeclipse6.5.zip'
//	if(!file){
//		alert('先上传')
//		return;
//	}
////	window.open(o.href+"?usr="+usr+"&file="+encodeURI("%E6%9D%82%E8%80%8D%E4%BA%BA%E6%96%B9%E6%B3%95%E7%BB%88%E6%9E%81%E7%89%88%20(1).rar"))
//	var ip = document.getElementById('ip').value
//	window.open(o.href+"?usr="+usr+"&file="+file+"&ip="+ip)
//}

/**
 * 监听服务器推送的消息
 */
function initListen(){
	PL.userId = usr
	PL._init();    
	PL.joinListen("count")
//	PL.joinListen("count")
}

function leaveListen(){
	PL.leave()
}

function onData(event){
	var msg = event.get('msg')
	var usr = document.getElementById('userid').value
	if(msg == usr){
		formSubmit()
		document.getElementById("message").innerHTML="排到头，正在开始下载"
		leaveListen()
	}else{
		document.getElementById('front').innerHTML = howlong-1
	}
	
	
}

var howlong;
function ajaxReq(url ){
	var http_request
	try{
		if (window.XMLHttpRequest) { // Mozilla, Safari, ...
		    http_request = new XMLHttpRequest();
		} else if (window.ActiveXObject) { // IE
		    http_request = new ActiveXObject("Microsoft.XMLHTTP");
		}else{
		}
		http_request.open('GET', encodeURI(url), true);
		http_request.send(null);
		http_request.onreadystatechange = function(){
		
			if (this.readyState == 4) {
		            if (this.status == 200) {
		            	//不需要排队，直接进行下载
		            	if(this.responseText && this.responseText=='true'){
		            		formSubmit()
		            	}
		            	else if(this.responseText ){//排队
		            		var howlong = this.responseText
		            		var value = '连接已达限制,前面还有<span id="front">'+this.responseText+'</span>个用户'
		            		document.getElementById('count').value = this.responseText
		            		document.getElementById("message").innerHTML=value
//		            		if(! hasListen){
	            			initListen()
//		            		}
		            	}
		            }
			  }
		};
	}catch(e){
		alert(e)
	}
}

var hasListen = false;

function formSubmit(){
	var form = document.getElementById('dwonform')
	form.action = 'fordown/'+document.getElementById('file').value+'?ip='+ip
	form.submit()
}

//function fn1(a,b,c,d){
//	  if (this.readyState == 4) {
//	  	alert(this.status)
//            if (this.status == 200) {
//            	if(this.responseText && this.responseText=='true'){
//            		formSubmit()
//            	}
//            	else if(this.responseText ){//排队
////	            	document.getElementById("message").innerHTML=this.responseText
////	            	init()
//            		var value = '连接已达限制,前面还有<span id="front">'+this.responseText+'</span>个用户'
//            		document.getElementById('count').value = this.responseText
//            		document.getElementById("message").innerHTML=value
//            		if(! hasListen){
//            			initListen()
//            		}
//            	}
//            }
//	  }
//}