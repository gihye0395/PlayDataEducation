package com.test01;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

public class Dao {
	
	public static DBCollection getConnection() throws UnknownHostException{
		MongoClient mongo;
		mongo = new MongoClient("localhost",27017);
		DB db = mongo.getDB("imagedb");
		DBCollection collection = db.getCollection("myDB"); //하위컬렉션
		return collection;
	}
	
	public void method() throws IOException{
		throw new IOException();
		
	}
	
	public static boolean Upload(String O_filename, String N_filename, DBCollection db) throws IOException {
		String newFileName = N_filename;
		File imageFile = new File(O_filename);
		GridFS gfsPhoto = new GridFS(db.getDB(), "photo");
		GridFSInputFile gfsFile = gfsPhoto.createFile(imageFile);
		gfsFile.setFilename(newFileName);
		gfsFile.save();
		return true;
		
	}
	
	public static void listAllImages(DB db) {
		GridFS gfsPhoto = new GridFS(db, "photo");
		DBCursor cursor = gfsPhoto.getFileList();
		while(cursor.hasNext()) {
			System.out.println(cursor.next());
		}
	}
	
	public static void deleteImageFromMongoDB(DB db, String d_file) {
		GridFS gfsPhoto = new GridFS(db, "photo");
		gfsPhoto.remove(gfsPhoto.findOne(d_file));
	}
	
	public static void Download(DB db, String down_file) throws IOException{
		GridFS gfsPhoto = new GridFS(db,"photo");
		GridFSDBFile res = gfsPhoto.findOne(down_file);
		res.writeTo("/Users/ohjihye/Desktop/Test/"+res.getFilename()+".jpg");
	}
	
	public static void File_Img(DB db, String filename) {
		GridFS gfsPhoto = new GridFS(db, "photo");
		DBCursor cursor = gfsPhoto.getFileList();
		while(cursor.hasNext()) {
			DBObject obj = cursor.next();
			if(obj.get("filename").equals(filename)) {
				System.out.println(obj);
			}
		}
	}

}
