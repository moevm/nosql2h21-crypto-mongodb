package com.nosql.cryp.service.impl;

import com.nosql.cryp.entity.Currency;
import com.nosql.cryp.repository.CurrencyRepository;
import com.nosql.cryp.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    @Autowired
    private CurrencyRepository currencyRepository;
    @Override
    public Currency saveCurrency(Currency currency) {
        return currencyRepository.save(currency);
    }

    @Override
    public List<Currency> getAllCurr() {
        return currencyRepository.findAll();
    }

    @Override
    public Optional<Currency> getCurrency(Integer id) {
        return currencyRepository.findById(id);
    }

    @Override
    public List<Currency> getByAsset_id(String asset_id) {
        return currencyRepository.findByAsset_id(asset_id);
    }

}
