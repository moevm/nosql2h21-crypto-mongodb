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
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/history")
public class HistoryController {

    //Данные для графика: валюта по времени
    @GetMapping("/currencyTimeRateGrahpicData")
    public String currencyTimeRateGrahpicData(@RequestParam  String asset_id_base,@RequestParam  String dateMin,@RequestParam  String dateMax, Model model) throws ParseException {
        if(Objects.equals("", asset_id_base) || Objects.equals("", dateMin) || Objects.equals("", dateMax))
            return "mainPage";
        dateMin += "T00:00:00.0000000Z";
        dateMax += "T00:00:00.0000000Z";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'");
        Date date1 = format.parse(dateMin);
        Date date2 = format.parse(dateMax);
        List<History> historyList = historyService.getAllHist();
        List<Double> rateList= new ArrayList<Double>();//Сами данные тут
        List<Date> dateList= new ArrayList<Date>();
        List<String> assetsNames = new ArrayList<String>();
        assetsNames.add(asset_id_base);
        if(historyList.size() > 0)
        {
            List<History> asset_history_list = new ArrayList<History>();
            for (int i = 0; i < historyList.size(); i++)
            {
                History element = historyList.get(i);
                if (Objects.equals(element.getAsset_id_base(), asset_id_base))
                {
                    Date date22 = element.getTime();
                    if (date22.after(date1))
                    {
                        if (date22.before(date2))
                        {
                            asset_history_list.add(element);
                        }
                    }
                }
            }
            if(asset_history_list.size() > 0)
            {
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

                for(int i = 0; i < asset_history_list.size(); i++)
                {
                    History element = asset_history_list.get(i);
                    rateList.add(element.getRate());
                    dateList.add(element.getTime());
                }
            }

        }
        model.addAttribute("rateList", rateList);
        model.addAttribute("dateList", dateList);
        model.addAttribute("assetsNames",assetsNames);
        return "mainPage";
    }

