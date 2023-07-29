package com.sip.store.repositories;

import com.sip.store.entities.Emploi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmploiRepository extends JpaRepository<Emploi,Long> {
}
