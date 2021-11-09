package com.nosql.cryp;


import com.nosql.cryp.entity.Currency;

import java.io.*;
import java.net.*;
import java.util.Properties;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

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

    public void list_all_assets() throws IOException, URISyntaxException {
        //Currency

        //String url = prop.getProperty("cUrl_list_all_assets");

        DefaultHttpClient httpclient = new DefaultHttpClient();

        HttpGet httpGet = new HttpGet("http://rest.coinapi.io/v1/assets/BTC");

        httpGet.setHeader("X-CoinAPI-Key" , "9D62D7F1-C92D-4615-BC53-BF7BC0B78399");

        HttpResponse response = httpclient.execute(httpGet);

        //System.out.println(response.toString());

        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String line = "";
        while ((line = rd.readLine()) != null) {
            System.out.println(line);
        }
    }

}
