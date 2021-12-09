package com.tiskel.geocoder.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.tiskel.geocoder.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PoiDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PoiDTO.class);
        PoiDTO poiDTO1 = new PoiDTO();
        poiDTO1.setId(1L);
        PoiDTO poiDTO2 = new PoiDTO();
        assertThat(poiDTO1).isNotEqualTo(poiDTO2);
        poiDTO2.setId(poiDTO1.getId());
        assertThat(poiDTO1).isEqualTo(poiDTO2);
        poiDTO2.setId(2L);
        assertThat(poiDTO1).isNotEqualTo(poiDTO2);
        poiDTO1.setId(null);
        assertThat(poiDTO1).isNotEqualTo(poiDTO2);
    }
}
