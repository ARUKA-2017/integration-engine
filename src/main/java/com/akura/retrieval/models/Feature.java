package com.akura.retrieval.models;

import com.akura.config.Config;
import com.akura.retrieval.response.PropertyResponse;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.StmtIterator;

import java.util.ArrayList;

public class Feature {
    private OntModel model;
    public Individual instance;

    private Property name;
    private ObjectProperty featureProperty;
    private ObjectProperty evaluatedBy;

    public Feature(OntModel m, Individual instance) {
        this.model = m;
        this.instance = instance;
        this.initProperties();
    }

    private void initProperties() {
        name = model.getProperty(Config.URI_NAMESPACE + "FeatureName");
        featureProperty = model.getObjectProperty(Config.URI_NAMESPACE + "FeatureProperty");
        evaluatedBy = model.getObjectProperty(Config.URI_NAMESPACE + "EvaluatedBy");

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
}
