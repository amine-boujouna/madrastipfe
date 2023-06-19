package com.sip.store.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.sip.store.entities.Messaging;
import com.sip.store.entities.User;

public interface MessagingRepository  extends CrudRepository<Messaging, Long> {
	/*@Query("FROM Article a WHERE a.inventoryQuantity = 0")
    List<Article> findProductSoldOut();*/
	
	@Query("FROM Messaging m WHERE m.recipient = ?1")
	List<Messaging> findAllMessageOfCurrentUser(User user);
	
	@Query("FROM Messaging m WHERE m.status = ?1 and m.recipient = ?2")
	List<Messaging> findAllMessageByStatus(String status,User user);
}
