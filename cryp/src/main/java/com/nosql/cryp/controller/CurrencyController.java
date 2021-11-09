package com.nosql.cryp.controller;

import com.nosql.cryp.entity.Currency;
import com.nosql.cryp.repository.CurrencyRepository;
import com.nosql.cryp.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
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
    public String getAllCurrencies(Model model){
        List<Currency> currencies = currencyService.getAllCurr();
        if (currencies.size() > 0){
            model.addAttribute("currencies", currencies);
            return "Vrode ok";
          //  return new ResponseEntity<List<Currency>>(currencies, HttpStatus.OK);
        }
        else{
            return "error";
         //   return new ResponseEntity<>("no comprende", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getAllCurr/{id}")
    public Optional<Currency> getCurrency(@PathVariable int id){
        return currencyService.getCurrency(id);
    }


}
