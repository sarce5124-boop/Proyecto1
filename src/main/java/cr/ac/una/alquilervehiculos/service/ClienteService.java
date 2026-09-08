package cr.ac.una.alquilervehiculos.service;

import cr.ac.una.alquilervehiculos.model.Cliente;
import cr.ac.una.alquilervehiculos.repository.Repositorio;

import java.util.List;

public class ClienteService {

    private Repositorio<Cliente> clientes;

    /**
     * Crea el servicio de clientes.
     */
    public ClienteService() {
        clientes = new Repositorio<>();
    }

    /**
     * Registra un cliente nuevo.
     *
     * @param cliente cliente que se desea registrar.
     */
    public void registrarCliente(Cliente cliente) {

        if (cliente == null) {
            throw new IllegalArgumentException(
                    "Debe ingresar un cliente."
            );
        }

        if (cliente.getIdentificacion() == null
                || cliente.getIdentificacion().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "La identificación es obligatoria."
            );
        }

        if (cliente.getNombre() == null
                || cliente.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre es obligatorio."
            );
        }

        if (buscarPorIdentificacion(
                cliente.getIdentificacion()) != null) {
            throw new IllegalArgumentException(
                    "Ya existe un cliente con esa identificación."
            );
        }

        clientes.agregar(cliente);
    }

    /**
     * Busca un cliente por su identificación.
     *
     * @param identificacion identificación que se desea buscar.
     * @return cliente encontrado o null si no existe.
     */
    public Cliente buscarPorIdentificacion(String identificacion) {

        if (identificacion == null) {
            return null;
        }

        for (Cliente cliente : clientes.obtenerTodos()) {
            if (cliente.getIdentificacion()
                    .equalsIgnoreCase(identificacion)) {
                return cliente;
            }
        }

        return null;
    }

    /**
     * Obtiene todos los clientes registrados.
     *
     * @return lista de clientes.
     */
    public List<Cliente> listarClientes() {
        return clientes.obtenerTodos();
    }

    /**
     * Actualiza los datos de un cliente existente.
     *
     * @param identificacion identificación del cliente que se desea actualizar.
     * @param nombre nuevo nombre.
     * @param telefono nuevo teléfono.
     * @param correo nuevo correo.
     * @return true si se actualizó, false si no existe.
     */
    public boolean actualizarCliente(
            String identificacion,
            String nombre,
            String telefono,
            String correo) {

        Cliente cliente = buscarPorIdentificacion(identificacion);

        if (cliente == null) {
            return false;
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre es obligatorio."
            );
        }

        cliente.setNombre(nombre);
        cliente.setTelefono(telefono);
        cliente.setCorreo(correo);

        return true;
    }

    /**
     * Elimina un cliente según su identificación.
     *
     * @param identificacion identificación del cliente.
     * @return true si se eliminó, false si no existe.
     */
    public boolean eliminarCliente(String identificacion) {

        Cliente cliente = buscarPorIdentificacion(identificacion);

        if (cliente == null) {
            return false;
        }

        return clientes.eliminar(cliente);
    }
}