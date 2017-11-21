package com.dochi.MyHomeServer.scheduler;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Future;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.dochi.MyHomeServer.EntityManager;
import com.dochi.MyHomeServer.PathStorageService;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;

@Service
public class TempController {
	final static Logger logger = Logger.getLogger(TempController.class);
	@Autowired
	private BoilerRunner boilerRunner;

	@Autowired
	PathStorageService pathService;;

	private Timer timer;

	public boolean runCheck() {
		if (timer == null)
			return false;
		return true;
	}

	public boolean init() {

		try {
			if (timer != null) {
				timer.cancel();
				timer.purge();
				timer = null;
			}
			boilerRunner.init();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean startTimer() {
		try {
			init();
			PropertyReader prop = new PropertyReader(pathService.getConfigPath());
			int duration = Integer.parseInt(prop.getWatchingMin());
			timer = new Timer();
			timer.scheduleAtFixedRate(new TempWatcher(), 0, duration * 1000 * 60);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void stopTimer() {
		init();
	}

	public TempController() {
	}

	class TempWatcher extends TimerTask {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			PropertyReader prop = new PropertyReader(pathService.getConfigPath());
			if (BoilerRunner.isRunning != true) {
				try {
					// Number 1 : wanna focus
					// logger.info("Watching Temperature");

					HttpResponse<InputStream> response = Unirest.get(prop.getResponseURL())
							.queryString("hkey", prop.getHkey()).queryString("hh_dong", prop.getHh_dong())
							.queryString("hh_ho", prop.getHh_ho()).queryString("no", "1").asBinary();

					// only number 1 room information is read
					// if(response)
					Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(response.getBody());
					XPath xpath = XPathFactory.newInstance().newXPath();
					String expression = "//boiler//boilerinfo";
					NodeList nodeList = (NodeList) xpath.compile(expression).evaluate(doc, XPathConstants.NODESET);
					String curTemp = null;
					String setTemp = null;
					if (nodeList != null) {
						for (int i = 0; nodeList != null && i < nodeList.getLength(); i++) {
							NodeList childNodes = nodeList.item(i).getChildNodes();
							for (int j = 0; j < childNodes.getLength(); j++) {
								Node tmpNode = childNodes.item(j);
								if (tmpNode.getNodeName().equals("curtemp")) {
									curTemp = tmpNode.getFirstChild().getNodeValue();
									logger.info("Current Temp : " + curTemp);
								} else if (tmpNode.getNodeName().equals("settemp")) {
									setTemp = tmpNode.getFirstChild().getNodeValue();
									logger.info("Setting Temp : " + setTemp);
								}
							}

						}
					}
					double convertedCurTemp = Double.parseDouble(curTemp);
					double convertedWannaTemp = Double.parseDouble(prop.getWannaTemp());
					if (convertedCurTemp < convertedWannaTemp && BoilerRunner.isRunning == false) {
						logger.info("Running Boiler / Seting Temporature : " + prop.getHighTemp());
						boilerRunner.runBoiler(prop.getHighTemp());
					}
				} catch (Exception e) {
					logger.error("Error!");
					logger.error(e.getMessage());
				}
			}

		}
	}
}
