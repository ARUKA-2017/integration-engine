package com.akura.retrieval;


import com.akura.mapping.service.MappingService;
import com.akura.retrieval.service.ProductService;
import com.google.gson.Gson;

 ;
import static spark.Spark.get;
import static spark.Spark.post;


public class App {
    public static void main(String[] args){
        Gson gson = new Gson();
        ProductService ps = new ProductService();
        MappingService mp = new MappingService();

        get("/search", (req, res) -> gson.toJson(ps.searchProduct(req.queryParams("search"), res, false)));

        //TODO Get suggestions upon keystroke type endpoint

        get("/search/:id", (req, res) -> gson.toJson(ps.searchProduct(req.params(":id"), res, true)));

        post("/update-ontology", (req, res) -> gson.toJson(mp.map(req.body(), res)));
    }
}
