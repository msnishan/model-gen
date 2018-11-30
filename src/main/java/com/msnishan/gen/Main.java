package com.msnishan.gen;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 *
 */
public class Main
{
    private static final Map<String, String> COLLECTION_TYPES = new HashMap<>();
    
    static {
        COLLECTION_TYPES.put("List", "java.util.List");
        COLLECTION_TYPES.put("Set", "java.util.Set");
    }
    public static void main (String[] args)
    {

        ModelList modelList = null;
        try
        {
            modelList = new ObjectMapper().readValue(Main.class.getClassLoader().getResourceAsStream("models.json"), ModelList.class);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        System.out.println(modelList);
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        
        ve.init();
        Template modelTemplate = ve.getTemplate("ModelEngine.vm");
        
        for (Model model : modelList.getModels()) {
            StringBuilder importStatements = new StringBuilder();
            VelocityContext ctx = new VelocityContext();
            ctx.put("Package", MessageFormat.format("package {0};", model.getPackageName()));
            ctx.put("GeneratedStatement", "Generated from model-gen framework");
            ctx.put("ClassNameUpper", model.getName());
            ctx.put("ModelType", model.getType());
            ctx.put("Imports", importStatements);
            Map<String, Attribute> attrMap = model.getAttributes().stream()
                                .collect(Collectors.toMap(Attribute::getName, Function.identity()));
            updateAttributes(attrMap, modelList, importStatements);
            ctx.put("attrMap", attrMap);
            String path = "/home/cpuser/mslearn/model-gen/src/main/java/com/msnishan/gen/" + model.getName() + "DTO" + ".java";
            try (PrintWriter pt = new PrintWriter(path)) {
                modelTemplate.merge(ctx, pt);
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
        }

    }

    private static void updateAttributes(Map<String, Attribute> attrMap,
                                          ModelList modelList, StringBuilder importStatements) {
        for (Map.Entry<String, Attribute> entry : attrMap.entrySet())
        {
            if (entry.getValue().getType().startsWith("$")) {
                Model targetModel = modelList.getModels().stream()
                                    .filter( model -> model.getName().equals(getAttributeName(entry, importStatements)))
                                    .findFirst().get();
                
                entry.getValue().setType(targetModel.getName() + targetModel.getType());
            }
        }
    }                                   

    private static String getAttributeName(Map.Entry<String, Attribute> entry, StringBuilder importStatements) {
        
        String modelName = entry.getValue().getType().substring(1);
        if (modelName.contains(":")) {
            String collectionType = modelName.substring(modelName.indexOf(":") + 1);
            importStatements.append(MessageFormat.format("import {0};\n", COLLECTION_TYPES.get(collectionType)));
            entry.getValue().setType(MessageFormat.format("{0}<{1}>",
                                  collectionType, modelName.substring(0, modelName.lastIndexOf(":"))));
            return modelName.substring(0, modelName.lastIndexOf(":"));
            
        }
        return modelName;
    }
}
