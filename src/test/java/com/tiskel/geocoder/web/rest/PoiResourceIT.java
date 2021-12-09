package com.tiskel.geocoder.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tiskel.geocoder.IntegrationTest;
import com.tiskel.geocoder.domain.Poi;
import com.tiskel.geocoder.domain.PoiGroup;
import com.tiskel.geocoder.domain.Source;
import com.tiskel.geocoder.repository.PoiRepository;
import com.tiskel.geocoder.service.dto.PoiDTO;
import com.tiskel.geocoder.service.mapper.PoiMapper;
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
 * Integration tests for the {@link PoiResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PoiResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_STREET = "AAAAAAAAAA";
    private static final String UPDATED_STREET = "BBBBBBBBBB";

    private static final String DEFAULT_BUILDING_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_BUILDING_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_POST_CODE = "AAAAAAAAAA";
    private static final String UPDATED_POST_CODE = "BBBBBBBBBB";

    private static final Double DEFAULT_LAT = 1D;
    private static final Double UPDATED_LAT = 2D;

    private static final Double DEFAULT_LNG = 1D;
    private static final Double UPDATED_LNG = 2D;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/pois";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PoiRepository poiRepository;

    @Autowired
    private PoiMapper poiMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPoiMockMvc;

    private Poi poi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Poi createEntity(EntityManager em) {
        Poi poi = new Poi()
            .name(DEFAULT_NAME)
            .street(DEFAULT_STREET)
            .buildingNumber(DEFAULT_BUILDING_NUMBER)
            .postCode(DEFAULT_POST_CODE)
            .lat(DEFAULT_LAT)
            .lng(DEFAULT_LNG)
            .active(DEFAULT_ACTIVE);
        // Add required entity
        Source source;
        if (TestUtil.findAll(em, Source.class).isEmpty()) {
            source = SourceResourceIT.createEntity(em);
            em.persist(source);
            em.flush();
        } else {
            source = TestUtil.findAll(em, Source.class).get(0);
        }
        poi.setSource(source);
        // Add required entity
        PoiGroup poiGroup;
        if (TestUtil.findAll(em, PoiGroup.class).isEmpty()) {
            poiGroup = PoiGroupResourceIT.createEntity(em);
            em.persist(poiGroup);
            em.flush();
        } else {
            poiGroup = TestUtil.findAll(em, PoiGroup.class).get(0);
        }
        poi.setPoiGroup(poiGroup);
        return poi;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Poi createUpdatedEntity(EntityManager em) {
        Poi poi = new Poi()
            .name(UPDATED_NAME)
            .street(UPDATED_STREET)
            .buildingNumber(UPDATED_BUILDING_NUMBER)
            .postCode(UPDATED_POST_CODE)
            .lat(UPDATED_LAT)
            .lng(UPDATED_LNG)
            .active(UPDATED_ACTIVE);
        // Add required entity
        Source source;
        if (TestUtil.findAll(em, Source.class).isEmpty()) {
            source = SourceResourceIT.createUpdatedEntity(em);
            em.persist(source);
            em.flush();
        } else {
            source = TestUtil.findAll(em, Source.class).get(0);
        }
        poi.setSource(source);
        // Add required entity
        PoiGroup poiGroup;
        if (TestUtil.findAll(em, PoiGroup.class).isEmpty()) {
            poiGroup = PoiGroupResourceIT.createUpdatedEntity(em);
            em.persist(poiGroup);
            em.flush();
        } else {
            poiGroup = TestUtil.findAll(em, PoiGroup.class).get(0);
        }
        poi.setPoiGroup(poiGroup);
        return poi;
    }

    @BeforeEach
    public void initTest() {
        poi = createEntity(em);
    }

    @Test
    @Transactional
    void createPoi() throws Exception {
        int databaseSizeBeforeCreate = poiRepository.findAll().size();
        // Create the Poi
        PoiDTO poiDTO = poiMapper.toDto(poi);
        restPoiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(poiDTO)))
            .andExpect(status().isCreated());

        // Validate the Poi in the database
        List<Poi> poiList = poiRepository.findAll();
        assertThat(poiList).hasSize(databaseSizeBeforeCreate + 1);
        Poi testPoi = poiList.get(poiList.size() - 1);
        assertThat(testPoi.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPoi.getStreet()).isEqualTo(DEFAULT_STREET);
        assertThat(testPoi.getBuildingNumber()).isEqualTo(DEFAULT_BUILDING_NUMBER);
        assertThat(testPoi.getPostCode()).isEqualTo(DEFAULT_POST_CODE);
        assertThat(testPoi.getLat()).isEqualTo(DEFAULT_LAT);
        assertThat(testPoi.getLng()).isEqualTo(DEFAULT_LNG);
        assertThat(testPoi.getActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    void createPoiWithExistingId() throws Exception {
        // Create the Poi with an existing ID
        poi.setId(1L);
        PoiDTO poiDTO = poiMapper.toDto(poi);

        int databaseSizeBeforeCreate = poiRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPoiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(poiDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Poi in the database
        List<Poi> poiList = poiRepository.findAll();
        assertThat(poiList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = poiRepository.findAll().size();
        // set the field null
        poi.setName(null);

        // Create the Poi, which fails.
        PoiDTO poiDTO = poiMapper.toDto(poi);

        restPoiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(poiDTO)))
            .andExpect(status().isBadRequest());

        List<Poi> poiList = poiRepository.findAll();
        assertThat(poiList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLatIsRequired() throws Exception {
        int databaseSizeBeforeTest = poiRepository.findAll().size();
        // set the field null
        poi.setLat(null);

        // Create the Poi, which fails.
        PoiDTO poiDTO = poiMapper.toDto(poi);

        restPoiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(poiDTO)))
            .andExpect(status().isBadRequest());

        List<Poi> poiList = poiRepository.findAll();
        assertThat(poiList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLngIsRequired() throws Exception {
        int databaseSizeBeforeTest = poiRepository.findAll().size();
        // set the field null
        poi.setLng(null);

        // Create the Poi, which fails.
        PoiDTO poiDTO = poiMapper.toDto(poi);

        restPoiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(poiDTO)))
            .andExpect(status().isBadRequest());

        List<Poi> poiList = poiRepository.findAll();
        assertThat(poiList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = poiRepository.findAll().size();
        // set the field null
        poi.setActive(null);

        // Create the Poi, which fails.
        PoiDTO poiDTO = poiMapper.toDto(poi);

        restPoiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(poiDTO)))
            .andExpect(status().isBadRequest());

        List<Poi> poiList = poiRepository.findAll();
        assertThat(poiList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPois() throws Exception {
        // Initialize the database
        poiRepository.saveAndFlush(poi);

        // Get all the poiList
        restPoiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(poi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET)))
            .andExpect(jsonPath("$.[*].buildingNumber").value(hasItem(DEFAULT_BUILDING_NUMBER)))
            .andExpect(jsonPath("$.[*].postCode").value(hasItem(DEFAULT_POST_CODE)))
            .andExpect(jsonPath("$.[*].lat").value(hasItem(DEFAULT_LAT.doubleValue())))
            .andExpect(jsonPath("$.[*].lng").value(hasItem(DEFAULT_LNG.doubleValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getPoi() throws Exception {
        // Initialize the database
        poiRepository.saveAndFlush(poi);

        // Get the poi
        restPoiMockMvc
            .perform(get(ENTITY_API_URL_ID, poi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(poi.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.street").value(DEFAULT_STREET))
            .andExpect(jsonPath("$.buildingNumber").value(DEFAULT_BUILDING_NUMBER))
            .andExpect(jsonPath("$.postCode").value(DEFAULT_POST_CODE))
            .andExpect(jsonPath("$.lat").value(DEFAULT_LAT.doubleValue()))
            .andExpect(jsonPath("$.lng").value(DEFAULT_LNG.doubleValue()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingPoi() throws Exception {
        // Get the poi
        restPoiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPoi() throws Exception {
        // Initialize the database
        poiRepository.saveAndFlush(poi);

        int databaseSizeBeforeUpdate = poiRepository.findAll().size();

        // Update the poi
        Poi updatedPoi = poiRepository.findById(poi.getId()).get();
        // Disconnect from session so that the updates on updatedPoi are not directly saved in db
        em.detach(updatedPoi);
        updatedPoi
            .name(UPDATED_NAME)
            .street(UPDATED_STREET)
            .buildingNumber(UPDATED_BUILDING_NUMBER)
            .postCode(UPDATED_POST_CODE)
            .lat(UPDATED_LAT)
            .lng(UPDATED_LNG)
            .active(UPDATED_ACTIVE);
        PoiDTO poiDTO = poiMapper.toDto(updatedPoi);

        restPoiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, poiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(poiDTO))
            )
            .andExpect(status().isOk());

        // Validate the Poi in the database
        List<Poi> poiList = poiRepository.findAll();
        assertThat(poiList).hasSize(databaseSizeBeforeUpdate);
        Poi testPoi = poiList.get(poiList.size() - 1);
        assertThat(testPoi.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPoi.getStreet()).isEqualTo(UPDATED_STREET);
        assertThat(testPoi.getBuildingNumber()).isEqualTo(UPDATED_BUILDING_NUMBER);
        assertThat(testPoi.getPostCode()).isEqualTo(UPDATED_POST_CODE);
        assertThat(testPoi.getLat()).isEqualTo(UPDATED_LAT);
        assertThat(testPoi.getLng()).isEqualTo(UPDATED_LNG);
        assertThat(testPoi.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingPoi() throws Exception {
        int databaseSizeBeforeUpdate = poiRepository.findAll().size();
        poi.setId(count.incrementAndGet());

        // Create the Poi
        PoiDTO poiDTO = poiMapper.toDto(poi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPoiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, poiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(poiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Poi in the database
        List<Poi> poiList = poiRepository.findAll();
        assertThat(poiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPoi() throws Exception {
        int databaseSizeBeforeUpdate = poiRepository.findAll().size();
        poi.setId(count.incrementAndGet());

        // Create the Poi
        PoiDTO poiDTO = poiMapper.toDto(poi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPoiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(poiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Poi in the database
        List<Poi> poiList = poiRepository.findAll();
        assertThat(poiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPoi() throws Exception {
        int databaseSizeBeforeUpdate = poiRepository.findAll().size();
        poi.setId(count.incrementAndGet());

        // Create the Poi
        PoiDTO poiDTO = poiMapper.toDto(poi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPoiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(poiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Poi in the database
        List<Poi> poiList = poiRepository.findAll();
        assertThat(poiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePoiWithPatch() throws Exception {
        // Initialize the database
        poiRepository.saveAndFlush(poi);

        int databaseSizeBeforeUpdate = poiRepository.findAll().size();

        // Update the poi using partial update
        Poi partialUpdatedPoi = new Poi();
        partialUpdatedPoi.setId(poi.getId());

        partialUpdatedPoi.postCode(UPDATED_POST_CODE).lng(UPDATED_LNG).active(UPDATED_ACTIVE);

        restPoiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPoi.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPoi))
            )
            .andExpect(status().isOk());

        // Validate the Poi in the database
        List<Poi> poiList = poiRepository.findAll();
        assertThat(poiList).hasSize(databaseSizeBeforeUpdate);
        Poi testPoi = poiList.get(poiList.size() - 1);
        assertThat(testPoi.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPoi.getStreet()).isEqualTo(DEFAULT_STREET);
        assertThat(testPoi.getBuildingNumber()).isEqualTo(DEFAULT_BUILDING_NUMBER);
        assertThat(testPoi.getPostCode()).isEqualTo(UPDATED_POST_CODE);
        assertThat(testPoi.getLat()).isEqualTo(DEFAULT_LAT);
        assertThat(testPoi.getLng()).isEqualTo(UPDATED_LNG);
        assertThat(testPoi.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdatePoiWithPatch() throws Exception {
        // Initialize the database
        poiRepository.saveAndFlush(poi);

        int databaseSizeBeforeUpdate = poiRepository.findAll().size();

        // Update the poi using partial update
        Poi partialUpdatedPoi = new Poi();
        partialUpdatedPoi.setId(poi.getId());

        partialUpdatedPoi
            .name(UPDATED_NAME)
            .street(UPDATED_STREET)
            .buildingNumber(UPDATED_BUILDING_NUMBER)
            .postCode(UPDATED_POST_CODE)
            .lat(UPDATED_LAT)
            .lng(UPDATED_LNG)
            .active(UPDATED_ACTIVE);

        restPoiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPoi.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPoi))
            )
            .andExpect(status().isOk());

        // Validate the Poi in the database
        List<Poi> poiList = poiRepository.findAll();
        assertThat(poiList).hasSize(databaseSizeBeforeUpdate);
        Poi testPoi = poiList.get(poiList.size() - 1);
        assertThat(testPoi.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPoi.getStreet()).isEqualTo(UPDATED_STREET);
        assertThat(testPoi.getBuildingNumber()).isEqualTo(UPDATED_BUILDING_NUMBER);
        assertThat(testPoi.getPostCode()).isEqualTo(UPDATED_POST_CODE);
        assertThat(testPoi.getLat()).isEqualTo(UPDATED_LAT);
        assertThat(testPoi.getLng()).isEqualTo(UPDATED_LNG);
        assertThat(testPoi.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingPoi() throws Exception {
        int databaseSizeBeforeUpdate = poiRepository.findAll().size();
        poi.setId(count.incrementAndGet());

        // Create the Poi
        PoiDTO poiDTO = poiMapper.toDto(poi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPoiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, poiDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(poiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Poi in the database
        List<Poi> poiList = poiRepository.findAll();
        assertThat(poiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPoi() throws Exception {
        int databaseSizeBeforeUpdate = poiRepository.findAll().size();
        poi.setId(count.incrementAndGet());

        // Create the Poi
        PoiDTO poiDTO = poiMapper.toDto(poi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPoiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(poiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Poi in the database
        List<Poi> poiList = poiRepository.findAll();
        assertThat(poiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPoi() throws Exception {
        int databaseSizeBeforeUpdate = poiRepository.findAll().size();
        poi.setId(count.incrementAndGet());

        // Create the Poi
        PoiDTO poiDTO = poiMapper.toDto(poi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPoiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(poiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Poi in the database
        List<Poi> poiList = poiRepository.findAll();
        assertThat(poiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePoi() throws Exception {
        // Initialize the database
        poiRepository.saveAndFlush(poi);

        int databaseSizeBeforeDelete = poiRepository.findAll().size();

        // Delete the poi
        restPoiMockMvc.perform(delete(ENTITY_API_URL_ID, poi.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Poi> poiList = poiRepository.findAll();
        assertThat(poiList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
