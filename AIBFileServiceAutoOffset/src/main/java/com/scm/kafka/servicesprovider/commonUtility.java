package com.scm.kafka.servicesprovider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import joptsimple.internal.Strings;

public class commonUtility {

	public Properties readProp() {
		Properties prop = null;
		try {
			String Path = new File("src//main//resources//Config.properties")
					.getAbsolutePath();
			FileInputStream fis = new FileInputStream(new File(Path));
			prop = new Properties();
			prop.load(fis);
			return prop;
		} catch (Exception e) {
		/*	System.out
					.println("Config.properties file not located on src//main//resources//Config.properties, hence reading from local file");*/
			String Path = new File("Config.properties").getAbsolutePath();
			FileInputStream fis;
			try {
				fis = new FileInputStream(new File(Path));
				prop = new Properties();
				prop.load(fis);
				return prop;
			} catch (Exception e1) {
				e1.printStackTrace();
				return null;
			}

		}

	}

}
