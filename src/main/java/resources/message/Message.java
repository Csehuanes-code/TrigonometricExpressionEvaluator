package resources.message;

import domine.Parser;
import domine.Token;

import java.util.List;

public class Message {
    public static void askVariableName(String variableName) {
        System.out.println("Ingrese el valor de la variable '" + variableName + "': ");
    }

    public static void showInitialMessage() {
        System.out.println("=== Parser y Evaluador de Expresiones Trigonométricas ===");
        System.out.println("Expresiones válidas:");
        System.out.println("  - sin(3.14/2) + cos(0)*2^2");
        System.out.println("  - sin(x) + cos(y)");
        System.out.println("  - cos(x)^2 + sin(x)^2");
        System.out.println("  - tan(x^2 + sin(x))");
        System.out.println();
    }

    public static void askInput(){
        System.out.print("Ingrese una expresión (o 'salir' para terminar): ");
    }

    public static void showTokens(List<Token> tokens) {
        System.out.println("\n--- Tokens generados ---");
        for (Token token : tokens) {
            System.out.println("  " + token.getTokenType() + " : " + token.getLexeme());
        }
    }

    public static void showAstTree(Parser parser){
        System.out.println("\n--- Árbol de Sintaxis Abstracta (AST) ---");
        System.out.println(parser.getAstNode().toString());
    }

    public static void showResult(double result){
        System.out.println("\n--- Resultado ---");
        System.out.printf("  %.6f\n", result);
    }

    public static void showErrorMessage(Exception e){
        System.out.println("\n[ERROR] " + e.getMessage());
    }

    public static void showGoodByeMessage(){
        System.out.println("¡Hasta luego!");
    }
}
