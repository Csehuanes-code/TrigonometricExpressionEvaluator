package domine.ast;

import lombok.Getter;

/**
 * Nodo que representa una operación binaria (+, -, *, /, ^)
 */
@Getter
public class BinaryOperationNode extends ASTNode {
    private final String operator;
    private final ASTNode left;
    private final ASTNode right;

    public BinaryOperationNode(String operator, ASTNode left, ASTNode right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    @Override
    public double evaluate() throws Exception {
        double leftValue = left.evaluate();
        double rightValue = right.evaluate();

        return switch (operator) {
            case "+" -> leftValue + rightValue;
            case "-" -> leftValue - rightValue;
            case "*" -> leftValue * rightValue;
            case "/" -> {
                if (rightValue == 0) {
                    throw new Exception("División por cero");
                }
                yield leftValue / rightValue;
            }
            case "^" -> Math.pow(leftValue, rightValue);
            default -> throw new Exception("Operador desconocido: " + operator);
        };
    }

    @Override
    public String toTreeString(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append("BinaryOp(").append(operator).append(")\n");
        sb.append(left.toTreeString(indent + "  ├─ ")).append("\n");
        sb.append(right.toTreeString(indent + "  └─ "));
        return sb.toString();
    }
}
