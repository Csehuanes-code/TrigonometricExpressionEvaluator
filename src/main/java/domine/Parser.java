package domine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Parser {
    private List<Token> tokens;
    private int currentTokenIndex;
    private Token currentToken;
    private Map<String, Double> variableValues;
    private Scanner scanner;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.currentTokenIndex = 0;
        this.currentToken = tokens.isEmpty() ? null : tokens.get(0);
        this.variableValues = new HashMap<>();
        this.scanner = new Scanner(System.in);
    }

    public double parse() throws Exception {
        double result = A();

        if (currentToken != null) {
            throw new Exception("Expresión no válida: tokens adicionales después del final");
        }

        return result;
    }

    // A -> BA'
    private double A() throws Exception {
        double value = B();
        return A_prime(value);
    }

    // A' -> +BA' | -BA' | λ
    private double A_prime(double inherited) throws Exception {
        if (currentToken == null) {
            return inherited;
        }

        if (currentToken.getTokenType() == TokenType.PLUS) {
            match(TokenType.PLUS);
            double value = B();
            return A_prime(inherited + value);
        } else if (currentToken.getTokenType() == TokenType.MINUS) {
            match(TokenType.MINUS);
            double value = B();
            return A_prime(inherited - value);
        }

        // λ (epsilon)
        return inherited;
    }

    // B -> CB'
    private double B() throws Exception {
        double value = C();
        return B_prime(value);
    }

    // B' -> *CB' | /CB' | λ
    private double B_prime(double inherited) throws Exception {
        if (currentToken == null) {
            return inherited;
        }

        if (currentToken.getTokenType() == TokenType.MULTIPLY) {
            match(TokenType.MULTIPLY);
            double value = C();
            return B_prime(inherited * value);
        } else if (currentToken.getTokenType() == TokenType.DIVIDE) {
            match(TokenType.DIVIDE);
            double value = C();
            if (value == 0) {
                throw new Exception("División por cero");
            }
            return B_prime(inherited / value);
        }

        // λ (epsilon)
        return inherited;
    }

    // C -> DC'
    private double C() throws Exception {
        double value = D();
        return C_prime(value);
    }

    // C' -> ^DC' | λ
    private double C_prime(double inherited) throws Exception {
        if (currentToken == null) {
            return inherited;
        }

        if (currentToken.getTokenType() == TokenType.POWER) {
            match(TokenType.POWER);
            double exponent = D();
            // Para manejar múltiples potencias de derecha a izquierda
            exponent = C_prime(exponent);
            return Math.pow(inherited, exponent);
        }

        // λ (epsilon)
        return inherited;
    }

    // D -> Función(A) | (A) | Letra | Digito
    private double D() throws Exception {
        if (currentToken == null) {
            throw new Exception("Expresión incompleta");
        }

        // Función(A)
        if (currentToken.getTokenType() == TokenType.SIN ||
                currentToken.getTokenType() == TokenType.COS ||
                currentToken.getTokenType() == TokenType.TAN) {

            TokenType functionType = currentToken.getTokenType();
            match(functionType);
            match(TokenType.LPARENT);
            double argValue = A();
            match(TokenType.RPARENT);

            return applyFunction(functionType, argValue);
        }
        // (A)
        else if (currentToken.getTokenType() == TokenType.LPARENT) {
            match(TokenType.LPARENT);
            double value = A();
            match(TokenType.RPARENT);
            return value;
        }
        // Digito
        else if (currentToken.getTokenType() == TokenType.DIGIT) {
            double value = currentToken.getValue();
            match(TokenType.DIGIT);
            return value;
        }
        // Variable (Letra)
        else if (currentToken.getTokenType() == TokenType.VARIABLE) {
            String varName = currentToken.getLexeme();

            // Si no tenemos el valor de la variable, pedirlo al usuario
            if (!variableValues.containsKey(varName)) {
                System.out.print("Ingrese el valor de la variable '" + varName + "': ");
                double value = scanner.nextDouble();
                variableValues.put(varName, value);
            }

            double value = variableValues.get(varName);
            match(TokenType.VARIABLE);
            return value;
        }
        else {
            throw new Exception("Token inesperado: " + currentToken.getLexeme());
        }
    }

    private void match(TokenType expectedType) throws Exception {
        if (currentToken == null) {
            throw new Exception("Se esperaba " + expectedType + " pero se encontró el final de la expresión");
        }

        if (currentToken.getTokenType() != expectedType) {
            throw new Exception("Se esperaba " + expectedType + " pero se encontró " + currentToken.getTokenType());
        }

        advance();
    }

    private void advance() {
        currentTokenIndex++;
        if (currentTokenIndex < tokens.size()) {
            currentToken = tokens.get(currentTokenIndex);
        } else {
            currentToken = null;
        }
    }

    private double applyFunction(TokenType functionType, double value) throws Exception {
        return switch (functionType) {
            case SIN -> Math.sin(value);
            case COS -> Math.cos(value);
            case TAN -> Math.tan(value);
            default -> throw new Exception("Función no reconocida: " + functionType);
        };
    }

}
