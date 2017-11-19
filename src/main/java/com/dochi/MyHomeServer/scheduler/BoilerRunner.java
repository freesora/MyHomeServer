package com.dochi.MyHomeServer.scheduler;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dochi.MyHomeServer.EntityManager;
import com.dochi.MyHomeServer.PathStorageService;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.HttpRequestWithBody;

@Service
public class BoilerRunner {
	public static boolean isRunning;
	final static Logger logger = Logger.getLogger(BoilerRunner.class);

	public int mPeriodTime;
	private Timer timer; 
	// private String m_hh_dong;
	// private String m_hkey;
	// private String m_hh_ho;
	// private String m_requestURL;
	
	@Autowired 
	PathStorageService pathService;;

	public BoilerRunner() {
	}
	
	public void init()
	{
		isRunning = false;
		if(timer != null)
		{
			timer.cancel();
			timer = null;
		}
		timer = new Timer();
	}

	public void setTempWithoutMin(String temp) {

		PropertyReader prop = new PropertyReader(pathService.getConfigPath());
		logger.info("Called settempWithoutMin Method -> Hoping Temp : " + temp);
		try {
			HttpRequestWithBody httpRequest = Unirest.post(prop.getRequestURL());
			for (int i = 1; i < 7; i++) {
				if (i == 5)
					continue;
				HttpResponse<InputStream> tempRequest = null;
				try {
					tempRequest = httpRequest.queryString("hkey", prop.getHkey())
							.queryString("hh_dong", prop.getHh_dong()).queryString("hh_ho", prop.getHh_ho())
							.queryString("no", Integer.toString(i)).queryString("onoff", "Y")
							.queryString("settemp", temp).asBinary();
					logger.info("Request Result : " + tempRequest.getStatusText());
				} catch (Exception ie)// = tempRequest.queryString("no",
										// "1").asBinary();
				{
					int retryCount = 5;
					for (int j = 0; j < retryCount; j++) {
						logger.error("Retry try : " + (j + 1));
						tempRequest = httpRequest.queryString("hkey", prop.getHkey())
								.queryString("hh_dong", prop.getHh_dong()).queryString("hh_ho", prop.getHh_ho())
								.queryString("no", Integer.toString(i)).queryString("onoff", "Y")
								.queryString("settemp", temp).asBinary();
						if (tempRequest.getStatusText().equals("OK")) {
							logger.info("Retry has been successed ");
							break;
						} else
							logger.error(tempRequest.getStatusText());
					}
				}
			}
		} catch (Exception e) {
			logger.error("Requesting URL has Error");
			logger.error(e.getMessage());
		}
	}

	public void runBoiler(String temp) {
		// String maxTemp = "30.0";
		
		//logger.info("Called runBoiler Method -> Hoping Temp : " + temp);

		//logger.info("Called settempWithoutMin Method -> Hoping Temp : " + temp);
		try {
			PropertyReader prop = new PropertyReader(pathService.getConfigPath());
			HttpRequestWithBody httpRequest = Unirest.post(prop.getRequestURL());
			for (int i = 1; i < 7; i++) {
				if (i == 5)
					continue;
				HttpResponse<InputStream> tempRequest = null;
				try {
					tempRequest = httpRequest.queryString("hkey", prop.getHkey())
							.queryString("hh_dong", prop.getHh_dong()).queryString("hh_ho", prop.getHh_ho())
							.queryString("no", Integer.toString(i)).queryString("onoff", "Y")
							.queryString("settemp", temp).asBinary();
					logger.info("Request Result : " + tempRequest.getStatusText());
				} catch (Exception ie)// = tempRequest.queryString("no",
										// "1").asBinary();
				{
					int retryCount = 5;
					for (int j = 0; j < retryCount; j++) {
						logger.error("Retry try : " + (j + 1));
						tempRequest = httpRequest.queryString("hkey", prop.getHkey())
								.queryString("hh_dong", prop.getHh_dong()).queryString("hh_ho", prop.getHh_ho())
								.queryString("no", Integer.toString(i)).queryString("onoff", "Y")
								.queryString("settemp", temp).asBinary();
						if (tempRequest.getStatusText().equals("OK")) {
							logger.info("Retry has been successed ");
							break;
						} else
							logger.error(tempRequest.getStatusText());
					}
				}
				// 0 -> maybe bathroom?
				// 1 -> 거실
				// 2 -> 안방
				// 3 -> 큰방
				// 4 -> 작은방
			}
			// Run the task
			isRunning = true;
			int runningMinTmp = Integer.parseInt(prop.getRunningMin());
			
			timer.cancel();
			timer = new Timer();
			timer.schedule(new BoilerStopTask(), Math.round(runningMinTmp * 1000 * 60));
			timer.schedule(new BoilerFlagChangeTask(), Math.round(runningMinTmp * 1000 * 60) * 2);
		}

		catch (Exception e) {
			logger.error("Requesting URL has Error");
			logger.error(e.getMessage());
		}
	}
	
	void cancle()
	{
		if(timer != null)
		{
			PropertyReader prop = new PropertyReader(pathService.getConfigPath());
			setTempWithoutMin(prop.getMinTemp());
			timer.cancel();
		}
	}
	
	class BoilerFlagChangeTask extends TimerTask{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			isRunning = false;
			logger.info("Running flag has been changed");
			this.cancel();
		}
		
	}

	class BoilerStopTask extends TimerTask {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			PropertyReader prop = new PropertyReader(pathService.getConfigPath());

			logger.info("in " + Integer.parseInt(prop.getRunningMin()) + "minutes");
			logger.info("Temporature will be changed");
			setTempWithoutMin(prop.getMinTemp());
			this.cancel();
		}
		
	}
}
