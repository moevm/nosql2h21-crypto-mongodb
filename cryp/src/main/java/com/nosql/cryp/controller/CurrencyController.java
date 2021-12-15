package com.nosql.cryp.controller;

import com.nosql.cryp.ApiToDb;
import com.nosql.cryp.entity.Currency;
import com.nosql.cryp.entity.History;
import com.nosql.cryp.service.CurrencyService;
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
import java.util.*;

import static com.fasterxml.jackson.databind.type.LogicalType.Collection;

@Controller
@RequestMapping("/currency")
public class CurrencyController {
    @Autowired
    private CurrencyService currencyService;
    //по цене
    @GetMapping("/getCurrencyByPrice")
    public String getCurrencyByPrice(@RequestParam double rateMin, @RequestParam double rateMax, Model model){
        List<Currency> currencies = currencyService.getAllCurr();
        if (currencies.size() > 0){
            List<Currency> newCurrncies = new ArrayList<Currency>();
            for (int i = 0 ; i < currencies.size(); i++) {
                Currency element = currencies.get(i);
                if(element.getPrice_usd() >= rateMin && element.getPrice_usd() <= rateMax) {
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
    public String getCurrencyRateFilter(Model model, int rateMax, int rateMin){
        List<Currency> currencies = currencyService.getAllCurr();
        if (currencies.size() > 0){
            List<Currency> newCurrncies = new ArrayList<Currency>();
            for (int i = 0 ; i < currencies.size(); i++) {
                Currency element = currencies.get(i);
                if(element.getPrice_usd() >= rateMin && element.getPrice_usd() <= rateMax) {
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
    public String getCurrencyByDate(@RequestParam Date dateMax, @RequestParam Date dateMin,  Model model){
        List<Currency> currencies = currencyService.getAllCurr();
        if (currencies.size() > 0){
            List<Currency> newCurrncies = new ArrayList<Currency>();
            for (int i = 0 ; i < currencies.size(); i++) {
                Currency element = currencies.get(i);
                if(element.getData_end() != null)
                {
                    if(element.getData_end().after(dateMin))
                    {
                        if(element.getData_end().before(dateMax))
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
    public String currConvert(String curr1, String curr2, int n1)
    {
        List<Currency> assetCurrency1 = currencyService.getByAsset_id(curr1);
        List<Currency> assetCurrency2 = currencyService.getByAsset_id(curr2);
        Currency currency1 = assetCurrency1.get(0);
        Currency currency2 = assetCurrency2.get(0);
        double price1 = currency1.getPrice_usd();
        double price2 = currency2.getPrice_usd();

        double result = (n1*price1)/price2;

        return String.valueOf(result);
    }

    //Получение списка asset_id для выпадающего списка
    public List<String> getListAsset_id()
    {
        List<Currency> currencies = currencyService.getAllCurr();
        List<String> listAsset_id = new ArrayList<String>();
        for (int i = 0 ; i < currencies.size(); i++) {
            Currency element = currencies.get(i);
            listAsset_id.add(element.getAsset_id());
        }
        return listAsset_id;
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
        for (int i = 0 ; i < curr.length(); i++) {
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
        System.out.println("rate: " + newCurrency.getRate());
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
