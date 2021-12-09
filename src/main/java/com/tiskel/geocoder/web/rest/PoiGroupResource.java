package com.tiskel.geocoder.web.rest;

import com.tiskel.geocoder.repository.PoiGroupRepository;
import com.tiskel.geocoder.service.PoiGroupService;
import com.tiskel.geocoder.service.dto.PoiGroupDTO;
import com.tiskel.geocoder.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.tiskel.geocoder.domain.PoiGroup}.
 */
@RestController
@RequestMapping("/api")
public class PoiGroupResource {

    private final Logger log = LoggerFactory.getLogger(PoiGroupResource.class);

    private static final String ENTITY_NAME = "poiGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PoiGroupService poiGroupService;

    private final PoiGroupRepository poiGroupRepository;

    public PoiGroupResource(PoiGroupService poiGroupService, PoiGroupRepository poiGroupRepository) {
        this.poiGroupService = poiGroupService;
        this.poiGroupRepository = poiGroupRepository;
    }

    /**
     * {@code POST  /poi-groups} : Create a new poiGroup.
     *
     * @param poiGroupDTO the poiGroupDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new poiGroupDTO, or with status {@code 400 (Bad Request)} if the poiGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/poi-groups")
    public ResponseEntity<PoiGroupDTO> createPoiGroup(@Valid @RequestBody PoiGroupDTO poiGroupDTO) throws URISyntaxException {
        log.debug("REST request to save PoiGroup : {}", poiGroupDTO);
        if (poiGroupDTO.getId() != null) {
            throw new BadRequestAlertException("A new poiGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PoiGroupDTO result = poiGroupService.save(poiGroupDTO);
        return ResponseEntity
            .created(new URI("/api/poi-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /poi-groups/:id} : Updates an existing poiGroup.
     *
     * @param id the id of the poiGroupDTO to save.
     * @param poiGroupDTO the poiGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated poiGroupDTO,
     * or with status {@code 400 (Bad Request)} if the poiGroupDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the poiGroupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/poi-groups/{id}")
    public ResponseEntity<PoiGroupDTO> updatePoiGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PoiGroupDTO poiGroupDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PoiGroup : {}, {}", id, poiGroupDTO);
        if (poiGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, poiGroupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!poiGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PoiGroupDTO result = poiGroupService.save(poiGroupDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, poiGroupDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /poi-groups/:id} : Partial updates given fields of an existing poiGroup, field will ignore if it is null
     *
     * @param id the id of the poiGroupDTO to save.
     * @param poiGroupDTO the poiGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated poiGroupDTO,
     * or with status {@code 400 (Bad Request)} if the poiGroupDTO is not valid,
     * or with status {@code 404 (Not Found)} if the poiGroupDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the poiGroupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/poi-groups/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PoiGroupDTO> partialUpdatePoiGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PoiGroupDTO poiGroupDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PoiGroup partially : {}, {}", id, poiGroupDTO);
        if (poiGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, poiGroupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!poiGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PoiGroupDTO> result = poiGroupService.partialUpdate(poiGroupDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, poiGroupDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /poi-groups} : get all the poiGroups.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of poiGroups in body.
     */
    @GetMapping("/poi-groups")
    public List<PoiGroupDTO> getAllPoiGroups() {
        log.debug("REST request to get all PoiGroups");
        return poiGroupService.findAll();
    }

    /**
     * {@code GET  /poi-groups/:id} : get the "id" poiGroup.
     *
     * @param id the id of the poiGroupDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the poiGroupDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/poi-groups/{id}")
    public ResponseEntity<PoiGroupDTO> getPoiGroup(@PathVariable Long id) {
        log.debug("REST request to get PoiGroup : {}", id);
        Optional<PoiGroupDTO> poiGroupDTO = poiGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(poiGroupDTO);
    }

    /**
     * {@code DELETE  /poi-groups/:id} : delete the "id" poiGroup.
     *
     * @param id the id of the poiGroupDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/poi-groups/{id}")
    public ResponseEntity<Void> deletePoiGroup(@PathVariable Long id) {
        log.debug("REST request to delete PoiGroup : {}", id);
        poiGroupService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
