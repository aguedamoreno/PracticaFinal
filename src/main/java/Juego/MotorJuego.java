package Juego;

import Juego.listas.ListaSimplementeEnlazada; // Lo usamos para manejar listas propias, por ejemplo posiciones alcanzables o inventario

/** Clase Motor (controla el mapa, el jugador, los turnos, la habitación actual, las acciones del jugador, la victoria, la derrota y el registro)
 */
public class MotorJuego {

    private final Grafo mapa; // Grafo que representa el mapa completo: cada vértice es una habitación y cada arista conecta habitaciones
    private final Jugador jugador; // Objeto que representa al jugador: vida, ataque, defensa, posición, inventario, etc.
    private final RegistroJuego registro; // Guarda los mensajes de lo que va ocurriendo en la partida
    private int habitacionActualIndice; // Índice de la habitación donde está el jugador dentro del grafo
    private int turnosRestantes; // Número de turnos que quedan antes de perder por quedarse sin tiempo
    private boolean victoria; // Vale true cuando el jugador gana la partida
    private boolean derrota; // Vale true cuando el jugador pierde la partida
    private boolean jugadorYaSeMovioEnEsteTurno = false; // Controla si el jugador ya ha usado su movimiento este turno
    private boolean jugadorYaActuoEnEsteTurno = false; // Controla si el jugador ya ha hecho una acción este turno: atacar, recoger o usar objeto

    /** Constructor del motor del juego
     */
    public MotorJuego(Grafo mapa, Jugador jugador, int habitacionInicialIndice, int turnosRestantes) {
        this.mapa = mapa; // Guarda el grafo/mapa que se usará durante toda la partida
        this.jugador = jugador; // Guarda el jugador que participará en la partida
        this.habitacionActualIndice = habitacionInicialIndice; // Indica en qué habitación empieza el jugador
        this.turnosRestantes = turnosRestantes; // Guarda los turnos iniciales disponibles
        this.registro = new RegistroJuego(); // Crea un registro vacío para guardar los mensajes de la partida

        Habitacion habitacion = getHabitacionActual(); // Obtiene la habitación inicial usando el índice guardado

        this.jugador.setHabitacionActualId(habitacion.getId()); // Guarda en el jugador el id de la habitación donde empieza

        this.victoria = false; // Al empezar la partida, todavía no se ha ganado
        this.derrota = false; // Al empezar la partida, todavía no se ha perdido

        registro.registrar("Partida iniciada en " + habitacion.getId() + "."); // Añade al registro el mensaje de inicio
    }

    /** Metodo que crea una partida de prueba ya montada
     */
    public static MotorJuego crearPartidaDemo() {

        Grafo mapa = new Grafo(false); // Crea un grafo NO dirigido: se puede ir y volver entre habitaciones conectadas

        Habitacion entrada = new Habitacion("Entrada", 5, 5); // Crea la habitación Entrada con 5 filas y 5 columnas
        Habitacion sala = new Habitacion("Sala", 6, 6); // Crea la habitación Sala con 6 filas y 6 columnas
        Habitacion salida = new Habitacion("Salida", 4, 5); // Crea la habitación Salida con 4 filas y 5 columnas

        entrada.colocarObjeto(0, 1, new Objeto(Objeto.Tipo.POCION_VIDA, "Pocion pequena", "+15 vida", 15));
        // En la habitación Entrada, coloca una poción en la fila 0, columna 1
        // Esa poción cura 15 puntos de vida

        entrada.colocarEnemigo(2, 3, new Enemigo(Enemigo.Tipo.GOBLIN));
        // En Entrada, coloca un Goblin en la posición (2,3)

        sala.colocarEnemigo(2, 4, new Enemigo(Enemigo.Tipo.LADRON));
        // En Sala, coloca un Ladrón en la posición (2,4)

        entrada.colocarPuerta(4, 4, "Sala");
        // En Entrada, coloca una puerta en (4,4)
        // Esa puerta lleva a la habitación cuyo id es "Sala"

        entrada.colocarTrampa(3, 1);
        // En Entrada, coloca una trampa en (3,1)

        sala.colocarObjeto(1, 1, new Objeto(Objeto.Tipo.ARMA, "Espada", "+4 ataque", 4));
        // En Sala, coloca una espada en (1,1)
        // Es un arma que aumenta el ataque en 4

        sala.colocarEnemigo(3, 3, new Enemigo(Enemigo.Tipo.ORCO));
        // En Sala, coloca un Orco en (3,3)

        sala.colocarPuerta(5, 5, "Salida");
        // En Sala, coloca una puerta hacia la habitación Salida

        sala.colocarPuerta(0, 0, "Entrada");
        // En Sala, coloca una puerta de vuelta hacia Entrada

        salida.colocarEnemigo(0, 3, new Enemigo(Enemigo.Tipo.DRAGON));
        // En Salida, coloca un Dragón en (0,3)

        salida.colocarEnemigo(1, 2, new Enemigo(Enemigo.Tipo.ESQUELETO));
        // En Salida, coloca un Esqueleto en (1,2)

        salida.colocarSalida(3, 4);
        // En Salida, coloca la casilla final de victoria en (3,4)

        salida.colocarPuerta(0, 0, "Sala");
        // En Salida, coloca una puerta para volver a Sala

        int iEntrada = mapa.agregarVertice(entrada);
        // Añade la habitación Entrada al grafo y guarda su índice dentro del grafo

        int iSala = mapa.agregarVertice(sala);
        // Añade la habitación Sala al grafo

        int iSalida = mapa.agregarVertice(salida);
        // Añade la habitación Salida al grafo

        mapa.agregarArista(iEntrada, iSala);
        // Conecta Entrada con Sala en el grafo

        mapa.agregarArista(iSala, iSalida);
        // Conecta Sala con Salida en el grafo

        Jugador jugador = new Jugador(100, 10, 3, 3);
        // Crea el jugador con: 100 de vida, 10 de ataque, 3 de defensa y velocidad 3

        jugador.setPosicionX(0); // Coloca al jugador en la fila 0
        jugador.setPosicionY(0); // Coloca al jugador en la columna 0

        return new MotorJuego(mapa, jugador, iEntrada, 30);
        // Devuelve un motor ya preparado: mapa creado, jugador creado, empieza en Entrada y tiene 30 turnos
    }

