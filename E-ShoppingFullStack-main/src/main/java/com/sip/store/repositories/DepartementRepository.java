package com.sip.store.repositories;

import com.sip.store.entities.Article;
import com.sip.store.entities.Departement;
import com.sip.store.entities.Niveau;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartementRepository extends JpaRepository<Departement,Long> {
    @Query("FROM Niveau a WHERE a.departement.id = ?1")
    List<Niveau> findNiveauByDepartement(long id);
}
