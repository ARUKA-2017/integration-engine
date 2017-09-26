package com.akura.parser.models;


import com.akura.parser.config.Config;
import com.akura.utility.OntologyReader;
import com.akura.utility.OntologyWriter;
import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.util.iterator.ExtendedIterator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.akura.utility.OntologyWriter.fileResourceManager;

import org.apache.jena.datatypes.xsd.XSDDatatype;

public class Ontology {

    public static OntModel m = null;

    public HashMap<String, ArrayList<OntProperty>> classRegistry;


    public static void saveOntologyFile(OntModel m) {

        ExtendedIterator<OntClass> classIter = m.listClasses();
        HashMap<String, ArrayList<OntProperty>> classRegistry = new HashMap<>();
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


        OntologyWriter.writeOntology(m, "test-1.owl");
    }

    public Ontology(OntModel m) {
        this.m = m;
        classRegistry = new HashMap<>();
        this.populateClasses();
    }

    public Map populateClasses() {
        ExtendedIterator<OntClass> classIter = this.m.listClasses();

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


    public String getClassName(String entityName, Map literalProperties, Map complexProperties, Map simpleComplexProperties) {

        String className = null;
        // first class name, second matching prop values
        HashMap<String, ClassHeirachy> classCandidates = new HashMap<>();
        ArrayList entityProps = this.mergeMaps(literalProperties, complexProperties, simpleComplexProperties);

        for (Object key : classRegistry.keySet()) {

            ClassHeirachy ch = isTwoArrayListsWithSameValues(classRegistry.get(key.toString()), entityProps);
            classCandidates.put(key.toString(), ch);


//            if (isTwoArrayListsWithSameValues(classRegistry.get(key.toString()),
//                    this.mergeMaps(literalProperties, complexProperties, simpleComplexProperties))) {
//
//                className = key.toString();
//
//            }
        }
        // class uri
        String selectedKey = null;

        for (Object key : classCandidates.keySet()) {
            if (selectedKey == null) {
                selectedKey = key.toString();
            } else if (classCandidates.get(selectedKey).count < classCandidates.get(key).count) {
                selectedKey = key.toString();
            }
            if (classCandidates.get(selectedKey).count == entityProps.size()) {
                className = key.toString();
                break;
            }
        }

        if(selectedKey == null){
            className = createNewClass(entityName, literalProperties, complexProperties, simpleComplexProperties).getURI();
        }

        if (className == null) {
            System.out.println(selectedKey);
            if (classCandidates.get(selectedKey).count == 0) {
                className = createNewClass(entityName, literalProperties, complexProperties, simpleComplexProperties).getURI();
            } else if (classCandidates.get(selectedKey).count > entityProps.size()) {
                // create superclass // need the matching props here
                className =   createSuperClass(selectedKey,entityName,classCandidates.get(selectedKey)).getURI();
            } else {
                System.out.println(selectedKey);
                System.out.println(classCandidates.get(selectedKey).count );
                System.out.println( entityProps.size());
               className = createSubClass(selectedKey, entityName, classCandidates.get(selectedKey),complexProperties ).getURI();
            }
            // create either super class or subclass
        }


//        if (className != null) {
//            return className;
//        } else {
//
//            className = createNewClass(entityName, literalProperties, complexProperties, simpleComplexProperties).getURI();
//        }

        return className;
    }

    public OntClass createSuperClass(String subClassURI, String entityName, ClassHeirachy ch) {
        OntClass subclass = this.m.getOntClass(subClassURI);

        OntClass superclass = this.m.createClass(Config.ONTOLOGY_URI + "SUP-" + UUID.randomUUID().toString() + entityName.replace("#", "").toUpperCase());

        classRegistry.put(superclass.getURI(), new ArrayList());

        // todo: create all the matched resources
        for (String prop : ch.matched) {
            OntProperty property = this.m.getObjectProperty(Config.ONTOLOGY_PROP_URI_PREFIX +prop);
            classRegistry.get(superclass.getURI()).add(property);
            //todo: add domain range
        }
        subclass.setSuperClass(superclass);

        return superclass;

    }

    public OntClass createSubClass(String superClassURI, String entityName, ClassHeirachy ch, Map complexProperties) {
        OntClass superclass = this.m.getOntClass(superClassURI);

        OntClass subclass= this.m.createClass(Config.ONTOLOGY_URI + "SUB-" + UUID.randomUUID().toString() + entityName.replace("#", "").toUpperCase());

        classRegistry.put(subclass.getURI(), new ArrayList());

        for (String prop : ch.unmatched) {
            if(complexProperties.get(prop) != null){
                OntProperty property = this.m.createObjectProperty(Config.ONTOLOGY_PROP_URI + prop.toString().replace("#", "").toUpperCase());
                property.addDomain(subclass);
                ArrayList<Entity> arr = (ArrayList) complexProperties.get(prop.toString());
                property.addRange(this.m.getOntClass(arr.get(0).classURI));
                classRegistry.get(subclass.getURI()).add(property);
            }else{
                OntProperty property = this.m.createDatatypeProperty(Config.ONTOLOGY_PROP_URI + prop.toString().replace("#", "").toUpperCase());
                property.addDomain(subclass);
                classRegistry.get(subclass.getURI()).add(property);
            }

        }

        //   add superclass properties to the class reg as well
        ExtendedIterator<? extends OntProperty> propertyIter = superclass.listDeclaredProperties();

        while (propertyIter.hasNext()) {
            OntProperty property = propertyIter.next();

            if (property != null) {
                if (classRegistry.get(superclass.getURI()) != null) {
                    classRegistry.get(superclass.getURI()).add(property);

                } else {
                    ArrayList arr = new ArrayList();
                    arr.add(property);
                    classRegistry.put(superclass.getURI(), arr);
                }
            }
        }

        superclass.setSubClass(subclass);

        return superclass;

    }

    public OntClass createNewClass(String className, Map literalProperties, Map complexProperties, Map simpleComplexProperties) {


        OntClass clazz = this.m.createClass(Config.ONTOLOGY_URI + className.replace("#", "").toUpperCase());
        classRegistry.put(clazz.getURI(), new ArrayList());


        // set simple properties
        for (Object key : literalProperties.keySet()) {
            OntProperty property = this.m.createDatatypeProperty(Config.ONTOLOGY_PROP_URI + key.toString().replace("#", "").toUpperCase());
            property.addDomain(clazz);
            classRegistry.get(clazz.getURI()).add(property);
        }

        for (Object key : simpleComplexProperties.keySet()) {
            OntProperty property = this.m.createDatatypeProperty(Config.ONTOLOGY_PROP_URI + key.toString().replace("#", "").toUpperCase());
            property.addDomain(clazz);
            classRegistry.get(clazz.getURI()).add(property);
        }

        // set complex properties
        for (Object key : complexProperties.keySet()) {

            OntProperty property = this.m.createObjectProperty(Config.ONTOLOGY_PROP_URI + key.toString().replace("#", "").toUpperCase());
            property.addDomain(clazz);
            ArrayList<Entity> arr = (ArrayList) complexProperties.get(key.toString());
            property.addRange(this.m.getOntClass(arr.get(0).classURI));
            classRegistry.get(clazz.getURI()).add(property);
        }

        return clazz;
    }


    //todo
    public OntProperty getProperty(String propertyName) {
        OntProperty selectedProperty = null;

        ExtendedIterator<? extends OntProperty> propertyIter = Ontology.this.m.listAllOntProperties();
        while (propertyIter.hasNext()) {
            OntProperty property = propertyIter.next();

            if (property != null && property.getLocalName().toUpperCase().equals(Config.ONTOLOGY_PROP_URI_PREFIX + propertyName.toUpperCase())) {
                selectedProperty = property;
            }
        }

        if (selectedProperty == null) {
            selectedProperty = this.m.createDatatypeProperty(Config.ONTOLOGY_PROP_URI + propertyName.replace("#", "").toUpperCase());
        }
        return selectedProperty;
    }


    public ArrayList mergeMaps(Map literalProperties, Map complexProperties, Map simpleComplexProperties) {
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

    public ClassHeirachy isTwoArrayListsWithSameValues(ArrayList<OntProperty> classProperties, ArrayList<Object> targetProperties) {

        ClassHeirachy ch = new ClassHeirachy();
        if (classProperties == null && targetProperties == null)
            return ch;

        if ((classProperties == null && targetProperties != null) || (classProperties != null && targetProperties == null))
            return ch;


        for (Object targetProp : targetProperties) {
            Integer current = ch.count;
            for (OntProperty classProp : classProperties) {

                if (classProp.getLocalName().toString().toUpperCase().equals((Config.ONTOLOGY_PROP_URI_PREFIX + targetProp.toString()).toUpperCase()))
                    ch.count++;
                ch.matched.add(classProp.getLocalName());
            }

            if (current == ch.count) {
                ch.unmatched.add(targetProp.toString());
            }
        }

        return ch;
    }
}
