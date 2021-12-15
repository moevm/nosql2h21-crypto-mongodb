package com.nosql.cryp.controller;

import com.nosql.cryp.ApiToDb;
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
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.*;

@Controller
@RequestMapping("/history")
public class HistoryController {

    //Данные для графика: Курс одной валюты к другой
    public String twoCurrenciesGhraphicData(String asset_id_base1, String asset_id_base2, Date date1, Date date2)
    {
        List<History> historyList = historyService.getAllHist();
        List<Double> rate1= new ArrayList<Double>();
        List<Double> rate2= new ArrayList<Double>();

        if(historyList.size() > 0)
        {
            List<History> asset_history_list1 = new ArrayList<History>();
            List<History> asset_history_list2 = new ArrayList<History>();
            for (int i = 0; i < historyList.size(); i++)
            {
                History element = historyList.get(i);
                if (element.getTime().after(date1))
                {
                    if (element.getTime().before(date2))
                    {
                        if(element.getAsset_id_base() == asset_id_base1)
                        {
                            asset_history_list1.add(element);
                        }
                        if(element.getAsset_id_base() == asset_id_base2)
                        {
                            asset_history_list2.add(element);
                        }
                    }
                }
            }

            Collections.sort(asset_history_list1, new Comparator<History>() {
                @Override
                public int compare(History o1, History o2) {
                    if(o1.getTime() != null && o2.getTime() != null){
                        if(o1.getTime().after(o2.getTime()))
                            return -1;
                        if(o1.getTime().equals(o2.getTime()))
                            return 0;
                        if(o1.getTime().before(o2.getTime()))
                            return 1;
                    }
                    return 1;
                }
            });

            /*Collections.sort(asset_history_list2, new Comparator<History>() {
                @Override
                public int compare(History o1, History o2) {
                    if(o1.getTime() != null && o2.getTime() != null){
                        if(o1.getTime().after(o2.getTime()))
                            return -1;
                        if(o1.getTime().equals(o2.getTime()))
                            return 0;
                        if(o1.getTime().before(o2.getTime()))
                            return 1;
                    }
                    return 1;
                }
            });*/

            for (int i = 0; i < asset_history_list1.size(); i++)
            {
                History element1 = asset_history_list1.get(i);
                for (int j = 0; i < asset_history_list2.size(); i++)
                {
                    History element2 = asset_history_list2.get(i);
                    int dateForElem1 = element1.getTime().getYear() + element1.getTime().getMonth() + element1.getTime().getDay();
                    int dateForElem2 = element2.getTime().getYear() + element2.getTime().getMonth() + element2.getTime().getDay();
                    if(dateForElem1 == dateForElem2)
                    {
                        rate1.add(element1.getRate());
                        rate2.add(element2.getRate());
                    }
                }
            }

        }
        return "two lists";
    }

    //Анализ правильности покупки
    public String purhcaseCorrect(String asset_id_base, Date date)
    {
        Date curr_date = new Date();
        List<History> historyList = historyService.getAllHist();
        double trend = 0;
        if(historyList.size() > 0)
        {
            List<History> asset_history_list = new ArrayList<History>();
            Date newDate = curr_date;
            newDate.setMonth(date.getMonth() - 1);
            for (int i = 0; i < historyList.size(); i++)
            {
                History element = historyList.get(i);
                if(element.getAsset_id_base() == asset_id_base)
                {
                    if (element.getTime().after(date))
                    {
                        if (element.getTime().before(newDate))
                        {
                            asset_history_list.add(element);
                        }
                    }
                }

            }

            Collections.sort(asset_history_list, new Comparator<History>() {
                @Override
                public int compare(History o1, History o2) {
                    if(o1.getTime() != null && o2.getTime() != null){
                        if(o1.getTime().after(o2.getTime()))
                            return -1;
                        if(o1.getTime().equals(o2.getTime()))
                            return 0;
                        if(o1.getTime().before(o2.getTime()))
                            return 1;
                    }
                    return 1;
                }
            });

            List<Double> rates = new ArrayList<Double>();

            double start_point = asset_history_list.get(0).getRate();
            for (int i = 1; i < asset_history_list.size(); i++)
            {
                double element = asset_history_list.get(i).getRate();
                //rates.add((element/start_point)*100 - 100);
                trend += (element/start_point)*100 - 100;

            }
            trend = trend/(asset_history_list.size()-1);
        }

        if(trend > 0)
        {
            return "Покупка правильна";
        }
        return "Покупка не правильна";
    }

