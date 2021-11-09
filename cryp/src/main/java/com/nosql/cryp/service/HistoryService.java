package com.nosql.cryp.service;

import com.nosql.cryp.entity.History;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
public interface HistoryService {

    public History saveHistory(History history);

    public List<History> getAllHist();

    public Optional<History> getHistory(Integer id);
}
