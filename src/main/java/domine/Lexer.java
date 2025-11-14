package domine;

import resources.message.ExpectedMessage;
import java.util.ArrayList;
import java.util.List;
import static resources.digit.automaton.DigitAutomaton.isCorrectDigit;

/**
 * Analizador Léxico (Lexer) para expresiones aritméticas y trigonométricas.
 *
 * El Lexer es la primera fase del proceso de compilación. Su función principal es:
 * 1. Leer la cadena de entrada carácter por carácter
 * 2. Agrupar caracteres en unidades significativas llamadas "tokens"
 * 3. Clasificar cada token según su tipo (número, operador, función, variable, etc.)
 * 4. Eliminar espacios en blanco y elementos irrelevantes
 *
 * Tipos de tokens reconocidos:
 * - Números: enteros, decimales, notación científica (3, 0.5, .2, -2, 2E2, -25.32)
 * - Operadores: +, -, *, /, ^
 * - Funciones: sin, cos, tan
 * - Paréntesis: (, )
 * - Variables: x, y, z, cualquier identificador alfabético
 *
 * Proceso de tokenización:
 *   Entrada:  "sin(x) + 2 * 3"
 *   Salida:   [SIN, LPARENT, VARIABLE(x), RPARENT, PLUS, DIGIT(2), MULTIPLY, DIGIT(3)]
 *
 * @author Compiladores - Trabajo Práctico 3
 * @version 1.0
 */
public class Lexer {
    private String input;              // Cadena de entrada a tokenizar
    private int position;              // Posición actual en la cadena
    private final Dictionary dictionary; // Diccionario de palabras reservadas

    /**
     * Constructor del Lexer.
     *
     * @param input Cadena de entrada que contiene la expresión a tokenizar
     *
     * Nota: Los espacios en blanco se eliminan automáticamente ya que no son
     * significativos en este lenguaje (no afectan la semántica)
     */
    public Lexer(String input) {
        this.input = input.replaceAll("\\s+", ""); // Eliminar todos los espacios en blanco
        this.position = 0;
        this.dictionary = new Dictionary();
    }

    /**
     * Método principal de tokenización.
     * Convierte la cadena de entrada en una lista de tokens.
     *
     * @return Lista de tokens identificados en la entrada
     * @throws Exception Si encuentra un carácter no reconocido o un formato inválido
     *
     * Algoritmo:
     * 1. Mientras haya caracteres por procesar:
     *    - Si es un dígito o punto, leer número completo
     *    - Si es una letra, leer identificador (función o variable)
     *    - Si es un operador/paréntesis, crear token correspondiente
     *    - Si no coincide con nada, lanzar error
     */
    public List<Token> tokenize() throws Exception {
        List<Token> tokens = new ArrayList<>();

        while (position < input.length()) {
            char currentChar = input.charAt(position);

            // ========== Reconocimiento de Números ==========
            // Un número puede comenzar con:
            // - Dígito: 3, 42
            // - Punto: .5, .123
            // - Signo menos seguido de dígito o punto: -2, -.5
            if (Character.isDigit(currentChar) || currentChar == '.') {
                tokens.add(readNumber());
            }
            // ========== Reconocimiento de Identificadores ==========
            // Los identificadores comienzan con letra y pueden ser:
            // - Funciones: sin, cos, tan
            // - Variables: x, y, z, abc, etc.
            else if (Character.isLetter(currentChar)) {
                tokens.add(readIdentifier());
            }
            // ========== Reconocimiento de Operadores y Paréntesis ==========
            else if (isOperatorOrParenthesis(currentChar)) {
                tokens.add(readOperatorOrParenthesis());
            }
            // ========== Carácter No Reconocido ==========
            else {
                throw new Exception(ExpectedMessage.unRecognizedCharacter(currentChar, position));
            }
        }

        return tokens;
    }

