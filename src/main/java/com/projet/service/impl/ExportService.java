package com.projet.service.impl;


import com.projet.domain.FichePresence;
import com.projet.domain.Consultant;
import com.projet.repository.ConsultantRepository;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

import com.projet.repository.FichePresenceRepository;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class ExportService {

    private final FichePresenceRepository fchrep;
    private final ConsultantRepository crep;

    public ExportService(FichePresenceRepository fchrep, ConsultantRepository crep) {
        super();
        this.fchrep = fchrep;
		this.crep = crep;
	
    }

    public List<FichePresence> ExportFiche(Long id) {
    	Optional<Consultant> consult =crep.findById(id);
    	
        return fchrep.findAllByConsultant(consult);
    }
}
