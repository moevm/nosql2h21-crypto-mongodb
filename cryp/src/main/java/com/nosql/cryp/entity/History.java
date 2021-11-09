package com.nosql.cryp.entity;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="History")
public class History {
    @Id
    private int id;

    private Date time;

    private String asset_id_base;

    private String asset_id_quote;

    private double rate;
}
