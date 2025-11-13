package domine;

import java.util.HashMap;
import java.util.Map;

public class Dictionary {
    private final Map<String, TokenType> dictionary = new HashMap<>();

    public Dictionary() {
        // Funciones
        dictionary.put("sin", TokenType.SIN);
        dictionary.put("sen", TokenType.SIN);
        dictionary.put("cos", TokenType.COS);
        dictionary.put("tan", TokenType.TAN);
        dictionary.put("Sin", TokenType.SIN);
        dictionary.put("Sen", TokenType.SIN);
        dictionary.put("Cos", TokenType.COS);
        dictionary.put("Tan", TokenType.TAN);
        dictionary.put("SIN", TokenType.SIN);
        dictionary.put("SEN", TokenType.SIN);
        dictionary.put("COS", TokenType.COS);
        dictionary.put("TAN", TokenType.TAN);

        // Operadores
        dictionary.put("+", TokenType.PLUS);
        dictionary.put("-", TokenType.MINUS);
        dictionary.put("*", TokenType.MULTIPLY);
        dictionary.put("/", TokenType.DIVIDE);
        dictionary.put("^", TokenType.POWER);

        // Paréntesis
        dictionary.put("(", TokenType.LPARENT);
        dictionary.put(")", TokenType.RPARENT);
    }

    public TokenType getTokenType(String lexeme) {
        return dictionary.get(lexeme);
    }
}
