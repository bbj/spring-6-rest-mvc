package com.bbj.spring6restmvc.controller;

import com.bbj.spring6restmvc.service.BeerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BeerController.class)
class BeerControllerTest {

    //Setup MockMvc by bringing a MockMvc component, injected by Spring.
    @Autowired
    MockMvc mockMvc;

    //tells Mockito to bring a mock of BeerService into the spring context
    //by default will return a null response
    @MockBean
    BeerService beerService;

    /*
        throws Exception: as mockMvc.perform is doing a throws Exception
     */
    @Test                       //JUnit test
    void getBeerById() throws Exception {
        //mock is simulating a HTTP get and will really call our controller!!!
        //and test the status of what it returns
        mockMvc.perform(get("/api/v1/beer/"+ UUID.randomUUID())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}

//WAS THE INITIAL BASIC TEST WHICH WAS RUNNING SPRING BOOT
//@SpringBootTest
//class BeerControllerTest {
//
//    @Autowired
//    BeerController beerController;
//
//    @Test
//    void getBeerById() {
//        System.out.println(beerController.getBeerById(UUID.randomUUID()));
//    }
//}