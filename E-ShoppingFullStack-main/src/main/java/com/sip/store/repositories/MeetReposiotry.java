package com.sip.store.repositories;

import com.sip.store.entities.Meet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetReposiotry extends JpaRepository<Meet,Long> {
}
