package cr.ac.una.alquilervehiculos.service;

import cr.ac.una.alquilervehiculos.model.Alquiler;
import cr.ac.una.alquilervehiculos.model.Cliente;
import cr.ac.una.alquilervehiculos.model.EstadoVehiculo;
import cr.ac.una.alquilervehiculos.model.Vehiculo;
import cr.ac.una.alquilervehiculos.repository.Repositorio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AlquilerService {

    private Repositorio<Alquiler> alquileres;
    private int siguienteId;

    /**
     * Crea el servicio de alquileres.
     */
    public AlquilerService() {
        alquileres = new Repositorio<>();
        siguienteId = 1;
    }

    /**
     * Registra un nuevo alquiler.
     *
     * @param cliente cliente que realiza el alquiler.
     * @param vehiculo vehículo seleccionado.
     * @param fechaInicio fecha de inicio del alquiler.
     * @param dias cantidad de días del alquiler.
     * @param depositoGarantia depósito solicitado al cliente.
     * @return alquiler creado.
     */
    public Alquiler registrarAlquiler(
            Cliente cliente,
            Vehiculo vehiculo,
            LocalDate fechaInicio,
            int dias,
            double depositoGarantia) {

        if (cliente == null) {
            throw new IllegalArgumentException(
                    "Debe seleccionar un cliente."
            );
        }

        if (vehiculo == null) {
            throw new IllegalArgumentException(
                    "Debe seleccionar un vehículo."
            );
        }

        if (fechaInicio == null) {
            throw new IllegalArgumentException(
                    "Debe seleccionar una fecha de inicio."
            );
        }

        if (vehiculo.getEstado()
                != EstadoVehiculo.DISPONIBLE) {

            throw new IllegalStateException(
                    "El vehículo no está disponible."
            );
        }

        if (dias <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad de días debe ser mayor a cero."
            );
        }

        if (depositoGarantia <= 0) {
            throw new IllegalArgumentException(
                    "El depósito de garantía debe ser mayor a cero."
            );
        }

        Alquiler alquiler = new Alquiler(
                siguienteId,
                cliente,
                vehiculo,
                fechaInicio,
                dias,
                depositoGarantia
        );

        vehiculo.setEstado(
                EstadoVehiculo.ALQUILADO
        );

        alquileres.agregar(alquiler);

        siguienteId++;

        return alquiler;
    }

    /**
     * Calcula la multa por atraso.
     * Se cobra una tarifa diaria completa
     * por cada día de atraso.
     *
     * @param alquiler alquiler que se desea evaluar.
     * @param fechaDevolucionReal fecha real de devolución.
     * @return monto total de la multa.
     */
    public double calcularMulta(
            Alquiler alquiler,
            LocalDate fechaDevolucionReal) {

        validarAlquiler(alquiler);

        if (fechaDevolucionReal == null) {
            throw new IllegalArgumentException(
                    "Debe indicar la fecha de devolución."
            );
        }

        long diasAtraso =
                alquiler.calcularDiasAtraso(
                        fechaDevolucionReal
                );

        double tarifaDiaria =
                alquiler.getVehiculo()
                        .getTarifaDiaria();

        return diasAtraso * tarifaDiaria;
    }

    /**
     * Calcula cuánto depósito se debe devolver al cliente.
     * El depósito se devuelve completo y la multa
     * por atraso se cobra por separado.
     *
     * @param alquiler alquiler que se está devolviendo.
     * @param fechaDevolucionReal fecha real de devolución.
     * @return monto del depósito a devolver.
     */
    public double calcularDepositoADevolver(
            Alquiler alquiler,
            LocalDate fechaDevolucionReal) {

        validarAlquiler(alquiler);
        alquiler.calcularDiasAtraso(fechaDevolucionReal);

        return alquiler.getDepositoGarantia();
    }

    /**
     * Calcula el monto que el cliente debe pagar
     * por concepto de multa al devolver el vehículo.
     *
     * @param alquiler alquiler que se está devolviendo.
     * @param fechaDevolucionReal fecha real de devolución.
     * @return monto de la multa que se debe cobrar.
     */
    public double calcularMontoPendiente(
            Alquiler alquiler,
            LocalDate fechaDevolucionReal) {

        return calcularMulta(
                alquiler,
                fechaDevolucionReal
        );
    }

    /**
     * Registra la devolución de un vehículo.
     * Finaliza el alquiler y vuelve a colocar
     * el vehículo como disponible.
     *
     * @param alquiler alquiler que se desea finalizar.
     * @param fechaDevolucionReal fecha real de devolución.
     * @return multa generada por atraso.
     */
    public double devolverVehiculo(
            Alquiler alquiler,
            LocalDate fechaDevolucionReal) {

        validarAlquiler(alquiler);

        if (fechaDevolucionReal == null) {
            throw new IllegalArgumentException(
                    "Debe indicar la fecha de devolución."
            );
        }

        if (alquiler.isFinalizado()) {
            throw new IllegalStateException(
                    "Este alquiler ya fue finalizado."
            );
        }

        double multa =
                calcularMulta(
                        alquiler,
                        fechaDevolucionReal
                );

        alquiler.finalizarAlquiler(
                fechaDevolucionReal
        );

        alquiler.getVehiculo()
                .setEstado(
                        EstadoVehiculo.DISPONIBLE
                );

        return multa;
    }

    /**
     * Obtiene todos los alquileres registrados.
     *
     * @return lista completa de alquileres.
     */
    public List<Alquiler> listarAlquileres() {
        return alquileres.obtenerTodos();
    }

    /**
     * Obtiene únicamente los alquileres activos.
     *
     * @return lista de alquileres activos.
     */
    public List<Alquiler> listarAlquileresActivos() {

        List<Alquiler> activos =
                new ArrayList<>();

        for (Alquiler alquiler :
                alquileres.obtenerTodos()) {

            if (!alquiler.isFinalizado()) {
                activos.add(alquiler);
            }
        }

        return activos;
    }

    /**
     * Obtiene únicamente los alquileres finalizados.
     *
     * @return lista de alquileres finalizados.
     */
    public List<Alquiler> listarAlquileresFinalizados() {

        List<Alquiler> finalizados =
                new ArrayList<>();

        for (Alquiler alquiler :
                alquileres.obtenerTodos()) {

            if (alquiler.isFinalizado()) {
                finalizados.add(alquiler);
            }
        }

        return finalizados;
    }

    /**
     * Busca un alquiler por su identificador.
     *
     * @param id identificador del alquiler.
     * @return alquiler encontrado o null.
     */
    public Alquiler buscarPorId(int id) {

        for (Alquiler alquiler :
                alquileres.obtenerTodos()) {

            if (alquiler.getId() == id) {
                return alquiler;
            }
        }

        return null;
    }

    /**
     * Valida que un alquiler exista.
     *
     * @param alquiler alquiler que se desea validar.
     */
    private void validarAlquiler(
            Alquiler alquiler) {

        if (alquiler == null) {
            throw new IllegalArgumentException(
                    "Debe seleccionar un alquiler."
            );
        }
    }
}