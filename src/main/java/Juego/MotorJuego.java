package Juego;

import Juego.listas.ListaSimplementeEnlazada;

public class MotorJuego {
    private final Grafo mapa;
    private final Jugador jugador;
    private final RegistroJuego registro;
    private int habitacionActualIndice;
    private int turnosRestantes;
    private boolean victoria;
    private boolean derrota;
    private boolean jugadorYaSeMovioEnEsteTurno = false;
    private boolean jugadorYaActuoEnEsteTurno = false;

    public MotorJuego(Grafo mapa, Jugador jugador, int habitacionInicialIndice, int turnosRestantes) {
        this.mapa = mapa;
        this.jugador = jugador;
        this.habitacionActualIndice = habitacionInicialIndice;
        this.turnosRestantes = turnosRestantes;
        this.registro = new RegistroJuego();
        Habitacion habitacion = getHabitacionActual();
        this.jugador.setHabitacionActualId(habitacion.getId());
        this.victoria = false;
        this.derrota = false;
        registro.registrar("Partida iniciada en " + habitacion.getId() + ".");
    }

    public static MotorJuego crearPartidaDemo() {
        Grafo mapa = new Grafo(false);
        Habitacion entrada = new Habitacion("Entrada", 5, 5);
        Habitacion sala = new Habitacion("Sala", 6, 6);
        Habitacion salida = new Habitacion("Salida", 4, 5);

        entrada.colocarObjeto(0, 1, new Objeto(Objeto.Tipo.POCION_VIDA, "Pocion pequena", "+15 vida", 15));
        entrada.colocarEnemigo(2, 3, new Enemigo(Enemigo.Tipo.GOBLIN));
        sala.colocarEnemigo(2, 4, new Enemigo(Enemigo.Tipo.LADRON));
        entrada.colocarPuerta(4, 4, "Sala");
        entrada.colocarTrampa(3, 1);

        sala.colocarObjeto(1, 1, new Objeto(Objeto.Tipo.ARMA, "Espada", "+4 ataque", 4));
        sala.colocarEnemigo(3, 3, new Enemigo(Enemigo.Tipo.ORCO));
        sala.colocarPuerta(5, 5, "Salida");
        sala.colocarPuerta(0, 0, "Entrada");

        salida.colocarEnemigo(0, 3, new Enemigo(Enemigo.Tipo.DRAGON));
        salida.colocarEnemigo(1, 2, new Enemigo(Enemigo.Tipo.ESQUELETO));
        salida.colocarSalida(3, 4);
        salida.colocarPuerta(0, 0, "Sala");

        int iEntrada = mapa.agregarVertice(entrada);
        int iSala = mapa.agregarVertice(sala);
        int iSalida = mapa.agregarVertice(salida);
        mapa.agregarArista(iEntrada, iSala);
        mapa.agregarArista(iSala, iSalida);

        Jugador jugador = new Jugador(100, 10, 3, 3);
        jugador.setPosicionX(0);
        jugador.setPosicionY(0);
        return new MotorJuego(mapa, jugador, iEntrada, 30);
    }

