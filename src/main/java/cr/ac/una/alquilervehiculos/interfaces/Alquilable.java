package cr.ac.una.alquilervehiculos.interfaces;

public interface Alquilable {

    /**
     * Calcula el costo de un alquiler.
     *
     * @param dias cantidad de días del alquiler.
     * @return costo total del alquiler.
     */
    double calcularCostoAlquiler(int dias);
}