package com.akura.test;


import com.akura.config.Config;
import com.akura.integration.dynamic.DynamicEntity;
import com.akura.integration.dynamic.ReviewInfo;
import com.akura.integration.models.Entity;
import com.akura.integration.models.Review;
import com.akura.integration.models.Reviewer;
import com.akura.integration.service.IntegrateService;
import com.akura.utility.OntologyReader;
import com.akura.utility.OntologyWriter;
import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.util.iterator.ExtendedIterator;

public class DynamicTest {


    public static void main(String[] args) {
        IntegrateService service = new IntegrateService(OntologyReader.getOntologyModel(Config.DYNAMIC_FILENAME));
        service.integrate();
//        ExtendedIterator<OntClass> classIter = dynamic.listClasses();
//
//        // step 1 init variables
//        Review review = null;
//
//
//        while (classIter.hasNext()) {
//            OntClass clazz = classIter.next();
////            System.out.println("********************** CLASS : " + clazz.getLocalName() + " *******************");
//
//            // list instances
//            ExtendedIterator<? extends OntResource> instanceIter = clazz.listInstances();
//
//
//            while (instanceIter.hasNext()) {
//                Individual instance = (Individual) instanceIter.next();
//
////                System.out.println("Instance name: " + instance.getLocalName());
//
//                // TODO insert classifier and determine classes from properties
//                // TODO after classification sort the order so that steps are matched
//
//                // for ReviewInfo Class
//                if (clazz.getLocalName().equals("ReviewInfo")) {
//                    ReviewInfo rf = new ReviewInfo(dynamic, instance);
//
//
//                    try {
//                        // step 2 : create reviewer and then review
//
//                        review = rf.createReview(rf.createReviewer(stat));
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//
//                    }
//
//                    if (review.status) {
//                        break;
//                    }
//                    System.out.println(review.status);
//                    // step 3 :create mainEntity from reviewInfo class from MainEntity Relationship
//                    try {
//                        rf.createMainEntity(stat, review);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                } else if (clazz.getLocalName().equals("Entity")) {
//
//                    DynamicEntity entity = new DynamicEntity(dynamic, stat, instance, review);
//
//                    if (entity.isEntity) {
//                        try {
//                            Entity ent = entity.setStaticOntoEntityInstance(stat);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//
//                }
//
//            }
//
//
////            System.out.println("**********************************************************************************");
//        }
//
//        if (!review.status) {
//            OntologyWriter.writeOntology(stat);
//        }
//

    }
}
