package cr.ac.una.alquilervehiculos.service;

import cr.ac.una.alquilervehiculos.model.EstadoVehiculo;
import cr.ac.una.alquilervehiculos.model.Vehiculo;
import cr.ac.una.alquilervehiculos.repository.Repositorio;

import java.util.List;

public class VehiculoService {

    private Repositorio<Vehiculo> vehiculos;

    /**
     * Crea el servicio de vehículos.
     */
    public VehiculoService() {
        vehiculos = new Repositorio<>();
    }

    /**
     * Registra un vehículo nuevo.
     *
     * @param vehiculo vehículo que se desea registrar.
     */
    public void registrarVehiculo(Vehiculo vehiculo) {

        if (vehiculo == null) {
            throw new IllegalArgumentException(
                    "Debe ingresar un vehículo."
            );
        }

        if (vehiculo.getPlaca() == null
                || vehiculo.getPlaca().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "La placa es obligatoria."
            );
        }

        if (vehiculo.getMarca() == null
                || vehiculo.getMarca().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "La marca es obligatoria."
            );
        }

        if (vehiculo.getTarifaDiaria() <= 0) {
            throw new IllegalArgumentException(
                    "La tarifa diaria debe ser mayor a cero."
            );
        }

        if (buscarPorPlaca(vehiculo.getPlaca()) != null) {
            throw new IllegalArgumentException(
                    "Ya existe un vehículo con esa placa."
            );
        }

        vehiculos.agregar(vehiculo);
    }

    /**
     * Busca un vehículo por su placa.
     *
     * @param placa placa del vehículo que se desea buscar.
     * @return vehículo encontrado o null si no existe.
     */
    public Vehiculo buscarPorPlaca(String placa) {

        if (placa == null) {
            return null;
        }

        for (Vehiculo vehiculo : vehiculos.obtenerTodos()) {
            if (vehiculo.getPlaca().equalsIgnoreCase(placa)) {
                return vehiculo;
            }
        }

        return null;
    }

    /**
     * Obtiene todos los vehículos registrados.
     *
     * @return lista de vehículos.
     */
    public List<Vehiculo> listarVehiculos() {
        return vehiculos.obtenerTodos();
    }

    /**
     * Obtiene únicamente los vehículos disponibles.
     *
     * @return lista de vehículos disponibles.
     */
    public List<Vehiculo> listarDisponibles() {

        Repositorio<Vehiculo> disponibles =
                new Repositorio<>();

        for (Vehiculo vehiculo : vehiculos.obtenerTodos()) {

            if (vehiculo.getEstado()
                    == EstadoVehiculo.DISPONIBLE) {

                disponibles.agregar(vehiculo);
            }
        }

        return disponibles.obtenerTodos();
    }

    /**
     * Actualiza los datos generales de un vehículo.
     *
     * @param placa placa del vehículo que se desea actualizar.
     * @param marca nueva marca.
     * @param tarifaDiaria nueva tarifa diaria.
     * @return true si se actualizó, false si no existe.
     */
    public boolean actualizarVehiculo(
            String placa,
            String marca,
            double tarifaDiaria) {

        Vehiculo vehiculo = buscarPorPlaca(placa);

        if (vehiculo == null) {
            return false;
        }

        if (marca == null || marca.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "La marca es obligatoria."
            );
        }

        if (tarifaDiaria <= 0) {
            throw new IllegalArgumentException(
                    "La tarifa diaria debe ser mayor a cero."
            );
        }

        vehiculo.setMarca(marca);
        vehiculo.setTarifaDiaria(tarifaDiaria);

        return true;
    }

    /**
     * Cambia el estado de un vehículo.
     *
     * @param placa placa del vehículo.
     * @param estado nuevo estado del vehículo.
     * @return true si se cambió el estado, false si no existe.
     */
    public boolean cambiarEstado(
            String placa,
            EstadoVehiculo estado) {

        Vehiculo vehiculo = buscarPorPlaca(placa);

        if (vehiculo == null) {
            return false;
        }

        if (estado == null) {
            throw new IllegalArgumentException(
                    "Debe indicar un estado válido."
            );
        }

        vehiculo.setEstado(estado);

        return true;
    }

    /**
     * Elimina un vehículo según su placa.
     *
     * @param placa placa del vehículo que se desea eliminar.
     * @return true si se eliminó, false si no existe.
     */
    public boolean eliminarVehiculo(String placa) {

        Vehiculo vehiculo = buscarPorPlaca(placa);

        if (vehiculo == null) {
            return false;
        }

        if (vehiculo.getEstado()
                == EstadoVehiculo.ALQUILADO) {

            throw new IllegalStateException(
                    "No se puede eliminar un vehículo alquilado."
            );
        }

        return vehiculos.eliminar(vehiculo);
    }
}