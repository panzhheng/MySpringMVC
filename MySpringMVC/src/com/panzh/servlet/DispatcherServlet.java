package com.panzh.servlet;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

@WebServlet("/DispatcherServlet")
public class DispatcherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;  
	//① 进行包扫描，就是初始化的时候先将整个项目中的包进行扫描，扫描各个文件分别存起来。
	List<String> packageNames = new ArrayList<String>();
	//所有类的实例，key是注解的value，value是所有类的实例
	Map<String, Object> instanceMap = new HashMap<String, Object>();
	Map<String, Object> handerMap = new HashMap<String, Object>();
	
	public DispatcherServlet(){
		super();
	}
	
	public void init(ServletConfig config)	throws ServletException{
		//包扫描，获取包中的文件
		scanPackage("com.panzh");
	}

	private void scanPackage(String packageString) {
		//将所有的.转译获取对应的路径
		URL url = this.getClass().getClassLoader().getResource("/" + replaceTo(packageString));
		String pathFile = url.getFile();
		File file = new File(pathFile);
		String fileList[] = file.list();
	}
	
	private String replaceTo(String path){
		return path.replaceAll("\\.", "/");
	}
	
}
