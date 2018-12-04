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
        COLLECTION_TYPES.put("Map", "java.util.Map");
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
            updateAttributes(model, modelList, importStatements);
            Map<String, Attribute> attrMap = model.getAttributes().stream()
                    .collect(Collectors.toMap(Attribute::getName, Function.identity()));
            ctx.put("attrMap", attrMap);
            String path = "/home/ksaleh/project-march/model-gen/src/main/java/com/msnishan/gen/" + model.getName() + "DTO" + ".java";
            try (PrintWriter pt = new PrintWriter(path)) {
                modelTemplate.merge(ctx, pt);
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
        }

    }

    private static void updateAttributes(Model model, ModelList modelList, StringBuilder importStatements) {

        Map<String, Attribute> attrMap = model.getAttributes().stream()
                .collect(Collectors.toMap(Attribute::getName, Function.identity()));

        for (Map.Entry<String, Attribute> entry : attrMap.entrySet())
        {
            if (entry.getValue().getType().contains("$")) {
                String modelName = entry.getValue().getType().contains(":") ?
                            entry.getValue().getType().substring(1, entry.getValue().getType().indexOf(":"))
                                    : entry.getValue().getType().substring(1);
                Model targetModel = modelList.getModels().stream()
                                    .filter( mod -> mod.getName().equals(modelName))
                                    .findFirst().get();
                updateImportStatement(model, targetModel, importStatements);
                entry.getValue().setType(entry.getValue().getType().substring(1)
                        .replace(modelName, targetModel.getName() + targetModel.getType()));
            }
            if (entry.getValue().getType().contains(":")) {
                String genericType = entry.getValue().getType().substring(entry.getValue().getType().indexOf(":") + 1);
                String modelType = entry.getValue().getType().substring(0, entry.getValue().getType().indexOf(":"));
                updateImportStatement(genericType, importStatements);
                entry.getValue().setType(MessageFormat.format("{0}<{1}>", genericType, modelType));
            }
        }
    }

    private static void updateImportStatement(String genericType, StringBuilder importStatements) {
        String importStatement = MessageFormat.format("import {0};\n", COLLECTION_TYPES.get(genericType));
        if (!importStatements.toString().contains(importStatement)) {
            importStatements.append(importStatement);
        }
    }

    private static void updateImportStatement(Model model, Model targetModel, StringBuilder importStatements) {
        if (!targetModel.getPackageName().equals(model.getPackageName())) {
            importStatements.append(MessageFormat.format("import {0}.{1}{2};\n",
                    targetModel.getPackageName(), targetModel.getName(), targetModel.getType()));
        }
    }

}