    public void moverJugador(int nuevaFila, int nuevaColumna) throws JuegoException {
        if (derrota || victoria) {
            throw new JuegoException("La partida ya ha finalizado.");
        }
        if (jugadorYaSeMovioEnEsteTurno) {
            throw new JuegoException("Ya te has movido en este turno.");
        }
        if (jugadorYaActuoEnEsteTurno) {
            throw new JuegoException("No puedes moverte después de haber realizado una acción.");
        }

        // Obtener la habitación actual usando tu método real
        Habitacion habActual = getHabitacionActual();

        // Obtener la posición actual del jugador
        Posicion origen = new Posicion(jugador.getPosicionX(), jugador.getPosicionY());

        // Validar si la celda destino está dentro de las alcanzables (MÁXIMO 2 EN CRUZ)
        ListaSimplementeEnlazada<Posicion> alcanzables = habActual.calcularPosicionesAlcanzables(origen, 2);
        boolean destinoValido = false;

        for (int i = 0; i < alcanzables.tamaño(); i++) {
            Posicion p = alcanzables.obtener(i);
            if (p.getFila() == nuevaFila && p.getColumna() == nuevaColumna) {
                destinoValido = true;
                break;
            }
        }

        if (!destinoValido) {
            throw new JuegoException("Movimiento inválido. Solo puedes moverte hasta 2 casillas en línea recta (arriba, abajo, izquierda, derecha).");
        }

        Celda celdaDestino = habActual.getCelda(nuevaFila, nuevaColumna);

        // --- CASO 1: LA CASILLA DESTINO ES UNA PUERTA ---
        if (celdaDestino.getTipo() == Celda.Tipo.PUERTA) {
            String idHabitacionDestino = (String) celdaDestino.getContenido();
            registro.registrar("¡El jugador cruza una puerta hacia la habitación " + idHabitacionDestino + "!");

            // Usamos tu lógica exacta original para cambiar el índice de la habitación en el Grafo
            int nuevoIndice = buscarHabitacionPorId(idHabitacionDestino);
            if (nuevoIndice != -1) {
                habitacionActualIndice = nuevoIndice;
                jugador.setHabitacionActualId(idHabitacionDestino);

                // Teletransportamos al jugador al origen (0,0) de la nueva habitación o donde corresponda
                jugador.setPosicionX(0);
                jugador.setPosicionY(0);
            }

            // Cruzar una puerta cuenta como movimiento, pero NO termina el turno automáticamente.
            // El turno solo termina cuando el jugador pulsa "Pasar Turno".
            jugadorYaSeMovioEnEsteTurno = true;
            return;
        }

        // --- CASO 2: LA CASILLA DESTINO ES LA SALIDA ---
        if (celdaDestino.getTipo() == Celda.Tipo.SALIDA) {
            victoria = true;
            registro.registrar("¡Has abierto la puerta de salida! ¡Ganaste la partida!");
            return;
        }

        // --- CASO 3: LA CASILLA DESTINO ES UNA TRAMPA ---
        // El jugador SÍ puede moverse a una trampa. Al pisarla, pierde vida
        // y después la trampa desaparece de la habitación.
        if (celdaDestino.getTipo() == Celda.Tipo.TRAMPA) {
            jugador.setPosicionX(nuevaFila);
            jugador.setPosicionY(nuevaColumna);

            int danoTrampa = 10;
            jugador.setVidaActual(jugador.getVidaActual() - danoTrampa);
            celdaDestino.limpiar();

            jugadorYaSeMovioEnEsteTurno = true;
            registro.registrar("El jugador pisó una trampa en (" + nuevaFila + ", " + nuevaColumna + ") y perdió " + danoTrampa + " puntos de vida.");
            comprobarDerrotaPorVida();
            return;
        }

        // --- CASO 4: MOVIMIENTO NORMAL ---
        // Simplemente actualizamos las coordenadas internas del jugador.
        // ¡YA NO cambiamos el tipo de la celda a VACIA ni metemos al jugador dentro!
        jugador.setPosicionX(nuevaFila);
        jugador.setPosicionY(nuevaColumna);

        jugadorYaSeMovioEnEsteTurno = true;
        registro.registrar("El jugador se movió a la casilla (" + nuevaFila + ", " + nuevaColumna + ").");
    }

    public void recogerObjetoAdyacente() throws JuegoException {
        // Versión de apoyo: si no se selecciona casilla, intenta recoger primero debajo
        // y luego en las 8 casillas de alrededor.
        validarPartidaActiva();

        for (int df = -1; df <= 1; df++) {
            for (int dc = -1; dc <= 1; dc++) {
                int fila = jugador.getPosicionX() + df;
                int columna = jugador.getPosicionY() + dc;
                if (getHabitacionActual().esPosicionValida(fila, columna)) {
                    Celda celda = getHabitacionActual().getCelda(fila, columna);
                    if (celda != null && celda.getTipo() == Celda.Tipo.OBJETO) {
                        recogerObjetoAdyacente(fila, columna);
                        return;
                    }
                }
            }
        }

        throw new JuegoException("No hay ningún objeto ni en tu casilla ni en las casillas de alrededor.");
    }