    /** Metodo que mueve al jugador a una nueva casilla
    */
    public void moverJugador(int nuevaFila, int nuevaColumna) throws JuegoException {

        if (derrota || victoria) {
            throw new JuegoException("La partida ya ha finalizado.");
        }
        // Si la partida terminó, no se permite seguir moviendo

        if (jugadorYaSeMovioEnEsteTurno) {
            throw new JuegoException("Ya te has movido en este turno.");
        }
        // Si ya se movió este turno, no puede moverse otra vez

        if (jugadorYaActuoEnEsteTurno) {
            throw new JuegoException("No puedes moverte después de haber realizado una acción.");
        }
        // En este juego, si ya has atacado, usado objeto o recogido objeto, no puedes moverte después

        Habitacion habActual = getHabitacionActual();
        // Obtiene la habitación donde está ahora mismo el jugador

        Posicion origen = new Posicion(jugador.getPosicionX(), jugador.getPosicionY());
        // Crea una posición con la fila y columna actuales del jugador

        ListaSimplementeEnlazada<Posicion> alcanzables = habActual.calcularPosicionesAlcanzables(origen, 2);
        // Calcula las posiciones a las que puede llegar el jugador, se limita a máximo 2 casillas en cruz: arriba, abajo, izquierda o derecha

        boolean destinoValido = false;
        // Al principio suponemos que la casilla elegida NO es válida

        for (int i = 0; i < alcanzables.tamaño(); i++) {
            // Recorre todas las posiciones alcanzables

            Posicion p = alcanzables.obtener(i);
            // Obtiene una posición alcanzable concreta

            if (p.getFila() == nuevaFila && p.getColumna() == nuevaColumna) {
                // Comprueba si esa posición coincide con el destino elegido
                destinoValido = true;
                // Si coincide, significa que el movimiento sí está permitido
                break;
                // Sale del bucle porque ya no hace falta seguir buscando
            }
        }

        if (!destinoValido) {
            throw new JuegoException("Movimiento inválido. Solo puedes moverte hasta 2 casillas en línea recta (arriba, abajo, izquierda, derecha).");
        }
        // Si el destino no estaba en la lista de alcanzables, se cancela el movimiento

        Celda celdaDestino = habActual.getCelda(nuevaFila, nuevaColumna);
        // Obtiene la celda a la que el jugador quiere moverse
        // CASO 1: si el jugador se mueve a una puerta
        if (celdaDestino.getTipo() == Celda.Tipo.PUERTA) {


            String idHabitacionDestino = (String) celdaDestino.getContenido();
            // El contenido de una puerta es el id de la habitación destino
            registro.registrar("¡El jugador cruza una puerta hacia la habitación " + idHabitacionDestino + "!");
            // Guarda en el registro que el jugador ha cruzado la puerta

            int nuevoIndice = buscarHabitacionPorId(idHabitacionDestino);
            // Busca en el grafo qué índice tiene la habitación destino

            if (nuevoIndice != -1) {
                // Si se ha encontrado una habitación con ese ID

                habitacionActualIndice = nuevoIndice;
                // Cambia la habitación actual del motor

                jugador.setHabitacionActualId(idHabitacionDestino);
                // Actualiza también el id de habitación guardado en el jugador

                jugador.setPosicionX(0);
                jugador.setPosicionY(0);
                // Coloca al jugador en la posición inicial de la nueva habitación
            }

            jugadorYaSeMovioEnEsteTurno = true;
            // Cruzar una puerta cuenta como movimiento del turno

            return;
            // Termina el metodo para no seguir evaluando otros tipos de celda
        }
        // CASO 2: si el jugador pisa la salida final
        if (celdaDestino.getTipo() == Celda.Tipo.SALIDA) {


            victoria = true;
            // Marca la partida como ganada

            registro.registrar("¡Has abierto la puerta de salida! ¡Ganaste la partida!");
            // Guarda el mensaje de victoria

            return;
            // Termina el metodo porque ya no hay más que mover
        }

        // CASO 3: si el jugador pisa una trampa
        if (celdaDestino.getTipo() == Celda.Tipo.TRAMPA) {
            // Primero mueve al jugador a la casilla de la trampa
            jugador.setPosicionX(nuevaFila);
            jugador.setPosicionY(nuevaColumna);


            int danoTrampa = 10;
            // Daño fijo que hace la trampa

            jugador.setVidaActual(jugador.getVidaActual() - danoTrampa);
            // Resta 10 puntos a la vida actual del jugador

            celdaDestino.limpiar();
            // Limpia la celda: la trampa desaparece después de activarse

            jugadorYaSeMovioEnEsteTurno = true;
            // Pisar la trampa cuenta como movimiento

            registro.registrar("El jugador pisó una trampa en (" + nuevaFila + ", " + nuevaColumna + ") y perdió " + danoTrampa + " puntos de vida.");
            // Guarda en el registro lo ocurrido

            comprobarDerrotaPorVida();
            // Comprueba si el jugador murió por culpa de la trampa

            return;
            // Termina el metodo
        }
        // CASO 4: movimiento normal, solo cambia las coordenadas del jugador
        jugador.setPosicionX(nuevaFila);
        jugador.setPosicionY(nuevaColumna);


        jugadorYaSeMovioEnEsteTurno = true;
        // Marca que el jugador ya se movió en este turno

        registro.registrar("El jugador se movió a la casilla (" + nuevaFila + ", " + nuevaColumna + ").");
        // Guarda el movimiento en el registro
    }

