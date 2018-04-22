package com.panzh.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.panzh.annotation.Controller;
import com.panzh.annotation.Quatifier;
import com.panzh.annotation.RequestMapping;
import com.panzh.service.impl.MyService;
import com.panzh.service.impl.MyService2;

@Controller("mySpring")
public class MySpringController {
	@Quatifier("MyServiceImpl")
	MyService myService;
	
	@Quatifier("MyServiceImpl2")
	MyService2 myService2;
	
	@RequestMapping("sayHello1")
	public void sayHello1(HttpServletRequest request, HttpServletResponse response, String param){
		myService.insert();
		myService.delete();
		myService.select();
		myService.update();
		try {
            response.getWriter().write( "sayHello1 method success!");
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	@RequestMapping("sayHello2")
	public void sayHello2(HttpServletRequest request, HttpServletResponse response, String param){
		try {
            response.getWriter().write( "sayHello1 method success!");
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
