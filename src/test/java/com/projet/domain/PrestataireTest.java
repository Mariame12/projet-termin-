package com.projet.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.projet.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PrestataireTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Prestataire.class);
        Prestataire prestataire1 = new Prestataire();
        prestataire1.setId(1L);
        Prestataire prestataire2 = new Prestataire();
        prestataire2.setId(prestataire1.getId());
        assertThat(prestataire1).isEqualTo(prestataire2);
        prestataire2.setId(2L);
        assertThat(prestataire1).isNotEqualTo(prestataire2);
        prestataire1.setId(null);
        assertThat(prestataire1).isNotEqualTo(prestataire2);
    }
}
