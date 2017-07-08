package integration.models;



import integration.OntologyClass;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;

public class RelationshipGenerator {

    public static void setRelationship(OntModel model,
                                       String relationship,
                                       Individual domain,
                                       Individual range) {

        // eg: A baseScrore has an entity
        // bascore --> domain
        // entity --> range

        ObjectProperty prop = model.getObjectProperty(OntologyClass.URI_NAMESPACE + relationship);

        prop.addDomain(domain);
        prop.addRange(range);


    }
}
