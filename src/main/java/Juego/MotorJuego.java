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
        entrada.colocarPuerta(4, 4, "Sala");
        entrada.colocarTrampa(3, 1);

        sala.colocarObjeto(1, 1, new Objeto(Objeto.Tipo.ARMA, "Espada", "+4 ataque", 4));
        sala.colocarEnemigo(3, 3, new Enemigo(Enemigo.Tipo.ORCO));
        sala.colocarPuerta(5, 5, "Salida");
        sala.colocarPuerta(0, 0, "Entrada");

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

            // Al abrir una puerta se acaba el turno al instante
            finalizarTurnoCompleto();
            return;
        }

        // --- CASO 2: LA CASILLA DESTINO ES LA SALIDA ---
        if (celdaDestino.getTipo() == Celda.Tipo.SALIDA) {
            victoria = true;
            registro.registrar("¡Has abierto la puerta de salida! ¡Ganaste la partida!");
            return;
        }

        // --- CASO 3: MOVIMIENTO NORMAL ---
        // Limpiar la celda antigua del jugador en la habitación
        Celda celdaActual = habActual.getCelda(jugador.getPosicionX(), jugador.getPosicionY());
        if (celdaActual != null) {
            celdaActual.limpiar();
        }

        // Actualizar coordenadas internas del jugador
        jugador.setPosicionX(nuevaFila);
        jugador.setPosicionY(nuevaColumna);

        // Colocar al jugador en la nueva celda (usamos VACIA porque no tienes tipo JUGADOR en Celda.java)
        celdaDestino.setTipo(Celda.Tipo.VACIA);
        celdaDestino.setContenido(jugador);

        jugadorYaSeMovioEnEsteTurno = true;
        registro.registrar("El jugador se movió a la casilla (" + nuevaFila + ", " + nuevaColumna + ").");
    }

    public void recogerObjetoAdyacente(int fila, int columna) throws JuegoException {
        validarPartidaActiva();
        validarAdyacente(fila, columna);
        Celda celda = getHabitacionActual().getCelda(fila, columna);
        if (celda.getTipo() != Celda.Tipo.OBJETO || !(celda.getContenido() instanceof Objeto)) {
            throw new JuegoException("No hay objeto recogible en esa celda.");
        }
        Objeto objeto = (Objeto) celda.getContenido();
        jugador.agregarObjetoInventario(objeto);
        celda.limpiar();
        registro.registrar("Jugador recoge " + objeto.getNombre() + ".");
    }

    public void usarObjeto(int indiceInventario) throws JuegoException {
        validarPartidaActiva();
        Objeto objeto = jugador.getInventario().obtener(indiceInventario);
        if (objeto == null) {
            throw new JuegoException("Objeto de inventario no valido.");
        }
        if (objeto.getTipo() == Objeto.Tipo.POCION_VIDA) {
            jugador.setVidaActual(jugador.getVidaActual() + objeto.getValor());
        } else if (objeto.isEquipable()) {
            if (!jugador.equipoObjetoInventario(indiceInventario)) {
                throw new JuegoException("No se puede equipar el objeto.");
            }
            registro.registrar("Jugador equipa " + objeto.getNombre() + ".");
            return;
        }
        if (!jugador.usarObjetoInventario(indiceInventario)) {
            throw new JuegoException("El objeto no puede usarse.");
        }
        registro.registrar("Jugador usa " + objeto.getNombre() + ".");
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
        registro.registrar("Jugador ataca a " + enemigo.getNombre() + " y causa " + dano + " de dano.");
        if (!enemigo.estaVivo()) {
            celda.limpiar();
            registro.registrar(enemigo.getNombre() + " queda derrotado.");
            jugadorYaActuoEnEsteTurno = true;
            registro.registrar("El jugador atacó al enemigo en (" + fila + ", " + columna + ").");

            // Ejecuta automáticamente el turno de los enemigos y resta el turno de la partida
            finalizarTurnoCompleto();
        }
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

    private void finalizarTurnoCompleto() {
        // 1. Turno de los enemigos de la habitación actual
        procesarTurnoEnemigosLocales();

        // 2. Restar turnos globales
        turnosRestantes--;
        registro.registrar("Turnos restantes de partida: " + turnosRestantes);

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

    private void procesarTurnoEnemigosLocales() {
        Habitacion hab = getHabitacionActual();
        // Recorrer la matriz de la habitación para encontrar enemigos vivientes
        for (int f = 0; f < hab.getFilas(); f++) {
            for (int c = 0; c < hab.getColumnas(); c++) {
                Celda celda = hab.getCelda(f, c);
                if (celda != null && celda.getTipo() == Celda.Tipo.ENEMIGO) {
                    Enemigo enemigo = (Enemigo) celda.getContenido();
                    if (enemigo != null && enemigo.estaVivo()) {
                        // Aquí va la IA básica de tu enemigo:
                        // Ej: Si está a rango 1 del jugador, le ataca. Si no, se acerca.
                        // (Recordando que no cambian de habitación)
                    }
                }
            }
        }
    }

    public void finalizarTurnoVoluntariamente() {
        registro.registrar("El jugador decide terminar su turno.");
        finalizarTurnoCompleto();
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
