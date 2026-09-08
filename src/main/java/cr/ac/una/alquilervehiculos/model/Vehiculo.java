package cr.ac.una.alquilervehiculos.model;

import cr.ac.una.alquilervehiculos.interfaces.Alquilable;

public abstract class Vehiculo implements Alquilable {

    private String placa;
    private String marca;
    private double tarifaDiaria;
    private EstadoVehiculo estado;

    /**
     * Crea un vehículo con sus datos principales.
     *
     * @param placa placa del vehículo.
     * @param marca marca del vehículo.
     * @param tarifaDiaria precio del alquiler por día.
     */
    public Vehiculo(String placa, String marca, double tarifaDiaria) {
        this.placa = placa;
        this.marca = marca;
        this.tarifaDiaria = tarifaDiaria;
        this.estado = EstadoVehiculo.DISPONIBLE;
    }

    /**
     * Calcula el costo del alquiler.
     *
     * @param dias cantidad de días del alquiler.
     * @return costo total del alquiler.
     */
    @Override
    public double calcularCostoAlquiler(int dias) {
        return tarifaDiaria * dias;
    }

    /**
     * Obtiene la placa del vehículo.
     *
     * @return placa del vehículo.
     */
    public String getPlaca() {
        return placa;
    }

    /**
     * Modifica la placa del vehículo.
     *
     * @param placa nueva placa.
     */
    public void setPlaca(String placa) {
        this.placa = placa;
    }

    /**
     * Obtiene la marca del vehículo.
     *
     * @return marca del vehículo.
     */
    public String getMarca() {
        return marca;
    }

    /**
     * Modifica la marca del vehículo.
     *
     * @param marca nueva marca.
     */
    public void setMarca(String marca) {
        this.marca = marca;
    }

    /**
     * Obtiene la tarifa diaria.
     *
     * @return tarifa diaria.
     */
    public double getTarifaDiaria() {
        return tarifaDiaria;
    }

    /**
     * Modifica la tarifa diaria.
     *
     * @param tarifaDiaria nueva tarifa diaria.
     */
    public void setTarifaDiaria(double tarifaDiaria) {
        this.tarifaDiaria = tarifaDiaria;
    }

    /**
     * Obtiene el estado actual del vehículo.
     *
     * @return estado del vehículo.
     */
    public EstadoVehiculo getEstado() {
        return estado;
    }

    /**
     * Modifica el estado del vehículo.
     *
     * @param estado nuevo estado del vehículo.
     */
    public void setEstado(EstadoVehiculo estado) {
        this.estado = estado;
    }

    /**
     * Obtiene una representación del vehículo en texto.
     *
     * @return placa, marca y estado del vehículo.
     */
    @Override
    public String toString() {
        return placa + " - " + marca + " - " + estado;
    }
}