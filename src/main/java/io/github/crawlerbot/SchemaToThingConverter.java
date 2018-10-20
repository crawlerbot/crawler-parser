package io.github.crawlerbot;

import io.github.crawlerbot.model.Schema;
import com.google.schemaorg.core.CoreConstants;
import com.google.schemaorg.core.CoreFactory;
import com.google.schemaorg.core.Thing;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

public class SchemaToThingConverter {

    private static final String PACKAGE_SCHEMA_ORG = "com.google.schemaorg.core";

    public static Optional<Thing> convert(Schema schema) {

        if (!schema.getType().startsWith(CoreConstants.NAMESPACE)) {
            return Optional.empty();
        }

        try {
            // Find the type of the schema we're currently converting
            String typeName = getTypeName(schema);
            if(typeName.equals(typeName.toLowerCase())) {
                typeName = typeName.substring(0, 1).toUpperCase() + typeName.substring(1).toLowerCase();
            }
            // In order to build a schema.org thing, we use builders
            // We need the class of the builder to find the method to call for settings the properties
            Class<?> builderClass = getBuilderClass(typeName);
            /*
             * Create the builder for the thing we're building
             * Note that builderClass != thingBuilder.getClass(). We use the first one as it's an interface and all
             * the methods are public and can be called using reflection.
             */
            Thing.Builder thingBuilder = getBuilder(typeName);

            setProperties(thingBuilder, schema, builderClass);
            setChildren(thingBuilder, schema, builderClass);

            return Optional.of(thingBuilder.build());

        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException
                | ClassNotFoundException e) {

            e.printStackTrace();
            return Optional.empty();
        }
    }

    private static void setProperties(Thing.Builder builder, Schema schema, Class<?> builderClass)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Set all the properties
        for (String propertyName : schema.getProperties().keySet()) {
            String methodName = String.format("add%s", capitalize(propertyName));
            System.out.println("methodName:" + methodName );
            //if(!methodName.equals("addHeadline")) {
                Method method = builderClass.getMethod(methodName, String.class);
                //System.out.println("setProperties:=========" + method.toString());
               // System.out.println("setProperties:=========" + method.getName());
                method.invoke(builder, schema.getProperties().get(propertyName).get(0));
            //}
        }
    }

    private static void setChildren(Thing.Builder builder, Schema schema, Class<?> builderClass)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException {

        for (Schema child : schema.getChildren()) {
            if (!StringUtils.isEmpty(child.getPropertyName())) {
                Optional<Thing> optionalThing = convert(child);
                if (optionalThing.isPresent()) {
                    String methodName = String.format("add%s", capitalize(child.getPropertyName()));
                    Method method = builderClass.getMethod(methodName, getInterfaceClass(getTypeName(child)));
                    method.invoke(builder, optionalThing.get());
                }
            }
        }
    }

    /**
     * Return a schema.org build for the specified type
     *
     * @param typeName the type of the builder we want
     * @return the builder
     * @throws NoSuchMethodException     if we can't find the function used to get the builder
     * @throws InvocationTargetException if we can't invoke the function used to get the builder
     * @throws IllegalAccessException    if the function used to get the builder is inaccessible
     */
    private static Thing.Builder getBuilder(String typeName) throws NoSuchMethodException, InvocationTargetException,
            IllegalAccessException {

        String builderName = String.format("new%sBuilder", typeName);
        Thing.Builder builder =  (Thing.Builder) CoreFactory.class.getMethod(builderName).invoke(null);
        return builder;
    }

    /**
     * Find the class builder for a schema
     *
     * @param typeName the type name of the schema
     * @return the class builder
     * @throws ClassNotFoundException if the class for the specified type name does not exist
     */
    private static Class<?> getBuilderClass(String typeName) throws ClassNotFoundException {
        try {
            String className = String.format("%s.%s$Builder", PACKAGE_SCHEMA_ORG, typeName);
            //className = className.substring(0,1).toUpperCase() + className.substring(1).toLowerCase();
            return Class.forName(className);
        }catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private static Class<?> getInterfaceClass(String typeName) throws ClassNotFoundException {
        String className = String.format("%s.%s", PACKAGE_SCHEMA_ORG, typeName);

        return Class.forName(className);
    }

    /**
     * Get the type name of a schema
     *
     * @param schema the schema
     * @return the schema.org type name of the schema
     */
    private static String getTypeName(Schema schema) {
        return schema.getType().substring(CoreConstants.NAMESPACE.length());
    }

    /**
     * Capitalize the first letter of the string in parameter
     *
     * @param name the string to capitalize
     * @return the capitalized string
     */
    private static String capitalize(String name) {
        System.out.println("capitalize:" + name);
        return name.substring(0, 1).toUpperCase() + name.substring(1).trim();
    }
}
