package com.bbj.spring6restmvc.controller;

import com.bbj.spring6restmvc.model.BeerDTO;
import com.bbj.spring6restmvc.repositories.BeerRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

//here, for integration test, we need the full Spring Context,
// not a test splice like @WebMvcTest(BeerController.class)
// so it will populate the H2 database
@SpringBootTest
class BeerControllerIT {
    @Autowired
    BeerController beerController;
    @Autowired
    BeerRepository beerRepository;

    @Test
    void testListBeers() {
        List<BeerDTO> dtos = beerController.listBeers();
        assertThat(dtos.size()).isEqualTo(3);
    }

    /*
        In fact this test is run first, so testListBeers() will fail
        As this test is deleting data, we tell Spring to run it in a transaction
        and to rollback the database to its initial state after the test, so next test will be ok
     */
    @Rollback
    @Transactional
    @Test
    void testEmptyList() {
        beerRepository.deleteAll();
        List<BeerDTO> dtos = beerController.listBeers();
        assertThat(dtos.size()).isEqualTo(0);
    }
}