    /** Metodo que recoge un objeto cercano al jugador
     */
    public void recogerObjetoAdyacente() throws JuegoException {

        validarPartidaActiva();
        // Comprueba que la partida siga activa, porque si ya ganó o perdió, no permite recoger objetos

        for (int df = -1; df <= 1; df++) {
            // Recorre desplazamientos verticales: -1 = fila de arriba; 0 = misma fila; 1 = fila de abajo

            for (int dc = -1; dc <= 1; dc++) {
                // Recorre desplazamientos horizontales:-1 = izquierda; 0 = misma columna; 1 = derecha

                int fila = jugador.getPosicionX() + df;
                // Calcula la fila real que se quiere comprobar.

                int columna = jugador.getPosicionY() + dc;
                // Calcula la columna real que se quiere comprobar.

                if (getHabitacionActual().esPosicionValida(fila, columna)) {
                    // Comprueba que la posición exista dentro de la habitación, y evita salirse del mapa

                    Celda celda = getHabitacionActual().getCelda(fila, columna);
                    // Obtiene la celda de esa posición

                    if (celda != null && celda.getTipo() == Celda.Tipo.OBJETO) {
                        // Comprueba: que la celda exista, y que sea una celda de tipo objeto

                        recogerObjetoAdyacente(fila, columna);
                        // Llama al otro metodo que realmente recoge el objeto: lo mete al inventario, limpia la celda, registra el evento

                        return;
                        // Termina el metodo inmediatamente y solo recoge un objeto
                    }
                }
            }
        }
        throw new JuegoException("No hay ningún objeto ni en tu casilla ni en las casillas de alrededor.");
    }

    /** Metodo que permite al jugador recoger un objeto que esté en su misma casilla
    */
    public void recogerObjetoAdyacente(int fila, int columna) throws JuegoException {

        // Comprueba que la partida sigue activa y no ha terminado
        validarPartidaActiva();

        // Comprueba si el jugador ya ha realizado una acción en este turno, esto evita que haga varias acciones seguidas
        if (jugadorYaActuoEnEsteTurno) {

            // Lanza una excepción indicando que el jugador ya actuó
            throw new JuegoException("Ya has realizado una acción en este turno.");
        }

        // Comprueba que la posición indicada existe dentro de los límites de la habitación actual
        if (!getHabitacionActual().esPosicionValida(fila, columna)) {

            // Si la posición no existe, se lanza una excepción
            throw new JuegoException("Posición fuera de la habitación.");
        }

        // Permitimos recoger en la casilla actual o en cualquiera de las 8 casillas que rodean al jugador

        // Calcula la distancia entre la fila indicada y la fila del jugador
        int distanciaFila = Math.abs(fila - jugador.getPosicionX());    // Se convierte el resultado en positivo

        // Calcula la distancia entre la columna indicada y la columna del jugador
        int distanciaColumna = Math.abs(columna - jugador.getPosicionY());

        // Comprueba si la casilla está demasiado lejos, y si alguna distancia es mayor que 1, ya no es adyacente
        if (distanciaFila > 1 || distanciaColumna > 1) {
            // Lanza una excepción indicando que el objeto no está cerca
            throw new JuegoException("Solo puedes recoger objetos de tu casilla o de las casillas de alrededor.");
        }

        // Guarda la habitación actual en una variable
        Habitacion habActual = getHabitacionActual();

        // Obtiene la celda concreta donde debería estar el objeto
        Celda celdaObjeto = habActual.getCelda(fila, columna);

        // Comprueba varias cosas: que la celda exista, que sea de tipo Objeto, y que el contenido realmente sea un Objeto
        if (celdaObjeto == null || celdaObjeto.getTipo() != Celda.Tipo.OBJETO || !(celdaObjeto.getContenido() instanceof Objeto)) {

            // Si no hay un objeto válido, lanza excepción
            throw new JuegoException("No hay ningún objeto en esa casilla para recoger.");
        }

        // Convierte el contenido de la celda en un Objeto
        Objeto objeto = (Objeto) celdaObjeto.getContenido();

        // Añade el objeto al final del inventario del jugador
        jugador.getInventario().insertarUltimo(objeto);

        // Registra en el historial del juego qué objeto se ha recogido
        registro.registrar("Has recogido el objeto: " + objeto.getNombre() + ".");

        // Limpia la celda para eliminar el objeto del mapa
        celdaObjeto.limpiar();

        // Marca que el jugador ya ha realizado una acción este turno
        jugadorYaActuoEnEsteTurno = true;

        // Registra un mensaje indicando que puede finalizar el turno
        registro.registrar("Acción realizada. Puedes terminar el turno con 'Pasar Turno'.");
    }

