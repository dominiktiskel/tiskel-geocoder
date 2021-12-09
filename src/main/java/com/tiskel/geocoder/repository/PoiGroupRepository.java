package com.tiskel.geocoder.repository;

import com.tiskel.geocoder.domain.PoiGroup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PoiGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PoiGroupRepository extends JpaRepository<PoiGroup, Long> {}
