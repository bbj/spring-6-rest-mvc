package com.bbj.spring6restmvc.repositories;

import com.bbj.spring6restmvc.entities.Beer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testSaveBeer() {
        /**
         * Hibernate will do reflection upon the 2 entities that we've created
         * then H2 in-mem database is created automatically by Spring Boot
         * then database is initialized by Hibernate
         */

        Beer savedBeer = beerRepository.save(Beer.builder()
                .beerName("My Beer")
                .build());
        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getId()).isNotNull();
    }
}