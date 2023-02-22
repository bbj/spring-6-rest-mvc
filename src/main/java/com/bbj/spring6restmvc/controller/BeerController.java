package com.bbj.spring6restmvc.controller;

import com.bbj.spring6restmvc.model.Beer;
import com.bbj.spring6restmvc.service.BeerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * why not having BeerController as an interface like for service?
 * because we generally do not switch controllers (1 class)
 * so would not be useful
 *
 * @AllArgsConstructor: instead of using IDE constructor generation
 * we ask lombok to do it, for the private final properties.
 *
 * BeerService will be injected at runtime, as lombok @AllArgsConstructor
 * will generate a constructor with the service as param
 *
 * replace @Controller (regular SpringMVC) by @RestController (Rest).
 * Will turn back JSON instead of HTML response
 */
@Slf4j
@AllArgsConstructor
@RestController                     //replace @Controller by @RestController
@RequestMapping("/api/v1/beer")   //base path for all queries
public class BeerController {

    private final BeerService beerService;

    @PatchMapping("{beerId}")
    public ResponseEntity patchBeerById(
            @PathVariable("beerId") UUID beerId,
            @RequestBody Beer beer) {

        beerService.patchBeerById(beerId, beer);

        return new ResponseEntity(HttpStatus.NO_CONTENT); //update ok, no content returned
    }

    @DeleteMapping("{beerId}")
    public ResponseEntity deleteById(@PathVariable("beerId") UUID beerId) {

        beerService.deleteById(beerId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**
     * It is more reliable to depend on @PathVariable("beerId")
     * than on the parameter name (UUID id)
     */
    @PutMapping("{beerId}")
    public ResponseEntity updateById(
            @PathVariable("beerId") UUID beerId,
            @RequestBody Beer beer) {

        beerService.updateBeerById(beerId, beer);

        return new ResponseEntity(HttpStatus.NO_CONTENT); //update ok, no content returned
    }

    //@RequestMapping(method = RequestMethod.POST) is equivalent to @PostMapping
    @PostMapping
    public ResponseEntity handlePost(@RequestBody Beer beer) {

        //@RequestBody tells Spring to bind the HTTP request body to this (Beer beer) param
        Beer savedBeer = beerService.saveNewBeer(beer);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/beer/" + savedBeer.getId().toString());

        return new ResponseEntity(
                headers,
                HttpStatus.CREATED); //201 status: resource created (saved in DB)
    }

    //if method not specified => will answer all methods (PUT, POST ....)
    @RequestMapping(method = RequestMethod.GET)
    public List<Beer> listBeers() {
        //beerService.listBeers() does a new ArrayList<>(beerMap.values())
        //where beerMap.values() returns a Collection<V> where V = String
        return beerService.listBeers();
    }

    /**
     * It is more reliable to depend on @PathVariable("beerId")
     * than on the parameter name (UUID id)
     */
    @RequestMapping("{beerId}")
    public Beer getBeerById(@PathVariable("beerId") UUID id) {

        log.debug("Get Beer by Id - in beer controller; id="+id.toString());
        return beerService.getBeerById(id);
    }
}
