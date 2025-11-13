package resources.message;

import domine.TokenType;

public class ExpectedMessage {
    public static String expectedTokenTypeButFound(TokenType expected, String found) {
        return ("Se esperaba" + expected + " pero se encontró " + found);
    }

    public static String unExpectedToken(String value) {
        return ("Token inesperado: " + value);
    }

    public static String unValidTokens(){
        return "Expresión no válida: tokens adicionales después del final";
    }

    public static String unRecognizedCharacter(char character, int index) {
        return ("Caracter no reconocido: " + character + " en posición " + index);
    }

    public static String unValidNumberFormat(String number) {
        return ("Número mal formado: " + number);
    }

    public static String unknownOperator(String operator) {
        return ("Operador desconocido: " + operator);
    }

    public static String dividedByZero(){
        return "División por cero";
    }

    public static String unknownFunction(String function) {
        return ("Función desconocida: " + function);
    }

    public static String unDefinedVariable(String variable) {
        return ("Variable no definida: " + variable);
    }
}
