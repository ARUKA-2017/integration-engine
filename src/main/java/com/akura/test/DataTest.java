package com.akura.test;

import com.akura.mapping.service.MappingService;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import spark.Response;

import java.io.File;
import java.io.InputStream;

public class DataTest {

    public static void main(String[] args) {

//        String url = "http://localhost:4567/update-ontology";
//        HttpClient client = HttpClientBuilder.create().build();
//        HttpPost postRequest = new HttpPost(url);
        MappingService mp = new MappingService();

//        HttpResponse response;

        File dir = new File("phone_dumps");
        File[] files = dir.listFiles((dir1, name) -> name.endsWith(".json"));

      for(File file: files) {

            try {
                String json = FileUtils.readFileToString(file);
                System.out.println(json);

     //           mp.map(json, null);

//                StringEntity se = new StringEntity(json);
//                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
//                postRequest.setEntity(se);

//                response = client.execute(postRequest);

//                System.out.println(response);

                 /*Checking response */
//                if (response != null) {
//                    InputStream in = response.getEntity().getContent(); //Get the data in the entity
//                }

            } catch(Exception e) {
                System.out.println(e);
            }
       }

    }

}