    public void recogerObjetoAdyacente(int fila, int columna) throws JuegoException {
        validarPartidaActiva();

        if (jugadorYaActuoEnEsteTurno) {
            throw new JuegoException("Ya has realizado una acción en este turno.");
        }

        if (!getHabitacionActual().esPosicionValida(fila, columna)) {
            throw new JuegoException("Posición fuera de la habitación.");
        }

        // Permitimos recoger en la casilla actual o en cualquiera de las 8 casillas de alrededor.
        int distanciaFila = Math.abs(fila - jugador.getPosicionX());
        int distanciaColumna = Math.abs(columna - jugador.getPosicionY());

        if (distanciaFila > 1 || distanciaColumna > 1) {
            throw new JuegoException("Solo puedes recoger objetos de tu casilla o de las casillas de alrededor.");
        }

        Habitacion habActual = getHabitacionActual();
        Celda celdaObjeto = habActual.getCelda(fila, columna);

        if (celdaObjeto == null || celdaObjeto.getTipo() != Celda.Tipo.OBJETO || !(celdaObjeto.getContenido() instanceof Objeto)) {
            throw new JuegoException("No hay ningún objeto en esa casilla para recoger.");
        }

        Objeto objeto = (Objeto) celdaObjeto.getContenido();
        jugador.getInventario().insertarUltimo(objeto);
        registro.registrar("Has recogido el objeto: " + objeto.getNombre() + ".");

        celdaObjeto.limpiar();

        jugadorYaActuoEnEsteTurno = true;
        registro.registrar("Acción realizada. Puedes terminar el turno con 'Pasar Turno'.");
    }

    public void usarObjeto(int indiceInventario) throws JuegoException {
        validarPartidaActiva();
        Objeto objeto = jugador.getInventario().obtener(indiceInventario);
        if (objeto == null) {
            throw new JuegoException("Objeto de inventario no valido.");
        }
        if (jugadorYaActuoEnEsteTurno) {
            throw new JuegoException("Ya has realizado una acción en este turno.");
        }

        if (objeto.getTipo() == Objeto.Tipo.POCION_VIDA) {
            jugador.setVidaActual(jugador.getVidaActual() + objeto.getValor());
            if (!jugador.usarObjetoInventario(indiceInventario)) {
                throw new JuegoException("El objeto no puede usarse.");
            }
            registro.registrar("Jugador usa " + objeto.getNombre() + ".");
        } else if (objeto.isEquipable()) {
            if (!jugador.equipoObjetoInventario(indiceInventario)) {
                throw new JuegoException("No se puede equipar el objeto.");
            }
            registro.registrar("Jugador equipa " + objeto.getNombre() + ".");
        } else {
            if (!jugador.usarObjetoInventario(indiceInventario)) {
                throw new JuegoException("El objeto no puede usarse.");
            }
            registro.registrar("Jugador usa " + objeto.getNombre() + ".");
        }

        jugadorYaActuoEnEsteTurno = true;
        registro.registrar("Acción realizada. Puedes terminar el turno con 'Pasar Turno'.");
    }

    public void atacarAdyacente(int fila, int columna) throws JuegoException {
        if (jugadorYaActuoEnEsteTurno) {
            throw new JuegoException("Ya has realizado tu acción en este turno.");
        }
        validarPartidaActiva();
        validarAdyacente(fila, columna);
        Celda celda = getHabitacionActual().getCelda(fila, columna);
        if (celda.getTipo() != Celda.Tipo.ENEMIGO || !(celda.getContenido() instanceof Enemigo)) {
            throw new JuegoException("No hay enemigo atacable en esa celda.");
        }
        Enemigo enemigo = (Enemigo) celda.getContenido();
        int dano = calcularDano(jugador.getAtaqueTotal(), enemigo.getDefensa());
        enemigo.setVidaActual(enemigo.getVidaActual() - dano);
        registro.registrar("Jugador ataca a " + enemigo.getNombre() + " y causa " + dano + " de daño. Vida enemiga: " + enemigo.getVidaActual() + "/" + enemigo.getVidaMax() + ".");

        if (!enemigo.estaVivo()) {
            celda.limpiar();
            registro.registrar(enemigo.getNombre() + " queda derrotado.");
        }

        jugadorYaActuoEnEsteTurno = true;
        registro.registrar("El jugador atacó al enemigo en (" + fila + ", " + columna + ").");

        // El ataque consume la acción del jugador, pero NO termina el turno automáticamente.
        // Así se cumple la norma: como máximo 1 movimiento y 1 acción por turno.
        // Los enemigos solo actúan cuando el jugador pulse "Pasar Turno".
        registro.registrar("Acción realizada. Puedes terminar el turno con 'Pasar Turno'.");
    }

