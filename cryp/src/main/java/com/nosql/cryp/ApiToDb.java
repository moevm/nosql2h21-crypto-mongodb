package com.nosql.cryp;


import com.nosql.cryp.entity.Currency;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

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


    String key_api = prop.getProperty("key_api");

    public void list_all_assets() throws IOException {
        //Currency
        System.out.println(key_api);
        String url = "http://www.google.com/";

        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

        connection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println(response.toString());
    }

}
