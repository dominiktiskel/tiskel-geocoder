package com.tiskel.geocoder.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.tiskel.geocoder.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SourceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SourceDTO.class);
        SourceDTO sourceDTO1 = new SourceDTO();
        sourceDTO1.setId(1L);
        SourceDTO sourceDTO2 = new SourceDTO();
        assertThat(sourceDTO1).isNotEqualTo(sourceDTO2);
        sourceDTO2.setId(sourceDTO1.getId());
        assertThat(sourceDTO1).isEqualTo(sourceDTO2);
        sourceDTO2.setId(2L);
        assertThat(sourceDTO1).isNotEqualTo(sourceDTO2);
        sourceDTO1.setId(null);
        assertThat(sourceDTO1).isNotEqualTo(sourceDTO2);
    }
}
