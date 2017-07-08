package com.akura.integration.models;

import com.akura.integration.OntologyClass;
import com.akura.utility.HashGeneratorClass;
import com.akura.utility.OntologyWriter;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Property;

import java.util.Map;

public class BaseScore {

    public static String prefix = "BASESCORE";

    public Individual instance;
    public OntClass baseScoreClass;

    private OntModel model;

    private Property score;

    private String hash;

    public BaseScore(OntModel m, Review review) {
        this.model = m;

        this.baseScoreClass = (OntClass) this.model.getOntClass(OntologyClass.BASESCORE);
        this.hash = HashGeneratorClass.generateFromTimeStamp(this.prefix);

        this.instance = baseScoreClass.createIndividual(OntologyClass.URI_NAMESPACE
                + this.hash);

        review.setBaseScore(this.instance);

        this.initProperties();
    }

    private void initProperties() {
        score = model.getProperty(OntologyClass.URI_NAMESPACE + "Score");
    }

    public void setScore(float score) {
        instance.addLiteral(this.score, score);
    }

}
