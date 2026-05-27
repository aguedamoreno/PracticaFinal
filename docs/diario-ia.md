# Diario de Uso de Inteligencia Artificial

## 1. Agentes de IA utilizados

Durante el desarrollo del proyecto se utilizaron distintos asistentes de inteligencia artificial con el objetivo de acelerar tareas repetitivas, resolver errores concretos y mejorar la organización general del código

Principalmente se utilizó:

- ChatGPT (modelo GPT-5.5)
- OpenCode 
- Google Gemini
- Asistentes integrados para generación y revisión de código Java

La IA se utilizó siempre como apoyo al desarrollo y no como sustitución de la revisión humana

---

# 2. Objetivos del uso de IA

La IA se utilizó para:

- Generar estructuras iniciales de clases
- Resolver errores de compilación
- Revisar lógica de movimiento y combate
- Generar ejemplos de JSON
- Ayudar con JavaFX
- Organizar la arquitectura general del proyecto
- Crear pruebas unitarias básicas
- Redactar partes de la memoria y documentación

---

# 3. Metodología seguida

El grupo siguió una metodología iterativa:

1. Primero se definieron los requisitos del sistema
2. Después se diseñaron las clases principales y estructuras de datos
3. Posteriormente se implementaron las estructuras propias
4. Más tarde se integró la lógica del juego
5. Finalmente se añadió la interfaz gráfica y la persistencia JSON

La IA se utilizó únicamente después de haber decidido previamente qué se quería implementar

El flujo de trabajo habitual era:

- Plantear el problema
- Pedir sugerencias o implementación parcial
- Revisar manualmente el código generado
- Adaptarlo al proyecto
- Corregir errores encontrados durante las pruebas

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

> “Utiliza nuestra lista simplemente enlazada para cualquiera de las clases que haga falta, en vez de crear una con Java”

### Resultado

- Se creó una estructura enlazada propia reutilizable
- Se añadieron iteradores y operaciones básicas
- Posteriormente modificamos varios métodos para adaptarlos al inventario y al sistema de turnos

---

## 4.3 Sistema de movimiento

### Prompt

> “Necesitamos calcular las casillas alcanzables por el jugador dentro de una matriz utilizando BFS y nuestra clase cola”

### Resultado

- Se implementó un algoritmo BFS
- Se utilizó una cola enlazada propia
- La IA explicó cómo limitar el movimiento según la velocidad del jugador

Posteriormente, adaptamos el algoritmo para impedir movimientos diagonales

---

## 4.4 Sistema de combate

### Prompt

> “Implementa un sistema de combate basado en ataque, defensa y un factor aleatorio siguiendo el enunciado del pdf”

### Resultado

- Se generó una primera versión funcional del combate
- Se añadieron reducciones de daño y comprobaciones de vida mínima

Después corregimos varios errores relacionados con ataques negativos

---

## 4.5 Interfaz JavaFX

### Prompt

> “Diseña una interfaz JavaFX sencilla usando GridPane para representar habitaciones como matrices”

### Resultado

- Se propuso una distribución basada en:
    - zona de mapa,
    - panel lateral,
    - inventario,
    - registro de eventos.

Simplificamos posteriormente varios componentes visuales para mejorar estabilidad y claridad

---

## 4.7 Resolución de errores

Durante el desarrollo se utilizaron múltiples prompts orientados a depuración y corrección de errores del sistema

---

### Problema 1: sistema de turnos

#### Prompt

> “El sistema de turnos no funciona correctamente entre jugador y enemigos. Necesitamos limitar cada turno a un máximo de un movimiento y una acción, además de añadir un botón para pasar turno manualmente”

#### Resultado

- Se añadió un botón de “Pasar turno”
- Se reorganizó el flujo de acciones del jugador
- Se limitó correctamente el número de acciones permitidas por turno

---

### Problema 2: recogida de objetos

#### Prompt

> “El jugador solo puede recoger objetos cuando está exactamente encima de ellos y no desde casillas adyacentes. Necesitamos permitir interacción lateral con objetos cercanos”

#### Resultado

