package com.projet.web.rest;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.projet.domain.FichePresence;
import com.projet.repository.FichePresenceRepository;
import com.projet.service.FichePresenceQueryService;
import com.projet.service.FichePresenceService;
import com.projet.service.criteria.FichePresenceCriteria;
import com.projet.service.impl.ExportService;
import com.projet.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.projet.domain.FichePresence}.
 */
@RestController
@RequestMapping("/api")
public class FichePresenceResource {

    private final Logger log = LoggerFactory.getLogger(FichePresenceResource.class);

    private static final String ENTITY_NAME = "fichePresence";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FichePresenceService fichePresenceService;

    private final FichePresenceRepository fichePresenceRepository;

    private final FichePresenceQueryService fichePresenceQueryService;
    private final ExportService exportService;

    public FichePresenceResource(
        FichePresenceService fichePresenceService,
        FichePresenceRepository fichePresenceRepository,
        FichePresenceQueryService fichePresenceQueryService,
        ExportService exportService
    ) {
        this.fichePresenceService = fichePresenceService;
        this.fichePresenceRepository = fichePresenceRepository;
        this.fichePresenceQueryService = fichePresenceQueryService;
        this.exportService = exportService;
    }

    /**
     * {@code POST  /fiche-presences} : Create a new fichePresence.
     *
     * @param fichePresence the fichePresence to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fichePresence, or with status {@code 400 (Bad Request)} if the fichePresence has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fiche-presences")
    public ResponseEntity<FichePresence> createFichePresence(@RequestBody FichePresence fichePresence) throws URISyntaxException {
        log.debug("REST request to save FichePresence : {}", fichePresence);
        if (fichePresence.getId() != null) {
            throw new BadRequestAlertException("A new fichePresence cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FichePresence result = fichePresenceService.save(fichePresence);
        return ResponseEntity
            .created(new URI("/api/fiche-presences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fiche-presences/:id} : Updates an existing fichePresence.
     *
     * @param id the id of the fichePresence to save.
     * @param fichePresence the fichePresence to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fichePresence,
     * or with status {@code 400 (Bad Request)} if the fichePresence is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fichePresence couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fiche-presences/{id}")
    public ResponseEntity<FichePresence> updateFichePresence(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FichePresence fichePresence
    ) throws URISyntaxException {
        log.debug("REST request to update FichePresence : {}, {}", id, fichePresence);
        if (fichePresence.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fichePresence.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fichePresenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FichePresence result = fichePresenceService.save(fichePresence);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fichePresence.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /fiche-presences/:id} : Partial updates given fields of an existing fichePresence, field will ignore if it is null
     *
     * @param id the id of the fichePresence to save.
     * @param fichePresence the fichePresence to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fichePresence,
     * or with status {@code 400 (Bad Request)} if the fichePresence is not valid,
     * or with status {@code 404 (Not Found)} if the fichePresence is not found,
     * or with status {@code 500 (Internal Server Error)} if the fichePresence couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/fiche-presences/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<FichePresence> partialUpdateFichePresence(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FichePresence fichePresence
    ) throws URISyntaxException {
        log.debug("REST request to partial update FichePresence partially : {}, {}", id, fichePresence);
        if (fichePresence.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fichePresence.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fichePresenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FichePresence> result = fichePresenceService.partialUpdate(fichePresence);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fichePresence.getId().toString())
        );
    }

    /**
     * {@code GET  /fiche-presences} : get all the fichePresences.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fichePresences in body.
     * @throws Exception
     */
    @GetMapping("/fiche-presences")
    public ResponseEntity<List<FichePresence>> getAllFichePresences(FichePresenceCriteria criteria, Pageable pageable) throws Exception {
        log.debug("REST request to get FichePresences by criteria: {}", criteria);
        Page<FichePresence> page = fichePresenceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);

        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /fiche-presences/export/:id} : export  the file.
     *
     * @param criteria the criteria which the requested entities should match.
     * @throws Exception
     */

    @GetMapping("/fiche-presences/export/{id}")
    public void ExportFic(@PathVariable("id") Long idConsultant, HttpServletResponse response) throws Exception {
        //set file name and content type
        String filename = "fiches.csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");

        //create a csv writer
        StatefulBeanToCsv<FichePresence> writer = new StatefulBeanToCsvBuilder<FichePresence>(response.getWriter())
            .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
            .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
            .withOrderedResults(false)
            .build();

        //write all users to csv file
        writer.write(exportService.ExportFiche(idConsultant));
    }

    /**
     * {@code GET  /fiche-presences/count} : count all the fichePresences.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/fiche-presences/count")
    public ResponseEntity<Long> countFichePresences(FichePresenceCriteria criteria) {
        log.debug("REST request to count FichePresences by criteria: {}", criteria);
        return ResponseEntity.ok().body(fichePresenceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /fiche-presences/:id} : get the "id" fichePresence.
     *
     * @param id the id of the fichePresence to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fichePresence, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fiche-presences/{id}")
    public ResponseEntity<FichePresence> getFichePresence(@PathVariable Long id) {
        log.debug("REST request to get FichePresence : {}", id);
        Optional<FichePresence> fichePresence = fichePresenceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fichePresence);
    }

    /**
     * {@code DELETE  /fiche-presences/:id} : delete the "id" fichePresence.
     *
     * @param id the id of the fichePresence to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fiche-presences/{id}")
    public ResponseEntity<Void> deleteFichePresence(@PathVariable Long id) {
        log.debug("REST request to delete FichePresence : {}", id);
        fichePresenceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
