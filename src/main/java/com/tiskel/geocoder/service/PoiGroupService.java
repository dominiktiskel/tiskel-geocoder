package com.tiskel.geocoder.service;

import com.tiskel.geocoder.service.dto.PoiGroupDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.tiskel.geocoder.domain.PoiGroup}.
 */
public interface PoiGroupService {
    /**
     * Save a poiGroup.
     *
     * @param poiGroupDTO the entity to save.
     * @return the persisted entity.
     */
    PoiGroupDTO save(PoiGroupDTO poiGroupDTO);

    /**
     * Partially updates a poiGroup.
     *
     * @param poiGroupDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PoiGroupDTO> partialUpdate(PoiGroupDTO poiGroupDTO);

    /**
     * Get all the poiGroups.
     *
     * @return the list of entities.
     */
    List<PoiGroupDTO> findAll();

    /**
     * Get the "id" poiGroup.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PoiGroupDTO> findOne(Long id);

    /**
     * Delete the "id" poiGroup.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
