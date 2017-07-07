package test;


import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.iterator.ExtendedIterator;


public class JenaTest {

    public static void main(String[] args) {

        OntModel m = ModelFactory.createOntologyModel();
        m.read("file:/Applications/MAMP/htdocs/CDAP/Jena/src/main/resources/rev_engine_base_ontology.owl");

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


        ExtendedIterator classes = m.listClasses();
        while (classes.hasNext())
        {
            OntClass thisClass = (OntClass) classes.next();
            System.out.println("Found class: " + thisClass.toString());
            ExtendedIterator instances = thisClass.listInstances();
//            while (instances.hasNext())
//            {
//                Individual thisInstance = (Individual) instances.next();
//                System.out.println("  Found instance: " + thisInstance.toString());
//            }
        }

//        // get instances in a class
//        OntClass myClass = m.getOntClass("http://www.akura.com#Entity");
//        ExtendedIterator instances = myClass.listInstances();
//        while (instances.hasNext())
//        {
////            Individual thisInstance = (Individual) instances.next();
////            System.out.println("  Found instance: " + thisInstance.toString());
//
//            OntResource c = (OntResource) instances.next();
//            System.out.println(c.getLocalName());
//        }
    }
}

