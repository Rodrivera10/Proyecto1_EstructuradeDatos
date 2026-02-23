/**
 * Se encarga de convertir el script de texto en una lista de instrucciones separadas.
 */
public class Tokenizer {

    /** Toma el sript completo como texto y lo separa con .split en instrucciones individuales */
    public String[] tokenize(String input) {
        input = input.trim();  /** borra espacios extras al inicio y al final */
        return input.split("\\s+"); /** corta el texto en pedazos por los espacios y devuelve el resultado */
        

    }
}