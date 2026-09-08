package cr.ac.una.alquilervehiculos.repository;

import java.util.ArrayList;
import java.util.List;

public class Repositorio<T> {

    private List<T> elementos;

    /**
     * Crea un repositorio vacío.
     */
    public Repositorio() {
        elementos = new ArrayList<>();
    }

    /**
     * Agrega un elemento al repositorio.
     *
     * @param elemento elemento que se desea agregar.
     */
    public void agregar(T elemento) {
        elementos.add(elemento);
    }

    /**
     * Elimina un elemento del repositorio.
     *
     * @param elemento elemento que se desea eliminar.
     * @return true si se eliminó correctamente, false si no existía.
     */
    public boolean eliminar(T elemento) {
        return elementos.remove(elemento);
    }

    /**
     * Obtiene todos los elementos almacenados.
     *
     * @return lista de elementos.
     */
    public List<T> obtenerTodos() {
        return elementos;
    }

    /**
     * Obtiene un elemento según su posición.
     *
     * @param indice posición del elemento.
     * @return elemento encontrado.
     */
    public T obtener(int indice) {
        return elementos.get(indice);
    }

    /**
     * Obtiene la cantidad de elementos almacenados.
     *
     * @return cantidad de elementos.
     */
    public int cantidad() {
        return elementos.size();
    }
}