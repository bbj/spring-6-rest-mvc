package com.bbj.spring6restmvc.controller;

import com.bbj.spring6restmvc.mappers.BeerMapper;
import com.bbj.spring6restmvc.model.BeerDTO;
import com.bbj.spring6restmvc.entities.Beer;
import com.bbj.spring6restmvc.repositories.BeerRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

//here, for integration test, we need the full Spring Context,
// not a test splice like @WebMvcTest(BeerController.class)
// so it will populate the H2 database
@SpringBootTest
class BeerControllerIT {
    @Autowired
    BeerController beerController;

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    BeerMapper beerMapper;

    @Test
    void testUpdateNotFound() {
        assertThrows(NotFoundException.class, () -> {
            //update a non-existing uuid, set it to an empty object (using builder)
            beerController.updateById(UUID.randomUUID(), BeerDTO.builder().build());
        });
    }

    @Test
    void updateExistingBeer() {
        Beer beer = beerRepository.findAll().get(0);
        BeerDTO beerDTO = beerMapper.beerToBeerDto(beer);
        beerDTO.setId(null);
        beerDTO.setVersion(null);
        final String beerName = "UPDATED";
        beerDTO.setBeerName(beerName);

        ResponseEntity<Object> responseEntity = beerController.updateById(beer.getId(), beerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Beer updatedBeer = beerRepository.findById(beer.getId()).get(); //Optional.get()
        assertThat(updatedBeer.getBeerName()).isEqualTo(beerName);
    }

    //WARNING this test is modifying the database => ask Spring to make it transactional and rollback it at the end
    @Rollback
    @Transactional
    @Test
    void saveNewBeerTest() {
        BeerDTO beerDTO = BeerDTO.builder()
                .beerName("New Beer")
                .build();

        //in reality SpringMVC will transform HTTP request body into a DTO object
        //here we create the DTO with Lombok builder and pass it to controller
        ResponseEntity<Object> responseEntity = beerController.handlePost(beerDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull(); //java.net.URI getLocation()

        //location is the URL of the newly created entity, UUID is in position 5
        //e.g. /api/v1/beer/theid
        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[4]);

        Beer beer = beerRepository.findById(savedUUID).get();
        assertThat(beer).isNotNull();
    }

    @Test
    void testBeerIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            beerController.getBeerById(UUID.randomUUID());
        });
    }

    @Test
    void testGetById() {
        Beer beer = beerRepository.findAll().get(0);
        BeerDTO dto = beerController.getBeerById(beer.getId());
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(beer.getId());
    }

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