package com.bbj.spring6restmvc.controller;

import com.bbj.spring6restmvc.model.Beer;
import com.bbj.spring6restmvc.service.BeerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

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
 */
@Slf4j
@AllArgsConstructor
@Controller
public class BeerController {
    private final BeerService beerService;

    public Beer getBeerById(UUID id) {

        log.debug("Get Beer by Id - in controller; id="+id.toString());

        return beerService.getBeerById(id);
    }
}
