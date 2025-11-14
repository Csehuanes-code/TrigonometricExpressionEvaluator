package domine;

import domine.ast.*;
import lombok.Getter;
import resources.message.ExpectedMessage;
import resources.message.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Parser {
    private List<Token> tokens;
    private int currentTokenIndex;
    private Token currentToken;
    @Getter
    private ASTNode astNode;
    @Getter
    private final Map<String, Double> variableValues;
    private Scanner scanner;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.currentTokenIndex = 0;
        this.currentToken = tokens.isEmpty() ? null : tokens.get(0);
        this.variableValues = new HashMap<>();
        this.scanner = new Scanner(System.in);
    }

    public void resetParser(){
        this.tokens.clear();
        this.currentTokenIndex = 0;
        this.currentToken = null;
        astNode = null;
        //scanner.close();
    }

    /**
     * Parse the expression and return the AST root node
     */
    private ASTNode parseToAST() throws Exception {
        ASTNode root = A();

        if (currentToken != null) {
            throw new Exception(ExpectedMessage.unValidTokens());
        }

        return root;
    }

    /**
     * Parse and evaluate the expression directly
     */
    public double parse() throws Exception {
        ASTNode ast = parseToAST();

        // Request variable values before evaluation
        requestVariableValues(ast);
        astNode =  ast;

        return ast.evaluate();
    }

    /**
     * Recursively find all variables in the AST and request their values
     */
    private void requestVariableValues(ASTNode node) {
        if (node instanceof VariableNode) {
            VariableNode varNode = (VariableNode) node;
            String varName = varNode.getName();

            if (!variableValues.containsKey(varName)) {
                Message.askVariableName(varName);
                double value = scanner.nextDouble();
                variableValues.put(varName, value);
            }
        } else if (node instanceof BinaryOperationNode) {
            BinaryOperationNode binOp = (BinaryOperationNode) node;
            requestVariableValues(binOp.getLeft());
            requestVariableValues(binOp.getRight());
        } else if (node instanceof FunctionNode) {
            FunctionNode funcNode = (FunctionNode) node;
            requestVariableValues(funcNode.getArgument());
        }
    }

    // A -> BA'
    private ASTNode A() throws Exception {
        ASTNode left = B();
        return A_prime(left);
    }

    // A' -> +BA' | -BA' | λ
    private ASTNode A_prime(ASTNode inherited) throws Exception {
        if (currentToken == null) {
            return inherited;
        }

        if (currentToken.getTokenType() == TokenType.PLUS) {
            match(TokenType.PLUS);
            ASTNode right = B();
            ASTNode node = new BinaryOperationNode("+", inherited, right);
            return A_prime(node);
        } else if (currentToken.getTokenType() == TokenType.MINUS) {
            match(TokenType.MINUS);
            ASTNode right = B();
            ASTNode node = new BinaryOperationNode("-", inherited, right);
            return A_prime(node);
        }

        // λ (epsilon)
        return inherited;
    }

    // B -> CB'
    private ASTNode B() throws Exception {
        ASTNode left = C();
        return B_prime(left);
    }

    // B' -> *CB' | /CB' | λ
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

        // λ (epsilon)
        return inherited;
    }

    // C -> DC'
    private ASTNode C() throws Exception {
        ASTNode left = D();
        return C_prime(left);
    }

    // C' -> ^DC' | λ
    private ASTNode C_prime(ASTNode inherited) throws Exception {
        if (currentToken == null) {
            return inherited;
        }

        if (currentToken.getTokenType() == TokenType.POWER) {
            match(TokenType.POWER);
            ASTNode right = D();
            // Para manejar múltiples potencias de derecha a izquierda
            right = C_prime(right);
            return new BinaryOperationNode("^", inherited, right);
        }

        // λ (epsilon)
        return inherited;
    }

    // D -> Función(A) | (A) | Letra | Digito
    private ASTNode D() throws Exception {
        if (currentToken == null) {
            throw new Exception("Expresión incompleta");
        }

        // Función(A)
        if (currentToken.getTokenType() == TokenType.SIN ||
                currentToken.getTokenType() == TokenType.COS ||
                currentToken.getTokenType() == TokenType.TAN) {

            String functionName = currentToken.getLexeme();
            match(currentToken.getTokenType());
            match(TokenType.LPARENT);
            ASTNode argument = A();
            match(TokenType.RPARENT);

            return new FunctionNode(functionName, argument);
        }
        // (A)
        else if (currentToken.getTokenType() == TokenType.LPARENT) {
            match(TokenType.LPARENT);
            ASTNode node = A();
            match(TokenType.RPARENT);
            return node;
        }
        // Digito
        else if (currentToken.getTokenType() == TokenType.DIGIT) {
            double value = currentToken.getValue();
            match(TokenType.DIGIT);
            return new NumberNode(value);
        }
        // Variable (Letra)
        else if (currentToken.getTokenType() == TokenType.VARIABLE) {
            String varName = currentToken.getLexeme();
            match(TokenType.VARIABLE);
            return new VariableNode(varName, variableValues);
        }
        else {
            throw new Exception(ExpectedMessage.unExpectedToken(currentToken.getLexeme()));
        }
    }

    private void match(TokenType expectedType) throws Exception {
        if (currentToken == null) {
            throw new Exception(ExpectedMessage.expectedTokenTypeButFound(expectedType, "el final de la expresión"));
        }

        if (currentToken.getTokenType() != expectedType) {
            throw new Exception(ExpectedMessage.expectedTokenTypeButFound(expectedType,currentToken.getTokenType().toString()));
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
}
