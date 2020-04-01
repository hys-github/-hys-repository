package com.hys.exception;
	//自定义异常，
public class MyDefineException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
		//异常构造器
	public MyDefineException(String message){
		super(message);
	}
	
	
	
}
