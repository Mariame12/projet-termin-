package com.projet.service.impl;

import com.projet.domain.Consultant;
import com.projet.repository.ConsultantRepository;
import com.projet.service.ConsultantService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Consultant}.
 */
@Service
@Transactional
public class ConsultantServiceImpl implements ConsultantService {

    private final Logger log = LoggerFactory.getLogger(ConsultantServiceImpl.class);

    private final ConsultantRepository consultantRepository;

    public ConsultantServiceImpl(ConsultantRepository consultantRepository) {
        this.consultantRepository = consultantRepository;
    }

    @Override
    public Consultant save(Consultant consultant) {
        log.debug("Request to save Consultant : {}", consultant);
        return consultantRepository.save(consultant);
    }

    @Override
    public Optional<Consultant> partialUpdate(Consultant consultant) {
        log.debug("Request to partially update Consultant : {}", consultant);

        return consultantRepository
            .findById(consultant.getId())
            .map(
                existingConsultant -> {
                    if (consultant.getNom() != null) {
                        existingConsultant.setNom(consultant.getNom());
                    }
                    if (consultant.getPrenom() != null) {
                        existingConsultant.setPrenom(consultant.getPrenom());
                    }
                    if (consultant.getFonction() != null) {
                        existingConsultant.setFonction(consultant.getFonction());
                    }

                    return existingConsultant;
                }
            )
            .map(consultantRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Consultant> findAll(Pageable pageable) {
        log.debug("Request to get all Consultants");
        return consultantRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Consultant> findOne(Long id) {
        log.debug("Request to get Consultant : {}", id);
        return consultantRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Consultant : {}", id);
        consultantRepository.deleteById(id);
    }
}
