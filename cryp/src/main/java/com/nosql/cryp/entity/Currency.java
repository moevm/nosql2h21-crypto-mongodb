package com.nosql.cryp.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="Currency")
public class Currency {
    @Id
    private int id;

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
}
