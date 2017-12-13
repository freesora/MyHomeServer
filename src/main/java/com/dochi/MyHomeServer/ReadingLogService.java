package com.dochi.MyHomeServer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;


@Service
public class ReadingLogService {
	
	Logger logger = Logger.getLogger(MyHomeController.class);

	
	public ArrayList<String> readLogFile(String filePath)
	{
		int count = 0;
		int runningMin = 0;
		ArrayList<String> returnLogs = new ArrayList<String>();
		try {
			List<String> logLines = FileUtils.readLines(new File(filePath),"UTF-8");
			for(String line : logLines)
			{
				if(line.contains("ON Heater.."))
				{
					String[] splitLines = line.split("\\t+");
					if(splitLines.length > 0)
					{
						String time = splitLines[0];
						String runningMinStr = line.replaceAll("\\D+","");
						int tempRunningMin = Integer.parseInt(runningMinStr);
						returnLogs.add("Running Time : " + time + " / Running Minute : " + tempRunningMin);
						runningMin+=tempRunningMin;
						count++;
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException ne)
		{
			logger.info("Failed to String to Integer... cannot parse Running Minute in log files");
			ne.printStackTrace();
		}
		if(returnLogs.size() > 0)
			returnLogs.add(0, "Total Running Count : " + count + " / Running Time : " + runningMin);
		return returnLogs;
	}
}
