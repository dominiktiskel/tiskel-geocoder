package com.tiskel.geocoder.service.mapper;

import com.tiskel.geocoder.domain.PoiGroup;
import com.tiskel.geocoder.service.dto.PoiGroupDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PoiGroup} and its DTO {@link PoiGroupDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PoiGroupMapper extends EntityMapper<PoiGroupDTO, PoiGroup> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PoiGroupDTO toDtoId(PoiGroup poiGroup);
}
