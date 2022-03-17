package com.projet.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.projet.IntegrationTest;
import com.projet.domain.Consultant;
import com.projet.domain.FichePresence;
import com.projet.repository.FichePresenceRepository;
import com.projet.service.criteria.FichePresenceCriteria;
import java.time.LocalDate;
import java.time.ZoneId;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link FichePresenceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FichePresenceResourceIT {

    private static final String DEFAULT_ACTIVITES = "AAAAAAAAAA";
    private static final String UPDATED_ACTIVITES = "BBBBBBBBBB";

    private static final String DEFAULT_HEUREDEBUT = "AAAAAAAAAA";
    private static final String UPDATED_HEUREDEBUT = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENTAIRE = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE = "BBBBBBBBBB";

    private static final String DEFAULT_HEUREFIN = "AAAAAAAAAA";
    private static final String UPDATED_HEUREFIN = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/fiche-presences";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FichePresenceRepository fichePresenceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFichePresenceMockMvc;

    private FichePresence fichePresence;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FichePresence createEntity(EntityManager em) {
        FichePresence fichePresence = new FichePresence()
            .activites(DEFAULT_ACTIVITES)
            .heuredebut(DEFAULT_HEUREDEBUT)
            .commentaire(DEFAULT_COMMENTAIRE)
            .heurefin(DEFAULT_HEUREFIN)
            .date(DEFAULT_DATE);
        return fichePresence;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FichePresence createUpdatedEntity(EntityManager em) {
        FichePresence fichePresence = new FichePresence()
            .activites(UPDATED_ACTIVITES)
            .heuredebut(UPDATED_HEUREDEBUT)
            .commentaire(UPDATED_COMMENTAIRE)
            .heurefin(UPDATED_HEUREFIN)
            .date(UPDATED_DATE);
        return fichePresence;
    }

    @BeforeEach
    public void initTest() {
        fichePresence = createEntity(em);
    }

    @Test
    @Transactional
    void createFichePresence() throws Exception {
        int databaseSizeBeforeCreate = fichePresenceRepository.findAll().size();
        // Create the FichePresence
        restFichePresenceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fichePresence)))
            .andExpect(status().isCreated());

        // Validate the FichePresence in the database
        List<FichePresence> fichePresenceList = fichePresenceRepository.findAll();
        assertThat(fichePresenceList).hasSize(databaseSizeBeforeCreate + 1);
        FichePresence testFichePresence = fichePresenceList.get(fichePresenceList.size() - 1);
        assertThat(testFichePresence.getActivites()).isEqualTo(DEFAULT_ACTIVITES);
        assertThat(testFichePresence.getHeuredebut()).isEqualTo(DEFAULT_HEUREDEBUT);
        assertThat(testFichePresence.getCommentaire()).isEqualTo(DEFAULT_COMMENTAIRE);
        assertThat(testFichePresence.getHeurefin()).isEqualTo(DEFAULT_HEUREFIN);
        assertThat(testFichePresence.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void createFichePresenceWithExistingId() throws Exception {
        // Create the FichePresence with an existing ID
        fichePresence.setId(1L);

        int databaseSizeBeforeCreate = fichePresenceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFichePresenceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fichePresence)))
            .andExpect(status().isBadRequest());

        // Validate the FichePresence in the database
        List<FichePresence> fichePresenceList = fichePresenceRepository.findAll();
        assertThat(fichePresenceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFichePresences() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList
        restFichePresenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fichePresence.getId().intValue())))
            .andExpect(jsonPath("$.[*].activites").value(hasItem(DEFAULT_ACTIVITES)))
            .andExpect(jsonPath("$.[*].heuredebut").value(hasItem(DEFAULT_HEUREDEBUT)))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE.toString())))
            .andExpect(jsonPath("$.[*].heurefin").value(hasItem(DEFAULT_HEUREFIN)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    void getFichePresence() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get the fichePresence
        restFichePresenceMockMvc
            .perform(get(ENTITY_API_URL_ID, fichePresence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fichePresence.getId().intValue()))
            .andExpect(jsonPath("$.activites").value(DEFAULT_ACTIVITES))
            .andExpect(jsonPath("$.heuredebut").value(DEFAULT_HEUREDEBUT))
            .andExpect(jsonPath("$.commentaire").value(DEFAULT_COMMENTAIRE.toString()))
            .andExpect(jsonPath("$.heurefin").value(DEFAULT_HEUREFIN))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getFichePresencesByIdFiltering() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        Long id = fichePresence.getId();

        defaultFichePresenceShouldBeFound("id.equals=" + id);
        defaultFichePresenceShouldNotBeFound("id.notEquals=" + id);

        defaultFichePresenceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFichePresenceShouldNotBeFound("id.greaterThan=" + id);

        defaultFichePresenceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFichePresenceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFichePresencesByActivitesIsEqualToSomething() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where activites equals to DEFAULT_ACTIVITES
        defaultFichePresenceShouldBeFound("activites.equals=" + DEFAULT_ACTIVITES);

        // Get all the fichePresenceList where activites equals to UPDATED_ACTIVITES
        defaultFichePresenceShouldNotBeFound("activites.equals=" + UPDATED_ACTIVITES);
    }

    @Test
    @Transactional
    void getAllFichePresencesByActivitesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where activites not equals to DEFAULT_ACTIVITES
        defaultFichePresenceShouldNotBeFound("activites.notEquals=" + DEFAULT_ACTIVITES);

        // Get all the fichePresenceList where activites not equals to UPDATED_ACTIVITES
        defaultFichePresenceShouldBeFound("activites.notEquals=" + UPDATED_ACTIVITES);
    }

    @Test
    @Transactional
    void getAllFichePresencesByActivitesIsInShouldWork() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where activites in DEFAULT_ACTIVITES or UPDATED_ACTIVITES
        defaultFichePresenceShouldBeFound("activites.in=" + DEFAULT_ACTIVITES + "," + UPDATED_ACTIVITES);

        // Get all the fichePresenceList where activites equals to UPDATED_ACTIVITES
        defaultFichePresenceShouldNotBeFound("activites.in=" + UPDATED_ACTIVITES);
    }

    @Test
    @Transactional
    void getAllFichePresencesByActivitesIsNullOrNotNull() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where activites is not null
        defaultFichePresenceShouldBeFound("activites.specified=true");

        // Get all the fichePresenceList where activites is null
        defaultFichePresenceShouldNotBeFound("activites.specified=false");
    }

    @Test
    @Transactional
    void getAllFichePresencesByActivitesContainsSomething() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where activites contains DEFAULT_ACTIVITES
        defaultFichePresenceShouldBeFound("activites.contains=" + DEFAULT_ACTIVITES);

        // Get all the fichePresenceList where activites contains UPDATED_ACTIVITES
        defaultFichePresenceShouldNotBeFound("activites.contains=" + UPDATED_ACTIVITES);
    }

    @Test
    @Transactional
    void getAllFichePresencesByActivitesNotContainsSomething() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where activites does not contain DEFAULT_ACTIVITES
        defaultFichePresenceShouldNotBeFound("activites.doesNotContain=" + DEFAULT_ACTIVITES);

        // Get all the fichePresenceList where activites does not contain UPDATED_ACTIVITES
        defaultFichePresenceShouldBeFound("activites.doesNotContain=" + UPDATED_ACTIVITES);
    }

    @Test
    @Transactional
    void getAllFichePresencesByHeuredebutIsEqualToSomething() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where heuredebut equals to DEFAULT_HEUREDEBUT
        defaultFichePresenceShouldBeFound("heuredebut.equals=" + DEFAULT_HEUREDEBUT);

        // Get all the fichePresenceList where heuredebut equals to UPDATED_HEUREDEBUT
        defaultFichePresenceShouldNotBeFound("heuredebut.equals=" + UPDATED_HEUREDEBUT);
    }

    @Test
    @Transactional
    void getAllFichePresencesByHeuredebutIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where heuredebut not equals to DEFAULT_HEUREDEBUT
        defaultFichePresenceShouldNotBeFound("heuredebut.notEquals=" + DEFAULT_HEUREDEBUT);

        // Get all the fichePresenceList where heuredebut not equals to UPDATED_HEUREDEBUT
        defaultFichePresenceShouldBeFound("heuredebut.notEquals=" + UPDATED_HEUREDEBUT);
    }

    @Test
    @Transactional
    void getAllFichePresencesByHeuredebutIsInShouldWork() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where heuredebut in DEFAULT_HEUREDEBUT or UPDATED_HEUREDEBUT
        defaultFichePresenceShouldBeFound("heuredebut.in=" + DEFAULT_HEUREDEBUT + "," + UPDATED_HEUREDEBUT);

        // Get all the fichePresenceList where heuredebut equals to UPDATED_HEUREDEBUT
        defaultFichePresenceShouldNotBeFound("heuredebut.in=" + UPDATED_HEUREDEBUT);
    }

    @Test
    @Transactional
    void getAllFichePresencesByHeuredebutIsNullOrNotNull() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where heuredebut is not null
        defaultFichePresenceShouldBeFound("heuredebut.specified=true");

        // Get all the fichePresenceList where heuredebut is null
        defaultFichePresenceShouldNotBeFound("heuredebut.specified=false");
    }

    @Test
    @Transactional
    void getAllFichePresencesByHeuredebutContainsSomething() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where heuredebut contains DEFAULT_HEUREDEBUT
        defaultFichePresenceShouldBeFound("heuredebut.contains=" + DEFAULT_HEUREDEBUT);

        // Get all the fichePresenceList where heuredebut contains UPDATED_HEUREDEBUT
        defaultFichePresenceShouldNotBeFound("heuredebut.contains=" + UPDATED_HEUREDEBUT);
    }

    @Test
    @Transactional
    void getAllFichePresencesByHeuredebutNotContainsSomething() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where heuredebut does not contain DEFAULT_HEUREDEBUT
        defaultFichePresenceShouldNotBeFound("heuredebut.doesNotContain=" + DEFAULT_HEUREDEBUT);

        // Get all the fichePresenceList where heuredebut does not contain UPDATED_HEUREDEBUT
        defaultFichePresenceShouldBeFound("heuredebut.doesNotContain=" + UPDATED_HEUREDEBUT);
    }

    @Test
    @Transactional
    void getAllFichePresencesByHeurefinIsEqualToSomething() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where heurefin equals to DEFAULT_HEUREFIN
        defaultFichePresenceShouldBeFound("heurefin.equals=" + DEFAULT_HEUREFIN);

        // Get all the fichePresenceList where heurefin equals to UPDATED_HEUREFIN
        defaultFichePresenceShouldNotBeFound("heurefin.equals=" + UPDATED_HEUREFIN);
    }

    @Test
    @Transactional
    void getAllFichePresencesByHeurefinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where heurefin not equals to DEFAULT_HEUREFIN
        defaultFichePresenceShouldNotBeFound("heurefin.notEquals=" + DEFAULT_HEUREFIN);

        // Get all the fichePresenceList where heurefin not equals to UPDATED_HEUREFIN
        defaultFichePresenceShouldBeFound("heurefin.notEquals=" + UPDATED_HEUREFIN);
    }

    @Test
    @Transactional
    void getAllFichePresencesByHeurefinIsInShouldWork() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where heurefin in DEFAULT_HEUREFIN or UPDATED_HEUREFIN
        defaultFichePresenceShouldBeFound("heurefin.in=" + DEFAULT_HEUREFIN + "," + UPDATED_HEUREFIN);

        // Get all the fichePresenceList where heurefin equals to UPDATED_HEUREFIN
        defaultFichePresenceShouldNotBeFound("heurefin.in=" + UPDATED_HEUREFIN);
    }

    @Test
    @Transactional
    void getAllFichePresencesByHeurefinIsNullOrNotNull() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where heurefin is not null
        defaultFichePresenceShouldBeFound("heurefin.specified=true");

        // Get all the fichePresenceList where heurefin is null
        defaultFichePresenceShouldNotBeFound("heurefin.specified=false");
    }

    @Test
    @Transactional
    void getAllFichePresencesByHeurefinContainsSomething() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where heurefin contains DEFAULT_HEUREFIN
        defaultFichePresenceShouldBeFound("heurefin.contains=" + DEFAULT_HEUREFIN);

        // Get all the fichePresenceList where heurefin contains UPDATED_HEUREFIN
        defaultFichePresenceShouldNotBeFound("heurefin.contains=" + UPDATED_HEUREFIN);
    }

    @Test
    @Transactional
    void getAllFichePresencesByHeurefinNotContainsSomething() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where heurefin does not contain DEFAULT_HEUREFIN
        defaultFichePresenceShouldNotBeFound("heurefin.doesNotContain=" + DEFAULT_HEUREFIN);

        // Get all the fichePresenceList where heurefin does not contain UPDATED_HEUREFIN
        defaultFichePresenceShouldBeFound("heurefin.doesNotContain=" + UPDATED_HEUREFIN);
    }

    @Test
    @Transactional
    void getAllFichePresencesByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where date equals to DEFAULT_DATE
        defaultFichePresenceShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the fichePresenceList where date equals to UPDATED_DATE
        defaultFichePresenceShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllFichePresencesByDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where date not equals to DEFAULT_DATE
        defaultFichePresenceShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);

        // Get all the fichePresenceList where date not equals to UPDATED_DATE
        defaultFichePresenceShouldBeFound("date.notEquals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllFichePresencesByDateIsInShouldWork() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where date in DEFAULT_DATE or UPDATED_DATE
        defaultFichePresenceShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the fichePresenceList where date equals to UPDATED_DATE
        defaultFichePresenceShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllFichePresencesByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where date is not null
        defaultFichePresenceShouldBeFound("date.specified=true");

        // Get all the fichePresenceList where date is null
        defaultFichePresenceShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    void getAllFichePresencesByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where date is greater than or equal to DEFAULT_DATE
        defaultFichePresenceShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the fichePresenceList where date is greater than or equal to UPDATED_DATE
        defaultFichePresenceShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllFichePresencesByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where date is less than or equal to DEFAULT_DATE
        defaultFichePresenceShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the fichePresenceList where date is less than or equal to SMALLER_DATE
        defaultFichePresenceShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllFichePresencesByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where date is less than DEFAULT_DATE
        defaultFichePresenceShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the fichePresenceList where date is less than UPDATED_DATE
        defaultFichePresenceShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllFichePresencesByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        // Get all the fichePresenceList where date is greater than DEFAULT_DATE
        defaultFichePresenceShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the fichePresenceList where date is greater than SMALLER_DATE
        defaultFichePresenceShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllFichePresencesByConsultantIsEqualToSomething() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);
        Consultant consultant = ConsultantResourceIT.createEntity(em);
        em.persist(consultant);
        em.flush();
        fichePresence.setConsultant(consultant);
        fichePresenceRepository.saveAndFlush(fichePresence);
        Long consultantId = consultant.getId();

        // Get all the fichePresenceList where consultant equals to consultantId
        defaultFichePresenceShouldBeFound("consultantId.equals=" + consultantId);

        // Get all the fichePresenceList where consultant equals to (consultantId + 1)
        defaultFichePresenceShouldNotBeFound("consultantId.equals=" + (consultantId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFichePresenceShouldBeFound(String filter) throws Exception {
        restFichePresenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fichePresence.getId().intValue())))
            .andExpect(jsonPath("$.[*].activites").value(hasItem(DEFAULT_ACTIVITES)))
            .andExpect(jsonPath("$.[*].heuredebut").value(hasItem(DEFAULT_HEUREDEBUT)))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE.toString())))
            .andExpect(jsonPath("$.[*].heurefin").value(hasItem(DEFAULT_HEUREFIN)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));

        // Check, that the count call also returns 1
        restFichePresenceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFichePresenceShouldNotBeFound(String filter) throws Exception {
        restFichePresenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFichePresenceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFichePresence() throws Exception {
        // Get the fichePresence
        restFichePresenceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFichePresence() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        int databaseSizeBeforeUpdate = fichePresenceRepository.findAll().size();

        // Update the fichePresence
        FichePresence updatedFichePresence = fichePresenceRepository.findById(fichePresence.getId()).get();
        // Disconnect from session so that the updates on updatedFichePresence are not directly saved in db
        em.detach(updatedFichePresence);
        updatedFichePresence
            .activites(UPDATED_ACTIVITES)
            .heuredebut(UPDATED_HEUREDEBUT)
            .commentaire(UPDATED_COMMENTAIRE)
            .heurefin(UPDATED_HEUREFIN)
            .date(UPDATED_DATE);

        restFichePresenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFichePresence.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFichePresence))
            )
            .andExpect(status().isOk());

        // Validate the FichePresence in the database
        List<FichePresence> fichePresenceList = fichePresenceRepository.findAll();
        assertThat(fichePresenceList).hasSize(databaseSizeBeforeUpdate);
        FichePresence testFichePresence = fichePresenceList.get(fichePresenceList.size() - 1);
        assertThat(testFichePresence.getActivites()).isEqualTo(UPDATED_ACTIVITES);
        assertThat(testFichePresence.getHeuredebut()).isEqualTo(UPDATED_HEUREDEBUT);
        assertThat(testFichePresence.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);
        assertThat(testFichePresence.getHeurefin()).isEqualTo(UPDATED_HEUREFIN);
        assertThat(testFichePresence.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingFichePresence() throws Exception {
        int databaseSizeBeforeUpdate = fichePresenceRepository.findAll().size();
        fichePresence.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFichePresenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fichePresence.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fichePresence))
            )
            .andExpect(status().isBadRequest());

        // Validate the FichePresence in the database
        List<FichePresence> fichePresenceList = fichePresenceRepository.findAll();
        assertThat(fichePresenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFichePresence() throws Exception {
        int databaseSizeBeforeUpdate = fichePresenceRepository.findAll().size();
        fichePresence.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFichePresenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fichePresence))
            )
            .andExpect(status().isBadRequest());

        // Validate the FichePresence in the database
        List<FichePresence> fichePresenceList = fichePresenceRepository.findAll();
        assertThat(fichePresenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFichePresence() throws Exception {
        int databaseSizeBeforeUpdate = fichePresenceRepository.findAll().size();
        fichePresence.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFichePresenceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fichePresence)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FichePresence in the database
        List<FichePresence> fichePresenceList = fichePresenceRepository.findAll();
        assertThat(fichePresenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFichePresenceWithPatch() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        int databaseSizeBeforeUpdate = fichePresenceRepository.findAll().size();

        // Update the fichePresence using partial update
        FichePresence partialUpdatedFichePresence = new FichePresence();
        partialUpdatedFichePresence.setId(fichePresence.getId());

        partialUpdatedFichePresence.heuredebut(UPDATED_HEUREDEBUT).commentaire(UPDATED_COMMENTAIRE);

        restFichePresenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFichePresence.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFichePresence))
            )
            .andExpect(status().isOk());

        // Validate the FichePresence in the database
        List<FichePresence> fichePresenceList = fichePresenceRepository.findAll();
        assertThat(fichePresenceList).hasSize(databaseSizeBeforeUpdate);
        FichePresence testFichePresence = fichePresenceList.get(fichePresenceList.size() - 1);
        assertThat(testFichePresence.getActivites()).isEqualTo(DEFAULT_ACTIVITES);
        assertThat(testFichePresence.getHeuredebut()).isEqualTo(UPDATED_HEUREDEBUT);
        assertThat(testFichePresence.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);
        assertThat(testFichePresence.getHeurefin()).isEqualTo(DEFAULT_HEUREFIN);
        assertThat(testFichePresence.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void fullUpdateFichePresenceWithPatch() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        int databaseSizeBeforeUpdate = fichePresenceRepository.findAll().size();

        // Update the fichePresence using partial update
        FichePresence partialUpdatedFichePresence = new FichePresence();
        partialUpdatedFichePresence.setId(fichePresence.getId());

        partialUpdatedFichePresence
            .activites(UPDATED_ACTIVITES)
            .heuredebut(UPDATED_HEUREDEBUT)
            .commentaire(UPDATED_COMMENTAIRE)
            .heurefin(UPDATED_HEUREFIN)
            .date(UPDATED_DATE);

        restFichePresenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFichePresence.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFichePresence))
            )
            .andExpect(status().isOk());

        // Validate the FichePresence in the database
        List<FichePresence> fichePresenceList = fichePresenceRepository.findAll();
        assertThat(fichePresenceList).hasSize(databaseSizeBeforeUpdate);
        FichePresence testFichePresence = fichePresenceList.get(fichePresenceList.size() - 1);
        assertThat(testFichePresence.getActivites()).isEqualTo(UPDATED_ACTIVITES);
        assertThat(testFichePresence.getHeuredebut()).isEqualTo(UPDATED_HEUREDEBUT);
        assertThat(testFichePresence.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);
        assertThat(testFichePresence.getHeurefin()).isEqualTo(UPDATED_HEUREFIN);
        assertThat(testFichePresence.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingFichePresence() throws Exception {
        int databaseSizeBeforeUpdate = fichePresenceRepository.findAll().size();
        fichePresence.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFichePresenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fichePresence.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fichePresence))
            )
            .andExpect(status().isBadRequest());

        // Validate the FichePresence in the database
        List<FichePresence> fichePresenceList = fichePresenceRepository.findAll();
        assertThat(fichePresenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFichePresence() throws Exception {
        int databaseSizeBeforeUpdate = fichePresenceRepository.findAll().size();
        fichePresence.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFichePresenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fichePresence))
            )
            .andExpect(status().isBadRequest());

        // Validate the FichePresence in the database
        List<FichePresence> fichePresenceList = fichePresenceRepository.findAll();
        assertThat(fichePresenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFichePresence() throws Exception {
        int databaseSizeBeforeUpdate = fichePresenceRepository.findAll().size();
        fichePresence.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFichePresenceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(fichePresence))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FichePresence in the database
        List<FichePresence> fichePresenceList = fichePresenceRepository.findAll();
        assertThat(fichePresenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFichePresence() throws Exception {
        // Initialize the database
        fichePresenceRepository.saveAndFlush(fichePresence);

        int databaseSizeBeforeDelete = fichePresenceRepository.findAll().size();

        // Delete the fichePresence
        restFichePresenceMockMvc
            .perform(delete(ENTITY_API_URL_ID, fichePresence.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FichePresence> fichePresenceList = fichePresenceRepository.findAll();
        assertThat(fichePresenceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
