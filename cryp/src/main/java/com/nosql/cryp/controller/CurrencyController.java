package com.nosql.cryp.controller;

import com.nosql.cryp.entity.Currency;
import com.nosql.cryp.repository.CurrencyRepository;
import com.nosql.cryp.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/currency")
public class CurrencyController {
    @Autowired
    private CurrencyService currencyService;

    @PostMapping("/new")
    public String saveCurrency(@RequestBody Currency newCurrency){
        currencyService.saveCurrency(newCurrency);
        return "Succesfully added currency: " + newCurrency.toString();
    }

    @GetMapping("/getAllCurr")
    //public List<Currency> getCurrencies(){
    public ResponseEntity<?> getAllCurrencies(){
        List<Currency> currencies = currencyService.getAllCurr();
        if (currencies.size() > 0){
            return new ResponseEntity<List<Currency>>(currencies, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("no comprende", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getAllCurr/{id}")
    public Optional<Currency> getCurrency(@PathVariable int id){
        return currencyService.getCurrency(id);
    }
}
