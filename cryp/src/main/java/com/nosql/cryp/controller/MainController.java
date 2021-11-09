package com.nosql.cryp.controller;

import com.nosql.cryp.entity.Currency;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MainController {
    @GetMapping("/main")
    public String showHomePage(Model model){
        /*
        Date date1 = new Date(453221);
        Date date2 = new Date(143232);
        Currency currency1 = new Currency("BTC", date1, 10.23);
        Currency currency2 = new Currency("DG", date2, 1.15);
        List<Currency> currencies = new ArrayList<Currency>();
        currencies.add(currency1);
        currencies.add(currency2);
        model.addAttribute("currencies", currencies);
         */
        return "main";
    }
}
