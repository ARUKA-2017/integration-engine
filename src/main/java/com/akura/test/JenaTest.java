package com.akura.test;


import com.akura.config.Config;
import com.akura.integration.models.*;
import com.akura.utility.OntologyReader;

import com.akura.utility.OntologyWriter;
import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.util.iterator.ExtendedIterator;

import java.util.HashMap;
import java.util.Map;


public class JenaTest {

    public static void main(String[] args) {

//        System.out.println(HashGeneratorClass.generateHashForString("iphone 7"));

        OntModel m = OntologyReader.getOntologyModel(Config.OWL_FILENAME);

//        StmtIterator iter = m.listStatements();
//
//        while (iter.hasNext()) {
//            Statement stmt      = iter.nextStatement();  // get next statement
//            Resource subject   = stmt.getSubject();     // get the subject
//            Property  predicate = stmt.getPredicate();   // get the predicate
//            RDFNode   object    = stmt.getObject();      // get the object
//
//            System.out.println(subject.toString());
//            System.out.print(" " + predicate.toString() + " ");
//            if (object instanceof Resource) {
//                System.out.print(object.toString());
//            } else {
//                // object is a literal
//                System.out.print(" \"" + object.toString() + "\"");
//            }
//
//            System.out.println(" .");
//
//        }

        //


//        ExtendedIterator classes = m.listClasses();
//        while (classes.hasNext())
//        {
//            OntClass thisClass = (OntClass) classes.next();
//            System.out.println("Found class: " + thisClass.toString());
//            ExtendedIterator instances = thisClass.listInstances();
////            while (instances.hasNext())
////            {
////                Individual thisInstance = (Individual) instances.next();
////                System.out.println("  Found instance: " + thisInstance.toString());
////            }
//        }

        // get instances in a class
//
//        Entity ent = new Entity(m);

        //        ent.save();
//
//
//        OntClass myClass = ent.entityClass;
//
//        ExtendedIterator prop = myClass.listDeclaredProperties();
//        while (prop.hasNext()) {
//            Property p =  (Property) prop.next();
//            System.out.println(p.getLocalName());
//        }


//        ExtendedIterator instances = myClass.listInstances();
//        while (instances.hasNext())
//        {
//            OntResource c = (OntResource) instances.next();
//            System.out.println(c.getLocalName());
//        }


        // test entity

//        Entity ent = new Entity(m, "Iphone 7");
//
//        ent.setProperty("price", "$112");
//        ent.setProperty("weight", "200g");
//
//        Map<String, String> map = new HashMap<String, String>();
//
//        map.put("FrontCam", "12MPX");
//        map.put("RearCam", "20MPX");
//        map.put("Optical Zoom", "Yes");
//
//        ent.setFeature("Camera", map);
//        ent.save();


        // step 1 : create reviewer - PASS

        Reviewer reviewer = new Reviewer(m, "nilesh.jayanandana@yahoo.com", "nilesh93");
        Reviewer reviewer2 = new Reviewer(m, "rishanthakumar@gmail.com", "rish");

        // step 2: create review - PASS
        Review review = reviewer.createReviewAndGetInstance("iPhone 7 is bloody bad. It's the worst phone there is");
        Review review2 = reviewer2.createReviewAndGetInstance("iPhone 7 is better than Samsung Galaxy S5");


        // step 4: create main entity
        Entity mainEntity = new Entity(m, "iPhone 7");
        Entity mainEntity2 = new Entity(m, "Samsung Galaxy S5");

        // step 5: set properties and features for main entity
        mainEntity.setProperty("price", "$112");
        mainEntity.setProperty("weight", "200g");

        mainEntity.setProperty("price", "$100");
        mainEntity.setProperty("weight", "180g");

        Map<String, String> map = new HashMap<String, String>();

        map.put("FrontCam", "12MPX");
        map.put("RearCam", "20MPX");
        map.put("Optical Zoom", "Yes");

        Map<String, String> map2 = new HashMap<String, String>();

        map.put("FrontCam", "10MPX");
        map.put("RearCam", "15MPX");
        map.put("Optical Zoom", "Yes");

        Feature feature_camera = mainEntity.setFeature("Camera", map);

        Feature feature_camera2 = mainEntity2.setFeature("Camera", map2);

        // step 6: Connect main enity with review
        mainEntity.makeThisMainEntityForCurrentReview(review);

        mainEntity2.makeThisMainEntityForCurrentReview(review2);

        // step 7: Set overall basescore for main Entity
        BaseScore baseScore = new BaseScore(m, review);
        baseScore.setScore((float) 0.9);

        BaseScore baseScore2 = new BaseScore(m, review2);
        baseScore2.setScore((float) 0.7);



        // step 9 create new basescore for camera
        BaseScore camScore = new BaseScore(m, review);
        camScore.setScore((float) 0.5);

        BaseScore camScore2 = new BaseScore(m, review);
        camScore2.setScore((float) 0.4);

        feature_camera.setBaseScore(camScore.instance);
        feature_camera2.setBaseScore(camScore2.instance);


        // step 10 set comparisons
        Comparison comparison1 = new Comparison(m, mainEntity.hash, mainEntity2.hash, mainEntity.instance, mainEntity2.instance);

        Comparison comparison2 = new Comparison(m, feature_camera.hash, feature_camera2.hash, feature_camera.instance, feature_camera2.instance);



        // save ontology
        OntologyWriter.writeOntology(m);


    }
}

