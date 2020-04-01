package com.hys.javaBean;

import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
@Component
@ConfigurationProperties(prefix="person")
public class Person {
	
	private String lastName;
	private Map<String,String> maps;
	private List<Object> lists;
	
	
	public Person() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Person(String lastName, Map<String, String> maps, List<Object> lists) {
		super();
		this.lastName = lastName;
		this.maps = maps;
		this.lists = lists;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Map<String, String> getMaps() {
		return maps;
	}
	public void setMaps(Map<String, String> maps) {
		this.maps = maps;
	}
	public List<Object> getLists() {
		return lists;
	}
	public void setLists(List<Object> lists) {
		this.lists = lists;
	}
	@Override
	public String toString() {
		return "Person [lastName=" + lastName + ", maps=" + maps + ", lists=" + lists + "]";
	}
	
}
