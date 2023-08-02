package com.sip.store.services;

import com.sip.store.entities.DepartementMontantDTO;
import com.sip.store.repositories.ReglementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ReglementService {
    @Autowired
    private ReglementRepository reglementRepository;
    public List<Object[]> getDepartementAndTotalMontantForUsers() {
        return reglementRepository.countDepartementAndTotalMontantForUsers();
    }
}
