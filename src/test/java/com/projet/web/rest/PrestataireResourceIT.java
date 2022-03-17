package com.projet.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.projet.IntegrationTest;
import com.projet.domain.Consultant;
import com.projet.domain.Prestataire;
import com.projet.repository.PrestataireRepository;
import com.projet.service.criteria.PrestataireCriteria;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PrestataireResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PrestataireResourceIT {

    private static final String DEFAULT_NOM_PRES = "AAAAAAAAAA";
    private static final String UPDATED_NOM_PRES = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_CONT = "AAAAAAAAAA";
    private static final String UPDATED_NOM_CONT = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM_CONT = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM_CONT = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/prestataires";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PrestataireRepository prestataireRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPrestataireMockMvc;

    private Prestataire prestataire;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Prestataire createEntity(EntityManager em) {
        Prestataire prestataire = new Prestataire()
            .nomPres(DEFAULT_NOM_PRES)
            .nomCont(DEFAULT_NOM_CONT)
            .prenomCont(DEFAULT_PRENOM_CONT)
            .email(DEFAULT_EMAIL);
        return prestataire;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Prestataire createUpdatedEntity(EntityManager em) {
        Prestataire prestataire = new Prestataire()
            .nomPres(UPDATED_NOM_PRES)
            .nomCont(UPDATED_NOM_CONT)
            .prenomCont(UPDATED_PRENOM_CONT)
            .email(UPDATED_EMAIL);
        return prestataire;
    }

    @BeforeEach
    public void initTest() {
        prestataire = createEntity(em);
    }

    @Test
    @Transactional
    void createPrestataire() throws Exception {
        int databaseSizeBeforeCreate = prestataireRepository.findAll().size();
        // Create the Prestataire
        restPrestataireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prestataire)))
            .andExpect(status().isCreated());

        // Validate the Prestataire in the database
        List<Prestataire> prestataireList = prestataireRepository.findAll();
        assertThat(prestataireList).hasSize(databaseSizeBeforeCreate + 1);
        Prestataire testPrestataire = prestataireList.get(prestataireList.size() - 1);
        assertThat(testPrestataire.getNomPres()).isEqualTo(DEFAULT_NOM_PRES);
        assertThat(testPrestataire.getNomCont()).isEqualTo(DEFAULT_NOM_CONT);
        assertThat(testPrestataire.getPrenomCont()).isEqualTo(DEFAULT_PRENOM_CONT);
        assertThat(testPrestataire.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void createPrestataireWithExistingId() throws Exception {
        // Create the Prestataire with an existing ID
        prestataire.setId(1L);

        int databaseSizeBeforeCreate = prestataireRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrestataireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prestataire)))
            .andExpect(status().isBadRequest());

        // Validate the Prestataire in the database
        List<Prestataire> prestataireList = prestataireRepository.findAll();
        assertThat(prestataireList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPrestataires() throws Exception {
        // Initialize the database
        prestataireRepository.saveAndFlush(prestataire);

        // Get all the prestataireList
        restPrestataireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prestataire.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomPres").value(hasItem(DEFAULT_NOM_PRES)))
            .andExpect(jsonPath("$.[*].nomCont").value(hasItem(DEFAULT_NOM_CONT)))
            .andExpect(jsonPath("$.[*].prenomCont").value(hasItem(DEFAULT_PRENOM_CONT)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }

    @Test
    @Transactional
    void getPrestataire() throws Exception {
        // Initialize the database
        prestataireRepository.saveAndFlush(prestataire);

        // Get the prestataire
        restPrestataireMockMvc
            .perform(get(ENTITY_API_URL_ID, prestataire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(prestataire.getId().intValue()))
            .andExpect(jsonPath("$.nomPres").value(DEFAULT_NOM_PRES))
            .andExpect(jsonPath("$.nomCont").value(DEFAULT_NOM_CONT))
            .andExpect(jsonPath("$.prenomCont").value(DEFAULT_PRENOM_CONT))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }

    @Test
    @Transactional
    void getPrestatairesByIdFiltering() throws Exception {
        // Initialize the database
        prestataireRepository.saveAndFlush(prestataire);

        Long id = prestataire.getId();

        defaultPrestataireShouldBeFound("id.equals=" + id);
        defaultPrestataireShouldNotBeFound("id.notEquals=" + id);

        defaultPrestataireShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPrestataireShouldNotBeFound("id.greaterThan=" + id);

        defaultPrestataireShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPrestataireShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPrestatairesByNomPresIsEqualToSomething() throws Exception {
        // Initialize the database
        prestataireRepository.saveAndFlush(prestataire);

        // Get all the prestataireList where nomPres equals to DEFAULT_NOM_PRES
        defaultPrestataireShouldBeFound("nomPres.equals=" + DEFAULT_NOM_PRES);

        // Get all the prestataireList where nomPres equals to UPDATED_NOM_PRES
        defaultPrestataireShouldNotBeFound("nomPres.equals=" + UPDATED_NOM_PRES);
    }

    @Test
    @Transactional
    void getAllPrestatairesByNomPresIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prestataireRepository.saveAndFlush(prestataire);

        // Get all the prestataireList where nomPres not equals to DEFAULT_NOM_PRES
        defaultPrestataireShouldNotBeFound("nomPres.notEquals=" + DEFAULT_NOM_PRES);

        // Get all the prestataireList where nomPres not equals to UPDATED_NOM_PRES
        defaultPrestataireShouldBeFound("nomPres.notEquals=" + UPDATED_NOM_PRES);
    }

    @Test
    @Transactional
    void getAllPrestatairesByNomPresIsInShouldWork() throws Exception {
        // Initialize the database
        prestataireRepository.saveAndFlush(prestataire);

        // Get all the prestataireList where nomPres in DEFAULT_NOM_PRES or UPDATED_NOM_PRES
        defaultPrestataireShouldBeFound("nomPres.in=" + DEFAULT_NOM_PRES + "," + UPDATED_NOM_PRES);

        // Get all the prestataireList where nomPres equals to UPDATED_NOM_PRES
        defaultPrestataireShouldNotBeFound("nomPres.in=" + UPDATED_NOM_PRES);
    }

    @Test
    @Transactional
    void getAllPrestatairesByNomPresIsNullOrNotNull() throws Exception {
        // Initialize the database
        prestataireRepository.saveAndFlush(prestataire);

        // Get all the prestataireList where nomPres is not null
        defaultPrestataireShouldBeFound("nomPres.specified=true");

        // Get all the prestataireList where nomPres is null
        defaultPrestataireShouldNotBeFound("nomPres.specified=false");
    }

    @Test
    @Transactional
    void getAllPrestatairesByNomPresContainsSomething() throws Exception {
        // Initialize the database
        prestataireRepository.saveAndFlush(prestataire);

        // Get all the prestataireList where nomPres contains DEFAULT_NOM_PRES
        defaultPrestataireShouldBeFound("nomPres.contains=" + DEFAULT_NOM_PRES);

        // Get all the prestataireList where nomPres contains UPDATED_NOM_PRES
        defaultPrestataireShouldNotBeFound("nomPres.contains=" + UPDATED_NOM_PRES);
    }

    @Test
    @Transactional
    void getAllPrestatairesByNomPresNotContainsSomething() throws Exception {
        // Initialize the database
        prestataireRepository.saveAndFlush(prestataire);

        // Get all the prestataireList where nomPres does not contain DEFAULT_NOM_PRES
        defaultPrestataireShouldNotBeFound("nomPres.doesNotContain=" + DEFAULT_NOM_PRES);

        // Get all the prestataireList where nomPres does not contain UPDATED_NOM_PRES
        defaultPrestataireShouldBeFound("nomPres.doesNotContain=" + UPDATED_NOM_PRES);
    }

    @Test
    @Transactional
    void getAllPrestatairesByNomContIsEqualToSomething() throws Exception {
        // Initialize the database
        prestataireRepository.saveAndFlush(prestataire);

        // Get all the prestataireList where nomCont equals to DEFAULT_NOM_CONT
        defaultPrestataireShouldBeFound("nomCont.equals=" + DEFAULT_NOM_CONT);

        // Get all the prestataireList where nomCont equals to UPDATED_NOM_CONT
        defaultPrestataireShouldNotBeFound("nomCont.equals=" + UPDATED_NOM_CONT);
    }

    @Test
    @Transactional
    void getAllPrestatairesByNomContIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prestataireRepository.saveAndFlush(prestataire);

        // Get all the prestataireList where nomCont not equals to DEFAULT_NOM_CONT
        defaultPrestataireShouldNotBeFound("nomCont.notEquals=" + DEFAULT_NOM_CONT);

        // Get all the prestataireList where nomCont not equals to UPDATED_NOM_CONT
        defaultPrestataireShouldBeFound("nomCont.notEquals=" + UPDATED_NOM_CONT);
    }

    @Test
    @Transactional
    void getAllPrestatairesByNomContIsInShouldWork() throws Exception {
        // Initialize the database
        prestataireRepository.saveAndFlush(prestataire);

        // Get all the prestataireList where nomCont in DEFAULT_NOM_CONT or UPDATED_NOM_CONT
        defaultPrestataireShouldBeFound("nomCont.in=" + DEFAULT_NOM_CONT + "," + UPDATED_NOM_CONT);

        // Get all the prestataireList where nomCont equals to UPDATED_NOM_CONT
        defaultPrestataireShouldNotBeFound("nomCont.in=" + UPDATED_NOM_CONT);
    }

    @Test
    @Transactional
    void getAllPrestatairesByNomContIsNullOrNotNull() throws Exception {
        // Initialize the database
        prestataireRepository.saveAndFlush(prestataire);

        // Get all the prestataireList where nomCont is not null
        defaultPrestataireShouldBeFound("nomCont.specified=true");

        // Get all the prestataireList where nomCont is null
        defaultPrestataireShouldNotBeFound("nomCont.specified=false");
    }

    @Test
    @Transactional
    void getAllPrestatairesByNomContContainsSomething() throws Exception {
        // Initialize the database
        prestataireRepository.saveAndFlush(prestataire);

        // Get all the prestataireList where nomCont contains DEFAULT_NOM_CONT
        defaultPrestataireShouldBeFound("nomCont.contains=" + DEFAULT_NOM_CONT);

        // Get all the prestataireList where nomCont contains UPDATED_NOM_CONT
        defaultPrestataireShouldNotBeFound("nomCont.contains=" + UPDATED_NOM_CONT);
    }

    @Test
    @Transactional
    void getAllPrestatairesByNomContNotContainsSomething() throws Exception {
        // Initialize the database
        prestataireRepository.saveAndFlush(prestataire);

        // Get all the prestataireList where nomCont does not contain DEFAULT_NOM_CONT
        defaultPrestataireShouldNotBeFound("nomCont.doesNotContain=" + DEFAULT_NOM_CONT);

        // Get all the prestataireList where nomCont does not contain UPDATED_NOM_CONT
        defaultPrestataireShouldBeFound("nomCont.doesNotContain=" + UPDATED_NOM_CONT);
    }

    @Test
    @Transactional
    void getAllPrestatairesByPrenomContIsEqualToSomething() throws Exception {
        // Initialize the database
        prestataireRepository.saveAndFlush(prestataire);

        // Get all the prestataireList where prenomCont equals to DEFAULT_PRENOM_CONT
        defaultPrestataireShouldBeFound("prenomCont.equals=" + DEFAULT_PRENOM_CONT);

        // Get all the prestataireList where prenomCont equals to UPDATED_PRENOM_CONT
        defaultPrestataireShouldNotBeFound("prenomCont.equals=" + UPDATED_PRENOM_CONT);
    }

    @Test
    @Transactional
    void getAllPrestatairesByPrenomContIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prestataireRepository.saveAndFlush(prestataire);

        // Get all the prestataireList where prenomCont not equals to DEFAULT_PRENOM_CONT
        defaultPrestataireShouldNotBeFound("prenomCont.notEquals=" + DEFAULT_PRENOM_CONT);

        // Get all the prestataireList where prenomCont not equals to UPDATED_PRENOM_CONT
        defaultPrestataireShouldBeFound("prenomCont.notEquals=" + UPDATED_PRENOM_CONT);
    }

    @Test
    @Transactional
    void getAllPrestatairesByPrenomContIsInShouldWork() throws Exception {
        // Initialize the database
        prestataireRepository.saveAndFlush(prestataire);

        // Get all the prestataireList where prenomCont in DEFAULT_PRENOM_CONT or UPDATED_PRENOM_CONT
        defaultPrestataireShouldBeFound("prenomCont.in=" + DEFAULT_PRENOM_CONT + "," + UPDATED_PRENOM_CONT);

        // Get all the prestataireList where prenomCont equals to UPDATED_PRENOM_CONT
        defaultPrestataireShouldNotBeFound("prenomCont.in=" + UPDATED_PRENOM_CONT);
    }

    @Test
    @Transactional
    void getAllPrestatairesByPrenomContIsNullOrNotNull() throws Exception {
        // Initialize the database
        prestataireRepository.saveAndFlush(prestataire);

        // Get all the prestataireList where prenomCont is not null
        defaultPrestataireShouldBeFound("prenomCont.specified=true");

        // Get all the prestataireList where prenomCont is null
        defaultPrestataireShouldNotBeFound("prenomCont.specified=false");
    }

    @Test
    @Transactional
    void getAllPrestatairesByPrenomContContainsSomething() throws Exception {
        // Initialize the database
        prestataireRepository.saveAndFlush(prestataire);

        // Get all the prestataireList where prenomCont contains DEFAULT_PRENOM_CONT
        defaultPrestataireShouldBeFound("prenomCont.contains=" + DEFAULT_PRENOM_CONT);

        // Get all the prestataireList where prenomCont contains UPDATED_PRENOM_CONT
        defaultPrestataireShouldNotBeFound("prenomCont.contains=" + UPDATED_PRENOM_CONT);
    }

    @Test
    @Transactional
    void getAllPrestatairesByPrenomContNotContainsSomething() throws Exception {
        // Initialize the database
        prestataireRepository.saveAndFlush(prestataire);

        // Get all the prestataireList where prenomCont does not contain DEFAULT_PRENOM_CONT
        defaultPrestataireShouldNotBeFound("prenomCont.doesNotContain=" + DEFAULT_PRENOM_CONT);

        // Get all the prestataireList where prenomCont does not contain UPDATED_PRENOM_CONT
        defaultPrestataireShouldBeFound("prenomCont.doesNotContain=" + UPDATED_PRENOM_CONT);
    }

    @Test
    @Transactional
    void getAllPrestatairesByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        prestataireRepository.saveAndFlush(prestataire);

        // Get all the prestataireList where email equals to DEFAULT_EMAIL
        defaultPrestataireShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the prestataireList where email equals to UPDATED_EMAIL
        defaultPrestataireShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPrestatairesByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prestataireRepository.saveAndFlush(prestataire);

        // Get all the prestataireList where email not equals to DEFAULT_EMAIL
        defaultPrestataireShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the prestataireList where email not equals to UPDATED_EMAIL
        defaultPrestataireShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPrestatairesByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        prestataireRepository.saveAndFlush(prestataire);

        // Get all the prestataireList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultPrestataireShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the prestataireList where email equals to UPDATED_EMAIL
        defaultPrestataireShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPrestatairesByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        prestataireRepository.saveAndFlush(prestataire);

        // Get all the prestataireList where email is not null
        defaultPrestataireShouldBeFound("email.specified=true");

        // Get all the prestataireList where email is null
        defaultPrestataireShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllPrestatairesByEmailContainsSomething() throws Exception {
        // Initialize the database
        prestataireRepository.saveAndFlush(prestataire);

        // Get all the prestataireList where email contains DEFAULT_EMAIL
        defaultPrestataireShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the prestataireList where email contains UPDATED_EMAIL
        defaultPrestataireShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPrestatairesByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        prestataireRepository.saveAndFlush(prestataire);

        // Get all the prestataireList where email does not contain DEFAULT_EMAIL
        defaultPrestataireShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the prestataireList where email does not contain UPDATED_EMAIL
        defaultPrestataireShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPrestatairesByConsultantIsEqualToSomething() throws Exception {
        // Initialize the database
        prestataireRepository.saveAndFlush(prestataire);
        Consultant consultant = ConsultantResourceIT.createEntity(em);
        em.persist(consultant);
        em.flush();
        prestataire.addConsultant(consultant);
        prestataireRepository.saveAndFlush(prestataire);
        Long consultantId = consultant.getId();

        // Get all the prestataireList where consultant equals to consultantId
        defaultPrestataireShouldBeFound("consultantId.equals=" + consultantId);

        // Get all the prestataireList where consultant equals to (consultantId + 1)
        defaultPrestataireShouldNotBeFound("consultantId.equals=" + (consultantId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPrestataireShouldBeFound(String filter) throws Exception {
        restPrestataireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prestataire.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomPres").value(hasItem(DEFAULT_NOM_PRES)))
            .andExpect(jsonPath("$.[*].nomCont").value(hasItem(DEFAULT_NOM_CONT)))
            .andExpect(jsonPath("$.[*].prenomCont").value(hasItem(DEFAULT_PRENOM_CONT)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));

        // Check, that the count call also returns 1
        restPrestataireMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPrestataireShouldNotBeFound(String filter) throws Exception {
        restPrestataireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPrestataireMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPrestataire() throws Exception {
        // Get the prestataire
        restPrestataireMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPrestataire() throws Exception {
        // Initialize the database
        prestataireRepository.saveAndFlush(prestataire);

        int databaseSizeBeforeUpdate = prestataireRepository.findAll().size();

        // Update the prestataire
        Prestataire updatedPrestataire = prestataireRepository.findById(prestataire.getId()).get();
        // Disconnect from session so that the updates on updatedPrestataire are not directly saved in db
        em.detach(updatedPrestataire);
        updatedPrestataire.nomPres(UPDATED_NOM_PRES).nomCont(UPDATED_NOM_CONT).prenomCont(UPDATED_PRENOM_CONT).email(UPDATED_EMAIL);

        restPrestataireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPrestataire.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPrestataire))
            )
            .andExpect(status().isOk());

        // Validate the Prestataire in the database
        List<Prestataire> prestataireList = prestataireRepository.findAll();
        assertThat(prestataireList).hasSize(databaseSizeBeforeUpdate);
        Prestataire testPrestataire = prestataireList.get(prestataireList.size() - 1);
        assertThat(testPrestataire.getNomPres()).isEqualTo(UPDATED_NOM_PRES);
        assertThat(testPrestataire.getNomCont()).isEqualTo(UPDATED_NOM_CONT);
        assertThat(testPrestataire.getPrenomCont()).isEqualTo(UPDATED_PRENOM_CONT);
        assertThat(testPrestataire.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void putNonExistingPrestataire() throws Exception {
        int databaseSizeBeforeUpdate = prestataireRepository.findAll().size();
        prestataire.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrestataireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prestataire.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prestataire))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prestataire in the database
        List<Prestataire> prestataireList = prestataireRepository.findAll();
        assertThat(prestataireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPrestataire() throws Exception {
        int databaseSizeBeforeUpdate = prestataireRepository.findAll().size();
        prestataire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrestataireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prestataire))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prestataire in the database
        List<Prestataire> prestataireList = prestataireRepository.findAll();
        assertThat(prestataireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPrestataire() throws Exception {
        int databaseSizeBeforeUpdate = prestataireRepository.findAll().size();
        prestataire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrestataireMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prestataire)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Prestataire in the database
        List<Prestataire> prestataireList = prestataireRepository.findAll();
        assertThat(prestataireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePrestataireWithPatch() throws Exception {
        // Initialize the database
        prestataireRepository.saveAndFlush(prestataire);

        int databaseSizeBeforeUpdate = prestataireRepository.findAll().size();

        // Update the prestataire using partial update
        Prestataire partialUpdatedPrestataire = new Prestataire();
        partialUpdatedPrestataire.setId(prestataire.getId());

        partialUpdatedPrestataire.nomPres(UPDATED_NOM_PRES).nomCont(UPDATED_NOM_CONT).email(UPDATED_EMAIL);

        restPrestataireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrestataire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrestataire))
            )
            .andExpect(status().isOk());

        // Validate the Prestataire in the database
        List<Prestataire> prestataireList = prestataireRepository.findAll();
        assertThat(prestataireList).hasSize(databaseSizeBeforeUpdate);
        Prestataire testPrestataire = prestataireList.get(prestataireList.size() - 1);
        assertThat(testPrestataire.getNomPres()).isEqualTo(UPDATED_NOM_PRES);
        assertThat(testPrestataire.getNomCont()).isEqualTo(UPDATED_NOM_CONT);
        assertThat(testPrestataire.getPrenomCont()).isEqualTo(DEFAULT_PRENOM_CONT);
        assertThat(testPrestataire.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void fullUpdatePrestataireWithPatch() throws Exception {
        // Initialize the database
        prestataireRepository.saveAndFlush(prestataire);

        int databaseSizeBeforeUpdate = prestataireRepository.findAll().size();

        // Update the prestataire using partial update
        Prestataire partialUpdatedPrestataire = new Prestataire();
        partialUpdatedPrestataire.setId(prestataire.getId());

        partialUpdatedPrestataire.nomPres(UPDATED_NOM_PRES).nomCont(UPDATED_NOM_CONT).prenomCont(UPDATED_PRENOM_CONT).email(UPDATED_EMAIL);

        restPrestataireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrestataire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrestataire))
            )
            .andExpect(status().isOk());

        // Validate the Prestataire in the database
        List<Prestataire> prestataireList = prestataireRepository.findAll();
        assertThat(prestataireList).hasSize(databaseSizeBeforeUpdate);
        Prestataire testPrestataire = prestataireList.get(prestataireList.size() - 1);
        assertThat(testPrestataire.getNomPres()).isEqualTo(UPDATED_NOM_PRES);
        assertThat(testPrestataire.getNomCont()).isEqualTo(UPDATED_NOM_CONT);
        assertThat(testPrestataire.getPrenomCont()).isEqualTo(UPDATED_PRENOM_CONT);
        assertThat(testPrestataire.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void patchNonExistingPrestataire() throws Exception {
        int databaseSizeBeforeUpdate = prestataireRepository.findAll().size();
        prestataire.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrestataireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, prestataire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prestataire))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prestataire in the database
        List<Prestataire> prestataireList = prestataireRepository.findAll();
        assertThat(prestataireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPrestataire() throws Exception {
        int databaseSizeBeforeUpdate = prestataireRepository.findAll().size();
        prestataire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrestataireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prestataire))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prestataire in the database
        List<Prestataire> prestataireList = prestataireRepository.findAll();
        assertThat(prestataireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPrestataire() throws Exception {
        int databaseSizeBeforeUpdate = prestataireRepository.findAll().size();
        prestataire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrestataireMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(prestataire))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Prestataire in the database
        List<Prestataire> prestataireList = prestataireRepository.findAll();
        assertThat(prestataireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePrestataire() throws Exception {
        // Initialize the database
        prestataireRepository.saveAndFlush(prestataire);

        int databaseSizeBeforeDelete = prestataireRepository.findAll().size();

        // Delete the prestataire
        restPrestataireMockMvc
            .perform(delete(ENTITY_API_URL_ID, prestataire.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Prestataire> prestataireList = prestataireRepository.findAll();
        assertThat(prestataireList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
