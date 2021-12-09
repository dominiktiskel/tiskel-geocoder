package com.tiskel.geocoder.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tiskel.geocoder.IntegrationTest;
import com.tiskel.geocoder.domain.Source;
import com.tiskel.geocoder.repository.SourceRepository;
import com.tiskel.geocoder.service.dto.SourceDTO;
import com.tiskel.geocoder.service.mapper.SourceMapper;
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
 * Integration tests for the {@link SourceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SourceResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/sources";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SourceRepository sourceRepository;

    @Autowired
    private SourceMapper sourceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSourceMockMvc;

    private Source source;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Source createEntity(EntityManager em) {
        Source source = new Source().name(DEFAULT_NAME).active(DEFAULT_ACTIVE);
        return source;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Source createUpdatedEntity(EntityManager em) {
        Source source = new Source().name(UPDATED_NAME).active(UPDATED_ACTIVE);
        return source;
    }

    @BeforeEach
    public void initTest() {
        source = createEntity(em);
    }

    @Test
    @Transactional
    void createSource() throws Exception {
        int databaseSizeBeforeCreate = sourceRepository.findAll().size();
        // Create the Source
        SourceDTO sourceDTO = sourceMapper.toDto(source);
        restSourceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sourceDTO)))
            .andExpect(status().isCreated());

        // Validate the Source in the database
        List<Source> sourceList = sourceRepository.findAll();
        assertThat(sourceList).hasSize(databaseSizeBeforeCreate + 1);
        Source testSource = sourceList.get(sourceList.size() - 1);
        assertThat(testSource.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSource.getActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    void createSourceWithExistingId() throws Exception {
        // Create the Source with an existing ID
        source.setId(1L);
        SourceDTO sourceDTO = sourceMapper.toDto(source);

        int databaseSizeBeforeCreate = sourceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSourceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sourceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Source in the database
        List<Source> sourceList = sourceRepository.findAll();
        assertThat(sourceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = sourceRepository.findAll().size();
        // set the field null
        source.setName(null);

        // Create the Source, which fails.
        SourceDTO sourceDTO = sourceMapper.toDto(source);

        restSourceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sourceDTO)))
            .andExpect(status().isBadRequest());

        List<Source> sourceList = sourceRepository.findAll();
        assertThat(sourceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = sourceRepository.findAll().size();
        // set the field null
        source.setActive(null);

        // Create the Source, which fails.
        SourceDTO sourceDTO = sourceMapper.toDto(source);

        restSourceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sourceDTO)))
            .andExpect(status().isBadRequest());

        List<Source> sourceList = sourceRepository.findAll();
        assertThat(sourceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSources() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);

        // Get all the sourceList
        restSourceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(source.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getSource() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);

        // Get the source
        restSourceMockMvc
            .perform(get(ENTITY_API_URL_ID, source.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(source.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingSource() throws Exception {
        // Get the source
        restSourceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSource() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);

        int databaseSizeBeforeUpdate = sourceRepository.findAll().size();

        // Update the source
        Source updatedSource = sourceRepository.findById(source.getId()).get();
        // Disconnect from session so that the updates on updatedSource are not directly saved in db
        em.detach(updatedSource);
        updatedSource.name(UPDATED_NAME).active(UPDATED_ACTIVE);
        SourceDTO sourceDTO = sourceMapper.toDto(updatedSource);

        restSourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sourceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sourceDTO))
            )
            .andExpect(status().isOk());

        // Validate the Source in the database
        List<Source> sourceList = sourceRepository.findAll();
        assertThat(sourceList).hasSize(databaseSizeBeforeUpdate);
        Source testSource = sourceList.get(sourceList.size() - 1);
        assertThat(testSource.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSource.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingSource() throws Exception {
        int databaseSizeBeforeUpdate = sourceRepository.findAll().size();
        source.setId(count.incrementAndGet());

        // Create the Source
        SourceDTO sourceDTO = sourceMapper.toDto(source);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sourceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Source in the database
        List<Source> sourceList = sourceRepository.findAll();
        assertThat(sourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSource() throws Exception {
        int databaseSizeBeforeUpdate = sourceRepository.findAll().size();
        source.setId(count.incrementAndGet());

        // Create the Source
        SourceDTO sourceDTO = sourceMapper.toDto(source);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Source in the database
        List<Source> sourceList = sourceRepository.findAll();
        assertThat(sourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSource() throws Exception {
        int databaseSizeBeforeUpdate = sourceRepository.findAll().size();
        source.setId(count.incrementAndGet());

        // Create the Source
        SourceDTO sourceDTO = sourceMapper.toDto(source);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSourceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sourceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Source in the database
        List<Source> sourceList = sourceRepository.findAll();
        assertThat(sourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSourceWithPatch() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);

        int databaseSizeBeforeUpdate = sourceRepository.findAll().size();

        // Update the source using partial update
        Source partialUpdatedSource = new Source();
        partialUpdatedSource.setId(source.getId());

        partialUpdatedSource.active(UPDATED_ACTIVE);

        restSourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSource.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSource))
            )
            .andExpect(status().isOk());

        // Validate the Source in the database
        List<Source> sourceList = sourceRepository.findAll();
        assertThat(sourceList).hasSize(databaseSizeBeforeUpdate);
        Source testSource = sourceList.get(sourceList.size() - 1);
        assertThat(testSource.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSource.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateSourceWithPatch() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);

        int databaseSizeBeforeUpdate = sourceRepository.findAll().size();

        // Update the source using partial update
        Source partialUpdatedSource = new Source();
        partialUpdatedSource.setId(source.getId());

        partialUpdatedSource.name(UPDATED_NAME).active(UPDATED_ACTIVE);

        restSourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSource.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSource))
            )
            .andExpect(status().isOk());

        // Validate the Source in the database
        List<Source> sourceList = sourceRepository.findAll();
        assertThat(sourceList).hasSize(databaseSizeBeforeUpdate);
        Source testSource = sourceList.get(sourceList.size() - 1);
        assertThat(testSource.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSource.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingSource() throws Exception {
        int databaseSizeBeforeUpdate = sourceRepository.findAll().size();
        source.setId(count.incrementAndGet());

        // Create the Source
        SourceDTO sourceDTO = sourceMapper.toDto(source);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sourceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Source in the database
        List<Source> sourceList = sourceRepository.findAll();
        assertThat(sourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSource() throws Exception {
        int databaseSizeBeforeUpdate = sourceRepository.findAll().size();
        source.setId(count.incrementAndGet());

        // Create the Source
        SourceDTO sourceDTO = sourceMapper.toDto(source);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Source in the database
        List<Source> sourceList = sourceRepository.findAll();
        assertThat(sourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSource() throws Exception {
        int databaseSizeBeforeUpdate = sourceRepository.findAll().size();
        source.setId(count.incrementAndGet());

        // Create the Source
        SourceDTO sourceDTO = sourceMapper.toDto(source);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSourceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sourceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Source in the database
        List<Source> sourceList = sourceRepository.findAll();
        assertThat(sourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSource() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);

        int databaseSizeBeforeDelete = sourceRepository.findAll().size();

        // Delete the source
        restSourceMockMvc
            .perform(delete(ENTITY_API_URL_ID, source.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Source> sourceList = sourceRepository.findAll();
        assertThat(sourceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
