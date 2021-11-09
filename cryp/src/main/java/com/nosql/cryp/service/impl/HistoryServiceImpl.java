package com.nosql.cryp.service.impl;

import com.nosql.cryp.entity.History;
import com.nosql.cryp.repository.HistoryRepository;
import com.nosql.cryp.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HistoryServiceImpl implements HistoryService {
    @Autowired
    private HistoryRepository historyRepository;
    @Override
    public History saveHistory(History History) {
        return historyRepository.save(History);
    }

    @Override
    public List<History> getAllHist() {
        return historyRepository.findAll();
    }

    @Override
    public Optional<History> getHistory(Integer id) {
        return historyRepository.findById(id);
    }
}
