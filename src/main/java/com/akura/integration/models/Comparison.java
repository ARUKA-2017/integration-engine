package com.akura.integration.models;


import com.akura.integration.OntologyClass;
import com.akura.utility.HashGeneratorClass;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Property;

public class Comparison {

    public static String prefix = "COMPARISON";

    public Individual instance;
    public OntClass entityClass;


    private ObjectProperty goodProperty;
    private ObjectProperty badProperty;

    private Property count;

    private String hash;
    private OntModel model;

    public Boolean status;


    public Comparison(OntModel m,
                      String goodInstanceHash,
                      String badInstanceHash,
                      Individual goodInstance,
                      Individual badInstance) {
        this.model = m;
        this.entityClass = (OntClass) this.model.getOntClass(OntologyClass.COMPARISON);
        this.initProperties();

        this.hash = this.prefix + "-" + goodInstanceHash + "-" + badInstanceHash;

        Individual ind = this.search(this.hash);
        if (ind == null) {
            this.instance = entityClass.createIndividual(OntologyClass.URI_NAMESPACE
                    + this.hash);

            this.setCount(1);
            this.setComparisonRelationship(goodInstance,badInstance);

            this.status = false;
        } else {
            this.status = true;
            this.instance = ind;
        }
    }


    private void initProperties() {
        count = model.getProperty(OntologyClass.URI_NAMESPACE + "Count"); //TODO set this

        goodProperty = model.getObjectProperty(OntologyClass.URI_NAMESPACE + "Good");
        badProperty = model.getObjectProperty(OntologyClass.URI_NAMESPACE + "Bad");

    }

    public void setCount(Integer count) {
        this.instance.addLiteral(this.count, count);
    }

    public Integer getCount(){
        return this.instance.getProperty(this.count).getLiteral().getInt(); //TODO test this
    }

    public void incrementCount(){
        this.setCount(this.getCount() + 1);
    }

    public void setComparisonRelationship(Individual good, Individual bad) {

        if (!this.model.listStatements(this.instance, this.goodProperty, good).hasNext()) {
            RelationshipGenerator.setRelationship(this.goodProperty, this.instance, good);
        }

        if (!this.model.listStatements(this.instance, this.badProperty, bad).hasNext()) {
            RelationshipGenerator.setRelationship(this.badProperty, this.instance, bad);
        }

    }

    public Individual search(String hash) {
        Individual ind = this.model.getIndividual(OntologyClass.URI_NAMESPACE + hash);
        return ind;
    }
}
