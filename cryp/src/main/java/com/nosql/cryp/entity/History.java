package com.nosql.cryp.entity;

import lombok.*;
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
@Document(collection="History")
public class History {

    //@Id
    private String asset_id_base;

    //@Id
    private Date time;

    private String asset_id_quote;

    private double rate;

    private double rate_low;
    private double rate_start;
    private double rate_end;

    public History(JSONObject curr) throws JSONException, ParseException {

        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'");

        this.asset_id_base = (String) curr.get("asset_id_base");

        if (curr.has("time"))
            this.time = simpleDateFormat.parse((String) curr.get("time"));

        if (curr.has("asset_id_quote"))
            this.asset_id_quote = (String) curr.get("asset_id_quote");

        if(curr.has("rate"))
            this.rate = curr.getDouble("rate");

        if(curr.has("rate_high"))
            this.rate = curr.getDouble("rate_high");

        if(curr.has("rate_open"))
            this.rate_start = curr.getDouble("rate_open");

        if(curr.has("rate_low"))
            this.rate_low = curr.getDouble("rate_low");

        if(curr.has("rate_close"))
            this.rate_end = curr.getDouble("rate_close");
    }

    public History(JSONObject curr, String asset_id_base) throws JSONException, ParseException {

        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'");

        this.asset_id_base = asset_id_base;

        if (curr.has("time_period_end"))
            this.time = simpleDateFormat.parse((String) curr.get("time_period_end"));

        if(curr.has("rate_high"))
            this.rate = curr.getDouble("rate_high");

        if(curr.has("rate_open"))
            this.rate_start = curr.getDouble("rate_open");

        if(curr.has("rate_low"))
            this.rate_low = curr.getDouble("rate_low");

        if(curr.has("rate_close"))
            this.rate_end = curr.getDouble("rate_close");
    }

}
