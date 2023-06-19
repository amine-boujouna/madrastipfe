package com.sip.store;

import com.sip.store.controllers.ProviderController;
import com.sip.store.entities.Provider;
import com.sip.store.repositories.ProviderRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class StoreApplication {

	public static void main(String[] args) {
		//new File(ArticleController.uploadDirectory).mkdir();

		SpringApplication.run(StoreApplication.class, args);
		/*Path path = Paths.get(ProviderController.uploadDirectory);
		try{
			Files.createDirectory(path);
		}
		catch(IOException ex){

		}*/

	}

}
