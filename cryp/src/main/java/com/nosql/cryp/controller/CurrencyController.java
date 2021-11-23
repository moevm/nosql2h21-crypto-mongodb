package com.nosql.cryp.controller;

import com.nosql.cryp.ApiToDb;
import com.nosql.cryp.entity.Currency;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.fasterxml.jackson.databind.type.LogicalType.Collection;

@Controller
@RequestMapping("/currency")
public class CurrencyController {
    @Autowired
    private CurrencyService currencyService;

    @GetMapping("/name_filter")
    public String getCurrencyNameFilter(Model model){
        List<Currency> currencies = currencyService.getAllCurr();
        if (currencies.size() > 0){
            Collections.sort(currencies, new Comparator<Currency>() {
                @Override
                public int compare(Currency o1, Currency o2) {
                    return o1.getAsset_id.compareTo(o2.getAsset_id);
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
                    return o1.getPrice_usd > o2.getPrice_usd;
                }
            });
            model.addAttribute("currencies", currencies);
            return "main";
        }
        else{
            return "error";
        }
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
