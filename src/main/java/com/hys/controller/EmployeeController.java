package com.hys.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.hys.dao.DepartmentDao;
import com.hys.dao.EmployeeDao;
import com.hys.pojo.Department;
import com.hys.pojo.Employee;

@Controller
public class EmployeeController {
	@Autowired
	EmployeeDao empDao;
	@Autowired
	DepartmentDao deptDao;
    	//查询所有员工返回列表页面
    @GetMapping("/emps")
    public String  list(Model model){
        	//相当于从数据库取出数据
    	Collection<Employee> employees = empDao.getAll();
        	//放在请求域中
        model.addAttribute("emps",employees);
	        // thymeleaf默认就会拼串
	        // classpath:/templates/xxxx.html
        return "emp/list";
    }
    	//去到员工添加页面添加员工，GET请求
	@GetMapping("/emp")
    public String ToAddPage(Model model) {
    		//查询所有部门
		Collection<Department> depts = deptDao.getDepartments();
			//封装到Model中，发送到add.html页面中
		model.addAttribute("depts", depts);
    	return "emp/add";
    }
    	//得到POST请求,添加员工到数据库中
	@PostMapping("/emp")
		//SpringMvc中自动将请求参数和对象属性一一对应：前提是参数名和属性名一样
	public String AddEmp(Employee emp) {
			//调用empDao的方法，保存到数据库中
		empDao.save(emp);
			//从定向到Controller,前面加了"forward:xxx"也是转发到某Controller
			//不加redirect或者forward表示转发到某thymeleaf模板引擎页面
			//原因参照ThymeleafViewResolver解析器的方法createView
		return "redirect:/emps";
	}
		//到修改页面去，GET请求，带参数
	@GetMapping("/emp/{id}")
		//rest风格的参数,使用@PathVariable
	public String toUpdatePage(@PathVariable("id")Integer id,Model model) {
			//根据id查找emp信息
		Employee emp = empDao.get(id);
			//将员工回显
		model.addAttribute("emp", emp);
			//转发到emp Controller控制器,在跳转到员工添加页面，
			//此时变为修改页面(根据emp是否有值判别是添加还是修改)
		return "forward:/emp";
	}
		//此时接受的是PUT请求
	@PutMapping("/emp")
	public String update(Employee emp) {
			//根据id删除已有的内容
		empDao.delete(emp.getId());
			//保存修改后的内容
		empDao.save(emp);
			//从定向到查看员工页面
		return "redirect:/emps";
	}
		//删除数据，接受的是DELETE请求
	@DeleteMapping("/emp/{id}")
	//rest风格的参数,使用@PathVariable
	public String delete(@PathVariable("id")Integer id) {
		empDao.delete(id);
			//从定向到查看员工页面
		return "redirect:/emps";
	}
	
	
}
