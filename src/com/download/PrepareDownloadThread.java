package com.download;

import java.util.HashMap;
import java.util.Map;

public class PrepareDownloadThread implements Runnable{

	private String ip;
	
	private Map<String , Object> prepareDownload;
	
	public PrepareDownloadThread(String ip , Map prepareDownload){
		this.ip = ip;
		this.prepareDownload = prepareDownload;
	}
	
	public static void main(String a[]){
		PrepareDownloadThread p = new PrepareDownloadThread("12",new HashMap());
		Thread t = new Thread(p);
		t.start();
	}
	
	@Override
	public void run() {
		//40 秒后移除如果还在允许下载列表
		//则视为该IP连接断开
		try {
			Thread.sleep(1000 * 40);
			Object o = prepareDownload.get(ip);
			if(o!=null){
				prepareDownload.remove(ip);
				DownloadObserver.getInstance().update(ip);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