    /** Metodo que permite usar o equipar un objeto del inventario
     */
    public void usarObjeto(int indiceInventario) throws JuegoException {
        // Comprueba que la partida sigue activa
        validarPartidaActiva();
        // Obtiene el objeto almacenado en la posición indicada del inventario
        Objeto objeto = jugador.getInventario().obtener(indiceInventario);

        // Comprueba que realmente exista un objeto en esa posición
        if (objeto == null) {
            // Si no existe, lanza excepción
            throw new JuegoException("Objeto de inventario no valido.");
        }

        // Comprueba si el jugador ya realizó una acción en este turno
        if (jugadorYaActuoEnEsteTurno) {
            // Si ya actuó, no puede volver a hacerlo
            throw new JuegoException("Ya has realizado una acción en este turno.");
        }

        // Comprueba si el objeto es una poción de vida
        if (objeto.getTipo() == Objeto.Tipo.POCION_VIDA) {
            // Aumenta la vida actual del jugador usando el valor de la poción
            jugador.setVidaActual(jugador.getVidaActual() + objeto.getValor());

            // Usa el objeto desde el inventario, y dependiendo del tipo, puede eliminarse o reducir usos
            if (!jugador.usarObjetoInventario(indiceInventario)) {
                // Si no puede usarse, lanza excepción
                throw new JuegoException("El objeto no puede usarse.");
            }

            // Registra en el historial el uso del objeto
            registro.registrar("Jugador usa " + objeto.getNombre() + ".");

            // Comprueba si el objeto es equipable
        } else if (objeto.isEquipable()) {
            // Intenta equipar el objeto desde el inventario
            if (!jugador.equipoObjetoInventario(indiceInventario)) {
                // Si no se puede equipar, lanza excepción
                throw new JuegoException("No se puede equipar el objeto.");
            }

            // Registra en el historial que el jugador ha equipado el objeto
            registro.registrar("Jugador equipa " + objeto.getNombre() + ".");

        } else {
            // Para cualquier otro objeto, intenta usarlo normal
            if (!jugador.usarObjetoInventario(indiceInventario)) {
                // Si falla el uso, lanza excepción
                throw new JuegoException("El objeto no puede usarse.");
            }

            // Registra el uso del objeto
            registro.registrar("Jugador usa " + objeto.getNombre() + ".");
        }

        // Marca que el jugador ya ha realizado una acción en este turno
        jugadorYaActuoEnEsteTurno = true;

        // Registra un mensaje indicando que puede terminar el turno
        registro.registrar("Acción realizada. Puedes terminar el turno con 'Pasar Turno'.");
    }

    /** Metodo que hace que el jugador ataque a un enemigo que esté en una casilla adyacente
     */
    public void atacarAdyacente(int fila, int columna) throws JuegoException {

        if (jugadorYaActuoEnEsteTurno) {
            // Si el jugador ya atacó, usó objeto o recogió algo este turno

            throw new JuegoException("Ya has realizado tu acción en este turno.");
            // No se permite hacer otra acción
        }

        validarPartidaActiva();
        // Comprueba que la partida no haya terminado por victoria o derrota

        validarAdyacente(fila, columna);
        // Comprueba que la casilla objetivo esté justo al lado del jugador, y solo permite arriba, abajo, izquierda o derecha

        Celda celda = getHabitacionActual().getCelda(fila, columna);
        // Obtiene la celda donde el jugador quiere atacar

        // Comprueba que la celda sea de tipo Enemigo y que el contenido realmente sea un objeto Enemigo
        // Si no se cumplen ambas, no hay enemigo válido que atacar
        if (celda.getTipo() != Celda.Tipo.ENEMIGO
                || !(celda.getContenido() instanceof Enemigo)) {

            throw new JuegoException("No hay enemigo atacable en esa celda.");
        }

        Enemigo enemigo = (Enemigo) celda.getContenido();
        // Convierte el contenido de la celda a tipo Enemigo
        // para poder usar sus métodos y atributos

        // Calcula el daño del ataque.
        int dano = calcularDano(
                jugador.getAtaqueTotal(),
                enemigo.getDefensa()
        );

        enemigo.setVidaActual(enemigo.getVidaActual() - dano);
        // Resta el daño calculado a la vida actual del enemigo

        // Guarda en el registro: qué enemigo fue atacado, cuánto daño recibió, y cuánta vida le queda
        registro.registrar(
                "Jugador ataca a "
                        + enemigo.getNombre()
                        + " y causa "
                        + dano
                        + " de daño. Vida enemiga: "
                        + enemigo.getVidaActual()
                        + "/"
                        + enemigo.getVidaMax()
                        + "."
        );

        if (!enemigo.estaVivo()) {
            // Si el enemigo se quedó sin vida

            celda.limpiar();
            // Borra el enemigo de la celda
            // La casilla vuelve a quedar vacía

            registro.registrar(enemigo.getNombre() + " queda derrotado.");
            // Guarda en el registro que el enemigo murió
        }

        // Marca que el jugador ya usó su acción de este turno, y después de esto ya no puede: volver a atacar, recoger objetos, usar objetos
        // Hasta pasar turno.
        jugadorYaActuoEnEsteTurno = true;

        // Guarda la posición donde ocurrió el ataque.
        registro.registrar(
                "El jugador atacó al enemigo en ("
                        + fila
                        + ", "
                        + columna
                        + ")."
        );

        // El ataque no termina automáticamente el turno
        // El jugador todavía puede: pasar turno manualmente, o simplemente no hacer nada más
        // Pero ya NO puede hacer otra acción este turno
        registro.registrar(
                "Acción realizada. Puedes terminar el turno con 'Pasar Turno'."
        );
    }

    /** Metodo que devuelve las celdas a las que el jugador puede llegar desde su posición actual
     */
    public ListaSimplementeEnlazada<Posicion> getCeldasAlcanzables() {
        // calcularPosicionesAlcanzables(...) devuelve las posiciones válidas.
        return getHabitacionActual().calcularPosicionesAlcanzables( // getHabitacionActual() obtiene la habitación donde está el jugador.
                new Posicion(jugador.getPosicionX(), jugador.getPosicionY()),   // new Posicion(...) crea una posición con las coordenadas actuales del jugador.
                jugador.getVelocidad()   // jugador.getVelocidad() indica cuántas casillas puede moverse.
        );
    }