    //Анализ тренда
    public String trendAnalys(String assetd_id_base, Date date)
    {
        List<History> historyList = historyService.getAllHist();
        double trend = 0;
        if(historyList.size() > 0)
        {
            List<History> asset_history_list = new ArrayList<History>();
            Date newDate = date;
            newDate.setMonth(date.getMonth() - 1);
            for (int i = 0; i < historyList.size(); i++)
            {
                History element = historyList.get(i);
                if(element.getAsset_id_base() == assetd_id_base)
                {
                    if (element.getTime().after(newDate))
                    {
                        if (element.getTime().before(date))
                        {
                            asset_history_list.add(element);
                        }
                    }
                }

            }

            Collections.sort(asset_history_list, new Comparator<History>() {
                @Override
                public int compare(History o1, History o2) {
                    if(o1.getTime() != null && o2.getTime() != null){
                        if(o1.getTime().after(o2.getTime()))
                            return -1;
                        if(o1.getTime().equals(o2.getTime()))
                            return 0;
                        if(o1.getTime().before(o2.getTime()))
                            return 1;
                    }
                    return 1;
                }
            });

            List<Double> rates = new ArrayList<Double>();

            double start_point = asset_history_list.get(0).getRate();
            for (int i = 1; i < asset_history_list.size(); i++)
            {
                double element = asset_history_list.get(i).getRate();
                //rates.add((element/start_point)*100 - 100);
                trend += (element/start_point)*100 - 100;

            }
            trend = trend/(asset_history_list.size()-1);
        }

        if(trend > 0)
        {
            return "Валюта в тренде";
        }
        return "Валюта не в тренде";
    }

    @Autowired
    private HistoryService historyService;

    @GetMapping("/new")
    public String showCurrencyForm(Model model){
        model.addAttribute("currency", new Currency());
        return "currency_form";
    }

    @PostMapping("/save")
    public String saveHistory(History newHistory){
        Date curr_date = new Date();
        newHistory.setTime(curr_date);
        historyService.saveHistory(newHistory);
       // System.out.println("rate: " + newHistory.getRate);
        /*for (int i = 0; i < historyService.getAllHist().size(); i++){
            System.out.println(historyService.getAllHist().get(i).toString());;
        }*/
        return "main";
    }

    @PostMapping("/saveJSON")
    public String saveHistoryJSON(@RequestBody String jsonString) throws JSONException, ParseException {
        System.out.println(jsonString);
        JSONArray curr = new JSONArray(jsonString);
        for (int i = 0 ; i < curr.length(); i++) {
            JSONObject obj = curr.getJSONObject(i);
            if(!obj.has("rate") || !obj.has("time") || !obj.has("asset_id_quote") || !obj.has("asset_id_base"))
                continue;
            if (obj.has("rate")) {
                if (((int) obj.getDouble("rate")) <= 0)
                    continue;
            } else {
                continue;
            }

            History history = new History(obj);
            historyService.saveHistory(history);
        }
        return "main";

    }

    public String historyFromApi(String asset_id, Date date1, Date date2) throws JSONException, IOException, URISyntaxException, ParseException {
        // запросы к апи
        ApiToDb apitodb = new ApiToDb();
        JSONArray curr = apitodb.list_all_history(asset_id, date1, date2);
        for (int i = 0; i < curr.length(); i++) {
            JSONObject obj = curr.getJSONObject(i);

            History history = new History(obj, asset_id);
            historyService.saveHistory(history);

        }
        return "redirect:/currency/getAllCurr";
    }
}
