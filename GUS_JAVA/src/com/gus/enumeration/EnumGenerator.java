package com.gus.enumeration;

import com.gus.annotation.ColumnMapping;
import com.gus.annotation.ColumnType;
import com.opencsv.CSVReader;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import org.apache.commons.lang3.StringUtils;
import com.gus.PrimaryKey;
import com.gus.annotation.TableMapping;
import com.gus.annotation.processor.GlobalImmutableData;

import javax.lang.model.element.Modifier;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is a java application you can tweak and run to generate a
 * {@link GlobalImmutableData} compliant Enum from its <code>seed_data</code>
 * <code>.csv</code> file.
 *
 * @author gus, ujjwal, cliff
 */
public class EnumGenerator {

    /** CHANGE these for every run. */
    private static final String enumName = "JspBandParameter";
    private static final String tableName = "t_jsp_band_parameter";
    private static final String csvFilePath = "./seed_data/data/" + tableName + ".csv";

    /** The index of the record for the eventual enum name */
    private static final int enumNameHeaderIndex = 0;
    /** The index of the record for the primary key */
    private static final int primaryKeyHeaderIndex = 0;
    /** The index of the record for the display value */
    private static final int displayTextHeaderIndex = 0;

    // The values entered for columnDataTypes and columnDataLengths is pretty tedious. Could enhance with a database
    // call that performs a lookup for these values instead
    /** Match primitives to the columns respective to order. Size MUST match {@link #headers} */
    private static final Class[] columnDataTypes = {
    		int.class, 				//0 jsp_band_id ->  JspBand 
    		int.class,  	 		//1 jsp_parameter_id -> JspParameter
    		
    };
    /** Assign lengths for string values for the columns respective to order. Size MUST match {@link #headers} */
    private static final Integer[] columnDataLengths = {
            null,
            null,
           
    };

    private static final String outputRootDir = "src";
    private static final String outputPackage = "tt.constants.globaldata";
    private static String[] headers;

