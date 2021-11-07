package com.nosql.cryp.repository;


import com.nosql.cryp.entity.Currency;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CurrencyRepository extends MongoRepository<Currency, Integer> {
}
