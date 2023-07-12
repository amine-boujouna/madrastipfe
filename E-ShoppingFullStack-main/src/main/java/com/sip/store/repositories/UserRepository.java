package com.sip.store.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sip.store.entities.Messaging;
import com.sip.store.entities.User;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    @Query("from User u  where u.username= ?1")
    User findByUsername(String username);
    
    @Query("FROM Messaging m WHERE m.recipient = ?1")
    List<Messaging> findMessaginngByUser(long id);
    
   /* @Query("FROM User u WHERE u.id = ?1")
    User findById(long id);*/
   @Modifying
   @Query("delete from User u where u.id=:id")
   void deleteuser(Long id);
    //@Query("SELECT u.nom, u.prenom, c.nomc FROM User u JOIN u.classeList c")
    @Query("SELECT u.nom AS nom, u.prenom AS prenom, c.nomc AS nomc FROM User u JOIN u.classeList c")
    List<Object[]> getUsersWithClasses();
}