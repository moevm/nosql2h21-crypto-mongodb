package com.nosql.cryp;


import com.nosql.cryp.entity.Currency;

import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.Properties;

import com.nosql.cryp.entity.History;
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
import org.json.JSONObject;
import org.json.JSONTokener;

public class ApiToDb {

    /*public static final String PATH_TO_PROPERTIES = "src/main/resources/TextConstants.properties";
    FileInputStream fileInputStream;
    Properties prop = new Properties();

    {
        try {
            prop.load(new FileInputStream(PATH_TO_PROPERTIES));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    String key_api = "9D62D7F1-C92D-4615-BC53-BF7BC0B78399";//prop.getProperty("key_api");

    public JSONArray list_all_assets() throws IOException, URISyntaxException, JSONException {
        //Currency

        //String url = prop.getProperty("cUrl_list_all_assets");

        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("http://rest.coinapi.io/v1/assets");
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
        //System.out.println(finalResult);

        return finalResult;
    }

    public JSONArray list_all_history(String asset_id_base, Date date1, Date date2) throws IOException, JSONException {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        String cUrl = "http://rest.coinapi.io/v1/exchangerate/" + asset_id_base +
                "/USD/history?period_id=7DAY&time_start=2019-01-01T00:00:00&time_end=2021-01-01T00:00:00";


        //HttpGet httpGet = new HttpGet(cUrl);
        HttpGet httpGet = new HttpGet(cUrl);
        httpGet.setHeader("X-CoinAPI-Key" , key_api);
        HttpResponse response = httpclient.execute(httpGet);

        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
        StringBuilder builder = new StringBuilder();
        for (String line = null; (line = reader.readLine()) != null;) {
            builder.append(line).append("\n");
        }
        JSONTokener tokener = new JSONTokener(builder.toString());
        JSONArray finalResult = new JSONArray(tokener);
        //System.out.println(finalResult);


        return finalResult;
    }



}
