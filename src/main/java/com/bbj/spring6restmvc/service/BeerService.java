package com.bbj.spring6restmvc.service;

import com.bbj.spring6restmvc.model.Beer;

import java.util.UUID;

public interface BeerService {
    Beer getBeerById(UUID id);
}
