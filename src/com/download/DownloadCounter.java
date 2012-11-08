package com.download;

import java.io.IOException;

import javax.servlet.ServletException;

public final class DownloadCounter {

	private static DownloadCounter instance = new DownloadCounter();
	
	private DownloadCounter(){
		observer = DownloadObserver.getInstance();
	};
	
	private DownloadObserver observer;
	
	public int number;
	
	public static DownloadCounter getInstance(){
		return instance;
	}
	
	/**
	 * 下载人数加1 
	 * @param ip
	 */
	public synchronized void numberAdd(String ip){
		numberAdd(ip , null);
	}
	
	public synchronized void numberAdd(String ip , String userName){
		boolean isDownloading = observer.hasDownloadingIp(ip);//该IP是否有线程在执行下载
		if(! isDownloading){
			if(number < DownloadObserver.max)
				number++;
			observer.addDownloadingIp(ip , 1);
			observer.removePrepare(ip);
		}else{
//			number++;//此句测试时使用,必删除
			observer.addDownloadingIp(ip);
		}
	}
	
	public synchronized void numberMinus() {
		number--;
	}
	
	public synchronized int getNumber(){
		return number;
	}
	
}
