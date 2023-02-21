package com.bbj.spring6restmvc.controller;

import com.bbj.spring6restmvc.model.Beer;
import com.bbj.spring6restmvc.service.BeerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
        log.debug("Foo");
        return beerService.getBeerById(id);
    }
}
