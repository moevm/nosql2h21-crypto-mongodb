package com.nosql.cryp;

import com.mongodb.*;
import org.json.JSONException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.net.UnknownHostException;

//@ComponentScan("com.nosql.cryp.service")
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Date;

import org.json.JSONArray;

@SpringBootApplication
public class CrypApplication {

    public static void main(String[] args) throws IOException, URISyntaxException, JSONException {


        SpringApplication.run(CrypApplication.class, args);

        //ApiToDb apiToDb = new ApiToDb();
        //apiToDb.list_all_history("BTC", new Date(), new Date());


    }

}
