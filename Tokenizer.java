

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
}