    /**
     * Lee un número completo desde la posición actual.
     * Soporta múltiples formatos numéricos:
     * - Enteros: 42
     * - Decimales: 3.14, .5
     * - Negativos: -2, -3.5
     * - Notación científica: 2E2, -1.5e-3
     *
     * @return Token de tipo DIGIT con el valor numérico parseado
     * @throws Exception Si el formato del número es inválido
     *
     * Algoritmo:
     * 1. Leer signo opcional (-)
     * 2. Leer parte entera y decimal
     * 3. Leer exponente opcional (E/e con signo y dígitos)
     * 4. Validar formato con autómata
     * 5. Convertir a double y crear token
     */
    private Token readNumber() throws Exception {
        StringBuilder number = new StringBuilder();

        // Paso 1: Manejar signo negativo opcional
        if (position < input.length() && input.charAt(position) == '-') {
            number.append(input.charAt(position));
            position++;
        }

        // Paso 2: Leer parte entera y decimal
        // Acepta secuencias como: 42, 3.14, .5
        while (position < input.length() &&
                (Character.isDigit(input.charAt(position)) || input.charAt(position) == '.')) {
            number.append(input.charAt(position));
            position++;
        }

        // Paso 3: Leer notación científica opcional (E o e)
        // Formato: [número]E[signo opcional][exponente]
        // Ejemplos: 2E2 = 200, 1.5e-3 = 0.0015
        if (position < input.length() &&
                (input.charAt(position) == 'E' || input.charAt(position) == 'e')) {
            number.append(input.charAt(position));
            position++;

            // Leer signo del exponente (+ o -)
            if (position < input.length() &&
                    (input.charAt(position) == '+' || input.charAt(position) == '-')) {
                number.append(input.charAt(position));
                position++;
            }

            // Leer dígitos del exponente
            while (position < input.length() && Character.isDigit(input.charAt(position))) {
                number.append(input.charAt(position));
                position++;
            }
        }

        String numStr = number.toString();

        // Paso 4: Validar formato con autómata de estados finitos
        // isCorrectDigit() verifica que el número esté bien formado
        if (!isCorrectDigit(numStr)) {
            throw new Exception(ExpectedMessage.unValidNumberFormat(numStr));
        }

        // Paso 5: Crear token con el valor numérico
        Token token = new Token();
        token.setLexeme(numStr);
        token.setTokenType(TokenType.DIGIT);
        token.setValue(Double.parseDouble(numStr));

        return token;
    }

    /**
     * Lee un identificador completo (secuencia de letras).
     * Puede ser una función trigonométrica o una variable.
     *
     * @return Token de tipo función (SIN, COS, TAN) o VARIABLE
     *
     * Proceso:
     * 1. Leer todas las letras consecutivas
     * 2. Consultar diccionario para ver si es palabra reservada (función)
     * 3. Si está en el diccionario -> Token de función
     * 4. Si no está en el diccionario -> Token de variable
     *
     * Ejemplos:
     *   "sin" -> Token(SIN)
     *   "x"   -> Token(VARIABLE, "x")
     *   "abc" -> Token(VARIABLE, "abc")
     */
    private Token readIdentifier() {
        StringBuilder identifier = new StringBuilder();

        // Leer todas las letras consecutivas
        while (position < input.length() && Character.isLetter(input.charAt(position))) {
            identifier.append(input.charAt(position));
            position++;
        }

        String id = identifier.toString();
        TokenType type = dictionary.getTokenType(id);

        Token token = new Token();
        token.setLexeme(id);

        if (type != null) {
            // Es una función trigonométrica reconocida
            token.setTokenType(type);
        } else {
            // Es una variable (identificador no reconocido como palabra reservada)
            token.setTokenType(TokenType.VARIABLE);
        }

        return token;
    }

    /**
     * Lee un operador o paréntesis (símbolos de un solo carácter).
     *
     * @return Token correspondiente al operador o paréntesis
     *
     * Operadores reconocidos: +, -, *, /, ^
     * Delimitadores: (, )
     */
    private Token readOperatorOrParenthesis() {
        String op = String.valueOf(input.charAt(position));
        position++;

        TokenType type = dictionary.getTokenType(op);

        Token token = new Token();
        token.setLexeme(op);
        token.setTokenType(type);

        return token;
    }

    /**
     * Verifica si un carácter es un operador aritmético o paréntesis.
     *
     * @param c Carácter a verificar
     * @return true si es operador o paréntesis, false en caso contrario
     */
    private boolean isOperatorOrParenthesis(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^' || c == '(' || c == ')';
    }
}
