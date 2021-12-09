package com.tiskel.geocoder.service.mapper;

import com.tiskel.geocoder.domain.Poi;
import com.tiskel.geocoder.service.dto.PoiDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Poi} and its DTO {@link PoiDTO}.
 */
@Mapper(componentModel = "spring", uses = { SourceMapper.class, PoiGroupMapper.class })
public interface PoiMapper extends EntityMapper<PoiDTO, Poi> {
    @Mapping(target = "source", source = "source", qualifiedByName = "id")
    @Mapping(target = "poiGroup", source = "poiGroup", qualifiedByName = "id")
    PoiDTO toDto(Poi s);
}
