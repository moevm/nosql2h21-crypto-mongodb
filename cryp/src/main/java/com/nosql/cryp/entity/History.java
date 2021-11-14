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
    private String asset_id_base;

    //@Id
    private Date time;

    private String asset_id_quote;

    private double rate;

    public History(JSONObject curr) throws JSONException, ParseException {

        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'");

        this.asset_id_base = (String) curr.get("asset_id_base");

        if (curr.has("time"))
            this.time = simpleDateFormat.parse((String) curr.get("time"));

        if (curr.has("asset_id_quote"))
            this.asset_id_quote = (String) curr.get("asset_id_quote");

        if(curr.has("rate"))
            this.rate = curr.getDouble("rate");
    }
}
