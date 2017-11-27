package com.akura.integration.service;

import com.akura.config.Config;
import com.akura.integration.dynamic.DynamicEntity;
import com.akura.integration.dynamic.ReviewInfo;
import com.akura.integration.models.Entity;
import com.akura.integration.models.Review;
import com.akura.logger.FileLogger;
import com.akura.ontology.BaseOntology;
import com.akura.utility.Log;
import com.akura.utility.OntologyReader;
import com.akura.utility.OntologyWriter;

import com.google.gson.Gson;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.util.iterator.ExtendedIterator;

import java.util.ArrayList;

/**
 * Class representing a IntegrateService.
 */
public class IntegrateService {

    // TODO insert classifier and determine classes from properties
    // TODO after classification sort the order so that steps are matched

    public static OntModel stat = BaseOntology.getInstance();
    public OntModel dynamic;
    Review review;
    Log log = new Log();

    public IntegrateService(OntModel dynamic) {
        this.dynamic = dynamic;
    }

    /**
     * Method used to integrate ontologies.
     *
     * @return - boolean value.
     */
    public Boolean integrate() {
        log.write("Merging Ontology....");
        FileLogger.Log("Merging",FileLogger.TYPE_SUB, FileLogger.DEST_INTEGRATION);
        // first get review class instances
        Boolean bool = this.processReviewClassInstances();
        if (!bool) {
            log.write("Ontology already merged. Aborted at processReviewClassInstances without saving");
            return false;
        }
        // second get entity class instances
        this.processEntityClassInstances();

        // save after everything
        return this.saveModifiedOntology();
    }

    /**
     * Method used to filter individuals.
     *
     * @param name - name of the class.
     * @return - list of individuals.
     */
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

    /**
     * Method used to process review class instances.
     *
     * @return - boolean value.
     */
    public Boolean processReviewClassInstances() {
        for (Individual instance : this.classIndividualFilter("ReviewInfo")) {

            ReviewInfo rf = new ReviewInfo(dynamic, instance);
            try {
                // step 2 : create reviewer and then review
                review = rf.createReview(rf.createReviewer(stat));
                if (review.status) {
                    return false;
                } else {
                    rf.createMainEntity(stat, review);
                }

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        return !review.status;
    }

    /**
     * Method used to process entity class instances.
     */
    public void processEntityClassInstances() {
        FileLogger.Log("Extracting Entity Instance List",FileLogger.TYPE_SUB, FileLogger.DEST_INTEGRATION);

        for (Individual instance : this.classIndividualFilter("Entity")) {
            DynamicEntity entity = new DynamicEntity(dynamic, stat, instance, review);

            if (entity.isEntity) {
                try {
                    FileLogger.Log("Identified Instance as an Entity",FileLogger.TYPE_SUB, FileLogger.DEST_INTEGRATION);
                    Entity ent = entity.setStaticOntoEntityInstance(stat);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                FileLogger.Log("Identified Instance as a Feature",FileLogger.TYPE_SUB, FileLogger.DEST_INTEGRATION);
            }
        }
    }

    // TODO Write an algortithm to save ontology model concurrently

    /**
     * Method used to save the modified ontology.
     *
     * @return - boolean value.
     */
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
