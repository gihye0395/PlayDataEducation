package com.test;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

public class MTest {
	
	public static void main(String[] args) {
		try {
			MongoClient mongo =null;
			mongo = new MongoClient("localhost",27017);
			DB db = mongo.getDB("imagedb");
			DBCollection collection = db.getCollection("photo.chunks");
			System.out.println(collection.getFullName());
			String newFileName = "mango";
			File imageFile = new File("/Users/ohjihye/Desktop/Test/mango.jpg");
			//1.사진을 입력 컬렉션 
			GridFS gfsPhoto = new GridFS(db,"photo");
			//2.드라이브파일 경로지정 
			GridFSInputFile gfsFile =gfsPhoto.createFile(imageFile);
			//3.저장할 파일이름지정
			gfsFile.setFilename(newFileName);
			//4.몽고 디비에 저장 
			gfsFile.save();
			//5.출력
			DBCursor cursor = gfsPhoto.getFileList();
			while(cursor.hasNext()) {
				System.out.println(cursor.next());
			}
			
			  GridFSDBFile imageForOutput = gfsPhoto.findOne(newFileName);
			  imageForOutput.writeTo("mango.jpg");
			  gfsPhoto.remove(gfsPhoto.findOne(newFileName));
			  System.out.println(imageForOutput.getChunkSize());
			  System.out.println("file size"+imageForOutput.getLength());
			  System.out.println(imageForOutput.getFilename());
			 
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}

}
