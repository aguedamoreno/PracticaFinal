package Juego;

import Juego.listas.ListaSimplementeEnlazada;

/**
 * Clase que guarda el historial de eventos de la partida
 * Cada vez que ocurre algo importante en el juego, se añade un mensaje a este registro
 * Por ejemplo: - el jugador se mueve, - recoge un objeto, - ataca a un enemigo, - pisa una trampa, - gana o pierde la partida.
 */
public class RegistroJuego {

    // lista donde se guardan todos los eventos ocurridos durante la partida
    // cada String es un mensaje distinto del historial
    // se usa final porque la lista como objeto no se cambia por otra, aunque sí se pueden añadir mensajes dentro de ella.
    private final ListaSimplementeEnlazada<String> eventos;

    /** constructor de RegistroJuego
     * Crea un registro vacío al empezar la partida
     */
    public RegistroJuego() {

        // Inicializamos la lista enlazada donde se guardarán los mensajes
        this.eventos = new ListaSimplementeEnlazada<>();
    }

    /** añade un nuevo mensaje al registro.
     * @param evento texto que describe lo que acaba de pasar en el juego
     */
    public void registrar(String evento) {

        // insertamos el evento al final de la lista
        // Así los mensajes quedan ordenados desde el más antiguo hasta el más reciente
        eventos.insertarUltimo(evento);
    }

    /** devuelve la lista completa de eventos.
     * @return lista enlazada con todos los mensajes registrados
     */
    public ListaSimplementeEnlazada<String> getEventos() {

        // devolvemos la lista real de eventos, esto permite que otras clases puedan consultar el historial
        return eventos;
    }

    /** Convierte todos los eventos en un único texto. Este método se usa normalmente para mostrar el historial
     * @return texto con todos los eventos numerados
     */
    public String comoTexto() {

        // StringBuilder sirve para construir un texto largo de forma eficiente, es mejor que ir sumando Strings con + dentro de un bucle.
        StringBuilder sb = new StringBuilder();

        // recorremos todos los eventos guardados en la lista
        for (int i = 0; i < eventos.tamaño(); i++) {

            // i empieza en 0, pero para mostrarlo al usuario usamos i + 1; así el primer evento aparece como 1, no como 0.
            sb.append(i + 1)

                    // añadimos ". " para que quede con formato: 1. Texto del evento
                    .append(". ")

                    // obtenemos el evento de la posición i y lo añadimos al texto
                    .append(eventos.obtener(i))

                    // añadimos salto de línea para que cada evento salga separado
                    .append("\n");
        }

        // convertimos el StringBuilder en un String normal y lo devolvemos
        return sb.toString();
    }
}

