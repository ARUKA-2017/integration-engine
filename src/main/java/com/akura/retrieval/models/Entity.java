package com.akura.retrieval.models;

import com.akura.config.Config;
import com.akura.retrieval.db.DBConnection;
import com.akura.retrieval.response.ComparisonResponse;
import com.akura.retrieval.response.FeatureResponse;
import com.akura.retrieval.response.PropertyResponse;

import com.mongodb.BasicDBObject;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.StmtIterator;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing an Entity.
 */
public class Entity {

    public Individual instance;

    private OntModel model;
    private Property name;
    private Property hashID;
    private ObjectProperty containProperty;
    private ObjectProperty feature;
    private ObjectProperty evaluatedBy;
    private List<String> pros;
    private List<String> cons;

    public Entity(OntModel m) {
        this.model = m;
        this.initProperties();
    }

    public Entity(OntModel m, Individual instance) {
        this.model = m;
        this.instance = instance;
        this.initProperties();
    }

    /**
     * Method used to get the entity by an hash id.
     *
     * @param hash - hash id
     * @return - Individual.
     */
    public Individual getEntityByHash(String hash) {
        Individual ind = this.model.getIndividual(Config.URI_NAMESPACE + hash);
        this.instance = ind;
        return ind;
    }

    /**
     * Method used to initialize the properties of an entity.
     */
    private void initProperties() {
        name = model.getProperty(Config.URI_NAMESPACE + "Name");
        hashID = model.getProperty(Config.URI_NAMESPACE + "HashID");
        containProperty = model.getObjectProperty(Config.URI_NAMESPACE + "ContainProperty");
        feature = model.getObjectProperty(Config.URI_NAMESPACE + "SubEntity");
        evaluatedBy = model.getObjectProperty(Config.URI_NAMESPACE + "EvaluatedBy");
    }

    /**
     * Method used to get the name of the entity,
     *
     * @return - entity name.
     */
    public String getName() {
        return this.instance.getProperty(this.name).getLiteral().toString();
    }

    /**
     * Method used to get the hash id of the entity.
     *
     * @return - hash id.
     */
    public String getHash() {
        return this.instance.getProperty(this.hashID).getLiteral().toString();
    }

    /**
     * Method used to get the base score of an entity.
     *
     * @return - base score.
     */
    public double getAvgBaseScore() {
        return BaseScore.getAvgBaseScore(model, this.instance, this.evaluatedBy);
    }

    /**
     * Method used to get the properties of an entity.
     *
     * @return - PropertyResponse array.
     */
    public PropertyResponse[] getProperties() {
        ArrayList<PropertyResponse> propList = new ArrayList<>();

        StmtIterator iter = this.instance.listProperties(this.containProperty);
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

    /**
     * Method used to get the features of an entity.
     *
     * @return - FeatureResponse array.
     */
    public FeatureResponse[] getFeatures() {
        ArrayList<FeatureResponse> featureList = new ArrayList<>();

        StmtIterator iter = this.instance.listProperties(this.feature);
        while (iter.hasNext()) {

            Individual featureInstance = this.model
                    .getIndividual(iter.next().getResource().toString());

            FeatureResponse fres = new FeatureResponse(this.model, featureInstance);

            featureList.add(fres);
        }

        return featureList.toArray(new FeatureResponse[featureList.size()]);
    }

    /**
     * Method used to get the comparisons of the entity.
     *
     * @return - ComparisonResponse.
     */
    public ComparisonResponse getComparisons() {
        ComparisonResponse comp = new ComparisonResponse(this.model, this.getHash());
        return comp;
    }

    /**
     * Method used to get the pros of the entity.
     *
     * @return - pros array.
     */
    public String[] getPros() {

        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put("name", new BasicDBObject("$regex", ".*" + this.getName() + ".*")
                .append("$options", "i"));

        try {
            Document doc = new DBConnection().Connect().getCollection("phone_pros_and_cons").find(whereQuery).first();
            this.pros = (ArrayList<String>) doc.get("pros");
            this.cons = (ArrayList<String>) doc.get("cons");

        } catch (Exception e) {
            this.pros = new ArrayList<String>();
            this.cons = new ArrayList<String>();
        }

        return this.pros.toArray(new String[this.pros.size()]);
    }

    /**
     * Method used to get the cons of an entity
     *
     * @return - cons array.
     */
    public String[] getCons() {

        if (this.pros == null) {
            this.cons = new ArrayList<String>();
        }

        return this.cons.toArray(new String[this.cons.size()]);
    }
}
