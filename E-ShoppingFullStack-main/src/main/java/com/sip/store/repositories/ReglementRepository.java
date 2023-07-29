package com.sip.store.repositories;

import com.sip.store.entities.Reglement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReglementRepository extends JpaRepository<Reglement,Long> {
}
