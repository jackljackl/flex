package com.download;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BeforeDownloadServlet extends HttpServlet{

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		boolean isResume = DownloadObserver.isResume(request);
//		String ip  =  DownloadObserver.getIpAddr(request);
		String ip = request.getParameter("ip");
		System.out.println(ip);
		if(isResume){
			
			boolean hasDownloading = DownloadObserver.getInstance().hasDownloadingIp(ip);
			boolean isPrepare =  DownloadObserver.getInstance().isPrepareed(ip);
			if(hasDownloading || isPrepare){
				goDownloading(request, response, ip);
				return ;
			}
			//判断队列
			boolean isLimited = DownloadObserver.isLimited();
			if(isLimited){
				lineUp(ip);
				try {
					Thread.sleep(1000 * 10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				response.setStatus(202);
				return;
			}else{
				System.out.println("--------------------------------");
				goDownloading(request, response, ip);
				return;
			}
			
		}else{
			System.out.println(DownloadObserver.getDispatcherPath(request.getRequestURI()));
			goDownloading(request, response, ip);
			return;
		}
	}
	
	private void goDownloading(HttpServletRequest request,HttpServletResponse response , String ip) throws ServletException, IOException{
		DownloadCounter.getInstance().numberAdd(ip);
		request.getRequestDispatcher(DownloadObserver.getDispatcherPath(request.getRequestURI())).forward(request, response);
	}
	
	private void lineUp(String ip){
		//是否已在队列
		boolean hasaLineUp = DownloadObserver.getInstance().hasQueueIp(ip);
		if(!hasaLineUp){
			//加入队列
			DownloadObserver.getInstance().addQueue(ip);
		}
	}

}
