package com.shawemou.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DemoController {

	@RequestMapping("/demo")
	@ResponseBody
	public String toGet(){
		
		return "hello";
	}
	
	@Autowired
	private ReadingSetting readingSeting;
	
	@RequestMapping("set/demo")
	public void demo(){
		System.out.println(readingSeting);
		ServiceDemo demo = new ServiceDemo();
		System.out.println(readingSeting.bankList);
	}
	
	
	
}
