<<<<<<< HEAD
import java.util.ArrayDeque;
import java.util.Deque;

public class ScriptStack {

    /** Estructura interna que almacena los elementos de la pila. */
    private Deque<String> stack;

    /**
     * Constructor: crea una pila vacía lista para usarse.
     */
    public ScriptStack() {
        stack = new ArrayDeque<>();
    }

    /**
     * Mete un valor en la cima de la pila.
     *
     * @param value el valor a apilar (no debe ser null)
     * @throws IllegalArgumentException si value es null
     */
    public void push(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Error: no se puede apilar un valor null");
        }
        stack.push(value);
    }

    /**
     * Saca el valor de la cima de la pila y lo devuelve.
     *
     * @return el valor en la cima de la pila
     * @throws RuntimeException si la pila está vacía
     */
    public String pop() {
        if (stack.isEmpty()) {
            throw new RuntimeException("Error: intento de pop() sobre pila vacía");
        }
        return stack.pop();
    }

    /**
     * Devuelve el valor de la cima de la pila sin sacarlo.
     *
     * @return el valor en la cima de la pila
     * @throws RuntimeException si la pila está vacía
     */
    public String peek() {
        if (stack.isEmpty()) {
            throw new RuntimeException("Error: intento de peek() sobre pila vacía");
        }
        return stack.peek();
    }

    /**
     * Indica si la pila está vacía.
     *
     * @return {@code true} si la pila no contiene elementos, {@code false} en caso contrario
     */
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    /**
     * Devuelve la cantidad de elementos actualmente en la pila.
     *
     * @return número de elementos en la pila
     */
    public int size() {
        return stack.size();
    }

    /**
     * Devuelve una representación en texto de la pila (de tope a fondo).
     *
     * @return cadena con los elementos de la pila
     */
    @Override
    public String toString() {
        return stack.toString();
    }
=======
import java.util.ArrayDeque;
import java.util.Deque;

public class ScriptStack {
     /** Estructura interna que almacena los elementos de la pila */
    private Deque<String> stack;

    /**
     * Constructor: crea una pila vacía lista para usarse.
     */
    public ScriptStack() {
        stack = new ArrayDeque<>();
    }
    /** Mete un valor en la cima de lapila */
    public void push(String value) {
        stack.push(value);
    }
    /** Saca el valor de la cima de la pila y lo devuelve. Si la pila está vacía, lanza una excepción. */
    public String pop() {
        if (stack.isEmpty()) {
            throw new RuntimeException("Error: pila vacia");
        }
        return stack.pop();
    }
    /** Devuelve el valor de la cima de la pila sin sacarlo. Si la pila está vacía, lanza una excepción. */
    public String peek() {
        if (stack.isEmpty()) {
            throw new RuntimeException("Error: pila vacia");
        }
        return stack.peek();
    }
        /** Dice si la pila está vacía o no */
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    /** Dice cuántos elementos tiene la pila */
    public int size() {
        return stack.size();
    }
    @Override
    public String toString() {
        return stack.toString();
    }

    
>>>>>>> a6fdf5c1f0a516ebd8a339ded512d03217c068ca
}