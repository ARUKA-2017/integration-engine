package com.akura.parser.models;


import com.akura.parser.config.Config;
import com.akura.utility.OntologyReader;
import com.akura.utility.OntologyWriter;
import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.util.iterator.ExtendedIterator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.akura.utility.OntologyWriter.fileResourceManager;
import org.apache.jena.datatypes.xsd.XSDDatatype;

public class Ontology {

    public static OntModel m = null;

    public HashMap<String, ArrayList<OntProperty>> classRegistry;

    public static OntModel getOntologyInstance() {
        if (m != null) {
            return m;
        } else {
            m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
            return m;
        }
    }

    public static void saveOntologyFile() {

        OntologyWriter.writeOntology(Ontology.getOntologyInstance(), "test-1.owl");
    }

    public Ontology() {
        classRegistry = new HashMap<>();
        this.populateClasses();
    }

    public Map populateClasses() {
        ExtendedIterator<OntClass> classIter = Ontology.getOntologyInstance().listClasses();

        while (classIter.hasNext()) {
            OntClass clazz = classIter.next();
            if (clazz != null) {
                ExtendedIterator<? extends OntProperty> propertyIter = clazz.listDeclaredProperties();

                while (propertyIter.hasNext()) {
                    OntProperty property = propertyIter.next();

                    if (property != null) {
                        if (classRegistry.get(clazz.getURI()) != null) {
                            classRegistry.get(clazz.getURI()).add(property);

                        } else {
                            ArrayList arr = new ArrayList();
                            arr.add(property);
                            classRegistry.put(clazz.getURI(), arr);
                        }
                    }
                }
            }
        }
        return classRegistry;
    }


    public String getClassName(String entityName, Map literalProperties, Map complexProperties , Map simpleComplexProperties) {

        String className = null;

        for (Object key : classRegistry.keySet()) {
            if (isTwoArrayListsWithSameValues(classRegistry.get(key.toString()),
                    this.mergeMaps(literalProperties, complexProperties, simpleComplexProperties))) {
                className = key.toString();
            }
        }

        if (className != null) {
            return className;
        } else {
            className = createNewClass(entityName, literalProperties, complexProperties, simpleComplexProperties).getURI();
        }

        return className;
    }

    public OntClass createNewClass(String className, Map literalProperties, Map complexProperties , Map simpleComplexProperties) {

        OntClass clazz = Ontology.getOntologyInstance().createClass(Config.ONTOLOGY_URI + className.toUpperCase());
        classRegistry.put(clazz.getURI(), new ArrayList());


        // set simple properties
        for (Object key : literalProperties.keySet()) {
            OntProperty property = Ontology.getOntologyInstance().createDatatypeProperty(Config.ONTOLOGY_PROP_URI + key.toString().toUpperCase());
            property.addDomain(clazz);
            classRegistry.get(clazz.getURI()).add(property);
        }

        for (Object key : simpleComplexProperties.keySet()) {
            OntProperty property = Ontology.getOntologyInstance().createDatatypeProperty(Config.ONTOLOGY_PROP_URI + key.toString().toUpperCase());
            property.addDomain(clazz);
            classRegistry.get(clazz.getURI()).add(property);
        }

      // set complex properties
        for (Object key : complexProperties.keySet()) {
            OntProperty property = Ontology.getOntologyInstance().createObjectProperty(Config.ONTOLOGY_PROP_URI + key.toString().toUpperCase());
            property.addDomain(clazz);
            ArrayList<Entity> arr = (ArrayList) complexProperties.get(key.toString());
            property.addRange(Ontology.getOntologyInstance().getOntClass(arr.get(0).classURI));
            classRegistry.get(clazz.getURI()).add(property);
        }

        return clazz;
    }


    public OntProperty getProperty(String propertyName) {
        OntProperty selectedProperty = null;

        ExtendedIterator<? extends OntProperty> propertyIter = Ontology.getOntologyInstance().listAllOntProperties();
        while (propertyIter.hasNext()) {
            OntProperty property = propertyIter.next();

            if (property != null && property.getLocalName().toUpperCase() == propertyName.toUpperCase()) {
                selectedProperty = property;
            }
        }

        if (selectedProperty == null) {
            selectedProperty = Ontology.getOntologyInstance().createDatatypeProperty(Config.ONTOLOGY_PROP_URI  + propertyName.toUpperCase());
        }
        return selectedProperty;
    }


    public ArrayList mergeMaps(Map literalProperties, Map complexProperties , Map simpleComplexProperties) {
        ArrayList arr = new ArrayList();

        for (Object key : literalProperties.keySet()) {
            arr.add(key.toString());
        }

        for (Object key : complexProperties.keySet()) {
            arr.add(key.toString());
        }

        for (Object key : simpleComplexProperties.keySet()) {
            arr.add(key.toString());
        }


        return arr;
    }

    public boolean isTwoArrayListsWithSameValues(ArrayList<OntProperty> list1, ArrayList<Object> list2) {

        if (list1 == null && list2 == null)
            return true;

        if ((list1 == null && list2 != null) || (list1 != null && list2 == null))
            return false;
        for (Object itemList2 : list2) {
            boolean bool = false;
            for (OntProperty itemList1 : list1) {
                if (itemList1.getLocalName().toLowerCase() == itemList2.toString().toLowerCase())
                    bool = true;
            }

            if (!bool) {
                return false;
            }
        }

        return true;
    }
}