    public ListaSimplementeEnlazada<Posicion> getCeldasAlcanzables() {
        return getHabitacionActual().calcularPosicionesAlcanzables(new Posicion(jugador.getPosicionX(), jugador.getPosicionY()), jugador.getVelocidad());
    }

    public Object[] getCaminoMinimoSalida() {
        int destino = buscarHabitacionConSalida();
        if (destino == -1) {
            return null;
        }
        return mapa.dijkstra(habitacionActualIndice, destino);
    }

    private void resolverCeldaDestino(Celda destino) throws JuegoException {
        if (destino.getTipo() == Celda.Tipo.TRAMPA) {
            jugador.setVidaActual(jugador.getVidaActual() - 10);
            destino.limpiar();
            registro.registrar("El jugador cae en una trampa y pierde 10 de vida.");
            comprobarDerrotaPorVida();
        } else if (destino.getTipo() == Celda.Tipo.PUERTA) {
            cambiarHabitacion((String) destino.getContenido());
        } else if (destino.getTipo() == Celda.Tipo.SALIDA) {
            victoria = true;
            registro.registrar("El jugador abre la salida exterior. Victoria.");
        }
    }

    private void cambiarHabitacion(String idDestino) throws JuegoException {
        int destino = buscarHabitacionPorId(idDestino);
        if (destino == -1) {
            throw new JuegoException("La puerta no tiene habitacion destino valida.");
        }
        habitacionActualIndice = destino;
        jugador.setHabitacionActualId(idDestino);
        jugador.setPosicionX(0);
        jugador.setPosicionY(0);
        registro.registrar("Jugador cambia a la habitacion " + idDestino + ".");
    }

    private void turnoEnemigos() {
        Habitacion habitacion = getHabitacionActual();
        ListaSimplementeEnlazada<Posicion> enemigos = habitacion.obtenerPosicionesConEnemigos();
        for (int i = 0; i < enemigos.tamaño(); i++) {
            Posicion posicion = enemigos.obtener(i);
            Celda celda = habitacion.getCelda(posicion.getFila(), posicion.getColumna());
            if (celda.getTipo() == Celda.Tipo.ENEMIGO && celda.getContenido() instanceof Enemigo) {
                Enemigo enemigo = (Enemigo) celda.getContenido();
                if (distanciaJugador(posicion) == 1) {
                    int dano = calcularDano(enemigo.getAtaque(), jugador.getDefensaTotal());
                    jugador.setVidaActual(jugador.getVidaActual() - dano);
                    registro.registrar(enemigo.getNombre() + " ataca al jugador y causa " + dano + " de dano.");
                    comprobarDerrotaPorVida();
                }
            }
        }
    }

    private int calcularDano(int ataque, int defensa) {
        double aleatorio = Math.random() * 2;
        return Math.max(0, (int) Math.round(ataque * aleatorio - defensa));
    }

    private int distanciaJugador(Posicion posicion) {
        return Math.abs(posicion.getFila() - jugador.getPosicionX()) + Math.abs(posicion.getColumna() - jugador.getPosicionY());
    }

    private void validarAdyacente(int fila, int columna) throws JuegoException {
        if (!getHabitacionActual().esPosicionValida(fila, columna)) {
            throw new JuegoException("Posicion fuera de la habitacion.");
        }
        if (Math.abs(fila - jugador.getPosicionX()) + Math.abs(columna - jugador.getPosicionY()) != 1) {
            throw new JuegoException("La accion solo puede hacerse sobre una celda adyacente.");
        }
    }

    private void validarPartidaActiva() throws JuegoException {
        if (victoria || derrota) {
            throw new JuegoException("La partida ya ha terminado.");
        }
    }

    private void consumirTurno() {
        turnosRestantes--;
        if (turnosRestantes <= 0 && !victoria) {
            derrota = true;
            registro.registrar("El jugador se queda sin turnos. Derrota.");
        }
    }

    private void comprobarDerrotaPorVida() {
        if (jugador.getVidaActual() <= 0) {
            jugador.setVivo(false);
            derrota = true;
            registro.registrar("El jugador pierde todos sus puntos de vida. Derrota.");
        }
    }

