package com.nosql.cryp;


import com.nosql.cryp.entity.Currency;

import java.io.*;
import java.net.*;
import java.util.Properties;

import jdk.nashorn.internal.parser.JSONParser;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

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

    public JSONArray list_all_assets() throws IOException, URISyntaxException, JSONException {
        //Currency

        //String url = prop.getProperty("cUrl_list_all_assets");

        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("http://rest.coinapi.io/v1/assets/BTC");
        httpGet.setHeader("X-CoinAPI-Key" , key_api);
        HttpResponse response = httpclient.execute(httpGet);

        //System.out.println(response.toString());

        /*BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String line = "";
        while ((line = rd.readLine()) != null) {
            System.out.println(line);
        }*/

        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
        StringBuilder builder = new StringBuilder();
        for (String line = null; (line = reader.readLine()) != null;) {
            builder.append(line).append("\n");
        }
        JSONTokener tokener = new JSONTokener(builder.toString());
        JSONArray finalResult = new JSONArray(tokener);
        System.out.println(finalResult);

        return finalResult;
    }

}
