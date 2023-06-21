package com.sip.store;

import com.sip.store.controllers.ProviderController;
import com.sip.store.entities.Provider;
import com.sip.store.repositories.ProviderRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


import com.sip.store.services.FileStorageService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

import javax.annotation.Resource;

@SpringBootApplication
public class StoreApplication {
	@Resource
	FileStorageService storageService;
	public static void main(String[] args) {

		SpringApplication.run(StoreApplication.class, args);
	}
	public void run(String... arg) throws Exception {
//    storageService.deleteAll();
		storageService.init();
	}

}
