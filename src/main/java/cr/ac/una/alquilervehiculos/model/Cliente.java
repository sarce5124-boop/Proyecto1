package cr.ac.una.alquilervehiculos.model;

public class Cliente {

    private String identificacion;
    private String nombre;
    private String telefono;
    private String correo;

    /**
     * Crea un cliente con sus datos principales.
     *
     * @param identificacion número de identificación del cliente.
     * @param nombre nombre completo del cliente.
     * @param telefono teléfono del cliente.
     * @param correo correo electrónico del cliente.
     */
    public Cliente(String identificacion, String nombre, String telefono, String correo) {
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
    }

    /**
     * Obtiene la identificación del cliente.
     *
     * @return identificación del cliente.
     */
    public String getIdentificacion() {
        return identificacion;
    }

    /**
     * Modifica la identificación del cliente.
     *
     * @param identificacion nueva identificación.
     */
    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    /**
     * Obtiene el nombre del cliente.
     *
     * @return nombre del cliente.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Modifica el nombre del cliente.
     *
     * @param nombre nuevo nombre.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el teléfono del cliente.
     *
     * @return teléfono del cliente.
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Modifica el teléfono del cliente.
     *
     * @param telefono nuevo teléfono.
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Obtiene el correo del cliente.
     *
     * @return correo del cliente.
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * Modifica el correo del cliente.
     *
     * @param correo nuevo correo.
     */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /**
     * Obtiene una representación del cliente en texto.
     *
     * @return identificación y nombre del cliente.
     */
    @Override
    public String toString() {
        return identificacion + " - " + nombre;
    }
}