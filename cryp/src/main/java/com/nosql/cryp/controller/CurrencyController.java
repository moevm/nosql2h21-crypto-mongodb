package com.nosql.cryp.controller;

import com.nosql.cryp.ApiToDb;
import com.nosql.cryp.entity.Currency;
import com.nosql.cryp.entity.History;
import com.nosql.cryp.service.CurrencyService;
import com.nosql.cryp.service.HistoryService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.fasterxml.jackson.databind.type.LogicalType.Collection;

@Controller
@RequestMapping("/currency")
public class CurrencyController {
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private HistoryService historyService;
    //по цене
    @GetMapping("/getCurrencyByPrice")
    public String getCurrencyByPrice(@RequestParam String rateMin, @RequestParam String rateMax, Model model){
        double rateMinimum = Double.valueOf(rateMin);
        double rateMaximum = Double.valueOf(rateMax);
        List<Currency> currencies = currencyService.getAllCurr();
        if (currencies.size() > 0){
            List<Currency> newCurrncies = new ArrayList<Currency>();
            for (int i = 0 ; i < currencies.size(); i++) {
                Currency element = currencies.get(i);
                if(element.getPrice_usd() >= rateMinimum && element.getPrice_usd() <= rateMaximum) {
                    newCurrncies.add(element);
                }
            }
            model.addAttribute("currencies", newCurrncies);
            return "main";
        }
        else{
            return "error";
        }
    }
    //по времени
    @GetMapping("/getCurrencyByDate")
    public String getCurrencyByDate(@RequestParam String dateMin, @RequestParam String dateMax,  Model model) throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date parsedDateMin = format.parse(dateMin);
        Date parsedDateMax = format.parse(dateMax);
        List<Currency> currencies = currencyService.getAllCurr();
        if (currencies.size() > 0){
            List<Currency> newCurrncies = new ArrayList<Currency>();
            for (int i = 0 ; i < currencies.size(); i++) {
                Currency element = currencies.get(i);
                if(element.getData_end() != null)
                {
                    if(element.getData_end().after(parsedDateMin))
                    {
                        if(element.getData_end().before(parsedDateMax))
                        {
                            newCurrncies.add(element);
                        }
                    }
                }
            }
            model.addAttribute("currencies", newCurrncies);
            return "main";
        }
        else{
            return "error";
        }
    }


    @GetMapping("/name_filter")
    public String getCurrencyNameFilter(Model model){
        List<Currency> currencies = currencyService.getAllCurr();
        if (currencies.size() > 0){
            Collections.sort(currencies, new Comparator<Currency>() {
                @Override
                public int compare(Currency o1, Currency o2) {
                    return o1.getAsset_id().compareTo(o2.getAsset_id());
                }
            });
            model.addAttribute("currencies", currencies);
            return "main";
        }
        else{
            return "error";
        }
    }

    @GetMapping("/get_currency_jsons")
    public String getCurrencyJsons(Model model) throws JSONException {
        List<Currency> currencies = currencyService.getAllCurr();
        String  result = new String();
        for (int i = 0 ; i < currencies.size(); i++) {
            Currency element = currencies.get(i);
            result += element.getCurrencyJSONOnject();
            result += '\n';
        }
        model.addAttribute("result", result);
        return "DBImport";
    }

    @GetMapping("/time_filter")
    public String getCurrencyTimeFilter(Model model){
        List<Currency> currencies = currencyService.getAllCurr();
        if (currencies.size() > 0){
            Collections.sort(currencies, new Comparator<Currency>() {
                @Override
                public int compare(Currency o1, Currency o2) {
                    if(o1.getData_end() != null && o2.getData_end() != null){
                        if(o1.getData_end().after(o2.getData_end()))
                            return -1;
                        if(o1.getData_end().equals(o2.getData_end()))
                            return 0;
                        if(o1.getData_end().before(o2.getData_end()))
                            return 1;
                    }
                    return 1;
                }
            });
            model.addAttribute("currencies", currencies);
            return "main";
        }
        else{
            return "error";
        }
    }

    @GetMapping("/price_filter")
    public String getCurrencyPriceFilter(Model model){
        List<Currency> currencies = currencyService.getAllCurr();
        if (currencies.size() > 0){
            Collections.sort(currencies, new Comparator<Currency>() {
                @Override
                public int compare(Currency o1, Currency o2) {
                    if(o1.getPrice_usd() > o2.getPrice_usd())
                    return -1;
                    if(o1.getPrice_usd() == o2.getPrice_usd())
                        return 0;
                    if(o1.getPrice_usd() < o2.getPrice_usd())
                        return 1;
                    return 1;
                }
            });
            model.addAttribute("currencies", currencies);
            return "main";
        }
        else{
            return "error";
        }
    }

    @GetMapping("/getCurrencyByName")
    public String getCurrencyByName(@RequestParam String text,  Model model){
        List<Currency> currencies = currencyService.getAllCurr();
        if (currencies.size() > 0){
            System.out.println(text);
            // удаление из списка валют, которые не соответствуют переданному имени
            currencies.removeIf(e -> ((e.getAsset_id()).indexOf(text)) == -1);
            model.addAttribute("currencies", currencies);
            return "main";
        }
        else{
            return "error";
        }
    }
    //Конвертер
    @GetMapping("/currConvert")
    public String currConvert(@RequestParam String curr1, @RequestParam String curr2,@RequestParam String n1, Model model)
    {
        int n = Integer.parseInt(n1);
        if(Objects.equals("", curr1) || Objects.equals("", curr2))
            return "mainPage";
        List<Currency> assets = currencyService.getAllCurr();
        List<Currency> assetCurrency1 = new ArrayList<Currency>();
        List<Currency> assetCurrency2 = new ArrayList<Currency>();
        List<String> finalresult = new ArrayList<String>();
        List<String> names = new ArrayList<String>();
        names.add(curr1);
        names.add(curr2);
        names.add(n1);
        if(assets.size() > 0)
        {
            for(int i = 0; i < assets.size(); i++)
            {
                Currency element = assets.get(i);
                if(Objects.equals(element.getAsset_id(), curr1))
                {
                    assetCurrency1.add(element);
                }
                if(Objects.equals(element.getAsset_id(), curr2))
                {
                    assetCurrency2.add(element);
                }
            }
            if(assetCurrency1.size() > 0 && assetCurrency2.size() > 0)
            {
                Currency currency1 = assetCurrency1.get(0);
                Currency currency2 = assetCurrency2.get(0);
                double price1 = currency1.getPrice_usd();
                double price2 = currency2.getPrice_usd();

                double result = (n * price1) / price2;
                String total2 = String.valueOf(result);
                finalresult.add(total2);
                model.addAttribute("converted", finalresult);
            }
        }
        model.addAttribute("namesConv", names);
        return "mainPage";
    }

    //Получение списка asset_id для выпадающего списка
    @GetMapping("/getListAsset_id")
    public String getListAsset_id(Model model)
    {
        List<Currency> currencies = currencyService.getAllCurr();
        List<String> listAsset_id = new ArrayList<String>();
        for (int i = 0 ; i < 10; i++) {
            Currency element = currencies.get(i);
            listAsset_id.add(element.getAsset_id());
        }
        //System.out.println(listAsset_id);
        if (currencies.size() > 0){
            model.addAttribute("listAsset_id", listAsset_id);
            return "mainPage";
        }
        return "mainPage";
    }

    @GetMapping("/test")
    public String testFund(){
        return "This is a test string";
    }

    @GetMapping("/apiFull")
    public String currencyFromApi() throws JSONException, IOException, URISyntaxException, ParseException {
            // запросы к апи
        ApiToDb apitodb = new ApiToDb();
        JSONArray curr = apitodb.list_all_assets();
        for (int i = 0 ; i < 100; i++) {
            JSONObject obj = curr.getJSONObject(i);
            if (((int) obj.get("type_is_crypto") == 0))
                continue;
            if(obj.has("price_usd"))
            {
                if(((int) obj.getDouble("price_usd")) <= 0)
                    continue;
            }
            else
            {
                continue;
            }

            Currency currency =  new Currency(obj);
            /*JSONArray curr2 = apitodb.list_all_history(currency.getAsset_id(), new Date(), new Date());
            for (int j = 0; j < curr2.length(); j++) {
                JSONObject obj2 = curr2.getJSONObject(j);

                History history = new History(obj2, currency.getAsset_id());
                historyService.saveHistory(history);

            }/*
            //apitodb.list_all_history(currency.getAsset_id(), new Date(), new Date());
            currencyService.saveCurrency(currency);
            /*String asset_id = (String) obj.get("asset_id");
            List<Currency> assetCurrency = currencyService.getByAsset_id(asset_id);
            if(assetCurrency.isEmpty())
            {
                Currency currency =  new Currency(obj);
                currencyService.saveCurrency(currency);
            }
            else
            {
                Currency currency =  new Currency(obj);
                currencyService.updateCurrencyByAssetId(currency, asset_id);
            }*/

        }
        return "redirect:/currency/getAllCurr";
    }
    @PostMapping("/new")
    public String saveCurrency(@RequestBody Currency newCurrency){
        currencyService.saveCurrency(newCurrency);
        return "Succesfully added currency: " + newCurrency.toString();

    }

    @PostMapping("/save")
    public String saveCurrencyHtml(Currency newCurrency){
        Date curr_date = new Date();
        newCurrency.setTime(curr_date);
        currencyService.saveCurrency(newCurrency);
        //System.out.println("rate: " + newCurrency.getRate());
        /*for (int i = 0; i < currencyService.getAllHist().size(); i++){
            System.out.println(historyService.getAllHist().get(i).toString());;
        }*/
        return "redirect:/currency/getAllCurr";
    }

    @GetMapping("/getAllCurr")
    //public List<Currency> getCurrencies(){
    public String getAllCurrencies(Model model){
        List<Currency> currencies = currencyService.getAllCurr();
        if (currencies.size() > 0){
            model.addAttribute("currencies", currencies);
            return "main";
        }
        else{
            return "error";
        }
    }

    @GetMapping("/getByAssetId/{asset_id}")
    public String getCurrencyList(@PathVariable String asset_id, Model model){
        List<Currency> assetCurrency = currencyService.getByAsset_id(asset_id);
        model.addAttribute(assetCurrency);
        /*System.out.println("asset_id: " + asset_id);
        for (int i = 0; i < assetCurrency.size(); i++){
            System.out.println(assetCurrency.get(i).toString());
        }*/
        return "main";
    }


}
