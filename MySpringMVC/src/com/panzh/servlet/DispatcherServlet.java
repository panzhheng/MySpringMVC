package com.panzh.servlet;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.Spring;

import com.panzh.annotation.Controller;
import com.panzh.annotation.Quatifier;
import com.panzh.annotation.RequestMapping;
import com.panzh.annotation.Service;
import com.panzh.controller.MySpringController;

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
		
		try {
			//过滤与实例化
			filterAndInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//建立映射关系
		handerMap();
		
		//实现注入
		ioc();
	}

	/**
	 * 实现注入
	 */
	private void ioc() {
		if (instanceMap.isEmpty()) {
			return;
		}
		
		for(Map.Entry<String, Object> entry : instanceMap.entrySet()){
			//获取Class中的全部属性
			Field fields[] = entry.getValue().getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);//可访问私有属性
				if (field.isAnnotationPresent(Quatifier.class)) {
					Quatifier quatifier = field.getAnnotation(Quatifier.class);
					String value = quatifier.value();
					try {
						field.set(entry.getValue(), instanceMap.get(value));
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * 建立映射关系
	 */
	private void handerMap() {
		if (instanceMap.size() <= 0) {
			return;
		}
		
		for(Map.Entry<String, Object> entry : instanceMap.entrySet()){
			if (entry.getValue().getClass().isAnnotationPresent(Controller.class)) {
				Controller controller = (Controller)entry.getValue().getClass().getAnnotation(Controller.class);
				String ctvalue = controller.value();
				Method[] methods = entry.getValue().getClass().getMethods();
				for (Method method : methods) {
					if (method.isAnnotationPresent(RequestMapping.class)) {
						RequestMapping rm = (RequestMapping)method.getAnnotation(RequestMapping.class);
						String rmvalue = rm.value();
						handerMap.put("/" + ctvalue + "/" + rmvalue, method);
					}
				}
			}else {
				continue;
			}
		}
		
	}

	/**
	 * 过滤和实例化
	 * @throws Exception 
	 */
	private void filterAndInstance() throws Exception {
		if (packageNames.size() <= 0) {
			return;
		}
		
		for (String className : packageNames) {
			Class<?> cName = Class.forName(className.replace(".class", "").trim());
			if (cName.isAnnotationPresent(Controller.class)) {
				Object instance = cName.newInstance();
				Controller controller = (Controller)cName.getAnnotation(Controller.class);
				
				String key = controller.value();
				instanceMap.put(key, instance);
			}else if (cName.isAnnotationPresent(Service.class)) {
				Object instance = cName.newInstance();
				Service service = (Service)cName.getAnnotation(Service.class);
				String key = service.value();
				instanceMap.put(key, instance);
			}else {
				continue;
			}
		}
	}

	/**
	 * 扫描包下所有的文件
	 */
	private void scanPackage(String packageString) {
		//将所有的.转译获取对应的路径
		URL url = this.getClass().getClassLoader().getResource("/" + replaceTo(packageString));
		String pathFile = url.getFile();
		File file = new File(pathFile);
		String fileList[] = file.list();
		for (String path : fileList) {
			File itemFile = new File(pathFile + path);
			if (itemFile.isDirectory()) {
				scanPackage(packageString +"."+ itemFile.getName());
			}else {
				packageNames.add(packageString + "." + itemFile.getName());
			}
		}
	}
	
	private String replaceTo(String path){
		return path.replaceAll("\\.", "/");
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String url = request.getRequestURI();
		String context = request.getContextPath();
		String path = url.replace(context, "");
		Method method = (Method) handerMap.get(path);
		
		MySpringController controller = (MySpringController) instanceMap.get(path.split("/")[1]);
		
		try {
			method.invoke(controller, new Object[]{request,response,null});
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {  
		this.doPost(request, response);  
	}  
}
