package com.dochi.MyHomeServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@PropertySource("classpath:application.properties")
@Service
public class EntityManager {

    @Autowired
    private Environment env;

    public String getConfigPath() {
        return env.getProperty("config_path");
    }
    
    

}
