package resources.digit.controller;

/**
 * Controlador para clasificar caracteres en el contexto de números.
 * Proporciona métodos para identificar signos, exponentes, puntos decimales
 * y mapear caracteres a índices de columna en la matriz de transición.
 */
public class CharacterController {
    /**
     * Verifica si un carácter es un signo (+ o -).
     *
     * @param sign El carácter a verificar
     * @return true si es '+' o '-', false en caso contrario
     */
    public static boolean isSign(char sign) {
        return sign == '+' || sign == '-';
    }

    /**
     * Verifica si un carácter es un exponente (e o E).
     *
     * @param exponent El carácter a verificar
     * @return true si es 'e' o 'E', false en caso contrario
     */
    public static boolean isExponent(char exponent) {
        return exponent == 'e' || exponent == 'E';
    }

    /**
     * Verifica si un carácter es un punto decimal.
     *
     * @param point El carácter a verificar
     * @return true si es '.', false en caso contrario
     */
    public static boolean isPoint(char point) {
        return point == '.';
    }

    /**
     * Mapea un carácter a su índice de columna correspondiente en la matriz de transición.
     * Los índices representan:
     * 0 - Dígito (0-9)
     * 1 - Punto decimal (.)
     * 2 - Signo (+/-)
     * 3 - Exponente (e/E)
     * 4 - Error (cualquier otro carácter)
     *
     * @param character El carácter a mapear
     * @return El índice de columna correspondiente
     */
    public static int indexByCharacter(char character) {
        if(Character.isDigit(character))  return 0;
        if(isPoint(character))  return 1;
        if(isSign(character))  return 2;
        if(isExponent(character)) return 3;
        else return 4; // Error - carácter no reconocido
    }
}
