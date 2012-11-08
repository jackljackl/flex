package com;

import java.io.IOException;
import java.util.Properties;

public final class ConstantsConfig {
	
	
	public final static String RESCOURCE_PATH = "config.properties";
	
	private static Properties p ;
	
	static{
		try {
			p = new Properties();
			p.load(ConstantsConfig.class.getClassLoader().getResourceAsStream(RESCOURCE_PATH));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public final static String filePath = p.getProperty("filePath");
	public final static String uploadPort = p.getProperty("uploadPort");
	public final static String maxLink = p.getProperty("maxLink");
	
	public static void main(String a[]){
		System.out.println(ConstantsConfig.maxLink);
	}
}
