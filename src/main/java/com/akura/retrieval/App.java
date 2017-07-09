package com.akura.retrieval;


import com.akura.retrieval.service.ProductService;
import com.google.gson.Gson;
import static spark.Spark.get;


public class App {
    public static void main(String[] args){
        Gson gson = new Gson();
        ProductService ps = new ProductService();

        get("/search", (req, res) -> gson.toJson(ps.searchProduct(req.queryParams("search"), res, false)));

        //TODO Get suggestions upon keystroke type endpoint

        // TODO get Product from HashID
        get("/search/:id", (req, res) -> gson.toJson(ps.searchProduct(req.params(":id"), res, true)));
    }
}
