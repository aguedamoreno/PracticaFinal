package Juego;

import Juego.listas.ListaSimplementeEnlazada;

public class RegistroJuego {
    private final ListaSimplementeEnlazada<String> eventos;

    public RegistroJuego() {
        this.eventos = new ListaSimplementeEnlazada<>();
    }

    public void registrar(String evento) {
        eventos.insertarUltimo(evento);
    }

    public ListaSimplementeEnlazada<String> getEventos() {
        return eventos;
    }

    public String comoTexto() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < eventos.tamaño(); i++) {
            sb.append(i + 1).append(". ").append(eventos.obtener(i)).append("\n");
        }
        return sb.toString();
    }
}
