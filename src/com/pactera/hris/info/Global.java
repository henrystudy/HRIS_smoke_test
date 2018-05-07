package com.pactera.hris.info;

import java.util.Map;

import com.pactera.hris.util.XmlReader;

/**
 * 解析存放全局信息的global.xml
 * @author zhenhaiw
 *
 */
public class Global {
	
	public static Map<String, String> global;
	
	static{
		XmlReader xr = new XmlReader("test-data/Global.xml");
		global = xr.getChildNodesByElement(xr.getElementObject("/*"));
	}
	
}
