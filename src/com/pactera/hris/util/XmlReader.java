package com.pactera.hris.util;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * .jar needed for parse XML
 * dom4j - parse xml
 * jaxen - recognize xpath expression
 * @author zhenhaiw
 *
 */

public class XmlReader 
{
	/**
	 * 解析xml文件，我们需要知道xml文件的路径，然后根据其路径加载xml文件后，生成一个Document的对象，
	 * 于是我们先定义两个变量String filePath,Document document
	 * 然后再定义一个load方法，这个方法用来加载xml文件，从而产生document对象。
	 */
	public XmlReader(String filePath)
	{
		this.filePath = filePath;
		this.load(this.filePath);
	}
	
	private String filePath;
	private Document document;
	private Log logger = new Log(this.getClass().getName());
	
	/**
	 * 用来加载xml文件，并且产生一个document的对象
	 */
	public void load(String filePath)
	{
		File file = new File(filePath);
		
		if(file.exists())
		{
			SAXReader reader = new SAXReader();
			try 
			{
				document = reader.read(file);
			} catch (DocumentException e) 
			{
				logger.logError("Loading file error: " + filePath);
				//e.printStackTrace();//调试可以用，比如xml写的格式不对
			}
		}
		else
		{
			logger.logError("File does NOT exist: " + filePath);
		}
	}
	
	/**
	 * 用xpath来得到一个元素节点对象
	 * @param elementPath elementPath是一个xpath路径,比如"/config/driver"
	 * @return 返回的是一个节点Element对象
	 */
	public Element getElementObject(String elementPath)
	{
//		Element rootElm = document.getRootElement();
//		return rootElm.element(elementPath);
		return (Element)document.selectSingleNode(elementPath);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	/**
	 * 用xpath来取得一个同级元素节点列表
	 * (比如多个配置，多个用户名密码，多个不同配置并列测试用例等)
	 */
	public List<Element> getElementObjects(String elementPath)
	{
//		Element rootElm = document.getRootElement();
//		return rootElm.elements(elementPath);
		return (List)document.selectNodes(elementPath);
	}
	
	/**
	 * 用xpath来取得一个结点对象的值
	 */
	public String getElementText(String elementPath)
	{
		Element element = this.getElementObject(elementPath);
		if(element != null)
		{
			return element.getTextTrim();
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * 用xpath来判断一个结点对象是否存在
	 */
	public boolean isExist(String elementPath)
	{
		boolean flag = false;
		if(this.getElementObject(elementPath) != null)//不要用equals(null), null时会返回空指针异常NullPointerException()
		{
			flag = true;
		}
		else
		{
			logger.logError("Element does NOT exist: " + elementPath);
		}
		return flag;
	}
	
	/**
	 * 取得所有子节点
	 */
	public Map<String, String> getChildNodesByElement(Element element)
	{
		Map<String,String> map = new HashMap<String, String>();
		List<Element> child = element.elements();
		for(Element e : child)
		{
			map.put(e.getName(), e.getText());
		}
		return map;
	}
	
	/**
	 * 取得所有子节点对象的值
	 */
	public Object[][] getAllChild(String elementPath)
	{
		List<Element> elements = this.getElementObjects(elementPath);
		Object[][] object = new Object[elements.size()][];
		for(int i = 0; i < elements.size(); i ++)
		{
			object[i] = new Object[] {this.getChildNodesByElement(elements.get(i))};
		}
		return object;
	}
	
	//测试
	@SuppressWarnings("unchecked")
	public static void main(String[] args)
	{
		XmlReader px = new XmlReader("test-data/Login.xml");
		System.out.println(px.isExist("/*/testLogin"));
		System.out.println(px.getElementText("/*/*/username"));
		System.out.println(px.getChildNodesByElement(px.getElementObject("/*/testLogin")));
		
		Object o = px.getAllChild("/*/testLogin")[1][0];
		System.out.println(((Map<String,String>)o).get("username"));
	}
}
