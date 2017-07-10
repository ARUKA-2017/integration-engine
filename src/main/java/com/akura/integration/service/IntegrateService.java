package com.akura.integration.service;


import com.akura.config.Config;
import com.akura.integration.dynamic.DynamicEntity;
import com.akura.integration.dynamic.ReviewInfo;
import com.akura.integration.models.Entity;
import com.akura.integration.models.Review;
import com.akura.utility.Log;
import com.akura.utility.OntologyReader;
import com.akura.utility.OntologyWriter;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.util.iterator.ExtendedIterator;

import java.util.ArrayList;

public class IntegrateService {

    // TODO insert classifier and determine classes from properties
    // TODO after classification sort the order so that steps are matched

    public static OntModel stat = OntologyReader.getOntologyModel(Config.OWL_FILENAME);
    public OntModel dynamic;
    Review review;
    Log log = new Log();

    public IntegrateService(OntModel dynamic) {
        this.dynamic = dynamic;
    }

    public Boolean integrate() {
        log.write("Merging Ontology....");
        // first get review class instances
        this.processReviewClassInstances();

        // second get entity class instances
        this.processEntityClassInstances();

        // save after everything
        return this.saveModifiedOntology();

    }

    public ArrayList<Individual> classIndividualFilter(String name) {
        ArrayList<Individual> individualList = new ArrayList<>();
        ExtendedIterator<OntClass> classIter = dynamic.listClasses();

        while (classIter.hasNext()) {
            OntClass clazz = classIter.next();

            if (clazz.getLocalName().equals(name)) {
                ExtendedIterator<? extends OntResource> instanceIter = clazz.listInstances();
                while (instanceIter.hasNext()) {
                    Individual instance = (Individual) instanceIter.next();
                    individualList.add(instance);
                }
            }

        }

        return individualList;

    }

    public void processReviewClassInstances() {
        for (Individual instance : this.classIndividualFilter("ReviewInfo")) {

            ReviewInfo rf = new ReviewInfo(dynamic, instance);
            try {
                // step 2 : create reviewer and then review
                review = rf.createReview(rf.createReviewer(stat));
                if (review.status) {
                    break;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                rf.createMainEntity(stat, review);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void processEntityClassInstances() {
        for (Individual instance : this.classIndividualFilter("Entity")) {
            DynamicEntity entity = new DynamicEntity(dynamic, stat, instance, review);

            if (entity.isEntity) {
                try {
                    Entity ent = entity.setStaticOntoEntityInstance(stat);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // TODO Write an algortithm to save ontology model concurrently
    public Boolean saveModifiedOntology() {

        if (!review.status) {
            log.write("Merge Successful. Saving Changes...");
            OntologyWriter.writeOntology(stat);
            log.write("Changes Saved");
            return true;
        } else {
            log.write("Ontology already merged. Aborted without saving");
            return false;
        }
    }
}