    public static void main(String[] args) {
        System.out.println("EnumGenerator starting ...");
        TypeSpec typeSpec = loadContentFromCSVFile();

        JavaFile javaFile = JavaFile.builder(outputPackage, typeSpec).build();

        System.out.println("EnumGenerator writing " + javaFile.packageName + "." + javaFile.typeSpec.name);
        try {
            File file = new File(outputRootDir);
            javaFile.writeTo(file);

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("EnumGenerator done ...");
    }

    private static TypeSpec loadContentFromCSVFile() {

        TypeSpec typeSpec = null;
        ClassName globalImmutableData = ClassName.get("com.innotas.globaldata", "GlobalImmutableData");
        ClassName enumClass = ClassName.get("tt.constants.globaldata", enumName);
        TypeName parameterizedClass = ParameterizedTypeName.get(globalImmutableData, enumClass);

        try {
            String fileName = new File(csvFilePath).getName();
            System.out.println("Reading csv file " + fileName);
        } catch (NullPointerException e) {
            System.out.println("Couldn't find " + csvFilePath);
            System.exit(1);
        }

        try (FileReader fin = new FileReader(csvFilePath)) {
            try (CSVReader cin = new CSVReader(fin, ',', '"', (char) 1)) {
                String[] rawHeaders = cin.readNext();

                System.out.println("Got csv headings " + Arrays.toString(rawHeaders));

                String idDatabaseColumnName = rawHeaders[primaryKeyHeaderIndex]; // Save name for annotation
                headers = camelizeHeaders(rawHeaders);
                // Change primary key to "id" and let code handle the rest
                headers[primaryKeyHeaderIndex] = "id";

                System.out.println("Converted csv headings to " + Arrays.toString(headers));

                // builds the javadoc
                StringBuilder sb = new StringBuilder(enumName);
                sb.append(" enum constants are synchronized with the ");
                sb.append(tableName.toUpperCase());
                sb.append("\n");
                sb.append("database table (signaled by the {@link TableMapping} annotation).\n");
                sb.append(enumName);
                sb.append(" constants contain the following:\n");
                for (int i = 0; i < headers.length; i++) {
                    sb.append("<li><code>");
                    sb.append(headers[i]);
                    sb.append("</code> - \n");
                }

                AnnotationSpec tableMapping = AnnotationSpec.builder(TableMapping.class)
                        .addMember("name", "$S", tableName.toUpperCase())
                        .build();

                TypeSpec.Builder enumTypeBuilder = TypeSpec.enumBuilder(enumName)
                        .addJavadoc(sb.toString())
                        .addModifiers(Modifier.PUBLIC)
                        .addSuperinterface(parameterizedClass)
                        .addAnnotation(tableMapping);

                MethodSpec.Builder constructorMethodBuilder = MethodSpec.constructorBuilder();


                for (int i = 0; i < headers.length; i++) {
                    enumTypeBuilder.addField(columnDataTypes[i], headers[i], Modifier.PUBLIC, Modifier.FINAL);
                    constructorMethodBuilder.addParameter(columnDataTypes[i], headers[i]);
                    constructorMethodBuilder.addStatement("this.$N = $N", headers[i], headers[i]);

                    if (i == primaryKeyHeaderIndex) continue; // Skip building generic getter method for the id
                    enumTypeBuilder.addMethod(buildGenericGetterMethod(
                            headers[i],
                            columnDataTypes[i],
                            columnDataLengths[i]));
                }

                // Add dynamically built generic getters
                enumTypeBuilder.addMethod(constructorMethodBuilder.build());

                // Build interface methods and non-generifiable methods
                enumTypeBuilder.addMethod(buildGetIdMethod(idDatabaseColumnName));
                enumTypeBuilder.addMethod(buildGetPrimaryKeyMethod());
                enumTypeBuilder.addMethod(buildGetDisplayTextMethod());
                enumTypeBuilder.addMethod(buildForIdMethod(enumClass));

                System.out.println("Reading csv records and generating Enum constants ...");

                // Datastructure to track enum names and increment where appropriate for uniqueness
                Map<String, Integer> enumNamesCount = new HashMap<>();
                String typeArgs = buildTypeArgs();
                List<Integer> headerBooleanIndexes = getHeaderBooleanIndexes();
                String[] entry = null;
                while ((entry = cin.readNext()) != null) {
                    String enumName = getSanitizedEnumName(entry);
                    if (enumNamesCount.containsKey(enumName)) {
                        enumNamesCount.put(enumName, enumNamesCount.get(enumName) + 1);
                        enumName = enumName + "_" + enumNamesCount.get(enumName).toString();
                    } else {
                        enumNamesCount.put(enumName, 1);
                    }
                    //Perform custom entry[x] conversions here
                    long jspBandId = Long.parseLong(entry[0]);
//                    JspBand jspBand = JspBand.forId(jspBandId);
//                    if (jspBand != null) {
//                    	entry[0] = "JspBand."+jspBand.name();
//                    }  else {
//                    	System.out.println("FAILED to find JspBand with id="+jspBandId+" ...skipping");
//                    	continue;
//                    }
                    // Perform boolean transform of any boolean values for this entry
//                    for (int index : headerBooleanIndexes) {
//                        entry[index] = (entry[index].equals("1")) ? "true" : "false";  
//                    }
                    
//                    if (StringUtils.isNotEmpty(entry[1])) {
//                    	long jspParameterId = Long.parseLong(entry[1]);
//                    	JspParameter jspParameter = JspParameter.forId(jspParameterId);
//                        if (jspParameter != null) {
//                        	entry[1] = "JspParameter."+jspParameter.name();
//                        }  else {
//                        	System.out.println("FAILED to find JspParameter with id="+jspParameterId+" ...skipping");
//                        	continue;
//                        }	
//                    } else {
//                    	continue;
//                    }
                    
                    // Build enum constant
                    enumTypeBuilder.addEnumConstant(
                            enumName,
                            TypeSpec.anonymousClassBuilder(typeArgs, (Object[]) entry).build()
                    );
                }

                typeSpec = enumTypeBuilder.build();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return typeSpec;
    }

    /**
     * Sanitizes the record name for the enum's constant's name
     *
     * @param entry - the csv values in a record
     * @return a unique 'name' for each Enum constant based upon one
     * or more Strings inside its csv record
     */
    private static String getSanitizedEnumName(String[] entry) {
//        String enumName = entry[enumNameHeaderIndex]
//                .replaceAll("\\s", "_")
//                .replaceAll("&nbsp;", "_")
//                .replaceAll("&amp;", "AND")
//                .replaceAll("&", "AND")
//                .replaceAll(" + ", "PLUS")
//                .replaceAll(".pluralName", "s");
//        enumName = StringUtils.remove(enumName, "/");
//        enumName = StringUtils.remove(enumName, "*");
//        enumName = StringUtils.remove(enumName, "-");
//        enumName = StringUtils.remove(enumName, "${");
//        enumName = StringUtils.remove(enumName, "}");
//        enumName = StringUtils.remove(enumName, "(");
//        enumName = StringUtils.remove(enumName, ")");
//        enumName = StringUtils.remove(enumName, ">");
//        enumName = StringUtils.remove(enumName, "=");
//        enumName = StringUtils.remove(enumName, "?");
//    	return enumName.toUpperCase();

        return "JSPBAND_PARAMETER_"+entry[0]+"_"+entry[1];
    }

    /**
     * Dynamically matches the columnDataTypes array to a formatting symbol
     * "$S" outputs a quoted String. "$L" outputs a literal value like a number or boolean.
     *
     * @return a string with the type formats that the TypeSpec.anonymousClassBuilder() expects
     */
    private static String buildTypeArgs() {
        List<String> typeArgs = new ArrayList<>();
        for (Class dataType : columnDataTypes) {
        	if (dataType == long.class) {
                typeArgs.add("$LL");
            } else
            if (dataType == int.class || dataType == boolean.class) {
                typeArgs.add("$L");
            } else {
            	typeArgs.add("$S");
            }
        }

        return String.join(", ", typeArgs);
    }

    private static String[] camelizeHeaders(String[] headers) {
        for (int i = 0; i < headers.length; i++) {
            String[] strings = StringUtils.split(headers[i].toLowerCase(), "_");
            for (int j = 1; j < strings.length; j++) {
                strings[j] = StringUtils.capitalize(strings[j]);
            }
            headers[i] = StringUtils.join(strings);
        }
        return headers;
    }

    /**
     * @return a getDisplayText() method that returns
     * the display text built around the user defined {@link #displayTextHeaderIndex}
     */
    private static MethodSpec buildGetDisplayTextMethod() {
        String displayTextHeaderName = headers[displayTextHeaderIndex];
        return MethodSpec.methodBuilder("getDisplayText")
                .addModifiers(Modifier.PUBLIC)
                .returns(String.class)
                .addStatement("return $L", displayTextHeaderName)
                .addAnnotation(java.lang.Override.class)
                .build();
    }

    /**
     * @return a getPrimaryKey() method that returns
     * a PrimaryKey built around the user defined {@link #primaryKeyHeaderIndex}
     */
    private static MethodSpec buildGetPrimaryKeyMethod() {
        String primaryKeyHeaderName = headers[primaryKeyHeaderIndex];
        return MethodSpec.methodBuilder("getPrimaryKeyFor")
                .addModifiers(Modifier.PUBLIC)
                .returns(PrimaryKey.class)
                .addStatement("return $T.getPrimaryKeyFor($L)", GlobalImmutableData.class, primaryKeyHeaderName)
                .addAnnotation(java.lang.Override.class)
                .build();
    }

    /**
     * @param enumClass - the Enum class being generated
     * @return a static <code>forId(long id)</code> method
     */
    private static MethodSpec buildForIdMethod(ClassName enumClass) {
        return MethodSpec.methodBuilder("forId")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(enumClass)
                .addParameter(long.class, "id")
                .addStatement("return $T.forId($T.getPrimaryKeyFor(id), $T.class)",
                        GlobalImmutableData.class, GlobalImmutableData.class, enumClass)
                .build();
    }

    /**
     * Creates a getId() method for this enum that needs special handling for the annotation
     *
     * @param idDatabaseColumnName - The header name of the primary key column for this enum
     * @return a getId() method that returns the constant's id defined by the name in {@link #headers}
     * * through {@link #primaryKeyHeaderIndex}
     */
    private static MethodSpec buildGetIdMethod(String idDatabaseColumnName) {
        String primaryKeyHeaderName = headers[primaryKeyHeaderIndex];
        Class primaryKeyDataType = columnDataTypes[primaryKeyHeaderIndex];

        AnnotationSpec as = AnnotationSpec.builder(ColumnMapping.class)
                .addMember("type", "$T.$L", ColumnType.class, ColumnType.getColumnTypeForPrimitive(primaryKeyDataType))
                .addMember("name", "$S", idDatabaseColumnName)
                .addMember("isPrimaryKey", "$L", true).build();

        return MethodSpec.methodBuilder("getId")
                .addModifiers(Modifier.PUBLIC)
                .returns(primaryKeyDataType)
                .addStatement("return this.$L", primaryKeyHeaderName)
                .addAnnotation(as)
                .build();
    }

    /**
     * @param name      - the name of the enum to create a getter signature
     * @param retType   - return type of the method generated
     * @param maxLength - length of value that matches the table column for {@link #tableName}
     * @return a generic getter method for the records values according to {@link #headers}
     */
    private static MethodSpec buildGenericGetterMethod(String name,
                                                       Class retType,
                                                       Integer maxLength) {
        String capName = name.substring(0, 1).toUpperCase() + name.substring(1);
        MethodSpec.Builder msb = MethodSpec.methodBuilder("get" + capName)
                .addModifiers(Modifier.PUBLIC)
                .returns(retType)
                .addStatement("return $L", name);

        // Build out the annotation
        AnnotationSpec.Builder asb = AnnotationSpec.builder(ColumnMapping.class)
                .addMember("type", "$T.$L", ColumnType.class, ColumnType.getColumnTypeForPrimitive(retType));

        if (maxLength != null) {
            asb.addMember("maxLength", "$L", maxLength);
        }

        msb.addAnnotation(asb.build());

        return msb.build();
    }

    /**
     * Helper method that retrieves any boolean columns defined in {@link #columnDataTypes}
     * This is primarily for enum constant conversion of legacy 1|0 booleans
     *
     * @return a <code>List<Integer></code> of indexes for boolean columns
     */
    private static List<Integer> getHeaderBooleanIndexes() {
        List<Integer> booleanIndexes = new ArrayList<>();
        for (int i = 0; i < columnDataTypes.length; i++) {
            if (columnDataTypes[i] == boolean.class) {
                booleanIndexes.add(i);
            }
        }
        return booleanIndexes;
    }
}
