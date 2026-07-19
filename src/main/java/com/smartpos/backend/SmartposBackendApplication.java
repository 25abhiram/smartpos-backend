package com.smartpos.backend;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SmartposBackendApplication {

	public static void main(String[] args) {
		Dotenv dotenv=Dotenv.configure().directory("./").ignoreIfMalformed().ignoreIfMissing().load();

		if (dotenv.get("DB_URL")!=null){
			System.setProperty("DB_URL",dotenv.get("DB_URL"));
		}
		if (dotenv.get("DB_USERNAME")!=null){
			System.setProperty("DB_USERNAME",dotenv.get("DB_USERNAME"));
		}
		if (dotenv.get("DB_PASSWORD")!=null){
			System.setProperty("DB_PASSWORD",dotenv.get("DB_PASSWORD"));
		}
		if (dotenv.get("JWT_SECRET")!=null){
			System.setProperty("JWT_SECRET",dotenv.get("JWT_SECRET"));
		}
		SpringApplication.run(SmartposBackendApplication.class, args);
	}

}
