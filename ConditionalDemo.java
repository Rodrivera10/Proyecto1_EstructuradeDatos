/**
 * Demostración de OP_IF / OP_NOTIF / OP_ELSE / OP_ENDIF.
 *
 * <p>El control de flujo en Bitcoin Script permite crear contratos con lógica
 * condicional. Los casos de uso reales incluyen:</p>
 * <ul>
 *   <li>Hash Time Locked Contracts (HTLC): fondos liberables con preimagen o tras un tiempo.</li>
 *   <li>Scripts de recuperación: ruta alternativa si el titular no puede firmar.</li>
 * </ul>
 *
 * <p>Reglas de ejecución:</p>
 * <ul>
 *   <li>OP_IF saca el tope; si es verdadero ejecuta el bloque THEN, si no el ELSE.</li>
 *   <li>OP_NOTIF es lo inverso: ejecuta THEN si el tope es falso.</li>
 *   <li>Los condicionales se pueden anidar sin límite (hasta que la memoria lo permita).</li>
 *   <li>Todo OP_IF / OP_NOTIF debe cerrarse con OP_ENDIF.</li>
 * </ul>
 *
 * @author 
 * @version 1.0
 */
public class ConditionalDemo {

    private static final String SEP = "=".repeat(60);

    /**
     * Ejecuta todos los escenarios de condicionales.
     *
     * @param args argumentos de línea de comandos (no usados)
     */
    public static void main(String[] args) {

        System.out.println(SEP);
        System.out.println("   DEMO OP_IF / OP_ELSE / OP_ENDIF");
        System.out.println(SEP);

     
        demostrar(
            "Escenario 1: OP_IF simple con condición VERDADERA",
            "OP_1 OP_IF OP_2 OP_ELSE OP_3 OP_ENDIF",
            true
        );

        
        demostrar(
            "Escenario 2: OP_IF simple con condición FALSA (ejecuta ELSE)",
            "OP_0 OP_IF OP_2 OP_ELSE OP_3 OP_ENDIF",
            true
        );

     
        demostrar(
            "Escenario 3: OP_NOTIF con condición 0 (ejecuta bloque)",
            "OP_0 OP_NOTIF OP_1 OP_ENDIF",
            true
        );

       
        demostrar(
            "Escenario 4: Condicionales anidados (IF dentro de IF)",
            "OP_1 OP_IF  OP_1 OP_IF OP_5 OP_ELSE OP_6 OP_ENDIF  OP_ELSE OP_7 OP_ENDIF",
            true
        );

    
        demostrar(
            "Escenario 5: IF externo falso — bloque interno SKIPEA completo",
            "OP_0 OP_IF  OP_1 OP_IF OP_9 OP_ENDIF  OP_ELSE OP_3 OP_ENDIF",
            true
        );

       
        demostrar(
            "Escenario 6: HTLC simplificado — preimagen correcta",
            "secreto OP_SHA256 SHA256_secreto OP_EQUAL OP_IF OP_1 OP_ELSE OP_0 OP_ENDIF",
            true
        );

        demostrar(
            "Escenario 7: HTLC simplificado — preimagen incorrecta",
            "dato_falso OP_SHA256 SHA256_secreto OP_EQUAL OP_IF OP_1 OP_ELSE OP_0 OP_ENDIF",
            true
        );
    }

    

    /**
     * Ejecuta un script y muestra su resultado.
     *
     * @param descripcion texto descriptivo
     * @param script      script completo a ejecutar
     * @param trace       si es {@code true}, muestra la pila paso a paso
     */
    private static void demostrar(String descripcion, String script, boolean trace) {
        System.out.println("\n" + descripcion);
        System.out.println("  Script: " + script);
        System.out.println();

        Interpreter interprete = new Interpreter(trace);

        try {
            boolean resultado = interprete.execute(script);
            System.out.println();
            System.out.println("  >>> RESULTADO: " + (resultado ? " VÁLIDO" : " INVÁLIDO"));
        } catch (RuntimeException e) {
            System.out.println();
            System.out.println("  >>> EXCEPCIÓN: " + e.getMessage());
            System.out.println("  >>> RESULTADO:  INVÁLIDO");
        }

        System.out.println(SEP);
    }
}
