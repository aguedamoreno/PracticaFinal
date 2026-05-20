# Diario de IA

## Agente
OpenCode con modelo `openai/gpt-5.5`.

## Skills
No se cargaron skills especializados. La skill disponible era `customize-opencode`, pero no aplicaba porque no se modificaba configuracion de OpenCode.

## Prompts Utilizados
1. "Con todos estos codigos, necesito que me digas cuales son los prompts que utilizaste, y que me hagas los codigos que faltan para completar el trabajo".
2. Enunciado completo de la practica conjunta de Estructuras de Datos y Metodologia de la Programacion, incluyendo requisitos funcionales, JSON, JavaFX, estructuras propias, pruebas, logs y diario de IA.
3. "hazlo ahora".

## Resultado
Se reviso el codigo existente y se completaron piezas necesarias para una version funcional minima:

- Cola propia para BFS y turnos.
- Metodos faltantes en lista simplemente enlazada: `tamaño`, `obtener`, `eliminar`.
- Iteradores de listas enlazadas.
- Motor de juego con movimiento, puertas, trampas, recogida de objetos, ataque, derrota, victoria y log.
- Persistencia JSON basica de estado de partida.
- Interfaz JavaFX con matriz, estado del jugador, acciones de guardar/cargar y registro de eventos.
- Configuracion JSON de ejemplo.
- Pruebas JUnit basicas de estructuras, motor y persistencia.

## Modificaciones Humanas Pendientes
El grupo debe revisar y adaptar:

- Reglas exactas de equipamiento por zonas corporales si se quieren completar como ampliacion.
- Carga completa de configuracion inicial desde JSON externo, si el profesor exige que no sea solo demo programatica.
- Diagramas UML, bocetos de interfaz, memoria PDF y video.

## Critica del Uso de IA
La IA fue util para integrar rapidamente clases sueltas y detectar errores de compilacion. El riesgo principal es que la solucion generada sea una version minima y no cubra todas las ampliaciones posibles. Para mejorar, el grupo debe ejecutar el juego, revisar cada requisito uno por uno y escribir mas pruebas antes de entregar.
