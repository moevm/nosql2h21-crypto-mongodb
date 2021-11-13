package com.nosql.cryp.controller;

import com.nosql.cryp.entity.History;
import com.nosql.cryp.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
@RequestMapping("/history")
public class HistoryController {

    @Autowired
    private HistoryService historyService;

    @GetMapping("/new")
    public String showCurrencyForm(Model model){
        model.addAttribute("history", new History());
        return "currency_form";
    }

    @PostMapping("/save")
    public String saveHistory(History newHistory){
        Date curr_date = new Date();
        newHistory.setTime(curr_date);
        historyService.saveHistory(newHistory);
        System.out.println("rate: " + newHistory.getRate);
        for (int i = 0; i < historyService.getAllHist().size(); i++){
            System.out.println(historyService.getAllHist().get(i).toString());;
        }
        return "main";
    }
}
