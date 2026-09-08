package cr.ac.una.alquilervehiculos.model;

public class Cuadraciclo extends Vehiculo {

    private String tipoTraccion;

    /**
     * Crea un cuadraciclo con sus datos principales.
     *
     * @param placa placa del cuadraciclo.
     * @param marca marca del cuadraciclo.
     * @param tarifaDiaria precio del alquiler por día.
     * @param tipoTraccion tipo de tracción del cuadraciclo.
     */
    public Cuadraciclo(String placa, String marca, double tarifaDiaria, String tipoTraccion) {
        super(placa, marca, tarifaDiaria);
        this.tipoTraccion = tipoTraccion;
    }

    /**
     * Obtiene el tipo de tracción del cuadraciclo.
     *
     * @return tipo de tracción.
     */
    public String getTipoTraccion() {
        return tipoTraccion;
    }

    /**
     * Modifica el tipo de tracción del cuadraciclo.
     *
     * @param tipoTraccion nuevo tipo de tracción.
     */
    public void setTipoTraccion(String tipoTraccion) {
        this.tipoTraccion = tipoTraccion;
    }

    /**
     * Obtiene una representación del cuadraciclo en texto.
     *
     * @return información principal del cuadraciclo.
     */
    @Override
    public String toString() {
        return "Cuadraciclo - " + super.toString() + " - " + tipoTraccion;
    }
}