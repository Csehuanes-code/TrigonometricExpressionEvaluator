package domine;

import domine.ast.*;
import lombok.Getter;
import resources.message.ExpectedMessage;
import resources.message.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Analizador Sintáctico (Parser) Descendente Recursivo para expresiones matemáticas.
 *
 * Este parser implementa una gramática LL(1) que reconoce expresiones aritméticas
 * y trigonométricas, construyendo un Árbol de Sintaxis Abstracta (AST) en el proceso.
 *
 * ========== GRAMÁTICA LL(1) IMPLEMENTADA ==========
 * A  -> B A'
 * A' -> + B A' | - B A' | λ
 * B  -> C B'
 * B' -> * C B' | / C B' | λ
 * C  -> D C'
 * C' -> ^ D C' | λ
 * D  -> Función(A) | (A) | Digito | Variable
 *
 * Donde:
 * - A maneja suma y resta (precedencia más baja)
 * - B maneja multiplicación y división (precedencia media)
 * - C maneja potenciación (precedencia más alta, asociativa a la derecha)
 * - D maneja factores: funciones, paréntesis, números y variables
 *
 * ========== CARACTERÍSTICAS ==========
 * - Análisis descendente recursivo (cada no-terminal es una función)
 * - Construcción de AST durante el parsing
 * - Manejo de precedencia de operadores
 * - Asociatividad correcta (potencia a la derecha)
 * - Detección y reporte de errores sintácticos
 * - Solicitud interactiva de valores para variables
 *
 * ========== PRECEDENCIA DE OPERADORES (de mayor a menor) ==========
 * 1. Funciones trigonométricas: sin(), cos(), tan()
 * 2. Paréntesis: ( )
 * 3. Potenciación: ^ (asociativa a la derecha)
 * 4. Multiplicación y División: *, /
 * 5. Suma y Resta: +, -
 *
 * Ejemplo de parsing:
 *   Entrada: "3 + 4 * 2"
 *   Tokens:  [DIGIT(3), PLUS, DIGIT(4), MULTIPLY, DIGIT(2)]
 *   AST:     BinaryOp(+)
 *              ├─ NumberNode(3)
 *              └─ BinaryOp(*)
 *                  ├─ NumberNode(4)
 *                  └─ NumberNode(2)
 *   Resultado: 11
 *
 * @author Compiladores - Trabajo Práctico 3
 * @version 1.0
 */
public class Parser {
    private List<Token> tokens;           // Lista de tokens a analizar
    private int currentTokenIndex;        // Índice del token actual
    private Token currentToken;           // Token siendo analizado actualmente

    @Getter
    private ASTNode astNode;              // Raíz del AST construido

    @Getter
    private final Map<String, Double> variableValues; // Valores de las variables

    private Scanner scanner;              // Scanner para leer valores de variables

    /**
     * Constructor del Parser.
     *
     * @param tokens Lista de tokens generada por el Lexer
     *
     * Inicializa el parser posicionándose en el primer token y preparando
     * las estructuras para almacenar el AST y los valores de variables.
     */
    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.currentTokenIndex = 0;
        this.currentToken = tokens.isEmpty() ? null : tokens.get(0);
        this.variableValues = new HashMap<>();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Reinicia el parser para poder procesar una nueva expresión.
     * Limpia todas las estructuras de datos pero mantiene el scanner activo.
     */
    public void resetParser(){
        this.tokens.clear();
        this.currentTokenIndex = 0;
        this.currentToken = null;
        astNode = null;
        // No cerramos el scanner para permitir múltiples expresiones
    }

    /**
     * Metodo principal de parsing.
     * Construye el AST completo a partir de los tokens.
     *
     * @return Nodo raíz del AST
     * @throws Exception Si hay errores sintácticos
     *
     * Verifica que todos los tokens sean consumidos (no queden tokens sin procesar)
     */
    private ASTNode parseToAST() throws Exception {
        ASTNode root = A(); // Comenzar desde el símbolo inicial de la gramática

        // Validar que no queden tokens sin procesar
        if (currentToken != null) {
            throw new Exception(ExpectedMessage.unValidTokens());
        }

        return root;
    }

