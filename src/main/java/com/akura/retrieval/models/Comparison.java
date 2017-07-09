package com.akura.retrieval.models;


import com.akura.config.Config;
import com.akura.retrieval.response.ComparisonEntityInstance;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Property;

import java.util.ArrayList;

public class Comparison {

    private OntModel model;

    public Comparison(OntModel m) {
        this.model = m;
    }


    public ArrayList<ComparisonEntityInstance> getBetterThanForEntity(String hash) {

        ArrayList<ComparisonEntityInstance> comparisonList = new ArrayList<>();

        ParameterizedSparqlString ps = new ParameterizedSparqlString();
        ps.setCommandText(
                "PREFIX akura: <http://www.akura.com#>\n" +
                        "SELECT ?comparison ?count ?bad ?name ?hashId \n" +
                        "WHERE { " +
                        "?bad akura:HashID  ?hashId ."+
                        "?bad akura:EntityName  ?name ."+
                        "?comparison akura:Bad  ?bad ."+
                        "?comparison akura:Count ?count ." +
                        "?comparison akura:Good ?y ." +
                        "?y  akura:HashID  ?hash ." +
                        "}"
        );
        ps.setLiteral("hash", hash);

        comparisonList = getResult(ps, comparisonList);

        return comparisonList;

    }

    public ArrayList<ComparisonEntityInstance> getWorseThanForEntity(String hash) {

        ArrayList<ComparisonEntityInstance> comparisonList = new ArrayList<>();

        ParameterizedSparqlString ps = new ParameterizedSparqlString();
        ps.setCommandText(
                "PREFIX akura: <http://www.akura.com#>\n" +
                        "SELECT ?comparison ?count ?good ?name ?hashId \n" +
                        "WHERE { " +
                        "?good akura:HashID  ?hashId ."+
                        "?good akura:EntityName  ?name ."+
                        "?comparison akura:Good  ?good ."+
                        "?comparison akura:Count ?count ." +
                        "?comparison akura:Bad ?y ." +
                        "?y  akura:HashID  ?hash ." +
                        "}"
        );
        ps.setLiteral("hash", hash);

        comparisonList = getResult(ps, comparisonList);

        return comparisonList;

    }




    private ArrayList<ComparisonEntityInstance> getResult(ParameterizedSparqlString ps, ArrayList<ComparisonEntityInstance> comparisonList)
    {
        Query query = QueryFactory.create(ps.toString());
        QueryExecution queryExecution = QueryExecutionFactory.create(query, this.model);


        try {

            ResultSet resultSet = queryExecution.execSelect();

            while( resultSet.hasNext() ) {
                QuerySolution solution = resultSet.nextSolution();

                ComparisonEntityInstance comparisonEntityInstance = new ComparisonEntityInstance();
                comparisonEntityInstance.id = solution.get("hashId").toString();
                comparisonEntityInstance.count = solution.getLiteral("count").getInt();
                comparisonEntityInstance.name = solution.getLiteral("name").toString();

                comparisonList.add(comparisonEntityInstance);

                System.out.println(solution.get("hashId").toString());
                System.out.println(solution.getLiteral("count").getInt());
                System.out.println(solution.getLiteral("name").toString());
            }
        } finally {
            queryExecution.close();
        }

        return comparisonList;

    }
}
