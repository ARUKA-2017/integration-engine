package com.akura.retrieval;


import com.akura.retrieval.service.ProductService;
import com.google.gson.Gson;

import static spark.Spark.*;
import static spark.Spark.get;
import static spark.Spark.post;

public class App {
    public static void main(String[] args){
        Gson gson = new Gson();
        ProductService ps = new ProductService();

        get("/search", (req, res) -> gson.toJson(ps.searchProduct(req.queryParams("search"), res)));
    }
}