    /** Metodo que calcula el camino mínimo desde la habitación actual hasta la habitación que tiene la salida
     */
    public Object[] getCaminoMinimoSalida() {

        int destino = buscarHabitacionConSalida();
        // Busca en qué habitación está la casilla SALIDA

        if (destino == -1) {
            return null;
        }
        // Si no encuentra ninguna salida, devuelve null

        return mapa.dijkstra(habitacionActualIndice, destino);
        // Calcula el camino mínimo desde la habitación actual hasta la habitación de salida
    }

    /** Metodo que resuelve qué ocurre cuando el jugador llega a una celda especial.
     */
    private void resolverCeldaDestino(Celda destino) throws JuegoException {

        if (destino.getTipo() == Celda.Tipo.TRAMPA) {
            // Si la celda es una trampa

            jugador.setVidaActual(jugador.getVidaActual() - 10);
            // Resta 10 puntos de vida al jugador

            destino.limpiar();
            // Limpia la celda para que la trampa desaparezca después de activarse

            registro.registrar("El jugador cae en una trampa y pierde 10 de vida.");
            // Añade al registro lo ocurrido

            comprobarDerrotaPorVida();
            // Comprueba si el jugador se ha quedado sin vida

        } else if (destino.getTipo() == Celda.Tipo.PUERTA) {
            // Si la celda es una puerta

            cambiarHabitacion((String) destino.getContenido());
            // El contenido de la puerta es el id de la habitación destino, se convierte a String y se cambia de habitación

        } else if (destino.getTipo() == Celda.Tipo.SALIDA) {
            // Si la celda es la salida final

            victoria = true;
            // Marca la partida como ganada

            registro.registrar("El jugador abre la salida exterior. Victoria.");
            // Guarda el mensaje de victoria en el registro
        }
    }

    /** Metodo que cambia al jugador a otra habitación
     */
    private void cambiarHabitacion(String idDestino) throws JuegoException {

        int destino = buscarHabitacionPorId(idDestino);
        // Busca el índice de la habitación cuyo id coincide con idDestino

        if (destino == -1) {
            throw new JuegoException("La puerta no tiene habitacion destino valida.");
        }
        // Si no existe ninguna habitación con ese id, lanza error

        habitacionActualIndice = destino;
        // Actualiza el índice de la habitación actual

        jugador.setHabitacionActualId(idDestino);
        // Guarda en el jugador el id de la nueva habitación

        jugador.setPosicionX(0);
        jugador.setPosicionY(0);
        // Coloca al jugador al inicio de la nueva habitación

        registro.registrar("Jugador cambia a la habitacion " + idDestino + ".");
        // Guarda el cambio de habitación en el registro
    }

    /** Turno simple de enemigos.
     */
    private void turnoEnemigos() {

        Habitacion habitacion = getHabitacionActual();
        // Obtiene la habitación donde está el jugador

        ListaSimplementeEnlazada<Posicion> enemigos = habitacion.obtenerPosicionesConEnemigos();
        // Busca todas las posiciones de la habitación donde hay enemigos

        for (int i = 0; i < enemigos.tamaño(); i++) {
            // Recorre todas las posiciones con enemigos

            Posicion posicion = enemigos.obtener(i);
            // Obtiene una posición concreta donde hay un enemigo

            Celda celda = habitacion.getCelda(posicion.getFila(), posicion.getColumna());
            // Obtiene la celda de esa posición

            if (celda.getTipo() == Celda.Tipo.ENEMIGO && celda.getContenido() instanceof Enemigo) {
                // Comprueba que la celda sigue siendo de tipo ENEMIGO y que su contenido realmente es un objeto Enemigo

                Enemigo enemigo = (Enemigo) celda.getContenido();
                // Convierte el contenido de la celda a Enemigo para poder usar sus métodos

                if (distanciaJugador(posicion) == 1) {
                    // Si la distancia al jugador es 1, significa que está al lado: arriba, abajo, izquierda o derecha.

                    int dano = calcularDano(enemigo.getAtaque(), jugador.getDefensaTotal());
                    // Calcula el daño teniendo en cuenta ataque del enemigo y defensa del jugador

                    jugador.setVidaActual(jugador.getVidaActual() - dano);
                    // Resta ese daño a la vida del jugador

                    registro.registrar(enemigo.getNombre() + " ataca al jugador y causa " + dano + " de dano.");
                    // Guarda el ataque en el registro

                    comprobarDerrotaPorVida();
                    // Comprueba si el jugador murió por ese ataque
                }
            }
        }
    }

    /** Metodo que calcula el daño de un ataque
     */
    private int calcularDano(int ataque, int defensa) {

        double aleatorio = Math.random() * 2;
        // Genera un número decimal aleatorio entre 0 y 2, esto hace que el ataque pueda variar bastante

        return Math.max(0, (int) Math.round(ataque * aleatorio - defensa));
        // Math.max(0, ...) evita que el daño sea negativo.
    }

    /** Metodo que calcula la distancia entre una posición y el jugador
    */
    private int distanciaJugador(Posicion posicion) {
        return Math.abs(posicion.getFila() - jugador.getPosicionX()) + Math.abs(posicion.getColumna() - jugador.getPosicionY());
        // Math.abs devuelve el valor absoluto
        // Así da igual si el enemigo está por encima/debajo o izquierda/derecha
    }

