<<<<<<< HEAD


public class Tokenizer {

    /**
     * Convierte un script completo en un arreglo de tokens individuales.
     *
     * <p>El método elimina espacios al inicio y al final, y luego separa
     * el contenido usando uno o más espacios como delimitador.</p>
     *
     * @param input el script completo como cadena de texto
     * @return arreglo de tokens listos para ser ejecutados
     * @throws IllegalArgumentException si el input es null o vacío
     */
    public String[] tokenize(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Error: el script no puede ser nulo o vacío");
        }
        input = input.trim();        // elimina espacios extras al inicio y al final
        return input.split("\\s+"); // separa por uno o más espacios/tabs/saltos de línea
    }
=======
/**
 * Se encarga de convertir el script de texto en una lista de instrucciones separadas.
 */
public class Tokenizer {

    /** Toma el sript completo como texto y lo separa con .split en instrucciones individuales */
    public String[] tokenize(String input) {
        input = input.trim();  /** borra espacios extras al inicio y al final */
        return input.split("\\s+"); /** corta el texto en pedazos por los espacios y devuelve el resultado */
        

    }
>>>>>>> a6fdf5c1f0a516ebd8a339ded512d03217c068ca
}