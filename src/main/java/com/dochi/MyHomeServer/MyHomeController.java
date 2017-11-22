package com.dochi.MyHomeServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
		String configPath = entityManager.getConfigPath();
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
