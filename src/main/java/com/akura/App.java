package com.akura;

import com.akura.mapping.service.MappingService;
import com.akura.parser.Parser;
import com.akura.retrieval.service.ProductService;
import com.akura.retrieval.service.SparkMiddleware;
import com.google.gson.Gson;
import spark.ModelAndView;
import spark.Spark;
import spark.template.jade.JadeTemplateEngine;

import java.util.HashMap;

import static spark.Spark.get;
import static spark.Spark.post;

public class App {


    public static void main(String[] args) {
        Gson gson = new Gson();
        ProductService ps = new ProductService();
        MappingService mp = new MappingService();
        Spark.staticFileLocation("/public");


        get("/json2owl", (req, res) ->new ModelAndView(new HashMap<>(), "j2owl"), new JadeTemplateEngine());

        SparkMiddleware.enableCORS("*", "POST, GET, OPTIONS, PUT, DELETE",
                "Content-Type, x-xsrf-token, content-Type, X-Auth-Token, Origin, Authorization");

        get("/search", (req, res) -> gson.toJson(ps.searchProduct(req.queryParams("search"), res, false, mp)));
        post("/generate-vowl", (req,res)->{
           String uid =  Parser.parseFromJsonString(req.body());
            return  uid;
        });

        //TODO Get suggestions upon keystroke type endpoint

        get("/search/:id", (req, res) -> gson.toJson(ps.searchProduct(req.params(":id"), res, true, mp)));

        post("/update-ontology", (req, res) -> gson.toJson(mp.map(req.body(), res)));
        post("/update-ontology-adaptor", (req, res) -> gson.toJson(mp.useAdaptor(req.body(), res)));

//        get("/test", (req, res) -> mp.mongoLoader(res));



    }
}