    private int buscarHabitacionPorId(String id) {
        for (int i = 0; i < mapa.getSize(); i++) {
            Habitacion habitacion = (Habitacion) mapa.obtenerDatosVertice(i);
            if (habitacion.getId().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    private int buscarHabitacionConSalida() {
        for (int i = 0; i < mapa.getSize(); i++) {
            Habitacion habitacion = (Habitacion) mapa.obtenerDatosVertice(i);
            for (int fila = 0; fila < habitacion.getFilas(); fila++) {
                for (int columna = 0; columna < habitacion.getColumnas(); columna++) {
                    if (habitacion.getCelda(fila, columna).getTipo() == Celda.Tipo.SALIDA) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    public void finalizarTurnoCompleto() {
        // Si el jugador ya ganó, no dejamos que los enemigos ataquen "después de la victoria".
        if (!victoria && !derrota) {
            // 1. Turno de los enemigos de la habitación actual.
            // IMPORTANTE: esto NO resta turnos del jugador.
            procesarTurnoEnemigosLocales();
        }

        // 2. Restar SOLO 1 turno global por ronda completa del jugador.
        // El enemigo actúa entre turnos, pero no consume otro turno adicional.
        if (!victoria && !derrota) {
            turnosRestantes--;
            registro.registrar("Turnos restantes de partida: " + turnosRestantes);
        }

        // 3. Comprobar condiciones de derrota por falta de tiempo o muerte
        if (jugador.getVidaActual() <= 0) {
            derrota = true;
            registro.registrar("El jugador ha muerto. Fin del juego.");
        } else if (turnosRestantes <= 0 && !victoria) {
            derrota = true;
            registro.registrar("Te has quedado sin turnos. ¡Fin del juego!");
        }

        // 4. Reiniciar los flags para el nuevo turno del jugador
        jugadorYaSeMovioEnEsteTurno = false;
        jugadorYaActuoEnEsteTurno = false;
    }

    public void finalizarTurnoVoluntariamente() {
        registro.registrar("El jugador decide terminar su turno.");
        finalizarTurnoCompleto();
    }

    private void procesarTurnoEnemigosLocales() {
        Habitacion hab = getHabitacionActual();

        // Primero guardamos las posiciones iniciales para que un enemigo que se mueva
        // no vuelva a actuar otra vez al ser encontrado en su nueva casilla.
        ListaSimplementeEnlazada<Posicion> posicionesIniciales = hab.obtenerPosicionesConEnemigos();

        for (int i = 0; i < posicionesIniciales.tamaño(); i++) {
            Posicion posicion = posicionesIniciales.obtener(i);
            Celda celda = hab.getCelda(posicion.getFila(), posicion.getColumna());

            if (celda == null || celda.getTipo() != Celda.Tipo.ENEMIGO || !(celda.getContenido() instanceof Enemigo)) {
                continue;
            }

            Enemigo enemigo = (Enemigo) celda.getContenido();

            if (enemigo == null || !enemigo.estaVivo()) {
                continue;
            }

            // Si ya está alrededor del jugador, ataca.
            if (estaAlrededorDelJugador(posicion.getFila(), posicion.getColumna())) {
                ataqueEnemigoAlJugador(enemigo);
                if (derrota) return;
            } else {
                // Si no está cerca, se mueve hacia el jugador usando su velocidad.
                Posicion nuevaPosicion = moverEnemigoHaciaJugador(hab, posicion, enemigo);

                // Después de moverse, si queda alrededor del jugador, ataca.
                if (estaAlrededorDelJugador(nuevaPosicion.getFila(), nuevaPosicion.getColumna())) {
                    ataqueEnemigoAlJugador(enemigo);
                    if (derrota) return;
                }
            }
        }
    }

    private boolean estaAlrededorDelJugador(int fila, int columna) {
        int distanciaFila = Math.abs(fila - jugador.getPosicionX());
        int distanciaColumna = Math.abs(columna - jugador.getPosicionY());

        // Casillas de alrededor: incluye diagonales. Excluimos la misma casilla.
        return distanciaFila <= 1 && distanciaColumna <= 1 && (distanciaFila + distanciaColumna > 0);
    }

    private void ataqueEnemigoAlJugador(Enemigo enemigo) {
        // Defensa automática: el jugador siempre aplica su defensa total.
        int danoReal = jugador.recibirDano(enemigo.getAtaque());
        registro.registrar("¡El " + enemigo.getNombre() + " te ataca! Te defiendes automáticamente con " +
                jugador.getDefensaTotal() + " de defensa y recibes " + danoReal + " puntos de daño.");

        comprobarDerrotaPorVida();
    }

    private Posicion moverEnemigoHaciaJugador(Habitacion hab, Posicion posicionInicial, Enemigo enemigo) {
        Posicion posicionActual = posicionInicial;

        for (int paso = 0; paso < enemigo.getVelocidad(); paso++) {
            Posicion siguiente = calcularSiguientePasoEnemigo(hab, posicionActual);

            if (siguiente == null) {
                break; // No puede acercarse más.
            }

            Celda celdaActual = hab.getCelda(posicionActual.getFila(), posicionActual.getColumna());
            Celda celdaSiguiente = hab.getCelda(siguiente.getFila(), siguiente.getColumna());

            celdaSiguiente.setTipo(Celda.Tipo.ENEMIGO);
            celdaSiguiente.setContenido(enemigo);

            celdaActual.limpiar();

            enemigo.setPosicion(siguiente.getFila(), siguiente.getColumna());
            posicionActual = siguiente;

            if (estaAlrededorDelJugador(posicionActual.getFila(), posicionActual.getColumna())) {
                break;
            }
        }

        return posicionActual;
    }

    private Posicion calcularSiguientePasoEnemigo(Habitacion hab, Posicion origen) {
        int[][] direcciones = {
                {-1, 0}, // arriba
                {1, 0},  // abajo
                {0, -1}, // izquierda
                {0, 1}   // derecha
        };

        Posicion mejor = null;
        int mejorDistancia = Math.abs(origen.getFila() - jugador.getPosicionX()) +
                Math.abs(origen.getColumna() - jugador.getPosicionY());

        for (int[] dir : direcciones) {
            int nuevaFila = origen.getFila() + dir[0];
            int nuevaColumna = origen.getColumna() + dir[1];

            if (!hab.esPosicionValida(nuevaFila, nuevaColumna)) {
                continue;
            }

            // El enemigo no puede meterse en la casilla exacta del jugador.
            if (nuevaFila == jugador.getPosicionX() && nuevaColumna == jugador.getPosicionY()) {
                continue;
            }

            Celda destino = hab.getCelda(nuevaFila, nuevaColumna);

            // Para no romper objetos, puertas, trampas, salida u otros enemigos,
            // el enemigo solo se mueve por casillas vacías.
            if (destino == null || destino.getTipo() != Celda.Tipo.VACIA) {
                continue;
            }

            int distancia = Math.abs(nuevaFila - jugador.getPosicionX()) +
                    Math.abs(nuevaColumna - jugador.getPosicionY());

            if (distancia < mejorDistancia) {
                mejorDistancia = distancia;
                mejor = new Posicion(nuevaFila, nuevaColumna);
            }
        }

        return mejor;
    }


    /**
     * Restaura los datos generales de la partida cuando se carga desde JSON.
     * No crea una partida nueva: solo coloca el motor en el mismo estado
     * en el que estaba cuando se guardó.
     */
    public void aplicarEstadoPartida(int habitacionActualIndice, int turnosRestantes, boolean victoria, boolean derrota,
                                     boolean jugadorYaSeMovioEnEsteTurno, boolean jugadorYaActuoEnEsteTurno) {
        this.habitacionActualIndice = habitacionActualIndice;
        this.turnosRestantes = turnosRestantes;
        this.victoria = victoria;
        this.derrota = derrota;
        this.jugadorYaSeMovioEnEsteTurno = jugadorYaSeMovioEnEsteTurno;
        this.jugadorYaActuoEnEsteTurno = jugadorYaActuoEnEsteTurno;

        Habitacion habitacion = getHabitacionActual();
        this.jugador.setHabitacionActualId(habitacion.getId());
    }

    public boolean isJugadorYaSeMovioEnEsteTurno() {
        return jugadorYaSeMovioEnEsteTurno;
    }

    public boolean isJugadorYaActuoEnEsteTurno() {
        return jugadorYaActuoEnEsteTurno;
    }

    public Habitacion getHabitacionActual() {
        return (Habitacion) mapa.obtenerDatosVertice(habitacionActualIndice);
    }

    public Grafo getMapa() {
        return mapa;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public RegistroJuego getRegistro() {
        return registro;
    }

    public int getTurnosRestantes() {
        return turnosRestantes;
    }

    public int getHabitacionActualIndice() {
        return habitacionActualIndice;
    }

    public boolean isVictoria() {
        return victoria;
    }

    public boolean isDerrota() {
        return derrota;
    }

}
