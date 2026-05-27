# Diario de Uso de Inteligencia Artificial

## 1. Agentes de IA utilizados

Durante el desarrollo del proyecto se utilizaron distintos asistentes de inteligencia artificial con el objetivo de acelerar tareas repetitivas, resolver errores concretos y mejorar la organización general del código.

Principalmente se utilizó:

- ChatGPT (modelo GPT-5.5)
- OpenCode como entorno de asistencia al desarrollo
- Asistentes integrados para generación y revisión de código Java

La IA se utilizó siempre como apoyo al desarrollo y no como sustitución de la revisión humana.

---

# 2. Objetivos del uso de IA

La IA se utilizó para:

- Generar estructuras iniciales de clases.
- Resolver errores de compilación.
- Diseñar estructuras de datos propias.
- Revisar lógica de movimiento y combate.
- Generar ejemplos de JSON.
- Ayudar con JavaFX.
- Organizar la arquitectura general del proyecto.
- Crear pruebas unitarias básicas.
- Redactar partes de la memoria y documentación.

---

# 3. Metodología seguida

El grupo siguió una metodología iterativa:

1. Primero se definieron los requisitos del sistema.
2. Después se diseñaron las clases principales y estructuras de datos.
3. Posteriormente se implementaron las estructuras propias.
4. Más tarde se integró la lógica del juego.
5. Finalmente se añadió la interfaz gráfica y la persistencia JSON.

La IA se utilizó únicamente después de haber decidido previamente qué se quería implementar.

El flujo de trabajo habitual era:

- Plantear el problema.
- Pedir sugerencias o implementación parcial.
- Revisar manualmente el código generado.
- Adaptarlo al proyecto.
- Corregir errores encontrados durante las pruebas.

---

# 4. Prompts utilizados

## 4.1 Diseño inicial del proyecto

### Prompt

> “Necesitamos diseñar un juego por turnos en JavaFX basado en habitaciones conectadas mediante un grafo. Cada habitación debe ser una matriz y no podemos usar ArrayList ni HashMap. Propón una arquitectura orientada a objetos y las estructuras de datos necesarias.”

### Resultado

- Se propusieron clases como:
    - Jugador
    - Habitacion
    - Celda
    - Enemigo
    - Objeto
    - GrafoHabitaciones
    - MotorJuego

También se sugirió separar lógica y vista para facilitar JavaFX.

---

## 4.2 Implementación de listas enlazadas propias

### Prompt

> “Implementa una lista simplemente enlazada genérica en Java con operaciones añadir, eliminar, obtener y tamaño, sin utilizar estructuras de java.util.”

### Resultado

- Se creó una estructura enlazada propia reutilizable.
- Se añadieron iteradores y operaciones básicas.
- Posteriormente el grupo modificó varios métodos para adaptarlos al inventario y al sistema de turnos.

---

## 4.3 Sistema de movimiento

### Prompt

> “Necesitamos calcular las casillas alcanzables por el jugador dentro de una matriz utilizando BFS y una cola propia.”

### Resultado

- Se implementó un algoritmo BFS.
- Se utilizó una cola enlazada propia.
- La IA explicó cómo limitar el movimiento según la velocidad del jugador.

Posteriormente el grupo adaptó el algoritmo para impedir movimientos diagonales.

---

## 4.4 Sistema de combate

### Prompt

> “Implementa un sistema de combate basado en ataque, defensa y un factor aleatorio siguiendo la fórmula del enunciado.”

### Resultado

- Se generó una primera versión funcional del combate.
- Se añadieron reducciones de daño y comprobaciones de vida mínima.

El grupo corrigió posteriormente varios errores relacionados con ataques negativos.

---

## 4.5 Persistencia JSON

### Prompt

> “Genera un ejemplo de serialización y deserialización del estado del juego en JSON incluyendo jugador, inventario y habitaciones.”

### Resultado

- Se creó una estructura JSON para:
    - estado de partida,
    - habitaciones,
    - enemigos,
    - inventario.

