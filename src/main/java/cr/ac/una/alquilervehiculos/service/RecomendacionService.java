package cr.ac.una.alquilervehiculos.service;

import cr.ac.una.alquilervehiculos.model.Bicimoto;
import cr.ac.una.alquilervehiculos.model.Cuadraciclo;
import cr.ac.una.alquilervehiculos.model.Moto;
import cr.ac.una.alquilervehiculos.model.Vehiculo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RecomendacionService {

    private VehiculoService vehiculoService;

    /**
     * Crea el servicio de recomendaciones.
     *
     * @param vehiculoService servicio que contiene los vehículos registrados.
     */
    public RecomendacionService(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    /**
     * Recomienda vehículos disponibles según la cantidad de días,
     * el presupuesto máximo y el tipo de vehículo solicitado.
     *
     * @param dias cantidad de días que desea alquilar el cliente.
     * @param presupuesto presupuesto máximo del cliente.
     * @param tipoVehiculo tipo deseado: MOTO, BICIMOTO, CUADRICICLO o TODOS.
     * @return lista de vehículos recomendados ordenados del más económico al más caro.
     */
    public List<Vehiculo> recomendarVehiculos(
            int dias,
            double presupuesto,
            String tipoVehiculo) {

        if (dias <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad de días debe ser mayor a cero."
            );
        }

        if (presupuesto <= 0) {
            throw new IllegalArgumentException(
                    "El presupuesto debe ser mayor a cero."
            );
        }

        if (tipoVehiculo == null
                || tipoVehiculo.trim().isEmpty()) {
            tipoVehiculo = "TODOS";
        }

        List<Vehiculo> recomendados = new ArrayList<>();

        for (Vehiculo vehiculo :
                vehiculoService.listarDisponibles()) {

            double costoAlquiler =
                    vehiculo.calcularCostoAlquiler(dias);

            if (costoAlquiler <= presupuesto
                    && cumpleTipo(vehiculo, tipoVehiculo)) {

                recomendados.add(vehiculo);
            }
        }

        recomendados.sort(
                Comparator.comparingDouble(
                        vehiculo ->
                                vehiculo.calcularCostoAlquiler(dias)
                )
        );

        return recomendados;
    }

    /**
     * Obtiene la mejor recomendación según el presupuesto indicado.
     * La mejor opción será el vehículo disponible más económico
     * que cumpla con los criterios del cliente.
     *
     * @param dias cantidad de días del alquiler.
     * @param presupuesto presupuesto máximo.
     * @param tipoVehiculo tipo de vehículo solicitado.
     * @return vehículo recomendado o null si no existe una opción.
     */
    public Vehiculo obtenerMejorRecomendacion(
            int dias,
            double presupuesto,
            String tipoVehiculo) {

        List<Vehiculo> recomendados =
                recomendarVehiculos(
                        dias,
                        presupuesto,
                        tipoVehiculo
                );

        if (recomendados.isEmpty()) {
            return null;
        }

        return recomendados.get(0);
    }

    /**
     * Calcula cuánto dinero le quedaría disponible al cliente
     * después de pagar el alquiler recomendado.
     *
     * @param vehiculo vehículo recomendado.
     * @param dias cantidad de días del alquiler.
     * @param presupuesto presupuesto del cliente.
     * @return dinero restante después del alquiler.
     */
    public double calcularDineroRestante(
            Vehiculo vehiculo,
            int dias,
            double presupuesto) {

        if (vehiculo == null) {
            throw new IllegalArgumentException(
                    "Debe indicar un vehículo."
            );
        }

        double costo =
                vehiculo.calcularCostoAlquiler(dias);

        return presupuesto - costo;
    }

    /**
     * Determina si un vehículo corresponde al tipo
     * solicitado por el cliente.
     *
     * @param vehiculo vehículo que se desea evaluar.
     * @param tipoVehiculo tipo solicitado.
     * @return true si cumple con el tipo solicitado.
     */
    private boolean cumpleTipo(
            Vehiculo vehiculo,
            String tipoVehiculo) {

        String tipo =
                tipoVehiculo.trim().toUpperCase();

        if (tipo.equals("TODOS")) {
            return true;
        }

        if (tipo.equals("MOTO")) {
            return vehiculo instanceof Moto;
        }

        if (tipo.equals("BICIMOTO")) {
            return vehiculo instanceof Bicimoto;
        }

        if (tipo.equals("CUADRICICLO")) {
            return vehiculo instanceof Cuadraciclo;
        }

        return false;
    }
}