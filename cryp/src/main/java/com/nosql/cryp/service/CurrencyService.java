package com.nosql.cryp.service;

import com.nosql.cryp.entity.Currency;

import java.util.List;
import java.util.Optional;

public interface CurrencyService {

    public Currency saveCurrency(Currency currency);

    public List<Currency> getAllCurr();

    public Optional<Currency> getCurrency(Integer id);

    public List<Currency> getByAsset_id(String asset_id);
}
