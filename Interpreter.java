
public class Interpreter {
    
    private ScriptStack stack;
    private boolean trace;

    public Interpreter(boolean trace) {
        this.stack = new ScriptStack(); /** crea una pila vacia nueva */
        this.trace = trace;  /** guarda si el trace esta activo */
    }

    /** Ejecuta un script de instrucciones */
    public boolean execute(String script) {

        //1. Tokenizar el script
        Tokenizer tokenizer = new Tokenizer(); /** crea un nuevo tokenizador */
        String[] tokens = tokenizer.tokenize(script); /** convierte el script en tokens */

        //2. Recorrer cada token uno por uno
        for (String token: tokens) {


        switch (token) {

        // ── GRUPO 1: Literales ─────────────
            case "OP_0":
            case "OP_FALSE":
                stack.push("0");
                break;

            case "OP_1":
            case "OP_TRUE":
                stack.push("1");
                break;
            
            case "OP_2":
                stack.push("2");
                break;
            
            case "OP_3":
                stack.push("3");
                break;
            
            case "OP_4":
                stack.push("4");
                break;
            
            case "OP_5":
                stack.push("5");
                break;

            case "OP_6":
                stack.push("6");
                break;

            case "OP_7":
                stack.push("7");    
                break;  
            
            case "OP_8":
                stack.push("8");
                break;
            
            case "OP_9":
                stack.push("9");
                break;

            case "OP_10":
                stack.push("10");
                break;

            case "OP_11":
                stack.push("11");
                break;

            case "OP_12":
                stack.push("12");
                break;

            case "OP_13":
                stack.push("13");
                break;  

            case "OP_14":
                stack.push("14");
                break;

            case "OP_15":
                stack.push("15");
                break;

            case "OP_16":
                stack.push("16");
                break;
            
            // ── GRUPO 2: Operaciones de pila ──────────────────
            // OP_DUP = Duplica el elemento del tope de la pila. 
            case "OP_DUP":
                String tope = stack.peek();
                stack.push(tope);
                break;

            // OP_DROP = Elimina el elemento del tope de la pila.    
            case "OP_DROP":
                stack.pop();
                break;
            
            // OP_SWAP = Intercambia los dos elementos de arriba de la pila.  
            case "OP_SWAP": {
                String primero = stack.pop();
                String segundo = stack.pop();
                stack.push(segundo);
                stack.push(primero);
                break;
            }

            // OP_OVER = Copia el segundo elemento y lo pone arriba. El segundo no se mueve, solo se copia. 
            case "OP_OVER": {
                String primero = stack.pop();
                String segundo = stack.peek();
                stack.push(primero);
                stack.push(segundo);
                break;
            }

            // OP_EQUAL = Saca los dos elementos de arriba, los compara, y mete "1" si son iguales o "0" si no.
            case "OP_EQUAL": {
                String primero = stack.pop();
                String segundo = stack.pop();
                if (primero.equals(segundo)) {
                    stack.push("1");
                } else {
                    stack.push("0");
                }
                break;
            }
                
            // OP_EQUALVERIFY = compara y si son iguales continúa,si NO son iguales ABORTA todo
            case "OP_EQUALVERIFY": {
                String primero = stack.pop();
                String segundo = stack.pop();
                if (!primero.equals(segundo)) {
                    throw new RuntimeException("Error: OP_EQUALVERIFY falló, los valores son distintos");
                }
                break;
            }
                

            // OP_NOT = Invierte el valor del tope. Si el tope es "0", lo cambia a "1". Si el tope es cualquier otro valor, lo cambia a "0".
            case "OP_NOT": {
                String valor = stack.pop();
                if (valor.equals("0")) {
                    stack.push("1");
                } else {
                    stack.push("0");
                }
                break;
            }

            // OP_BOOLAND = Mete "1" solo si los dos elementos son verdaderos (diferentes de 0).
            case "OP_BOOLAND": {
                String primero = stack.pop();
                String segundo = stack.pop();
                if (!primero.equals("0") && !segundo.equals("0")) {
                    stack.push("1");
                } else {
                    stack.push("0");
                }
                break;
            }  
            
            // OP_BOOLOR = Mete "1" si al menos uno de los dos elementos es verdadero (diferente de 0). Mete "0" solo si ambos son "0".
            case "OP_BOOLOR": {
                String primero = stack.pop();
                String segundo = stack.pop();
                if (!primero.equals("0") || !segundo.equals("0")) {
                    stack.push("1");
                } else {
                    stack.push("0");
                }
                break;
            }

             // OP_ADD = Saca dos elementos, los suma como números y mete el resultado.
            case "OP_ADD": {
                String primero = stack.pop();
                String segundo = stack.pop();
                int resultado = Integer.parseInt(primero) + Integer.parseInt(segundo);
                stack.push(String.valueOf(resultado));
                break;
            }  
            
            // OP_HASH160 = Saca un elemento, le aplica la función hash160 (simulada) y mete el resultado.
            case "OP_HASH160": {
                String valor = stack.pop();
                stack.push("HASH_" + valor ); /** simula el hash agregando un prefijo al valor */;
                break;
            }

            // OP_CHECKSIG = Simula verificación de firma, siempre retorna true
            case "OP_CHECKSIG": {
                stack.pop(); /** saca pubKey */
                stack.pop(); /** saca firma */
                stack.push("1"); /** simula que la firma es válida */
                break;
            }



             
            default:
            stack.push(token);  // mete el dato a la pila
            break;
        }
        if (trace) {
            System.out.println(token + " -> pila: " + stack.toString());
    }

        }

        // Validar el resultado final
        if (stack.isEmpty()) {
            return false; /** si la pila esta vacia, el resultado es falso */
        }
        return !stack.peek().equals("0"); /** si el valor en la cima de la pila es "0", el resultado es falso, de lo contrario es verdadero */


    }

}