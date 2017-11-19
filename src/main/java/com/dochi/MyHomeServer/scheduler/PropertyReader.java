package com.dochi.MyHomeServer.scheduler;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.dochi.MyHomeServer.EntityManager;
import com.dochi.MyHomeServer.MyHomeController;


public class PropertyReader {

	Logger logger = Logger.getLogger(PropertyReader.class);
	
	

	private String requestURL;
	private String responseURL;
	private String hkey;
	private String highTemp;
	public String getRequestURL() {
		return requestURL;
	}

	public void setRequestURL(String requestURL) {
		this.requestURL = requestURL;
	}

	public String getResponseURL() {
		return responseURL;
	}

	public void setResponseURL(String responseURL) {
		this.responseURL = responseURL;
	}

	public String getHkey() {
		return hkey;
	}

	public void setHkey(String hkey) {
		this.hkey = hkey;
	}

	public String getHh_dong() {
		return hh_dong;
	}

	public void setHh_dong(String hh_dong) {
		this.hh_dong = hh_dong;
	}

	public String getHh_ho() {
		return hh_ho;
	}

	public void setHh_ho(String hh_ho) {
		this.hh_ho = hh_ho;
	}

	public String getWannaTemp() {
		return wannaTemp;
	}

	public void setWannaTemp(String wannaTemp) {
		this.wannaTemp = wannaTemp;
	}

	public String getRunningMin() {
		return runningMin;
	}

	public void setRunningMin(String runningMin) {
		this.runningMin = runningMin;
	}

	public String getMinTemp() {
		return minTemp;
	}

	public void setMinTemp(String minTemp) {
		this.minTemp = minTemp;
	}

	private String hh_dong;
	private String hh_ho;
	private String wannaTemp;
	private String runningMin;
	private String minTemp;
	private String watchingMin;
	
//	responseURL=http://m.ezville.net/ezvillehn/mobile/service/temSetSearchCall.php
//		requestURL=http://m.ezville.net/ezvillehn/mobile/service/temSetControlCall.php
//		hkey=0008911
//		hh_dong=101
//		hh_ho=705
//		wannaTemp=22.0
//		runningMin=30
//		minTemp=11.0
	
	public String getWatchingMin() {
		return watchingMin;
	}

	public void setWatchingMin(String watchingMin) {
		this.watchingMin = watchingMin;
	}

	public PropertyReader(String configPath)
	{
		InputStream input;
		try {
			
			//String configPath = env.getProperty("config_path");
			input = new FileInputStream(configPath);
			logger.info("Input Path : " + input);
			Properties prop = new Properties();
			prop.load(input);
			requestURL = prop.getProperty("requestURL");
			responseURL = prop.getProperty("responseURL");
			hh_dong = prop.getProperty("hh_dong");
			hkey = prop.getProperty("hkey");
			hh_ho = prop.getProperty("hh_ho");
			wannaTemp = prop.getProperty("wannaTemp");
			runningMin = prop.getProperty("runningMin");
			minTemp = prop.getProperty("minTemp");
			highTemp = prop.getProperty("highTemp");
			watchingMin = prop.getProperty("watchingMin");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Reading Property Error");
			logger.error(e.getMessage());
			//e.printStackTrace();
		}
	}
	
	public String getHighTemp() {
		return highTemp;
	}

	public void setHighTemp(String highTemp) {
		this.highTemp = highTemp;
	}
}
