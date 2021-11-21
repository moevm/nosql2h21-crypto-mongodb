package com.nosql.cryp.controller;

import com.nosql.cryp.entity.Currency;
import com.nosql.cryp.entity.History;
import com.nosql.cryp.service.HistoryService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.ParseException;
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
       // System.out.println("rate: " + newHistory.getRate);
        for (int i = 0; i < historyService.getAllHist().size(); i++){
            System.out.println(historyService.getAllHist().get(i).toString());;
        }
        return "main";
    }

    @PostMapping("/saveJSON")
    public String saveHistoryJSON(@RequestBody String jsonString) throws JSONException, ParseException {
        System.out.println(jsonString);
        JSONArray curr = new JSONArray(jsonString);
        for (int i = 0 ; i < curr.length(); i++) {
            JSONObject obj = curr.getJSONObject(i);
            if (obj.has("price_usd")) {
                if (((int) obj.getDouble("price_usd")) <= 0)
                    continue;
            } else {
                continue;
            }

            History history = new History(obj);
            historyService.saveHistory(history);
        }
        return "main";

    }
}
