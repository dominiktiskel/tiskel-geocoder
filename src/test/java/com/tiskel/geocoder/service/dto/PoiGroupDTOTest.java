package com.tiskel.geocoder.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.tiskel.geocoder.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PoiGroupDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PoiGroupDTO.class);
        PoiGroupDTO poiGroupDTO1 = new PoiGroupDTO();
        poiGroupDTO1.setId(1L);
        PoiGroupDTO poiGroupDTO2 = new PoiGroupDTO();
        assertThat(poiGroupDTO1).isNotEqualTo(poiGroupDTO2);
        poiGroupDTO2.setId(poiGroupDTO1.getId());
        assertThat(poiGroupDTO1).isEqualTo(poiGroupDTO2);
        poiGroupDTO2.setId(2L);
        assertThat(poiGroupDTO1).isNotEqualTo(poiGroupDTO2);
        poiGroupDTO1.setId(null);
        assertThat(poiGroupDTO1).isNotEqualTo(poiGroupDTO2);
    }
}
