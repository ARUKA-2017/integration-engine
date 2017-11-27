package com.akura.adaptor.resolver;

import com.akura.logger.FileLogger;
import com.akura.mapping.service.MappingService;

import com.google.gson.Gson;
import spark.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.HashMap;

/**
 * Class represernting an EntityNameResolver.
 */
public class EntityNameResolver {

    public static HashMap<String, String> device_registry = new HashMap();

    /**
     * Method used to get the relevant mobile name.
     *
     * @param name - search key.
     * @return - correct mobile name.
     */
    public static String getMobileName(String name) {

        FileLogger.Log("Resolving name for: " + name, FileLogger.TYPE_SUB, FileLogger.DEST_INTEGRATION);


        if (device_registry.get(name) != null) {
            FileLogger.Log("Resolved: " + device_registry.get(name), FileLogger.TYPE_SUB, FileLogger.DEST_INTEGRATION);
            return device_registry.get(name);
        }

        URL url = null;

        try {
            String search = name;
            url = new URL("http://35.198.251.53:3002/phone_name/" + URLEncoder.encode(name, "UTF-8"));
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

            if (status == 200) {
                name = content.toString();
                FileLogger.Log("Resolved: " + name, FileLogger.TYPE_SUB, FileLogger.DEST_INTEGRATION);
                device_registry.put(search, name);
            } else {
                FileLogger.Log("Resolve Failed for: " + name, FileLogger.TYPE_SUB, FileLogger.DEST_INTEGRATION);
                name = null;
            }
            in.close();

        } catch (MalformedURLException e) {
            FileLogger.Log("Resolve Failed for: " + name, FileLogger.TYPE_SUB, FileLogger.DEST_INTEGRATION);
            name = null;
            e.printStackTrace();
        } catch (ProtocolException e) {
            FileLogger.Log("Resolve Failed for: " + name, FileLogger.TYPE_SUB, FileLogger.DEST_INTEGRATION);
            name = null;
            e.printStackTrace();
        } catch (IOException e) {
            FileLogger.Log("Resolve Failed for: " + name, FileLogger.TYPE_SUB, FileLogger.DEST_INTEGRATION);
            name = null;
            e.printStackTrace();
        }

        return name;
    }

    /**
     * Method used to resolve the url for name.
     *
     * @param name - name of the entity.
     * @return - URL.
     */
    public static String urlNameResolve(String name) {


        System.out.println("URL Name Resolver for " + name);
        String address = null;
        URL url = null;

        try {
            url = new URL("http://35.202.18.187:5000/amazon_url/" + URLEncoder.encode(name, "UTF-8"));
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

            if (status == 200) {
                address = content.toString();
            } else {
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

    /**
     * Method used to extract data and resolve,
     *
     * @param name - name of the entity.
     * @param res  - response of the request.
     * @param mp   - mapping service.
     */
    public static void dataExtractionResolve(String name, Response res, MappingService mp) {

        FileLogger.Log("Starting Data Extraction from akrua.com.integration-engine", FileLogger.TYPE_TITLE, FileLogger.DEST_RETRIEVAL);
        FileLogger.Log("Retrieving  Amazon URL for Product: " + name, FileLogger.TYPE_SUB, FileLogger.DEST_RETRIEVAL);
        String amazon = urlNameResolve(name).replaceAll("\"", "");
        FileLogger.Log(amazon, FileLogger.TYPE_CONT, FileLogger.DEST_RETRIEVAL);
        try {
            mp.mongoLoaderSingleEntity(name, res);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (amazon != null) {
            URL url = null;

            try {
                FileLogger.Log("Requesting Data Extraction from NLU Engine", FileLogger.TYPE_TITLE, FileLogger.DEST_RETRIEVAL);
                FileLogger.Log("http://35.198.251.53:4568/extract-review?search=" + URLEncoder.encode(name, "UTF-8")
                        + "&url=" + URLEncoder.encode(amazon, "UTF-8"), FileLogger.TYPE_SUB, FileLogger.DEST_RETRIEVAL);


                url = new URL("http://35.198.251.53:4568/extract-review?search=" + URLEncoder.encode(name, "UTF-8")
                        + "&url=" + URLEncoder.encode(amazon, "UTF-8"));

                System.out.println("start sending extract request");

                System.out.println("http://35.198.251.53:4568/extract-review?search=" + URLEncoder.encode(name, "UTF-8")
                        + "&url=" + URLEncoder.encode(amazon, "UTF-8"));
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Content-Type", "application/json");
                String contentType = con.getHeaderField("Content-Type");
                con.setConnectTimeout(150000);
                con.setReadTimeout(150000);

                System.out.println("start sending extract request");
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

                if (status == 200) {

                    System.out.println("Extraction Success for: " + name);
                    FileLogger.Log("Data Extracton Success from NLU for: " + name, FileLogger.TYPE_SUB, FileLogger.DEST_RETRIEVAL);
                    FileLogger.Log(content.toString(), FileLogger.TYPE_JSON, FileLogger.DEST_RETRIEVAL);


                    System.out.println("Starting Mapping");

                    mp.useAdaptor(content.toString(), res);


                } else {
                    FileLogger.Log("Data Extracton Failed from NLU for: " + name, FileLogger.TYPE_SUB, FileLogger.DEST_RETRIEVAL);
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
