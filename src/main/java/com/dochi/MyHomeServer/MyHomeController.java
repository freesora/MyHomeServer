package com.dochi.MyHomeServer;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MyHomeController {
	@RequestMapping(value = "/demo", method= RequestMethod.GET)
	public String demo(Model model)
	{
		return "demo";
	}
}
