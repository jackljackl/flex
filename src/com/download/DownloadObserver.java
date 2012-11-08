package com.download;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.ConstantsConfig;

import nl.justobjects.pushlet.core.Dispatcher;
import nl.justobjects.pushlet.core.Event;
import nl.justobjects.pushlet.core.SessionManager;

public class DownloadObserver {
	
	public final static int max = Integer.parseInt(ConstantsConfig.maxLink);
	
	private static DownloadObserver instance= new DownloadObserver();
	private DownloadObserver(){
		queue = new LinkedList<DownloadBean>();
		downloadingIP = new HashMap<String, Integer>();
		queueIp = new HashMap<String, Object>();
		prepareDownload = new HashMap<String, Object>();
	}
	public static DownloadObserver getInstance(){
		return instance;
	}
	
	private LinkedList<DownloadBean> queue;//排队序列
	
	private Map<String,Integer> downloadingIP;//记录正在执行下载的IP
	
	private Map<String,Object> queueIp;//记录正在排队的IP
	
	private Map<String,Object> prepareDownload;//记录允许下载但还没开始下载的IP
	
	public synchronized boolean update(String ip) {
		
		DownloadBean bean = pollQueue();
		if(bean==null){
			DownloadCounter.getInstance().numberMinus();
			return true;
		}
		queueIp.remove(bean.getIp());
		String username = bean.getUserName();
		Event e = Event.createDataEvent("count");
		e.setField("msg", username);
		Dispatcher.getInstance().broadcast(e);
		System.out.println("send to "+username);
		
		addPrepare(bean.getIp());
		
		return false;
	}
	
	/**
	 * 加入到允许下载列表中
	 */
	private void addPrepare(String ip){
		prepareDownload.put(ip, true);
		PrepareDownloadThread thread = new PrepareDownloadThread(ip , prepareDownload	);
		Thread t = new Thread(thread);
		t.start();
	}
	
	/**
	 * 开始下载则从允许下载列表中移除
	 */
	public void removePrepare(String ip){
		prepareDownload.remove(ip);
	}
	
	/**
	 * 是否在允许下载列表中
	 */
	public boolean isPrepareed(String ip){
		return prepareDownload.get(ip) == null ? false : true;
	}
	
	/**
	 * 判断是否续传
	 * @param request
	 * @return
	 */
	public static boolean isResume(HttpServletRequest request){
		String flag = request.getParameter("userid");
		if(flag != null){
			return false;
		}
		return true;
	}
	
	/**
	 * 判断是否达到最大连接数
	 * @return
	 */
	public static boolean isLimited(){
		DownloadCounter d = DownloadCounter.getInstance();
		return d.getNumber()==max?true : false;
	}
	
	/**
	 * 得到转发地址
	 * @param requestURI
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String getDispatcherPath(String requestURI) throws UnsupportedEncodingException{
		String s = requestURI.substring(requestURI.indexOf("fordown/")+8);
		return "/down/"+requestURI.substring(requestURI.indexOf("fordown/")+8);
	}
	
	/**
	 * 记录正在排队的IP
	 * @param ip
	 */
	public void addQueueIp(String ip){
		queueIp.put(ip, true);
	}
	
	/**
	 * 检查 @ip 是否在排队列中
	 * @param ip
	 * @return
	 */
	public boolean hasQueueIp(String ip){
		if(queueIp.get(ip) != null)
			return true;
		return false;
	}
	
	/**
	 * 移除排队中的 @ip 
	 * @param ip
	 */
	public void removeQueuIp(String ip){
		queueIp.remove(ip);
	}
	
	/**
	 * 记录正在下载的IP
	 * @param ip
	 */
	public  void addDownloadingIp(String ip , int thread) {
		downloadingIP.put(ip, thread);
	}
	
	public void addDownloadingIp(String ip){
		Integer thread = downloadingIP.get(ip);
		downloadingIP.put(ip, ++thread);
	}
	
	/**
	 * 该IP是否已有下载任务
	 * @return
	 */
	public boolean hasDownloadingIp(String ip){
		Integer thread = downloadingIP.get(ip);
//		if(thread!=null && thread > 0)
		if(thread!=null )
			return true;
		return false;
	}
	
	/**
	 * 移除正在下载的IP
	 * @param ip
	 */
	public int removeDownloadingIp(String ip){
		Integer thread = downloadingIP.get(ip);
		thread -= 1;
		downloadingIP.put(ip, thread);
		if(thread <= 0){
			try {
				//当某IP的连接全部关闭时，延迟3秒在进行判断，如果仍没新连接则视为下载完成或放弃下载
				Thread.sleep(1000 * 3);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(downloadingIP.get(ip) <= 0 ){
				downloadingIP.remove(ip);
				update(ip);
				return 0;
			}else{
				return downloadingIP.get(ip);
			}
		}
		return thread;
//		if(thread==0)
//			downloadingIP.remove(ip);
		
	}
	
	/**
	 * 把用户加入队列
	 */
	public void addQueue(String ip , String username){
		//先检查是否在队列
		Object o = queueIp.get(ip);
		if(o == null){
			//加入
			DownloadBean bean = new DownloadBean(ip , username);
			queue.offer(bean);
			queueIp.put(ip, true);
		}
	}
	
	public void addQueue(String ip){
		addQueue(ip,null);
	}
	
	/**
	 * 收到下载通知出队列
	 * @return
	 */
	public DownloadBean pollQueue(){
		return queue.poll();
	}
	
	/**
	 * 得到队列中的人数
	 * @return
	 */
	public int getQueueSize(){
		return queue.size();
	}
	
	/**
	 * 得到客户端IP地址
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {      
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
	
}
