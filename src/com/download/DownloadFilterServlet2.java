package com.download;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

public class DownloadFilterServlet2 extends HttpServlet{
	
	private final static int max = 11;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
//		String ip = DownloadObserver.getIpAddr(request);
		String ip = request.getParameter("ip");
		
		boolean isLimited = DownloadObserver.isLimited();
		if(isLimited){
				boolean isInPrepare = DownloadObserver.getInstance().isPrepareed(ip);
				if(isInPrepare){//在允许下载列表中
					String msg = "true";
					setResponseContent(response, msg);
					return;
				}else{
					String username = request.getParameter("userid");
					System.out.println(username);
					DownloadObserver.getInstance().addQueue(ip, username);
					String msg = String.valueOf(getFrontSize());
					setResponseContent(response , msg);
					return;
				}
		}else{
			String msg = "true";
			setResponseContent(response, msg);
			return ;
		}
	}
	
	/**
	 * 得到队列前面人数
	 * @return
	 */
	private int getFrontSize(){
		return DownloadObserver.getInstance().getQueueSize();
	}
	
	private void setResponseContent(HttpServletResponse response , String msg) throws IOException{
		response.setContentType("text/html;charset=utf-8");
		response.setHeader("Charset","UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(String.valueOf(msg));
	}
	
//	/**
//	 * 判断是否续传
//	 * @param request
//	 * @return
//	 */
//	private boolean isResume(HttpServletRequest request){
//		String flag = request.getParameter("userid");
//		if(flag != null){
//			return false;
//		}
//		return true;
//	}
	
//	/**
//	 * 判断是否达到最大连接数
//	 * @return
//	 */
//	private boolean isLimited(){
//		DownloadCounter d = DownloadCounter.getInstance();
//		return d.getNumber()==max?true : false;
//	}
	
//	/**
//	 * 得到客户端IP地址
//	 * @param request
//	 * @return
//	 */
//	private String getIpAddr(HttpServletRequest request) {      
//	    String ip = request.getHeader("x-forwarded-for");      
//	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
//	        ip = request.getHeader("Proxy-Client-IP") ;      
//	    }      
//	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
//	        ip = request.getHeader("WL-Proxy-Client-IP");      
//	    }      
//	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
//	        ip = request.getRemoteAddr();      
//	    }      
//	    return ip;      
//	} 
	
}
