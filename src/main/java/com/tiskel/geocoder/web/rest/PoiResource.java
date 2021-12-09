package com.tiskel.geocoder.web.rest;

import com.tiskel.geocoder.repository.PoiRepository;
import com.tiskel.geocoder.service.PoiService;
import com.tiskel.geocoder.service.dto.PoiDTO;
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
 * REST controller for managing {@link com.tiskel.geocoder.domain.Poi}.
 */
@RestController
@RequestMapping("/api")
public class PoiResource {

    private final Logger log = LoggerFactory.getLogger(PoiResource.class);

    private static final String ENTITY_NAME = "poi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PoiService poiService;

    private final PoiRepository poiRepository;

    public PoiResource(PoiService poiService, PoiRepository poiRepository) {
        this.poiService = poiService;
        this.poiRepository = poiRepository;
    }

    /**
     * {@code POST  /pois} : Create a new poi.
     *
     * @param poiDTO the poiDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new poiDTO, or with status {@code 400 (Bad Request)} if the poi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pois")
    public ResponseEntity<PoiDTO> createPoi(@Valid @RequestBody PoiDTO poiDTO) throws URISyntaxException {
        log.debug("REST request to save Poi : {}", poiDTO);
        if (poiDTO.getId() != null) {
            throw new BadRequestAlertException("A new poi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PoiDTO result = poiService.save(poiDTO);
        return ResponseEntity
            .created(new URI("/api/pois/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pois/:id} : Updates an existing poi.
     *
     * @param id the id of the poiDTO to save.
     * @param poiDTO the poiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated poiDTO,
     * or with status {@code 400 (Bad Request)} if the poiDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the poiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pois/{id}")
    public ResponseEntity<PoiDTO> updatePoi(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody PoiDTO poiDTO)
        throws URISyntaxException {
        log.debug("REST request to update Poi : {}, {}", id, poiDTO);
        if (poiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, poiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!poiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PoiDTO result = poiService.save(poiDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, poiDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pois/:id} : Partial updates given fields of an existing poi, field will ignore if it is null
     *
     * @param id the id of the poiDTO to save.
     * @param poiDTO the poiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated poiDTO,
     * or with status {@code 400 (Bad Request)} if the poiDTO is not valid,
     * or with status {@code 404 (Not Found)} if the poiDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the poiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pois/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PoiDTO> partialUpdatePoi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PoiDTO poiDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Poi partially : {}, {}", id, poiDTO);
        if (poiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, poiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!poiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PoiDTO> result = poiService.partialUpdate(poiDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, poiDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /pois} : get all the pois.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pois in body.
     */
    @GetMapping("/pois")
    public List<PoiDTO> getAllPois() {
        log.debug("REST request to get all Pois");
        return poiService.findAll();
    }

    /**
     * {@code GET  /pois/:id} : get the "id" poi.
     *
     * @param id the id of the poiDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the poiDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pois/{id}")
    public ResponseEntity<PoiDTO> getPoi(@PathVariable Long id) {
        log.debug("REST request to get Poi : {}", id);
        Optional<PoiDTO> poiDTO = poiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(poiDTO);
    }

    /**
     * {@code DELETE  /pois/:id} : delete the "id" poi.
     *
     * @param id the id of the poiDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pois/{id}")
    public ResponseEntity<Void> deletePoi(@PathVariable Long id) {
        log.debug("REST request to delete Poi : {}", id);
        poiService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
