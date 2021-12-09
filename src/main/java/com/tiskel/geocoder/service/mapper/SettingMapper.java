package com.tiskel.geocoder.service.mapper;

import com.tiskel.geocoder.domain.Setting;
import com.tiskel.geocoder.service.dto.SettingDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Setting} and its DTO {@link SettingDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SettingMapper extends EntityMapper<SettingDTO, Setting> {}
