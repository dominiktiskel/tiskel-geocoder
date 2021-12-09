package com.tiskel.geocoder.service;

import com.tiskel.geocoder.service.dto.PoiDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.tiskel.geocoder.domain.Poi}.
 */
public interface PoiService {
    /**
     * Save a poi.
     *
     * @param poiDTO the entity to save.
     * @return the persisted entity.
     */
    PoiDTO save(PoiDTO poiDTO);

    /**
     * Partially updates a poi.
     *
     * @param poiDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PoiDTO> partialUpdate(PoiDTO poiDTO);

    /**
     * Get all the pois.
     *
     * @return the list of entities.
     */
    List<PoiDTO> findAll();

    /**
     * Get the "id" poi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PoiDTO> findOne(Long id);

    /**
     * Delete the "id" poi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
