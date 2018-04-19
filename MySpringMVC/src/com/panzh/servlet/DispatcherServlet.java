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
	//�� ���а�ɨ�裬���ǳ�ʼ����ʱ���Ƚ�������Ŀ�еİ�����ɨ�裬ɨ������ļ��ֱ��������
	List<String> packageNames = new ArrayList<String>();
	//�������ʵ����key��ע���value��value���������ʵ��
	Map<String, Object> instanceMap = new HashMap<String, Object>();
	Map<String, Object> handerMap = new HashMap<String, Object>();
	
	public DispatcherServlet(){
		super();
	}
	
	public void init(ServletConfig config)	throws ServletException{
		//��ɨ�裬��ȡ���е��ļ�
		scanPackage("com.panzh");
	}

	private void scanPackage(String packageString) {
		//�����е�.ת���ȡ��Ӧ��·��
		URL url = this.getClass().getClassLoader().getResource("/" + replaceTo(packageString));
		String pathFile = url.getFile();
		File file = new File(pathFile);
		String fileList[] = file.list();
	}
	
	private String replaceTo(String path){
		return path.replaceAll("\\.", "/");
	}
	
}
