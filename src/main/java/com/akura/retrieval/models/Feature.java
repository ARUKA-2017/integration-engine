package com.akura.retrieval.models;

import com.akura.config.Config;
import com.akura.retrieval.response.ComparisonResponse;
import com.akura.retrieval.response.PropertyResponse;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.StmtIterator;

import java.util.ArrayList;

public class Feature {
    private OntModel model;
    public Individual instance;

    private Property name;
    private Property hashID;
    private ObjectProperty featureProperty;
    private ObjectProperty evaluatedBy;
    private ObjectProperty subEntity;

    public Feature(OntModel m, Individual instance) {
        this.model = m;
        this.instance = instance;
        this.initProperties();
    }

    private void initProperties() {
        name = model.getProperty(Config.URI_NAMESPACE + "Name");
        featureProperty = model.getObjectProperty(Config.URI_NAMESPACE + "FeatureProperty");
        evaluatedBy = model.getObjectProperty(Config.URI_NAMESPACE + "EvaluatedBy");
        hashID = model.getProperty(Config.URI_NAMESPACE + "HashID");
        subEntity = model.getObjectProperty(Config.URI_NAMESPACE + "SubEntity");
    }

    public String getName() {
        return this.instance.getProperty(this.name).getLiteral().toString();
    }

    public double getAvgBaseScore() {

        return BaseScore.getAvgBaseScore(model, this.instance, this.evaluatedBy);
    }


    public PropertyResponse[] getProperties() {
        ArrayList<PropertyResponse> propList = new ArrayList<>();

        StmtIterator iter = this.instance.listProperties(this.featureProperty);
        while (iter.hasNext()) {

            Individual propInstance = this.model
                    .getIndividual(iter.next().getResource().toString());


            PropertyObject p = new PropertyObject(this.model, propInstance);
            PropertyResponse pres = new PropertyResponse();
            pres.key = p.getKey();
            pres.value = p.getValue();

            propList.add(pres);
        }
        return propList.toArray(new PropertyResponse[propList.size()]);
    }

    public ComparisonResponse getComparisons() {
        ComparisonResponse comp = new ComparisonResponse(this.model,this.getHash());
        return comp;
    }


    public String getHash(){
        return this.instance.getProperty(this.hashID).getLiteral().toString();
    }

    public String getEntityHash(){

        String entityHash = null;

        ParameterizedSparqlString ps = new ParameterizedSparqlString();
        ps.setCommandText(
                "PREFIX akura: <http://www.akura.com#>\n" +
                        "SELECT ?hashID \n" +
                        "WHERE { " +
                        "?y akura:HashID ?hashID ." +
                        "?y  akura:SubEntity  ?x ." +
                        "?x  akura:HashID ?hash  ." +
                        "}"
        );
        ps.setLiteral("hash", this.getHash());

        Query query1 = QueryFactory.create(ps.toString());
        QueryExecution queryExecution = QueryExecutionFactory.create(query1, model);

        try {

            ResultSet resultSet = queryExecution.execSelect();

            if( resultSet.hasNext() ) {
                QuerySolution solution = resultSet.nextSolution();
                entityHash = solution.get("hashID").toString();
            }
        } finally {
            queryExecution.close();
        }

        return entityHash;
    }

    public String getEntityName(){

        String entityName = null;

        ParameterizedSparqlString ps = new ParameterizedSparqlString();
        ps.setCommandText(
                "PREFIX akura: <http://www.akura.com#>\n" +
                        "SELECT ?name \n" +
                        "WHERE { " +
                        "?y akura:Name ?name ." +
                        "?y  akura:SubEntity  ?x ." +
                        "?x  akura:HashID ?hash  ." +
                        "}"
        );
        ps.setLiteral("hash", this.getHash());

        Query query1 = QueryFactory.create(ps.toString());
        QueryExecution queryExecution = QueryExecutionFactory.create(query1, model);

        try {

            ResultSet resultSet = queryExecution.execSelect();

            if( resultSet.hasNext() ) {
                QuerySolution solution = resultSet.nextSolution();
                entityName = solution.get("name").toString();
            }
        } finally {
            queryExecution.close();
        }

        return entityName;
    }


}
