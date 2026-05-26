package Juego;

import Juego.listas.ListaSimplementeEnlazada;

/**
 * Clase que guarda el historial de eventos de la partida.
 *
 * Cada vez que ocurre algo importante en el juego, se añade un mensaje
 * a este registro.
 *
 * Por ejemplo:
 * - el jugador se mueve,
 * - recoge un objeto,
 * - ataca a un enemigo,
 * - pisa una trampa,
 * - gana o pierde la partida.
 */
public class RegistroJuego {

    // Lista donde se guardan todos los eventos ocurridos durante la partida.
    // Cada String es un mensaje distinto del historial.
    // Se usa final porque la lista como objeto no se cambia por otra,
    // aunque sí se pueden añadir mensajes dentro de ella.
    private final ListaSimplementeEnlazada<String> eventos;

    /**
     * Constructor de RegistroJuego.
     *
     * Crea un registro vacío al empezar la partida.
     */
    public RegistroJuego() {

        // Inicializamos la lista enlazada donde se guardarán los mensajes.
        // Al principio no tiene ningún evento.
        this.eventos = new ListaSimplementeEnlazada<>();
    }

    /**
     * Añade un nuevo mensaje al registro.
     *
     * @param evento texto que describe lo que acaba de pasar en el juego
     */
    public void registrar(String evento) {

        // Insertamos el evento al final de la lista.
        // Así los mensajes quedan ordenados desde el más antiguo
        // hasta el más reciente.
        eventos.insertarUltimo(evento);
    }

    /**
     * Devuelve la lista completa de eventos.
     *
     * @return lista enlazada con todos los mensajes registrados
     */
    public ListaSimplementeEnlazada<String> getEventos() {

        // Devolvemos la lista real de eventos.
        // Esto permite que otras clases puedan consultar el historial.
        return eventos;
    }

    /**
     * Convierte todos los eventos en un único texto.
     *
     * Este método se usa normalmente para mostrar el historial
     * en el TextArea de la interfaz gráfica.
     *
     * @return texto con todos los eventos numerados
     */
    public String comoTexto() {

        // StringBuilder sirve para construir un texto largo de forma eficiente.
        // Es mejor que ir sumando Strings con + dentro de un bucle.
        StringBuilder sb = new StringBuilder();

        // Recorremos todos los eventos guardados en la lista.
        for (int i = 0; i < eventos.tamaño(); i++) {

            // i empieza en 0, pero para mostrarlo al usuario usamos i + 1.
            // Así el primer evento aparece como 1, no como 0.
            sb.append(i + 1)

                    // Añadimos ". " para que quede con formato:
                    // 1. Texto del evento
                    .append(". ")

                    // Obtenemos el evento de la posición i y lo añadimos al texto.
                    .append(eventos.obtener(i))

                    // Añadimos salto de línea para que cada evento salga separado.
                    .append("\n");
        }

        // Convertimos el StringBuilder en un String normal y lo devolvemos.
        return sb.toString();
    }
}

