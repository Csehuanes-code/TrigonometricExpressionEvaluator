package domine.ast;

import lombok.Getter;

/**
 * Nodo que representa un número constante
 */
@Getter
public class NumberNode extends ASTNode {
    private double value;

    public NumberNode(double value) {
        this.value = value;
    }

    @Override
    public double evaluate() {
        return value;
    }

    @Override
    public String toTreeString(String indent) {
        return indent + "NumberNode(" + value + ")";
    }
}
