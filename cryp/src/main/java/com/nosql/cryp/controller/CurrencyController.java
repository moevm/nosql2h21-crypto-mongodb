package com.nosql.cryp.controller;

import com.nosql.cryp.ApiToDb;
import com.nosql.cryp.entity.Currency;
import com.nosql.cryp.repository.CurrencyRepository;
import com.nosql.cryp.service.CurrencyService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/currency")
public class CurrencyController {
    @Autowired
    private CurrencyService currencyService;

    @GetMapping("/apiFull")
    public String currencyFromApi() throws JSONException, IOException, URISyntaxException, ParseException {
            // запросы к апи
        ApiToDb apitodb = new ApiToDb();
        JSONArray curr = apitodb.list_all_assets();
        for (int i = 0 ; i < curr.length(); i++) {
            JSONObject obj = curr.getJSONObject(i);
            if (((int) obj.get("type_is_crypto") == 0))
                continue;
            Currency currency =  new Currency(obj);
            currencyService.saveCurrency(currency);
            System.out.println("Succesfully added currency: " + currency.toString());
        }
        return "redirect:/main";
    }
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
