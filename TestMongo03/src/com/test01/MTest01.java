package com.test01;

import java.io.IOException;

import com.mongodb.DB;

import static com.test01.Dao.*;

public class MTest01 {
	
	public static void main(String[] args) {
		try {
			if (Upload("/Users/ohjihye/Desktop/Test/mango.jpg","mango02.jpg",getConnection())) {
				System.out.println("업로드 성공");
			}
			System.out.println("2.전체출력");
			listAllImages(getConnection().getDB());
			
			
			System.out.println("3.apple02을 찾아서 정보를 출력하자.");
			File_Img(getConnection().getDB(),"mango02.jpg");
			
			System.out.println("4.업로드 하나 하고");
			if(Upload("/Users/ohjihye/Desktop/Test/mango.jpg","mango03.jpg",getConnection())) {
				System.out.println("업로드 성공");
			}
			
			System.out.println("5.다운로드 하나 하자.");
			Download(getConnection().getDB(),"mango03.jpg");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