    /**
     * Punto de entrada principal para el parsing y evaluación.
     *
     * @return Valor numérico de la expresión evaluada
     * @throws Exception Si hay errores sintácticos o de evaluación
     *
     * Proceso:
     * 1. Construir el AST (parsing)
     * 2. Identificar variables y solicitar sus valores
     * 3. Evaluar el AST y retornar el resultado
     */
    public double parse() throws Exception {
        ASTNode ast = parseToAST();

        // Solicitar valores de variables antes de evaluar
        requestVariableValues(ast);
        astNode = ast;

        return ast.evaluate();
    }

    /**
     * Recorre recursivamente el AST para encontrar todas las variables
     * y solicitar sus valores al usuario.
     *
     * @param node Nodo actual del AST
     *
     * Este metodo realiza un recorrido en profundidad del árbol buscando
     * nodos de tipo VariableNode y solicitando sus valores solo una vez.
     */
    private void requestVariableValues(ASTNode node) {
        if (node instanceof VariableNode) {
            // Caso base: encontramos una variable
            VariableNode varNode = (VariableNode) node;
            String varName = varNode.getName();

            // Solo solicitar el valor si no lo tenemos aún
            if (!variableValues.containsKey(varName)) {
                Message.askVariableName(varName);
                double value = scanner.nextDouble();
                variableValues.put(varName, value);
            }
        } else if (node instanceof BinaryOperationNode) {
            // Caso recursivo: operación binaria -> revisar ambos hijos
            BinaryOperationNode binOp = (BinaryOperationNode) node;
            requestVariableValues(binOp.getLeft());
            requestVariableValues(binOp.getRight());
        } else if (node instanceof FunctionNode) {
            // Caso recursivo: función -> revisar su argumento
            FunctionNode funcNode = (FunctionNode) node;
            requestVariableValues(funcNode.getArgument());
        }
        // NumberNode no necesita procesamiento (no tiene variables)
    }

    // ========== MÉTODOS DE PARSING (Uno por cada no-terminal de la gramática) ==========

    /**
     * A -> B A'
     *
     * Procesa expresiones con suma y resta (precedencia más baja).
     * Delega el trabajo a B para manejar operaciones de mayor precedencia primero.
     *
     * @return Nodo del AST que representa la expresión
     */
    private ASTNode A() throws Exception {
        ASTNode left = B();
        return A_prime(left);
    }

    /**
     * A' -> +B A' | -B A' | λ
     *
     * Maneja la recursión por la derecha para suma y resta.
     * Implementa asociatividad izquierda: 3-2-1 = (3-2)-1 = 0
     *
     * @param inherited Subárbol izquierdo heredado
     * @return Nodo del AST completo con las operaciones aplicadas
     *
     * El patrón "inherited" permite construir el árbol de izquierda a derecha
     * manteniendo la asociatividad correcta.
     */
    private ASTNode A_prime(ASTNode inherited) throws Exception {
        if (currentToken == null) {
            // Caso λ (epsilon): no hay más operadores
            return inherited;
        }

        if (currentToken.getTokenType() == TokenType.PLUS) {
            match(TokenType.PLUS);
            ASTNode right = B();
            // Crear nodo de suma y continuar procesando
            ASTNode node = new BinaryOperationNode("+", inherited, right);
            return A_prime(node);
        } else if (currentToken.getTokenType() == TokenType.MINUS) {
            match(TokenType.MINUS);
            ASTNode right = B();
            // Crear nodo de resta y continuar procesando
            ASTNode node = new BinaryOperationNode("-", inherited, right);
            return A_prime(node);
        }

        // Caso λ (epsilon): no es + ni -, devolver lo heredado
        return inherited;
    }

    /**
     * B -> C B'
     *
     * Procesa expresiones con multiplicación y división (precedencia media).
     */
    private ASTNode B() throws Exception {
        ASTNode left = C();
        return B_prime(left);
    }

    /**
     * B' -> *C B' | /C B' | λ
     *
     * Maneja la recursión por la derecha para multiplicación y división.
     * Asociatividad izquierda: 8/4/2 = (8/4)/2 = 1
     */
    private ASTNode B_prime(ASTNode inherited) throws Exception {
        if (currentToken == null) {
            return inherited;
        }

        if (currentToken.getTokenType() == TokenType.MULTIPLY) {
            match(TokenType.MULTIPLY);
            ASTNode right = C();
            ASTNode node = new BinaryOperationNode("*", inherited, right);
            return B_prime(node);
        } else if (currentToken.getTokenType() == TokenType.DIVIDE) {
            match(TokenType.DIVIDE);
            ASTNode right = C();
            ASTNode node = new BinaryOperationNode("/", inherited, right);
            return B_prime(node);
        }

        return inherited;
    }

