package com.akura.retrieval.response;

import com.akura.retrieval.models.Feature;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntModel;

/**
 * Class representing a FeatureResponse.
 */
public class FeatureResponse {

    public String name;
    public double avg_baseScore;
    public ComparisonResponse comparisons;
    public PropertyResponse[] properties;

    public FeatureResponse(OntModel m, Individual featureInstance) {

        Feature f = new Feature(m, featureInstance);
        this.name = f.getName();
        this.avg_baseScore = f.getAvgBaseScore();
        this.properties = f.getProperties();
        this.comparisons = f.getComparisons();
    }
}
