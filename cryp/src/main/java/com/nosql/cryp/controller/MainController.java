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

    @GetMapping("/")
    public String apiGet(){

        return "redirect:/currency/apiFull";
    }
    @GetMapping("/mainPage")
    public String showHomePage(Model model){
        
        return "mainPage";
    }
}
