package com.projet.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.projet.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConsultantTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Consultant.class);
        Consultant consultant1 = new Consultant();
        consultant1.setId(1L);
        Consultant consultant2 = new Consultant();
        consultant2.setId(consultant1.getId());
        assertThat(consultant1).isEqualTo(consultant2);
        consultant2.setId(2L);
        assertThat(consultant1).isNotEqualTo(consultant2);
        consultant1.setId(null);
        assertThat(consultant1).isNotEqualTo(consultant2);
    }
}
