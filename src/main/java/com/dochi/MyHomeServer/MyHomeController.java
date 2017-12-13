package com.dochi.MyHomeServer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dochi.MyHomeServer.Domain.ConfigProperty;
import com.dochi.MyHomeServer.Domain.TempInfo;
import com.dochi.MyHomeServer.scheduler.PropertyReader;
import com.dochi.MyHomeServer.scheduler.TempController;

@Controller
public class MyHomeController {

	@Autowired
	EntityManager entityManager;

	@Autowired
	TempController tempController;

	@Autowired
	TempService tempService;
	
	@Autowired
	ReadingLogService readingLogService;

	@Value("${config_path}")
	private String configPath;

	Logger logger = Logger.getLogger(MyHomeController.class);

	@RequestMapping(value = "/start")
	@ResponseBody
	public String start() {

		try {
			// String contents = FileUtils.readFileToString(new File(configPath), "UTF8");
			logger.info("Config Path : " + configPath);

			// PropertyReader prop = new PropertyReader(configPath);
			// tempController.init();
			tempController.startTimer();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "NOTOK";
		}

		return "OKAY";
	}

	@RequestMapping(value = "/log", method = RequestMethod.GET)
	public String log(Model model) {

		// FileUtils.readFileToString(new File(setting.config));
		// logger.info(");

		return "log";
	}

	@RequestMapping(value = "/getLogFiles", method = RequestMethod.GET)
	@ResponseBody
	public ArrayList<String> applyConfig() {
		logger.info("Called getLogFiles...");

		File logFile = new File("logs");
		ArrayList<String> logFileList = new ArrayList<>();
		ArrayList<String> currFileList = new ArrayList<>();
		if (logFile.isDirectory()) {
			Collection<File> fileList = FileUtils.listFiles(logFile, new String[] { "log" }, true);
			for (File tmpFile : fileList) {
				logFileList.add(FilenameUtils.getName(tmpFile.getAbsolutePath()));
			}
			Collections.sort(logFileList);

			for (int i = logFileList.size() - 1, count = 0; count < 20 && i >=0 ; i--, count++) {
				currFileList.add(logFileList.get(i));
			}
			

			// switch today file from last to first line
			int lastIdx = currFileList.size()-1;
			String todayLog = currFileList.remove(lastIdx);
			currFileList.add(0,todayLog);

		}
		else
		{
			logger.info("logs direction not exist");
		}
		
		
		return currFileList;

	}
	
	@RequestMapping(value ="/showLogFile", method = RequestMethod.GET)
	@ResponseBody
	public ArrayList<String> showLogFile(@RequestParam("fileName") String fileName)
	{
		fileName = FilenameUtils.concat("logs", fileName);
		logger.info("Request Show Log Files / FileName : " + fileName);
		ArrayList<String> logs = readingLogService.readLogFile(fileName);
		return logs;
	}

	@RequestMapping(value = "/applyConfig", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> applyConfig(@RequestBody ConfigProperty configProperty, HttpServletRequest request) {
		// logger.info("Config Property" + configProperty.getHighTemp());
		logger.info("Called applyConfig");
		PropertyReader propertyReader = new PropertyReader(configPath);
		boolean result = propertyReader.saveConfigProperty(configPath, configProperty);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(result == true)
		{
			tempController.startTimer();
		}
		resultMap.put("result", result);

		return resultMap;
	}

	@RequestMapping(value = "/requestChangeSetting", method = RequestMethod.POST)
	@ResponseBody
	public ArrayList<TempInfo> requestChangeSetting() {
		return tempService.getTemperature();
	}

	@RequestMapping(value = "/getCurrTempInfo", method = RequestMethod.GET)
	@ResponseBody
	public ArrayList<TempInfo> getCurrTempInfo() {
		return tempService.getTemperature();
	}

	@RequestMapping(value = "/test", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> test() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("message", "TEST MESSAGE");
		return resultMap;
	}

	@RequestMapping(value = "/getInfo", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getInfo() {
		PropertyReader prop = new PropertyReader(configPath);

		Map<String, Object> resultMap = new HashMap<String, Object>();

		boolean working = tempController.runCheck();

		resultMap.put("working", working);
		resultMap.put("minTemp", prop.getMinTemp());
		resultMap.put("runningMin", prop.getRunningMin());
		resultMap.put("watchingMin", prop.getWatchingMin());
		resultMap.put("targetTemp", prop.getWannaTemp());
		resultMap.put("setTemp", prop.getHighTemp());

		return resultMap;
	}

	@RequestMapping(value = "/switchHeater", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> switchHeater(@RequestParam("val") String onOffVal, HttpServletResponse response) {
		logger.info("switchHeater url entered");
		Boolean result = false;
		logger.info("GET AJAX Message : " + onOffVal);
		Map<String, Object> resultMap = new HashMap<String, Object>();

		if (onOffVal.equalsIgnoreCase("Off")) {
			logger.info("Switching On..");
			result = tempController.startTimer();
			logger.info("Result : " + result);
		} else {
			logger.info("Switching Off..");
			result = tempController.init();
			logger.info("Result : " + result);
		}
		resultMap.put("result", result);
		return resultMap;
	}

	@RequestMapping(value = "/stop")
	@ResponseBody
	public String stop() {
		try {
			tempController.init();
		} catch (Exception e) {
			e.printStackTrace();
			return "NOTOK";
		}
		return "OKAY";
	}

	@RequestMapping(value = "/demo", method = RequestMethod.GET)
	public String demo(Model model) {

		// FileUtils.readFileToString(new File(setting.config));
		// logger.info(");

		return "demo";
	}
}
