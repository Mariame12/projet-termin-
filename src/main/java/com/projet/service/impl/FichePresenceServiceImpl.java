package com.projet.service.impl;

import com.projet.domain.FichePresence;
import com.projet.repository.FichePresenceRepository;
import com.projet.service.FichePresenceService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FichePresence}.
 */
@Service
@Transactional
public class FichePresenceServiceImpl implements FichePresenceService {

    private final Logger log = LoggerFactory.getLogger(FichePresenceServiceImpl.class);

    private final FichePresenceRepository fichePresenceRepository;

    public FichePresenceServiceImpl(FichePresenceRepository fichePresenceRepository) {
        this.fichePresenceRepository = fichePresenceRepository;
    }

    @Override
    public FichePresence save(FichePresence fichePresence) {
        log.debug("Request to save FichePresence : {}", fichePresence);
        return fichePresenceRepository.save(fichePresence);
    }

    @Override
    public Optional<FichePresence> partialUpdate(FichePresence fichePresence) {
        log.debug("Request to partially update FichePresence : {}", fichePresence);

        return fichePresenceRepository
            .findById(fichePresence.getId())
            .map(
                existingFichePresence -> {
                    if (fichePresence.getActivites() != null) {
                        existingFichePresence.setActivites(fichePresence.getActivites());
                    }
                    if (fichePresence.getHeuredebut() != null) {
                        existingFichePresence.setHeuredebut(fichePresence.getHeuredebut());
                    }
                    if (fichePresence.getCommentaire() != null) {
                        existingFichePresence.setCommentaire(fichePresence.getCommentaire());
                    }
                    if (fichePresence.getHeurefin() != null) {
                        existingFichePresence.setHeurefin(fichePresence.getHeurefin());
                    }
                    if (fichePresence.getDate() != null) {
                        existingFichePresence.setDate(fichePresence.getDate());
                    }

                    return existingFichePresence;
                }
            )
            .map(fichePresenceRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FichePresence> findAll(Pageable pageable) {
        log.debug("Request to get all FichePresences");
        return fichePresenceRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FichePresence> findOne(Long id) {
        log.debug("Request to get FichePresence : {}", id);
        return fichePresenceRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FichePresence : {}", id);
        fichePresenceRepository.deleteById(id);
    }
}
