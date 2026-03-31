# Proyecto #1 — Intérprete de Bitcoin Script

## Descripción

Este proyecto consiste en el diseño e implementación de un intérprete para un subconjunto de Bitcoin Script, un lenguaje basado en pila (stack-based) utilizado para la validación de transacciones.

El sistema ejecuta instrucciones de izquierda a derecha utilizando una estructura de datos tipo pila. La validación del script es exitosa si ninguna instrucción falla y la cima de la pila final es verdadera (diferente de cero).

Este proyecto forma parte del curso de Estructura de Datos y pone en práctica el uso del Java Collections Framework, modelado UML y principios de diseño de software.



## Estructura del Proyecto

- Main.java → Punto de entrada del programa
- Interpreter.java → Núcleo del intérprete
- ScriptStack.java → Implementación de la pila
- Tokenizer.java → Conversión del script en tokens

## Compilación

```bash
javac *.java
