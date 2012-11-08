package com;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.Queue;
 
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.download.DownloadCounter;
import com.download.DownloadObserver;
 
/**
 * Servlet implementation class Download
 */
public class DownloadServlet extends HttpServlet {
private static final long serialVersionUID = 1L;
 
/**
* @see HttpServlet#HttpServlet()
*/
public DownloadServlet() {
super();
}

public static void main(String[] args) throws UnsupportedEncodingException {  
	String ss = "[小王子].(法)圣埃克苏佩里.插图版.pdf";
	System.out.println(URLEncoder.encode(ss,"UTF-8"));
    String aa  =  "java%C3%A5%C2%85%C2%AD%C3%A5%C2%A4%C2%A7%C3%A5%C2%BF%C2%85%C3%A9%C2%A1%C2%BB%C3%A7%C2%90%C2%86%C3%A8%C2%A7%C2%A3%C3%A7%C2%9A%C2%84%C3%A9%C2%97%C2%AE%C3%A9%C2%A2%C2%98-%C3%A6%C2%9D%C2%A5%C3%A8%C2%87%C2%AA%C3%A7%C2%BD%C2%91%C3%A7%C2%BB%C2%9C%C3%A5%C2%B0%C2%91%C3%A8%C2%AE%C2%B8%C3%A6%C2%9C%C2%89%C3%A8%C2%AF%C2%AF.txt";
    String bb  =  "%5B%E5%B0%8F%E7%8E%8B%E5%AD%90%5D.%28%E6%B3%95%29%E5%9C%A3%E5%9F%83%E5%85%8B%E8%8B%8F%E4%BD%A9%E9%87%8C.%E6%8F%92%E5%9B%BE%E7%89%88.pdf";

    System.out.println(new String(URLDecoder.decode(aa,"UTF-8").getBytes("ISO-8859-1"),"UTF-8"));
}  

public String getIpAddr(HttpServletRequest request) {      
    String ip = request.getHeader("x-forwarded-for");      
    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
        ip = request.getHeader("Proxy-Client-IP") ;      
    }      
    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
        ip = request.getHeader("WL-Proxy-Client-IP");      
    }      
    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
        ip = request.getRemoteAddr();      
    }      
    return ip;      
} 
 
@Override
protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException,SocketException {
	
	OutputStream os=null;
	InputStream is=null;
//	String ip = getIpAddr(request);
	String ip = request.getParameter("ip");
	try {
//		response.setCharacterEncoding("GBK");
		String requestUrl = new String(URLDecoder.decode(request.getRequestURI(),"UTF-8").getBytes("ISO-8859-1"),"UTF-8");
		System.out.println("解码前"+request.getRequestURI());
		System.out.println("解码后" + requestUrl);
		System.out.println("IP:"+getIpAddr(request));
//		request.set
		if (requestUrl.endsWith("/down/") || requestUrl.endsWith("/down")) {
			response.sendRedirect("/flex/downloadErr.jsp");
//			PrintWriter pw = response.getWriter();
//			Error404(pw);
			return;
		}
// 获取文件
//String filePath = this.getServletConfig().getServletContext().getRealPath("/")+"upload/" + requestUrl.substring(11,requestUrl.length());
		String filePath  =  ConstantsConfig.filePath +"\\"+ requestUrl.substring(11,requestUrl.length());
System.out.println("文件位置:"+filePath);
//		String fileName = (String) request.getParameter("fileName");
//		if(fileName==null || fileName.equals("")){
//			response.sendRedirect("/flex/downloadErr.jsp");
//			request.getRequestDispatcher("downloadErr.jsp").forward(request, response);
//			return;
//		}
//		String filePath = "d:\\upload\\"+fileName;
//		System.out.println("文件路径：" + filePath);
		File file = new File(filePath);
		if (!file.exists()) {
			System.out.println("文件“" + file.getAbsolutePath() + "不存在!");
			PrintWriter pw = response.getWriter();
			Error404(pw);
			pw.close();
			return;
		}
		os = response.getOutputStream();
		long start = 0;
		String range = request.getHeader("range");
		if (range != null) {
//			System.out.println("range=" + range);
			String rg = range.split("=")[1];
			start = Long.parseLong(rg.split("-")[0]);
			response.setStatus(206);
		}
		response.setContentType("application/octet-stream;charset=UTF-8");
		response.setHeader("Accept-Ranges", "bytes");
		response.setHeader("Content-Range", "bytes  " + start + "-" + (file.length() - 1) + "/" + file.length());
		response.setHeader("Content-Length", " " + file.length());
		//response.setContentLength(Integer.parseInt(file.length()+""));
//		response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(file.getName().getBytes(), "ISO-8859-1") + "\"");
//		response.setHeader("Content-Disposition", "attachment; filename=\"" + URLDecoder.decode(file.getName(),"GB2312") + "\"");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(file.getName(),"UTF-8") + "\"");
		is = new FileInputStream(file);
//		System.out.println("start=" + start);
		is.skip(start);
		byte[] buffer = new byte[6 * 1024];
		int len = 0;
		while ((len = is.read(buffer, 0, buffer.length)) != -1) {
			os.write(buffer, 0, len);
		}
		System.out.println("下载完成!");
		
	} catch (Exception e) {
//		e.printStackTrace();
	}finally{
//			os.flush();
//			os.close();
//			is.close();
			IOUtils.closeQuietly(os);
			IOUtils.closeQuietly(is);
			try {
				DownloadObserver.getInstance().
				removeDownloadingIp(ip);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	}
}
private static void Error404(PrintWriter pw) {
String msg = "<html><title>未找到……</title><body>";
msg += "<div style='color:red;text-align:center'>抱歉!未找到您要下载的资源……<br><input type='button' value='返回' onclick='javascrip:history.go(-1)'></div>";
msg += "</body></html>";
pw.write(msg);
pw.flush();
pw.close();
}
}