- Se revisó la comprobación de posiciones
- Se permitió recoger objetos desde casillas contiguas válidas
- Se corrigieron errores en la detección de interacción dentro de la matriz

---

### Problema 3: comportamiento de enemigos

#### Prompt

> “Los enemigos no realizan acciones durante sus turnos y permanecen inmóviles. Revisa la lógica de IA y el sistema de turnos para que puedan moverse y atacar automáticamente”

#### Resultado

- Se revisó el sistema de turnos de enemigos
- Se implementó movimiento automático básico
- Se añadieron acciones automáticas de ataque cuando el jugador estaba cerca

---

### Problema 4: guardado de partida

#### Prompt

> “El botón de guardar partida existe en JavaFX, pero el sistema produce errores al escribir el JSON y no guarda el estado del juego”

#### Resultado

- Se revisó la serialización de objetos
- Se corrigieron errores relacionados con rutas y escritura de archivos
- Se añadieron controles de excepciones para evitar fallos durante el guardado

---

### Problema 5: interfaz gráfica

#### Prompt

> “Después de varias acciones consecutivas algunos botones de JavaFX dejan de responder”

#### Resultado

- Se reorganizó la gestión de eventos de JavaFX
- Se actualizaron correctamente los paneles después de cada turno
- Se corrigieron bloqueos parciales de la interfaz

# 5. Skills y herramientas utilizadas

Las herramientas principales utilizadas fueron:

- Generación de código Java
- Explicación de errores de compilación
- Ayuda con JavaFX
- Generación de estructuras JSON
- Asistencia en pruebas unitarias
- Revisión de arquitectura orientada a objetos

No se utilizaron skills externas especializadas adicionales

---

# 6. Resultados obtenidos

Gracias al uso de IA se consiguió:

- Acelerar la creación de estructuras base
- Reducir tiempo de depuración
- Mejorar la organización del proyecto
- Obtener ejemplos rápidos de implementación
- Resolver dudas sobre BFS, grafos y JavaFX

También permitió generar prototipos rápidos antes de realizar modificaciones manuales

---

# 7. Modificaciones humanas realizadas

El código generado nunca se utilizó directamente sin revisión

El grupo realizó numerosas modificaciones:

- Adaptación de nombres y arquitectura
- Corrección de errores lógicos
- Integración entre clases
- Simplificación de código excesivamente complejo
- Corrección de problemas de JavaFX
- Ajustes del sistema de combate
- Adaptación de estructuras propias para cumplir restricciones de la práctica
- Revisión manual de pruebas y excepciones

---

# 8. Problemas encontrados con la IA

Se detectaron varios problemas durante el uso de IA:

- Algunas soluciones utilizaban estructuras prohibidas (`ArrayList`, `HashMap`)
- Parte del código generado no encajaba con la arquitectura del proyecto
- Algunos métodos eran demasiado genéricos o poco eficientes
- Varias soluciones JavaFX provocaban errores de sincronización
- Algunas respuestas ignoraban restricciones concretas del enunciado

Por ello fue necesario revisar siempre manualmente el código generado

---

# 9. Crítica del uso de IA

La IA resultó especialmente útil como herramienta de apoyo y aprendizaje, pero no sustituye el razonamiento del programador

## Ventajas observadas

- Rapidez para generar estructuras base
- Ayuda para detectar errores
- Explicaciones útiles sobre algoritmos y diseño
- Facilita mucho la documentación y organización

## Desventajas observadas

- Puede generar código incorrecto o incompatible
- A veces ignora restricciones del proyecto
- Requiere revisión constante
- Puede producir soluciones innecesariamente complejas

El grupo considera que la IA es útil como asistente de desarrollo, siempre que exista supervisión humana y comprensión real del código generado

---

# 10. Metodología final aprendida

Tras el desarrollo del proyecto, el grupo concluyó que la forma más efectiva de trabajar con IA es:

1. Diseñar primero manualmente
2. Utilizar IA solo para tareas concretas
3. Revisar todo el código generado
4. Integrar progresivamente las soluciones
5. Probar continuamente cada módulo

La experiencia permitió comprender mejor la importancia de combinar herramientas automáticas con diseño y validación manual