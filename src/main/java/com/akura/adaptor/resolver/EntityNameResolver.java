package com.akura.adaptor.resolver;

import com.akura.mapping.service.MappingService;
import spark.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class EntityNameResolver {

    public static String getMobileName(String name){

        System.out.println("EntityNameResolver for "+ name);
        URL url = null;

        try {
            url = new URL("http://35.198.251.53:3002/phone_name/"+  URLEncoder.encode(name, "UTF-8"));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            String contentType = con.getHeaderField("Content-Type");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);

            int status = con.getResponseCode();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            System.out.println(status);
            System.out.println(content.toString());

            if(status == 200){
                name = content.toString();
            }else{
                name = null;
            }
            in.close();



        } catch (MalformedURLException e) {
            name = null;
            e.printStackTrace();
        } catch (ProtocolException e) {
            name = null;
            e.printStackTrace();
        } catch (IOException e) {
            name = null;
            e.printStackTrace();
        }

        return name;
    }

    public static String urlNameResolve(String name){

        System.out.println("URL Name Resolver for "+ name);
        String address = null;
        URL url = null;

        try {
            url = new URL("http://35.198.251.53:5000/amazon_url/"+  URLEncoder.encode(name, "UTF-8"));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            String contentType = con.getHeaderField("Content-Type");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);

            int status = con.getResponseCode();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            System.out.println(status);
            System.out.println(content.toString());

            if(status == 200){
                address = content.toString();
            }else{
                address = null;
            }
            in.close();



        } catch (MalformedURLException e) {
            address = null;
            e.printStackTrace();
        } catch (ProtocolException e) {
            address = null;
            e.printStackTrace();
        } catch (IOException e) {
            address = null;
            e.printStackTrace();
        }

        return address;
    }

    public static void dataExtractionResolve(String name, Response res, MappingService mp){

        String amazon = urlNameResolve(name).replaceAll("\"","");

        if(amazon != null){
            URL url = null;

            try {
                url = new URL("http://35.198.251.53:4568/extract-review?search="+  URLEncoder.encode(name, "UTF-8")
                        +"&url="+ URLEncoder.encode(amazon, "UTF-8") );
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Content-Type", "application/json");
                String contentType = con.getHeaderField("Content-Type");
                con.setConnectTimeout(5000);
                con.setReadTimeout(5000);

                int status = con.getResponseCode();
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                System.out.println(status);
                System.out.println(content.toString());

                if(status == 200){

                    System.out.println("Extraction Success for: " + name);
                    if(content.toString() != "null") {
                        //TODO: check this integration code here
                        mp.useAdaptor(content.toString(), res);
                    }

                }else{
                    System.out.println("Extraction Failed for: " + name);
                }
                in.close();



            } catch (MalformedURLException e) {

                e.printStackTrace();
            } catch (ProtocolException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }

        }
    }
}
