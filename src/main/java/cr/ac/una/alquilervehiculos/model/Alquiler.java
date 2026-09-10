package cr.ac.una.alquilervehiculos.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Alquiler {

    private int id;
    private Cliente cliente;
    private Vehiculo vehiculo;
    private LocalDate fechaInicio;
    private int dias;
    private double depositoGarantia;
    private double costoAlquiler;

    private boolean finalizado;
    private LocalDate fechaDevolucionReal;

    /**
     * Crea un alquiler con sus datos principales.
     *
     * @param id identificador del alquiler.
     * @param cliente cliente que realiza el alquiler.
     * @param vehiculo vehículo que se alquila.
     * @param fechaInicio fecha de inicio del alquiler.
     * @param dias cantidad de días del alquiler.
     * @param depositoGarantia monto del depósito de garantía.
     */
    public Alquiler(int id, Cliente cliente, Vehiculo vehiculo,
                    LocalDate fechaInicio, int dias, double depositoGarantia) {

        this.id = id;
        this.cliente = cliente;
        this.vehiculo = vehiculo;
        this.fechaInicio = fechaInicio;
        this.dias = dias;
        this.depositoGarantia = depositoGarantia;

        this.costoAlquiler =
                vehiculo.calcularCostoAlquiler(dias);

        this.finalizado = false;
        this.fechaDevolucionReal = null;
    }

    /**
     * Obtiene el identificador del alquiler.
     *
     * @return identificador del alquiler.
     */
    public int getId() {
        return id;
    }

    /**
     * Obtiene el cliente asociado al alquiler.
     *
     * @return cliente del alquiler.
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * Obtiene el vehículo alquilado.
     *
     * @return vehículo asociado.
     */
    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    /**
     * Obtiene la fecha de inicio.
     *
     * @return fecha de inicio del alquiler.
     */
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    /**
     * Obtiene la cantidad de días del alquiler.
     *
     * @return cantidad de días.
     */
    public int getDias() {
        return dias;
    }

    /**
     * Obtiene el depósito de garantía.
     *
     * @return monto del depósito.
     */
    public double getDepositoGarantia() {
        return depositoGarantia;
    }

    /**
     * Obtiene el costo del alquiler sin incluir el depósito.
     *
     * @return costo del alquiler.
     */
    public double getCostoAlquiler() {
        return costoAlquiler;
    }

    /**
     * Indica si el alquiler ya fue finalizado.
     *
     * @return true si ya fue devuelto, false si continúa activo.
     */
    public boolean isFinalizado() {
        return finalizado;
    }

    /**
     * Obtiene la fecha real en que se devolvió el vehículo.
     *
     * @return fecha real de devolución o null si sigue activo.
     */
    public LocalDate getFechaDevolucionReal() {
        return fechaDevolucionReal;
    }

    /**
     * Calcula el monto total inicial del alquiler.
     *
     * @return costo del alquiler más depósito de garantía.
     */
    public double calcularTotal() {
        return costoAlquiler + depositoGarantia;
    }

    /**
     * Obtiene la fecha prevista de devolución.
     *
     * @return fecha en que debe devolverse el vehículo.
     */
    public LocalDate getFechaDevolucionPrevista() {
        return fechaInicio.plusDays(dias);
    }

    /**
     * Calcula la cantidad de días de atraso.
     *
     * @param fechaDevolucionReal fecha real de devolución.
     * @return días de atraso o cero si se devuelve a tiempo.
     */
    public long calcularDiasAtraso(
            LocalDate fechaDevolucionReal) {

        if (fechaDevolucionReal == null) {
            throw new IllegalArgumentException(
                    "Debe indicar la fecha de devolución."
            );
        }

        if (fechaDevolucionReal.isBefore(fechaInicio)) {
            throw new IllegalArgumentException(
                    "La fecha de devolución no puede ser anterior a la fecha de inicio."
            );
        }

        LocalDate fechaPrevista =
                getFechaDevolucionPrevista();

        if (!fechaDevolucionReal.isAfter(fechaPrevista)) {
            return 0;
        }

        return ChronoUnit.DAYS.between(
                fechaPrevista,
                fechaDevolucionReal
        );
    }

    /**
     * Finaliza el alquiler y guarda la fecha real
     * en que el vehículo fue devuelto.
     *
     * @param fechaDevolucionReal fecha real de devolución.
     */
    public void finalizarAlquiler(
            LocalDate fechaDevolucionReal) {

        if (fechaDevolucionReal == null) {
            throw new IllegalArgumentException(
                    "Debe indicar la fecha de devolución."
            );
        }

        if (fechaDevolucionReal.isBefore(fechaInicio)) {
            throw new IllegalArgumentException(
                    "La fecha de devolución no puede ser anterior a la fecha de inicio."
            );
        }

        this.fechaDevolucionReal = fechaDevolucionReal;
        this.finalizado = true;
    }

    /**
     * Obtiene una representación del alquiler en texto.
     *
     * @return información principal del alquiler.
     */
    @Override
    public String toString() {

        String estadoAlquiler;

        if (finalizado) {
            estadoAlquiler = "FINALIZADO";
        } else {
            estadoAlquiler = "ACTIVO";
        }

        return "Alquiler #" + id
                + " - " + cliente.getNombre()
                + " - " + vehiculo.getPlaca()
                + " - " + estadoAlquiler;
    }
}