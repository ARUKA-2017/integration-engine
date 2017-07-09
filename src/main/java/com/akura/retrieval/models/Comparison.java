package com.akura.retrieval.models;

import com.akura.config.Config;
import com.akura.retrieval.response.ComparisonEntityInstance;
import com.akura.retrieval.response.FeatureResponse;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.StmtIterator;


import java.util.ArrayList;

public class Comparison {

    private OntModel model;

    Property goodProperty;
    Property badProperty;
    Property count;
    Property name;
    Property hashID;

    public Individual instance;

    public Comparison(OntModel m, Individual instance) {
        this.model = m;
        this.instance = instance;
        this.initProperties();
    }


    public ComparisonEntityInstance getGoodInstance() {
        ComparisonEntityInstance compEntity;

        StmtIterator iter = this.instance.listProperties(this.goodProperty);
        if (iter.hasNext()) {

            Individual entityInstance = this.model
                    .getIndividual(iter.next().getResource().toString());

            compEntity = new ComparisonEntityInstance(this.model, entityInstance);
            compEntity.count = this.getCount();


        } else {
            compEntity = null;
        }

        return compEntity;

    }

    public ComparisonEntityInstance getBadInstance() {
        ComparisonEntityInstance compEntity;

        StmtIterator iter = this.instance.listProperties(this.badProperty);
        if (iter.hasNext()) {
            Individual entityOrFeatureInstance = this.model
                    .getIndividual(iter.next().getResource().toString());

            compEntity = new ComparisonEntityInstance(this.model, entityOrFeatureInstance);
            compEntity.count = this.getCount();

        } else {
            compEntity = null;
        }

        return compEntity;
    }

    public Integer getCount() {
        return this.instance.getProperty(this.count).getLiteral().getInt();
    }

    public void initProperties() {

        goodProperty = model.getObjectProperty(Config.URI_NAMESPACE + "Good");
        badProperty = model.getObjectProperty(Config.URI_NAMESPACE + "Bad");
        count = model.getProperty(Config.URI_NAMESPACE + "Count");
        name = model.getProperty(Config.URI_NAMESPACE + "Name");
        hashID = model.getProperty(Config.URI_NAMESPACE + "HashID");

    }
}
