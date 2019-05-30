package com.sean.flysky.kafka;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class BeanUtils {
    public static byte[] bean2Byte(Object obj){
    	byte[] btArr=null;
    	try{
    		ByteArrayOutputStream byteArray=new ByteArrayOutputStream();
    		ObjectOutputStream outputStream=new ObjectOutputStream(byteArray);
    		outputStream.writeObject(obj);
    		outputStream.flush();
    		btArr=byteArray.toByteArray();
    		
    		outputStream.close();
    		byteArray.close();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return btArr;
    }
    
    
    public static Object byte2Object(byte[] bytes){
    	Object readObject=null;
    	try{
    		ByteArrayInputStream in=new ByteArrayInputStream(bytes);
    		ObjectInputStream inputStream=new ObjectInputStream(in);
    		readObject=inputStream.readObject();
    		
    		inputStream.close();
    		in.close(); 		
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return readObject;
    }
}
