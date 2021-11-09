package com.nosql.cryp.repository;

import com.nosql.cryp.entity.History;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HistoryRepository extends MongoRepository<History, Integer> {
}
