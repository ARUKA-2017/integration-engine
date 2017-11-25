package com.akura.retrieval.models;


import com.akura.config.Config;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.StmtIterator;

public class BaseScore {

    public static double getAvgBaseScore(OntModel model, Individual instance, Property property) {

        int count = 0;
        double total = 0.0;
        StmtIterator iter = instance.listProperties(property);
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
}
