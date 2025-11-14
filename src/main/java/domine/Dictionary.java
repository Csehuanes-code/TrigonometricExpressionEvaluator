package domine;

import java.util.HashMap;
import java.util.Map;

/**
 * Diccionario que mapea cadenas de texto (lexemas) a sus tipos de token correspondientes.
 *
 * Esta clase actúa como una tabla de símbolos para palabras reservadas y operadores,
 * permitiendo la identificación rápida de tokens durante el análisis léxico.
 *
 * Funcionalidades:
 * - Mapea funciones trigonométricas (sin, cos, tan) con soporte para variantes (sen, Sin, SIN, etc.)
 * - Mapea operadores aritméticos (+, -, *, /, ^)
 * - Mapea delimitadores (paréntesis)
 *
 * @author Compiladores - Trabajo Práctico 3
 * @version 1.0
 */
public class Dictionary {
    // Mapa que almacena la relación lexema -> tipo de token
    private final Map<String, TokenType> dictionary = new HashMap<>();

    /**
     * Constructor que inicializa el diccionario con todas las palabras reservadas
     * y símbolos especiales del lenguaje.
     */
    public Dictionary() {
        // ========== Funciones Trigonométricas ==========
        // Se aceptan múltiples variantes para soportar diferentes estilos de escritura

        // Seno: sin, sen (español)
        dictionary.put("sin", TokenType.SIN);
        dictionary.put("sen", TokenType.SIN);
        dictionary.put("Sin", TokenType.SIN);
        dictionary.put("Sen", TokenType.SIN);
        dictionary.put("SIN", TokenType.SIN);
        dictionary.put("SEN", TokenType.SIN);

        // Coseno: cos
        dictionary.put("cos", TokenType.COS);
        dictionary.put("Cos", TokenType.COS);
        dictionary.put("COS", TokenType.COS);

        // Tangente: tan
        dictionary.put("tan", TokenType.TAN);
        dictionary.put("Tan", TokenType.TAN);
        dictionary.put("TAN", TokenType.TAN);

        // ========== Operadores Aritméticos ==========
        dictionary.put("+", TokenType.PLUS);     // Suma
        dictionary.put("-", TokenType.MINUS);    // Resta / Negación unaria
        dictionary.put("*", TokenType.MULTIPLY); // Multiplicación
        dictionary.put("/", TokenType.DIVIDE);   // División
        dictionary.put("^", TokenType.POWER);    // Potenciación

        // ========== Delimitadores ==========
        dictionary.put("(", TokenType.LPARENT);  // Paréntesis izquierdo
        dictionary.put(")", TokenType.RPARENT);  // Paréntesis derecho
    }

    /**
     * Obtiene el tipo de token asociado a un lexema.
     *
     * @param lexeme El lexema a buscar (cadena de caracteres)
     * @return El TokenType correspondiente, o null si el lexema no está en el diccionario
     *
     * Ejemplo:
     *   getTokenType("sin") -> TokenType.SIN
     *   getTokenType("+")   -> TokenType.PLUS
     *   getTokenType("x")   -> null (es una variable, no está en el diccionario)
     */
    public TokenType getTokenType(String lexeme) {
        return dictionary.get(lexeme);
    }
}
