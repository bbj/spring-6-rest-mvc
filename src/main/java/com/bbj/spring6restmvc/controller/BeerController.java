package com.bbj.spring6restmvc.controller;

import com.bbj.spring6restmvc.model.BeerDTO;
import com.bbj.spring6restmvc.services.BeerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * why not having BeerController as an interface like for services?
 * because we generally do not switch controllers (1 class)
 * so would not be useful
 *
 * @AllArgsConstructor: instead of using IDE constructor generation
 * we ask lombok to do it, for the private final properties.
 *
 * BeerService will be injected at runtime, as lombok @AllArgsConstructor
 * will generate a constructor with the services as param
 *
 * replace @Controller (regular SpringMVC) by @RestController (Rest).
 * Will turn back JSON instead of HTML response.
 * It applies the @ResponseBody annotation to all methods which instructs Spring to
 * serialize the body of the response into JSON.
 * Remember, we don't need to annotate the @RestController-annotated controllers
 * with the @ResponseBody annotation since Spring does it by default.
 */
@Slf4j
//@AllArgsConstructor               //was a mistake before => @RequiredArgsConstructor
@RequiredArgsConstructor
@RestController                     //replace @Controller by @RestController (@ResponseBody applied)
@RequestMapping("/api/v1/beer")   //base path for all queries
public class BeerController {

    public static final String BEER_PATH = "/api/v1/beer";
    public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";

    private final BeerService beerService;

    @PatchMapping(BEER_PATH_ID)
    //@ResponseBody - not needed because we have @RestController
    public ResponseEntity patchBeerById(
            @PathVariable("beerId") UUID beerId,
            @RequestBody BeerDTO beer) {

        beerService.patchBeerById(beerId, beer);

        return new ResponseEntity(HttpStatus.NO_CONTENT); //update ok, no content returned
    }

    @DeleteMapping(BEER_PATH_ID)
    public ResponseEntity deleteById(@PathVariable("beerId") UUID beerId) {

        beerService.deleteById(beerId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**
     * It is more reliable to depend on @PathVariable("beerId")
     * than on the parameter name (UUID id)
     */
    @PutMapping(BEER_PATH_ID)
    public ResponseEntity updateById(
            @PathVariable("beerId") UUID beerId,
            @RequestBody BeerDTO beer) {

        beerService.updateBeerById(beerId, beer);

        return new ResponseEntity(HttpStatus.NO_CONTENT); //update ok, no content returned
    }

    //@RequestMapping(method = RequestMethod.POST) is equivalent to @PostMapping
    @PostMapping(BEER_PATH)
    public ResponseEntity handlePost(@RequestBody BeerDTO beer) {

        //@RequestBody tells Spring to bind the HTTP request body to this (BeerDTO beer) param
        BeerDTO savedBeer = beerService.saveNewBeer(beer);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", BEER_PATH +"/"+savedBeer.getId().toString());

        return new ResponseEntity(
                headers,
                HttpStatus.CREATED); //201 status: resource created (saved in DB)
    }

    //if method not specified => will answer all methods (PUT, POST ....)
    @RequestMapping(method = RequestMethod.GET)
    public List<BeerDTO> listBeers() {
        //beerService.listBeers() does a new ArrayList<>(beerMap.values())
        //where beerMap.values() returns a Collection<V> where V = String
        return beerService.listBeers();
    }

    /**
     * It is more reliable to depend on @PathVariable("beerId")
     * than on the parameter name (UUID id)
     */
    @RequestMapping(BEER_PATH_ID)
    public BeerDTO getBeerById(@PathVariable("beerId") UUID id) {

        log.debug("Get BeerDTO by Id - in beer controller; id="+id.toString());
        return beerService.getBeerById(id).orElseThrow(NotFoundException::new);
    }
}
