package com.nosql.cryp.repository;


import com.nosql.cryp.entity.Currency;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface CurrencyRepository extends MongoRepository<Currency, Integer> {
    @Query("{'asset_id': ?0}")
    List<Currency> findByAsset_id(String asset_id);

}
