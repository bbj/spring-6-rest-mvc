package com.bbj.spring6restmvc.services;

import com.bbj.spring6restmvc.model.BeerDTO;
import com.bbj.spring6restmvc.model.BeerStyle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

    //beers are stored in a HashMap, initialized in the constructor
    private Map<UUID, BeerDTO> beerMap;

    //the constructor creates 3 beers and add it to the beerMap HashMap
    public BeerServiceImpl() {
        this.beerMap = new HashMap<>();

        BeerDTO beer1 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Galaxy Cat")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(122)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        BeerDTO beer2 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Crank")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356222")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(392)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        BeerDTO beer3 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Sunshine City")
                .beerStyle(BeerStyle.IPA)
                .upc("12356")
                .price(new BigDecimal("13.99"))
                .quantityOnHand(144)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        beerMap.put(beer1.getId(), beer1);
        beerMap.put(beer2.getId(), beer2);
        beerMap.put(beer3.getId(), beer3);
    }

    @Override
    public List<BeerDTO> listBeers() {
        return new ArrayList<>(beerMap.values());
    }

//    @Override
//    public BeerDTO getBeerById(UUID id) {
//        log.debug("Get BeerDTO Id - in services. Id: "+id.toString());
//        return beerMap.get(id);
//    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {

        log.debug("Get Beer by Id - in services. Id: " + id.toString());

        return Optional.of(beerMap.get(id));
    }

    @Override
    public BeerDTO saveNewBeer(BeerDTO beer) {
        //we are mimicking what a persistent store would do; using lombok generated builder
        BeerDTO savedBeer = BeerDTO.builder()
                .id(UUID.randomUUID())
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .beerName(beer.getBeerName())
                .beerStyle(beer.getBeerStyle())
                .quantityOnHand(beer.getQuantityOnHand())
                .upc(beer.getUpc())
                .price(beer.getPrice())
                .build();
        beerMap.put(savedBeer.getId(), savedBeer);
        return savedBeer;
    }

    @Override
    public Optional<BeerDTO> updateBeerById(UUID beerId, BeerDTO beer) {
        BeerDTO existing = beerMap.get(beerId);
        existing.setBeerName(beer.getBeerName());
        existing.setPrice(beer.getPrice());
        existing.setUpc(beer.getUpc());
        existing.setQuantityOnHand(beer.getQuantityOnHand());

        beerMap.put(existing.getId(), existing);

        return Optional.of(existing);
    }

    @Override
    public void deleteById(UUID beerId) {
        beerMap.remove(beerId);
    }

    // PATCH operation is pretty rare, most of the time we do not need it
    // and we use UPDATE instead
    @Override
    public void patchBeerById(UUID beerId, BeerDTO beer) {
        BeerDTO existing = beerMap.get(beerId);

        // patching is about detecting what has changed
        // and update the existing object in the database

        boolean isUpdated = false;
        if (StringUtils.hasText(beer.getBeerName())){
            existing.setBeerName(beer.getBeerName());
            isUpdated = true;
        }

        if (beer.getBeerStyle() != null) {
            existing.setBeerStyle(beer.getBeerStyle());
            isUpdated = true;
        }

        if (beer.getPrice() != null) {
            existing.setPrice(beer.getPrice());
            isUpdated = true;
        }

        if (beer.getQuantityOnHand() != null){
            existing.setQuantityOnHand(beer.getQuantityOnHand());
            isUpdated = true;
        }

        if (StringUtils.hasText(beer.getUpc())) {
            existing.setUpc(beer.getUpc());
            isUpdated = true;
        }

        if (isUpdated) existing.setUpdateDate(LocalDateTime.now());
    }
}