    /** Metodo que comprueba que una celda esté justo al lado del jugador
    */
    private void validarAdyacente(int fila, int columna) throws JuegoException {
        // Primero comprueba que la posición exista dentro de la habitación
        if (!getHabitacionActual().esPosicionValida(fila, columna)) {
            throw new JuegoException("Posicion fuera de la habitacion.");
        }

        // Comprueba que la distancia sea exactamente 1, que está arriba, abajo, izquierda o derecha
        // No permite diagonales
        if (Math.abs(fila - jugador.getPosicionX()) + Math.abs(columna - jugador.getPosicionY()) != 1) {
            throw new JuegoException("La accion solo puede hacerse sobre una celda adyacente.");
        }

    }

    /** Metodo que comprueba que la partida siga activa
     */
    private void validarPartidaActiva() throws JuegoException {
        // Si victoria o derrota son true, se lanza error
        if (victoria || derrota) {
            throw new JuegoException("La partida ya ha terminado.");
        }
    }

    /** Metodo que resta un turno a la partida
    */
    private void consumirTurno() {
        turnosRestantes--;
        // Resta 1 al contador de turno

        if (turnosRestantes <= 0 && !victoria) {
            // Si ya no quedan turnos y el jugador no ha ganado
            derrota = true;
            // Marca la partida como perdida

            registro.registrar("El jugador se queda sin turnos. Derrota.");
            // Guarda el motivo de la derrota
        }
    }

    /** Metodo que comprueba si el jugador ha perdido por quedarse sin vida
     */
    private void comprobarDerrotaPorVida() {
        // Si la vida del jugador es 0 o menos
        if (jugador.getVidaActual() <= 0) {
            jugador.setVivo(false);
            // Marca al jugador como no vivo

            derrota = true;
            // Marca la partida como perdida

            registro.registrar("El jugador pierde todos sus puntos de vida. Derrota.");
            // Guarda el mensaje de derrota
        }
    }

    /** Metodo que busca una habitación por su id
     */
    private int buscarHabitacionPorId(String id) {

        for (int i = 0; i < mapa.getSize(); i++) {
            // Recorre todos los vértices del grafo

            Habitacion habitacion = (Habitacion) mapa.obtenerDatosVertice(i);
            // Obtiene los datos del vértice i, que son una Habitacion

            if (habitacion.getId().equals(id)) {
                return i;
            }
            // Si el id de esa habitación coincide con el id buscado, devuelve el índice donde está en el grafo
        }

        return -1;
        // Si no encuentra ninguna habitación con ese id, devuelve -1
    }

    /** Metodo que busca qué habitación contiene la casilla de salida final
     */
    private int buscarHabitacionConSalida() {
        for (int i = 0; i < mapa.getSize(); i++) {
            // Recorre todas las habitaciones guardadas en el grafo

            Habitacion habitacion = (Habitacion) mapa.obtenerDatosVertice(i);
            // Obtiene una habitación concreta

            for (int fila = 0; fila < habitacion.getFilas(); fila++) {
                // Recorre todas las filas de esa habitación

                for (int columna = 0; columna < habitacion.getColumnas(); columna++) {
                    // Recorre todas las columnas de esa fila

                    if (habitacion.getCelda(fila, columna).getTipo() == Celda.Tipo.SALIDA) {
                        return i;
                    }
                    // Si encuentra una celda de tipo SALIDA, devuelve el índice de la habitación que la contiene
                }
            }
        }

        return -1;
        // Si ninguna habitación tiene salida, devuelve -1
    }

    /** Metodo que finaliza completamente el turno actual
    */
    public void finalizarTurnoCompleto() {
        if (!victoria && !derrota) {
            // Solo procesa enemigos si la partida sigue activa; si ya ganó o perdió, los enemigos no deben actuar

            procesarTurnoEnemigosLocales();
            // Hace actuar a todos los enemigos de la habitación actual
        }

        if (!victoria && !derrota) {
            // Solo resta turnos si la partida no terminó durante el turno enemigo

            turnosRestantes--;
            // Resta 1 turno global de la partida

            registro.registrar("Turnos restantes de partida: " + turnosRestantes);
            // Guarda en el registro cuántos turnos quedan
        }

        if (jugador.getVidaActual() <= 0) {
            // Si el jugador murió durante el turno enemigo

            derrota = true;
            // Marca la partida como perdida

            registro.registrar("El jugador ha muerto. Fin del juego.");
            // Guarda mensaje de derrota

        } else if (turnosRestantes <= 0 && !victoria) {
            // Si ya no quedan turnos y todavía no ganó

            derrota = true;
            // Marca derrota por tiempo

            registro.registrar("Te has quedado sin turnos. ¡Fin del juego!");
            // Guarda mensaje correspondiente
        }

        jugadorYaSeMovioEnEsteTurno = false;
        // Reinicia el movimiento para el siguiente turno

        jugadorYaActuoEnEsteTurno = false;
        // Reinicia las acciones para el siguiente turno
    }

    /** Metodo que permite terminar el turno manualmente
     */
    public void finalizarTurnoVoluntariamente() {
        registro.registrar("El jugador decide terminar su turno.");
        // Guarda en el registro que el jugador pasó turno

        finalizarTurnoCompleto();
        // Llama al metodo que hace toda la lógica de final de turno
    }

