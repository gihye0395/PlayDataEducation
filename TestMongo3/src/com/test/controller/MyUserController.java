package com.test.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.test.dao.MONGOService;
import com.test.domain.MyUser;

@RestController
@RequestMapping("/")
public class MyUserController {
	@Autowired
	private MONGOService mongoService;
	public MyUserController() {
		// TODO Auto-generated constructor stub
	}
	@RequestMapping(value="index.html")
	public String index() {
		StringBuilder sb = new StringBuilder();
		sb.append("<h2> <a href='/TestMongo3/myall' >1. allList </a> </h2>");
		sb.append("<h2> <a href='/TestMongo3/addMyUser' >2. (MyUser add) </a> </h2>");
		sb.append("<h2> <a href='/TestMongo3/myUpdate' >3. update: updateMyUser </a> </h2>");
		sb.append("<h2> <a href='/TestMongo3/count' >4. count </a> </h2>");
		return sb.toString();
	}//index() end 
	
	@RequestMapping(value="addMyUser")
	public String insertForm(){
		StringBuilder sb = new StringBuilder();
		sb.append("<html><body>");
		sb.append("<form action='myinsert' > <br/>");
		sb.append("<label> id: <input type='text' name='id' /> </label><br/>");
		sb.append("<label> firstname: <input type='text' name='firstname' /> </label><br/>");
		sb.append("<label> lastname: <input type='text' name='lastname' /> </label><br/>");
		sb.append("<label> email: <input type='text' name='email' /> </label><br/>");
		sb.append("<button type='submit'>submit</button> </form></body></html>");
		return sb.toString();
	}
	
	@RequestMapping(value="myinsert")
	public void addUser(HttpServletResponse response, @RequestParam("id") String id, @RequestParam("firstname") String firstname, 
			@RequestParam("lastname") String lastname,
			@RequestParam("email") String email) throws IOException{
		MyUser user = new MyUser(id, firstname, lastname, email);
		mongoService.addUser(user);
		response.sendRedirect("myall");
	}
	@RequestMapping(value="myall", headers="Accept=application/json")
	public List<MyUser> getAllUsers(){
		List<MyUser> users = mongoService.allPrn();
		System.out.println(users.size());
		return users;
		
	}
	@RequestMapping(value="count", produces = "text/html; charset=UTF-8")
	public String valuecount() {
		StringBuilder sb = new StringBuilder();
		sb.append("<html><body><font size=7> Count :");
		sb.append(mongoService.count()+"이다<font></body></html>");
		return sb.toString();
	}
	
	//public String updateForm() {}

//		
//	}
}
