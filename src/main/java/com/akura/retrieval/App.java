package com.akura.retrieval;


import com.akura.mapping.service.MappingService;
import com.akura.retrieval.service.ProductService;
import com.akura.retrieval.service.SparkMiddleware;
import com.google.gson.Gson;

;import static spark.Spark.*;


public class App {


    public static void main(String[] args) {
        Gson gson = new Gson();
        ProductService ps = new ProductService();
        MappingService mp = new MappingService();

        SparkMiddleware.enableCORS("*", "POST, GET, OPTIONS, PUT, DELETE",
                "Content-Type, x-xsrf-token, content-Type, X-Auth-Token, Origin, Authorization");

        get("/search", (req, res) -> gson.toJson(ps.searchProduct(req.queryParams("search"), res, false)));

        //TODO Get suggestions upon keystroke type endpoint

        get("/search/:id", (req, res) -> gson.toJson(ps.searchProduct(req.params(":id"), res, true)));

        post("/update-ontology", (req, res) -> gson.toJson(mp.map(req.body(), res)));
    }
}
