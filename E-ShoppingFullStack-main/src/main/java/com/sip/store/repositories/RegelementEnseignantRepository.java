package com.sip.store.repositories;

import com.sip.store.entities.ReglementEnseignant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegelementEnseignantRepository extends JpaRepository<ReglementEnseignant,Long> {
}
