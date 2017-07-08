package com.akura.retrieval.models;

import com.akura.config.Config;
import com.akura.retrieval.models.response.FeatureResponse;
import com.akura.retrieval.models.response.PropertyResponse;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.StmtIterator;

import java.util.ArrayList;

public class Entity {

    private OntModel model;
    public Individual instance;

    private Property name;
    private Property hashID;
    private ObjectProperty containProperty;
    private ObjectProperty feature;
    private ObjectProperty evaluatedBy;

    public Entity(OntModel m) {
        this.model = m;
        this.initProperties();
    }

    public Individual getEntityByHash(String hash) {
        Individual ind = this.model.getIndividual(Config.URI_NAMESPACE + hash);
        this.instance = ind;
        return ind;
    }


    public void searchEntityByString(String name) {

        //TODO
    }

    private void initProperties() {
        name = model.getProperty(Config.URI_NAMESPACE + "EntityName");
        hashID = model.getProperty(Config.URI_NAMESPACE + "HashID");
        containProperty = model.getObjectProperty(Config.URI_NAMESPACE + "ContainProperty");
        feature = model.getObjectProperty(Config.URI_NAMESPACE + "SubEntity");
        evaluatedBy = model.getObjectProperty(Config.URI_NAMESPACE + "EvaluatedBy");
    }

    public String getName() {
        return this.instance.getProperty(this.name).getLiteral().toString();
    }

    public String getHash() {
        return this.instance.getProperty(this.hashID).getLiteral().toString();
    }

    public double getAvgBaseScore() {
        int count = 0;
        double total = 0.0;
        StmtIterator iter = this.instance.listProperties(this.evaluatedBy);
        while (iter.hasNext()) {

            total += iter.next()
                    .getResource()
                    .getProperty(model.getProperty(Config.URI_NAMESPACE + "Score"))
                    .getLiteral()
                    .getDouble();

            count++;
        }
        if (count == 0) {
            return 0;
        }
        return (total / count);


    }

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

    public FeatureResponse[] getFeatures() {
        ArrayList<FeatureResponse> featureList = new ArrayList<>();

        StmtIterator iter = this.instance.listProperties(this.feature);
        while (iter.hasNext()) {

            Individual featureInstance = this.model
                    .getIndividual(iter.next().getResource().toString());

            FeatureResponse fres = new FeatureResponse(this.model,featureInstance);

            featureList.add(fres);
        }

        return featureList.toArray(new FeatureResponse[featureList.size()]);
    }


}
