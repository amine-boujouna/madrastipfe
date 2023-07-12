package com.sip.store.repositories;

import com.sip.store.entities.Meet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetReposiotry extends JpaRepository<Meet,Long> {

    @Query("SELECT lienmeet FROM Meet")
    List<Object[]>   getLienmeet();
}
