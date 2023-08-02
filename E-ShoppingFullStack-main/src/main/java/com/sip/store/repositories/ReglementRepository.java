package com.sip.store.repositories;

import com.sip.store.entities.DepartementMontantDTO;
import com.sip.store.entities.Reglement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReglementRepository extends JpaRepository<Reglement,Long> {
    @Query("SELECT d.titre AS departement, u.nom AS username, SUM(r.montant) AS totalMontant "
            + "FROM Reglement r "
            + "JOIN r.user u "
            + "JOIN u.classeList c "
            + "JOIN c.niveau n "
            + "JOIN n.departement d "
            + "GROUP BY d.titre, u.nom "
            + "ORDER BY SUM(r.montant) DESC")
    List<Object[]> countDepartementAndTotalMontantForUsers();
}