El grupo adaptó manualmente varios nombres de atributos para mantener coherencia con las clases reales.

---

## 4.6 Interfaz JavaFX

### Prompt

> “Diseña una interfaz JavaFX sencilla usando GridPane para representar habitaciones como matrices.”

### Resultado

- Se propuso una distribución basada en:
    - zona de mapa,
    - panel lateral,
    - inventario,
    - registro de eventos.

El grupo simplificó posteriormente varios componentes visuales para mejorar estabilidad y claridad.

---

## 4.7 Resolución de errores

Durante el desarrollo se utilizaron múltiples prompts orientados a depuración.

### Ejemplos

> “El botón de acciones deja de responder después de cargar partida.”

> “Existe una excepción NullPointerException al mover enemigos.”

> “Revisa este método porque el jugador atraviesa paredes.”

### Resultado

- La IA ayudó a localizar errores concretos.
- En varios casos la solución generada no era completamente correcta y tuvo que revisarse manualmente.

---

# 5. Skills y herramientas utilizadas

Las herramientas principales utilizadas fueron:

- Generación de código Java.
- Explicación de errores de compilación.
- Ayuda con JavaFX.
- Generación de estructuras JSON.
- Asistencia en pruebas unitarias.
- Revisión de arquitectura orientada a objetos.

No se utilizaron skills externas especializadas adicionales.

---

# 6. Resultados obtenidos

Gracias al uso de IA se consiguió:

- Acelerar la creación de estructuras base.
- Reducir tiempo de depuración.
- Mejorar la organización del proyecto.
- Obtener ejemplos rápidos de implementación.
- Resolver dudas sobre BFS, grafos y JavaFX.

También permitió generar prototipos rápidos antes de realizar modificaciones manuales.

---

# 7. Modificaciones humanas realizadas

El código generado nunca se utilizó directamente sin revisión.

El grupo realizó numerosas modificaciones:

- Adaptación de nombres y arquitectura.
- Corrección de errores lógicos.
- Integración entre clases.
- Simplificación de código excesivamente complejo.
- Corrección de problemas de JavaFX.
- Ajustes del sistema de combate.
- Adaptación de estructuras propias para cumplir restricciones de la práctica.
- Revisión manual de pruebas y excepciones.

---

# 8. Problemas encontrados con la IA

Se detectaron varios problemas durante el uso de IA:

- Algunas soluciones utilizaban estructuras prohibidas (`ArrayList`, `HashMap`).
- Parte del código generado no encajaba con la arquitectura del proyecto.
- Algunos métodos eran demasiado genéricos o poco eficientes.
- Varias soluciones JavaFX provocaban errores de sincronización.
- Algunas respuestas ignoraban restricciones concretas del enunciado.

Por ello fue necesario revisar siempre manualmente el código generado.

---

# 9. Crítica del uso de IA

La IA resultó especialmente útil como herramienta de apoyo y aprendizaje, pero no sustituye el razonamiento del programador.

## Ventajas observadas

- Rapidez para generar estructuras base.
- Ayuda para detectar errores.
- Explicaciones útiles sobre algoritmos y diseño.
- Facilita la documentación y organización.

## Desventajas observadas

- Puede generar código incorrecto o incompatible.
- A veces ignora restricciones del proyecto.
- Requiere revisión constante.
- Puede producir soluciones innecesariamente complejas.

El grupo considera que la IA es útil como asistente de desarrollo, siempre que exista supervisión humana y comprensión real del código generado.

---

# 10. Metodología final aprendida

Tras el desarrollo del proyecto, el grupo concluyó que la forma más efectiva de trabajar con IA es:

1. Diseñar primero manualmente.
2. Utilizar IA solo para tareas concretas.
3. Revisar todo el código generado.
4. Integrar progresivamente las soluciones.
5. Probar continuamente cada módulo.

La experiencia permitió comprender mejor la importancia de combinar herramientas automáticas con diseño y validación manual.