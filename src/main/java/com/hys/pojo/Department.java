package com.hys.pojo;

public class Department {
	private Integer id;
	private String depName;
	
	
	
	@Override
	public String toString() {
		return "Department [id=" + id + ", depName=" + depName + "]";
	}
	public Department(Integer id, String depName) {
		super();
		this.id = id;
		this.depName = depName;
	}
	public Department() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDepName() {
		return depName;
	}
	public void setDepName(String depName) {
		this.depName = depName;
	}
	
	
	
}
