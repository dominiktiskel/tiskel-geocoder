package com.tiskel.geocoder.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tiskel.geocoder.IntegrationTest;
import com.tiskel.geocoder.domain.PoiGroup;
import com.tiskel.geocoder.repository.PoiGroupRepository;
import com.tiskel.geocoder.service.dto.PoiGroupDTO;
import com.tiskel.geocoder.service.mapper.PoiGroupMapper;
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
 * Integration tests for the {@link PoiGroupResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PoiGroupResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PREFIX = "AAAAAAAAAA";
    private static final String UPDATED_PREFIX = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/poi-groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PoiGroupRepository poiGroupRepository;

    @Autowired
    private PoiGroupMapper poiGroupMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPoiGroupMockMvc;

    private PoiGroup poiGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PoiGroup createEntity(EntityManager em) {
        PoiGroup poiGroup = new PoiGroup().name(DEFAULT_NAME).prefix(DEFAULT_PREFIX).active(DEFAULT_ACTIVE);
        return poiGroup;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PoiGroup createUpdatedEntity(EntityManager em) {
        PoiGroup poiGroup = new PoiGroup().name(UPDATED_NAME).prefix(UPDATED_PREFIX).active(UPDATED_ACTIVE);
        return poiGroup;
    }

    @BeforeEach
    public void initTest() {
        poiGroup = createEntity(em);
    }

    @Test
    @Transactional
    void createPoiGroup() throws Exception {
        int databaseSizeBeforeCreate = poiGroupRepository.findAll().size();
        // Create the PoiGroup
        PoiGroupDTO poiGroupDTO = poiGroupMapper.toDto(poiGroup);
        restPoiGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(poiGroupDTO)))
            .andExpect(status().isCreated());

        // Validate the PoiGroup in the database
        List<PoiGroup> poiGroupList = poiGroupRepository.findAll();
        assertThat(poiGroupList).hasSize(databaseSizeBeforeCreate + 1);
        PoiGroup testPoiGroup = poiGroupList.get(poiGroupList.size() - 1);
        assertThat(testPoiGroup.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPoiGroup.getPrefix()).isEqualTo(DEFAULT_PREFIX);
        assertThat(testPoiGroup.getActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    void createPoiGroupWithExistingId() throws Exception {
        // Create the PoiGroup with an existing ID
        poiGroup.setId(1L);
        PoiGroupDTO poiGroupDTO = poiGroupMapper.toDto(poiGroup);

        int databaseSizeBeforeCreate = poiGroupRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPoiGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(poiGroupDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PoiGroup in the database
        List<PoiGroup> poiGroupList = poiGroupRepository.findAll();
        assertThat(poiGroupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = poiGroupRepository.findAll().size();
        // set the field null
        poiGroup.setName(null);

        // Create the PoiGroup, which fails.
        PoiGroupDTO poiGroupDTO = poiGroupMapper.toDto(poiGroup);

        restPoiGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(poiGroupDTO)))
            .andExpect(status().isBadRequest());

        List<PoiGroup> poiGroupList = poiGroupRepository.findAll();
        assertThat(poiGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrefixIsRequired() throws Exception {
        int databaseSizeBeforeTest = poiGroupRepository.findAll().size();
        // set the field null
        poiGroup.setPrefix(null);

        // Create the PoiGroup, which fails.
        PoiGroupDTO poiGroupDTO = poiGroupMapper.toDto(poiGroup);

        restPoiGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(poiGroupDTO)))
            .andExpect(status().isBadRequest());

        List<PoiGroup> poiGroupList = poiGroupRepository.findAll();
        assertThat(poiGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = poiGroupRepository.findAll().size();
        // set the field null
        poiGroup.setActive(null);

        // Create the PoiGroup, which fails.
        PoiGroupDTO poiGroupDTO = poiGroupMapper.toDto(poiGroup);

        restPoiGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(poiGroupDTO)))
            .andExpect(status().isBadRequest());

        List<PoiGroup> poiGroupList = poiGroupRepository.findAll();
        assertThat(poiGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPoiGroups() throws Exception {
        // Initialize the database
        poiGroupRepository.saveAndFlush(poiGroup);

        // Get all the poiGroupList
        restPoiGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(poiGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].prefix").value(hasItem(DEFAULT_PREFIX)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getPoiGroup() throws Exception {
        // Initialize the database
        poiGroupRepository.saveAndFlush(poiGroup);

        // Get the poiGroup
        restPoiGroupMockMvc
            .perform(get(ENTITY_API_URL_ID, poiGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(poiGroup.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.prefix").value(DEFAULT_PREFIX))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingPoiGroup() throws Exception {
        // Get the poiGroup
        restPoiGroupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPoiGroup() throws Exception {
        // Initialize the database
        poiGroupRepository.saveAndFlush(poiGroup);

        int databaseSizeBeforeUpdate = poiGroupRepository.findAll().size();

        // Update the poiGroup
        PoiGroup updatedPoiGroup = poiGroupRepository.findById(poiGroup.getId()).get();
        // Disconnect from session so that the updates on updatedPoiGroup are not directly saved in db
        em.detach(updatedPoiGroup);
        updatedPoiGroup.name(UPDATED_NAME).prefix(UPDATED_PREFIX).active(UPDATED_ACTIVE);
        PoiGroupDTO poiGroupDTO = poiGroupMapper.toDto(updatedPoiGroup);

        restPoiGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, poiGroupDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(poiGroupDTO))
            )
            .andExpect(status().isOk());

        // Validate the PoiGroup in the database
        List<PoiGroup> poiGroupList = poiGroupRepository.findAll();
        assertThat(poiGroupList).hasSize(databaseSizeBeforeUpdate);
        PoiGroup testPoiGroup = poiGroupList.get(poiGroupList.size() - 1);
        assertThat(testPoiGroup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPoiGroup.getPrefix()).isEqualTo(UPDATED_PREFIX);
        assertThat(testPoiGroup.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingPoiGroup() throws Exception {
        int databaseSizeBeforeUpdate = poiGroupRepository.findAll().size();
        poiGroup.setId(count.incrementAndGet());

        // Create the PoiGroup
        PoiGroupDTO poiGroupDTO = poiGroupMapper.toDto(poiGroup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPoiGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, poiGroupDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(poiGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PoiGroup in the database
        List<PoiGroup> poiGroupList = poiGroupRepository.findAll();
        assertThat(poiGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPoiGroup() throws Exception {
        int databaseSizeBeforeUpdate = poiGroupRepository.findAll().size();
        poiGroup.setId(count.incrementAndGet());

        // Create the PoiGroup
        PoiGroupDTO poiGroupDTO = poiGroupMapper.toDto(poiGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPoiGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(poiGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PoiGroup in the database
        List<PoiGroup> poiGroupList = poiGroupRepository.findAll();
        assertThat(poiGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPoiGroup() throws Exception {
        int databaseSizeBeforeUpdate = poiGroupRepository.findAll().size();
        poiGroup.setId(count.incrementAndGet());

        // Create the PoiGroup
        PoiGroupDTO poiGroupDTO = poiGroupMapper.toDto(poiGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPoiGroupMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(poiGroupDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PoiGroup in the database
        List<PoiGroup> poiGroupList = poiGroupRepository.findAll();
        assertThat(poiGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePoiGroupWithPatch() throws Exception {
        // Initialize the database
        poiGroupRepository.saveAndFlush(poiGroup);

        int databaseSizeBeforeUpdate = poiGroupRepository.findAll().size();

        // Update the poiGroup using partial update
        PoiGroup partialUpdatedPoiGroup = new PoiGroup();
        partialUpdatedPoiGroup.setId(poiGroup.getId());

        partialUpdatedPoiGroup.prefix(UPDATED_PREFIX).active(UPDATED_ACTIVE);

        restPoiGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPoiGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPoiGroup))
            )
            .andExpect(status().isOk());

        // Validate the PoiGroup in the database
        List<PoiGroup> poiGroupList = poiGroupRepository.findAll();
        assertThat(poiGroupList).hasSize(databaseSizeBeforeUpdate);
        PoiGroup testPoiGroup = poiGroupList.get(poiGroupList.size() - 1);
        assertThat(testPoiGroup.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPoiGroup.getPrefix()).isEqualTo(UPDATED_PREFIX);
        assertThat(testPoiGroup.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdatePoiGroupWithPatch() throws Exception {
        // Initialize the database
        poiGroupRepository.saveAndFlush(poiGroup);

        int databaseSizeBeforeUpdate = poiGroupRepository.findAll().size();

        // Update the poiGroup using partial update
        PoiGroup partialUpdatedPoiGroup = new PoiGroup();
        partialUpdatedPoiGroup.setId(poiGroup.getId());

        partialUpdatedPoiGroup.name(UPDATED_NAME).prefix(UPDATED_PREFIX).active(UPDATED_ACTIVE);

        restPoiGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPoiGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPoiGroup))
            )
            .andExpect(status().isOk());

        // Validate the PoiGroup in the database
        List<PoiGroup> poiGroupList = poiGroupRepository.findAll();
        assertThat(poiGroupList).hasSize(databaseSizeBeforeUpdate);
        PoiGroup testPoiGroup = poiGroupList.get(poiGroupList.size() - 1);
        assertThat(testPoiGroup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPoiGroup.getPrefix()).isEqualTo(UPDATED_PREFIX);
        assertThat(testPoiGroup.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingPoiGroup() throws Exception {
        int databaseSizeBeforeUpdate = poiGroupRepository.findAll().size();
        poiGroup.setId(count.incrementAndGet());

        // Create the PoiGroup
        PoiGroupDTO poiGroupDTO = poiGroupMapper.toDto(poiGroup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPoiGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, poiGroupDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(poiGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PoiGroup in the database
        List<PoiGroup> poiGroupList = poiGroupRepository.findAll();
        assertThat(poiGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPoiGroup() throws Exception {
        int databaseSizeBeforeUpdate = poiGroupRepository.findAll().size();
        poiGroup.setId(count.incrementAndGet());

        // Create the PoiGroup
        PoiGroupDTO poiGroupDTO = poiGroupMapper.toDto(poiGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPoiGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(poiGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PoiGroup in the database
        List<PoiGroup> poiGroupList = poiGroupRepository.findAll();
        assertThat(poiGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPoiGroup() throws Exception {
        int databaseSizeBeforeUpdate = poiGroupRepository.findAll().size();
        poiGroup.setId(count.incrementAndGet());

        // Create the PoiGroup
        PoiGroupDTO poiGroupDTO = poiGroupMapper.toDto(poiGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPoiGroupMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(poiGroupDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PoiGroup in the database
        List<PoiGroup> poiGroupList = poiGroupRepository.findAll();
        assertThat(poiGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePoiGroup() throws Exception {
        // Initialize the database
        poiGroupRepository.saveAndFlush(poiGroup);

        int databaseSizeBeforeDelete = poiGroupRepository.findAll().size();

        // Delete the poiGroup
        restPoiGroupMockMvc
            .perform(delete(ENTITY_API_URL_ID, poiGroup.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PoiGroup> poiGroupList = poiGroupRepository.findAll();
        assertThat(poiGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
