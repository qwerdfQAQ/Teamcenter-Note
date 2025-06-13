package com.teamcenter.clientx;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
	public String getProperyValue(String propertyName)
	{
		Properties prop = new Properties();
		InputStream fis = getClass().getClassLoader().getResourceAsStream("pdmserver.properties");
		String value="";
		try {
			prop.load(fis);
			value = prop.getProperty(propertyName);
		} catch (IOException e1) {
	       //TODO Auto-generated catch block
	       e1.printStackTrace();
	       }finally{
	    	   try {
	    		   fis.close();
	    		   } catch (IOException e) {
	    			   //TODO Auto-generated catch block
	    			   e.printStackTrace();
	    	   }
	    }
		return value;
	}
}