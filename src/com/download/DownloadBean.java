package com.download;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DownloadBean {

	private String ip;
	
	private String userName;
	
	private DownloadObserver observer;
	
	public DownloadBean(String ip , String userName) {
		this.ip = ip;
		this.userName = userName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void addObserver(DownloadObserver observer){
		this.observer = observer;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
