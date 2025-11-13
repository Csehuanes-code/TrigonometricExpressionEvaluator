package resources.digit.transition;

import lombok.Getter;

/**
 * Enumeración que representa los estados del autómata finito para reconocimiento de números.
 * Cada estado tiene un indicador de si es aceptable (estado final válido) y un índice único.
 *
 * Estados de aceptación (números válidos):
 * - Digit: Número entero simple
 * - PointDigit: Número decimal con dígitos después del punto
 * - ExponentDigit: Número en notación científica
 * - DigitPoint: Número entero seguido de punto decimal
 */
public enum State {
    /** Estado inicial del autómata */
    Initial(false, 0),

    /** Estado de dígito - número entero válido */
    Digit(true, 1),

    /** Estado de punto decimal sin dígitos después */
    Point(false,2),

    /** Estado de signo al inicio */
    Sign(false,3),

    /** Estado de exponente sin dígitos después */
    Exponent(false,4),

    /** Estado de punto decimal con dígitos - número decimal válido */
    PointDigit(true,5),

    /** Estado de exponente con dígitos - notación científica válida */
    ExponentDigit(true,6),

    /** Estado de error - secuencia inválida */
    Error(false,7),

    /** Estado de dígito seguido de punto - número válido */
    DigitPoint(true,8),

    /** Estado de exponente seguido de signo */
    ExponentSign(false, 9);

    /** Indica si este estado es un estado de aceptación (número válido) */
    private final boolean isAceptable;

    /** Índice único del estado para la matriz de transición
     * -- GETTER --
     *  Obtiene el índice único de este estado.
     *
     * @return El índice del estado
     */
    @Getter
    private final int index;

    /**
     * Constructor del estado.
     *
     * @param isAcceptable true si es un estado de aceptación
     * @param index Índice único del estado
     */
    State(boolean isAcceptable, int index){
        this.isAceptable=isAcceptable;
        this.index=index;
    }

    /**
     * Obtiene si este estado es aceptable (estado final válido).
     *
     * @return true si es un estado de aceptación
     */
    public boolean getIsAceptable(){
        return isAceptable;
    }

    /**
     * Obtiene el estado correspondiente a un índice dado.
     *
     * @param indice El índice del estado a buscar
     * @return El estado correspondiente, o Error si el índice no existe
     */
    public static State getEstadoPorIndice(int indice){
        return switch (indice) {
            case 0 -> Initial;
            case 1 -> Digit;
            case 2 -> Point;
            case 3 -> Sign;
            case 4 -> Exponent;
            case 5 -> PointDigit;
            case 6 -> ExponentDigit;
            case 8 -> DigitPoint;
            case 9 -> ExponentSign;
            default -> Error;
        };
    }
}
