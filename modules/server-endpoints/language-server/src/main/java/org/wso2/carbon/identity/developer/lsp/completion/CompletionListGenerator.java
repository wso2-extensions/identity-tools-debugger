/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.developer.lsp.completion;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.wso2.carbon.identity.application.authentication.framework.JsFunctionRegistry;

import java.util.HashMap;

/**
 * Class to generate completion list.
 */
public class CompletionListGenerator {

    private JsFunctionRegistry jsFunctionRegistry;

    private HashMap<String, JsonArray> keywordsMap;

    public CompletionListGenerator() {
        keywordsMap = new HashMap<>();
    }

    public void setJsFunctionRegistry(JsFunctionRegistry jsFunctionRegistry) {
        this.jsFunctionRegistry = jsFunctionRegistry;
    }

    /**
     * This function generate JSON object with required keywords for given scope.
     *
     * @param scope scope of typed string
     * @return JSON Object with required hashmap
     */
    public JsonObject getList(String scope) {

        JsonObject mainObj = new JsonObject();
        mainObj.add("re", generateJsonArray(scope.toLowerCase()));
        return mainObj;

    }

    /**
     * This function gets JSON array from keywordsMap.
     *
     * @param scope scope of typed string
     * @return JSON Object with required hashmap
     */
    public JsonArray generateJsonArray(String scope) {

        final Keywords keywords = new Keywords();
        if (!keywordsMap.containsKey(scope)) {
            switch (scope) {
                case "program":
                    HashMap<String, String[]> program = new HashMap<String, String[]>() {{
                        put("function", keywords.functionKeyword);
                        put("if", keywords.ifKeyword);
                        put("try", keywords.tryKeyword);
                        put("class", keywords.classKeyword);
                        put("for", keywords.forKeyword);
                        put("while", keywords.whileKeyword);
                        put("do", keywords.doKeyword);
                        put("var", keywords.varKeyword);
                        put("let", keywords.letKeyword);
                        put("const", keywords.constKeyword);
                        put("switch", keywords.switchKeyword);

                    }};
                    keywordsMap.put(scope, generateArray(program));
                    break;
                case "sourceelement":
                    HashMap<String, String[]> sourceElement = new HashMap<String, String[]>() {{
                        put("function", keywords.functionKeyword);
                        put("if", keywords.ifKeyword);
                        put("try", keywords.tryKeyword);
                        put("class", keywords.classKeyword);
                        put("for", keywords.forKeyword);
                        put("while", keywords.whileKeyword);
                        put("do", keywords.doKeyword);
                        put("var", keywords.varKeyword);
                        put("let", keywords.letKeyword);
                        put("const", keywords.constKeyword);
                        put("return", keywords.returnKeyword);
                        put("switch", keywords.switchKeyword);
                    }};
                    keywordsMap.put(scope, generateArray(sourceElement));
                    break;
                case "statement":
                    HashMap<String, String[]> statement = new HashMap<String, String[]>() {{
                        put("function", keywords.functionKeyword);
                        put("if", keywords.ifKeyword);
                        put("try", keywords.tryKeyword);
                        put("class", keywords.classKeyword);
                        put("for", keywords.forKeyword);
                        put("while", keywords.whileKeyword);
                        put("do", keywords.doKeyword);
                        put("var", keywords.varKeyword);
                        put("let", keywords.letKeyword);
                        put("const", keywords.constKeyword);
                        put("return", keywords.returnKeyword);
                        put("switch", keywords.switchKeyword);
                    }};
                    keywordsMap.put(scope, generateArray(statement));
                    break;
                case "block":
                    HashMap<String, String[]> block = new HashMap<String, String[]>() {{
                        put("function", keywords.functionKeyword);
                        put("if", keywords.ifKeyword);
                        put("try", keywords.tryKeyword);
                        put("class", keywords.classKeyword);
                        put("for", keywords.forKeyword);
                        put("while", keywords.whileKeyword);
                        put("do", keywords.doKeyword);
                        put("var", keywords.varKeyword);
                        put("let", keywords.letKeyword);
                        put("const", keywords.constKeyword);
                        put("continue", keywords.continueKeyword);
                        put("break", keywords.breakKeyword);
                        put("return", keywords.returnKeyword);
                        put("switch", keywords.switchKeyword);
                    }};
                    keywordsMap.put(scope, generateArray(block));
                    break;
                case "statementlist":
                    HashMap<String, String[]> statementList = new HashMap<String, String[]>() {{
                        put("function", keywords.functionKeyword);
                        put("if", keywords.ifKeyword);
                        put("try", keywords.tryKeyword);
                        put("class", keywords.classKeyword);
                        put("for", keywords.forKeyword);
                        put("while", keywords.whileKeyword);
                        put("do", keywords.doKeyword);
                        put("var", keywords.varKeyword);
                        put("let", keywords.letKeyword);
                        put("const", keywords.constKeyword);
                        put("switch", keywords.switchKeyword);
                    }};
                    keywordsMap.put(scope, generateArray(statementList));
                    break;
                case "variablestatement":
                    HashMap<String, String[]> variableStatement = new HashMap<String, String[]>() {{
                        put("variableStatement", new String[]{"c", "d"});
                    }};
                    keywordsMap.put(scope, generateArray(variableStatement));
                    break;
                case "variabledeclarationlist":
                    HashMap<String, String[]> variableDeclarationList = new HashMap<String, String[]>() {{
                        put("true", keywords.trueKeyword);
                        put("false", keywords.falseKeyword);
                        put("null", keywords.nullKeyword);
                    }};
                    keywordsMap.put(scope, generateArray(variableDeclarationList));
                    break;
                case "variabledeclaration":
                    HashMap<String, String[]> variableDeclaration = new HashMap<String, String[]>() {{
                        put("var", keywords.varKeyword);
                        put("let", keywords.letKeyword);
                        put("const", keywords.constKeyword);
                    }};
                    keywordsMap.put(scope, generateArray(variableDeclaration));
                    break;
                case "emptystatement":
                    HashMap<String, String[]> emptyStatement = new HashMap<String, String[]>() {{
                        put("emptyAStatement", new String[]{"c", "d"});
                    }};
                    keywordsMap.put(scope, generateArray(emptyStatement));
                    break;
                case "expressionstatement":
                    HashMap<String, String[]> expressionStatement = new HashMap<String, String[]>() {{
                        put("function", keywords.functionKeyword);
                        put("if", keywords.ifKeyword);
                        put("try", keywords.tryKeyword);
                        put("class", keywords.classKeyword);
                        put("for", keywords.forKeyword);
                        put("while", keywords.whileKeyword);
                        put("do", keywords.doKeyword);
                        put("var", keywords.varKeyword);
                        put("let", keywords.letKeyword);
                        put("const", keywords.constKeyword);
                        put("switch", keywords.switchKeyword);
                    }};
                    keywordsMap.put(scope, generateArray(expressionStatement));
                    break;
                case "ifstatement":
                    HashMap<String, String[]> ifStatement = new HashMap<String, String[]>() {{
                        put("if", keywords.ifKeyword);
                    }};
                    keywordsMap.put(scope, generateArray(ifStatement));
                    break;
                case "iterationstatement":
                    HashMap<String, String[]> iterationStatement = new HashMap<String, String[]>() {{
                        put("for", keywords.forKeyword);
                        put("while", keywords.whileKeyword);
                        put("do", keywords.doKeyword);
                    }};
                    keywordsMap.put(scope, generateArray(iterationStatement));
                    break;
                case "varmodifier":
                    HashMap<String, String[]> varModifier = new HashMap<String, String[]>() {{
                        put("var", keywords.varKeyword);
                        put("let", keywords.letKeyword);
                        put("const", keywords.constKeyword);
                    }};
                    keywordsMap.put(scope, generateArray(varModifier));
                    break;
                case "continuestatement":
                    HashMap<String, String[]> continueStatement = new HashMap<String, String[]>() {{
                        put("continue", keywords.continueKeyword);
                    }};
                    keywordsMap.put(scope, generateArray(continueStatement));
                    break;
                case "breakstatement":
                    HashMap<String, String[]> breakStatement = new HashMap<String, String[]>() {{
                        put("break", keywords.breakKeyword);
                    }};
                    keywordsMap.put(scope, generateArray(breakStatement));
                    break;
                case "returnstatement":
                    final HashMap<String, String[]> returnStatement = new HashMap<String, String[]>() {{
                        put("return", keywords.returnKeyword);
                    }};
                    keywordsMap.put(scope, generateArray(returnStatement));
                    break;
                case "withstatement":
                    HashMap<String, String[]> withStatement = new HashMap<String, String[]>() {{
                        put("withStatement", new String[]{"c", "d"});
                    }};
                    keywordsMap.put(scope, generateArray(withStatement));
                    break;
                case "switchstatement":
                    HashMap<String, String[]> switchStatement = new HashMap<String, String[]>() {{
                        put("switchStatement", new String[]{"c", "d"});
                    }};
                    keywordsMap.put(scope, generateArray(switchStatement));
                    break;
                case "caseblock":
                    HashMap<String, String[]> caseBlock = new HashMap<String, String[]>() {{
                        put("case", keywords.caseKeyword);
                    }};
                    keywordsMap.put(scope, generateArray(caseBlock));
                    break;
                case "caseclauses":
                    HashMap<String, String[]> caseClauses = new HashMap<String, String[]>() {{
                        put("case", keywords.caseKeyword);
                        put("if", keywords.ifKeyword);
                        put("try", keywords.tryKeyword);
                        put("for", keywords.forKeyword);
                        put("while", keywords.whileKeyword);
                        put("do", keywords.doKeyword);
                        put("var", keywords.varKeyword);
                        put("let", keywords.letKeyword);
                        put("const", keywords.constKeyword);
                        put("switch", keywords.switchKeyword);
                    }};
                    keywordsMap.put(scope, generateArray(caseClauses));
                    break;
                case "caseclause":
                    HashMap<String, String[]> caseClause = new HashMap<String, String[]>() {{
                        put("caseclause", new String[]{"c", "d"});
                    }};
                    keywordsMap.put(scope, generateArray(caseClause));
                    break;
                case "defaultclause":
                    HashMap<String, String[]> defaultClause = new HashMap<String, String[]>() {{
                        put("default", keywords.defaultKeyword);
                        put("case", keywords.caseKeyword);
                    }};
                    keywordsMap.put(scope, generateArray(defaultClause));
                    break;
                case "labelledstatement":
                    HashMap<String, String[]> labelledStatement = new HashMap<String, String[]>() {{
                        put("labelled", new String[]{"c", "d"});
                    }};
                    keywordsMap.put(scope, generateArray(labelledStatement));
                    break;
                case "throwstatement":
                    HashMap<String, String[]> throwStatement = new HashMap<String, String[]>() {{
                        put("throw", keywords.throwKeyword);
                    }};
                    keywordsMap.put(scope, generateArray(throwStatement));
                    break;
                case "trystatement":
                    HashMap<String, String[]> tryStatement = new HashMap<String, String[]>() {{
                        put("try", keywords.tryKeyword);
                    }};
                    keywordsMap.put(scope, generateArray(tryStatement));
                    break;
                case "catchproduction":
                    HashMap<String, String[]> catchProduction = new HashMap<String, String[]>() {{
                        put("catch", keywords.catchKeyword);
                    }};
                    keywordsMap.put(scope, generateArray(catchProduction));
                    break;
                case "finallyproduction":
                    HashMap<String, String[]> finallyProduction = new HashMap<String, String[]>() {{
                        put("finally", keywords.finallyKeyword);
                    }};
                    keywordsMap.put(scope, generateArray(finallyProduction));
                    break;
                case "debuggerstatement":
                    HashMap<String, String[]> debuggerStatement = new HashMap<String, String[]>() {{
                        put("debugger", keywords.debuggerKeyword);
                    }};
                    keywordsMap.put(scope, generateArray(debuggerStatement));
                    break;
                case "functiondeclaration":
                    HashMap<String, String[]> functionDeclaration = new HashMap<String, String[]>() {{
                        put("function", new String[]{"Function", "function"});
                    }};
                    keywordsMap.put(scope, generateArray(functionDeclaration));
                    break;
                case "classdeclaration":
                    HashMap<String, String[]> classDeclaration = new HashMap<String, String[]>() {{
                        put("class", new String[]{"Class", "class"});
                    }};
                    keywordsMap.put(scope, generateArray(classDeclaration));
                    break;
                case "classtail":
                    HashMap<String, String[]> classTail = new HashMap<String, String[]>() {{
                        put("extends", keywords.extendsKeyword);
                        put("implements", keywords.implementsKeyword);
                    }};
                    keywordsMap.put(scope, generateArray(classTail));
                    break;
                case "classelement":
                    HashMap<String, String[]> classElement = new HashMap<String, String[]>() {{
                        put("function", keywords.functionKeyword);
                        put("constructor", keywords.constructorKeyword);
                        put("class", keywords.classKeyword);
                        put("static", keywords.staticKeyword);
                        put("private", keywords.privateKeyword);
                        put("public", keywords.publicKeyword);
                        put("var", keywords.varKeyword);
                        put("let", keywords.letKeyword);
                        put("const", keywords.constKeyword);

                    }};
                    keywordsMap.put(scope, generateArray(classElement));
                    break;
                case "methoddefinition":
                    HashMap<String, String[]> methodDefinition = new HashMap<String, String[]>() {{
                        put("methodDEfinition", new String[]{"c", "d"});
                    }};
                    keywordsMap.put(scope, generateArray(methodDefinition));
                    break;
                case "generatormethod":
                    HashMap<String, String[]> generatorMethod = new HashMap<String, String[]>() {{
                        put("generatorMethod", new String[]{"c", "d"});
                    }};
                    keywordsMap.put(scope, generateArray(generatorMethod));
                    break;
                case "formalparameterlist":
                    HashMap<String, String[]> formalParameterList = new HashMap<String, String[]>() {{
                        put("formalparametrListt", new String[]{"c", "d"});
                    }};
                    keywordsMap.put(scope, generateArray(formalParameterList));
                    break;
                case "formalparameterarg":
                    HashMap<String, String[]> formalParameterArg = new HashMap<String, String[]>() {{
                        put("formalParamaterArg", new String[]{"c", "d"});
                    }};
                    keywordsMap.put(scope, generateArray(formalParameterArg));
                    break;
                case "lastformalparameterarg":
                    HashMap<String, String[]> lastFormalParameterArg = new HashMap<String, String[]>() {{
                        put("lastFormalPArameter", new String[]{"c", "d"});
                    }};
                    keywordsMap.put(scope, generateArray(lastFormalParameterArg));
                    break;
                case "functionbody":
                    HashMap<String, String[]> functionBody = new HashMap<String, String[]>() {{
                        put("function", keywords.functionKeyword);
                        put("if", keywords.ifKeyword);
                        put("try", keywords.tryKeyword);
                        put("for", keywords.forKeyword);
                        put("while", keywords.whileKeyword);
                        put("do", keywords.doKeyword);
                        put("var", keywords.varKeyword);
                        put("let", keywords.letKeyword);
                        put("const", keywords.constKeyword);
                        put("switch", keywords.switchKeyword);

                    }};
                    if (jsFunctionRegistry != null) {
                        for (String jsFunctionFromRegistry : jsFunctionRegistry.getSubsystemFunctionsMap(
                                JsFunctionRegistry.Subsystem.SEQUENCE_HANDLER).keySet()) {
                            functionBody.put(jsFunctionFromRegistry, new String[]{"Code", jsFunctionFromRegistry});
                        }
                    }
                    keywordsMap.put(scope, generateArray(functionBody));
                    break;
                case "sourceelements":
                    HashMap<String, String[]> sourceElements = new HashMap<String, String[]>() {{
                        put("sourceElement", new String[]{"c", "d"});
                    }};
                    keywordsMap.put(scope, generateArray(sourceElements));
                    break;
                case "arrayliteral":
                    HashMap<String, String[]> arrayLiteral = new HashMap<String, String[]>() {{
                        put("arrayLiteral", new String[]{"c", "d"});
                    }};
                    keywordsMap.put(scope, generateArray(arrayLiteral));
                    break;
                case "elementlist":
                    HashMap<String, String[]> elementList = new HashMap<String, String[]>() {{
                        put("elementList", new String[]{"c", "d"});
                    }};
                    keywordsMap.put(scope, generateArray(elementList));
                    break;
                case "lastelement":
                    HashMap<String, String[]> lastElement = new HashMap<String, String[]>() {{
                        put("lastElement", new String[]{"c", "d"});
                    }};
                    keywordsMap.put(scope, generateArray(lastElement));
                    break;
                case "objectliteral":
                    HashMap<String, String[]> objectLiteral = new HashMap<String, String[]>() {{
                        put("objectiveLiteral", new String[]{"c", "d"});
                    }};
                    keywordsMap.put(scope, generateArray(objectLiteral));
                    break;
                case "propertyassignment":
                    HashMap<String, String[]> propertyAssignment = new HashMap<String, String[]>() {{
                        put("propertyAssignment", new String[]{"c", "d"});
                    }};
                    keywordsMap.put(scope, generateArray(propertyAssignment));
                    break;
                case "propertyname":
                    HashMap<String, String[]> propertyName = new HashMap<String, String[]>() {{
                        put("propertyName", new String[]{"c", "d"});
                    }};
                    keywordsMap.put(scope, generateArray(propertyName));
                    break;
                case "arguments":
                    HashMap<String, String[]> arguments = new HashMap<String, String[]>() {{
                        put("argument", new String[]{"c", "d"});
                    }};
                    keywordsMap.put(scope, generateArray(arguments));
                    break;
                case "lastargument":
                    HashMap<String, String[]> lastArgument = new HashMap<String, String[]>() {{
                        put("lastargument", new String[]{"c", "d"});
                    }};
                    keywordsMap.put(scope, generateArray(lastArgument));
                    break;
                case "expressionsequence":
                    HashMap<String, String[]> expressionSequence = new HashMap<String, String[]>() {{
                        put("function", keywords.functionKeyword);
                        put("if", keywords.ifKeyword);
                        put("try", keywords.tryKeyword);
                        put("class", keywords.classKeyword);
                        put("for", keywords.forKeyword);
                        put("while", keywords.whileKeyword);
                        put("do", keywords.doKeyword);
                        put("var", keywords.varKeyword);
                        put("let", keywords.letKeyword);
                        put("const", keywords.constKeyword);
                        put("switch", keywords.switchKeyword);
                    }};
                    keywordsMap.put(scope, generateArray(expressionSequence));
                    break;
                case "singleexpression":
                    HashMap<String, String[]> singleExpression = new HashMap<String, String[]>() {{
                        put("singleExpression", new String[]{"c", "d"});
                    }};
                    keywordsMap.put(scope, generateArray(singleExpression));
                    break;
                case "arrowfunctionparameters":
                    HashMap<String, String[]> arrowFunctionParameters = new HashMap<String, String[]>() {{
                        put("arrowFunction", new String[]{"c", "d"});
                    }};
                    keywordsMap.put(scope, generateArray(arrowFunctionParameters));
                    break;
                case "arrowfunctionbody":
                    HashMap<String, String[]> arrowFunctionBody = new HashMap<String, String[]>() {{
                        put("arrowfunction", new String[]{"c", "d"});
                    }};
                    keywordsMap.put(scope, generateArray(arrowFunctionBody));
                    break;
                case "assignmentoperator":
                    HashMap<String, String[]> assignmentOperator = new HashMap<String, String[]>() {{
                        put("assignmentOpertaor", new String[]{"c", "d"});
                    }};
                    keywordsMap.put(scope, generateArray(assignmentOperator));
                    break;
                case "literal":
                    HashMap<String, String[]> literal = new HashMap<String, String[]>() {{
                        put("literal", new String[]{"c", "d"});
                    }};
                    keywordsMap.put(scope, generateArray(literal));
                    break;
                case "numericliteral":
                    HashMap<String, String[]> numericLiteral = new HashMap<String, String[]>() {{
                        put("numericLiteral", new String[]{"c", "d"});
                    }};

                    keywordsMap.put(scope, generateArray(numericLiteral));
                    break;
                case "identifiername":
                    HashMap<String, String[]> identifierName = new HashMap<String, String[]>() {{
                        put("identifierNAme", new String[]{"c", "d"});
                    }};
                    keywordsMap.put(scope, generateArray(identifierName));
                    break;
                case "reservedword":
                    HashMap<String, String[]> reservedWord = new HashMap<String, String[]>() {{
                        put("reserved", new String[]{"c", "d"});
                    }};
                    keywordsMap.put(scope, generateArray(reservedWord));
                    break;
                case "keyword":
                    HashMap<String, String[]> keyword = new HashMap<String, String[]>() {{
                        put("keyword", new String[]{"c", "d"});
                    }};
                    keywordsMap.put(scope, generateArray(keyword));
                    break;
                case "getter":
                    HashMap<String, String[]> getter = new HashMap<String, String[]>() {{
                        put("getter", new String[]{"c", "d"});
                    }};
                    keywordsMap.put(scope, generateArray(getter));
                    break;
                case "setter":
                    HashMap<String, String[]> setter = new HashMap<String, String[]>() {{
                        put("setter", new String[]{"c", "d"});
                    }};
                    keywordsMap.put(scope, generateArray(setter));
                    break;
                case "eos":
                    HashMap<String, String[]> eos = new HashMap<String, String[]>() {{
                        put(";", new String[]{"End of Statement", ";"});
                    }};
                    keywordsMap.put(scope, generateArray(eos));
                    break;
                default:
                    HashMap<String, String[]> whenNull = new HashMap<String, String[]>() {{
                        put(scope, new String[]{"c", "d"});
                    }};
                    keywordsMap.put(scope, generateArray(whenNull));
                    break;
            }
        }
        return keywordsMap.get(scope);
    }

    private JsonArray generateArray(HashMap<String, String[]> keywords) {

        JsonArray arr = new JsonArray();
        for (String key : keywords.keySet()) {
            JsonObject json = new JsonObject();
            json.addProperty("label", key);
            json.addProperty("kind", keywords.get(key)[0]);
            json.addProperty("insertText", keywords.get(key)[1]);
            arr.add(json);
        }
        return arr;
    }

}
