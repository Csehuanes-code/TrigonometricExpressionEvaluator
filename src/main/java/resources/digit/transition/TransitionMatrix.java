package resources.digit.transition;

/**
 * Matriz de transición del autómata finito para reconocimiento de números.
 * Implementa el patrón Singleton para garantizar una única instancia.
 *
 * La matriz tiene dimensiones [10][4] donde:
 * - Filas (10): Representan los estados del autómata
 * - Columnas (4): Representan los tipos de caracteres (dígito, punto, signo, exponente)
 *
 * Cada celda contiene el estado al que se transiciona al leer un carácter específico
 * desde un estado dado.
 */
public class TransitionMatrix {
    /** Instancia única de la matriz de transición (patrón Singleton) */
    public static TransitionMatrix instance;

    /** Matriz de transición de estados [estado_actual][tipo_caracter] -> estado_siguiente */
    public State[][] matrix = new State[10][4];

    /**
     * Constructor privado que inicializa la matriz de transición.
     * Define todas las transiciones válidas del autómata para reconocer números.
     */
    private TransitionMatrix() {
        // Estado 0 (Initial): Estado inicial
        matrix[0][0] = State.Digit;      // Dígito -> Digit
        matrix[0][1] = State.Point;      // Punto -> Point
        matrix[0][2] = State.Sign;       // Signo -> Sign
        matrix[0][3] = State.Error;      // Exponente -> Error

        // Estado 1 (Digit): Después de leer un dígito
        matrix[1][0] = State.Digit;      // Dígito -> Digit (más dígitos)
        matrix[1][1] = State.DigitPoint; // Punto -> DigitPoint (decimal)
        matrix[1][2] = State.Error;      // Signo -> Error
        matrix[1][3] = State.Exponent;   // Exponente -> Exponent (notación científica)

        // Estado 2 (Point): Después de leer un punto decimal
        matrix[2][0] = State.PointDigit; // Dígito -> PointDigit
        matrix[2][1] = State.Error;      // Punto -> Error
        matrix[2][2] = State.Error;      // Signo -> Error
        matrix[2][3] = State.Error;      // Exponente -> Error

        // Estado 3 (Sign): Después de leer un signo
        matrix[3][0] = State.Digit;      // Dígito -> Digit
        matrix[3][1] = State.Point;      // Punto -> Point
        matrix[3][2] = State.Error;      // Signo -> Error
        matrix[3][3] = State.Error;      // Exponente -> Error

        // Estado 4 (Exponent): Después de leer un exponente
        matrix[4][0] = State.ExponentDigit; // Dígito -> ExponentDigit
        matrix[4][1] = State.Error;         // Punto -> Error
        matrix[4][2] = State.ExponentSign;  // Signo -> ExponentSign
        matrix[4][3] = State.Error;         // Exponente -> Error

        // Estado 5 (PointDigit): Después de punto con dígitos
        matrix[5][0] = State.PointDigit; // Dígito -> PointDigit (más decimales)
        matrix[5][1] = State.Error;      // Punto -> Error
        matrix[5][2] = State.Error;      // Signo -> Error
        matrix[5][3] = State.Exponent;   // Exponente -> Exponent

        // Estado 6 (ExponentDigit): Después de exponente con dígitos
        matrix[6][0] = State.ExponentDigit; // Dígito -> ExponentDigit (más dígitos)
        matrix[6][1] = State.Error;         // Punto -> Error
        matrix[6][2] = State.Error;         // Signo -> Error
        matrix[6][3] = State.Error;         // Exponente -> Error

        // Estado 7 (Error): Estado de error
        matrix[7][0] = State.Error;      // Cualquier entrada -> Error
        matrix[7][1] = State.Error;
        matrix[7][2] = State.Error;
        matrix[7][3] = State.Error;

        // Estado 8 (DigitPoint): Después de dígito y punto
        matrix[8][0] = State.PointDigit; // Dígito -> PointDigit
        matrix[8][1] = State.Error;      // Punto -> Error
        matrix[8][2] = State.Error;      // Signo -> Error
        matrix[8][3] = State.Exponent;   // Exponente -> Exponent

        // Estado 9 (ExponentSign): Después de exponente y signo
        matrix[9][0] = State.ExponentDigit; // Dígito -> ExponentDigit
        matrix[9][1] = State.Error;         // Punto -> Error
        matrix[9][2] = State.Error;         // Signo -> Error
        matrix[9][3] = State.Error;         // Exponente -> Error
    }

    /**
     * Obtiene la instancia única de la matriz de transición.
     * Crea la instancia si no existe (patrón Singleton).
     *
     * @return La instancia única de TransitionMatrix
     */
    public static TransitionMatrix getInstance(){
        if(instance == null){ instance = new TransitionMatrix(); }
        return instance;
    }
}