    /** Metodo que procesa el turno de todos los enemigos de la habitación actual
     */
    private void procesarTurnoEnemigosLocales() {

        Habitacion hab = getHabitacionActual();
        // Obtiene la habitación donde está el jugador

        ListaSimplementeEnlazada<Posicion> posicionesIniciales = hab.obtenerPosicionesConEnemigos();
        // Guarda las posiciones iniciales de los enemigos
        // Esto evita que un enemigo se mueva y luego vuelva a encontrarse otra vez más adelante en el recorrido

        for (int i = 0; i < posicionesIniciales.tamaño(); i++) {
            // Recorre todos los enemigos encontrados

            Posicion posicion = posicionesIniciales.obtener(i);
            // Obtiene la posición actual del enemigo

            Celda celda = hab.getCelda(posicion.getFila(), posicion.getColumna());
            // Obtiene la celda donde está el enemigo

            // Comprueba que: la celda exista, siga siendo Enemigo, y el contenido realmente sea un Enemigo
            // Si no, pasa al siguiente enemigo
            if (celda == null || celda.getTipo() != Celda.Tipo.ENEMIGO || !(celda.getContenido() instanceof Enemigo)) {
                continue;
            }

            Enemigo enemigo = (Enemigo) celda.getContenido();
            // Convierte el contenido de la celda en objeto Enemigo

            if (enemigo == null || !enemigo.estaVivo()) {
                continue;
            }
            // Si el enemigo está muerto o es null, no actúa

            if (estaAlrededorDelJugador(posicion.getFila(), posicion.getColumna())) {
                // Si el enemigo ya está junto al jugador

                ataqueEnemigoAlJugador(enemigo);
                // Ataca directamente

                if (derrota) return;
                // Si el jugador murió, termina inmediatamente el turno enemigo

            } else {    // Si no está cerca
                Posicion nuevaPosicion = moverEnemigoHaciaJugador(hab, posicion, enemigo);
                // Intenta mover al enemigo hacia el jugador

                // Después de moverse, comprueba si quedó al lado del jugador
                if (estaAlrededorDelJugador(nuevaPosicion.getFila(), nuevaPosicion.getColumna())) {

                    ataqueEnemigoAlJugador(enemigo);
                    // Si quedó cerca, ataca

                    if (derrota) return;
                    // Si el jugador murió, termina el turno enemigo
                }
            }
        }
    }

    /** Metodo que comprueba si una posición está alrededor del jugador
     */
    private boolean estaAlrededorDelJugador(int fila, int columna) {

        int distanciaFila = Math.abs(fila - jugador.getPosicionX());
        // Distancia vertical entre enemigo y jugador

        int distanciaColumna = Math.abs(columna - jugador.getPosicionY());
        // Distancia horizontal entre enemigo y jugador

        return distanciaFila <= 1
                && distanciaColumna <= 1    // <= 1 permite diagonales.
                && (distanciaFila + distanciaColumna > 0);   // > 0 evita contar la misma casilla del jugador.
    }

    /** Metodo que hace que un enemigo ataque al jugador
     */
    private void ataqueEnemigoAlJugador(Enemigo enemigo) {

        int danoReal = jugador.recibirDano(enemigo.getAtaque());
        // El jugador recibe daño automáticamente, y recibirDano ya tiene en cuenta la defensa del jugador

        // Guarda en el registro: qué enemigo atacó, la defensa usada, y el daño recibido
        registro.registrar(
                "¡El " + enemigo.getNombre() + " te ataca! Te defiendes automáticamente con " +
                        jugador.getDefensaTotal() +
                        " de defensa y recibes " +
                        danoReal +
                        " puntos de daño."
        );

        comprobarDerrotaPorVida();
        // Comprueba si el jugador murió tras el ataque
    }

    /** Metodo que mueve un enemigo hacia el jugador
     */
    private Posicion moverEnemigoHaciaJugador(Habitacion hab, Posicion posicionInicial, Enemigo enemigo) {
        Posicion posicionActual = posicionInicial;
        // Empieza desde su posición inicial

        for (int paso = 0; paso < enemigo.getVelocidad(); paso++) {
            // El enemigo se mueve tantos pasos como indique su velocidad

            Posicion siguiente = calcularSiguientePasoEnemigo(hab, posicionActual);
            // Calcula cuál es la mejor casilla siguiente para acercarse al jugador

            if (siguiente == null) {
                break;
            }
            // Si no existe movimiento válido, deja de moverse

            Celda celdaActual =
                    hab.getCelda(posicionActual.getFila(), posicionActual.getColumna());
            // Celda donde estaba antes

            Celda celdaSiguiente =
                    hab.getCelda(siguiente.getFila(), siguiente.getColumna());
            // Celda a la que se moverá

            celdaSiguiente.setTipo(Celda.Tipo.ENEMIGO);
            // Convierte la nueva celda en una celda de enemigo

            celdaSiguiente.setContenido(enemigo);
            // Mete el enemigo en esa nueva celda

            celdaActual.limpiar();
            // Borra la celda anterior para que quede vacía

            enemigo.setPosicion(siguiente.getFila(), siguiente.getColumna());
            // Actualiza las coordenadas internas del enemigo

            posicionActual = siguiente;
            // La nueva posición actual pasa a ser la siguiente

            // Si ya llegó junto al jugador, deja de moverse aunque le queden pasos
            if (estaAlrededorDelJugador(
                    posicionActual.getFila(),
                    posicionActual.getColumna())) {

                break;
            }
        }

        return posicionActual;
        // Devuelve la posición final donde terminó el enemigo
    }

