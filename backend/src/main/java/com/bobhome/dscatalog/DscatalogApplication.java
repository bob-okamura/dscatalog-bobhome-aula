package com.bobhome.dscatalog;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bobhome.dscatalog.services.S3Service;

@SpringBootApplication
public class DscatalogApplication implements CommandLineRunner{
	
	@Autowired
	private S3Service s3Service;

	public static void main(String[] args) {
		SpringApplication.run(DscatalogApplication.class, args);
	}
	

	void contextLoads() {
		
	}


	@Override
	public void run(String... args) throws Exception {
		
		s3Service.uploadFile("C:\\github-imagens\\print.jpg");
		
	}

}
