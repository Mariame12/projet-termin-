package com.projet.service.impl;

import com.projet.domain.Prestataire;
import com.projet.repository.PrestataireRepository;
import com.projet.service.PrestataireService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Prestataire}.
 */
@Service
@Transactional
public class PrestataireServiceImpl implements PrestataireService {

    private final Logger log = LoggerFactory.getLogger(PrestataireServiceImpl.class);

    private final PrestataireRepository prestataireRepository;

    public PrestataireServiceImpl(PrestataireRepository prestataireRepository) {
        this.prestataireRepository = prestataireRepository;
    }

    @Override
    public Prestataire save(Prestataire prestataire) {
        log.debug("Request to save Prestataire : {}", prestataire);
        return prestataireRepository.save(prestataire);
    }

    @Override
    public Optional<Prestataire> partialUpdate(Prestataire prestataire) {
        log.debug("Request to partially update Prestataire : {}", prestataire);

        return prestataireRepository
            .findById(prestataire.getId())
            .map(
                existingPrestataire -> {
                    if (prestataire.getNomPres() != null) {
                        existingPrestataire.setNomPres(prestataire.getNomPres());
                    }
                    if (prestataire.getNomCont() != null) {
                        existingPrestataire.setNomCont(prestataire.getNomCont());
                    }
                    if (prestataire.getPrenomCont() != null) {
                        existingPrestataire.setPrenomCont(prestataire.getPrenomCont());
                    }
                    if (prestataire.getEmail() != null) {
                        existingPrestataire.setEmail(prestataire.getEmail());
                    }

                    return existingPrestataire;
                }
            )
            .map(prestataireRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Prestataire> findAll(Pageable pageable) {
        log.debug("Request to get all Prestataires");
        return prestataireRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Prestataire> findOne(Long id) {
        log.debug("Request to get Prestataire : {}", id);
        return prestataireRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Prestataire : {}", id);
        prestataireRepository.deleteById(id);
    }
}
