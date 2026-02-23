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

    
}