    /** Metodo que calcula el mejor siguiente paso para acercarse al jugador
     */
    private Posicion calcularSiguientePasoEnemigo(Habitacion hab, Posicion origen) {

        // Movimientos posibles del enemigo
        int[][] direcciones = {
                {-1, 0}, // arriba
                {1, 0},  // abajo
                {0, -1}, // izquierda
                {0, 1}   // derecha
        };

        Posicion mejor = null;
        // Guardará la mejor casilla encontrada

        // Distancia actual entre enemigo y jugador
        int mejorDistancia = Math.abs(origen.getFila() - jugador.getPosicionX()) + Math.abs(origen.getColumna() - jugador.getPosicionY());

        for (int[] dir : direcciones) {
            // Recorre las 4 direcciones posibles

            int nuevaFila = origen.getFila() + dir[0];
            // Calcula nueva fila

            int nuevaColumna = origen.getColumna() + dir[1];
            // Calcula nueva columna

            if (!hab.esPosicionValida(nuevaFila, nuevaColumna)) {
                continue;
            }
            // Si la posición no existe dentro del mapa, la ignora

            if (nuevaFila == jugador.getPosicionX() && nuevaColumna == jugador.getPosicionY()) {
                continue;
            }
            // El enemigo NO puede entrar en la casilla exacta del jugador

            Celda destino = hab.getCelda(nuevaFila, nuevaColumna);
            // Obtiene la celda candidata

            // El enemigo solo puede moverse por casillas vacías.
            // No atraviesa: objetos, trampas, puertas, salida, otros enemigos
            if (destino == null || destino.getTipo() != Celda.Tipo.VACIA) {
                continue;
            }

            int distancia =
                    Math.abs(nuevaFila - jugador.getPosicionX())
                            + Math.abs(nuevaColumna - jugador.getPosicionY());
            // Calcula distancia jugador-casilla candidata

            if (distancia < mejorDistancia) {
                // Si esta casilla lo acerca más al jugador

                mejorDistancia = distancia;
                // Actualiza la mejor distancia encontrada

                mejor = new Posicion(nuevaFila, nuevaColumna);
                // Guarda esa posición como la mejor opción
            }
        }

        return mejor;
        // Devuelve la mejor casilla encontrada
    }

    /** Metodo que restaura el estado general del motor cuando se carga una partida desde JSON
     * Actualiza los datos que cambian durante la partida: en qué habitación estaba el jugador, cuántos turnos quedaban, si la partida estaba ganada o perdida, si el jugador ya se había movido en ese turno, si el jugador ya había hecho una acción en ese turno
     */
    public void aplicarEstadoPartida(int habitacionActualIndice, int turnosRestantes, boolean victoria, boolean derrota, boolean jugadorYaSeMovioEnEsteTurno, boolean jugadorYaActuoEnEsteTurno) {
        this.habitacionActualIndice = habitacionActualIndice;
        // Restaura el índice de la habitación actual, el cual indica qué vértice del grafo/mapa contiene la habitación donde estaba el jugador

        this.turnosRestantes = turnosRestantes;
        // Restaura los turnos que quedaban cuando se guardó la partida

        this.victoria = victoria;
        // Restaura si la partida ya estaba ganada

        this.derrota = derrota;
        // Restaura si la partida ya estaba perdida

        this.jugadorYaSeMovioEnEsteTurno = jugadorYaSeMovioEnEsteTurno;
        // Restaura si el jugador ya había usado su movimiento en ese turno

        this.jugadorYaActuoEnEsteTurno = jugadorYaActuoEnEsteTurno;
        // Restaura si el jugador ya había usado su acción en ese turno

        Habitacion habitacion = getHabitacionActual();
        // Obtiene la habitación actual usando el índice restaurado

        this.jugador.setHabitacionActualId(habitacion.getId());
        // Actualiza el id de habitación guardado dentro del jugador, así el jugador y el motor quedan sincronizados
    }

    /** Metodo que indica si el jugador ya se movió durante el turno actual
    */
    public boolean isJugadorYaSeMovioEnEsteTurno() {
        return jugadorYaSeMovioEnEsteTurno;
        // Devuelve true si ya se movió y devuelve false si todavía puede moverse
    }

    /** Metodo que indica si el jugador ya hizo una acción durante el turno actual
     */
    public boolean isJugadorYaActuoEnEsteTurno() {
        return jugadorYaActuoEnEsteTurno;
        // Devuelve true si ya hizo una acción y devuelve false si todavía puede actuar.
    }

    /** Metodo que devuelve la habitación en la que está actualmente el jugador
     */
    public Habitacion getHabitacionActual() {
        return (Habitacion) mapa.obtenerDatosVertice(habitacionActualIndice);

    }

    /** Metodo que devuelve el mapa completo del juego
     */
    public Grafo getMapa() {
        return mapa;
        // Devuelve el grafo con todas las habitaciones y conexiones
    }

    /** Metodo que devuelve el jugador de la partida
     */
    public Jugador getJugador() {
        return jugador;
        // Permite acceder a vida, ataque, defensa, inventario y posición del jugador
    }

    /** Metodo que devuelve el registro de la partida
     */
    public RegistroJuego getRegistro() {
        return registro;
        // El registro contiene los mensajes de lo que va pasando en el juego
    }

    /** Metodo que devuelve cuántos turnos quedan
     */
    public int getTurnosRestantes() {
        return turnosRestantes;
        // Sirve para mostrar los turnos en la interfaz y para guardar el estado de la partida
    }

    /** Metodo que devuelve el índice de la habitación actual dentro del grafo
     */
    public int getHabitacionActualIndice() {
        return habitacionActualIndice;
        // Este número indica en qué vértice del grafo está la habitación actual, y se usa especialmente al guardar/cargar JSON
    }

    /** Metodo que indica si la partida está ganada
     */
    public boolean isVictoria() {
        return victoria;
        // Devuelve true si el jugador llegó a la salida
    }

    /** Metodo que indica si la partida está perdida
     */
    public boolean isDerrota() {
        return derrota;
        // Devuelve true si el jugador murió o se quedó sin turnos
    }
}