package com.akura.integration.models;

import com.akura.config.Config;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Property;

/**
 * Class representing a Comparison.
 */
public class Comparison {

    public static String prefix = "COMPARISON";

    public Individual instance;
    public OntClass entityClass;
    public Boolean status;

    private ObjectProperty goodProperty;
    private ObjectProperty badProperty;
    private Property count;
    private String hash;
    private OntModel model;

    public Comparison(OntModel m,
                      String goodInstanceHash,
                      String badInstanceHash,
                      Individual goodInstance,
                      Individual badInstance) {
        this.model = m;
        this.entityClass = (OntClass) this.model.getOntClass(Config.COMPARISON);
        this.initProperties();

        this.hash = this.prefix + "-" + goodInstanceHash + "-" + badInstanceHash;

        Individual ind = this.search(this.hash);
        if (ind == null) {
            this.instance = entityClass.createIndividual(Config.URI_NAMESPACE
                    + this.hash);

            this.setCount(1);
            this.setComparisonRelationship(goodInstance, badInstance);

            this.status = false;
        } else {
            this.status = true;
            this.instance = ind;
            this.incrementCount();
        }
    }

    /**
     * Method used to initialize the properties.
     */
    private void initProperties() {
        count = model.getProperty(Config.URI_NAMESPACE + "Count");

        goodProperty = model.getObjectProperty(Config.URI_NAMESPACE + "Good");
        badProperty = model.getObjectProperty(Config.URI_NAMESPACE + "Bad");
    }

    /**
     * Method used to set the count.
     *
     * @param count - count value.
     */
    public void setCount(Integer count) {
        if (this.instance.getProperty(this.count) == null) {
            this.instance.addLiteral(this.count, count);
        } else {
            this.instance.getProperty(this.count).changeLiteralObject(count);
        }
    }

    /**
     * Method used to get the count.
     *
     * @return - integer count value.
     */
    public Integer getCount() {
        return this.instance.getProperty(this.count).getLiteral().getInt();
    }

    /**
     * Method used to increment the comparison count.
     */
    public void incrementCount() {
        this.setCount(this.getCount() + 1);
    }

    /**
     * Method used to set comaparison relationship.
     *
     * @param good - good individual.
     * @param bad  - bad individual.
     */
    public void setComparisonRelationship(Individual good, Individual bad) {

        if (!this.model.listStatements(this.instance, this.goodProperty, good).hasNext()) {
            RelationshipGenerator.setRelationship(this.goodProperty, this.instance, good);
        }

        if (!this.model.listStatements(this.instance, this.badProperty, bad).hasNext()) {
            RelationshipGenerator.setRelationship(this.badProperty, this.instance, bad);
        }
    }

    /**
     * Method used to search by hash id.
     *
     * @param hash - hash id.
     * @return - individual.
     */
    public Individual search(String hash) {
        Individual ind = this.model.getIndividual(Config.URI_NAMESPACE + hash);
        return ind;
    }
}
