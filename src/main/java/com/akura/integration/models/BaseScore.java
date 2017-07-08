package com.akura.integration.models;

import com.akura.config.Config;
import com.akura.utility.HashGeneratorClass;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Property;


public class BaseScore {

    public static String prefix = "BASESCORE";

    public Individual instance;
    public OntClass baseScoreClass;

    private OntModel model;

    private Property score;

    private String hash;

    public BaseScore(OntModel m, Review review) {
        this.model = m;

        this.baseScoreClass = (OntClass) this.model.getOntClass(Config.BASESCORE);
        this.hash = HashGeneratorClass.generateFromTimeStamp(this.prefix);

        this.instance = baseScoreClass.createIndividual(Config.URI_NAMESPACE
                + this.hash);

        review.setBaseScore(this.instance);

        this.initProperties();
    }

    private void initProperties() {
        score = model.getProperty(Config.URI_NAMESPACE + "Score");
    }

    public void setScore(float score) {
        instance.addLiteral(this.score, score);
    }

}
