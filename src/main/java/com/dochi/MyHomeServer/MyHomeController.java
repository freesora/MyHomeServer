package com.dochi.MyHomeServer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.dochi.MyHomeServer.scheduler.BoilerRunner;
import com.dochi.MyHomeServer.scheduler.PropertyReader;
import com.dochi.MyHomeServer.scheduler.TempController;

@Controller
public class MyHomeController {

	@Autowired 
	EntityManager entityManager;
	
	@Autowired
	TempController tempController;
	
	@Autowired 
	PathStorageService pathService;;
	
	
	Logger logger = Logger.getLogger(MyHomeController.class);
	
	@RequestMapping(value ="/start")
	@ResponseBody
	public String start()
	{
		String configPath = entityManager.getConfigPath();
		pathService.setConfigPath(configPath);
		try {
			String contents = FileUtils.readFileToString(new File(configPath), "UTF8");
			logger.info("Config Path : " + configPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PropertyReader prop = new PropertyReader(configPath);
		tempController.init();
		tempController.startTimer();

		return "OKAY";
	}
	
	@RequestMapping(value ="/stop")
	@ResponseBody
	public String stop()
	{
		tempController.stopTimer();
		return "OKAY";
	}

	@RequestMapping(value = "/demo", method = RequestMethod.GET)
	public String demo(Model model) {

		
	
		
		//FileUtils.readFileToString(new File(setting.config));
		//logger.info(");
		
		return "demo";
	}
}
