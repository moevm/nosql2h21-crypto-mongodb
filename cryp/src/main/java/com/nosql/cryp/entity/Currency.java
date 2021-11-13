package com.nosql.cryp.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="Currency")
public class Currency {
    //@GeneratedValue (strategy = GenerationType.IDENTITY)
    /*@Id
    private int id;*/
    @Id
    private String asset_id;

    private String name;

    private Date data_start;
    private Date data_end;

    private Date data_quote_start;
    private Date data_quote_end;

    private Date data_orderbook_start;
    private Date data_orderbook_end;

    private Date data_trade_start;
    private Date data_trade_end;

    private int data_symbols_count;

    private double volume_1hrs_usd;
    private double volume_1day_usd;
    private double volume_1mth_usd;

    private double price_usd;

    private History currHistory;


    public Currency(JSONObject curr) throws JSONException, ParseException {

        DateFormat simpleDateFormat_1 = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat simpleDateFormat_2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'");

        this.asset_id = (String) curr.get("asset_id");

        this.name= (String) curr.get("name");

        if(curr.has("data_start"))
            this.data_start = simpleDateFormat_1.parse((String) curr.get("data_start"));
        if(curr.has("data_end"))
            this.data_end  = simpleDateFormat_1.parse((String) curr.get("data_end"));

        if(curr.has("data_quote_start"))
            this.data_quote_start = simpleDateFormat_2.parse((String) curr.get("data_quote_start"));
        if(curr.has("data_quote_end"))
            this.data_quote_end = simpleDateFormat_2.parse((String) curr.get("data_quote_end"));

        if(curr.has("data_orderbook_start"))
            this.data_orderbook_start = simpleDateFormat_2.parse((String) curr.get("data_orderbook_start"));
        if(curr.has("data_orderbook_end"))
            this.data_orderbook_end  = simpleDateFormat_2.parse((String) curr.get("data_orderbook_end"));

        if(curr.has("data_trade_start"))
            this.data_trade_start = simpleDateFormat_2.parse((String) curr.get("data_trade_start"));
        if(curr.has("data_trade_end"))
            this.data_trade_end  = simpleDateFormat_2.parse((String) curr.get("data_trade_end"));

        if(curr.has("data_symbols_count"))
            this.data_symbols_count = curr.getInt("data_symbols_count");

        if(curr.has("volume_1hrs_usd"))
            this.volume_1hrs_usd = curr.getDouble("volume_1hrs_usd");
        if(curr.has("volume_1day_usd"))
            this.volume_1day_usd = curr.getDouble("volume_1day_usd");
        if(curr.has("volume_1mth_usd"))
            this.volume_1mth_usd = curr.getDouble("volume_1mth_usd");

        if(curr.has("price_usd"))
            this.price_usd = curr.getDouble("price_usd");

    }
/*
    public Currency(String asset_id, Date data_end, double price_usd){
        this.asset_id = asset_id;
        this.data_end = data_end;
        this.price_usd = price_usd;
    }

 */
}
