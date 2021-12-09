package com.tiskel.geocoder.repository;

import com.tiskel.geocoder.domain.Poi;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Poi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PoiRepository extends JpaRepository<Poi, Long> {}
