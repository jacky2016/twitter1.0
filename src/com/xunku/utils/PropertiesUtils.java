package com.xunku.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

public class PropertiesUtils {
    private static HashMap<String, Properties> propsMap = new HashMap<String, Properties>();

    /**
     * 根据本地资源文本文件名称与键的名字获取值
     * 
     * @param fileName
     *                本地资源文本文件名称
     * @param name
     *                键的名字
     * @return 值
     * @throws IOException
     */
    public static String getString(String fileName, String name)
	    throws IOException {
	if (!propsMap.containsKey(fileName)) {
	    synchronized (PropertiesUtils.class) {
		if (!propsMap.containsKey(fileName)) {
		    InputStream is = PropertiesUtils.class.getClassLoader()
			    .getResourceAsStream(fileName + ".properties");
		    Properties props = new Properties();
		    props.load(is);
		    propsMap.put(fileName, props);		    
		}
	    }
	}

	return propsMap.get(fileName).getProperty(name);
    }

}
