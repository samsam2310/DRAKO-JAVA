package DRAKO.Data;

import java.io.*;
import java.util.*;
import java.util.Properties;

public class Text
{
	Properties textdata;
	public Text(){
		textdata=new Properties();
		try{
			textdata.loadFromXML(new FileInputStream("DRAKO/language/textdata.xml"));
		}catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
	}
	public String get_text(String key){
		return textdata.getProperty(key);
	}
}

/*
props.load(new FileInputStream("properties.properties"));
props.loadFromXML(new FileInputStream("properties.xml"));

props.storeToXML(new FileOutputStream("properties.xml"), "storeToXML");
props.store(new FileOutputStream("properties.properties"), "store");
*/