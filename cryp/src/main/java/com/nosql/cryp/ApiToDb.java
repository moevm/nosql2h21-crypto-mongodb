package com.nosql.cryp;


import com.nosql.cryp.entity.Currency;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.io.FileInputStream;

public class ApiToDb {

    public static final String PATH_TO_PROPERTIES = "src/main/resources/TextConstants.properties";
    FileInputStream fileInputStream;
    Properties prop = new Properties();

    {
        try {
            prop.load(new FileInputStream(PATH_TO_PROPERTIES));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    String api_key = prop.getProperty("api_key");

    public void list_all_assets()
    {
        Currency
    }

}
