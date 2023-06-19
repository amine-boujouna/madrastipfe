package com.sip.store.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sip.store.entities.Messaging;
import com.sip.store.entities.User;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    
    @Query("FROM Messaging m WHERE m.recipient = ?1")
    List<Messaging> findMessaginngByUser(long id);
    
   /* @Query("FROM User u WHERE u.id = ?1")
    User findById(long id);*/
}