    //Данные для графика: Курс одной валюты к другой
    @GetMapping("/twoCurrenciesGhraphicData")
    public String twoCurrenciesGhraphicData(@RequestParam String asset_id_base1,@RequestParam String asset_id_base2,@RequestParam String dateMin,@RequestParam String dateMax, Model model) throws ParseException {
        if(Objects.equals("", asset_id_base1) || Objects.equals("", dateMin) || Objects.equals("", dateMax) || Objects.equals("", asset_id_base2))
            return "mainPage";
        dateMin += "T00:00:00.0000000Z";
        dateMax += "T00:00:00.0000000Z";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'");
        Date date1 = format.parse(dateMin);
        Date date2 = format.parse(dateMax);
        List<History> historyList = historyService.getAllHist();
        List<Double> rate1= new ArrayList<Double>();//Сами данные тут
        List<Double> rate2= new ArrayList<Double>();
        List<Date> dateList= new ArrayList<Date>();
        List<String> assetsNames = new ArrayList<String>();
        assetsNames.add(asset_id_base1);
        assetsNames.add(asset_id_base2);

        if(historyList.size() > 0)
        {
            List<History> asset_history_list1 = new ArrayList<History>();
            List<History> asset_history_list2 = new ArrayList<History>();
            for (int i = 0; i < historyList.size(); i++)
            {
                History element = historyList.get(i);
                Date date22 = element.getTime();
                if (date22.after(date1))
                {
                    if (date22.before(date2))
                    {
                        if(Objects.equals(element.getAsset_id_base(), asset_id_base1))
                        {
                            asset_history_list1.add(element);
                        }
                        if(Objects.equals(element.getAsset_id_base(), asset_id_base2))
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

            Collections.sort(asset_history_list2, new Comparator<History>() {
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
            if(asset_history_list1.size() > 0 && asset_history_list2.size() > 0)
            {
                for (int i = 0; i < asset_history_list1.size(); i++)
                {
                    History element1 = asset_history_list1.get(i);
                    dateList.add(element1.getTime());
                    rate1.add(element1.getRate());

                }
                for (int j = 0; j < asset_history_list2.size(); j++)
                {
                    History element2 = asset_history_list2.get(j);
                    rate2.add(element2.getRate());
                }
                /*for (int i = 0; i < asset_history_list1.size();)
                {
                    History element1 = asset_history_list1.get(i);
                    for (int j = 0; j < asset_history_list2.size(); j++)
                    {
                        History element2 = asset_history_list2.get(j);
                        Date date22 = element1.getTime();
                        Date date33 = element1.getTime();
                        long milliseconds = date22.getTime() - date33.getTime();
                        //System.out.println("\nРазница между датами в миллисекундах: " + milliseconds);

                        // 1000 миллисекунд = 1 секунда
                        int seconds = (int) (milliseconds / (1000));
                        //System.out.println("Разница между датами в секундах: " + seconds);

                        // 60 000 миллисекунд = 60 секунд = 1 минута
                        int minutes = (int) (milliseconds / (60 * 1000));
                        //System.out.println("Разница между датами в минутах: " + minutes);

                        // 3 600 секунд = 60 минут = 1 час
                        int hours = (int) (milliseconds / (60 * 60 * 1000));
                        //System.out.println("Разница между датами в часах: " + hours);

                        // 24 часа = 1 440 минут = 1 день
                        int days = (int) (milliseconds / (24 * 60 * 60 * 1000));
                        //System.out.println("Разница между датами в днях: " + days);

                        if (days <=3)
                        {
                            dateList.add(element1.getTime());
                            rate1.add(element1.getRate());
                            rate2.add(element2.getRate());

                       }
                    }
                }*/
            }
            model.addAttribute("rate1", rate1);
            model.addAttribute("rate2", rate2);
            model.addAttribute("dateTwoCurr", dateList);
            model.addAttribute("assetsNames2", assetsNames);

        }
        return "mainPage";
    }

    //Анализ правильности покупки на один месяц
    @GetMapping("/purhcaseCorrect")
    public String purhcaseCorrect(@RequestParam String datePurchase,@RequestParam String asset_id_base, Model model) throws ParseException {
        if(Objects.equals("", asset_id_base) || Objects.equals("", datePurchase))
            return "mainPage";
        datePurchase += "T00:00:00.0000000Z";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'");
        Date date = format.parse(datePurchase);
        //System.out.println(date);

        Date curr_date = new Date();
        List<History> historyList = historyService.getAllHist();
        List<String> finalResult = new ArrayList<String>();
        List<String> rateTopPurchases = new ArrayList<String>();
        List<String> dateTopPurchases = new ArrayList<String>();
        List<Currency> finalTop = new ArrayList<Currency>();
        List<String> names = new ArrayList<String>();
        names.add(asset_id_base);

        Map treeMap = new TreeMap<>();

        double trend = 0;
        double maxProfit = 0;
        double minProfit = 0;
        double maxLoss = 0;
        double minLoss = 0;
        double maxProfitPercent = 0;
        double minProfitPercent = 0;
        double maxLossPercent = 0;
        double minLossPercent = 0;
        Date maxProfitDate = new Date();
        Date minProfitDate = new Date();
        Date maxLossDate = new Date();
        Date minLossDate = new Date();
        if(historyList.size() > 0)
        {
            List<History> asset_history_list = new ArrayList<History>();
            Date newDate = curr_date;
            //newDate.setMonth(date.getMonth() - 1);
            for (int i = 0; i < historyList.size(); i++)
            {
                History element = historyList.get(i);
                if(Objects.equals(element.getAsset_id_base(), asset_id_base))
                {
                    Date date22 = element.getTime();
                    if (date22.after(date))
                    {
                        if (date22.before(newDate))
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
            if(asset_history_list.size() > 0) {
                double start_point = asset_history_list.get(0).getRate();
                for (int i = 1; i < asset_history_list.size(); i++) {
                    double element = asset_history_list.get(i).getRate();
                    //rates.add((element/start_point)*100 - 100);
                    double currPercent = ((element - start_point) / start_point) * 100;

                    trend += currPercent;
                    finalTop.add(new Currency((start_point * currPercent - start_point), asset_history_list.get(i).getTime()));

                    if (currPercent > 0) {
                        if (maxProfitPercent < currPercent) {
                            maxProfitPercent = currPercent;
                            maxProfit = start_point * maxProfitPercent - start_point;
                            maxProfitDate = asset_history_list.get(i).getTime();
                        }
                        if (minProfitPercent > currPercent) {
                            minProfitPercent = currPercent;
                            minProfit = start_point * minProfitPercent - start_point;
                            minProfitDate = asset_history_list.get(i).getTime();
                        }
                    } else if (currPercent < 0) {
                        if (maxLossPercent > currPercent) {
                            maxLossPercent = currPercent;
                            maxLoss = start_point * maxLossPercent - start_point;
                            maxLossDate = asset_history_list.get(i).getTime();
                        }
                        if (minLossPercent < currPercent) {
                            minLossPercent = currPercent;
                            minLoss = start_point * minLossPercent - start_point;
                            minLossDate = asset_history_list.get(i).getTime();
                        }
                    }

                }
                trend = trend / (asset_history_list.size() - 1);
                finalResult.add("Максимальная прибыль: " + maxProfit + " на срок продажи: " + maxProfitDate);
                //finalResult.add("Минимальная прибыль: " + minProfit + " на срок продажи: " + minProfitDate);
                finalResult.add("Максимальный убыток: " + maxLoss + " на срок продажи: " + maxLossDate);
                //finalResult.add("Минимальный убыток: " + minLoss + " на срок продажи: " + minLossDate);
                model.addAttribute("purchaseCorrectList", finalResult);
            }
        }

        model.addAttribute("rateTopPurchases", finalTop);
        model.addAttribute("namePurchase", names);
        return "mainPage";
    }

    //Анализ тренда
    @GetMapping("/trendAnalys")
    public String trendAnalys(@RequestParam String dateAnalys,@RequestParam String asset_id_base, Model model) throws ParseException {
        if(Objects.equals("", asset_id_base) || Objects.equals("", dateAnalys) )
            return "mainPage";
        dateAnalys += "T00:00:00.0000000Z";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'");
        Date date = format.parse(dateAnalys);
        List<History> historyList = historyService.getAllHist();
        List<String> finalResult = new ArrayList<String>();
        List<String> names = new ArrayList<String>();
        names.add(asset_id_base);
        double trend = 0;
        if(historyList.size() > 0)
        {
            List<History> asset_history_list = new ArrayList<History>();
            Date newDate = new Date();
            newDate.setMonth(date.getMonth() - 1);
            for (int i = 0; i < historyList.size(); i++)
            {
                History element = historyList.get(i);
                if(Objects.equals(element.getAsset_id_base(), asset_id_base))
                {
                    Date date22 = element.getTime();
                    if (date22.after(date))
                    {
                        if (date22.before(newDate))
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
            if(asset_history_list.size() > 0) {
                double start_point = asset_history_list.get(0).getRate();
                for (int i = 1; i < asset_history_list.size(); i++) {

                    double element = asset_history_list.get(i).getRate();
                    //rates.add((element/start_point)*100 - 100);
                    trend += ((element - start_point) / start_point) * 100;;

                }
                trend = trend / (asset_history_list.size() - 1);
            }
        }
        if(trend > 0)
        {
            finalResult.add("Валюта в тренде");
        }
        else
        {
            finalResult.add("Валюта не в тренде");
        }
        model.addAttribute("trend", finalResult);
        model.addAttribute("namesTrend", names);
        return "mainPage";
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
