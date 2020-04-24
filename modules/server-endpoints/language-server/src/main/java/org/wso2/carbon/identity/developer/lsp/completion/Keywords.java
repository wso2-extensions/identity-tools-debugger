package org.wso2.carbon.identity.developer.lsp.completion;

/**
 * The class contains the keywords.
 */
public class Keywords {

    String[] functionKeyword = new String[]{"Function", "function ${1:functionName} (){\n\t\n}"};

    String[] ifKeyword = new String[]{"If", "if (true) {\n\t\n}"};

    String[] tryKeyword = new String[]{"Try", "try {\n\t\n}catch(err) {\nconsole.log(err)\n}"};

    String[] classKeyword = new String[]{"Class", "class ClassName {\n" +
            "  constructor() {\n" +
            "  }\n" +
            "}"};

    String[] forKeyword = new String[]{"For", "for (var i = 0; i < 10; i++) {\n\t\n}"};

    String[] whileKeyword = new String[]{"While", "while (true) {\n\t\n}"};

    String[] doKeyword = new String[]{"Do", "do {\n\t\n}}while (true);"};

    String[] switchKeyword = new String[]{"Switch", "switch(expression) {\n" +
            "            case x:\n" +
            "                // code block\n" +
            "                break;\n" +
            "            case y:\n" +
            "                // code block\n" +
            "                break;\n" +
            "            default:\n" +
            "                // code block\n" +
            "        }"};

    String[] catchKeyword = new String[]{"Catch", "catch(err) {\nconsole.log(err);\n}"};

    String[] constructorKeyword = new String[]{"Constructor", "constructor()(){\n\t\n}"};

    String[] extendsKeyword = new String[]{"Extends", "extends"};

    String[] implementsKeyword = new String[]{"Implements", "implements"};

    String[] varKeyword = new String[]{"Var", "var"};

    String[] letKeyword = new String[]{"Let", "let"};

    String[] constKeyword = new String[]{"Const", "const"};

    String[] staticKeyword = new String[]{"Static", "static"};

    String[] privateKeyword = new String[]{"Private", "private"};

    String[] publicKeyword = new String[]{"Public", "public"};

    String[] returnKeyword = new String[]{"Return", "return"};

    String[] breakKeyword = new String[]{"Break", "break"};

    String[] continueKeyword = new String[]{"Continue", "continue"};

    String[] debuggerKeyword = new String[]{"Debugger", "debugger"};

    String[] finallyKeyword = new String[]{"Finally", "finally {\n\n}"};

    String[] throwKeyword = new String[]{"Throw", "throw "};

    String[] defaultKeyword = new String[]{"Default", "default:\n\t\n\tbreak;"};

    String[] caseKeyword = new String[]{"Case", "case:\n\t\n\tbreak;"};

    String[] nullKeyword = new String[]{"Null", "null"};

    String[] trueKeyword = new String[]{"True", "true"};

    String[] falseKeyword = new String[]{"False", "false"};
}
