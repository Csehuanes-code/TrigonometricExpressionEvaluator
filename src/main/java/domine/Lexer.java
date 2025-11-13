package domine;

import resources.message.ExpectedMessage;

import java.util.ArrayList;
import java.util.List;

import static resources.digit.automaton.DigitAutomaton.isCorrectDigit;

public class Lexer {
    private String input;
    private int position;
    private final Dictionary dictionary;

    public Lexer(String input) {
        this.input = input.replaceAll("\\s+", ""); // Eliminar espacios en blanco
        this.position = 0;
        this.dictionary = new Dictionary();
    }

    public List<Token> tokenize() throws Exception {
        List<Token> tokens = new ArrayList<>();

        while (position < input.length()) {
            char currentChar = input.charAt(position);

            // Verificar si es un dígito o número
            if (Character.isDigit(currentChar) || currentChar == '.' ||
                    (currentChar == '-' && position + 1 < input.length() &&
                            (Character.isDigit(input.charAt(position + 1)) || input.charAt(position + 1) == '.'))) {
                tokens.add(readNumber());
            }
            // Verificar si es una letra (función o variable)
            else if (Character.isLetter(currentChar)) {
                tokens.add(readIdentifier());
            }
            // Operadores y paréntesis
            else if (isOperatorOrParenthesis(currentChar)) {
                tokens.add(readOperatorOrParenthesis());
            }
            else {
                throw new Exception(ExpectedMessage.unRecognizedCharacter(currentChar, position));
            }
        }

        return tokens;
    }

    private Token readNumber() throws Exception {
        StringBuilder number = new StringBuilder();

        // Manejar signo negativo
        if (position < input.length() && input.charAt(position) == '-') {
            number.append(input.charAt(position));
            position++;
        }

        // Leer parte entera y decimal
        while (position < input.length() &&
                (Character.isDigit(input.charAt(position)) || input.charAt(position) == '.')) {
            number.append(input.charAt(position));
            position++;
        }

        // Leer notación científica (E o e)
        if (position < input.length() &&
                (input.charAt(position) == 'E' || input.charAt(position) == 'e')) {
            number.append(input.charAt(position));
            position++;

            // Manejar signo en exponente
            if (position < input.length() &&
                    (input.charAt(position) == '+' || input.charAt(position) == '-')) {
                number.append(input.charAt(position));
                position++;
            }

            // Leer exponente
            while (position < input.length() && Character.isDigit(input.charAt(position))) {
                number.append(input.charAt(position));
                position++;
            }
        }

        String numStr = number.toString();
        if (!isCorrectDigit(numStr)) {
            throw new Exception(ExpectedMessage.unValidNumberFormat(numStr));
        }

        Token token = new Token();
        token.setLexeme(numStr);
        token.setTokenType(TokenType.DIGIT);
        token.setValue(Double.parseDouble(numStr));

        return token;
    }

    private Token readIdentifier() {
        StringBuilder identifier = new StringBuilder();

        while (position < input.length() && Character.isLetter(input.charAt(position))) {
            identifier.append(input.charAt(position));
            position++;
        }

        String id = identifier.toString();
        TokenType type = dictionary.getTokenType(id);

        Token token = new Token();
        token.setLexeme(id);

        if (type != null) {
            // Es una función (sin, cos, tan)
            token.setTokenType(type);
        } else {
            // Es una variable
            token.setTokenType(TokenType.VARIABLE);
        }

        return token;
    }

    private Token readOperatorOrParenthesis() {
        String op = String.valueOf(input.charAt(position));
        position++;

        TokenType type = dictionary.getTokenType(op);

        Token token = new Token();
        token.setLexeme(op);
        token.setTokenType(type);

        return token;
    }

    private boolean isOperatorOrParenthesis(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^' || c == '(' || c == ')';
    }
}
