package com.test.dao;

import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapreduce.GroupBy;
import org.springframework.data.mongodb.core.mapreduce.GroupByResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.test.domain.MyUser;
import com.test.utility.DBUtility;


@Service
public class MONGOService {

	private MongoOperations mongoOperations;
	
	public MONGOService() {
		this.mongoOperations = DBUtility.getCollection();
	}
//	public void setMongoOperations(MongoOperations mongoOperations) {
//		this.mongoOperations = mongoOperations;
//	}
	public List<MyUser> allPrn(){
		List<MyUser> userList = mongoOperations.find(new Query(), MyUser.class);
		System.out.println(userList.size());
		
		return userList;
	}
	
	public void addUser(MyUser user) {
		mongoOperations.insert(user);
	}
	
	@SuppressWarnings("static-access")
	public void updateUser(MyUser user) {
		mongoOperations.findAndModify(new Query(Criteria.where("_id").is(user.getUserid())), 
				Update.update("firstname", user.getFirstName()).update("lastname", user.getLastName())
				.update("email", user.getEmail()), MyUser.class);
	}
	public double count() {
		GroupByResults<MyUser> results = mongoOperations.group("myUser",
				GroupBy.key("_id").initialDocument("{ count: 0 }")
				.reduceFunction("function(doc,prev){prev.count += 1 }"), MyUser.class);
		
		
		
		return results.getCount();
	}
	
}
