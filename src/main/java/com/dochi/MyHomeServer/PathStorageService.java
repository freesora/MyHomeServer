package com.dochi.MyHomeServer;

import org.springframework.stereotype.Service;

@Service
public class PathStorageService {

	
	String configPath;
	public void setConfigPath(String configPath)
	{
		this.configPath = configPath;
	}
	public String getConfigPath() {
		return configPath;
	}
	
	
}