    /**
     * C -> D C'
     *
     * Procesa expresiones con potenciación (precedencia más alta).
     */
    private ASTNode C() throws Exception {
        ASTNode left = D();
        return C_prime(left);
    }

    /**
     * C' -> ^D C' | λ
     *
     * Maneja la recursión por la derecha para potenciación.
     *
     * IMPORTANTE: Implementa asociatividad DERECHA para potencia.
     * Esto significa: 2^3^2 = 2^(3^2) = 2^9 = 512
     * No es: (2^3)^2 = 8^2 = 64
     *
     * La asociatividad derecha se logra construyendo primero el subárbol derecho
     * completo antes de crear el nodo de potencia.
     */
    private ASTNode C_prime(ASTNode inherited) throws Exception {
        if (currentToken == null) {
            return inherited;
        }

        if (currentToken.getTokenType() == TokenType.POWER) {
            match(TokenType.POWER);
            ASTNode right = D();
            // CLAVE: Procesar recursivamente el lado derecho primero
            right = C_prime(right);
            // Ahora crear el nodo con el subárbol derecho completo
            return new BinaryOperationNode("^", inherited, right);
        }

        return inherited;
    }

    /**
     * D -> Función(A) | (A) | Letra | Digito
     *
     * Procesa los elementos atómicos de la expresión:
     * - Funciones trigonométricas con sus argumentos
     * - Expresiones entre paréntesis
     * - Números
     * - Variables
     *
     * @return Nodo del AST representando el factor
     *
     * Este es el metodo más complejo ya que maneja múltiples casos.
     */
    private ASTNode D() throws Exception {
        if (currentToken == null) {
            throw new Exception("Expresión incompleta");
        }

        // Caso 1: Función trigonométrica -> Función(A)
        if (currentToken.getTokenType() == TokenType.SIN ||
                currentToken.getTokenType() == TokenType.COS ||
                currentToken.getTokenType() == TokenType.TAN) {

            String functionName = currentToken.getLexeme();
            match(currentToken.getTokenType());
            match(TokenType.LPARENT);
            ASTNode argument = A(); // El argumento es una expresión completa
            match(TokenType.RPARENT);

            return new FunctionNode(functionName, argument);
        }
        // Caso 2: Expresión entre paréntesis -> (A)
        else if (currentToken.getTokenType() == TokenType.LPARENT) {
            match(TokenType.LPARENT);
            ASTNode node = A(); // Procesar la expresión dentro del paréntesis
            match(TokenType.RPARENT);
            return node;
        }
        // Caso 3: Número constante -> Digito
        else if (currentToken.getTokenType() == TokenType.DIGIT) {
            double value = currentToken.getValue();
            match(TokenType.DIGIT);
            return new NumberNode(value);
        }
        // Caso 4: Variable simbólica -> Letra
        else if (currentToken.getTokenType() == TokenType.VARIABLE) {
            String varName = currentToken.getLexeme();
            match(TokenType.VARIABLE);
            return new VariableNode(varName, variableValues);
        }
        // Token no esperado
        else {
            throw new Exception(ExpectedMessage.unExpectedToken(currentToken.getLexeme()));
        }
    }

    /**
     * Verifica que el token actual sea del tipo esperado y avanza al siguiente.
     *
     * @param expectedType Tipo de token que se espera encontrar
     * @throws Exception Si el token actual no coincide con el esperado
     *
     * Este metodo es fundamental para el análisis sintáctico predictivo.
     * Valida que la secuencia de tokens siga las reglas de la gramática.
     */
    private void match(TokenType expectedType) throws Exception {
        if (currentToken == null) {
            throw new Exception(ExpectedMessage.expectedTokenTypeButFound(
                    expectedType, "el final de la expresión"));
        }

        if (currentToken.getTokenType() != expectedType) {
            throw new Exception(ExpectedMessage.expectedTokenTypeButFound(
                    expectedType, currentToken.getTokenType().toString()));
        }

        advance();
    }

    /**
     * Avanza al siguiente token en la secuencia.
     * Si no hay más tokens, currentToken se establece en null.
     */
    private void advance() {
        currentTokenIndex++;
        if (currentTokenIndex < tokens.size()) {
            currentToken = tokens.get(currentTokenIndex);
        } else {
            currentToken = null;
        }
    }
}
